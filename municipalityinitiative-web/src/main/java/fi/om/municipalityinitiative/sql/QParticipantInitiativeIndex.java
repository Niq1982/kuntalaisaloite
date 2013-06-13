package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QParticipantInitiativeIndex is a Querydsl query type for QParticipantInitiativeIndex
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QParticipantInitiativeIndex extends com.mysema.query.sql.RelationalPathBase<QParticipantInitiativeIndex> {

    private static final long serialVersionUID = -119507517;

    public static final QParticipantInitiativeIndex participantInitiativeIndex = new QParticipantInitiativeIndex("participant_initiative_index");

    public final NumberPath<Long> municipalityInitiativeId = createNumber("municipality_initiative_id", Long.class);

    public QParticipantInitiativeIndex(String variable) {
        super(QParticipantInitiativeIndex.class, forVariable(variable), "municipalityinitiative", "participant_initiative_index");
    }

    public QParticipantInitiativeIndex(Path<? extends QParticipantInitiativeIndex> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "participant_initiative_index");
    }

    public QParticipantInitiativeIndex(PathMetadata<?> metadata) {
        super(QParticipantInitiativeIndex.class, metadata, "municipalityinitiative", "participant_initiative_index");
    }

}

