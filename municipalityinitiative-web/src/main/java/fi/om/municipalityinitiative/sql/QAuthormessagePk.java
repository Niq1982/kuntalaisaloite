package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QAuthormessagePk is a Querydsl query type for QAuthormessagePk
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QAuthormessagePk extends com.mysema.query.sql.RelationalPathBase<QAuthormessagePk> {

    private static final long serialVersionUID = 95574503;

    public static final QAuthormessagePk authormessagePk = new QAuthormessagePk("authormessage_pk");

    public final StringPath confirmationCode = createString("confirmation_code");

    public QAuthormessagePk(String variable) {
        super(QAuthormessagePk.class, forVariable(variable), "municipalityinitiative", "authormessage_pk");
    }

    public QAuthormessagePk(Path<? extends QAuthormessagePk> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "authormessage_pk");
    }

    public QAuthormessagePk(PathMetadata<?> metadata) {
        super(QAuthormessagePk.class, metadata, "municipalityinitiative", "authormessage_pk");
    }

}

