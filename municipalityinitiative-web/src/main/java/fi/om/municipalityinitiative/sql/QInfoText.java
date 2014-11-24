package fi.om.municipalityinitiative.sql;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;

import com.mysema.query.sql.ColumnMetadata;


/**
 * QInfoText is a Querydsl query type for QInfoText
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QInfoText extends com.mysema.query.sql.RelationalPathBase<QInfoText> {

    private static final long serialVersionUID = -696389045;

    public static final QInfoText infoText = new QInfoText("info_text");

    public final EnumPath<fi.om.municipalityinitiative.util.InfoTextCategory> category = createEnum("category", fi.om.municipalityinitiative.util.InfoTextCategory.class);

    public final StringPath draft = createString("draft");

    public final StringPath draftSubject = createString("draftSubject");

    public final BooleanPath footerDisplay = createBoolean("footerDisplay");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<fi.om.municipalityinitiative.util.LanguageCode> languagecode = createEnum("languagecode", fi.om.municipalityinitiative.util.LanguageCode.class);

    public final DateTimePath<org.joda.time.DateTime> modified = createDateTime("modified", org.joda.time.DateTime.class);

    public final StringPath modifier = createString("modifier");

    public final NumberPath<Integer> orderposition = createNumber("orderposition", Integer.class);

    public final StringPath published = createString("published");

    public final StringPath publishedSubject = createString("publishedSubject");

    public final StringPath uri = createString("uri");

    public final com.mysema.query.sql.PrimaryKey<QInfoText> infotextPk = createPrimaryKey(id);

    public QInfoText(String variable) {
        super(QInfoText.class,  forVariable(variable), "municipalityinitiative", "info_text");
        addMetadata();
    }

    public QInfoText(Path<? extends QInfoText> path) {
        super(path.getType(), path.getMetadata(), "municipalityinitiative", "info_text");
        addMetadata();
    }

    public QInfoText(PathMetadata<?> metadata) {
        super(QInfoText.class,  metadata, "municipalityinitiative", "info_text");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(category, ColumnMetadata.named("category").ofType(1111).withSize(2147483647).notNull());
        addMetadata(draft, ColumnMetadata.named("draft").ofType(12).withSize(2147483647));
        addMetadata(draftSubject, ColumnMetadata.named("draft_subject").ofType(12).withSize(100));
        addMetadata(footerDisplay, ColumnMetadata.named("footer_display").ofType(-7).withSize(1).notNull());
        addMetadata(id, ColumnMetadata.named("id").ofType(-5).withSize(19).notNull());
        addMetadata(languagecode, ColumnMetadata.named("languagecode").ofType(1111).withSize(2147483647).notNull());
        addMetadata(modified, ColumnMetadata.named("modified").ofType(93).withSize(29).withDigits(6));
        addMetadata(modifier, ColumnMetadata.named("modifier").ofType(12).withSize(100));
        addMetadata(orderposition, ColumnMetadata.named("orderposition").ofType(4).withSize(10).notNull());
        addMetadata(published, ColumnMetadata.named("published").ofType(12).withSize(2147483647));
        addMetadata(publishedSubject, ColumnMetadata.named("published_subject").ofType(12).withSize(100));
        addMetadata(uri, ColumnMetadata.named("uri").ofType(12).withSize(100));
    }

}

