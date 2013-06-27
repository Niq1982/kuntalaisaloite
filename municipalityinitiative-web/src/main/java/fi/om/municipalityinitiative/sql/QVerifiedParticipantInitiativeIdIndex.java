package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QVerifiedParticipantInitiativeIdIndex is a Querydsl query type for QVerifiedParticipantInitiativeIdIndex
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QVerifiedParticipantInitiativeIdIndex extends com.mysema.query.sql.RelationalPathBase<QVerifiedParticipantInitiativeIdIndex> {

    private static final long serialVersionUID = -941531120;

    public static final QVerifiedParticipantInitiativeIdIndex verifiedParticipantInitiativeIdIndex = new QVerifiedParticipantInitiativeIdIndex("verified_participant_initiative_id_index");

    public final NumberPath<Long> initiativeId = createNumber("initiative_id", Long.class);

    public QVerifiedParticipantInitiativeIdIndex(String variable) {
        super(QVerifiedParticipantInitiativeIdIndex.class, forVariable(variable), "municipalityinitiative", "verified_participant_initiative_id_index");
    }

    public QVerifiedParticipantInitiativeIdIndex(Path<? extends QVerifiedParticipantInitiativeIdIndex> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "verified_participant_initiative_id_index");
    }

    public QVerifiedParticipantInitiativeIdIndex(PathMetadata<?> metadata) {
        super(QVerifiedParticipantInitiativeIdIndex.class, metadata, "municipalityinitiative", "verified_participant_initiative_id_index");
    }

}

