package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * QMunicipalityInitiative is a Querydsl query type for QMunicipalityInitiative
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QMunicipalityInitiative extends com.mysema.query.sql.RelationalPathBase<QMunicipalityInitiative> {

    private static final long serialVersionUID = -2033192200;

    public static final QMunicipalityInitiative municipalityInitiative = new QMunicipalityInitiative("municipality_initiative");

    public final NumberPath<Integer> externalparticipantcount = createNumber("externalparticipantcount", Integer.class);

    public final StringPath extraInfo = createString("extraInfo");

    public final EnumPath<fi.om.municipalityinitiative.util.FixState> fixState = createEnum("fixState", fi.om.municipalityinitiative.util.FixState.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<org.joda.time.DateTime> lastEmailReportTime = createDateTime("lastEmailReportTime", org.joda.time.DateTime.class);

    public final EnumPath<fi.om.municipalityinitiative.service.email.EmailReportType> lastEmailReportType = createEnum("lastEmailReportType", fi.om.municipalityinitiative.service.email.EmailReportType.class);

    public final StringPath moderatorComment = createString("moderatorComment");

    public final DateTimePath<org.joda.time.DateTime> modified = createDateTime("modified", org.joda.time.DateTime.class);

    public final StringPath municipalityDecision = createString("municipalityDecision");

    public final DateTimePath<org.joda.time.DateTime> municipalityDecisionDate = createDateTime("municipalityDecisionDate", org.joda.time.DateTime.class);

    public final DateTimePath<org.joda.time.DateTime> municipalityDecisionModifiedDate = createDateTime("municipalityDecisionModifiedDate", org.joda.time.DateTime.class);

    public final NumberPath<Long> municipalityId = createNumber("municipalityId", Long.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> participantCount = createNumber("participantCount", Integer.class);

    public final NumberPath<Integer> participantCountCitizen = createNumber("participantCountCitizen", Integer.class);

    public final NumberPath<Integer> participantCountPublic = createNumber("participantCountPublic", Integer.class);

    public final StringPath proposal = createString("proposal");

    public final DateTimePath<org.joda.time.DateTime> sent = createDateTime("sent", org.joda.time.DateTime.class);

    public final StringPath sentComment = createString("sentComment");

    public final EnumPath<fi.om.municipalityinitiative.util.InitiativeState> state = createEnum("state", fi.om.municipalityinitiative.util.InitiativeState.class);

    public final DateTimePath<org.joda.time.DateTime> stateTimestamp = createDateTime("stateTimestamp", org.joda.time.DateTime.class);

    public final StringPath supportCountData = createString("supportCountData");

    public final EnumPath<fi.om.municipalityinitiative.util.InitiativeType> type = createEnum("type", fi.om.municipalityinitiative.util.InitiativeType.class);

    public final StringPath videoUrl = createString("videoUrl");

    public final NumberPath<Long> youthInitiativeId = createNumber("youthInitiativeId", Long.class);

    public final com.mysema.query.sql.PrimaryKey<QMunicipalityInitiative> municipalityInitiativePk = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<QMunicipality> municipalityInitiativeMunicipalityFk = createForeignKey(municipalityId, "id");

    public final com.mysema.query.sql.ForeignKey<QMunicipalityUser> _municipalityUserInitiativeId = createInvForeignKey(id, "initiative_id");

    public final com.mysema.query.sql.ForeignKey<QReviewHistory> _reviewHistoryInitiativeId = createInvForeignKey(id, "initiative_id");

    public final com.mysema.query.sql.ForeignKey<QAuthorInvitation> _authorInvitationInitiativeIdFk = createInvForeignKey(id, "initiative_id");

    public final com.mysema.query.sql.ForeignKey<QAuthorMessage> _authormessageInitiativeidFk = createInvForeignKey(id, "initiative_id");

    public final com.mysema.query.sql.ForeignKey<QVerifiedParticipant> _verifiedParticipantInitiativeFk = createInvForeignKey(id, "initiative_id");

    public final com.mysema.query.sql.ForeignKey<QFollowInitiative> _followInitiativeInitiativeId = createInvForeignKey(id, "initiative_id");

    public final com.mysema.query.sql.ForeignKey<QAuthor> _authorInitiativeIdFk = createInvForeignKey(id, "initiative_id");

    public final com.mysema.query.sql.ForeignKey<QLocation> _locationInitiativeId = createInvForeignKey(id, "initiative_id");

    public final com.mysema.query.sql.ForeignKey<QInitiativeSupportVoteDay> _supportVoteDayInitiativeIdFk = createInvForeignKey(id, "initiative_id");

    public final com.mysema.query.sql.ForeignKey<QEmail> _emailInitiativeId = createInvForeignKey(id, "initiative_id");

    public final com.mysema.query.sql.ForeignKey<QParticipant> _participantMunicipalityInitiativeIdFk = createInvForeignKey(id, "municipality_initiative_id");

    public final com.mysema.query.sql.ForeignKey<QDecisionAttachment> _decisionAttachmentInitiativeId = createInvForeignKey(id, "initiative_id");

    public final com.mysema.query.sql.ForeignKey<QAttachment> _attachmentInitiativeId = createInvForeignKey(id, "initiative_id");

    public final com.mysema.query.sql.ForeignKey<QVerifiedAuthor> _verifiedAuthorInitiativeFk = createInvForeignKey(id, "initiative_id");

    public QMunicipalityInitiative(String variable) {
        super(QMunicipalityInitiative.class,  forVariable(variable), "municipalityinitiative", "municipality_initiative");
        addMetadata();
    }

    public QMunicipalityInitiative(Path<? extends QMunicipalityInitiative> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "municipality_initiative");
        addMetadata();
    }

    public QMunicipalityInitiative(PathMetadata<?> metadata) {
        super(QMunicipalityInitiative.class,  metadata, "municipalityinitiative", "municipality_initiative");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(externalparticipantcount, ColumnMetadata.named("externalparticipantcount").ofType(4).withSize(10));
        addMetadata(extraInfo, ColumnMetadata.named("extra_info").ofType(12).withSize(10000));
        addMetadata(fixState, ColumnMetadata.named("fix_state").ofType(1111).withSize(2147483647).notNull());
        addMetadata(id, ColumnMetadata.named("id").ofType(-5).withSize(19).notNull());
        addMetadata(lastEmailReportTime, ColumnMetadata.named("last_email_report_time").ofType(93).withSize(29).withDigits(6));
        addMetadata(lastEmailReportType, ColumnMetadata.named("last_email_report_type").ofType(1111).withSize(2147483647));
        addMetadata(moderatorComment, ColumnMetadata.named("moderator_comment").ofType(12).withSize(1024));
        addMetadata(modified, ColumnMetadata.named("modified").ofType(93).withSize(29).withDigits(6).notNull());
        addMetadata(municipalityDecision, ColumnMetadata.named("municipality_decision").ofType(12).withSize(2147483647));
        addMetadata(municipalityDecisionDate, ColumnMetadata.named("municipality_decision_date").ofType(93).withSize(29).withDigits(6));
        addMetadata(municipalityDecisionModifiedDate, ColumnMetadata.named("municipality_decision_modified_date").ofType(93).withSize(29).withDigits(6));
        addMetadata(municipalityId, ColumnMetadata.named("municipality_id").ofType(-5).withSize(19).notNull());
        addMetadata(name, ColumnMetadata.named("name").ofType(12).withSize(512));
        addMetadata(participantCount, ColumnMetadata.named("participant_count").ofType(4).withSize(10));
        addMetadata(participantCountCitizen, ColumnMetadata.named("participant_count_citizen").ofType(4).withSize(10).notNull());
        addMetadata(participantCountPublic, ColumnMetadata.named("participant_count_public").ofType(4).withSize(10));
        addMetadata(proposal, ColumnMetadata.named("proposal").ofType(12).withSize(2147483647));
        addMetadata(sent, ColumnMetadata.named("sent").ofType(93).withSize(29).withDigits(6));
        addMetadata(sentComment, ColumnMetadata.named("sent_comment").ofType(12).withSize(1024));
        addMetadata(state, ColumnMetadata.named("state").ofType(1111).withSize(2147483647).notNull());
        addMetadata(stateTimestamp, ColumnMetadata.named("state_timestamp").ofType(93).withSize(29).withDigits(6).notNull());
        addMetadata(supportCountData, ColumnMetadata.named("support_count_data").ofType(12).withSize(2147483647));
        addMetadata(type, ColumnMetadata.named("type").ofType(1111).withSize(2147483647).notNull());
        addMetadata(videoUrl, ColumnMetadata.named("video_url").ofType(12).withSize(2147483647));
        addMetadata(youthInitiativeId, ColumnMetadata.named("youth_initiative_id").ofType(-5).withSize(19));
    }

}

