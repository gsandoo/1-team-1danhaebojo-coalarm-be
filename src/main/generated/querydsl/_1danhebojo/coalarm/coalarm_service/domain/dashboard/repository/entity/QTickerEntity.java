package _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTickerEntity is a Querydsl query type for TickerEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTickerEntity extends EntityPathBase<TickerEntity> {

    private static final long serialVersionUID = -254655652L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTickerEntity tickerEntity = new QTickerEntity("tickerEntity");

    public final NumberPath<java.math.BigDecimal> baseVolume = createNumber("baseVolume", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> change = createNumber("change", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> close = createNumber("close", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> high = createNumber("high", java.math.BigDecimal.class);

    public final QTickerCompositeKey id;

    public final NumberPath<java.math.BigDecimal> last = createNumber("last", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> low = createNumber("low", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> open = createNumber("open", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> percentage = createNumber("percentage", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> previousClose = createNumber("previousClose", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> quoteVolume = createNumber("quoteVolume", java.math.BigDecimal.class);

    public QTickerEntity(String variable) {
        this(TickerEntity.class, forVariable(variable), INITS);
    }

    public QTickerEntity(Path<? extends TickerEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTickerEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTickerEntity(PathMetadata metadata, PathInits inits) {
        this(TickerEntity.class, metadata, inits);
    }

    public QTickerEntity(Class<? extends TickerEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new QTickerCompositeKey(forProperty("id")) : null;
    }

}

