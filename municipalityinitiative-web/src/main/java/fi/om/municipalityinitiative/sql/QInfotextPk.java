package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QInfotextPk is a Querydsl query type for QInfotextPk
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QInfotextPk extends com.mysema.query.sql.RelationalPathBase<QInfotextPk> {

    private static final long serialVersionUID = 1701161350;

    public static final QInfotextPk infotextPk = new QInfotextPk("infotext_pk");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QInfotextPk(String variable) {
        super(QInfotextPk.class, forVariable(variable), "municipalityinitiative", "infotext_pk");
    }

    public QInfotextPk(Path<? extends QInfotextPk> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "infotext_pk");
    }

    public QInfotextPk(PathMetadata<?> metadata) {
        super(QInfotextPk.class, metadata, "municipalityinitiative", "infotext_pk");
    }

}

