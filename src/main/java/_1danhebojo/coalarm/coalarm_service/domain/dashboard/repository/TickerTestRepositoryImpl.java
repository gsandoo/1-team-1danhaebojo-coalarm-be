package _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository;

import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.QCoinEntity;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.QTickerTestEntity;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.TickerTestEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TickerTestRepositoryImpl implements TickerTestRepository {

    private final JPAQueryFactory queryFactory;

    public List<TickerTestEntity> findByCoinIdOrderedByUtcDateTime(Long coinId) {
        QTickerTestEntity ticker = QTickerTestEntity.tickerTestEntity;
        QCoinEntity coin = QCoinEntity.coinEntity;

        return queryFactory
                .selectFrom(ticker)
                .join(coin).on(ticker.id.code.substring(4).eq(coin.symbol)) // "KRW-" 제거 후 조인
                .where(coin.coinId.eq(coinId))
                .orderBy(ticker.id.utcDateTime.asc())
                .fetch();
    }
}
