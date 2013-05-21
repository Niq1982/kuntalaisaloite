package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QInfoText is a Querydsl query type for QInfoText
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QInfoText extends com.mysema.query.sql.RelationalPathBase<QInfoText> {

    private static final long serialVersionUID = -696389045;

    public static final QInfoText infoText = new QInfoText("info_text");

    public final EnumPath<fi.om.municipalityinitiative.util.InfoTextCategory> category = createEnum("category", fi.om.municipalityinitiative.util.InfoTextCategory.class);

    public final StringPath draft = createString("draft");

    public final StringPath draftSubject = createString("draft_subject");

    public final BooleanPath footerDisplay = createBoolean("footer_display");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<fi.om.municipalityinitiative.util.LanguageCode> languagecode = createEnum("languagecode", fi.om.municipalityinitiative.util.LanguageCode.class);

    public final DateTimePath<org.joda.time.DateTime> modified = createDateTime("modified", org.joda.time.DateTime.class);

    public final StringPath modifier = createString("modifier");

    public final NumberPath<Integer> orderposition = createNumber("orderposition", Integer.class);

    public final StringPath published = createString("published");

    public final StringPath publishedSubject = createString("published_subject");

    public final StringPath uri = createString("uri");

    public final com.mysema.query.sql.PrimaryKey<QInfoText> infotextPk = createPrimaryKey(id);

    public QInfoText(String variable) {
        super(QInfoText.class, forVariable(variable), "municipalityinitiative", "info_text");
    }

    public QInfoText(Path<? extends QInfoText> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "info_text");
    }

    public QInfoText(PathMetadata<?> metadata) {
        super(QInfoText.class, metadata, "municipalityinitiative", "info_text");
    }

}

