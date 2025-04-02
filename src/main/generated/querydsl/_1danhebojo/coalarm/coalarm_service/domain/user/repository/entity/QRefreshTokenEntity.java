package _1danhebojo.coalarm.coalarm_service.domain.user.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRefreshTokenEntity is a Querydsl query type for RefreshTokenEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRefreshTokenEntity extends EntityPathBase<RefreshTokenEntity> {

    private static final long serialVersionUID = 884537405L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRefreshTokenEntity refreshTokenEntity = new QRefreshTokenEntity("refreshTokenEntity");

    public final DateTimePath<java.time.Instant> expiresAt = createDateTime("expiresAt", java.time.Instant.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isRevoked = createBoolean("isRevoked");

    public final DateTimePath<java.time.Instant> issuedAt = createDateTime("issuedAt", java.time.Instant.class);

    public final StringPath token = createString("token");

    public final QUserEntity user;

    public final StringPath userAgent = createString("userAgent");

    public QRefreshTokenEntity(String variable) {
        this(RefreshTokenEntity.class, forVariable(variable), INITS);
    }

    public QRefreshTokenEntity(Path<? extends RefreshTokenEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRefreshTokenEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRefreshTokenEntity(PathMetadata metadata, PathInits inits) {
        this(RefreshTokenEntity.class, metadata, inits);
    }

    public QRefreshTokenEntity(Class<? extends RefreshTokenEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUserEntity(forProperty("user")) : null;
    }

}

