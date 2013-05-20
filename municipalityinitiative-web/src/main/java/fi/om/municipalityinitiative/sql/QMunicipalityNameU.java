package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QMunicipalityNameU is a Querydsl query type for QMunicipalityNameU
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QMunicipalityNameU extends com.mysema.query.sql.RelationalPathBase<QMunicipalityNameU> {

    private static final long serialVersionUID = 1497296574;

    public static final QMunicipalityNameU municipalityNameU = new QMunicipalityNameU("municipality_name_u");

    public final StringPath name = createString("name");

    public QMunicipalityNameU(String variable) {
        super(QMunicipalityNameU.class, forVariable(variable), "municipalityinitiative", "municipality_name_u");
    }

    public QMunicipalityNameU(Path<? extends QMunicipalityNameU> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "municipality_name_u");
    }

    public QMunicipalityNameU(PathMetadata<?> metadata) {
        super(QMunicipalityNameU.class, metadata, "municipalityinitiative", "municipality_name_u");
    }

}

