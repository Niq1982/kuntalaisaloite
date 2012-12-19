package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QMunicipalityIdIndex is a Querydsl query type for QMunicipalityIdIndex
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QMunicipalityIdIndex extends com.mysema.query.sql.RelationalPathBase<QMunicipalityIdIndex> {

    private static final long serialVersionUID = -1661173;

    public static final QMunicipalityIdIndex municipalityIdIndex = new QMunicipalityIdIndex("municipality_id_index");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QMunicipalityIdIndex(String variable) {
        super(QMunicipalityIdIndex.class, forVariable(variable), "municipalityinitiative", "municipality_id_index");
    }

    public QMunicipalityIdIndex(Path<? extends QMunicipalityIdIndex> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "municipality_id_index");
    }

    public QMunicipalityIdIndex(PathMetadata<?> metadata) {
        super(QMunicipalityIdIndex.class, metadata, "municipalityinitiative", "municipality_id_index");
    }

}

