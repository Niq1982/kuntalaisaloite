package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QInitiativeInvitation is a Querydsl query type for QInitiativeInvitation
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QInitiativeInvitation extends com.mysema.query.sql.RelationalPathBase<QInitiativeInvitation> {

    private static final long serialVersionUID = -894763239;

    public static final QInitiativeInvitation initiativeInvitation = new QInitiativeInvitation("initiative_invitation");

    public final DateTimePath<org.joda.time.DateTime> created = createDateTime("created", org.joda.time.DateTime.class);

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> initiativeId = createNumber("initiative_id", Long.class);

    public final StringPath invitationcode = createString("invitationcode");

    public final EnumPath<fi.om.municipalityinitiative.dto.AuthorRole> role = createEnum("role", fi.om.municipalityinitiative.dto.AuthorRole.class);

    public final DateTimePath<org.joda.time.DateTime> sent = createDateTime("sent", org.joda.time.DateTime.class);

    public final com.mysema.query.sql.PrimaryKey<QInitiativeInvitation> invitationPk = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<QInitiative> invitationInitiativeIdFk = createForeignKey(initiativeId, "id");

    public QInitiativeInvitation(String variable) {
        super(QInitiativeInvitation.class, forVariable(variable), "initiative", "initiative_invitation");
    }

    public QInitiativeInvitation(Path<? extends QInitiativeInvitation> path) {
        super(path.getType(), path.getMetadata(), "initiative", "initiative_invitation");
    }

    public QInitiativeInvitation(PathMetadata<?> metadata) {
        super(QInitiativeInvitation.class, metadata, "initiative", "initiative_invitation");
    }

}

