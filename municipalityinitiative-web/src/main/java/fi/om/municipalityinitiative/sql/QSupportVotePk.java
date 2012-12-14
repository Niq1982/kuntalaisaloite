package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QSupportVotePk is a Querydsl query type for QSupportVotePk
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QSupportVotePk extends com.mysema.query.sql.RelationalPathBase<QSupportVotePk> {

    private static final long serialVersionUID = -820807328;

    public static final QSupportVotePk supportVotePk = new QSupportVotePk("support_vote_pk");

    public final NumberPath<Long> initiativeId = createNumber("initiative_id", Long.class);

    public final StringPath supportid = createString("supportid");

    public QSupportVotePk(String variable) {
        super(QSupportVotePk.class, forVariable(variable), "initiative", "support_vote_pk");
    }

    public QSupportVotePk(Path<? extends QSupportVotePk> path) {
        super(path.getType(), path.getMetadata(), "initiative", "support_vote_pk");
    }

    public QSupportVotePk(PathMetadata<?> metadata) {
        super(QSupportVotePk.class, metadata, "initiative", "support_vote_pk");
    }

}

