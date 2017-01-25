package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * QNotification is a Querydsl query type for QNotification
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QNotification extends com.mysema.query.sql.RelationalPathBase<QNotification> {

    private static final long serialVersionUID = 1088371771;

    public static final QNotification notification = new QNotification("notification");

    public final BooleanPath enabled = createBoolean("enabled");

    public final StringPath fi = createString("fi");

    public final BooleanPath lock = createBoolean("lock");

    public final StringPath sv = createString("sv");

    public final StringPath urlfi = createString("urlfi");

    public final StringPath urlfitext = createString("urlfitext");

    public final StringPath urlsv = createString("urlsv");

    public final StringPath urlsvtext = createString("urlsvtext");

    public QNotification(String variable) {
        super(QNotification.class,  forVariable(variable), "municipalityinitiative", "notification");
        addMetadata();
    }

    public QNotification(Path<? extends QNotification> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "notification");
        addMetadata();
    }

    public QNotification(PathMetadata<?> metadata) {
        super(QNotification.class,  metadata, "municipalityinitiative", "notification");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(enabled, ColumnMetadata.named("enabled").ofType(-7).withSize(1).notNull());
        addMetadata(fi, ColumnMetadata.named("fi").ofType(12).withSize(10000));
        addMetadata(lock, ColumnMetadata.named("lock").ofType(-7).withSize(1));
        addMetadata(sv, ColumnMetadata.named("sv").ofType(12).withSize(10000));
        addMetadata(urlfi, ColumnMetadata.named("urlfi").ofType(12).withSize(1000));
        addMetadata(urlfitext, ColumnMetadata.named("urlfitext").ofType(12).withSize(1000));
        addMetadata(urlsv, ColumnMetadata.named("urlsv").ofType(12).withSize(1000));
        addMetadata(urlsvtext, ColumnMetadata.named("urlsvtext").ofType(12).withSize(1000));
    }

}

