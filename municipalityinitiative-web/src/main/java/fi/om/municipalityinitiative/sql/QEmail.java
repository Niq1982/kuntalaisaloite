package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * QEmail is a Querydsl query type for QEmail
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QEmail extends com.mysema.query.sql.RelationalPathBase<QEmail> {

    private static final long serialVersionUID = 2091758156;

    public static final QEmail email = new QEmail("email");

    public final EnumPath<fi.om.municipalityinitiative.util.EmailAttachmentType> attachment = createEnum("attachment", fi.om.municipalityinitiative.util.EmailAttachmentType.class);

    public final StringPath bodyHtml = createString("bodyHtml");

    public final StringPath bodyText = createString("bodyText");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> initiativeId = createNumber("initiativeId", Long.class);

    public final DateTimePath<org.joda.time.DateTime> lastFailed = createDateTime("lastFailed", org.joda.time.DateTime.class);

    public final StringPath recipients = createString("recipients");

    public final StringPath replyTo = createString("replyTo");

    public final StringPath sender = createString("sender");

    public final StringPath subject = createString("subject");

    public final DateTimePath<org.joda.time.DateTime> succeeded = createDateTime("succeeded", org.joda.time.DateTime.class);

    public final BooleanPath tried = createBoolean("tried");

    public final com.mysema.query.sql.PrimaryKey<QEmail> emailPk = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<QMunicipalityInitiative> emailInitiativeId = createForeignKey(initiativeId, "id");

    public QEmail(String variable) {
        super(QEmail.class,  forVariable(variable), "municipalityinitiative", "email");
        addMetadata();
    }

    public QEmail(Path<? extends QEmail> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "email");
        addMetadata();
    }

    public QEmail(PathMetadata<?> metadata) {
        super(QEmail.class,  metadata, "municipalityinitiative", "email");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(attachment, ColumnMetadata.named("attachment").ofType(1111).withSize(2147483647));
        addMetadata(bodyHtml, ColumnMetadata.named("body_html").ofType(12).withSize(2147483647).notNull());
        addMetadata(bodyText, ColumnMetadata.named("body_text").ofType(12).withSize(2147483647).notNull());
        addMetadata(id, ColumnMetadata.named("id").ofType(-5).withSize(19).notNull());
        addMetadata(initiativeId, ColumnMetadata.named("initiative_id").ofType(-5).withSize(19).notNull());
        addMetadata(lastFailed, ColumnMetadata.named("last_failed").ofType(93).withSize(29).withDigits(6));
        addMetadata(recipients, ColumnMetadata.named("recipients").ofType(12).withSize(1024).notNull());
        addMetadata(replyTo, ColumnMetadata.named("reply_to").ofType(12).withSize(50).notNull());
        addMetadata(sender, ColumnMetadata.named("sender").ofType(12).withSize(50).notNull());
        addMetadata(subject, ColumnMetadata.named("subject").ofType(12).withSize(1024).notNull());
        addMetadata(succeeded, ColumnMetadata.named("succeeded").ofType(93).withSize(29).withDigits(6));
        addMetadata(tried, ColumnMetadata.named("tried").ofType(-7).withSize(1).notNull());
    }

}

