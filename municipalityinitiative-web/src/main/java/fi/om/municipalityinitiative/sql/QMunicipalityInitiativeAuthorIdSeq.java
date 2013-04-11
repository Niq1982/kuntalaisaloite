package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QMunicipalityInitiativeAuthorIdSeq is a Querydsl query type for QMunicipalityInitiativeAuthorIdSeq
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QMunicipalityInitiativeAuthorIdSeq extends com.mysema.query.sql.RelationalPathBase<QMunicipalityInitiativeAuthorIdSeq> {

    private static final long serialVersionUID = 1626027105;

    public static final QMunicipalityInitiativeAuthorIdSeq municipalityInitiativeAuthorIdSeq = new QMunicipalityInitiativeAuthorIdSeq("municipality_initiative_author_id_seq");

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

    public QMunicipalityInitiativeAuthorIdSeq(String variable) {
        super(QMunicipalityInitiativeAuthorIdSeq.class, forVariable(variable), "municipalityinitiative", "municipality_initiative_author_id_seq");
    }

    public QMunicipalityInitiativeAuthorIdSeq(Path<? extends QMunicipalityInitiativeAuthorIdSeq> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "municipality_initiative_author_id_seq");
    }

    public QMunicipalityInitiativeAuthorIdSeq(PathMetadata<?> metadata) {
        super(QMunicipalityInitiativeAuthorIdSeq.class, metadata, "municipalityinitiative", "municipality_initiative_author_id_seq");
    }

}

