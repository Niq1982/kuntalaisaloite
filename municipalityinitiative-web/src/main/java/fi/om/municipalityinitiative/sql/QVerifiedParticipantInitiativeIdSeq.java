package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QVerifiedParticipantInitiativeIdSeq is a Querydsl query type for QVerifiedParticipantInitiativeIdSeq
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QVerifiedParticipantInitiativeIdSeq extends com.mysema.query.sql.RelationalPathBase<QVerifiedParticipantInitiativeIdSeq> {

    private static final long serialVersionUID = 535341853;

    public static final QVerifiedParticipantInitiativeIdSeq verifiedParticipantInitiativeIdSeq = new QVerifiedParticipantInitiativeIdSeq("verified_participant_initiative_id_seq");

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

    public QVerifiedParticipantInitiativeIdSeq(String variable) {
        super(QVerifiedParticipantInitiativeIdSeq.class, forVariable(variable), "municipalityinitiative", "verified_participant_initiative_id_seq");
    }

    public QVerifiedParticipantInitiativeIdSeq(Path<? extends QVerifiedParticipantInitiativeIdSeq> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "verified_participant_initiative_id_seq");
    }

    public QVerifiedParticipantInitiativeIdSeq(PathMetadata<?> metadata) {
        super(QVerifiedParticipantInitiativeIdSeq.class, metadata, "municipalityinitiative", "verified_participant_initiative_id_seq");
    }

}

