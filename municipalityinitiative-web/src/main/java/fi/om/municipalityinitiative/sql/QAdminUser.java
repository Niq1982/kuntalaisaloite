package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QAdminUser is a Querydsl query type for QAdminUser
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QAdminUser extends com.mysema.query.sql.RelationalPathBase<QAdminUser> {

    private static final long serialVersionUID = -1312378262;

    public static final QAdminUser adminUser = new QAdminUser("admin_user");

    public final DateTimePath<org.joda.time.DateTime> lastOnline = createDateTime("last_online", org.joda.time.DateTime.class);

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final StringPath username = createString("username");

    public final com.mysema.query.sql.PrimaryKey<QAdminUser> adminUserPk = createPrimaryKey(name);

    public QAdminUser(String variable) {
        super(QAdminUser.class, forVariable(variable), "municipalityinitiative", "admin_user");
    }

    public QAdminUser(Path<? extends QAdminUser> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "admin_user");
    }

    public QAdminUser(PathMetadata<?> metadata) {
        super(QAdminUser.class, metadata, "municipalityinitiative", "admin_user");
    }

}

