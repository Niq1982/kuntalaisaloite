package fi.om.municipalityinitiative.sql;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.NumberPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QVerifiedParticipantUserIdIndex is a Querydsl query type for QVerifiedParticipantUserIdIndex
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QVerifiedParticipantUserIdIndex extends com.mysema.query.sql.RelationalPathBase<QVerifiedParticipantUserIdIndex> {

    private static final long serialVersionUID = 968611121;

    public static final QVerifiedParticipantUserIdIndex verifiedParticipantUserIdIndex = new QVerifiedParticipantUserIdIndex("verified_participant_user_id_index");

    public final NumberPath<Long> verifiedUserId = createNumber("verified_user_id", Long.class);

    public QVerifiedParticipantUserIdIndex(String variable) {
        super(QVerifiedParticipantUserIdIndex.class, forVariable(variable), "municipalityinitiative", "verified_participant_user_id_index");
    }

    public QVerifiedParticipantUserIdIndex(Path<? extends QVerifiedParticipantUserIdIndex> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "verified_participant_user_id_index");
    }

    public QVerifiedParticipantUserIdIndex(PathMetadata<?> metadata) {
        super(QVerifiedParticipantUserIdIndex.class, metadata, "municipalityinitiative", "verified_participant_user_id_index");
    }

}

