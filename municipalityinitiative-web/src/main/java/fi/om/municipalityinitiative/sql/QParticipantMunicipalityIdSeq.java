package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QParticipantMunicipalityIdSeq is a Querydsl query type for QParticipantMunicipalityIdSeq
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QParticipantMunicipalityIdSeq extends com.mysema.query.sql.RelationalPathBase<QParticipantMunicipalityIdSeq> {

    private static final long serialVersionUID = 912844421;

    public static final QParticipantMunicipalityIdSeq participantMunicipalityIdSeq = new QParticipantMunicipalityIdSeq("participant_municipality_id_seq");

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

    public QParticipantMunicipalityIdSeq(String variable) {
        super(QParticipantMunicipalityIdSeq.class, forVariable(variable), "municipalityinitiative", "participant_municipality_id_seq");
    }

    public QParticipantMunicipalityIdSeq(Path<? extends QParticipantMunicipalityIdSeq> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "participant_municipality_id_seq");
    }

    public QParticipantMunicipalityIdSeq(PathMetadata<?> metadata) {
        super(QParticipantMunicipalityIdSeq.class, metadata, "municipalityinitiative", "participant_municipality_id_seq");
    }

}

