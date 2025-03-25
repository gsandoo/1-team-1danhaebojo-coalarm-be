package _1danhebojo.coalarm.coalarm_service.domain.coin.repository;

import _1danhebojo.coalarm.coalarm_service.domain.coin.repository.entity.CoinEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.QAlert.alert;
import static _1danhebojo.coalarm.coalarm_service.domain.coin.repository.entity.QCoinEntity.coinEntity;

@Repository
@RequiredArgsConstructor
public class CoinRepositoryImpl implements CoinRepository {

    private final JPAQueryFactory query;

    @Override
    public List<CoinEntity> findAlertCoinsByUserId(Long userId) {
        return query.selectDistinct(coinEntity)
                .from(alert)
                .join(coinEntity).on(coinEntity.coinId.eq(alert.coin.coinId))
                .where(alert.user.userId.eq(userId))
                .orderBy(coinEntity.coinId.asc())
                .fetch();
    }
}
