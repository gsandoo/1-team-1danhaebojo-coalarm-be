package _1danhebojo.coalarm.coalarm_service.domain.alert.service;

import _1danhebojo.coalarm.coalarm_service.domain.alert.controller.response.AlertSSEResponse;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.AlertHistoryRepository;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.AlertRepository;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.AlertSSERepository;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.AlertEntity;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.GoldenCrossEntity;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.TargetPriceEntity;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.TickerEntity;
import _1danhebojo.coalarm.coalarm_service.domain.user.repository.entity.UserEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertSSEService {
    private final AlertHistoryService alertHistoryService;
    private final AlertRepository alertRepository;
    private final AlertHistoryRepository alertHistoryRepository;
    private final AlertSSERepository alertSSERepository;
    private final DiscordService discordService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String UPBIT_MARKET_URL = "https://api.upbit.com/v1/market/all?is_details=true"; // 예제 URL

    private final Map<Boolean, List<String>> volumeDatas = new HashMap<>();
    private final Map<Long, List<SseEmitter>> userEmitters = new ConcurrentHashMap<>();
    private final Map<Long, List<AlertEntity>> activeAlertList = new ConcurrentHashMap<>();
    private final Map<Long, Queue<AlertEntity>> userAlertQueue = new ConcurrentHashMap<>();

    // 서버 시작 시 자동 실행 → 업비트에서 초기 데이터 가져오기
    @PostConstruct
    @Transactional(readOnly = true)
    public void init() {
        updateTradingVolumeData();
        getActiveAlertsGroupedByUser();
    }

    // <editor-fold desc="스케줄러 관련">
    // 매일 오전 10시에 실행 (cron 표현식: "0 0 10 * * *")
    @Scheduled(cron = "0 0 10 * * *")
    public void updateTradingVolume() {
        updateTradingVolumeData();
    }

    //3초마다 큐에 있는값을 전송
    @Scheduled(fixedRateString = "#{@alarmProperties.sendQueueInterval}")
    @Transactional(readOnly = true)
    public void sendAlertsSequentially() {
        userAlertQueue.forEach((userId, queue) -> {
            if (!queue.isEmpty()) {
                AlertEntity alert = queue.poll();
                sendAlertToUserSSE(userId, alert);
            }
        });
    }

    // 중간중간 전체 알람 상태 재로딩
    @Scheduled(fixedRateString = "#{@alarmProperties.refreshActive}") // 3분마다 실행
    @Transactional(readOnly = true)
    public void refreshActiveAlerts() {
        log.info("전체 알람 상태 재로딩 시작");
        getActiveAlertsGroupedByUser();
    }

    // SSE 연결 유지를 위한 heartbeat 이벤트 주기적 전송
    @Scheduled(fixedRateString = "#{@alarmProperties.sendHeartClient}") // 15초마다 실행
    public void sendHeartbeatToClients() {
        for (Map.Entry<Long, List<SseEmitter>> entry : userEmitters.entrySet()) {
            List<SseEmitter> failedEmitters = new ArrayList<>();
            Long userId = entry.getKey();
            List<SseEmitter> emitters = entry.getValue();

            if (emitters != null && !Objects.requireNonNull(emitters).isEmpty()) {
                for (SseEmitter emitter : emitters) {
                    try {
                        emitter.send(SseEmitter.event()
                                .name("heartbeat")
                                .data("keep-alive")); // 클라이언트에선 로그로만 찍어도 OK
                    } catch (IOException e) {
                        failedEmitters.add(emitter);
                    }
                }
            }

            for (SseEmitter failed : failedEmitters) {
                removeSingleEmitter(userId, failed);
            }
        }
    }

    // 특정 시간마다 디스코드 알림 전송
    @Scheduled(fixedRateString = "#{@alarmProperties.sendDiscordInterval}")
    public void discordScheduler() {
        log.info("디스코드 알림 전송 시작");
        // 조건에 맞는 알람만 필터링 (지정가 or 골든크로스)
        Map<Long, List<AlertEntity>> filteredAlerts = activeAlertList.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .filter(alert -> alert.getIsTargetPrice() || alert.getIsGoldenCross())
                                .collect(Collectors.toList())
                ));

        // 전체 심볼 수집 → 가격 데이터 로딩
        List<String> allSymbols = allSymbols(filteredAlerts);

        // 티커 정보 조회
        List<TickerEntity> tickerList = alertRepository.findLatestTickersBySymbolList(allSymbols);

        // 유저별 조건 체크 및 디스코드 전송
        filteredAlerts.forEach((userId, alertList) -> {
            List<AlertEntity> triggeredAlerts = new ArrayList<>();

            for (AlertEntity alert : alertList) {
                String symbol = alert.getCoin().getSymbol();

                TickerEntity ticker = tickerList.stream()
                        .filter(t -> t.getId().getBaseSymbol().equals(symbol))
                        .findFirst()
                        .orElse(null);

                if (ticker != null && isPriceReached(alert, ticker)) {
                    triggeredAlerts.add(alert);
                }
            }

            if (!triggeredAlerts.isEmpty()) {
                sendAlertListToUserDiscord(userId, triggeredAlerts);
            }
        });
        log.info("디스코드 알림 종료");
    }

    // 특정 시간마다 긁어와서 queue에 추가
    @Scheduled(fixedRateString = "#{@alarmProperties.sendSubscription}")
    @Transactional(readOnly = true)
    public void checkAlertsForSubscribedUsers() {
        try {
            checkUserAlert();
        } catch (Exception e) {
            log.error("알림 체크 중 에러 발생", e);
        }
    }
    // </editor-fold">

    // 특정 시간마다 가격 비교해서 보낼 알람 체크 (티커 체크 + 히스토리 체크 + 조건 도달 체크)
    public void checkUserAlert(){
        // 티커 테이블에서 코인의 최신 값을 한번에 불러와서 조회 후 비교
        List<String> allSymbols = allSymbols(activeAlertList);
        List<TickerEntity> tickerList = alertRepository.findLatestTickersBySymbolList(allSymbols);

        // 최근 알람 히스토리를 한 번에 조회
        LocalDateTime minutesAgo = LocalDateTime.now().minusSeconds(30);
        List<Long> recentAlertIds = alertHistoryRepository.findRecentHistories(minutesAgo);
        Set<Long> recentAlertIdSet = new HashSet<>(recentAlertIds);

        // SSE 알람 전송
        for (Long userId : userEmitters.keySet()) {
            List<AlertEntity> activeAlerts = new ArrayList<>(activeAlertList.getOrDefault(userId, Collections.emptyList()));

            // 유효성 추가
            if (activeAlerts == null || activeAlerts.isEmpty()) continue;

            // 활성화된 알람 SSE로 보내기
            for (AlertEntity alert : activeAlerts) {
                String symbol = alert.getCoin().getSymbol();

                TickerEntity ticker = tickerList.stream()
                        .filter(t -> t.getId().getBaseSymbol().equals(symbol))
                        .findFirst()
                        .orElse(null);
                if (ticker != null) {
                    // 알람 도달 조건 체크
                    if (isPriceReached(alert, ticker)) {
                        // 알람 히스토리 존재 여부 체크
                        if (!recentAlertIdSet.contains(alert.getId())){
                            insertUserAlertQueue(userId, alert);
                        }
                    }
                }
            }
        }
    }

    // 사용자에게 보낼 알람 Queue에 추가
    public void insertUserAlertQueue(Long userId, AlertEntity alert) {
        Queue<AlertEntity> queue = userAlertQueue.computeIfAbsent(userId, k -> new ConcurrentLinkedQueue<>());

        // 이미 보냈던 애를 중복처리
        boolean alreadyQueued = queue.stream()
                .anyMatch(a -> a.getId().equals(alert.getId()));
        if (!alreadyQueued) {
            queue.add(alert);
        }
    }

    // 로그인한 사용자가 실행 SSE 전송 요청
    public SseEmitter subscribe(Long userId) {
        if(userId == null) { return null;}
        // 이미 존재하는 emitter가 있으면 재사용
        List<SseEmitter> existingEmitters = userEmitters.get(userId);
        if (existingEmitters != null && !Objects.requireNonNull(existingEmitters).isEmpty()) {
            // 살아있는 emitter만 필터링
            List<SseEmitter> failedEmitters = new ArrayList<>();

            for (SseEmitter emitter : existingEmitters) {
                try {
                    emitter.send(SseEmitter.event().name("ping").data("alive-check"));
                    return emitter;
                } catch (IOException e) {
                    failedEmitters.add(emitter);
                }
            }

            for (SseEmitter failed : failedEmitters) {
                removeSingleEmitter(userId, failed);
            }
        }

        // 새 emitter 생성
        SseEmitter emitter = new SseEmitter(0L);
        userEmitters.computeIfAbsent(userId, k -> new ArrayList<>()).add(emitter);

        // emitter 정리 로직
        emitter.onCompletion(() -> removeEmitter(userId));
        emitter.onTimeout(() -> removeEmitter(userId));
        emitter.onError((e) -> removeEmitter(userId));

        return emitter;
    }

    // 전체 활성화된 사용자의 알람 저장
    @Transactional(readOnly = true)
    public void getActiveAlertsGroupedByUser() {
        List<AlertEntity> activeAlerts = alertRepository.findAllActiveAlerts();

        // userId를 key로, List<Alert>을 value로 하는 Map 생성
        activeAlertList.clear(); // 기존 데이터 삭제
        activeAlertList.putAll(
                activeAlerts.stream()
                        .collect(Collectors.groupingBy(alert -> alert.getUser().getId()))
        );
    }

    // 알람을 보낸 뒤 히스토리 저장 (해당 함수의 경우 비동기로 전송)
    @Async
    public void saveAlertHistoryAsync(Long alertId, Long userId) {
        alertHistoryService.addAlertHistory(alertId, userId);
    }

    // 사용자의 기존 알람 SSE 전송
    @Transactional
    public void sendAlertToUserSSE(Long userId, AlertEntity alert) {
        List<SseEmitter> emitters = userEmitters.get(userId);

        if (emitters != null && !Objects.requireNonNull(emitters).isEmpty()) {
            AlertSSEResponse response = new AlertSSEResponse(alert);
            List<SseEmitter> failedEmitters = new ArrayList<>();

            for (SseEmitter emitter : emitters) {
                try {
                    emitter.send(SseEmitter.event()
                            .name("alert")
                            .data(response));
                } catch (Exception e) {
                    // 예외가 발생한 Emitter는 제거할 목록에 추가
                    failedEmitters.add(emitter);
                }
            }

            for (SseEmitter failed : failedEmitters) {
                removeSingleEmitter(userId, failed);
            }
        }

        // 알람 히스토리 저장
        saveAlertHistoryAsync(alert.getId(), userId);
    }

    // 단일 알람 디스코드 전송
    public void sendAlertToUserDiscord(Long userId, AlertEntity alert) {
        if (alert == null || alert.getUser() == null || alert.getUser().getDiscordWebhook() == null) return;

        List<Map<String, Object>> embeds = List.of(discordService.buildEmbedMapFromAlert(alert));
        discordService.sendDiscordEmbed(alert.getUser().getDiscordWebhook(), embeds);
    }

    // 리스트 알람 디스코드 전송
    public void sendAlertListToUserDiscord(Long userId, List<AlertEntity> alerts) {
        if (alerts.isEmpty()) return;

        AlertEntity firstAlert = alerts.get(0);
        UserEntity user = firstAlert.getUser();
        if (user == null || user.getDiscordWebhook() == null || user.getDiscordWebhook().isEmpty()) return;

        List<Map<String, Object>> embeds = alerts.stream()
                .map(discordService::buildEmbedMapFromAlert)
                .collect(Collectors.toList());

        discordService.sendDiscordEmbed(user.getDiscordWebhook(), embeds);
    }

    // 새로운 알람 추가
    public void addEmitter(Long userId, AlertEntity alert) {
        SseEmitter emitter = new SseEmitter(0L);
        // 내부 동작
        userEmitters.computeIfAbsent(userId, k -> Collections.synchronizedList(new ArrayList<>())).add(emitter);
        activeAlertList.computeIfAbsent(userId, k -> Collections.synchronizedList(new ArrayList<>())).add(alert);

        emitter.onCompletion(() -> removeEmitter(userId));
        emitter.onTimeout(() -> removeEmitter(userId));
        emitter.onError((e) -> removeEmitter(userId));

        log.info("📢 사용자 " + userId + " 에 대한 새로운 SSE 구독 추가됨. 활성화된 알람 개수: " + activeAlertList.get(userId).size());
    }

    // SSE 알람 제거
    public void deleteEmitter(Long userId, AlertEntity alert) {
        // 사용자의 알람 리스트에서 해당 알람 제거
        activeAlertList.computeIfPresent(userId, (k, alerts) -> {
            alerts.removeIf(a -> a.getId().equals(alert.getId())); // ✅ alertId가 동일한 경우만 삭제
            return alerts.isEmpty() ? null : alerts; // 리스트가 비면 null 반환해서 Map에서 삭제
        });

        log.info("사용자 " + userId + " 의 알람 제거됨. 남은 알람 개수: "
                + (activeAlertList.containsKey(userId) ? activeAlertList.get(userId).size() : 0));
    }

    // SSE 구독 취소
    public void removeEmitter(Long userId) {
        List<SseEmitter> emitters = userEmitters.remove(userId); // 해당 userId의 모든 SSE 제거

        if (emitters != null && !Objects.requireNonNull(emitters).isEmpty()) {
            for (SseEmitter emitter : emitters) {
                try {
                    emitter.complete(); // 안전하게 종료
                } catch (Exception e) {
                    log.warn("emitter 종료 중 예외 발생: {}", e.getMessage());
                }
            }
        }
        log.info("사용자 " + userId + " 의 모든 SSE 구독 취소 완료");
    }

    // userEmitters에서 사용자 제거
    public void removeSingleEmitter(Long userId, SseEmitter emitter) {
        List<SseEmitter> emitters = userEmitters.get(userId);
        if (emitters != null && !Objects.requireNonNull(emitters).isEmpty()) {
            emitters.remove(emitter);
            try {
                emitter.complete();
            } catch (Exception ignored) {}

            if (emitters.isEmpty()) {
                userEmitters.remove(userId);
            }
        }
    }

    // 닉네임 변경 시 알람 정보에 업데이트
    public void updateUserNicknameInAlerts(Long userId, String newNickname) {
        // 1. activeAlertList 내 수정
        List<AlertEntity> alerts = activeAlertList.get(userId);
        if (alerts != null) {
            for (AlertEntity alert : alerts) {
                if (alert.getUser() != null) {
                    alert.getUser().updateNickname(newNickname);
                }
            }
        }

        // 2. userAlertQueue 내 수정
        Queue<AlertEntity> alertQueue = userAlertQueue.get(userId);
        if (alertQueue != null) {
            for (AlertEntity alert : alertQueue) {
                if (alert.getUser() != null) {
                    alert.getUser().updateNickname(newNickname);
                }
            }
        }
    }

    // 웹훅 변경 시 알람 정보에 업데이트
    public void updateUserWebhookInAlerts(Long userId, String newWebhook) {
        // 1. activeAlertList 내 수정
        List<AlertEntity> alerts = activeAlertList.get(userId);
        if (alerts != null) {
            for (AlertEntity alert : alerts) {
                if (alert.getUser() != null) {
                    alert.getUser().updateDiscordWebhook(newWebhook);
                }
            }
        }

        // 2. userAlertQueue 내 수정
        Queue<AlertEntity> alertQueue = userAlertQueue.get(userId);
        if (alertQueue != null) {
            for (AlertEntity alert : alertQueue) {
                if (alert.getUser() != null) {
                    alert.getUser().updateDiscordWebhook(newWebhook);
                }
            }
        }
    }

    // 알람 설정에 도달했는지 체크
    private boolean isPriceReached(AlertEntity alert, TickerEntity ticker) {
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

    // 지정가 체크
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
            if (lastPrice.compareTo(targetPriceValue) >= 0) {
                targetPriceReached = true;
            }
        }

        // 퍼센트가 음수면 하락 → 가격이 목표 이하이면 도달
        else if (percent < 0) {
            // lastPrice > targetPriceValue : 1
            if (lastPrice.compareTo(targetPriceValue) <= 0) {
                targetPriceReached = true;
            }
        }

        return targetPriceReached;
    }

    // 골든 크로스 가격 비교
    private boolean checkGoldenCross(AlertEntity alert, TickerEntity tickerEntity) {
        GoldenCrossEntity goldenCross = alert.getGoldenCross();

        if (goldenCross == null) return false;

        Instant startDate = Instant.now().minusSeconds(20 * 86400); // 최근 20일 데이터 조회
        String baseSymbol = alert.getCoin().getSymbol();

        List<TickerEntity> tickers = alertSSERepository.findBySymbolAndDateRangeAndExchange(baseSymbol, startDate, "upbit", "KRW");

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
                                .divide(BigDecimal.valueOf(entry.getValue().size()), RoundingMode.HALF_UP)
                ));
    }

    // TRADING VOLUME SOARING 체크
    private void updateTradingVolumeData() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(UPBIT_MARKET_URL, String.class);
            JsonNode root = objectMapper.readTree(response.getBody());

            List<String> list = new ArrayList<>();
            for (JsonNode market : root) {
                JsonNode marketEvent = market.get("market_event");
                if (marketEvent != null && marketEvent.has("caution")) {
                    JsonNode caution = marketEvent.get("caution");
                    if (caution.has("TRADING_VOLUME_SOARING")){ //&& caution.get("TRADING_VOLUME_SOARING").asBoolean()) {
                        boolean TRADING_VOLUME_SOARING = Boolean.parseBoolean(caution.get("TRADING_VOLUME_SOARING").asText());
                        if(TRADING_VOLUME_SOARING) {
                            String originalMarket = market.get("market").asText();
                            String[] parts = originalMarket.split("-");

                            String convertedMarket = parts[1] + "/" + parts[0];

                            list.add(convertedMarket);
                        }
                    }
                }
            }

            // 메모리에 저장
            volumeDatas.put(true, list);

            sendVolumeToUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 특정 코인(Symbol)이 거래량 급등 리스트에 있는지 체크
    private boolean hasVolumeSpike(String symbol) {
        List<String> CoinList = volumeDatas.get(true);

        if (CoinList == null) {
            return false;
        }
        return CoinList.contains(symbol);
}

    // 전체 사용자에게 거래량 급등 알림 전송
    private void sendVolumeToUser() {
        List<AlertEntity> volumeSpikeAlerts = alertSSERepository.findAllVolumeSpikeAlertByStatus();
        if (!volumeSpikeAlerts.isEmpty()) {
            for (AlertEntity alert : volumeSpikeAlerts) {
                String symbol = alert.getCoin().getSymbol() + "/KRW";
                boolean tradingVolume = hasVolumeSpike(symbol);

                if (tradingVolume) {
                    insertUserAlertQueue(alert.getUser().getId(), alert);
                    sendAlertToUserDiscord(alert.getUser().getId(), alert);
                }
            }
        }
    }

    // 포함된 심볼 필터링
    private List<String> allSymbols(Map<Long, List<AlertEntity>> filteredAlerts) {
        return filteredAlerts.values().stream()
                .flatMap(List::stream)
                .map(alert -> alert.getCoin().getSymbol())
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }
}

