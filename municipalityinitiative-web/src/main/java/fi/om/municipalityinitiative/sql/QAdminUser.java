package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * QAdminUser is a Querydsl query type for QAdminUser
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QAdminUser extends com.mysema.query.sql.RelationalPathBase<QAdminUser> {

    private static final long serialVersionUID = -1312378262;

    public static final QAdminUser adminUser = new QAdminUser("admin_user");

    public final DateTimePath<org.joda.time.DateTime> lastOnline = createDateTime("lastOnline", org.joda.time.DateTime.class);

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final StringPath username = createString("username");

    public final com.mysema.query.sql.PrimaryKey<QAdminUser> adminUserPk = createPrimaryKey(name);

    public QAdminUser(String variable) {
        super(QAdminUser.class,  forVariable(variable), "municipalityinitiative", "admin_user");
        addMetadata();
    }

    public QAdminUser(Path<? extends QAdminUser> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "admin_user");
        addMetadata();
    }

    public QAdminUser(PathMetadata<?> metadata) {
        super(QAdminUser.class,  metadata, "municipalityinitiative", "admin_user");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(lastOnline, ColumnMetadata.named("last_online").ofType(93).withSize(29).withDigits(6));
        addMetadata(name, ColumnMetadata.named("name").ofType(12).withSize(40).notNull());
        addMetadata(password, ColumnMetadata.named("password").ofType(1).withSize(40).notNull());
        addMetadata(username, ColumnMetadata.named("username").ofType(12).withSize(30));
    }

}

