package _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository;

import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.CandleEntity;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.QCandleEntity;
import com.querydsl.core.types.dsl.Expressions;
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

    @Override
    public List<CandleEntity> findDailyCandles(String symbol, int limit) {
        QCandleEntity candle = QCandleEntity.candleEntity;

        return queryFactory
                .selectFrom(candle)
                .where(
                        candle.id.exchange.eq("upbit"),
                        candle.id.baseSymbol.eq(symbol),
                        candle.id.quoteSymbol.eq("KRW"),
                        candle.id.timeframe.eq("1m"),  // 분봉 데이터
                        // 시간이 0시(자정)이고 분이 0분인 데이터만 필터링
                        Expressions.numberTemplate(Integer.class,
                                "extract(hour from {0})", candle.id.timestamp).eq(0),
                        Expressions.numberTemplate(Integer.class,
                                "extract(minute from {0})", candle.id.timestamp).eq(0)
                )
                .orderBy(candle.id.timestamp.desc())  // 최신 날짜부터 정렬
                .limit(limit)
                .fetch();
    }
}
