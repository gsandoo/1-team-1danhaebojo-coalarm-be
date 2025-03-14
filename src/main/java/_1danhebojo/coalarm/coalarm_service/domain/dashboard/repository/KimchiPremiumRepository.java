package _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository;

import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.KimchiPremiumEntity;

import java.util.List;

public interface KimchiPremiumRepository {
    List<KimchiPremiumEntity> findAllKimchiPremiums(int offset,int limit);
    void saveKimchiPremium(KimchiPremiumEntity kimchiPremiumEntity);
}
