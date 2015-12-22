package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * QVerifiedUserNormalInitiatives is a Querydsl query type for QVerifiedUserNormalInitiatives
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QVerifiedUserNormalInitiatives extends com.mysema.query.sql.RelationalPathBase<QVerifiedUserNormalInitiatives> {

    private static final long serialVersionUID = 1003950269;

    public static final QVerifiedUserNormalInitiatives verifiedUserNormalInitiatives = new QVerifiedUserNormalInitiatives("verified_user_normal_initiatives");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> participant = createNumber("participant", Long.class);

    public final NumberPath<Long> verifiedUser = createNumber("verifiedUser", Long.class);

    public final com.mysema.query.sql.PrimaryKey<QVerifiedUserNormalInitiatives> verifiedUserNormalInitiativesInitiativePk = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<QVerifiedUser> verifiedUserNormalInitiativesVerifiedUserId = createForeignKey(verifiedUser, "id");

    public final com.mysema.query.sql.ForeignKey<QParticipant> verifiedUserNormalInitiativesParticipantId = createForeignKey(participant, "id");

    public QVerifiedUserNormalInitiatives(String variable) {
        super(QVerifiedUserNormalInitiatives.class,  forVariable(variable), "municipalityinitiative", "verified_user_normal_initiatives");
        addMetadata();
    }

    public QVerifiedUserNormalInitiatives(Path<? extends QVerifiedUserNormalInitiatives> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "verified_user_normal_initiatives");
        addMetadata();
    }

    public QVerifiedUserNormalInitiatives(PathMetadata<?> metadata) {
        super(QVerifiedUserNormalInitiatives.class,  metadata, "municipalityinitiative", "verified_user_normal_initiatives");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").ofType(-5).withSize(19).notNull());
        addMetadata(participant, ColumnMetadata.named("participant").ofType(-5).withSize(19).notNull());
        addMetadata(verifiedUser, ColumnMetadata.named("verified_user").ofType(-5).withSize(19).notNull());
    }

}

