package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QInituserPk is a Querydsl query type for QInituserPk
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QInituserPk extends com.mysema.query.sql.RelationalPathBase<QInituserPk> {

    private static final long serialVersionUID = 1722252682;

    public static final QInituserPk inituserPk = new QInituserPk("inituser_pk");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QInituserPk(String variable) {
        super(QInituserPk.class, forVariable(variable), "initiative", "inituser_pk");
    }

    public QInituserPk(Path<? extends QInituserPk> path) {
        super(path.getType(), path.getMetadata(), "initiative", "inituser_pk");
    }

    public QInituserPk(PathMetadata<?> metadata) {
        super(QInituserPk.class, metadata, "initiative", "inituser_pk");
    }

}

