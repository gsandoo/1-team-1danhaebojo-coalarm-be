package _1danhebojo.coalarm.coalarm_service.domain.alert.repository;

import _1danhebojo.coalarm.coalarm_service.domain.alert.controller.request.GoldenCrossAlertRequest;
import _1danhebojo.coalarm.coalarm_service.domain.alert.controller.request.TargetPriceAlertRequest;
import _1danhebojo.coalarm.coalarm_service.domain.alert.controller.request.VolumeSpikeAlertRequest;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.*;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.jpa.AlertJpaRepository;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.jpa.GoldenCrossJpaRepository;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.jpa.TargetPriceJpaRepository;
import _1danhebojo.coalarm.coalarm_service.domain.alert.repository.jpa.VolumeSpikeJpaRepository;

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

import static _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity.QAlert.alert;
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

    public Long saveTargetPriceAlert(TargetPriceAlert targetPriceAlert) {
        TargetPriceAlert savedTargetPriceAlert = targetPriceJpaRepository.save(targetPriceAlert);
        entityManager.flush();
        return savedTargetPriceAlert.getTargetPriceId();
    }

    public Long saveGoldenCrossAlert(GoldenCrossAlert goldenCrossAlert) {
        GoldenCrossAlert savedGoldenCrossAlert = goldenCrossJpaRepository.save(goldenCrossAlert);
        entityManager.flush();  // ★ 즉시 반영
        return savedGoldenCrossAlert.getGoldenCrossId();
    }

    public Long saveVolumeSpikeAlert(VolumeSpikeAlert volumeSpikeAlert) {
        VolumeSpikeAlert savedVolumeSpikeAlert = volumeSpikeJpaRepository.save(volumeSpikeAlert);
        entityManager.flush();  // ★ 즉시 반영
        return savedVolumeSpikeAlert.getMarketAlertId();
    }

    public List<Alert> findAll() {
        return alertJpaRepository.findAll();
    }

    public Optional<Alert> findById(Long alertId) {
        return alertJpaRepository.findById(alertId);
    }

    public Optional<Alert> findByIdWithCoin(Long alertId) {
        return alertJpaRepository.findByIdWithCoin(alertId);
    }

    public void deleteById(Long alertId) {
        alertJpaRepository.deleteById(alertId);
    }

    public void deleteByUserId(Long userId){
        alertJpaRepository.deleteAlertByUserId(userId);
    }

    public Alert save(Alert alert) {
        Alert savedAlert = alertJpaRepository.save(alert);

        entityManager.flush();
        entityManager.refresh(alert);

        return savedAlert;
    }

    public Page<Alert> findAlertsByFilter(Boolean active, String filter, Pageable pageable) {
        return alertJpaRepository.findAlertsByFilter(active, filter, pageable);
    }

    public List<Alert> findActiveAlertsByUserId(Long userId) {
        return alertJpaRepository.findActiveAlertsByUserId(userId);
    }

    public List<Alert> findAllActiveAlerts() {
        return alertJpaRepository.findAllActiveAlerts();
    }

    @Override
    public Page<Alert> findAllUserAlerts(Long userId, String symbol, Boolean active, String sort, int offset, int limit) {

        List<Alert> contents = query.selectFrom(alert)
                .join(coinEntity).on(coinEntity.coinId.eq(alert.coin.coinId))
                .where(
                        alert.user.userId.eq(userId),
                        filterActive(active),
                        filterSymbol(symbol)
                )
                .offset(offset)
                .limit(limit)
                .orderBy(getSort(sort))
                .fetch()
                .stream()
                .toList();

        Long total = query.select(alert.count())
                .from(alert)
                .where(
                        alert.user.userId.eq(userId),
                        filterActive(active),
                        filterSymbol(symbol)
                )
                .fetchOne();

        Pageable pageable = PageRequest.ofSize(limit);

        return new PageImpl<>(contents, pageable, total);
    }

    private OrderSpecifier<?> getSort(String sort) {
        return sort.equals("LATEST") ? alert.regDt.desc() : alert.regDt.asc();
    }

    private BooleanExpression filterActive(Boolean active) {
        if (active == null) return null;

        return active ? alert.active.isTrue() : alert.active.isFalse();
    }

    private BooleanExpression filterSymbol(String symbol) {
        return StringUtils.hasText(symbol) ? alert.coin.symbol.eq(symbol) : null;
    }

    // 알람에서 코인 심볼 조회 추가
    public Optional<Coin> findCoinBySymbol(String symbol) {
        return alertJpaRepository.findCoinBySymbol(symbol);
    }

    public boolean findAlertsByUserIdAndSymbolAndAlertType(Long userId, String symbol, String alertType) {
        return alertJpaRepository.findAlertsByUserIdAndSymbolAndAlertType(userId, symbol, alertType);
    }

    public List<TickerEntity> findLatestTickersBySymbolList(List<String> symbolList) {
        QTickerEntity ticker = QTickerEntity.tickerEntity;
        QTickerEntity tickerSub = new QTickerEntity("tickerSub");

        return query.selectFrom(ticker)
                .where(
                        ticker.id.quoteSymbol.in(symbolList),
                        ticker.id.timestamp.eq(
                                JPAExpressions
                                        .select(tickerSub.id.timestamp.max())
                                        .from(tickerSub)
                                        .where(tickerSub.id.quoteSymbol.eq(ticker.id.quoteSymbol))
                        )
                )
                .fetch();
    }
}

