package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * QAuthorMessage is a Querydsl query type for QAuthorMessage
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QAuthorMessage extends com.mysema.query.sql.RelationalPathBase<QAuthorMessage> {

    private static final long serialVersionUID = 1351903916;

    public static final QAuthorMessage authorMessage = new QAuthorMessage("author_message");

    public final StringPath confirmationCode = createString("confirmationCode");

    public final StringPath contactor = createString("contactor");

    public final StringPath contactorEmail = createString("contactorEmail");

    public final NumberPath<Long> initiativeId = createNumber("initiativeId", Long.class);

    public final StringPath message = createString("message");

    public final com.mysema.query.sql.PrimaryKey<QAuthorMessage> authormessagePk = createPrimaryKey(confirmationCode);

    public final com.mysema.query.sql.ForeignKey<QMunicipalityInitiative> authormessageInitiativeidFk = createForeignKey(initiativeId, "id");

    public QAuthorMessage(String variable) {
        super(QAuthorMessage.class,  forVariable(variable), "municipalityinitiative", "author_message");
        addMetadata();
    }

    public QAuthorMessage(Path<? extends QAuthorMessage> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "author_message");
        addMetadata();
    }

    public QAuthorMessage(PathMetadata<?> metadata) {
        super(QAuthorMessage.class,  metadata, "municipalityinitiative", "author_message");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(confirmationCode, ColumnMetadata.named("confirmation_code").ofType(12).withSize(40).notNull());
        addMetadata(contactor, ColumnMetadata.named("contactor").ofType(12).withSize(100).notNull());
        addMetadata(contactorEmail, ColumnMetadata.named("contactor_email").ofType(12).withSize(100).notNull());
        addMetadata(initiativeId, ColumnMetadata.named("initiative_id").ofType(-5).withSize(19).notNull());
        addMetadata(message, ColumnMetadata.named("message").ofType(12).withSize(2048).notNull());
    }

}

