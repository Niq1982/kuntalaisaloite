package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QAdminUserPk is a Querydsl query type for QAdminUserPk
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QAdminUserPk extends com.mysema.query.sql.RelationalPathBase<QAdminUserPk> {

    private static final long serialVersionUID = 1524877829;

    public static final QAdminUserPk adminUserPk = new QAdminUserPk("admin_user_pk");

    public final StringPath name = createString("name");

    public QAdminUserPk(String variable) {
        super(QAdminUserPk.class, forVariable(variable), "municipalityinitiative", "admin_user_pk");
    }

    public QAdminUserPk(Path<? extends QAdminUserPk> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "admin_user_pk");
    }

    public QAdminUserPk(PathMetadata<?> metadata) {
        super(QAdminUserPk.class, metadata, "municipalityinitiative", "admin_user_pk");
    }

}

