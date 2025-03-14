package _1danhebojo.coalarm.coalarm_service.domain.dashboard.service;

import _1danhebojo.coalarm.coalarm_service.domain.dashboard.controller.response.ResponseKimchiPremium;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.KimchiPremiumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KimchiPremiumServiceImpl implements KimchiPremiumService{
    private final KimchiPremiumRepository kimchiPremiumRepository;

    @Override
    public List<ResponseKimchiPremium> getKimchiPremiums(int offset, int limit) {
        return kimchiPremiumRepository.findAllKimchiPremiums(offset,limit)
                .stream()
                .map(ResponseKimchiPremium::fromEntity)
                .toList();
    }

    @Override
    public void calculateAndSaveKimchiPremium() {

    }
}
