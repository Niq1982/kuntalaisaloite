package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QComposer is a Querydsl query type for QComposer
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QComposer extends com.mysema.query.sql.RelationalPathBase<QComposer> {

    private static final long serialVersionUID = -1473667984;

    public static final QComposer composer = new QComposer("composer");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isMunicipalityCitizen = createBoolean("is_municipality_citizen");

    public final NumberPath<Long> municipalityId = createNumber("municipality_id", Long.class);

    public final NumberPath<Long> municipalityInitiativeId = createNumber("municipality_initiative_id", Long.class);

    public final StringPath name = createString("name");

    public final BooleanPath showName = createBoolean("show_name");

    public final com.mysema.query.sql.PrimaryKey<QComposer> composerPk = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<QMunicipalityInitiative> composerMunicipalityInitiativeId = createForeignKey(municipalityInitiativeId, "id");

    public QComposer(String variable) {
        super(QComposer.class, forVariable(variable), "municipalityinitiative", "composer");
    }

    public QComposer(Path<? extends QComposer> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "composer");
    }

    public QComposer(PathMetadata<?> metadata) {
        super(QComposer.class, metadata, "municipalityinitiative", "composer");
    }

}

