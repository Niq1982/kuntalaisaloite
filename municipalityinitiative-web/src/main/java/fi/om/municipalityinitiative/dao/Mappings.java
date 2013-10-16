package fi.om.municipalityinitiative.dao;

import com.mysema.query.Tuple;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.sql.QMunicipality;
import fi.om.municipalityinitiative.util.Maybe;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class Mappings {

    public static int nullToZero(Integer integer) {
        if (integer == null) {
            return 0;
        }
        return integer;
    }

    public static Maybe<Municipality> parseMaybeMunicipality(Tuple row) {
        Long municipalityId = row.get(QMunicipality.municipality.id);
        if (municipalityId == null) {
            return Maybe.absent();
        }
        return Maybe.of(new Municipality(
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

    public static Maybe<LocalDate> maybeLocalDate(DateTime sentTime) {
        if (sentTime != null) {
            return Maybe.of(sentTime.toLocalDate());
        }
        return Maybe.absent();
    }
}
