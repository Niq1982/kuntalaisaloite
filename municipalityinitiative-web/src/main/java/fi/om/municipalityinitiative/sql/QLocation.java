package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * QLocation is a Querydsl query type for QLocation
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QLocation extends com.mysema.query.sql.RelationalPathBase<QLocation> {

    private static final long serialVersionUID = 1026718469;

    public static final QLocation location = new QLocation("location");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> initiativeId = createNumber("initiativeId", Long.class);

    public final NumberPath<Double> locationLat = createNumber("locationLat", Double.class);

    public final NumberPath<Double> locationLng = createNumber("locationLng", Double.class);

    public final com.mysema.query.sql.PrimaryKey<QLocation> locationPk = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<QMunicipalityInitiative> locationInitiativeId = createForeignKey(initiativeId, "id");

    public QLocation(String variable) {
        super(QLocation.class,  forVariable(variable), "municipalityinitiative", "location");
        addMetadata();
    }

    public QLocation(Path<? extends QLocation> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "location");
        addMetadata();
    }

    public QLocation(PathMetadata<?> metadata) {
        super(QLocation.class,  metadata, "municipalityinitiative", "location");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").ofType(-5).withSize(19).notNull());
        addMetadata(initiativeId, ColumnMetadata.named("initiative_id").ofType(-5).withSize(19).notNull());
        addMetadata(locationLat, ColumnMetadata.named("location_lat").ofType(2).withSize(9).withDigits(6).notNull());
        addMetadata(locationLng, ColumnMetadata.named("location_lng").ofType(2).withSize(9).withDigits(6).notNull());
    }

}

