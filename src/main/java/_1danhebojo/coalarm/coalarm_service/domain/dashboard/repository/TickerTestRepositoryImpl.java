package _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository;

import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.QTickerTestEntity;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.TickerTestEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TickerTestRepositoryImpl {

    private final JPAQueryFactory queryFactory;

    public List<TickerTestEntity> findByCodeOrderedByUtcDateTime(String code) {
        QTickerTestEntity ticker = QTickerTestEntity.tickerTestEntity;

        return queryFactory
                .selectFrom(ticker)
                .where(ticker.id.code.eq(code))
                .orderBy(ticker.id.utcDateTime.asc())
                .fetch();
    }
}
