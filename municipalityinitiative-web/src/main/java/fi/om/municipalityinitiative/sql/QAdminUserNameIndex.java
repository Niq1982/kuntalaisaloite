package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QAdminUserNameIndex is a Querydsl query type for QAdminUserNameIndex
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QAdminUserNameIndex extends com.mysema.query.sql.RelationalPathBase<QAdminUserNameIndex> {

    private static final long serialVersionUID = -472964675;

    public static final QAdminUserNameIndex adminUserNameIndex = new QAdminUserNameIndex("admin_user_name_index");

    public final StringPath name = createString("name");

    public QAdminUserNameIndex(String variable) {
        super(QAdminUserNameIndex.class, forVariable(variable), "municipalityinitiative", "admin_user_name_index");
    }

    public QAdminUserNameIndex(Path<? extends QAdminUserNameIndex> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "admin_user_name_index");
    }

    public QAdminUserNameIndex(PathMetadata<?> metadata) {
        super(QAdminUserNameIndex.class, metadata, "municipalityinitiative", "admin_user_name_index");
    }

}

