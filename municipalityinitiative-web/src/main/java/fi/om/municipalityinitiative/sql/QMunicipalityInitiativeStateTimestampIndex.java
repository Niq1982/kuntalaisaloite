package fi.om.municipalityinitiative.sql;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.DateTimePath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QMunicipalityInitiativeStateTimestampIndex is a Querydsl query type for QMunicipalityInitiativeStateTimestampIndex
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QMunicipalityInitiativeStateTimestampIndex extends com.mysema.query.sql.RelationalPathBase<QMunicipalityInitiativeStateTimestampIndex> {

    private static final long serialVersionUID = -206614987;

    public static final QMunicipalityInitiativeStateTimestampIndex municipalityInitiativeStateTimestampIndex = new QMunicipalityInitiativeStateTimestampIndex("municipality_initiative_state_timestamp_index");

    public final DateTimePath<org.joda.time.DateTime> stateTimestamp = createDateTime("state_timestamp", org.joda.time.DateTime.class);

    public QMunicipalityInitiativeStateTimestampIndex(String variable) {
        super(QMunicipalityInitiativeStateTimestampIndex.class, forVariable(variable), "municipalityinitiative", "municipality_initiative_state_timestamp_index");
    }

    public QMunicipalityInitiativeStateTimestampIndex(Path<? extends QMunicipalityInitiativeStateTimestampIndex> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "municipality_initiative_state_timestamp_index");
    }

    public QMunicipalityInitiativeStateTimestampIndex(PathMetadata<?> metadata) {
        super(QMunicipalityInitiativeStateTimestampIndex.class, metadata, "municipalityinitiative", "municipality_initiative_state_timestamp_index");
    }

}

