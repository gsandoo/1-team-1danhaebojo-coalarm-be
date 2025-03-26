package _1danhebojo.coalarm.coalarm_service.domain.alert.service;

import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.AlertHistoryRepositoryImpl;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.AlertSSERepositoryImpl;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.*;
import _1danhebojo.coalarm.coalarm_service.domain.alert.service.util.FormatUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.time.Instant;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoldCrossAndTargetPriceService {
    private final AlertSSERepositoryImpl alertSSERepositoryImpl;
    private final AlertHistoryRepositoryImpl alertHistoryRepositoryImpl;

    // 알람 설정에 도달했는지 체크
    boolean isPriceReached(Alert alert) {
        // 가격 지정가 알람 확인
        if (alert.isTargetPriceFlag()) {
            return checkTargetPrice(alert);
        }

        // 골든 크로스 알람 확인
        else if (alert.isGoldenCrossFlag()) {
            return checkGoldenCross(alert);
        }

        return false;
    }

    private boolean checkTargetPrice(Alert alert) {
        TargetPriceAlert targetPrice = alert.getTargetPrice();
        if (targetPrice == null) return false;

        BigDecimal price = BigDecimal.valueOf(targetPrice.getPrice());
        int percent = targetPrice.getPercentage();

        // 마지막 데이터 가져와서 비교.
        String convertedMarket = FormatUtil.convertMarketFormat(alert.getCoin().getSymbol());
        Optional<Ticker> tickers = alertSSERepositoryImpl.findLatestBySymbol(convertedMarket,"upbit");
        if (tickers.isEmpty()) return false;

        // 퍼센트가 음수면 하락지점이라서 그 값보다 작을 때로 비교
        boolean targetPriceReached = false;
        if(percent > 0){
            BigDecimal max = tickers.stream()
                    .map(Ticker::getLast)        // last 값만 추출
                    .max(BigDecimal::compareTo)  // 최대값 찾기
                    .orElse(BigDecimal.ZERO);    // 값이 없으면 0 반환
            if(max.compareTo(price) >= 0){
                targetPriceReached = true;
            }

        }
        // 퍼센트가 양수면 상승지점이라서 그 값보다 클 때로 비교
        else if(percent < 0){
            BigDecimal min = tickers.stream()
                    .map(Ticker::getLast)
                    .min(BigDecimal::compareTo)  // 최소값 찾기
                    .orElse(BigDecimal.ZERO);
            if(min.compareTo(price) <= 0){
                targetPriceReached = true;
            }
        }
        return targetPriceReached;
    }

    // 골든 크로스 가격 비교 확인 필요
    private boolean checkGoldenCross(Alert alert) {
        GoldenCrossAlert goldenCross = alert.getGoldenCross();
        if (goldenCross == null) return false;

        Instant startDate = Instant.now().minusSeconds(20 * 86400); // 최근 20일 데이터 조회
        String convertedMarket = FormatUtil.convertMarketFormat(alert.getCoin().getSymbol());

        List<Ticker> tickers = alertSSERepositoryImpl.findBySymbolAndDateRangeAndExchange(convertedMarket, startDate, "upbit");

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

    boolean isPriceStillValid(Alert alert) {
        // 1분 뒤에도 가격 유지 여부 확인
        LocalDateTime minutesAgo = LocalDateTime.now().minusMinutes(1);
        boolean recentHistory = alertHistoryRepositoryImpl.findRecentHistory(alert.getUser().getUserId(), alert.getAlertId(), minutesAgo);

        // 1분 내 동일한 알람이 있음 → 알람 전송 안함
        return !recentHistory;
    }

    // 이동 평균 계산
    private BigDecimal calculateMovingAverage(List<BigDecimal> tickers) {
        if (tickers.isEmpty()) return BigDecimal.ZERO;

        BigDecimal sum = tickers.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return sum.divide(BigDecimal.valueOf(tickers.size()), 2, RoundingMode.HALF_UP);
    }

    // 날짜별 종가 계산
    private Map<LocalDate, BigDecimal> calculateDailyAverages(List<Ticker> tickers) {
        Map<LocalDate, List<BigDecimal>> dailyPrices = new HashMap<>();

        for (Ticker ticker : tickers) {
            LocalDate date = Instant.ofEpochMilli(ticker.getTimestamp().toEpochMilli())
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
