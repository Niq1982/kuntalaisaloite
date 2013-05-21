package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QInfotextOrderpositionU is a Querydsl query type for QInfotextOrderpositionU
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QInfotextOrderpositionU extends com.mysema.query.sql.RelationalPathBase<QInfotextOrderpositionU> {

    private static final long serialVersionUID = 1603338185;

    public static final QInfotextOrderpositionU infotextOrderpositionU = new QInfotextOrderpositionU("infotext_orderposition_u");

    public final SimplePath<Object> category = createSimple("category", Object.class);

    public final SimplePath<Object> languagecode = createSimple("languagecode", Object.class);

    public final NumberPath<Integer> orderposition = createNumber("orderposition", Integer.class);

    public QInfotextOrderpositionU(String variable) {
        super(QInfotextOrderpositionU.class, forVariable(variable), "municipalityinitiative", "infotext_orderposition_u");
    }

    public QInfotextOrderpositionU(Path<? extends QInfotextOrderpositionU> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "infotext_orderposition_u");
    }

    public QInfotextOrderpositionU(PathMetadata<?> metadata) {
        super(QInfotextOrderpositionU.class, metadata, "municipalityinitiative", "infotext_orderposition_u");
    }

}

