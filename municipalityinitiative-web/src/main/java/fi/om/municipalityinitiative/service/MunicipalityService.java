package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dao.MunicipalityDao;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.ui.MunicipalityInfoDto;
import fi.om.municipalityinitiative.util.Locales;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class MunicipalityService {

    @Resource
    private MunicipalityDao municipalityDao;

    @Transactional(readOnly = true)
    @Cacheable("allMunicipalities")
    public List<Municipality> findAllMunicipalities(Locale localeForOrdering) {
        return municipalityDao.findMunicipalities(Locales.LOCALE_FI.equals(localeForOrdering));
    }


    @Transactional(readOnly = true)
    @Cacheable("allMunicipalityInfos")
    public List<MunicipalityInfoDto> findAllMunicipalityInfos(Locale localeForOrdering) {
        return municipalityDao.findMunicipalitiesForEdit()
                .stream()
                .sorted((Locales.LOCALE_FI.equals(localeForOrdering))
                        ? Comparator.comparing(Municipality::getNameFi)
                        : Comparator.comparing(Municipality::getNameSv))
                .collect(Collectors.toList());
    }

}
