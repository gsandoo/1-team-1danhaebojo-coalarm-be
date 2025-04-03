package _1danhebojo.coalarm.coalarm_service.domain.user.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserEntity is a Querydsl query type for UserEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserEntity extends EntityPathBase<UserEntity> {

    private static final long serialVersionUID = 2121749098L;

    public static final QUserEntity userEntity = new QUserEntity("userEntity");

    public final DateTimePath<java.time.Instant> chgDt = createDateTime("chgDt", java.time.Instant.class);

    public final StringPath discordWebhook = createString("discordWebhook");

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath kakaoId = createString("kakaoId");

    public final StringPath nickname = createString("nickname");

    public final StringPath profileImg = createString("profileImg");

    public final DateTimePath<java.time.Instant> regDt = createDateTime("regDt", java.time.Instant.class);

    public QUserEntity(String variable) {
        super(UserEntity.class, forVariable(variable));
    }

    public QUserEntity(Path<? extends UserEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserEntity(PathMetadata metadata) {
        super(UserEntity.class, metadata);
    }

}

