package _1danhebojo.coalarm.coalarm_service.domain.dashboard.service;

import _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller.response.ResponseKimchiPremium;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardScheduler {
    private final KimchiPremiumService kimchiPremiumService;
    private final CoinIndicatorService coinIndicatorService;

    // TODO: 멀티 스레드를 통해 김치 프리미엄 계산시간을 다른 곳에 영향이 가지 않게하기

    @Scheduled(fixedRate = 60000)
    public void calculateCoinIndicators(){
        log.info("코인지표 데이터 계산 시작...");
        long startTime = System.currentTimeMillis();

        coinIndicatorService.saveIndicators("BTC");

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        log.info("코인지표 데이터 계산 완료. 실행 시간: " + executionTime + "ms");
    }

    @Scheduled(fixedRate = 300000)
    public void calculateAndSaveKimchiPremiumTask(){

        log.info("김치프리미엄 데이터 계산 시작...");
        long startTime = System.currentTimeMillis();

        kimchiPremiumService.calculateAndSaveKimchiPremium();

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        log.info("김치프리미엄 데이터 계산 완료. 실행 시간: " + executionTime + "ms");
    }
}
