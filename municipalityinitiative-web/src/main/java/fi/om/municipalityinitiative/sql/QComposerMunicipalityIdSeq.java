package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QComposerMunicipalityIdSeq is a Querydsl query type for QComposerMunicipalityIdSeq
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QComposerMunicipalityIdSeq extends com.mysema.query.sql.RelationalPathBase<QComposerMunicipalityIdSeq> {

    private static final long serialVersionUID = 456894360;

    public static final QComposerMunicipalityIdSeq composerMunicipalityIdSeq = new QComposerMunicipalityIdSeq("composer_municipality_id_seq");

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

    public QComposerMunicipalityIdSeq(String variable) {
        super(QComposerMunicipalityIdSeq.class, forVariable(variable), "municipalityinitiative", "composer_municipality_id_seq");
    }

    public QComposerMunicipalityIdSeq(Path<? extends QComposerMunicipalityIdSeq> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "composer_municipality_id_seq");
    }

    public QComposerMunicipalityIdSeq(PathMetadata<?> metadata) {
        super(QComposerMunicipalityIdSeq.class, metadata, "municipalityinitiative", "composer_municipality_id_seq");
    }

}

