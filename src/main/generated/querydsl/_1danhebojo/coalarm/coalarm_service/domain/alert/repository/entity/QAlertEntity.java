package _1danhebojo.coalarm.coalarm_service.domain.alert.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAlertEntity is a Querydsl query type for AlertEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlertEntity extends EntityPathBase<AlertEntity> {

    private static final long serialVersionUID = -1553797000L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAlertEntity alertEntity = new QAlertEntity("alertEntity");

    public final BooleanPath active = createBoolean("active");

    public final DateTimePath<java.time.Instant> chgDt = createDateTime("chgDt", java.time.Instant.class);

    public final _1danhebojo.coalarm.coalarm_service.domain.coin.repository.entity.QCoinEntity coin;

    public final QGoldenCrossEntity goldenCross;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isGoldenCross = createBoolean("isGoldenCross");

    public final BooleanPath isTargetPrice = createBoolean("isTargetPrice");

    public final BooleanPath isVolumeSpike = createBoolean("isVolumeSpike");

    public final DateTimePath<java.time.Instant> regDt = createDateTime("regDt", java.time.Instant.class);

    public final QTargetPriceEntity targetPrice;

    public final StringPath title = createString("title");

    public final _1danhebojo.coalarm.coalarm_service.domain.user.repository.entity.QUserEntity user;

    public final QVolumeSpikeEntity volumeSpike;

    public QAlertEntity(String variable) {
        this(AlertEntity.class, forVariable(variable), INITS);
    }

    public QAlertEntity(Path<? extends AlertEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAlertEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAlertEntity(PathMetadata metadata, PathInits inits) {
        this(AlertEntity.class, metadata, inits);
    }

    public QAlertEntity(Class<? extends AlertEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.coin = inits.isInitialized("coin") ? new _1danhebojo.coalarm.coalarm_service.domain.coin.repository.entity.QCoinEntity(forProperty("coin")) : null;
        this.goldenCross = inits.isInitialized("goldenCross") ? new QGoldenCrossEntity(forProperty("goldenCross"), inits.get("goldenCross")) : null;
        this.targetPrice = inits.isInitialized("targetPrice") ? new QTargetPriceEntity(forProperty("targetPrice"), inits.get("targetPrice")) : null;
        this.user = inits.isInitialized("user") ? new _1danhebojo.coalarm.coalarm_service.domain.user.repository.entity.QUserEntity(forProperty("user")) : null;
        this.volumeSpike = inits.isInitialized("volumeSpike") ? new QVolumeSpikeEntity(forProperty("volumeSpike"), inits.get("volumeSpike")) : null;
    }

}

