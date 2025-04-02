package _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAlertHistoryEntity is a Querydsl query type for AlertHistoryEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlertHistoryEntity extends EntityPathBase<AlertHistoryEntity> {

    private static final long serialVersionUID = 1168541538L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAlertHistoryEntity alertHistoryEntity = new QAlertHistoryEntity("alertHistoryEntity");

    public final QAlertEntity alert;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.Instant> regDt = createDateTime("regDt", java.time.Instant.class);

    public final _1danhebojo.coalarm.coalarm_service.domain.user.repository.entity.QUserEntity user;

    public QAlertHistoryEntity(String variable) {
        this(AlertHistoryEntity.class, forVariable(variable), INITS);
    }

    public QAlertHistoryEntity(Path<? extends AlertHistoryEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAlertHistoryEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAlertHistoryEntity(PathMetadata metadata, PathInits inits) {
        this(AlertHistoryEntity.class, metadata, inits);
    }

    public QAlertHistoryEntity(Class<? extends AlertHistoryEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.alert = inits.isInitialized("alert") ? new QAlertEntity(forProperty("alert"), inits.get("alert")) : null;
        this.user = inits.isInitialized("user") ? new _1danhebojo.coalarm.coalarm_service.domain.user.repository.entity.QUserEntity(forProperty("user")) : null;
    }

}

