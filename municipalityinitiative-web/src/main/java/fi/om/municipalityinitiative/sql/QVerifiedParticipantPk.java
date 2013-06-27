package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QVerifiedParticipantPk is a Querydsl query type for QVerifiedParticipantPk
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QVerifiedParticipantPk extends com.mysema.query.sql.RelationalPathBase<QVerifiedParticipantPk> {

    private static final long serialVersionUID = 126637398;

    public static final QVerifiedParticipantPk verifiedParticipantPk = new QVerifiedParticipantPk("verified_participant_pk");

    public final NumberPath<Long> initiativeId = createNumber("initiative_id", Long.class);

    public final NumberPath<Long> verifiedUserId = createNumber("verified_user_id", Long.class);

    public QVerifiedParticipantPk(String variable) {
        super(QVerifiedParticipantPk.class, forVariable(variable), "municipalityinitiative", "verified_participant_pk");
    }

    public QVerifiedParticipantPk(Path<? extends QVerifiedParticipantPk> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "verified_participant_pk");
    }

    public QVerifiedParticipantPk(PathMetadata<?> metadata) {
        super(QVerifiedParticipantPk.class, metadata, "municipalityinitiative", "verified_participant_pk");
    }

}

