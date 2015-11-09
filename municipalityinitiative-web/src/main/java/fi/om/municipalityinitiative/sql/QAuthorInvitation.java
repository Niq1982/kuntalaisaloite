package fi.om.municipalityinitiative.sql;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QAuthorInvitation is a Querydsl query type for QAuthorInvitation
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QAuthorInvitation extends com.mysema.query.sql.RelationalPathBase<QAuthorInvitation> {

    private static final long serialVersionUID = -747581740;

    public static final QAuthorInvitation authorInvitation = new QAuthorInvitation("author_invitation");

    public final StringPath confirmationCode = createString("confirmationCode");

    public final StringPath email = createString("email");

    public final NumberPath<Long> initiativeId = createNumber("initiativeId", Long.class);

    public final DateTimePath<org.joda.time.DateTime> invitationTime = createDateTime("invitationTime", org.joda.time.DateTime.class);

    public final StringPath name = createString("name");

    public final DateTimePath<org.joda.time.DateTime> rejectTime = createDateTime("rejectTime", org.joda.time.DateTime.class);

    public final com.mysema.query.sql.PrimaryKey<QAuthorInvitation> authorInvitationPk = createPrimaryKey(initiativeId, confirmationCode);

    public final com.mysema.query.sql.ForeignKey<QMunicipalityInitiative> authorInvitationInitiativeIdFk = createForeignKey(initiativeId, "id");

    public QAuthorInvitation(String variable) {
        super(QAuthorInvitation.class,  forVariable(variable), "municipalityinitiative", "author_invitation");
        addMetadata();
    }

    public QAuthorInvitation(Path<? extends QAuthorInvitation> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "author_invitation");
        addMetadata();
    }

    public QAuthorInvitation(PathMetadata<?> metadata) {
        super(QAuthorInvitation.class,  metadata, "municipalityinitiative", "author_invitation");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(confirmationCode, ColumnMetadata.named("confirmation_code").ofType(12).withSize(20).notNull());
        addMetadata(email, ColumnMetadata.named("email").ofType(12).withSize(100).notNull());
        addMetadata(initiativeId, ColumnMetadata.named("initiative_id").ofType(-5).withSize(19).notNull());
        addMetadata(invitationTime, ColumnMetadata.named("invitation_time").ofType(93).withSize(29).withDigits(6).notNull());
        addMetadata(name, ColumnMetadata.named("name").ofType(12).withSize(100).notNull());
        addMetadata(rejectTime, ColumnMetadata.named("reject_time").ofType(93).withSize(29).withDigits(6));
    }

}

