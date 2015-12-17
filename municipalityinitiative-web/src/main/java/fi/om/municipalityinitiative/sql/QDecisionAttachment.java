package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * QDecisionAttachment is a Querydsl query type for QDecisionAttachment
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QDecisionAttachment extends com.mysema.query.sql.RelationalPathBase<QDecisionAttachment> {

    private static final long serialVersionUID = 1419852143;

    public static final QDecisionAttachment decisionAttachment = new QDecisionAttachment("decision_attachment");

    public final DateTimePath<org.joda.time.DateTime> added = createDateTime("added", org.joda.time.DateTime.class);

    public final StringPath contentType = createString("contentType");

    public final StringPath description = createString("description");

    public final StringPath fileType = createString("fileType");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> initiativeId = createNumber("initiativeId", Long.class);

    public final com.mysema.query.sql.PrimaryKey<QDecisionAttachment> decisionAttachmentPk = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<QMunicipalityInitiative> decisionAttachmentInitiativeId = createForeignKey(initiativeId, "id");

    public QDecisionAttachment(String variable) {
        super(QDecisionAttachment.class,  forVariable(variable), "municipalityinitiative", "decision_attachment");
        addMetadata();
    }

    public QDecisionAttachment(Path<? extends QDecisionAttachment> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "decision_attachment");
        addMetadata();
    }

    public QDecisionAttachment(PathMetadata<?> metadata) {
        super(QDecisionAttachment.class,  metadata, "municipalityinitiative", "decision_attachment");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(added, ColumnMetadata.named("added").ofType(93).withSize(29).withDigits(6));
        addMetadata(contentType, ColumnMetadata.named("content_type").ofType(12).withSize(20).notNull());
        addMetadata(description, ColumnMetadata.named("description").ofType(12).withSize(100).notNull());
        addMetadata(fileType, ColumnMetadata.named("file_type").ofType(12).withSize(4).notNull());
        addMetadata(id, ColumnMetadata.named("id").ofType(-5).withSize(19).notNull());
        addMetadata(initiativeId, ColumnMetadata.named("initiative_id").ofType(-5).withSize(19).notNull());
    }

}

