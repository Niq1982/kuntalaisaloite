package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QAuthorInvitationIdSeq is a Querydsl query type for QAuthorInvitationIdSeq
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QAuthorInvitationIdSeq extends com.mysema.query.sql.RelationalPathBase<QAuthorInvitationIdSeq> {

    private static final long serialVersionUID = -959003952;

    public static final QAuthorInvitationIdSeq authorInvitationIdSeq = new QAuthorInvitationIdSeq("author_invitation_id_seq");

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

    public QAuthorInvitationIdSeq(String variable) {
        super(QAuthorInvitationIdSeq.class, forVariable(variable), "municipalityinitiative", "author_invitation_id_seq");
    }

    public QAuthorInvitationIdSeq(Path<? extends QAuthorInvitationIdSeq> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "author_invitation_id_seq");
    }

    public QAuthorInvitationIdSeq(PathMetadata<?> metadata) {
        super(QAuthorInvitationIdSeq.class, metadata, "municipalityinitiative", "author_invitation_id_seq");
    }

}

