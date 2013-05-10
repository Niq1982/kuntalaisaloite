package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QMunicipality is a Querydsl query type for QMunicipality
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QMunicipality extends com.mysema.query.sql.RelationalPathBase<QMunicipality> {

    private static final long serialVersionUID = -614926388;

    public static final QMunicipality municipality = new QMunicipality("municipality");

    public final BooleanPath active = createBoolean("active");

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final StringPath nameSv = createString("name_sv");

    public final com.mysema.query.sql.PrimaryKey<QMunicipality> municipalityPk = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<QParticipant> _participantMunicipalityFk = createInvForeignKey(id, "municipality_id");

    public final com.mysema.query.sql.ForeignKey<QMunicipalityInitiative> _municipalityInitiativeMunicipalityFk = createInvForeignKey(id, "municipality_id");

    public QMunicipality(String variable) {
        super(QMunicipality.class, forVariable(variable), "municipalityinitiative", "municipality");
    }

    public QMunicipality(Path<? extends QMunicipality> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "municipality");
    }

    public QMunicipality(PathMetadata<?> metadata) {
        super(QMunicipality.class, metadata, "municipalityinitiative", "municipality");
    }

}

