package fi.om.municipalityinitiative.sql;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.NumberPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QAuthorPk is a Querydsl query type for QAuthorPk
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QAuthorPk extends com.mysema.query.sql.RelationalPathBase<QAuthorPk> {

    private static final long serialVersionUID = 558747702;

    public static final QAuthorPk authorPk = new QAuthorPk("author_pk");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QAuthorPk(String variable) {
        super(QAuthorPk.class, forVariable(variable), "municipalityinitiative", "author_pk");
    }

    public QAuthorPk(Path<? extends QAuthorPk> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "author_pk");
    }

    public QAuthorPk(PathMetadata<?> metadata) {
        super(QAuthorPk.class, metadata, "municipalityinitiative", "author_pk");
    }

}

