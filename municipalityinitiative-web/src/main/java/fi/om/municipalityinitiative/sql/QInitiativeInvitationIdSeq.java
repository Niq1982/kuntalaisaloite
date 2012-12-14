package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QInitiativeInvitationIdSeq is a Querydsl query type for QInitiativeInvitationIdSeq
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QInitiativeInvitationIdSeq extends com.mysema.query.sql.RelationalPathBase<QInitiativeInvitationIdSeq> {

    private static final long serialVersionUID = -1573325397;

    public static final QInitiativeInvitationIdSeq initiativeInvitationIdSeq = new QInitiativeInvitationIdSeq("initiative_invitation_id_seq");

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

    public QInitiativeInvitationIdSeq(String variable) {
        super(QInitiativeInvitationIdSeq.class, forVariable(variable), "initiative", "initiative_invitation_id_seq");
    }

    public QInitiativeInvitationIdSeq(Path<? extends QInitiativeInvitationIdSeq> path) {
        super(path.getType(), path.getMetadata(), "initiative", "initiative_invitation_id_seq");
    }

    public QInitiativeInvitationIdSeq(PathMetadata<?> metadata) {
        super(QInitiativeInvitationIdSeq.class, metadata, "initiative", "initiative_invitation_id_seq");
    }

}

