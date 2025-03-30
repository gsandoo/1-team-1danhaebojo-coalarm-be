package _1danhebojo.coalarm.coalarm_service.domain.dashboard.service;

import _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller.response.ResponseKimchiPremium;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.KimchiPremiumRepository;
import _1danhebojo.coalarm.coalarm_service.domain.coin.repository.entity.CoinEntity;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.KimchiPremiumEntity;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.TickerEntity;
import _1danhebojo.coalarm.coalarm_service.domain.coin.repository.jpa.CoinJpaRepository;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.jpa.KimchiPreminumJpaRepository;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.jpa.TickerJpaRepository;
import _1danhebojo.coalarm.coalarm_service.global.api.ApiException;
import _1danhebojo.coalarm.coalarm_service.global.api.AppHttpStatus;
import _1danhebojo.coalarm.coalarm_service.global.api.OffsetResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class KimchiPremiumServiceImpl implements KimchiPremiumService{
    private final KimchiPremiumRepository kimchiPremiumRepository;
    private final KimchiPreminumJpaRepository kimchiPreminumJpaRepository;
    private final TickerJpaRepository tickerJpaRepository;
    private final CoinJpaRepository coinJpaRepository;
    private static final String EXCHANGE_RATE_API_URL = "https://api.exchangerate-api.com/v4/latest/USD";
    private static final List<String> SUPPORTED_COINS = Arrays.asList("BTC", "ETH", "XRP");
    // 계산 시 사용할 스케일 상수 정의
    private static final int CALCULATION_SCALE = 16;
    private static final int DISPLAY_SCALE = 8;

    @Override
    public OffsetResponse<ResponseKimchiPremium> getKimchiPremiums(int offset, int limit) {
        List<ResponseKimchiPremium> premiums = kimchiPremiumRepository.findAllKimchiPremiums(offset, limit)
                .stream()
                .map(ResponseKimchiPremium::fromEntity)
                .toList();

        long totalElements = kimchiPremiumRepository.countAllKimchiPremiums();

        return OffsetResponse.of(
                premiums,
                offset,
                limit,
                totalElements
        );
    }

    @Override
    public void calculateAndSaveKimchiPremium() {
        // USD/KRW 환율 한 번만 가져오기
        BigDecimal exchangeRate = getUsdToKrwExchangeRate();
        if (exchangeRate.compareTo(BigDecimal.ZERO) == 0) {
            log.warn("환율 데이터를 가져올 수 없습니다.");
            throw new ApiException(AppHttpStatus.NOT_FOUND);
        }

        log.info("오늘의 USD/KRW 환율: {}", exchangeRate);

        // 모든 지원 코인에 대해 김치프리미엄 계산
        for (String coinSymbol : SUPPORTED_COINS) {
            try {
                calculateAndSaveKimchiPremiumForCoin(coinSymbol, exchangeRate);
            } catch (Exception e) {
                log.error("{} 코인의 김치 프리미엄 계산 중 오류 발생: {}", coinSymbol, e.getMessage());
                // 한 코인에서 오류가 발생해도 다른 코인은 계속 진행
            }
        }
    }

    private void calculateAndSaveKimchiPremiumForCoin(String coinSymbol, BigDecimal exchangeRate) {
        String krwQuoteSymbol = "KRW";
        String usdtQuoteSymbol ="USDT";

        Optional<TickerEntity> krwTicker = tickerJpaRepository.findFirstByIdBaseSymbolAndIdQuoteSymbolOrderByIdTimestampDesc(
                coinSymbol, krwQuoteSymbol);
        Optional<TickerEntity> usdtTicker = tickerJpaRepository.findFirstByIdBaseSymbolAndIdQuoteSymbolOrderByIdTimestampDesc(
                coinSymbol, usdtQuoteSymbol);

        if (krwTicker.isEmpty() || usdtTicker.isEmpty()) {
            log.warn("{}의 가격 데이터를 찾을 수 없습니다.", coinSymbol);
            throw new ApiException(AppHttpStatus.NOT_FOUND);
        }

        BigDecimal krwPrice = krwTicker.get().getClose();
        BigDecimal usdtPrice = usdtTicker.get().getClose();

        log.info("{} KRW 가격: {}", coinSymbol, krwPrice);
        log.info("{} USDT 가격: {}", coinSymbol, usdtPrice);

        // 김치 프리미엄 계산 (정확도를 위해 높은 스케일 사용)
        BigDecimal globalPriceInKrw = usdtPrice.multiply(exchangeRate).setScale(CALCULATION_SCALE, RoundingMode.HALF_UP);
        log.info("{} 글로벌 가격(KRW 환산): {}", coinSymbol, globalPriceInKrw);

        // 김치프리미엄 = ((한국가격 - 글로벌가격) / 글로벌가격) * 100
        BigDecimal priceDifference = krwPrice.subtract(globalPriceInKrw);
        BigDecimal kimchiPremium;

        if (globalPriceInKrw.compareTo(BigDecimal.ZERO) == 0) {
            // 분모가 0인 경우 방지
            kimchiPremium = BigDecimal.ZERO;
            log.warn("글로벌 가격이 0이라 김치프리미엄을 0으로 설정합니다.");
        } else {
            kimchiPremium = priceDifference
                    .divide(globalPriceInKrw, CALCULATION_SCALE, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(DISPLAY_SCALE, RoundingMode.HALF_UP);
        }

        log.info("{} 김치프리미엄 계산 결과: {}%", coinSymbol, kimchiPremium);

        // 코인 엔티티 가져오기
        Optional<CoinEntity> coinEntity = coinJpaRepository.findBySymbol(coinSymbol);
        if (coinEntity.isEmpty()) {
            log.warn("{} 코인 정보를 찾을 수 없습니다.", coinSymbol);
            throw new ApiException(AppHttpStatus.NOT_FOUND);
        }

        // 어제 자정 시간 계산
        LocalDateTime yesterday = LocalDate.now().minusDays(1).atStartOfDay();
        LocalDateTime today = LocalDate.now().atStartOfDay();

        // 어제의 마지막 김치 프리미엄 조회 (어제 자정 이후부터 오늘 자정 이전까지)
        Optional<KimchiPremiumEntity> yesterdayPremium = kimchiPremiumRepository
                .findTopByCoinAndRegDtBetweenOrderByRegDtDesc(coinEntity.get(), yesterday, today);

        // 일별 변동률 계산
        BigDecimal dailyChange = calculateDailyChange(kimchiPremium, yesterdayPremium);

        // 김치 프리미엄 엔티티 생성 및 저장
        KimchiPremiumEntity kimchiPremiumEntity = new KimchiPremiumEntity(
                coinEntity.get(),
                krwPrice,
                usdtPrice,
                exchangeRate,
                kimchiPremium,
                dailyChange
        );

        kimchiPreminumJpaRepository.save(kimchiPremiumEntity);
        log.info("{} 김치 프리미엄 저장 완료: {}%, 일별 변동률: {}%", coinSymbol, kimchiPremium, dailyChange);
    }

    private BigDecimal calculateDailyChange(BigDecimal currentValue, Optional<KimchiPremiumEntity> yesterdayPremium) {
        if (yesterdayPremium.isEmpty()) {
            return BigDecimal.ZERO; // 어제 데이터가 없으면 변동률 0
        }

        BigDecimal yesterdayValue = yesterdayPremium.get().getKimchiPremium();
        log.info("어제의 김치프리미엄: {}", yesterdayValue);

        // 변동률 계산: (오늘값 - 어제값) / |어제값| * 100
        // 분모가 0인 경우를 방지하기 위한 처리
        if (yesterdayValue.compareTo(BigDecimal.ZERO) == 0) {
            // 어제 값이 0인 경우 (변동률 계산 불가)
            return currentValue.compareTo(BigDecimal.ZERO) > 0 ?
                    new BigDecimal("100") : new BigDecimal("-100");
        }

        // 정확한 계산을 위해 높은 스케일 사용
        return currentValue.subtract(yesterdayValue)
                .divide(yesterdayValue.abs(), CALCULATION_SCALE, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(DISPLAY_SCALE, RoundingMode.HALF_UP);
    }

    private BigDecimal getUsdToKrwExchangeRate() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(EXCHANGE_RATE_API_URL, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);
            return BigDecimal.valueOf(jsonNode.get("rates").get("KRW").asDouble());
        } catch (Exception e) {
            log.error("환율 데이터를 가져오는 중 오류 발생: {}", e.getMessage());
            return BigDecimal.ZERO;
        }
    }
}