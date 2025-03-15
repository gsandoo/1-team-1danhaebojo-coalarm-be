package _1danhebojo.coalarm.coalarm_service.domain.dashboard.service;


import _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller.response.MacdDTO;
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
public class CoinMarketServiceImpl implements CoinMarketService {

    private final TickerTestRepository tickerTestRepository;

    public MacdDTO getMacdForSymbol(String symbol) {
        List<Double> prices = getClosingPrices(symbol);

        System.out.println("Closing Prices Count: " + prices.size());
        System.out.println("Prices: " + prices);

        if (prices.size() < 26) {
            throw new IllegalArgumentException("데이터 부족: MACD 계산을 위해 최소 26일 이상의 가격 데이터가 필요합니다.");
        }

        return calculateMACD(prices);
    }

    private List<Double> getClosingPrices(String symbol) {
        List<TickerTestEntity> tickers = tickerTestRepository.findByCodeOrderedByUtcDateTime(symbol);
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
}