package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QMunicipalityIdSeq is a Querydsl query type for QMunicipalityIdSeq
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QMunicipalityIdSeq extends com.mysema.query.sql.RelationalPathBase<QMunicipalityIdSeq> {

    private static final long serialVersionUID = 1492743384;

    public static final QMunicipalityIdSeq municipalityIdSeq = new QMunicipalityIdSeq("municipality_id_seq");

    public final NumberPath<Long> cacheValue = createNumber("cache_value", Long.class);

    public final NumberPath<Long> incrementBy = createNumber("increment_by", Long.class);

    public final BooleanPath isCalled = createBoolean("is_called");

    public final BooleanPath isCycled = createBoolean("is_cycled");

    public final NumberPath<Long> lastValue = createNumber("last_value", Long.class);

    public final NumberPath<Long> logCnt = createNumber("log_cnt", Long.class);

    public final NumberPath<Long> maxValue = createNumber("max_value", Long.class);

    public final NumberPath<Long> minValue = createNumber("min_value", Long.class);

    public final StringPath sequenceName = createString("sequence_name");

    public final NumberPath<Long> startValue = createNumber("start_value", Long.class);

    public QMunicipalityIdSeq(String variable) {
        super(QMunicipalityIdSeq.class, forVariable(variable), "municipalityinitiative", "municipality_id_seq");
    }

    public QMunicipalityIdSeq(Path<? extends QMunicipalityIdSeq> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "municipality_id_seq");
    }

    public QMunicipalityIdSeq(PathMetadata<?> metadata) {
        super(QMunicipalityIdSeq.class, metadata, "municipalityinitiative", "municipality_id_seq");
    }

}

