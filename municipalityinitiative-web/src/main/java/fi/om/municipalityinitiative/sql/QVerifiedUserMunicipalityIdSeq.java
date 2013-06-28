package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QVerifiedUserMunicipalityIdSeq is a Querydsl query type for QVerifiedUserMunicipalityIdSeq
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QVerifiedUserMunicipalityIdSeq extends com.mysema.query.sql.RelationalPathBase<QVerifiedUserMunicipalityIdSeq> {

    private static final long serialVersionUID = 89763429;

    public static final QVerifiedUserMunicipalityIdSeq verifiedUserMunicipalityIdSeq = new QVerifiedUserMunicipalityIdSeq("verified_user_municipality_id_seq");

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

    public QVerifiedUserMunicipalityIdSeq(String variable) {
        super(QVerifiedUserMunicipalityIdSeq.class, forVariable(variable), "municipalityinitiative", "verified_user_municipality_id_seq");
    }

    public QVerifiedUserMunicipalityIdSeq(Path<? extends QVerifiedUserMunicipalityIdSeq> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "verified_user_municipality_id_seq");
    }

    public QVerifiedUserMunicipalityIdSeq(PathMetadata<?> metadata) {
        super(QVerifiedUserMunicipalityIdSeq.class, metadata, "municipalityinitiative", "verified_user_municipality_id_seq");
    }

}

