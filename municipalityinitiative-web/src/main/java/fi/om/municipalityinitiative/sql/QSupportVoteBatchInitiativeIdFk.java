package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QSupportVoteBatchInitiativeIdFk is a Querydsl query type for QSupportVoteBatchInitiativeIdFk
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QSupportVoteBatchInitiativeIdFk extends com.mysema.query.sql.RelationalPathBase<QSupportVoteBatchInitiativeIdFk> {

    private static final long serialVersionUID = 1476253825;

    public static final QSupportVoteBatchInitiativeIdFk supportVoteBatchInitiativeIdFk = new QSupportVoteBatchInitiativeIdFk("support_vote_batch_initiative_id_fk");

    public final NumberPath<Long> initiativeId = createNumber("initiative_id", Long.class);

    public QSupportVoteBatchInitiativeIdFk(String variable) {
        super(QSupportVoteBatchInitiativeIdFk.class, forVariable(variable), "initiative", "support_vote_batch_initiative_id_fk");
    }

    public QSupportVoteBatchInitiativeIdFk(Path<? extends QSupportVoteBatchInitiativeIdFk> path) {
        super(path.getType(), path.getMetadata(), "initiative", "support_vote_batch_initiative_id_fk");
    }

    public QSupportVoteBatchInitiativeIdFk(PathMetadata<?> metadata) {
        super(QSupportVoteBatchInitiativeIdFk.class, metadata, "initiative", "support_vote_batch_initiative_id_fk");
    }

}

