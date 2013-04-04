package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QAuthorParticipantIdSeq is a Querydsl query type for QAuthorParticipantIdSeq
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QAuthorParticipantIdSeq extends com.mysema.query.sql.RelationalPathBase<QAuthorParticipantIdSeq> {

    private static final long serialVersionUID = 1685963980;

    public static final QAuthorParticipantIdSeq authorParticipantIdSeq = new QAuthorParticipantIdSeq("author_participant_id_seq");

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

    public QAuthorParticipantIdSeq(String variable) {
        super(QAuthorParticipantIdSeq.class, forVariable(variable), "municipalityinitiative", "author_participant_id_seq");
    }

    public QAuthorParticipantIdSeq(Path<? extends QAuthorParticipantIdSeq> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "author_participant_id_seq");
    }

    public QAuthorParticipantIdSeq(PathMetadata<?> metadata) {
        super(QAuthorParticipantIdSeq.class, metadata, "municipalityinitiative", "author_participant_id_seq");
    }

}

