package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QInitiativeAuthor is a Querydsl query type for QInitiativeAuthor
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QInitiativeAuthor extends com.mysema.query.sql.RelationalPathBase<QInitiativeAuthor> {

    private static final long serialVersionUID = -860074489;

    public static final QInitiativeAuthor initiativeAuthor = new QInitiativeAuthor("initiative_author");

    public final StringPath address = createString("address");

    public final DateTimePath<org.joda.time.DateTime> confirmationrequestsent = createDateTime("confirmationrequestsent", org.joda.time.DateTime.class);

    public final DateTimePath<org.joda.time.DateTime> confirmed = createDateTime("confirmed", org.joda.time.DateTime.class);

    public final DateTimePath<org.joda.time.DateTime> created = createDateTime("created", org.joda.time.DateTime.class);

    public final StringPath email = createString("email");

    public final StringPath firstnames = createString("firstnames");

    public final StringPath homemunicipalityFi = createString("homemunicipality_fi");

    public final StringPath homemunicipalitySv = createString("homemunicipality_sv");

    public final NumberPath<Long> initiativeId = createNumber("initiative_id", Long.class);

    public final BooleanPath initiator = createBoolean("initiator");

    public final StringPath lastname = createString("lastname");

    public final StringPath phone = createString("phone");

    public final EnumPath<fi.om.municipalityinitiative.dto.AuthorRole> role = createEnum("role", fi.om.municipalityinitiative.dto.AuthorRole.class);

    public final NumberPath<Long> userId = createNumber("user_id", Long.class);

    public final com.mysema.query.sql.PrimaryKey<QInitiativeAuthor> authorPk = createPrimaryKey(initiativeId, userId);

    public final com.mysema.query.sql.ForeignKey<QInituser> authorUserIdFk = createForeignKey(userId, "id");

    public final com.mysema.query.sql.ForeignKey<QInitiative> authorInitiativeIdFk = createForeignKey(initiativeId, "id");

    public QInitiativeAuthor(String variable) {
        super(QInitiativeAuthor.class, forVariable(variable), "municipalityinitiative", "initiative_author");
    }

    public QInitiativeAuthor(Path<? extends QInitiativeAuthor> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "initiative_author");
    }

    public QInitiativeAuthor(PathMetadata<?> metadata) {
        super(QInitiativeAuthor.class, metadata, "municipalityinitiative", "initiative_author");
    }

}

