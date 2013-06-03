package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QAuthorMessage is a Querydsl query type for QAuthorMessage
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QAuthorMessage extends com.mysema.query.sql.RelationalPathBase<QAuthorMessage> {

    private static final long serialVersionUID = 1351903916;

    public static final QAuthorMessage authorMessage = new QAuthorMessage("author_message");

    public final StringPath confirmationCode = createString("confirmation_code");

    public final StringPath contactor = createString("contactor");

    public final StringPath contactorEmail = createString("contactor_email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> initiativeId = createNumber("initiative_id", Long.class);

    public final StringPath message = createString("message");

    public final com.mysema.query.sql.PrimaryKey<QAuthorMessage> authormessagePk = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<QMunicipalityInitiative> authormessageInitiativeidFk = createForeignKey(initiativeId, "id");

    public QAuthorMessage(String variable) {
        super(QAuthorMessage.class, forVariable(variable), "municipalityinitiative", "author_message");
    }

    public QAuthorMessage(Path<? extends QAuthorMessage> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "author_message");
    }

    public QAuthorMessage(PathMetadata<?> metadata) {
        super(QAuthorMessage.class, metadata, "municipalityinitiative", "author_message");
    }

}

