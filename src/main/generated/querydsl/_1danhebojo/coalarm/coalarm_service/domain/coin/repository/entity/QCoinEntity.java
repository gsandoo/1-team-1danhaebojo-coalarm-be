package _1danhebojo.coalarm.coalarm_service.domain.coin.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCoinEntity is a Querydsl query type for CoinEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCoinEntity extends EntityPathBase<CoinEntity> {

    private static final long serialVersionUID = 590062506L;

    public static final QCoinEntity coinEntity = new QCoinEntity("coinEntity");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final StringPath symbol = createString("symbol");

    public QCoinEntity(String variable) {
        super(CoinEntity.class, forVariable(variable));
    }

    public QCoinEntity(Path<? extends CoinEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCoinEntity(PathMetadata metadata) {
        super(CoinEntity.class, metadata);
    }

}

