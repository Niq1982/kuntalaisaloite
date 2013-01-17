package fi.om.municipalityinitiative.sql;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.NumberPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QParticipantIdIndex is a Querydsl query type for QParticipantIdIndex
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QParticipantIdIndex extends com.mysema.query.sql.RelationalPathBase<QParticipantIdIndex> {

    private static final long serialVersionUID = 145478932;

    public static final QParticipantIdIndex participantIdIndex = new QParticipantIdIndex("participant_id_index");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QParticipantIdIndex(String variable) {
        super(QParticipantIdIndex.class, forVariable(variable), "municipalityinitiative", "participant_id_index");
    }

    public QParticipantIdIndex(Path<? extends QParticipantIdIndex> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "participant_id_index");
    }

    public QParticipantIdIndex(PathMetadata<?> metadata) {
        super(QParticipantIdIndex.class, metadata, "municipalityinitiative", "participant_id_index");
    }

}

