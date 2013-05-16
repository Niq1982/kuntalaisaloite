package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QAuthorManagementHashIndex is a Querydsl query type for QAuthorManagementHashIndex
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QAuthorManagementHashIndex extends com.mysema.query.sql.RelationalPathBase<QAuthorManagementHashIndex> {

    private static final long serialVersionUID = 440716934;

    public static final QAuthorManagementHashIndex authorManagementHashIndex = new QAuthorManagementHashIndex("author_management_hash_index");

    public final StringPath managementHash = createString("management_hash");

    public QAuthorManagementHashIndex(String variable) {
        super(QAuthorManagementHashIndex.class, forVariable(variable), "municipalityinitiative", "author_management_hash_index");
    }

    public QAuthorManagementHashIndex(Path<? extends QAuthorManagementHashIndex> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "author_management_hash_index");
    }

    public QAuthorManagementHashIndex(PathMetadata<?> metadata) {
        super(QAuthorManagementHashIndex.class, metadata, "municipalityinitiative", "author_management_hash_index");
    }

}

