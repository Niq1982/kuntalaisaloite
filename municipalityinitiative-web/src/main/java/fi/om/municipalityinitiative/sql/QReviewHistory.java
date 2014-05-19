package fi.om.municipalityinitiative.sql;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.EnumPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QReviewHistory is a Querydsl query type for QReviewHistory
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QReviewHistory extends com.mysema.query.sql.RelationalPathBase<QReviewHistory> {

    private static final long serialVersionUID = 89049292;

    public static final QReviewHistory reviewHistory = new QReviewHistory("review_history");

    public final DateTimePath<org.joda.time.DateTime> created = createDateTime("created", org.joda.time.DateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> initiativeId = createNumber("initiativeId", Long.class);

    public final StringPath initiativeSnapshot = createString("initiativeSnapshot");

    public final StringPath message = createString("message");

    public final EnumPath<fi.om.municipalityinitiative.util.ReviewHistoryType> type = createEnum("type", fi.om.municipalityinitiative.util.ReviewHistoryType.class);

    public final com.mysema.query.sql.PrimaryKey<QReviewHistory> reviewHistoryPk = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<QMunicipalityInitiative> reviewHistoryInitiativeId = createForeignKey(initiativeId, "id");

    public QReviewHistory(String variable) {
        super(QReviewHistory.class,  forVariable(variable), "municipalityinitiative", "review_history");
        addMetadata();
    }

    public QReviewHistory(Path<? extends QReviewHistory> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "review_history");
        addMetadata();
    }

    public QReviewHistory(PathMetadata<?> metadata) {
        super(QReviewHistory.class,  metadata, "municipalityinitiative", "review_history");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(created, ColumnMetadata.named("created").ofType(93).withSize(29).withDigits(6).notNull());
        addMetadata(id, ColumnMetadata.named("id").ofType(-5).withSize(19).notNull());
        addMetadata(initiativeId, ColumnMetadata.named("initiative_id").ofType(-5).withSize(19).notNull());
        addMetadata(initiativeSnapshot, ColumnMetadata.named("initiative_snapshot").ofType(12).withSize(2147483647));
        addMetadata(message, ColumnMetadata.named("message").ofType(12).withSize(1024));
        addMetadata(type, ColumnMetadata.named("type").ofType(1111).withSize(2147483647).notNull());
    }

}

