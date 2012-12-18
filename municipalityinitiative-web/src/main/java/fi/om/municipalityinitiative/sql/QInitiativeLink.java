package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QInitiativeLink is a Querydsl query type for QInitiativeLink
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QInitiativeLink extends com.mysema.query.sql.RelationalPathBase<QInitiativeLink> {

    private static final long serialVersionUID = 173722486;

    public static final QInitiativeLink initiativeLink = new QInitiativeLink("initiative_link");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> initiativeId = createNumber("initiative_id", Long.class);

    public final StringPath label = createString("label");

    public final StringPath uri = createString("uri");

    public final com.mysema.query.sql.PrimaryKey<QInitiativeLink> linkPk = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<QInitiative> linkInitiativeIdFk = createForeignKey(initiativeId, "id");

    public QInitiativeLink(String variable) {
        super(QInitiativeLink.class, forVariable(variable), "municipalityinitiative", "initiative_link");
    }

    public QInitiativeLink(Path<? extends QInitiativeLink> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "initiative_link");
    }

    public QInitiativeLink(PathMetadata<?> metadata) {
        super(QInitiativeLink.class, metadata, "municipalityinitiative", "initiative_link");
    }

}

