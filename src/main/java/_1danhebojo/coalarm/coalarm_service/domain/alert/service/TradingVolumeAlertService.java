package _1danhebojo.coalarm.coalarm_service.domain.alert.service;

import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.AlertSSERepositoryImpl;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.AlertEntity;
import _1danhebojo.coalarm.coalarm_service.domain.alert.service.util.FormatUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class TradingVolumeAlertService {
    // TODO : 현재는 코인명 : {TRADING_VOLUME_SOARING : true} 형식으로 들어가는데, 추후에 또 다른 값들이 추가
    private static final Map<Boolean, List<String>> volumeDatas = new HashMap<>();
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String UPBIT_MARKET_URL = "https://api.upbit.com/v1/market/all?is_details=true"; // 예제 URL

    private final AlertSSERepositoryImpl alertSSERepositoryImpl;
    private final AlertSSEService alertSSEService;
    private final FormatUtil formatUtil;


    // 서버 시작 시 자동 실행 → 업비트에서 초기 데이터 가져오기
    @PostConstruct
    public void init() {
        updateTradingVolumeData();
    }

    // 매일 오전 10시에 실행 (cron 표현식: "0 0 10 * * *")
    @Scheduled(cron = "0 0 10 * * *")
    public void updateTradingVolume() {
        updateTradingVolumeData();
    }

    // 업비트에서 최신 데이터를 조회하고 메모리에 저장
    // TODO : 현재는 10시에만 알람을 보내지만, 추후에는 사용자가 골든 크로스 알람을 설정했을 때도 바로 알람을 보낼 수 있도록 수정 (우선 메모리에 저장해뒀음)
    public void updateTradingVolumeData() {
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
                            String convertedMarket = FormatUtil.convertMarketFormat(originalMarket);

                            list.add(convertedMarket);
                        }
                    }
                }
            }

            // 메모리에 저장
            volumeDatas.put(true, list);

            log.info("거래량 급등 데이터 업데이트 완료!");

            sendVolumeToUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 특정 코인(Symbol)이 거래량 급등 리스트에 있는지 확인
    public boolean hasVolumeSpike(String symbol) {
        List<String> CoinList = volumeDatas.get(true);

        if (CoinList == null) {
            return false;
        }
        return CoinList.contains(symbol);
}

    // 전체 사용자에게 거래량 급등 알림 전송
    private void sendVolumeToUser() {
        List<AlertEntity> volumeSpikeAlerts = alertSSERepositoryImpl.findAllVolumeSpikeAlertByStatus();
        if (!volumeSpikeAlerts.isEmpty()) {
            for (AlertEntity alert : volumeSpikeAlerts) {
                String symbol = alert.getCoin().getSymbol() + "/KRW";
                boolean tradingVolume = hasVolumeSpike(symbol);

                if (tradingVolume) {
                    alertSSEService.sendAlertToUserSSE(alert.getUser().getId(), alert);
                    alertSSEService.sendAlertToUserDiscord(alert.getUser().getId(), alert);
                    log.info("거래량 급등 알림 전송: " + symbol);
                }
            }
        }
    }
}
