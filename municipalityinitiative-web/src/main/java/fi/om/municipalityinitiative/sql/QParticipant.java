package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QParticipant is a Querydsl query type for QParticipant
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QParticipant extends com.mysema.query.sql.RelationalPathBase<QParticipant> {

    private static final long serialVersionUID = -1571974685;

    public static final QParticipant participant = new QParticipant("participant");

    public final BooleanPath franchise = createBoolean("franchise");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> municipalityId = createNumber("municipality_id", Long.class);

    public final NumberPath<Long> municipalityInitiativeId = createNumber("municipality_initiative_id", Long.class);

    public final StringPath name = createString("name");

    public final DatePath<org.joda.time.LocalDate> participateTime = createDate("participate_time", org.joda.time.LocalDate.class);

    public final BooleanPath showName = createBoolean("show_name");

    public final com.mysema.query.sql.PrimaryKey<QParticipant> participantPk = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<QMunicipality> participantMunicipalityFk = createForeignKey(municipalityId, "id");

    public final com.mysema.query.sql.ForeignKey<QMunicipalityInitiative> participantMunicipalityInitiativeIdFk = createForeignKey(municipalityInitiativeId, "id");

    public final com.mysema.query.sql.ForeignKey<QAuthor> _authorParticipantFk = createInvForeignKey(id, "participant_id");

    public QParticipant(String variable) {
        super(QParticipant.class, forVariable(variable), "municipalityinitiative", "participant");
    }

    public QParticipant(Path<? extends QParticipant> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "participant");
    }

    public QParticipant(PathMetadata<?> metadata) {
        super(QParticipant.class, metadata, "municipalityinitiative", "participant");
    }

}

