package _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCoinIndicatorEntity is a Querydsl query type for CoinIndicatorEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCoinIndicatorEntity extends EntityPathBase<CoinIndicatorEntity> {

    private static final long serialVersionUID = 1955917138L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCoinIndicatorEntity coinIndicatorEntity = new QCoinIndicatorEntity("coinIndicatorEntity");

    public final _1danhebojo.coalarm.coalarm_service.domain.coin.repository.entity.QCoinEntity coin;

    public final NumberPath<java.math.BigDecimal> histogram = createNumber("histogram", java.math.BigDecimal.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<java.math.BigDecimal> longStrength = createNumber("longStrength", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> macd = createNumber("macd", java.math.BigDecimal.class);

    public final DateTimePath<java.time.Instant> regDt = createDateTime("regDt", java.time.Instant.class);

    public final NumberPath<java.math.BigDecimal> rsi = createNumber("rsi", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> signal = createNumber("signal", java.math.BigDecimal.class);

    public final StringPath trend = createString("trend");

    public QCoinIndicatorEntity(String variable) {
        this(CoinIndicatorEntity.class, forVariable(variable), INITS);
    }

    public QCoinIndicatorEntity(Path<? extends CoinIndicatorEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCoinIndicatorEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCoinIndicatorEntity(PathMetadata metadata, PathInits inits) {
        this(CoinIndicatorEntity.class, metadata, inits);
    }

    public QCoinIndicatorEntity(Class<? extends CoinIndicatorEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.coin = inits.isInitialized("coin") ? new _1danhebojo.coalarm.coalarm_service.domain.coin.repository.entity.QCoinEntity(forProperty("coin")) : null;
    }

}

