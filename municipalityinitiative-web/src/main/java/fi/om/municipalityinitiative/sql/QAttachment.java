package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QAttachment is a Querydsl query type for QAttachment
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QAttachment extends com.mysema.query.sql.RelationalPathBase<QAttachment> {

    private static final long serialVersionUID = -376397709;

    public static final QAttachment attachment = new QAttachment("attachment");

    public final BooleanPath accepted = createBoolean("accepted");

    public final DateTimePath<org.joda.time.DateTime> added = createDateTime("added", org.joda.time.DateTime.class);

    public final StringPath contentType = createString("content_type");

    public final StringPath filename = createString("filename");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> initiativeId = createNumber("initiative_id", Long.class);

    public final com.mysema.query.sql.PrimaryKey<QAttachment> attachmentPk = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<QMunicipalityInitiative> attachmentInitiativeId = createForeignKey(initiativeId, "id");

    public QAttachment(String variable) {
        super(QAttachment.class, forVariable(variable), "municipalityinitiative", "attachment");
    }

    public QAttachment(Path<? extends QAttachment> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "attachment");
    }

    public QAttachment(PathMetadata<?> metadata) {
        super(QAttachment.class, metadata, "municipalityinitiative", "attachment");
    }

}

