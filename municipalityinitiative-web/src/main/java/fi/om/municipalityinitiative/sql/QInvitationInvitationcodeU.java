package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QInvitationInvitationcodeU is a Querydsl query type for QInvitationInvitationcodeU
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QInvitationInvitationcodeU extends com.mysema.query.sql.RelationalPathBase<QInvitationInvitationcodeU> {

    private static final long serialVersionUID = -461331614;

    public static final QInvitationInvitationcodeU invitationInvitationcodeU = new QInvitationInvitationcodeU("invitation_invitationcode_u");

    public final NumberPath<Long> initiativeId = createNumber("initiative_id", Long.class);

    public final StringPath invitationcode = createString("invitationcode");

    public QInvitationInvitationcodeU(String variable) {
        super(QInvitationInvitationcodeU.class, forVariable(variable), "initiative", "invitation_invitationcode_u");
    }

    public QInvitationInvitationcodeU(Path<? extends QInvitationInvitationcodeU> path) {
        super(path.getType(), path.getMetadata(), "initiative", "invitation_invitationcode_u");
    }

    public QInvitationInvitationcodeU(PathMetadata<?> metadata) {
        super(QInvitationInvitationcodeU.class, metadata, "initiative", "invitation_invitationcode_u");
    }

}

