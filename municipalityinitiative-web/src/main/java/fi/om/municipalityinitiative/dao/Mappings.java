package fi.om.municipalityinitiative.dao;

import com.mysema.commons.lang.Assert;
import com.mysema.query.Tuple;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.sql.QMunicipality;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.Optional;

public class Mappings {

    public static int nullToZero(Integer integer) {
        if (integer == null) {
            return 0;
        }
        return integer;
    }

    public static Optional<Municipality> parseOptionalMunicipality(Tuple row) {
        Long municipalityId = row.get(QMunicipality.municipality.id);
        if (municipalityId == null) {
            return Optional.empty();
        }
        return Optional.of(new Municipality(
                municipalityId,
                row.get(QMunicipality.municipality.name),
                row.get(QMunicipality.municipality.nameSv),
                row.get(QMunicipality.municipality.active)));
    }

    public static Municipality parseMunicipality(Tuple row) {
        return new Municipality(
                row.get(QMunicipality.municipality.id),
                row.get(QMunicipality.municipality.name),
                row.get(QMunicipality.municipality.nameSv),
                row.get(QMunicipality.municipality.active));
    }

    public static Optional<LocalDate> OptionalLocalDate(DateTime sentTime) {
        if (sentTime != null) {
            return Optional.of(sentTime.toLocalDate());
        }
        return Optional.empty();
    }

    public static void assertSingleAffection(long affectedRows) {
        Assert.isTrue(affectedRows == 1, "Should have affected only one row. Affected: " + affectedRows);
    }
}
