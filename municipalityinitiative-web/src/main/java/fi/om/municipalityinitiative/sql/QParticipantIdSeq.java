package fi.om.municipalityinitiative.sql;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BooleanPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QParticipantIdSeq is a Querydsl query type for QParticipantIdSeq
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QParticipantIdSeq extends com.mysema.query.sql.RelationalPathBase<QParticipantIdSeq> {

    private static final long serialVersionUID = -1139502815;

    public static final QParticipantIdSeq participantIdSeq = new QParticipantIdSeq("participant_id_seq");

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

    public QParticipantIdSeq(String variable) {
        super(QParticipantIdSeq.class, forVariable(variable), "municipalityinitiative", "participant_id_seq");
    }

    public QParticipantIdSeq(Path<? extends QParticipantIdSeq> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "participant_id_seq");
    }

    public QParticipantIdSeq(PathMetadata<?> metadata) {
        super(QParticipantIdSeq.class, metadata, "municipalityinitiative", "participant_id_seq");
    }

}

