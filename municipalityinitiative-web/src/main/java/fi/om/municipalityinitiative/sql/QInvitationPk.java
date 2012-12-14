package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QInvitationPk is a Querydsl query type for QInvitationPk
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QInvitationPk extends com.mysema.query.sql.RelationalPathBase<QInvitationPk> {

    private static final long serialVersionUID = 1553919944;

    public static final QInvitationPk invitationPk = new QInvitationPk("invitation_pk");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QInvitationPk(String variable) {
        super(QInvitationPk.class, forVariable(variable), "initiative", "invitation_pk");
    }

    public QInvitationPk(Path<? extends QInvitationPk> path) {
        super(path.getType(), path.getMetadata(), "initiative", "invitation_pk");
    }

    public QInvitationPk(PathMetadata<?> metadata) {
        super(QInvitationPk.class, metadata, "initiative", "invitation_pk");
    }

}

