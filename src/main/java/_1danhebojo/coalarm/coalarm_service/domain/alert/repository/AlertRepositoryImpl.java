package _1danhebojo.coalarm.coalarm_service.domain.alert.repository;

import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.*;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.jpa.AlertJpaRepository;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.jpa.GoldenCrossJpaRepository;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.jpa.TargetPriceJpaRepository;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.jpa.VolumeSpikeJpaRepository;

import _1danhebojo.coalarm.coalarm_service.domain.coin.repository.entity.CoinEntity;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.QTickerEntity;
import _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity.TickerEntity;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.util.StringUtils;

import java.util.List;

import static _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.QAlertEntity.alertEntity;
import static _1danhebojo.coalarm.coalarm_service.domain.coin.repository.entity.QCoinEntity.coinEntity;

@Repository
@RequiredArgsConstructor
public class AlertRepositoryImpl implements AlertRepository {

    private final AlertJpaRepository alertJpaRepository;
    private final TargetPriceJpaRepository targetPriceJpaRepository;
    private final GoldenCrossJpaRepository goldenCrossJpaRepository;
    private final VolumeSpikeJpaRepository volumeSpikeJpaRepository;
    private final JPAQueryFactory query;
    @PersistenceContext
    private EntityManager entityManager; // ★ EntityManager 추가

    public Long saveTargetPriceAlert(TargetPriceEntity targetPriceAlert) {
        TargetPriceEntity savedTargetPriceAlert = targetPriceJpaRepository.save(targetPriceAlert);
        entityManager.flush();
        return savedTargetPriceAlert.getId();
    }

    public Long saveGoldenCrossAlert(GoldenCrossEntity goldenCrossAlert) {
        GoldenCrossEntity savedGoldenCrossAlert = goldenCrossJpaRepository.save(goldenCrossAlert);
        entityManager.flush();  // ★ 즉시 반영
        return savedGoldenCrossAlert.getId();
    }

    public Long saveVolumeSpikeAlert(VolumeSpikeEntity volumeSpikeAlert) {
        VolumeSpikeEntity savedVolumeSpikeAlert = volumeSpikeJpaRepository.save(volumeSpikeAlert);
        entityManager.flush();  // ★ 즉시 반영
        return savedVolumeSpikeAlert.getId();
    }

    public List<AlertEntity> findAll() {
        return alertJpaRepository.findAll();
    }

    public Optional<AlertEntity> findById(Long alertId) {
        return alertJpaRepository.findById(alertId);
    }

    public Optional<AlertEntity> findByIdWithCoin(Long alertId) {
        return alertJpaRepository.findByIdWithCoin(alertId);
    }

    public void deleteById(Long alertId) {
        alertJpaRepository.deleteById(alertId);
    }

    public void deleteByUserId(Long userId){
        alertJpaRepository.deleteAlertByUserId(userId);
    }

    public AlertEntity save(AlertEntity alert) {
        AlertEntity savedAlert = alertJpaRepository.save(alert);

        entityManager.flush();
        entityManager.refresh(alert);

        return savedAlert;
    }

    public Page<AlertEntity> findAlertsByFilter(Boolean active, String filter, Pageable pageable) {
        return alertJpaRepository.findAlertsByFilter(active, filter, pageable);
    }

    public List<AlertEntity> findActiveAlertsByUserId(Long userId) {
        return alertJpaRepository.findActiveAlertsByUserId(userId);
    }

    @Override
    public List<Long> findAlertIdsByUserId(Long userId) {

        return new JPAQuery<>(entityManager)
                .select(alertEntity.id)
                .from(alertEntity)
                .where(alertEntity.user.id.eq(userId))
                .fetch();
    }

    public List<AlertEntity> findAllActiveAlerts() {
        return alertJpaRepository.findAllActiveAlerts();
    }

    @Override
    public Page<AlertEntity> findAllUserAlerts(Long userId, String symbol, Boolean active, String sort, int offset, int limit) {

        List<AlertEntity> contents = query.selectFrom(alertEntity)
                .join(coinEntity).on(coinEntity.id.eq(alertEntity.coin.id))
                .where(
                        alertEntity.user.id.eq(userId),
                        filterActive(active),
                        filterSymbol(symbol)
                )
                .offset(offset)
                .limit(limit)
                .orderBy(getSort(sort))
                .fetch()
                .stream()
                .toList();

        Long total = query.select(alertEntity.count())
                .from(alertEntity)
                .join(coinEntity).on(coinEntity.id.eq(alertEntity.coin.id))
                .where(
                        alertEntity.user.id.eq(userId),
                        filterActive(active),
                        filterSymbol(symbol)
                )
                .fetchOne();

        Pageable pageable = PageRequest.ofSize(limit);

        return new PageImpl<>(contents, pageable, total);
    }

    private OrderSpecifier<?> getSort(String sort) {
        return sort.equals("LATEST") ? alertEntity.regDt.desc() : alertEntity.regDt.asc();
    }

    private BooleanExpression filterActive(Boolean active) {
        if (active == null) return null;

        return active ? alertEntity.active.isTrue() : alertEntity.active.isFalse();
    }

    private BooleanExpression filterSymbol(String symbol) {
        return StringUtils.hasText(symbol) ? coinEntity.symbol.eq(symbol) : null;
    }

    // 알람에서 코인 심볼 조회 추가
    public Optional<CoinEntity> findCoinBySymbol(String symbol) {
        return alertJpaRepository.findCoinBySymbol(symbol);
    }

    public boolean findAlertsByUserIdAndSymbolAndAlertType(Long userId, String symbol, String alertType, Long alarmCountLimit) {
        QAlertEntity alert = QAlertEntity.alertEntity;

        switch (alertType) {
            case "GOLDEN_CROSS":
                return query.selectOne()
                        .from(alert)
                        .where(
                                alert.user.id.eq(userId),
                                alert.coin.symbol.eq(symbol),
                                alert.isGoldenCross.isTrue()
                        )
                        .fetchFirst() != null;

            case "VOLUME_SPIKE":
                return query.selectOne()
                        .from(alert)
                        .where(
                                alert.user.id.eq(userId),
                                alert.coin.symbol.eq(symbol),
                                alert.isVolumeSpike.isTrue()
                        )
                        .fetchFirst() != null;

            case "TARGET_PRICE":
                List<Long> dummyList = query.select(alert.id)
                        .from(alert)
                        .where(
                                alert.user.id.eq(userId),
                                alert.coin.symbol.eq(symbol),
                                alert.isTargetPrice.isTrue()
                        )
                        .limit(alarmCountLimit + 1)
                        .fetch();

                return dummyList.size() >= alarmCountLimit;
        }

        return false;

//        return alertJpaRepository.findAlertsByUserIdAndSymbolAndAlertType(userId, symbol, alertType, alarmCountLimit);
    }

    public List<TickerEntity> findLatestTickersBySymbolList(List<String> symbolList) {
        QTickerEntity ticker = QTickerEntity.tickerEntity;
        QTickerEntity tickerSub = new QTickerEntity("tickerSub");

        return query.selectFrom(ticker)
                .where(
                        ticker.id.baseSymbol.in(symbolList),
                        ticker.id.timestamp.in(
                                JPAExpressions
                                        .select(tickerSub.id.timestamp.max())
                                        .from(tickerSub)
                                        .where(tickerSub.id.baseSymbol.eq(ticker.id.baseSymbol))
                                        .where(tickerSub.id.quoteSymbol.eq("KRW")
                                        )
                        )
                )
                .fetch();
    }
}

