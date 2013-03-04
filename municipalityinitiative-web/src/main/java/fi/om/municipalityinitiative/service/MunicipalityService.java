package fi.om.municipalityinitiative.service;

import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.newdao.MunicipalityDao;
import fi.om.municipalityinitiative.newdto.service.Municipality;
import fi.om.municipalityinitiative.newdto.ui.MunicipalityInfo;
import fi.om.municipalityinitiative.util.Locales;

import javax.annotation.Resource;

import java.util.List;
import java.util.Locale;

public class MunicipalityService {

    @Resource
    private MunicipalityDao municipalityDao;

    public List<MunicipalityInfo> findAllMunicipalities(Locale locale) {
        List<MunicipalityInfo> municipalities = Lists.newArrayList();
        boolean finnishLocale = Locales.LOCALE_FI.equals(locale);
        for (Municipality m : municipalityDao.findMunicipalities(finnishLocale)) {
            MunicipalityInfo municipalityInfo = new MunicipalityInfo();
            municipalityInfo.setName(finnishLocale ? m.getFinnishName() : m.getSwedishName());
            municipalityInfo.setId(m.getId());
            municipalities.add(municipalityInfo);
        }

        return municipalities;
    }
}
