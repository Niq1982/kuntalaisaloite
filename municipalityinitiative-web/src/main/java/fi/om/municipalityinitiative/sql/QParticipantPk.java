package fi.om.municipalityinitiative.sql;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.NumberPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QParticipantPk is a Querydsl query type for QParticipantPk
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QParticipantPk extends com.mysema.query.sql.RelationalPathBase<QParticipantPk> {

    private static final long serialVersionUID = 1160818494;

    public static final QParticipantPk participantPk = new QParticipantPk("participant_pk");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QParticipantPk(String variable) {
        super(QParticipantPk.class, forVariable(variable), "municipalityinitiative", "participant_pk");
    }

    public QParticipantPk(Path<? extends QParticipantPk> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "participant_pk");
    }

    public QParticipantPk(PathMetadata<?> metadata) {
        super(QParticipantPk.class, metadata, "municipalityinitiative", "participant_pk");
    }

}

