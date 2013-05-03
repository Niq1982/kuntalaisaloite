package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QAuthorInvitationInitiativeIdSeq is a Querydsl query type for QAuthorInvitationInitiativeIdSeq
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QAuthorInvitationInitiativeIdSeq extends com.mysema.query.sql.RelationalPathBase<QAuthorInvitationInitiativeIdSeq> {

    private static final long serialVersionUID = -2018849500;

    public static final QAuthorInvitationInitiativeIdSeq authorInvitationInitiativeIdSeq = new QAuthorInvitationInitiativeIdSeq("author_invitation_initiative_id_seq");

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

    public QAuthorInvitationInitiativeIdSeq(String variable) {
        super(QAuthorInvitationInitiativeIdSeq.class, forVariable(variable), "municipalityinitiative", "author_invitation_initiative_id_seq");
    }

    public QAuthorInvitationInitiativeIdSeq(Path<? extends QAuthorInvitationInitiativeIdSeq> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "author_invitation_initiative_id_seq");
    }

    public QAuthorInvitationInitiativeIdSeq(PathMetadata<?> metadata) {
        super(QAuthorInvitationInitiativeIdSeq.class, metadata, "municipalityinitiative", "author_invitation_initiative_id_seq");
    }

}

