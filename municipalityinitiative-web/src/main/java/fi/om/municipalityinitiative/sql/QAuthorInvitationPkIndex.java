package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QAuthorInvitationPkIndex is a Querydsl query type for QAuthorInvitationPkIndex
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QAuthorInvitationPkIndex extends com.mysema.query.sql.RelationalPathBase<QAuthorInvitationPkIndex> {

    private static final long serialVersionUID = -370810333;

    public static final QAuthorInvitationPkIndex authorInvitationPkIndex = new QAuthorInvitationPkIndex("author_invitation_pk_index");

    public final StringPath confirmationCode = createString("confirmation_code");

    public final NumberPath<Long> initiativeId = createNumber("initiative_id", Long.class);

    public QAuthorInvitationPkIndex(String variable) {
        super(QAuthorInvitationPkIndex.class, forVariable(variable), "municipalityinitiative", "author_invitation_pk_index");
    }

    public QAuthorInvitationPkIndex(Path<? extends QAuthorInvitationPkIndex> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "author_invitation_pk_index");
    }

    public QAuthorInvitationPkIndex(PathMetadata<?> metadata) {
        super(QAuthorInvitationPkIndex.class, metadata, "municipalityinitiative", "author_invitation_pk_index");
    }

}

