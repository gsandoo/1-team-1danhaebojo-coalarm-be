package _1danhebojo.coalarm.coalarm_service.domain.dashboard.service;

import _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller.response.ResponseKimchiPremium;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.KimchiPremiumRepository;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.TickerTestRepository;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.CoinEntity;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.KimchiPremiumEntity;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.TickerTestEntity;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.jpa.CoinJpaRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class KimchiPremiumServiceImpl implements KimchiPremiumService{
    private final KimchiPremiumRepository kimchiPremiumRepository;
    private final TickerTestRepository tickerTestRepository;
    private final CoinJpaRepository coinJpaRepository;
    private static final String EXCHANGE_RATE_API_URL = "https://api.exchangerate-api.com/v4/latest/USD";
    @Override
    public List<ResponseKimchiPremium> getKimchiPremiums(int offset, int limit) {
        return kimchiPremiumRepository.findAllKimchiPremiums(offset,limit)
                .stream()
                .map(ResponseKimchiPremium::fromEntity)
                .toList();
    }

    @Override
    public void calculateAndSaveKimchiPremium() {
        Optional<TickerTestEntity> krwBtc = tickerTestRepository.findLatestByCode("KRW-BTC");
        Optional<TickerTestEntity> usdtBtc = tickerTestRepository.findLatestByCode("USDT-BTC");

        if (krwBtc.isEmpty() || usdtBtc.isEmpty()) {
            log.warn("가격 데이터를 찾을 수 없습니다.");
            return;
        }

        BigDecimal krwPrice = krwBtc.get().getTradePrice();
        BigDecimal usdtPrice = usdtBtc.get().getTradePrice();

        BigDecimal exchangeRate = getUsdToKrwExchangeRate();
        if (exchangeRate.compareTo(BigDecimal.ZERO) == 0) {
            log.warn("환율 데이터를 가져올 수 없습니다.");
            return;
        }

        // 김치 프리미엄 계산
        BigDecimal globalPriceInKrw = usdtPrice.multiply(exchangeRate);

        BigDecimal kimchiPremium = krwPrice
                .divide(globalPriceInKrw, 8, BigDecimal.ROUND_HALF_UP) // (국내 가격 / 해외 가격(원화 환산))
                .subtract(BigDecimal.ONE) // -1
                .multiply(BigDecimal.valueOf(100)); // * 100


        // BTC 코인 엔티티 가져오기
        Optional<CoinEntity> btcCoin = coinJpaRepository.findBySymbol("BTC");
        if (btcCoin.isEmpty()) {
            log.warn("BTC 코인 정보를 찾을 수 없습니다.");
            return;
        }

        // 어제 자정 시간 계산
        LocalDateTime yesterday = LocalDate.now().minusDays(1).atStartOfDay();
        LocalDateTime today = LocalDate.now().atStartOfDay();

        // 어제의 마지막 김치 프리미엄 조회 (어제 자정 이후부터 오늘 자정 이전까지)
        Optional<KimchiPremiumEntity> yesterdayPremium = kimchiPremiumRepository
                .findTopByCoinAndRegDtBetweenOrderByRegDtDesc(btcCoin.get(), yesterday, today);

        // 일별 변동률 계산
        BigDecimal dailyChange = BigDecimal.ZERO; // 기본값

        if (yesterdayPremium.isPresent()) {
            BigDecimal yesterdayValue = yesterdayPremium.get().getKimchiPremium();

            // 변동률 계산: (오늘값 - 어제값) / |어제값| * 100
            // 분모가 0인 경우를 방지하기 위한 처리
            if (yesterdayValue.compareTo(BigDecimal.ZERO) != 0) {
                dailyChange = kimchiPremium.subtract(yesterdayValue)
                        .divide(yesterdayValue.abs(), 8, BigDecimal.ROUND_HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
            } else {
                // 어제 값이 0인 경우 (변동률 계산 불가)
                dailyChange = kimchiPremium.compareTo(BigDecimal.ZERO) > 0 ?
                        new BigDecimal("100") : new BigDecimal("-100");
            }
        }

        // 김치 프리미엄 엔티티 생성 및 저장
        KimchiPremiumEntity kimchiPremiumEntity = new KimchiPremiumEntity(
                btcCoin.get(),
                krwPrice,
                usdtPrice,
                exchangeRate.intValue(),
                kimchiPremium,
                dailyChange
        );

        kimchiPremiumRepository.saveKimchiPremium(kimchiPremiumEntity);
        log.info("김치 프리미엄 저장 완료: {}, 일별 변동률: {}%", kimchiPremium, dailyChange);
    }

    private BigDecimal getUsdToKrwExchangeRate() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(EXCHANGE_RATE_API_URL, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);
            return BigDecimal.valueOf(jsonNode.get("rates").get("KRW").asDouble());
        } catch (Exception e) {
            log.error("환율 데이터를 가져오는 중 오류 발생", e);
            return BigDecimal.ZERO;
        }
    }
}
