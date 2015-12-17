package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * QVerifiedAuthor is a Querydsl query type for QVerifiedAuthor
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QVerifiedAuthor extends com.mysema.query.sql.RelationalPathBase<QVerifiedAuthor> {

    private static final long serialVersionUID = 1283921347;

    public static final QVerifiedAuthor verifiedAuthor = new QVerifiedAuthor("verified_author");

    public final NumberPath<Long> initiativeId = createNumber("initiativeId", Long.class);

    public final NumberPath<Long> verifiedUserId = createNumber("verifiedUserId", Long.class);

    public final com.mysema.query.sql.PrimaryKey<QVerifiedAuthor> verifiedAuthorPk = createPrimaryKey(initiativeId, verifiedUserId);

    public final com.mysema.query.sql.ForeignKey<QVerifiedUser> verifiedAuthorVerifiedUserFk = createForeignKey(verifiedUserId, "id");

    public final com.mysema.query.sql.ForeignKey<QMunicipalityInitiative> verifiedAuthorInitiativeFk = createForeignKey(initiativeId, "id");

    public QVerifiedAuthor(String variable) {
        super(QVerifiedAuthor.class,  forVariable(variable), "municipalityinitiative", "verified_author");
        addMetadata();
    }

    public QVerifiedAuthor(Path<? extends QVerifiedAuthor> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "verified_author");
        addMetadata();
    }

    public QVerifiedAuthor(PathMetadata<?> metadata) {
        super(QVerifiedAuthor.class,  metadata, "municipalityinitiative", "verified_author");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(initiativeId, ColumnMetadata.named("initiative_id").ofType(-5).withSize(19).notNull());
        addMetadata(verifiedUserId, ColumnMetadata.named("verified_user_id").ofType(-5).withSize(19).notNull());
    }

}

