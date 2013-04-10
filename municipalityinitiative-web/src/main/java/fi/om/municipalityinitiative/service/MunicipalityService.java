package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.newdao.MunicipalityDao;
import fi.om.municipalityinitiative.newdto.service.Municipality;
import fi.om.municipalityinitiative.util.Locales;

import javax.annotation.Resource;

import java.util.List;
import java.util.Locale;

public class MunicipalityService {

    @Resource
    private MunicipalityDao municipalityDao;

    public List<Municipality> findAllMunicipalities(Locale locale) {
        return municipalityDao.findMunicipalities(Locales.LOCALE_FI.equals(locale));
    }

}
