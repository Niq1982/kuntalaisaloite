package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QSupportVote is a Querydsl query type for QSupportVote
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QSupportVote extends com.mysema.query.sql.RelationalPathBase<QSupportVote> {

    private static final long serialVersionUID = 490765445;

    public static final QSupportVote supportVote = new QSupportVote("support_vote");

    public final NumberPath<Long> batchId = createNumber("batch_id", Long.class);

    public final DateTimePath<org.joda.time.DateTime> created = createDateTime("created", org.joda.time.DateTime.class);

    public final StringPath details = createString("details");

    public final NumberPath<Long> initiativeId = createNumber("initiative_id", Long.class);

    public final StringPath supportid = createString("supportid");

    public final com.mysema.query.sql.PrimaryKey<QSupportVote> supportVotePk = createPrimaryKey(initiativeId, supportid);

    public final com.mysema.query.sql.ForeignKey<QInitiative> supportVoteInitiativeIdFk = createForeignKey(initiativeId, "id");

    public final com.mysema.query.sql.ForeignKey<QSupportVoteBatch> supportVoteBatchIdFk = createForeignKey(batchId, "id");

    public QSupportVote(String variable) {
        super(QSupportVote.class, forVariable(variable), "initiative", "support_vote");
    }

    public QSupportVote(Path<? extends QSupportVote> path) {
        super(path.getType(), path.getMetadata(), "initiative", "support_vote");
    }

    public QSupportVote(PathMetadata<?> metadata) {
        super(QSupportVote.class, metadata, "initiative", "support_vote");
    }

}

