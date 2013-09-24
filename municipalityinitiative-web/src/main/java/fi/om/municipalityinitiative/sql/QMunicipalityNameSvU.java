package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QMunicipalityNameSvU is a Querydsl query type for QMunicipalityNameSvU
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QMunicipalityNameSvU extends com.mysema.query.sql.RelationalPathBase<QMunicipalityNameSvU> {

    private static final long serialVersionUID = 87965275;

    public static final QMunicipalityNameSvU municipalityNameSvU = new QMunicipalityNameSvU("municipality_name_sv_u");

    public final StringPath nameSv = createString("name_sv");

    public QMunicipalityNameSvU(String variable) {
        super(QMunicipalityNameSvU.class, forVariable(variable), "municipalityinitiative", "municipality_name_sv_u");
    }

    public QMunicipalityNameSvU(Path<? extends QMunicipalityNameSvU> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "municipality_name_sv_u");
    }

    public QMunicipalityNameSvU(PathMetadata<?> metadata) {
        super(QMunicipalityNameSvU.class, metadata, "municipalityinitiative", "municipality_name_sv_u");
    }

}

