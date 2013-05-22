package fi.om.municipalityinitiative.newdao;

import com.mysema.query.Tuple;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.types.Expression;
import com.mysema.query.types.MappingProjection;
import fi.om.municipalityinitiative.dao.SQLExceptionTranslated;
import fi.om.municipalityinitiative.newdto.SchemaVersion;
import fi.om.municipalityinitiative.sql.QSchemaVersion;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;

@SQLExceptionTranslated
public class JdbcSchemaVersionDao {

    @Resource
    PostgresQueryFactory queryFactory;

    @Transactional(readOnly = true)
    public List<SchemaVersion> findExecutedScripts() {

        return queryFactory.from(QSchemaVersion.schemaVersion)
                .orderBy(QSchemaVersion.schemaVersion.executed.desc())
                .list(schemaVersionMapper);

    }

    private Expression<SchemaVersion> schemaVersionMapper =  new MappingProjection<SchemaVersion>(SchemaVersion.class,
            QSchemaVersion.schemaVersion.all()) {
        @Override
        protected SchemaVersion map(Tuple row) {
            SchemaVersion schemaVersion = new SchemaVersion();
            schemaVersion.setScript(row.get(QSchemaVersion.schemaVersion.script));
            schemaVersion.setExecuted(row.get(QSchemaVersion.schemaVersion.executed));
            return schemaVersion;
        }
    };

}
