package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QVerifiedParticipantInitiativeIdOrderedIndex is a Querydsl query type for QVerifiedParticipantInitiativeIdOrderedIndex
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QVerifiedParticipantInitiativeIdOrderedIndex extends com.mysema.query.sql.RelationalPathBase<QVerifiedParticipantInitiativeIdOrderedIndex> {

    private static final long serialVersionUID = 1477264231;

    public static final QVerifiedParticipantInitiativeIdOrderedIndex verifiedParticipantInitiativeIdOrderedIndex = new QVerifiedParticipantInitiativeIdOrderedIndex("verified_participant_initiative_id_ordered_index");

    public final NumberPath<Long> initiativeId = createNumber("initiative_id", Long.class);

    public final DatePath<org.joda.time.LocalDate> participateTime = createDate("participate_time", org.joda.time.LocalDate.class);

    public QVerifiedParticipantInitiativeIdOrderedIndex(String variable) {
        super(QVerifiedParticipantInitiativeIdOrderedIndex.class, forVariable(variable), "municipalityinitiative", "verified_participant_initiative_id_ordered_index");
    }

    public QVerifiedParticipantInitiativeIdOrderedIndex(Path<? extends QVerifiedParticipantInitiativeIdOrderedIndex> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "verified_participant_initiative_id_ordered_index");
    }

    public QVerifiedParticipantInitiativeIdOrderedIndex(PathMetadata<?> metadata) {
        super(QVerifiedParticipantInitiativeIdOrderedIndex.class, metadata, "municipalityinitiative", "verified_participant_initiative_id_ordered_index");
    }

}

