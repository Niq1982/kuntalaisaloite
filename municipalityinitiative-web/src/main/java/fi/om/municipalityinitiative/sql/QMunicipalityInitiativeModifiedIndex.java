package fi.om.municipalityinitiative.sql;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.DateTimePath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QMunicipalityInitiativeModifiedIndex is a Querydsl query type for QMunicipalityInitiativeModifiedIndex
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QMunicipalityInitiativeModifiedIndex extends com.mysema.query.sql.RelationalPathBase<QMunicipalityInitiativeModifiedIndex> {

    private static final long serialVersionUID = 1681611633;

    public static final QMunicipalityInitiativeModifiedIndex municipalityInitiativeModifiedIndex = new QMunicipalityInitiativeModifiedIndex("municipality_initiative_modified_index");

    public final DateTimePath<org.joda.time.DateTime> modified = createDateTime("modified", org.joda.time.DateTime.class);

    public QMunicipalityInitiativeModifiedIndex(String variable) {
        super(QMunicipalityInitiativeModifiedIndex.class, forVariable(variable), "municipalityinitiative", "municipality_initiative_modified_index");
    }

    public QMunicipalityInitiativeModifiedIndex(Path<? extends QMunicipalityInitiativeModifiedIndex> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "municipality_initiative_modified_index");
    }

    public QMunicipalityInitiativeModifiedIndex(PathMetadata<?> metadata) {
        super(QMunicipalityInitiativeModifiedIndex.class, metadata, "municipalityinitiative", "municipality_initiative_modified_index");
    }

}

