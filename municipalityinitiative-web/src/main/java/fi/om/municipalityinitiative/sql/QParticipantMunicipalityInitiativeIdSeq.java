package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QParticipantMunicipalityInitiativeIdSeq is a Querydsl query type for QParticipantMunicipalityInitiativeIdSeq
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QParticipantMunicipalityInitiativeIdSeq extends com.mysema.query.sql.RelationalPathBase<QParticipantMunicipalityInitiativeIdSeq> {

    private static final long serialVersionUID = -1293970791;

    public static final QParticipantMunicipalityInitiativeIdSeq participantMunicipalityInitiativeIdSeq = new QParticipantMunicipalityInitiativeIdSeq("participant_municipality_initiative_id_seq");

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

    public QParticipantMunicipalityInitiativeIdSeq(String variable) {
        super(QParticipantMunicipalityInitiativeIdSeq.class, forVariable(variable), "municipalityinitiative", "participant_municipality_initiative_id_seq");
    }

    public QParticipantMunicipalityInitiativeIdSeq(Path<? extends QParticipantMunicipalityInitiativeIdSeq> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "participant_municipality_initiative_id_seq");
    }

    public QParticipantMunicipalityInitiativeIdSeq(PathMetadata<?> metadata) {
        super(QParticipantMunicipalityInitiativeIdSeq.class, metadata, "municipalityinitiative", "participant_municipality_initiative_id_seq");
    }

}

