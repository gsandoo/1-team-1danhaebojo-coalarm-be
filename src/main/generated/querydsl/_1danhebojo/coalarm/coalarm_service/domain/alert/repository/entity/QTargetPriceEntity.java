package _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTargetPriceEntity is a Querydsl query type for TargetPriceEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTargetPriceEntity extends EntityPathBase<TargetPriceEntity> {

    private static final long serialVersionUID = 1693498772L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTargetPriceEntity targetPriceEntity = new QTargetPriceEntity("targetPriceEntity");

    public final QAlertEntity alert;

    public final DateTimePath<java.time.Instant> chgDt = createDateTime("chgDt", java.time.Instant.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> percentage = createNumber("percentage", Integer.class);

    public final NumberPath<java.math.BigDecimal> price = createNumber("price", java.math.BigDecimal.class);

    public final DateTimePath<java.time.Instant> regDt = createDateTime("regDt", java.time.Instant.class);

    public QTargetPriceEntity(String variable) {
        this(TargetPriceEntity.class, forVariable(variable), INITS);
    }

    public QTargetPriceEntity(Path<? extends TargetPriceEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTargetPriceEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTargetPriceEntity(PathMetadata metadata, PathInits inits) {
        this(TargetPriceEntity.class, metadata, inits);
    }

    public QTargetPriceEntity(Class<? extends TargetPriceEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.alert = inits.isInitialized("alert") ? new QAlertEntity(forProperty("alert"), inits.get("alert")) : null;
    }

}

