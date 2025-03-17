package _1danhebojo.coalarm.coalarm_service.domain.dashboard.service;


import _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller.response.CoinIndicatorResponse;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller.response.MacdDTO;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller.response.RsiDTO;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.TickerTestRepository;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.TickerTestEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CoinIndicatorServiceImpl implements CoinIndicatorService {

    private final TickerTestRepository tickerTestRepository;

    public CoinIndicatorResponse getDashboardIndicators(Long coinId) {
        List<Double> prices = getClosingPrices(coinId);

        if (prices.size() < 26) {
            throw new IllegalArgumentException("데이터 부족: MACD 계산을 위해 최소 26개 이상의 가격 데이터가 필요합니다.");
        }

        MacdDTO macdData = calculateMACD(prices);
        RsiDTO rsiData = calculateRSI(prices, 14);

        return new CoinIndicatorResponse(macdData, rsiData);
    }

    private List<Double> getClosingPrices(Long coinId) {
        List<TickerTestEntity> tickers = tickerTestRepository.findByCoinIdOrderedByUtcDateTime(coinId);
        System.out.println("Fetched Data Count: " + tickers.size());
        System.out.println("Fetched Data: " + tickers);
        return tickers.stream()
                .map(ticker -> ticker.getTradePrice().doubleValue())
                .collect(Collectors.toList());
    }

    private MacdDTO calculateMACD(List<Double> prices) {
        Collections.reverse(prices);

        // 12일 EMA 계산
        List<Double> ema12 = calculateEMAList(prices, 12);

        // 26일 EMA 계산
        List<Double> ema26 = calculateEMAList(prices, 26);

        // MACD Line 계산 (12일 EMA - 26일 EMA)
        List<Double> macdLine = new ArrayList<>();
        for (int i = 0; i < ema12.size() && i < ema26.size(); i++) {
            macdLine.add(ema12.get(i) - ema26.get(i));
        }

        if (macdLine.size() < 9) {
            throw new IllegalArgumentException("MACD 데이터 부족: 최소 9개 이상의 MACD 데이터가 필요합니다.");
        }

        // Signal Line (MACD Line의 9일 EMA)
        List<Double> signalLine = calculateEMAList(macdLine, 9);

        // 최신 MACD 값
        double macd = macdLine.get(macdLine.size() - 1);
        double signal = signalLine.get(signalLine.size() - 1);
        double histogram = macd - signal;
        String trend = histogram > 0 ? "RISE" : "FALL";

        return new MacdDTO(macd, signal, histogram, trend);
    }

    private List<Double> calculateEMAList(List<Double> prices, int period) {
        List<Double> emaList = new ArrayList<>();

        // 첫 번째 EMA는 해당 기간의 SMA(단순 이동평균)로 계산
        double sma = 0;
        for (int i = 0; i < period; i++) {
            sma += prices.get(i);
        }
        sma /= period;

        // 첫 번째 EMA 값 추가
        emaList.add(sma);

        // EMA 계산 공식: EMA(today) = Price(today) * k + EMA(yesterday) * (1-k)
        // k = 2/(period+1)
        double multiplier = 2.0 / (period + 1);

        // 나머지 EMA 계산
        for (int i = period; i < prices.size(); i++) {
            double currentPrice = prices.get(i);
            double previousEma = emaList.get(emaList.size() - 1);
            double currentEma = (currentPrice * multiplier) + (previousEma * (1 - multiplier));
            emaList.add(currentEma);
        }

        return emaList;
    }

    private RsiDTO calculateRSI(List<Double> prices, int period) {
        Collections.reverse(prices);

        List<Double> gains = new ArrayList<>();
        List<Double> losses = new ArrayList<>();

        for (int i = 1; i < prices.size(); i++) {
            double change = prices.get(i) - prices.get(i - 1);
            if (change > 0) {
                gains.add(change);
                losses.add(0.0);
            } else {
                gains.add(0.0);
                losses.add(-change);
            }
        }

        double avgGain = gains.subList(0, period).stream().mapToDouble(Double::doubleValue).sum() / period;
        double avgLoss = losses.subList(0, period).stream().mapToDouble(Double::doubleValue).sum() / period;

        for (int i = period; i < gains.size(); i++) {
            avgGain = ((avgGain * (period - 1)) + gains.get(i)) / period;
            avgLoss = ((avgLoss * (period - 1)) + losses.get(i)) / period;
        }

        double rs = avgGain / (avgLoss == 0 ? 1 : avgLoss);
        double rsi = 100 - (100 / (1 + rs));

        return new RsiDTO(rsi);
    }
}