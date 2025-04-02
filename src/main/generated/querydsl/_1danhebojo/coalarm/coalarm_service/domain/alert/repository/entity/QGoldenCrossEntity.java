package _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGoldenCrossEntity is a Querydsl query type for GoldenCrossEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGoldenCrossEntity extends EntityPathBase<GoldenCrossEntity> {

    private static final long serialVersionUID = 684243L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGoldenCrossEntity goldenCrossEntity = new QGoldenCrossEntity("goldenCrossEntity");

    public final QAlertEntity alert;

    public final DateTimePath<java.time.Instant> chgDt = createDateTime("chgDt", java.time.Instant.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> longMa = createNumber("longMa", Integer.class);

    public final DateTimePath<java.time.Instant> regDt = createDateTime("regDt", java.time.Instant.class);

    public final NumberPath<Integer> shortMa = createNumber("shortMa", Integer.class);

    public QGoldenCrossEntity(String variable) {
        this(GoldenCrossEntity.class, forVariable(variable), INITS);
    }

    public QGoldenCrossEntity(Path<? extends GoldenCrossEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGoldenCrossEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGoldenCrossEntity(PathMetadata metadata, PathInits inits) {
        this(GoldenCrossEntity.class, metadata, inits);
    }

    public QGoldenCrossEntity(Class<? extends GoldenCrossEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.alert = inits.isInitialized("alert") ? new QAlertEntity(forProperty("alert"), inits.get("alert")) : null;
    }

}

