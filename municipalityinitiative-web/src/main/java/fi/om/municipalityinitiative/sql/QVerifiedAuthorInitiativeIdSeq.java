package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QVerifiedAuthorInitiativeIdSeq is a Querydsl query type for QVerifiedAuthorInitiativeIdSeq
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QVerifiedAuthorInitiativeIdSeq extends com.mysema.query.sql.RelationalPathBase<QVerifiedAuthorInitiativeIdSeq> {

    private static final long serialVersionUID = 2041440853;

    public static final QVerifiedAuthorInitiativeIdSeq verifiedAuthorInitiativeIdSeq = new QVerifiedAuthorInitiativeIdSeq("verified_author_initiative_id_seq");

    public final NumberPath<Long> cacheValue = createNumber("cache_value", Long.class);

    public final NumberPath<Long> incrementBy = createNumber("increment_by", Long.class);

    public final BooleanPath isCalled = createBoolean("is_called");

    public final BooleanPath isCycled = createBoolean("is_cycled");

    public final NumberPath<Long> lastValue = createNumber("last_value", Long.class);

    public final NumberPath<Long> logCnt = createNumber("log_cnt", Long.class);

    public final NumberPath<Long> maxValue = createNumber("max_value", Long.class);

    public final NumberPath<Long> minValue = createNumber("min_value", Long.class);

    public final StringPath sequenceName = createString("sequence_name");

    public final NumberPath<Long> startValue = createNumber("start_value", Long.class);

    public QVerifiedAuthorInitiativeIdSeq(String variable) {
        super(QVerifiedAuthorInitiativeIdSeq.class, forVariable(variable), "municipalityinitiative", "verified_author_initiative_id_seq");
    }

    public QVerifiedAuthorInitiativeIdSeq(Path<? extends QVerifiedAuthorInitiativeIdSeq> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "verified_author_initiative_id_seq");
    }

    public QVerifiedAuthorInitiativeIdSeq(PathMetadata<?> metadata) {
        super(QVerifiedAuthorInitiativeIdSeq.class, metadata, "municipalityinitiative", "verified_author_initiative_id_seq");
    }

}

