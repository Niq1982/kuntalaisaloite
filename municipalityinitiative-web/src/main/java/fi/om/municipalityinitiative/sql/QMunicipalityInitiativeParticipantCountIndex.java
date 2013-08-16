package fi.om.municipalityinitiative.sql;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.NumberPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QMunicipalityInitiativeParticipantCountIndex is a Querydsl query type for QMunicipalityInitiativeParticipantCountIndex
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QMunicipalityInitiativeParticipantCountIndex extends com.mysema.query.sql.RelationalPathBase<QMunicipalityInitiativeParticipantCountIndex> {

    private static final long serialVersionUID = -1824634754;

    public static final QMunicipalityInitiativeParticipantCountIndex municipalityInitiativeParticipantCountIndex = new QMunicipalityInitiativeParticipantCountIndex("municipality_initiative_participant_count_index");

    public final NumberPath<Integer> participantCount = createNumber("participant_count", Integer.class);

    public QMunicipalityInitiativeParticipantCountIndex(String variable) {
        super(QMunicipalityInitiativeParticipantCountIndex.class, forVariable(variable), "municipalityinitiative", "municipality_initiative_participant_count_index");
    }

    public QMunicipalityInitiativeParticipantCountIndex(Path<? extends QMunicipalityInitiativeParticipantCountIndex> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "municipality_initiative_participant_count_index");
    }

    public QMunicipalityInitiativeParticipantCountIndex(PathMetadata<?> metadata) {
        super(QMunicipalityInitiativeParticipantCountIndex.class, metadata, "municipalityinitiative", "municipality_initiative_participant_count_index");
    }

}

