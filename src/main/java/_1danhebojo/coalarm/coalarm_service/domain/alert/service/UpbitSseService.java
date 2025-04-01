package _1danhebojo.coalarm.coalarm_service.domain.alert.service;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.socket.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpbitSseService {

    private final Map<String, List<SseEmitter>> emitterMap = new ConcurrentHashMap<>();
    private WebSocketSession session;
    private final Set<String> subscribedSymbols = ConcurrentHashMap.newKeySet();

    @PostConstruct
    public void connectToUpbit() {
        var client = new StandardWebSocketClient();
        client.execute(new WebSocketHandler() {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                UpbitSseService.this.session = session;
                System.out.println("✅ Upbit WebSocket 연결 성공");

                // 연결 재설정 시 기존 심볼 재구독
                for (String symbol : subscribedSymbols) {
                    subscribeSymbol(symbol);
                }
            }

            @Override
            public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
                try {
                    // ByteBuffer → String 디코딩
                    String payload;
                    if (message.getPayload() instanceof java.nio.ByteBuffer byteBuffer) {
                        byte[] bytes = new byte[byteBuffer.remaining()];
                        byteBuffer.get(bytes);
                        payload = new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
                    } else {
                        payload = message.getPayload().toString(); // fallback
                    }

                    log.info("📥 WebSocket 수신 payload: {}", payload);

                    String code = extractCodeFromPayload(payload);
                    if (code == null) {
                        log.warn("❗ code 추출 실패");
                        return;
                    }

                    String symbol = code.split("-")[1];

                    emitterMap.getOrDefault(symbol, List.of()).forEach(emitter -> {
                        try {
                            emitter.send(SseEmitter.event().data(payload));
                            log.info("📤 SSE 전송: {}", symbol);
                        } catch (IOException e) {
                            emitter.completeWithError(e);
                        }
                    });

                } catch (Exception e) {
                    log.error("❌ WebSocket 메시지 처리 중 오류", e);
                }
            }



            private String extractCodeFromPayload(String payload) {
                int idx = payload.indexOf("\"code\":\"");
                if (idx == -1) return null;
                int start = idx + 8;
                int end = payload.indexOf("\"", start);
                return payload.substring(start, end); // 예: "KRW-BTC"
            }

            @Override
            public void handleTransportError(WebSocketSession session, Throwable exception) {
                exception.printStackTrace();
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
                System.out.println("❌ WebSocket 연결 종료: " + closeStatus);
                UpbitSseService.this.session = null;

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("🔁 WebSocket 재연결 시도...");
                        connectToUpbit();
                    }
                }, 3000);
            }

            @Override
            public boolean supportsPartialMessages() {
                return false;
            }
        }, "wss://api.upbit.com/websocket/v1");
    }

    public void subscribeSymbol(String symbol) {
        subscribedSymbols.add(symbol);
        log.info("✅ subscribeSymbol called for {}", symbol);
        if (session == null || !session.isOpen()) {
            log.warn("⚠️ WebSocket 연결이 안 되어 있어, 심볼 구독 지연됨: {}", symbol);
            return;
        }

        try {
            String json = "[{\"ticket\":\"trade-sse\"},{\"type\":\"trade\",\"codes\":[\"KRW-" + symbol + "\"]}]";
            session.sendMessage(new TextMessage(json));
            log.info("✅ 업비트 WebSocket 심볼 구독: {}", symbol);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public SseEmitter addEmitter(String symbol) {
        emitterMap.putIfAbsent(symbol, new CopyOnWriteArrayList<>());
        SseEmitter emitter = new SseEmitter(0L);
        emitterMap.get(symbol).add(emitter);

        emitter.onCompletion(() -> emitterMap.get(symbol).remove(emitter));
        emitter.onTimeout(() -> emitterMap.get(symbol).remove(emitter));
        emitter.onError(e -> emitterMap.get(symbol).remove(emitter));

        return emitter;
    }
}


