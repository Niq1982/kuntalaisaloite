package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QEmailPk is a Querydsl query type for QEmailPk
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QEmailPk extends com.mysema.query.sql.RelationalPathBase<QEmailPk> {

    private static final long serialVersionUID = 134895975;

    public static final QEmailPk emailPk = new QEmailPk("email_pk");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QEmailPk(String variable) {
        super(QEmailPk.class, forVariable(variable), "municipalityinitiative", "email_pk");
    }

    public QEmailPk(Path<? extends QEmailPk> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "email_pk");
    }

    public QEmailPk(PathMetadata<?> metadata) {
        super(QEmailPk.class, metadata, "municipalityinitiative", "email_pk");
    }

}

