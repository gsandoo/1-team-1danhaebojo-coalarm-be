package _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository;

import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.CandleEntity;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.QCandleEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CandleRepositoryImpl implements CandleRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CandleEntity> findRecentCandles(String symbol, int limit) {
        QCandleEntity candle = QCandleEntity.candleEntity;

        return queryFactory
                .selectFrom(candle)
                .where(
                        candle.id.exchange.eq("upbit"),
                        candle.id.baseSymbol.eq(symbol),
                        candle.id.quoteSymbol.eq("KRW"),
                        candle.id.timeframe.eq("1m")
                )
                .orderBy(candle.id.timestamp.desc())
                .limit(limit)
                .fetch();
    }
}
