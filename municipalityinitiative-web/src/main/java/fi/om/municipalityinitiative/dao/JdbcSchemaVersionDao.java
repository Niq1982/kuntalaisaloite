package fi.om.municipalityinitiative.dao;

import com.mysema.query.Tuple;
import com.mysema.query.sql.postgres.PostgresQuery;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.types.Expression;
import com.mysema.query.types.MappingProjection;
import fi.om.municipalityinitiative.dao.SQLExceptionTranslated;
import fi.om.municipalityinitiative.dto.SchemaVersion;
import fi.om.municipalityinitiative.sql.QFlywaySchema;
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

        List<SchemaVersion> oldMigrations = queryFactory.from(QSchemaVersion.schemaVersion)
                .orderBy(QSchemaVersion.schemaVersion.executed.desc())
                .list(schemaVersionMapper);

        oldMigrations.addAll(queryFactory.from(QFlywaySchema.flywaySchema)
                .orderBy(QFlywaySchema.flywaySchema.installedOn.asc())
                .list(flywaySchemaVersionMapping));


        return oldMigrations;

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

    private static final MappingProjection<SchemaVersion> flywaySchemaVersionMapping =
            new MappingProjection<SchemaVersion>(SchemaVersion.class, QFlywaySchema.flywaySchema.all()) {

                private static final long serialVersionUID = -1940230714453573464L;

                @Override
                protected SchemaVersion map(Tuple tuple) {
                    if (tuple == null) {
                        return null;
                    }
                    SchemaVersion schemaVersion = new SchemaVersion();
                    schemaVersion.setExecuted(tuple.get(QFlywaySchema.flywaySchema.installedOn));
                    schemaVersion.setScript(tuple.get(QFlywaySchema.flywaySchema.script));
                    return schemaVersion;
                }

            };


}
