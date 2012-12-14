package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QSupportVoteBatchIdFk is a Querydsl query type for QSupportVoteBatchIdFk
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QSupportVoteBatchIdFk extends com.mysema.query.sql.RelationalPathBase<QSupportVoteBatchIdFk> {

    private static final long serialVersionUID = -2067250539;

    public static final QSupportVoteBatchIdFk supportVoteBatchIdFk = new QSupportVoteBatchIdFk("support_vote_batch_id_fk");

    public final NumberPath<Long> batchId = createNumber("batch_id", Long.class);

    public QSupportVoteBatchIdFk(String variable) {
        super(QSupportVoteBatchIdFk.class, forVariable(variable), "initiative", "support_vote_batch_id_fk");
    }

    public QSupportVoteBatchIdFk(Path<? extends QSupportVoteBatchIdFk> path) {
        super(path.getType(), path.getMetadata(), "initiative", "support_vote_batch_id_fk");
    }

    public QSupportVoteBatchIdFk(PathMetadata<?> metadata) {
        super(QSupportVoteBatchIdFk.class, metadata, "initiative", "support_vote_batch_id_fk");
    }

}

