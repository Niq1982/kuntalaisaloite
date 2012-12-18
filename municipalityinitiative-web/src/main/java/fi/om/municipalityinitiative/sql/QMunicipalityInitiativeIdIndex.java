package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QMunicipalityInitiativeIdIndex is a Querydsl query type for QMunicipalityInitiativeIdIndex
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QMunicipalityInitiativeIdIndex extends com.mysema.query.sql.RelationalPathBase<QMunicipalityInitiativeIdIndex> {

    private static final long serialVersionUID = 1164116575;

    public static final QMunicipalityInitiativeIdIndex municipalityInitiativeIdIndex = new QMunicipalityInitiativeIdIndex("municipality_initiative_id_index");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QMunicipalityInitiativeIdIndex(String variable) {
        super(QMunicipalityInitiativeIdIndex.class, forVariable(variable), "municipalityinitiative", "municipality_initiative_id_index");
    }

    public QMunicipalityInitiativeIdIndex(Path<? extends QMunicipalityInitiativeIdIndex> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "municipality_initiative_id_index");
    }

    public QMunicipalityInitiativeIdIndex(PathMetadata<?> metadata) {
        super(QMunicipalityInitiativeIdIndex.class, metadata, "municipalityinitiative", "municipality_initiative_id_index");
    }

}

