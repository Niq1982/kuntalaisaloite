package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QInituserHashU is a Querydsl query type for QInituserHashU
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QInituserHashU extends com.mysema.query.sql.RelationalPathBase<QInituserHashU> {

    private static final long serialVersionUID = -2025246820;

    public static final QInituserHashU inituserHashU = new QInituserHashU("inituser_hash_u");

    public final StringPath hash = createString("hash");

    public QInituserHashU(String variable) {
        super(QInituserHashU.class, forVariable(variable), "municipalityinitiative", "inituser_hash_u");
    }

    public QInituserHashU(Path<? extends QInituserHashU> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "inituser_hash_u");
    }

    public QInituserHashU(PathMetadata<?> metadata) {
        super(QInituserHashU.class, metadata, "municipalityinitiative", "inituser_hash_u");
    }

}

