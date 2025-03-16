package _1danhebojo.coalarm.coalarm_service.domain.dashboard.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KimchiPremiumScheduler {
    private final KimchiPremiumService kimchiPremiumService;

    @Scheduled(fixedRate = 300000)
    public void calculateAndSaveKimchiPremiumTask(){
        log.info("김치 프리미엄 계산 시작...");
        kimchiPremiumService.calculateAndSaveKimchiPremium();
        log.info("김치 프리미엄 계산 완료.");
    }
}
