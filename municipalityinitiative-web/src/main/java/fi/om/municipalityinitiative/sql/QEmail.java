package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QEmail is a Querydsl query type for QEmail
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QEmail extends com.mysema.query.sql.RelationalPathBase<QEmail> {

    private static final long serialVersionUID = 2091758156;

    public static final QEmail email = new QEmail("email");

    public final EnumPath<fi.om.municipalityinitiative.util.EmailAttachmentType> attachment = createEnum("attachment", fi.om.municipalityinitiative.util.EmailAttachmentType.class);

    public final StringPath bodyHtml = createString("body_html");

    public final StringPath bodyText = createString("body_text");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> initiativeId = createNumber("initiative_id", Long.class);

    public final DateTimePath<org.joda.time.DateTime> lastFailed = createDateTime("last_failed", org.joda.time.DateTime.class);

    public final StringPath recipients = createString("recipients");

    public final StringPath replyTo = createString("reply_to");

    public final StringPath sender = createString("sender");

    public final StringPath subject = createString("subject");

    public final DateTimePath<org.joda.time.DateTime> succeeded = createDateTime("succeeded", org.joda.time.DateTime.class);

    public final BooleanPath tried = createBoolean("tried");

    public final com.mysema.query.sql.PrimaryKey<QEmail> emailPk = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<QMunicipalityInitiative> emailInitiativeId = createForeignKey(initiativeId, "id");

    public QEmail(String variable) {
        super(QEmail.class, forVariable(variable), "municipalityinitiative", "email");
    }

    public QEmail(Path<? extends QEmail> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "email");
    }

    public QEmail(PathMetadata<?> metadata) {
        super(QEmail.class, metadata, "municipalityinitiative", "email");
    }

}

