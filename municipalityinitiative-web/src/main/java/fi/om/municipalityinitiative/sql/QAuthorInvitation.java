package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QAuthorInvitation is a Querydsl query type for QAuthorInvitation
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QAuthorInvitation extends com.mysema.query.sql.RelationalPathBase<QAuthorInvitation> {

    private static final long serialVersionUID = -747581740;

    public static final QAuthorInvitation authorInvitation = new QAuthorInvitation("author_invitation");

    public final StringPath confirmationCode = createString("confirmation_code");

    public final StringPath email = createString("email");

    public final NumberPath<Long> initiativeId = createNumber("initiative_id", Long.class);

    public final DateTimePath<org.joda.time.DateTime> invitationTime = createDateTime("invitation_time", org.joda.time.DateTime.class);

    public final StringPath name = createString("name");

    public final DateTimePath<org.joda.time.DateTime> rejectTime = createDateTime("reject_time", org.joda.time.DateTime.class);

    public final com.mysema.query.sql.PrimaryKey<QAuthorInvitation> authorInvitationPk = createPrimaryKey(initiativeId, confirmationCode);

    public final com.mysema.query.sql.ForeignKey<QMunicipalityInitiative> authorInvitationInitiativeIdFk = createForeignKey(initiativeId, "id");

    public QAuthorInvitation(String variable) {
        super(QAuthorInvitation.class, forVariable(variable), "municipalityinitiative", "author_invitation");
    }

    public QAuthorInvitation(Path<? extends QAuthorInvitation> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "author_invitation");
    }

    public QAuthorInvitation(PathMetadata<?> metadata) {
        super(QAuthorInvitation.class, metadata, "municipalityinitiative", "author_invitation");
    }

}

