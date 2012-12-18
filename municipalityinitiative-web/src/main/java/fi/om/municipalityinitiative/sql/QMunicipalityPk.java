package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QMunicipalityPk is a Querydsl query type for QMunicipalityPk
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QMunicipalityPk extends com.mysema.query.sql.RelationalPathBase<QMunicipalityPk> {

    private static final long serialVersionUID = 1761230567;

    public static final QMunicipalityPk municipalityPk = new QMunicipalityPk("municipality_pk");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QMunicipalityPk(String variable) {
        super(QMunicipalityPk.class, forVariable(variable), "municipalityinitiative", "municipality_pk");
    }

    public QMunicipalityPk(Path<? extends QMunicipalityPk> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "municipality_pk");
    }

    public QMunicipalityPk(PathMetadata<?> metadata) {
        super(QMunicipalityPk.class, metadata, "municipalityinitiative", "municipality_pk");
    }

}

