package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QAttachmentInitiativeIdIndex is a Querydsl query type for QAttachmentInitiativeIdIndex
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QAttachmentInitiativeIdIndex extends com.mysema.query.sql.RelationalPathBase<QAttachmentInitiativeIdIndex> {

    private static final long serialVersionUID = -2087002984;

    public static final QAttachmentInitiativeIdIndex attachmentInitiativeIdIndex = new QAttachmentInitiativeIdIndex("attachment_initiative_id_index");

    public final NumberPath<Long> initiativeId = createNumber("initiative_id", Long.class);

    public QAttachmentInitiativeIdIndex(String variable) {
        super(QAttachmentInitiativeIdIndex.class, forVariable(variable), "municipalityinitiative", "attachment_initiative_id_index");
    }

    public QAttachmentInitiativeIdIndex(Path<? extends QAttachmentInitiativeIdIndex> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "attachment_initiative_id_index");
    }

    public QAttachmentInitiativeIdIndex(PathMetadata<?> metadata) {
        super(QAttachmentInitiativeIdIndex.class, metadata, "municipalityinitiative", "attachment_initiative_id_index");
    }

}

