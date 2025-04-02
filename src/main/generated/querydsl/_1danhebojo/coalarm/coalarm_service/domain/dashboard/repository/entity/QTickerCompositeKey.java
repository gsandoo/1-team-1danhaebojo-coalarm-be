package _1danhebojo.coalarm.coalarm_service.domain.dashboard.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTickerCompositeKey is a Querydsl query type for TickerCompositeKey
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QTickerCompositeKey extends BeanPath<TickerCompositeKey> {

    private static final long serialVersionUID = 702739217L;

    public static final QTickerCompositeKey tickerCompositeKey = new QTickerCompositeKey("tickerCompositeKey");

    public final StringPath baseSymbol = createString("baseSymbol");

    public final StringPath exchange = createString("exchange");

    public final StringPath quoteSymbol = createString("quoteSymbol");

    public final DateTimePath<java.time.Instant> timestamp = createDateTime("timestamp", java.time.Instant.class);

    public QTickerCompositeKey(String variable) {
        super(TickerCompositeKey.class, forVariable(variable));
    }

    public QTickerCompositeKey(Path<? extends TickerCompositeKey> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTickerCompositeKey(PathMetadata metadata) {
        super(TickerCompositeKey.class, metadata);
    }

}

