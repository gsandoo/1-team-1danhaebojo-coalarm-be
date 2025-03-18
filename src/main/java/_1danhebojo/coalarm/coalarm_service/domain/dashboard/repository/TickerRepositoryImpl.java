package _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository;

import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.CoinEntity;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.QCoinEntity;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.QTickerEntity;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.TickerEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TickerRepositoryImpl implements TickerRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<TickerEntity> findByCoinIdOrderedByUtcDateTime(Long coinId) {
        QTickerEntity ticker = QTickerEntity.tickerEntity;
        QCoinEntity coin = QCoinEntity.coinEntity;

        // 코인 정보 먼저 조회
        CoinEntity coinEntity = queryFactory
                .selectFrom(coin)
                .where(coin.coinId.eq(coinId))
                .fetchOne();

        if (coinEntity == null) {
            return Collections.emptyList();
        }

        String symbolPrefix = coinEntity.getSymbol() + "/"; // "BTC/"와 같은 형태로 검색

        // 명확한 형태로 검색
        return queryFactory
                .selectFrom(ticker)
                .where(ticker.id.symbol.startsWith(symbolPrefix))
                .orderBy(ticker.id.timestamp.asc())
                .limit(100)
                .fetch();
    }

}
