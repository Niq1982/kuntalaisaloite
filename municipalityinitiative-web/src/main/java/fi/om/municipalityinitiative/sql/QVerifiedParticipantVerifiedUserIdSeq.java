package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QVerifiedParticipantVerifiedUserIdSeq is a Querydsl query type for QVerifiedParticipantVerifiedUserIdSeq
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QVerifiedParticipantVerifiedUserIdSeq extends com.mysema.query.sql.RelationalPathBase<QVerifiedParticipantVerifiedUserIdSeq> {

    private static final long serialVersionUID = -867565162;

    public static final QVerifiedParticipantVerifiedUserIdSeq verifiedParticipantVerifiedUserIdSeq = new QVerifiedParticipantVerifiedUserIdSeq("verified_participant_verified_user_id_seq");

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

    public QVerifiedParticipantVerifiedUserIdSeq(String variable) {
        super(QVerifiedParticipantVerifiedUserIdSeq.class, forVariable(variable), "municipalityinitiative", "verified_participant_verified_user_id_seq");
    }

    public QVerifiedParticipantVerifiedUserIdSeq(Path<? extends QVerifiedParticipantVerifiedUserIdSeq> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "verified_participant_verified_user_id_seq");
    }

    public QVerifiedParticipantVerifiedUserIdSeq(PathMetadata<?> metadata) {
        super(QVerifiedParticipantVerifiedUserIdSeq.class, metadata, "municipalityinitiative", "verified_participant_verified_user_id_seq");
    }

}

