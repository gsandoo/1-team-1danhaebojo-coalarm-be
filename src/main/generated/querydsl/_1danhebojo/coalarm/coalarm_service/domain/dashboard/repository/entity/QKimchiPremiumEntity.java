package _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QKimchiPremiumEntity is a Querydsl query type for KimchiPremiumEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QKimchiPremiumEntity extends EntityPathBase<KimchiPremiumEntity> {

    private static final long serialVersionUID = -1741640458L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QKimchiPremiumEntity kimchiPremiumEntity = new QKimchiPremiumEntity("kimchiPremiumEntity");

    public final _1danhebojo.coalarm.coalarm_service.domain.coin.repository.entity.QCoinEntity coin;

    public final NumberPath<java.math.BigDecimal> dailyChange = createNumber("dailyChange", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> domesticPrice = createNumber("domesticPrice", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> exchangeRate = createNumber("exchangeRate", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> globalPrice = createNumber("globalPrice", java.math.BigDecimal.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<java.math.BigDecimal> kimchiPremium = createNumber("kimchiPremium", java.math.BigDecimal.class);

    public final DateTimePath<java.time.Instant> regDt = createDateTime("regDt", java.time.Instant.class);

    public QKimchiPremiumEntity(String variable) {
        this(KimchiPremiumEntity.class, forVariable(variable), INITS);
    }

    public QKimchiPremiumEntity(Path<? extends KimchiPremiumEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QKimchiPremiumEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QKimchiPremiumEntity(PathMetadata metadata, PathInits inits) {
        this(KimchiPremiumEntity.class, metadata, inits);
    }

    public QKimchiPremiumEntity(Class<? extends KimchiPremiumEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.coin = inits.isInitialized("coin") ? new _1danhebojo.coalarm.coalarm_service.domain.coin.repository.entity.QCoinEntity(forProperty("coin")) : null;
    }

}

