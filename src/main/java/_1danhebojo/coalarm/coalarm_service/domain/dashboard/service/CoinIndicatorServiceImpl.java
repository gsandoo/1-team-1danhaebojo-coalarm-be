package _1danhebojo.coalarm.coalarm_service.domain.dashboard.service;

import _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller.response.*;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.CandleRepository;
import _1danhebojo.coalarm.coalarm_service.domain.coin.repository.entity.CoinEntity;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.CandleEntity;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.CoinIndicatorEntity;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.jpa.CoinIndicatorJpaRepository;
import _1danhebojo.coalarm.coalarm_service.domain.coin.repository.jpa.CoinJpaRepository;
import _1danhebojo.coalarm.coalarm_service.global.api.ApiException;
import _1danhebojo.coalarm.coalarm_service.global.api.AppHttpStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CoinIndicatorServiceImpl implements CoinIndicatorService {

    private final CoinJpaRepository coinJpaRepository;
    private final CoinIndicatorJpaRepository coinIndicatorJpaRepository;
    private final CandleRepository candleRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String BINANCE_BASE_URL = "https://fapi.binance.com";

    public CoinIndicatorResponse getDashboardIndicators(String symbol) {
        Optional<CoinEntity> coinEntity = coinJpaRepository.findBySymbol(symbol);
        if(coinEntity.isEmpty()) throw new ApiException(AppHttpStatus.NOT_FOUND);
        CoinEntity coin = coinEntity.get();

        Optional<CoinIndicatorEntity> latestIndicator = coinIndicatorJpaRepository.findTopByCoinIdOrderByRegDtDesc(coin.getId());
        if(latestIndicator.isEmpty()) throw new ApiException(AppHttpStatus.NOT_FOUND);
        CoinIndicatorEntity indicator = latestIndicator.get();

        CoinDTO coinDTO = new CoinDTO(coin);
        MacdDTO macdDTO = new MacdDTO(
                indicator.getMacd(),
                indicator.getSignal(),
                indicator.getHistogram(),
                indicator.getTrend()
        );
        RsiDTO rsiDTO = new RsiDTO(indicator.getRsi());

        // longStrength를 사용하여 롱/숏 비율 계산
        BigDecimal longStrength = indicator.getLongStrength();

        // longStrength가 null인 경우 기본값 처리
        if (longStrength == null) {
            longStrength = BigDecimal.valueOf(0.5);  // 기본값 50:50
        }

        // longStrength를 0과 1 사이의 값으로 변환 (0.5가 50:50 비율)
        BigDecimal normalizedStrength;

        if (longStrength.compareTo(BigDecimal.ZERO) < 0) {
            // 음수인 경우 0.0 ~ 0.5 범위로 변환 (숏 우세)
            normalizedStrength = BigDecimal.valueOf(0.5).multiply(
                    BigDecimal.ONE.add(longStrength.abs().min(BigDecimal.ONE))
            );
        } else if (longStrength.compareTo(BigDecimal.ZERO) > 0) {
            // 양수인 경우 0.5 ~ 1.0 범위로 변환 (롱 우세)
            normalizedStrength = BigDecimal.valueOf(0.5).add(
                    BigDecimal.valueOf(0.5).multiply(longStrength.min(BigDecimal.ONE))
            );
        } else {
            // 0인 경우 0.5 (50:50)
            normalizedStrength = BigDecimal.valueOf(0.5);
        }

        // 롱/숏 비율 계산 (백분율로 표현)
        BigDecimal longRatio = normalizedStrength.multiply(BigDecimal.valueOf(100));
        BigDecimal shortRatio = BigDecimal.valueOf(100).subtract(longRatio);

        // 소수점 2자리까지 반올림
        longRatio = longRatio.setScale(2, RoundingMode.HALF_UP);
        shortRatio = shortRatio.setScale(2, RoundingMode.HALF_UP);

        LongShortStrengthDTO longShortStrengthDTO = new LongShortStrengthDTO(longRatio, shortRatio);

        return new CoinIndicatorResponse(macdDTO, rsiDTO, longShortStrengthDTO, coinDTO);
    }

    private List<BigDecimal> getClosingPricesMinutes(String symbol) {
        List<CandleEntity> candles = candleRepository.findRecentCandles(symbol, 200);
        List<BigDecimal> prices = candles.stream()
                .map(CandleEntity::getClose)
                .collect(Collectors.toList());
        Collections.reverse(prices); // 최신순 → 오래된 순으로 변경
        return prices;
    }

    private List<BigDecimal> getClosingPricesDay(String symbol){
        List<CandleEntity> candles = candleRepository.findDailyCandles(symbol,35);
        List<BigDecimal> prices =  candles.stream()
                .map(CandleEntity::getClose)
                .collect(Collectors.toList());
        Collections.reverse(prices);
        return prices;
    }

    private MacdDTO calculateMACD(List<BigDecimal> prices) {
        // 가격 데이터 정규화
        List<BigDecimal> normalizedPrices = prices.stream()
                .map(price -> price.divide(BigDecimal.valueOf(10), 8, RoundingMode.HALF_UP))
                .toList();

        // 12일 EMA 계산
        List<BigDecimal> ema12 = calculateEMAList(normalizedPrices, 12);

        // 26일 EMA 계산
        List<BigDecimal> ema26 = calculateEMAList(normalizedPrices, 26);

        // MACD Line 계산 (12일 EMA - 26일 EMA)
        List<BigDecimal> macdLine = new ArrayList<>();
        for (int i = 0; i < ema12.size() && i < ema26.size(); i++) {
            macdLine.add(ema12.get(i).subtract(ema26.get(i)));
        }

        if (macdLine.size() < 9) {
            throw new ApiException(AppHttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Signal Line (MACD Line의 9일 EMA)
        List<BigDecimal> signalLine = calculateEMAList(macdLine, 9);

        // 최신 MACD 값
        BigDecimal macd = macdLine.get(macdLine.size() - 1);
        BigDecimal signal = signalLine.get(signalLine.size() - 1);
        BigDecimal histogram = macd.subtract(signal);
        String trend = histogram.compareTo(BigDecimal.ZERO) > 0 ? "RISE" : "FALL";

        return new MacdDTO(macd, signal, histogram, trend);
    }

    private List<BigDecimal> calculateEMAList(List<BigDecimal> prices, int period) {
        List<BigDecimal> emaList = new ArrayList<>();

        // 첫 번째 EMA는 해당 기간의 SMA(단순 이동평균)로 계산
        BigDecimal sma = BigDecimal.ZERO;
        for (int i = 0; i < period; i++) {
            sma = sma.add(prices.get(i));
        }
        sma = sma.divide(BigDecimal.valueOf(period), 8, BigDecimal.ROUND_HALF_UP);

        // 첫 번째 EMA 값 추가
        emaList.add(sma);

        // EMA 계산 공식: EMA(today) = Price(today) * k + EMA(yesterday) * (1-k)
        // k = 2/(period+1)
        BigDecimal multiplier = BigDecimal.valueOf(2.0).divide(BigDecimal.valueOf(period + 1), 8, BigDecimal.ROUND_HALF_UP);
        BigDecimal oneMinusMultiplier = BigDecimal.ONE.subtract(multiplier);

        // 나머지 EMA 계산
        for (int i = period; i < prices.size(); i++) {
            BigDecimal currentPrice = prices.get(i);
            BigDecimal previousEma = emaList.get(emaList.size() - 1);
            BigDecimal currentEma = currentPrice.multiply(multiplier).add(previousEma.multiply(oneMinusMultiplier));
            emaList.add(currentEma);
        }

        return emaList;
    }

    private RsiDTO calculateRSI(List<BigDecimal> prices, int period) {
        Collections.reverse(prices);

        List<BigDecimal> gains = new ArrayList<>();
        List<BigDecimal> losses = new ArrayList<>();

        for (int i = 1; i < prices.size(); i++) {
            BigDecimal change = prices.get(i).subtract(prices.get(i - 1));
            if (change.compareTo(BigDecimal.ZERO) > 0) {
                gains.add(change);
                losses.add(BigDecimal.ZERO);
            } else {
                gains.add(BigDecimal.ZERO);
                losses.add(change.abs());
            }
        }

        BigDecimal avgGain = BigDecimal.ZERO;
        BigDecimal avgLoss = BigDecimal.ZERO;

        for (int i = 0; i < period; i++) {
            avgGain = avgGain.add(gains.get(i));
            avgLoss = avgLoss.add(losses.get(i));
        }

        avgGain = avgGain.divide(BigDecimal.valueOf(period), 8, BigDecimal.ROUND_HALF_UP);
        avgLoss = avgLoss.divide(BigDecimal.valueOf(period), 8, BigDecimal.ROUND_HALF_UP);

        for (int i = period; i < gains.size(); i++) {
            avgGain = (avgGain.multiply(BigDecimal.valueOf(period - 1)).add(gains.get(i)))
                    .divide(BigDecimal.valueOf(period), 8, BigDecimal.ROUND_HALF_UP);
            avgLoss = (avgLoss.multiply(BigDecimal.valueOf(period - 1)).add(losses.get(i)))
                    .divide(BigDecimal.valueOf(period), 8, BigDecimal.ROUND_HALF_UP);
        }

        BigDecimal rs;
        if (avgLoss.compareTo(BigDecimal.ZERO) == 0) {
            rs = BigDecimal.valueOf(100); // 손실이 없는 경우 큰 값 할당
        } else {
            rs = avgGain.divide(avgLoss, 8, BigDecimal.ROUND_HALF_UP);
        }

        BigDecimal rsi = BigDecimal.valueOf(100).subtract(
                BigDecimal.valueOf(100).divide(BigDecimal.ONE.add(rs), 8, BigDecimal.ROUND_HALF_UP)
        );

        return new RsiDTO(rsi);
    }

    private BigDecimal calculateLongStrength(String symbol) {
        try {
            // 1. 롱/숏 비율 데이터 가져오기
            JsonNode longShortData = getLongShortRatioData(symbol);
            if (longShortData == null || longShortData.isEmpty()) {
                log.error("롱/숏 비율 데이터를 가져오는데 실패했습니다.");
                return BigDecimal.valueOf(0); // 기본값 0 (롱/숏 비율 1:1)
            }

            JsonNode latestLsData = longShortData.get(0);
            BigDecimal longAccount = BigDecimal.valueOf(latestLsData.get("longAccount").asDouble());
            BigDecimal shortAccount = BigDecimal.valueOf(latestLsData.get("shortAccount").asDouble());

            // 2. 미결제약정 데이터 가져오기
            JsonNode openInterestData = getOpenInterestData(symbol);
            if (openInterestData == null) {
                log.error("미결제약정 데이터를 가져오는데 실패했습니다.");
                return BigDecimal.valueOf(0);
            }

            BigDecimal openInterest = BigDecimal.valueOf(openInterestData.get("openInterest").asDouble());

            // 3. 펀딩 비율 데이터 가져오기
            JsonNode fundingRateData = getFundingRateData(symbol);
            if (fundingRateData == null || fundingRateData.isEmpty()) {
                log.error("펀딩 비율 데이터를 가져오는데 실패했습니다.");
                return BigDecimal.valueOf(0);
            }

            JsonNode latestFrData = fundingRateData.get(0);
            BigDecimal fundingRate = BigDecimal.valueOf(latestFrData.get("fundingRate").asDouble());

            // 4. 롱 강도와 숏 강도 계산
            BigDecimal longStrength = longAccount.multiply(openInterest).multiply(BigDecimal.ONE.add(fundingRate));
            BigDecimal shortStrength = shortAccount.multiply(openInterest).multiply(BigDecimal.ONE.subtract(fundingRate));

            // 5. 롱/숏 비율 계산 (-1 ~ 1 사이의 값) - -1은 완전 숏, 0은 균형, 1은 완전 롱
            BigDecimal total = longStrength.add(shortStrength);
            if (total.compareTo(BigDecimal.ZERO) == 0) {
                return BigDecimal.ZERO; // 총 강도가 0인 경우 균형을 의미하는 0 반환
            }

            // 비율을 -1 ~ 1 사이의 값으로 정규화
            BigDecimal longRatio = longStrength.divide(total, 8, RoundingMode.HALF_UP);

            // longRatio - shortRatio 계산 (범위: -1 ~ 1)
            BigDecimal longStrengthValue = longRatio.multiply(BigDecimal.valueOf(2)).subtract(BigDecimal.ONE);

            // 값이 너무 극단적이지 않도록 제한
            longStrengthValue = longStrengthValue.max(BigDecimal.valueOf(-1)).min(BigDecimal.valueOf(1));

            return longStrengthValue;
        } catch (Exception e) {
            System.err.println("롱 강도 계산 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return BigDecimal.ZERO; // 오류 발생 시 0 반환
        }
    }

    public void saveIndicators(String symbol) {
        // 1. 코인 엔티티 조회
        CoinEntity coinEntity = coinJpaRepository.findBySymbol(symbol)
                .orElseThrow(() -> new ApiException(AppHttpStatus.NOT_FOUND));

        // 2. 가격 데이터 조회
        List<BigDecimal> pricesMinutes = getClosingPricesMinutes(symbol);
        List<BigDecimal> pricesDay = getClosingPricesDay(symbol);

        if (pricesDay.size() < 26 && pricesMinutes.size() <14) {
            throw new ApiException(AppHttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 3. MACD 계산
        MacdDTO macdDTO = calculateMACD(pricesDay);

        // 4. RSI 계산
        RsiDTO rsiDTO = calculateRSI(pricesMinutes, 14);

        // 5. 롱 강도 계산
        BigDecimal longStrength = calculateLongStrength("BTCUSDT");

        // 6. CoinIndicatorEntity 생성 및 저장
        CoinIndicatorEntity indicatorEntity = CoinIndicatorEntity.builder()
                .coin(coinEntity)
                .macd(macdDTO.getValue())
                .signal(macdDTO.getSignal())
                .histogram(macdDTO.getHistogram())
                .trend(macdDTO.getTrend())
                .rsi(rsiDTO.getValue())
                .longStrength(longStrength)
                .build();

        coinIndicatorJpaRepository.save(indicatorEntity);
    }


    // NOTE:새로 추가된 함수: 롱/숏 비율 데이터 가져오기
    private JsonNode getLongShortRatioData(String symbol) throws JsonProcessingException {
        String endpoint = "/futures/data/globalLongShortAccountRatio";
        String url = BINANCE_BASE_URL + endpoint + "?symbol=" + symbol + "&period=1h&limit=1";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return objectMapper.readTree(response.getBody());
        } else {
            System.err.println("롱/숏 비율 데이터 요청 실패: " + response.getStatusCode());
            return null;
        }
    }

    // NOTE:새로 추가된 함수: 미결제약정 데이터 가져오기
    private JsonNode getOpenInterestData(String symbol) throws JsonProcessingException {
        String endpoint = "/fapi/v1/openInterest";
        String url = BINANCE_BASE_URL + endpoint + "?symbol=" + symbol;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return objectMapper.readTree(response.getBody());
        } else {
            System.err.println("미결제약정 데이터 요청 실패: " + response.getStatusCode());
            return null;
        }
    }

    // NOTE:펀딩 비율 데이터 가져오기
    private JsonNode getFundingRateData(String symbol) throws JsonProcessingException {
        String endpoint = "/fapi/v1/fundingRate";
        String url = BINANCE_BASE_URL + endpoint + "?symbol=" + symbol + "&limit=1";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return objectMapper.readTree(response.getBody());
        } else {
            System.err.println("펀딩 비율 데이터 요청 실패: " + response.getStatusCode());
            return null;
        }
    }
}