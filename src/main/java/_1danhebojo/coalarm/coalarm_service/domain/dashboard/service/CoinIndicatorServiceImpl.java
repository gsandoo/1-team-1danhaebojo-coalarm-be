package _1danhebojo.coalarm.coalarm_service.domain.dashboard.service;


import _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller.response.*;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.TickerRepository;
import _1danhebojo.coalarm.coalarm_service.domain.coin.repository.entity.CoinEntity;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.CoinIndicatorEntity;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.TickerEntity;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.jpa.CoinIndicatorJpaRepository;
import _1danhebojo.coalarm.coalarm_service.domain.coin.repository.jpa.CoinJpaRepository;
import _1danhebojo.coalarm.coalarm_service.global.api.ApiException;
import _1danhebojo.coalarm.coalarm_service.global.api.AppHttpStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CoinIndicatorServiceImpl implements CoinIndicatorService {

    private final TickerRepository tickerRepository;
    private final CoinJpaRepository coinJpaRepository;
    private final CoinIndicatorJpaRepository coinIndicatorJpaRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String BINANCE_BASE_URL = "https://fapi.binance.com";

    public CoinIndicatorResponse getDashboardIndicators(Long coinId) {
        Optional<CoinIndicatorEntity> latestIndicator = coinIndicatorJpaRepository.findTopByCoinCoinIdOrderByCreatedAtDesc(coinId);
        Optional<CoinEntity> coinEntity = coinJpaRepository.findByCoinId(coinId);

        if(latestIndicator.isEmpty()) throw new ApiException(AppHttpStatus.NOT_FOUND);
        if(coinEntity.isEmpty()) throw new ApiException(AppHttpStatus.NOT_FOUND);

        CoinIndicatorEntity indicator = latestIndicator.get();
        CoinEntity coin = coinEntity.get();

        CoinDTO coinDTO = new CoinDTO(coin);
        MacdDTO macdDTO = new MacdDTO(
                indicator.getMacd(),
                indicator.getSignal(),
                indicator.getHistogram(),
                indicator.getTrend()
        );
        RsiDTO rsiDTO = new RsiDTO(indicator.getRsi());
        LongShortStrengthDTO longShortStrengthDTO = new LongShortStrengthDTO(indicator.getLongShortStrength(), BigDecimal.valueOf(1.0).subtract(indicator.getLongShortStrength()));

        return new CoinIndicatorResponse(macdDTO, rsiDTO, longShortStrengthDTO, coinDTO);
    }

    private List<BigDecimal> getClosingPrices(Long coinId) {
        List<TickerEntity> tickers = tickerRepository.findByCoinIdOrderedByUtcDateTime(coinId);
        return tickers.stream()
                .map(TickerEntity::getClose)
                .collect(Collectors.toList());
    }

    private MacdDTO calculateMACD(List<BigDecimal> prices) {
        Collections.reverse(prices);

        // 12일 EMA 계산
        List<BigDecimal> ema12 = calculateEMAList(prices, 12);

        // 26일 EMA 계산
        List<BigDecimal> ema26 = calculateEMAList(prices, 26);

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

    private LongShortStrengthDTO calculateLongShortStrength(String symbol) {
        try {
            // 1. 롱/숏 비율 데이터 가져오기
            JsonNode longShortData = getLongShortRatioData(symbol);
            if (longShortData == null || longShortData.isEmpty()) {
                System.out.println("롱/숏 비율 데이터를 가져오는데 실패했습니다.");
                return null;
            }

            JsonNode latestLsData = longShortData.get(0);
            BigDecimal longRatio = BigDecimal.valueOf(latestLsData.get("longAccount").asDouble());
            BigDecimal shortRatio = BigDecimal.valueOf(latestLsData.get("shortAccount").asDouble());

            // 2. 미결제약정 데이터 가져오기
            JsonNode openInterestData = getOpenInterestData(symbol);
            if (openInterestData == null) {
                System.out.println("미결제약정 데이터를 가져오는데 실패했습니다.");
                return null;
            }

            BigDecimal openInterest = BigDecimal.valueOf(openInterestData.get("openInterest").asDouble());

            // 3. 펀딩 비율 데이터 가져오기
            JsonNode fundingRateData = getFundingRateData(symbol);
            if (fundingRateData == null || fundingRateData.isEmpty()) {
                System.out.println("펀딩 비율 데이터를 가져오는데 실패했습니다.");
                return null;
            }

            JsonNode latestFrData = fundingRateData.get(0);
            BigDecimal fundingRate = BigDecimal.valueOf(latestFrData.get("fundingRate").asDouble());

            // 4. 공매수/공매도 강도 계산
            BigDecimal longStrength = longRatio.multiply(openInterest).multiply(BigDecimal.ONE.add(fundingRate));
            BigDecimal shortStrength = shortRatio.multiply(openInterest).multiply(BigDecimal.ONE.subtract(fundingRate));

            BigDecimal longShortStrength;
            if (shortStrength.compareTo(BigDecimal.ZERO) == 0) {
                longShortStrength = BigDecimal.valueOf(1000); // 무한대 대신 큰 값 사용
            } else {
                longShortStrength = longStrength.divide(shortStrength, 8, BigDecimal.ROUND_HALF_UP).subtract(BigDecimal.ONE);
            }

            String status = longShortStrength.compareTo(BigDecimal.ZERO) > 0 ? "LONG_DOMINANCE" : "SHORT_DOMINANCE";

            LongShortStrengthDTO result = LongShortStrengthDTO.builder()
                    .longRatio(longRatio.multiply(BigDecimal.valueOf(100))) // 퍼센트로 변환
                    .shortRatio(shortRatio.multiply(BigDecimal.valueOf(100))) // 퍼센트로 변환
                    .build();

            return result;
        } catch (Exception e) {
            System.err.println("공매수/공매도 강도 계산 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void saveIndicators(Long coinId) {
        // 1. 코인 엔티티 조회
        CoinEntity coinEntity = coinJpaRepository.findById(coinId)
                .orElseThrow(() -> new ApiException(AppHttpStatus.NOT_FOUND));

        // 2. 가격 데이터 조회
        List<BigDecimal> prices = getClosingPrices(coinId);

        if (prices.size() < 26) {
            throw new ApiException(AppHttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 3. MACD 계산
        MacdDTO macdDTO = calculateMACD(prices);

        // 4. RSI 계산
        RsiDTO rsiDTO = calculateRSI(prices, 14);

        // 5. 롱숏 강도 계산
        LongShortStrengthDTO longShortDTO = calculateLongShortStrength("BTCUSDT");

        // 6. CoinIndicatorEntity 생성 및 저장
        CoinIndicatorEntity indicatorEntity = CoinIndicatorEntity.builder()
                .coin(coinEntity)
                .macd(macdDTO.getValue())
                .signal(macdDTO.getSignal())
                .histogram(macdDTO.getHistogram())
                .trend(macdDTO.getTrend())
                .rsi(rsiDTO.getValue())
                .longShortStrength(longShortDTO != null
                        ? (longShortDTO.getLongRatio().subtract(longShortDTO.getShortRatio()))
                        .divide(BigDecimal.valueOf(100), 8, BigDecimal.ROUND_HALF_UP)
                        : null)
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