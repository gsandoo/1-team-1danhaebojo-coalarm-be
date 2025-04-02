package _1danhebojo.coalarm.coalarm_service.domain.auth.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QJwtBlacklistEntity is a Querydsl query type for JwtBlacklistEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QJwtBlacklistEntity extends EntityPathBase<JwtBlacklistEntity> {

    private static final long serialVersionUID = -1164818964L;

    public static final QJwtBlacklistEntity jwtBlacklistEntity = new QJwtBlacklistEntity("jwtBlacklistEntity");

    public final DateTimePath<java.time.Instant> expiryDate = createDateTime("expiryDate", java.time.Instant.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath token = createString("token");

    public QJwtBlacklistEntity(String variable) {
        super(JwtBlacklistEntity.class, forVariable(variable));
    }

    public QJwtBlacklistEntity(Path<? extends JwtBlacklistEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QJwtBlacklistEntity(PathMetadata metadata) {
        super(JwtBlacklistEntity.class, metadata);
    }

}

