package _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository;

import _1danhebojo.coalarm.coalarm_service.domain.coin.repository.entity.CoinEntity;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.KimchiPremiumEntity;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.QKimchiPremiumEntity;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class KimchiPremiumRepositoryImpl implements KimchiPremiumRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<KimchiPremiumEntity> findAllKimchiPremiums(int offset, int limit) {
        QKimchiPremiumEntity kp = QKimchiPremiumEntity.kimchiPremiumEntity;

        // 코인별 최신 프리미엄 ID를 먼저 조회
        List<Long> latestPremiumIds = queryFactory
                .select(kp.id.max())
                .from(kp)
                .groupBy(kp.coin.id)
                .fetch();

        // 해당 ID로 실제 데이터 조회
        return queryFactory
                .selectFrom(kp)
                .join(kp.coin).fetchJoin()
                .where(kp.id.in(latestPremiumIds))
                .orderBy(kp.kimchiPremium.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    @Override
    public Optional<KimchiPremiumEntity> findTopByCoinAndRegDtBetweenOrderByRegDtDesc(
            CoinEntity coin,
            LocalDateTime fromDateTime,
            LocalDateTime toDateTime
    ) {
        QKimchiPremiumEntity kp = QKimchiPremiumEntity.kimchiPremiumEntity;

        Instant fromInstant = fromDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Instant toInstant = toDateTime.atZone(ZoneId.systemDefault()).toInstant();

        KimchiPremiumEntity result = queryFactory
                .selectFrom(kp)
                .where(
                        kp.coin.eq(coin),
                        kp.regDt.after(fromInstant).or(kp.regDt.eq(fromInstant)),
                        kp.regDt.before(toInstant).or(kp.regDt.eq(toInstant))
                )
                .orderBy(kp.regDt.desc())
                .fetchFirst();

        return Optional.ofNullable(result);
    }

    @Override
    public long countAllKimchiPremiums() {
        QKimchiPremiumEntity kp = QKimchiPremiumEntity.kimchiPremiumEntity;

        // 중복 없이 코인 ID의 종류 수 카운트
        Long count = queryFactory
                .select(kp.coin.id.countDistinct())
                .from(kp)
                .fetchOne();
        // (null 처리 추가)
        return count != null ? count : 0L;
    }
}
