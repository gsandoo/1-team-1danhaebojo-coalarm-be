package _1danhebojo.coalarm.coalarm_service.domain.alert.service;

import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.AlertHistoryRepositoryImpl;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.AlertRepositoryImpl;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.Alert;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.stream.Collectors;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertSSEService {
    private final AlertHistoryService alertHistoryService;
    private final AlertRepositoryImpl alertRepositoryImpl;
    private final Map<Long, List<SseEmitter>> userEmitters = new ConcurrentHashMap<>();
    private final Map<Long, List<Alert>> activeAlertList = new ConcurrentHashMap<>();
    private final DiscordService discordService;

    @Lazy
    @Autowired
    private GoldCrossAndTargetPriceService goldCrossAndTargetPriceService;

    @PostConstruct
    public void init() {
        getActiveAlertsGroupedByUser();
    }

    // 전체 활성화된 사용자의 알람 저장
    public void getActiveAlertsGroupedByUser() {
        List<Alert> activeAlerts = alertRepositoryImpl.findAllActiveAlerts();

        // userId를 key로, List<Alert>을 value로 하는 Map 생성
        activeAlertList.clear(); // 기존 데이터 삭제
        activeAlertList.putAll(
                activeAlerts.stream()
                        .collect(Collectors.groupingBy(alert -> alert.getUser().getUserId()))
        );
    }
    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void discordScheduler() {
        Map<Long, List<Alert>> filteredAlerts = activeAlertList.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey, // 사용자 ID 유지 (key)
                        entry -> entry.getValue().stream() // value(알람 리스트) 필터링
                                .filter(alert -> alert.isTargetPrice() || alert.isGoldenCross())
                                .collect(Collectors.toList())
                ));
        filteredAlerts.forEach(this::sendAlertListToUserDiscord);
    }
    @Scheduled(fixedRate = 1000) // 1분마다 실행
    public void checkAlertsForSubscribedUsers() {
        for (Long userId : userEmitters.keySet()) {
            List<Alert> activeAlerts = activeAlertList.get(userId);

            // 활성화된 알람 SSE로 보내기
            for (Alert alert : activeAlerts) {
                if (goldCrossAndTargetPriceService.isPriceReached(alert) && goldCrossAndTargetPriceService.isPriceStillValid(alert)) {
                    sendAlertToUserSSE(userId, alert);
                }
            }
        }
    }

    // 맨처음에 SSE 구독 실행 : 활성화 되어있는 알람들 다 보낸다. 사용자가 받든지 말든지...
    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = new SseEmitter(0L);

        userEmitters.computeIfAbsent(userId, k -> new ArrayList<>()).add(emitter);

        // 사용자의 기존 알람을 전송
        sendUserAlerts(userId, emitter);

        // 연결 종료 시 emitter 제거
        emitter.onCompletion(() -> removeEmitter(userId));
        emitter.onTimeout(() -> removeEmitter(userId));
        emitter.onError((e) -> removeEmitter(userId));

        return emitter;
    }

    // 사용자의 기존 알람을 새로운 Emitter에게 전송
    public void sendUserAlerts(Long userId, SseEmitter emitter) {
        // TargetPrice랑 GoldenCross만 가져오기
        List<Alert> alerts = activeAlertList.get(userId)
                .stream()
                .filter(alert -> alert.isTargetPrice() || alert.isGoldenCross()) //
                .collect(Collectors.toList());
        try {
            if(alerts != null) {
                emitter.send(SseEmitter.event()
                        .name("existing-alerts") // 기존 알람 목록
                        .data(alerts) // 기존 알람 데이터를 리스트로 전송
                );

                for (Alert alert : alerts) {
                    alertHistoryService.addAlertHistory(alert.getAlertId(), userId); // 🔥 기존 Alert ID 활용
                }
            }
        } catch (IOException e) {
            removeEmitter(userId);
        }
    }

    // 사용자의 기존 알람 SSE 전송
    public void sendAlertToUserSSE(Long userId, Alert alert) {
        userEmitters.computeIfAbsent(userId, k -> new ArrayList<>());
        activeAlertList.get(userId);

        List<SseEmitter> emitters = userEmitters.get(userId);
        if (emitters == null || emitters.isEmpty()) {
            log.info("사용자 " + userId + " 에 대한 SSE 연결이 없습니다.");
            return;
        }

        List<SseEmitter> deadEmitters = new ArrayList<>();
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("alert").data(alert));
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        }

        emitters.removeAll(deadEmitters);
        // 알람 히스토리 저장
        alertHistoryService.addAlertHistory(alert.getAlertId(), Long.valueOf(userId));
    }

    // 사용자의 기존 알람 discord 전송
    public void sendAlertToUserDiscord(Long userId, Alert alert) {
        // 최종 메시지 생성
        if(alert == null) {
            return;
        }
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("👤 사용자 닉네임: ").append(alert.getUser().getNickname()).append("\n");
        messageBuilder.append("📢(코인) ").append(alert.getCoin().getName());
        messageBuilder.append(", (제목) " + alert.getTitle()).append("\n");
        discordService.sendDiscordAlert(alert.getUser().getDiscordWebhook(), messageBuilder.toString());
    }
    public void sendAlertListToUserDiscord(Long userId, List<Alert> alerts) {
        StringBuilder messageBuilder = new StringBuilder();

        if (alerts.isEmpty()) {
            return;
        }

        String nickname = alerts.get(0).getUser().getNickname();
        messageBuilder.append("👤 사용자 닉네임: ").append(alerts.get(0).getUser().getNickname()).append("\n");

        alerts.forEach(alert -> {
            messageBuilder.append("📢(코인) ").append(alert.getCoin().getSymbol())
                    .append(", (제목) ").append(alert.getTitle()).append("\n");
        });

        String message = messageBuilder.toString();

        // 디스코드로 메시지 전송
        discordService.sendDiscordAlert(alerts.get(0).getUser().getDiscordWebhook(), message);
    }
    // 새로운 알람 추가 -> 하는 부분은 이미 구현이 되어있고
    // 알림을 추가했을 때 SseEmitter에 추가하는 부분이 필요
    public void addEmitter(Long userId, Alert alert) {
        SseEmitter emitter = new SseEmitter(0L);
        userEmitters.computeIfAbsent(userId, k -> new ArrayList<>()).add(emitter);
        activeAlertList.computeIfAbsent(userId, k -> new ArrayList<>()).add(alert);

        emitter.onCompletion(() -> removeEmitter(userId));
        emitter.onTimeout(() -> removeEmitter(userId));
        emitter.onError((e) -> removeEmitter(userId));

        log.info("📢 사용자 " + userId + " 에 대한 새로운 SSE 구독 추가됨. 활성화된 알람 개수: " + activeAlertList.get(userId).size());
    }

    // SSE 구독 취소
    private void removeEmitter(Long userId) {
        List<SseEmitter> emitters = userEmitters.remove(userId); // 해당 userId의 모든 SSE 제거
        activeAlertList.remove(userId);
        if (emitters != null) {
            for (SseEmitter emitter : emitters) {
                emitter.complete(); // 모든 SSE 연결 강제 종료
            }
        }
        log.info("사용자 " + userId + " 의 모든 SSE 구독 취소 완료");
    }

}
