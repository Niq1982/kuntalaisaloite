package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dao.MunicipalityDao;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.util.Locales;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;
import java.util.Locale;

public class MunicipalityService {

    @Resource
    private MunicipalityDao municipalityDao;

    @Transactional(readOnly = true)
    @Cacheable("allMunicipalities")
    public List<Municipality> findAllMunicipalities(Locale localeForOrdering) {
        return municipalityDao.findMunicipalities(Locales.LOCALE_FI.equals(localeForOrdering));
    }

}
