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
        QKimchiPremiumEntity subKp = new QKimchiPremiumEntity("subKp");

        // 서브쿼리를 사용하여 각 코인별 최신 프리미엄 조회
        return queryFactory
                .selectFrom(kp)
                .join(kp.coin).fetchJoin()
                .where(
                        kp.regDt.eq(
                                JPAExpressions
                                        .select(subKp.regDt.max())
                                        .from(subKp)
                                        .where(subKp.coin.coinId.eq(kp.coin.coinId))
                        )
                )
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
                .select(kp.coin.coinId.countDistinct())
                .from(kp)
                .fetchOne();
        // (null 처리 추가)
        return count != null ? count : 0L;
    }
}
