package _1danhebojo.coalarm.coalarm_service.domain.alert.service;

import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.AlertHistoryRepositoryImpl;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.AlertSSERepositoryImpl;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.*;
import _1danhebojo.coalarm.coalarm_service.domain.alert.service.util.FormatUtil;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.TickerEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.time.Instant;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoldCrossAndTargetPriceService {
    private final AlertSSERepositoryImpl alertSSERepositoryImpl;
    private final AlertHistoryRepositoryImpl alertHistoryRepositoryImpl;

    // 알람 설정에 도달했는지 체크
    boolean isPriceReached(AlertEntity alert, TickerEntity ticker) {
        // 가격 지정가 알람 확인
        if (alert.getIsTargetPrice()) {
            return checkTargetPrice(alert, ticker);
        }

        // 골든 크로스 알람 확인
        else if (alert.getIsGoldenCross()) {
            return checkGoldenCross(alert, ticker);
        }

        return false;
    }

    private boolean checkTargetPrice(AlertEntity alert, TickerEntity tickerEntity) {
        TargetPriceEntity targetPrice = alert.getTargetPrice();
        if (targetPrice == null || tickerEntity == null) return false;

        BigDecimal targetPriceValue = targetPrice.getPrice();
        int percent = targetPrice.getPercentage();
        BigDecimal lastPrice = tickerEntity.getLast(); // ticker에서 최신 가격 가져오기

        if (lastPrice == null) return false;

        boolean targetPriceReached = false;

        // 퍼센트가 양수면 상승 → 가격이 목표 이상이면 도달
        if (percent > 0) {
            if (lastPrice.compareTo(targetPriceValue) <= 0) {
                targetPriceReached = true;
            }
        }

        // 퍼센트가 음수면 하락 → 가격이 목표 이하이면 도달
        else if (percent < 0) {
            if (lastPrice.compareTo(targetPriceValue) >= 0) {
                targetPriceReached = true;
            }
        }

        return targetPriceReached;
    }

    // 골든 크로스 가격 비교 확인 필요
    private boolean checkGoldenCross(AlertEntity alert, TickerEntity tickerEntity) {
        GoldenCrossEntity goldenCross = alert.getGoldenCross();

        if (goldenCross == null) return false;

        Instant startDate = Instant.now().minusSeconds(20 * 86400); // 최근 20일 데이터 조회
        String convertedMarket = FormatUtil.convertMarketFormat(alert.getCoin().getSymbol());

        List<TickerEntity> tickers = alertSSERepositoryImpl.findBySymbolAndDateRangeAndExchange(convertedMarket, startDate, "upbit");

        if (tickers.size() < 20) {
            return false; // 20일치 데이터가 부족하면 계산 불가능
        }

        // 날짜별 종가 평균을 계산
        Map<LocalDate, BigDecimal> dailyAverages = calculateDailyAverages(tickers);

        //최근 7일 데이터 조회
        List<BigDecimal> last7Days = dailyAverages.values().stream()
                .skip(Math.max(0, dailyAverages.size() - 7)) // 최근 7일만 가져옴
                .collect(Collectors.toList());

        // 최근 20일 데이터 조회
        List<BigDecimal> last20Days = new ArrayList<>(dailyAverages.values()); // 최근 20일 데이터

        // 단기(7일) 이동평균 계산
        BigDecimal shortMA = calculateMovingAverage(last7Days);

        // 장기(20일) 이동평균 계산
        BigDecimal longMA = calculateMovingAverage(last20Days);

        // 골든 크로스 발생 여부 (단기 > 장기)
        return shortMA.compareTo(longMA) > 0;
    }

    boolean isPriceStillValid(AlertEntity alert, Set<Long> recentAlertIdSet) {
        // recentAlertIdSet에 해당 alertId가 있으면 → 1분 내에 이미 알림 전송됨 → false
        return !recentAlertIdSet.contains(alert.getId());
    }

    // 이동 평균 계산
    private BigDecimal calculateMovingAverage(List<BigDecimal> tickers) {
        if (tickers.isEmpty()) return BigDecimal.ZERO;

        BigDecimal sum = tickers.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return sum.divide(BigDecimal.valueOf(tickers.size()), 2, RoundingMode.HALF_UP);
    }

    // 날짜별 종가 계산
    private Map<LocalDate, BigDecimal> calculateDailyAverages(List<TickerEntity> tickers) {
        Map<LocalDate, List<BigDecimal>> dailyPrices = new HashMap<>();

        for (TickerEntity ticker : tickers) {
            LocalDate date = Instant.ofEpochMilli(ticker.getId().getTimestamp().toEpochMilli())
                    .atZone(ZoneId.systemDefault()).toLocalDate();

            dailyPrices.computeIfAbsent(date, k -> new ArrayList<>()).add(ticker.getClose());
        }

        return dailyPrices.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                                .divide(BigDecimal.valueOf(entry.getValue().size()), BigDecimal.ROUND_HALF_UP)
                ));
    }
}
