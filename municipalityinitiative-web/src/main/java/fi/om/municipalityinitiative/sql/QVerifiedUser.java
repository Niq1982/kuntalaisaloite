package fi.om.municipalityinitiative.sql;

import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QVerifiedUser is a Querydsl query type for QVerifiedUser
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QVerifiedUser extends com.mysema.query.sql.RelationalPathBase<QVerifiedUser> {

    private static final long serialVersionUID = -932147709;

    public static final QVerifiedUser verifiedUser = new QVerifiedUser("verified_user");

    public final StringPath address = createString("address");

    public final StringPath email = createString("email");

    public final StringPath hash = createString("hash");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> municipalityId = createNumber("municipalityId", Long.class);

    public final StringPath name = createString("name");

    public final StringPath phone = createString("phone");

    public final com.mysema.query.sql.PrimaryKey<QVerifiedUser> verifiedUserPk = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<QMunicipality> verifiedUserMunicipalityFk = createForeignKey(municipalityId, "id");

    public final com.mysema.query.sql.ForeignKey<QVerifiedParticipant> _verifiedParticipantVerifiedUserFk = createInvForeignKey(id, "verified_user_id");

    public final com.mysema.query.sql.ForeignKey<QVerifiedUserNormalInitiatives> _verifiedUserNormalInitiativesVerifiedUserId = createInvForeignKey(id, "verified_user");

    public final com.mysema.query.sql.ForeignKey<QVerifiedAuthor> _verifiedAuthorVerifiedUserFk = createInvForeignKey(id, "verified_user_id");

    public QVerifiedUser(String variable) {
        super(QVerifiedUser.class,  forVariable(variable), "municipalityinitiative", "verified_user");
        addMetadata();
    }

    public QVerifiedUser(Path<? extends QVerifiedUser> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "verified_user");
        addMetadata();
    }

    public QVerifiedUser(PathMetadata<?> metadata) {
        super(QVerifiedUser.class,  metadata, "municipalityinitiative", "verified_user");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(address, ColumnMetadata.named("address").ofType(12).withSize(256));
        addMetadata(email, ColumnMetadata.named("email").ofType(12).withSize(100));
        addMetadata(hash, ColumnMetadata.named("hash").ofType(12).withSize(64).notNull());
        addMetadata(id, ColumnMetadata.named("id").ofType(-5).withSize(19).notNull());
        addMetadata(municipalityId, ColumnMetadata.named("municipality_id").ofType(-5).withSize(19));
        addMetadata(name, ColumnMetadata.named("name").ofType(12).withSize(100));
        addMetadata(phone, ColumnMetadata.named("phone").ofType(12).withSize(30));
    }

}

