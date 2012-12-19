package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QMunicipalityInitiativePk is a Querydsl query type for QMunicipalityInitiativePk
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QMunicipalityInitiativePk extends com.mysema.query.sql.RelationalPathBase<QMunicipalityInitiativePk> {

    private static final long serialVersionUID = 312418067;

    public static final QMunicipalityInitiativePk municipalityInitiativePk = new QMunicipalityInitiativePk("municipality_initiative_pk");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QMunicipalityInitiativePk(String variable) {
        super(QMunicipalityInitiativePk.class, forVariable(variable), "municipalityinitiative", "municipality_initiative_pk");
    }

    public QMunicipalityInitiativePk(Path<? extends QMunicipalityInitiativePk> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "municipality_initiative_pk");
    }

    public QMunicipalityInitiativePk(PathMetadata<?> metadata) {
        super(QMunicipalityInitiativePk.class, metadata, "municipalityinitiative", "municipality_initiative_pk");
    }

}

