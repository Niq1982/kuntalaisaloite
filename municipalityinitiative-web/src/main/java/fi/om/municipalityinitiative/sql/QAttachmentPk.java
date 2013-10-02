package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QAttachmentPk is a Querydsl query type for QAttachmentPk
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QAttachmentPk extends com.mysema.query.sql.RelationalPathBase<QAttachmentPk> {

    private static final long serialVersionUID = -940942898;

    public static final QAttachmentPk attachmentPk = new QAttachmentPk("attachment_pk");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QAttachmentPk(String variable) {
        super(QAttachmentPk.class, forVariable(variable), "municipalityinitiative", "attachment_pk");
    }

    public QAttachmentPk(Path<? extends QAttachmentPk> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "attachment_pk");
    }

    public QAttachmentPk(PathMetadata<?> metadata) {
        super(QAttachmentPk.class, metadata, "municipalityinitiative", "attachment_pk");
    }

}

