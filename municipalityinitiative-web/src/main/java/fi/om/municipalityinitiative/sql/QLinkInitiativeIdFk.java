package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QLinkInitiativeIdFk is a Querydsl query type for QLinkInitiativeIdFk
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QLinkInitiativeIdFk extends com.mysema.query.sql.RelationalPathBase<QLinkInitiativeIdFk> {

    private static final long serialVersionUID = -67490474;

    public static final QLinkInitiativeIdFk linkInitiativeIdFk = new QLinkInitiativeIdFk("link_initiative_id_fk");

    public final NumberPath<Long> initiativeId = createNumber("initiative_id", Long.class);

    public QLinkInitiativeIdFk(String variable) {
        super(QLinkInitiativeIdFk.class, forVariable(variable), "municipalityinitiative", "link_initiative_id_fk");
    }

    public QLinkInitiativeIdFk(Path<? extends QLinkInitiativeIdFk> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "link_initiative_id_fk");
    }

    public QLinkInitiativeIdFk(PathMetadata<?> metadata) {
        super(QLinkInitiativeIdFk.class, metadata, "municipalityinitiative", "link_initiative_id_fk");
    }

}

