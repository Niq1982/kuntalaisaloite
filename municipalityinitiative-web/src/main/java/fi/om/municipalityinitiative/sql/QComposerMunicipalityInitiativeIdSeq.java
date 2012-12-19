package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QComposerMunicipalityInitiativeIdSeq is a Querydsl query type for QComposerMunicipalityInitiativeIdSeq
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QComposerMunicipalityInitiativeIdSeq extends com.mysema.query.sql.RelationalPathBase<QComposerMunicipalityInitiativeIdSeq> {

    private static final long serialVersionUID = -1326557204;

    public static final QComposerMunicipalityInitiativeIdSeq composerMunicipalityInitiativeIdSeq = new QComposerMunicipalityInitiativeIdSeq("composer_municipality_initiative_id_seq");

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

    public QComposerMunicipalityInitiativeIdSeq(String variable) {
        super(QComposerMunicipalityInitiativeIdSeq.class, forVariable(variable), "municipalityinitiative", "composer_municipality_initiative_id_seq");
    }

    public QComposerMunicipalityInitiativeIdSeq(Path<? extends QComposerMunicipalityInitiativeIdSeq> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "composer_municipality_initiative_id_seq");
    }

    public QComposerMunicipalityInitiativeIdSeq(PathMetadata<?> metadata) {
        super(QComposerMunicipalityInitiativeIdSeq.class, metadata, "municipalityinitiative", "composer_municipality_initiative_id_seq");
    }

}

