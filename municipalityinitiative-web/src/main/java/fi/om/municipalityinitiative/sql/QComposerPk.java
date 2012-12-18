package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QComposerPk is a Querydsl query type for QComposerPk
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QComposerPk extends com.mysema.query.sql.RelationalPathBase<QComposerPk> {

    private static final long serialVersionUID = 1144277643;

    public static final QComposerPk composerPk = new QComposerPk("composer_pk");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QComposerPk(String variable) {
        super(QComposerPk.class, forVariable(variable), "municipalityinitiative", "composer_pk");
    }

    public QComposerPk(Path<? extends QComposerPk> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "composer_pk");
    }

    public QComposerPk(PathMetadata<?> metadata) {
        super(QComposerPk.class, metadata, "municipalityinitiative", "composer_pk");
    }

}

