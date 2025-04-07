package _1danhebojo.coalarm.coalarm_service.domain.dashboard.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardScheduler {
    private final KimchiPremiumService kimchiPremiumService;
    private final CoinIndicatorService coinIndicatorService;

    @Scheduled(fixedRate = 3000000)
    public void calculateAndSaveKimchiPremiumTask(){
        log.info("대시보드 데이터 계산 시작...");
        kimchiPremiumService.calculateAndSaveKimchiPremium();
        coinIndicatorService.saveIndicators("BTC");
        log.info("대시보드 데이터 계산 완료.");
    }
}
