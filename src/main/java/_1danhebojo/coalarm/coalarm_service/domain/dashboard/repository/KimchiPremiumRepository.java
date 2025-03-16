package _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository;

import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.CoinEntity;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.KimchiPremiumEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface KimchiPremiumRepository {
    List<KimchiPremiumEntity> findAllKimchiPremiums(int offset,int limit);
    void saveKimchiPremium(KimchiPremiumEntity kimchiPremiumEntity);

    Optional<KimchiPremiumEntity> findTopByCoinAndRegDtBetweenOrderByRegDtDesc(
            CoinEntity coin,
            LocalDateTime fromDateTime,
            LocalDateTime toDateTime
    );
}
