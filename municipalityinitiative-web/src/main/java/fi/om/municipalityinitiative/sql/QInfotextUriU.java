package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QInfotextUriU is a Querydsl query type for QInfotextUriU
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QInfotextUriU extends com.mysema.query.sql.RelationalPathBase<QInfotextUriU> {

    private static final long serialVersionUID = -1566323404;

    public static final QInfotextUriU infotextUriU = new QInfotextUriU("infotext_uri_u");

    public final StringPath uri = createString("uri");

    public QInfotextUriU(String variable) {
        super(QInfotextUriU.class, forVariable(variable), "municipalityinitiative", "infotext_uri_u");
    }

    public QInfotextUriU(Path<? extends QInfotextUriU> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "infotext_uri_u");
    }

    public QInfotextUriU(PathMetadata<?> metadata) {
        super(QInfotextUriU.class, metadata, "municipalityinitiative", "infotext_uri_u");
    }

}

