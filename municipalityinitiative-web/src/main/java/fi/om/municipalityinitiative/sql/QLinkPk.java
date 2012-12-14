package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QLinkPk is a Querydsl query type for QLinkPk
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QLinkPk extends com.mysema.query.sql.RelationalPathBase<QLinkPk> {

    private static final long serialVersionUID = -1838849303;

    public static final QLinkPk linkPk = new QLinkPk("link_pk");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QLinkPk(String variable) {
        super(QLinkPk.class, forVariable(variable), "initiative", "link_pk");
    }

    public QLinkPk(Path<? extends QLinkPk> path) {
        super(path.getType(), path.getMetadata(), "initiative", "link_pk");
    }

    public QLinkPk(PathMetadata<?> metadata) {
        super(QLinkPk.class, metadata, "initiative", "link_pk");
    }

}

