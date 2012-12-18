package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QSupportVoteBatchPk is a Querydsl query type for QSupportVoteBatchPk
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QSupportVoteBatchPk extends com.mysema.query.sql.RelationalPathBase<QSupportVoteBatchPk> {

    private static final long serialVersionUID = 1035566220;

    public static final QSupportVoteBatchPk supportVoteBatchPk = new QSupportVoteBatchPk("support_vote_batch_pk");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QSupportVoteBatchPk(String variable) {
        super(QSupportVoteBatchPk.class, forVariable(variable), "municipalityinitiative", "support_vote_batch_pk");
    }

    public QSupportVoteBatchPk(Path<? extends QSupportVoteBatchPk> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "support_vote_batch_pk");
    }

    public QSupportVoteBatchPk(PathMetadata<?> metadata) {
        super(QSupportVoteBatchPk.class, metadata, "municipalityinitiative", "support_vote_batch_pk");
    }

}

