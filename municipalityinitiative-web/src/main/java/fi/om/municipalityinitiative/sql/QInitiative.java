package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QInitiative is a Querydsl query type for QInitiative
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QInitiative extends com.mysema.query.sql.RelationalPathBase<QInitiative> {

    private static final long serialVersionUID = -1836534436;

    public static final QInitiative initiative = new QInitiative("initiative");

    public final StringPath acceptanceidentifier = createString("acceptanceidentifier");

    public final DatePath<org.joda.time.LocalDate> enddate = createDate("enddate", org.joda.time.LocalDate.class);

    public final NumberPath<Integer> externalsupportcount = createNumber("externalsupportcount", Integer.class);

    public final BooleanPath financialsupport = createBoolean("financialsupport");

    public final StringPath financialsupporturl = createString("financialsupporturl");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<org.joda.time.DateTime> modified = createDateTime("modified", org.joda.time.DateTime.class);

    public final NumberPath<Long> modifierId = createNumber("modifier_id", Long.class);

    public final StringPath nameFi = createString("name_fi");

    public final StringPath nameSv = createString("name_sv");

    public final EnumPath<fi.om.municipalityinitiative.dto.LanguageCode> primarylanguage = createEnum("primarylanguage", fi.om.municipalityinitiative.dto.LanguageCode.class);

    public final StringPath proposalFi = createString("proposal_fi");

    public final StringPath proposalSv = createString("proposal_sv");

    public final EnumPath<fi.om.municipalityinitiative.dto.ProposalType> proposaltype = createEnum("proposaltype", fi.om.municipalityinitiative.dto.ProposalType.class);

    public final StringPath rationaleFi = createString("rationale_fi");

    public final StringPath rationaleSv = createString("rationale_sv");

    public final NumberPath<Integer> sentsupportcount = createNumber("sentsupportcount", Integer.class);

    public final DatePath<org.joda.time.LocalDate> startdate = createDate("startdate", org.joda.time.LocalDate.class);

    public final EnumPath<fi.om.municipalityinitiative.dto.InitiativeState> state = createEnum("state", fi.om.municipalityinitiative.dto.InitiativeState.class);

    public final StringPath statecomment = createString("statecomment");

    public final DateTimePath<org.joda.time.DateTime> statedate = createDateTime("statedate", org.joda.time.DateTime.class);

    public final NumberPath<Integer> supportcount = createNumber("supportcount", Integer.class);

    public final BooleanPath supportstatementsinweb = createBoolean("supportstatementsinweb");

    public final BooleanPath supportstatementsonpaper = createBoolean("supportstatementsonpaper");

    public final DateTimePath<org.joda.time.DateTime> supportstatementsremoved = createDateTime("supportstatementsremoved", org.joda.time.DateTime.class);

    public final StringPath verificationidentifier = createString("verificationidentifier");

    public final DatePath<org.joda.time.LocalDate> verified = createDate("verified", org.joda.time.LocalDate.class);

    public final NumberPath<Integer> verifiedsupportcount = createNumber("verifiedsupportcount", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<QInitiative> initiativePk = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<QInituser> initiativeModifierIdFk = createForeignKey(modifierId, "id");

    public final com.mysema.query.sql.ForeignKey<QInitiativeLink> _linkInitiativeIdFk = createInvForeignKey(id, "initiative_id");

    public final com.mysema.query.sql.ForeignKey<QSupportVote> _supportVoteInitiativeIdFk = createInvForeignKey(id, "initiative_id");

    public final com.mysema.query.sql.ForeignKey<QInitiativeInvitation> _invitationInitiativeIdFk = createInvForeignKey(id, "initiative_id");

    public final com.mysema.query.sql.ForeignKey<QInitiativeAuthor> _authorInitiativeIdFk = createInvForeignKey(id, "initiative_id");

    public final com.mysema.query.sql.ForeignKey<QSupportVoteBatch> _supportVoteBatchInitiativeIdFk = createInvForeignKey(id, "initiative_id");

    public QInitiative(String variable) {
        super(QInitiative.class, forVariable(variable), "municipalityinitiative", "initiative");
    }

    public QInitiative(Path<? extends QInitiative> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "initiative");
    }

    public QInitiative(PathMetadata<?> metadata) {
        super(QInitiative.class, metadata, "municipalityinitiative", "initiative");
    }

}

