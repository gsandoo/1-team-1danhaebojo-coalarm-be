package _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository;

import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.KimchiPremiumEntity;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.jpa.KimchiPreminumJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class KimchiPremiumRepositoryImpl implements KimchiPremiumRepository{

    private final KimchiPreminumJpaRepository kimchiPreminumJpaRepository;

    @Override
    public List<KimchiPremiumEntity> findAllKimchiPremiums(int offset, int limit) {
        return kimchiPreminumJpaRepository.findAll()
                .stream()
                .skip(offset).limit(limit)
                .toList();
    }

    @Override
    public void saveKimchiPremium(KimchiPremiumEntity kimchiPremiumEntity) {

    }
}
