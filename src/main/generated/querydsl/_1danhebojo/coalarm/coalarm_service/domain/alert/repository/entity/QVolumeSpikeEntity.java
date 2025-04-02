package _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVolumeSpikeEntity is a Querydsl query type for VolumeSpikeEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QVolumeSpikeEntity extends EntityPathBase<VolumeSpikeEntity> {

    private static final long serialVersionUID = -1324399256L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QVolumeSpikeEntity volumeSpikeEntity = new QVolumeSpikeEntity("volumeSpikeEntity");

    public final QAlertEntity alert;

    public final DateTimePath<java.time.Instant> chgDt = createDateTime("chgDt", java.time.Instant.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.Instant> regDt = createDateTime("regDt", java.time.Instant.class);

    public final BooleanPath tradingVolumeSoaring = createBoolean("tradingVolumeSoaring");

    public QVolumeSpikeEntity(String variable) {
        this(VolumeSpikeEntity.class, forVariable(variable), INITS);
    }

    public QVolumeSpikeEntity(Path<? extends VolumeSpikeEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QVolumeSpikeEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QVolumeSpikeEntity(PathMetadata metadata, PathInits inits) {
        this(VolumeSpikeEntity.class, metadata, inits);
    }

    public QVolumeSpikeEntity(Class<? extends VolumeSpikeEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.alert = inits.isInitialized("alert") ? new QAlertEntity(forProperty("alert"), inits.get("alert")) : null;
    }

}

