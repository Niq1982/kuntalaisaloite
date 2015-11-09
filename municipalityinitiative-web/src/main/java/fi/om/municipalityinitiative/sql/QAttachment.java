package fi.om.municipalityinitiative.sql;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BooleanPath;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QAttachment is a Querydsl query type for QAttachment
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QAttachment extends com.mysema.query.sql.RelationalPathBase<QAttachment> {

    private static final long serialVersionUID = -376397709;

    public static final QAttachment attachment = new QAttachment("attachment");

    public final BooleanPath accepted = createBoolean("accepted");

    public final DateTimePath<org.joda.time.DateTime> added = createDateTime("added", org.joda.time.DateTime.class);

    public final StringPath contentType = createString("contentType");

    public final StringPath description = createString("description");

    public final StringPath fileType = createString("fileType");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> initiativeId = createNumber("initiativeId", Long.class);

    public final com.mysema.query.sql.PrimaryKey<QAttachment> attachmentPk = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<QMunicipalityInitiative> attachmentInitiativeId = createForeignKey(initiativeId, "id");

    public QAttachment(String variable) {
        super(QAttachment.class,  forVariable(variable), "municipalityinitiative", "attachment");
        addMetadata();
    }

    public QAttachment(Path<? extends QAttachment> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "attachment");
        addMetadata();
    }

    public QAttachment(PathMetadata<?> metadata) {
        super(QAttachment.class,  metadata, "municipalityinitiative", "attachment");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(accepted, ColumnMetadata.named("accepted").ofType(-7).withSize(1).notNull());
        addMetadata(added, ColumnMetadata.named("added").ofType(93).withSize(29).withDigits(6));
        addMetadata(contentType, ColumnMetadata.named("content_type").ofType(12).withSize(20).notNull());
        addMetadata(description, ColumnMetadata.named("description").ofType(12).withSize(100).notNull());
        addMetadata(fileType, ColumnMetadata.named("file_type").ofType(12).withSize(4).notNull());
        addMetadata(id, ColumnMetadata.named("id").ofType(-5).withSize(19).notNull());
        addMetadata(initiativeId, ColumnMetadata.named("initiative_id").ofType(-5).withSize(19).notNull());
    }

}

