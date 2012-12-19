package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QMunicipalityInitiativeIdSeq is a Querydsl query type for QMunicipalityInitiativeIdSeq
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QMunicipalityInitiativeIdSeq extends com.mysema.query.sql.RelationalPathBase<QMunicipalityInitiativeIdSeq> {

    private static final long serialVersionUID = 45913388;

    public static final QMunicipalityInitiativeIdSeq municipalityInitiativeIdSeq = new QMunicipalityInitiativeIdSeq("municipality_initiative_id_seq");

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

    public QMunicipalityInitiativeIdSeq(String variable) {
        super(QMunicipalityInitiativeIdSeq.class, forVariable(variable), "municipalityinitiative", "municipality_initiative_id_seq");
    }

    public QMunicipalityInitiativeIdSeq(Path<? extends QMunicipalityInitiativeIdSeq> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "municipality_initiative_id_seq");
    }

    public QMunicipalityInitiativeIdSeq(PathMetadata<?> metadata) {
        super(QMunicipalityInitiativeIdSeq.class, metadata, "municipalityinitiative", "municipality_initiative_id_seq");
    }

}

