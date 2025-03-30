package _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository;

import _1danhebojo.coalarm.coalarm_service.domain.coin.repository.entity.CoinEntity;
import _1danhebojo.coalarm.coalarm_service.domain.coin.repository.entity.QCoinEntity;
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

        String coinSymbol = coinEntity.getSymbol();

        // 명확한 형태로 검색
        return queryFactory
                .selectFrom(ticker)
                .where(ticker.id.baseSymbol.eq(coinSymbol))
                .orderBy(ticker.id.timestamp.asc())
                .limit(100)
                .fetch();
    }

    @Override
    public Optional<TickerEntity> findLatestBySymbol(String symbol) {
        QTickerEntity ticker = QTickerEntity.tickerEntity;

        return Optional.ofNullable(
                queryFactory
                        .selectFrom(ticker)
                        .where(ticker.id.baseSymbol.eq(symbol))
                        .orderBy(ticker.id.timestamp.desc()) // 최신 순으로 정렬
                        .fetchFirst()
        );
    }
}
