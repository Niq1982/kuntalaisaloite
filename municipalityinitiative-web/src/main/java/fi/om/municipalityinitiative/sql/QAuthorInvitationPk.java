package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QAuthorInvitationPk is a Querydsl query type for QAuthorInvitationPk
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QAuthorInvitationPk extends com.mysema.query.sql.RelationalPathBase<QAuthorInvitationPk> {

    private static final long serialVersionUID = -1166511121;

    public static final QAuthorInvitationPk authorInvitationPk = new QAuthorInvitationPk("author_invitation_pk");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QAuthorInvitationPk(String variable) {
        super(QAuthorInvitationPk.class, forVariable(variable), "municipalityinitiative", "author_invitation_pk");
    }

    public QAuthorInvitationPk(Path<? extends QAuthorInvitationPk> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "author_invitation_pk");
    }

    public QAuthorInvitationPk(PathMetadata<?> metadata) {
        super(QAuthorInvitationPk.class, metadata, "municipalityinitiative", "author_invitation_pk");
    }

}

