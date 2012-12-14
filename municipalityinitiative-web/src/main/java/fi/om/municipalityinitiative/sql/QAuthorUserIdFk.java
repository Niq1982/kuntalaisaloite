package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QAuthorUserIdFk is a Querydsl query type for QAuthorUserIdFk
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QAuthorUserIdFk extends com.mysema.query.sql.RelationalPathBase<QAuthorUserIdFk> {

    private static final long serialVersionUID = 33272746;

    public static final QAuthorUserIdFk authorUserIdFk = new QAuthorUserIdFk("author_user_id_fk");

    public final NumberPath<Long> userId = createNumber("user_id", Long.class);

    public QAuthorUserIdFk(String variable) {
        super(QAuthorUserIdFk.class, forVariable(variable), "initiative", "author_user_id_fk");
    }

    public QAuthorUserIdFk(Path<? extends QAuthorUserIdFk> path) {
        super(path.getType(), path.getMetadata(), "initiative", "author_user_id_fk");
    }

    public QAuthorUserIdFk(PathMetadata<?> metadata) {
        super(QAuthorUserIdFk.class, metadata, "initiative", "author_user_id_fk");
    }

}

