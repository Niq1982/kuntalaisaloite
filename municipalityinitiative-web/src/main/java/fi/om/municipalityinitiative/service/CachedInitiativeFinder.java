package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dto.InitiativeSearch;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.ui.InitiativeListInfo;
import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.web.Urls;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class CachedInitiativeFinder {

    @Resource
    private InitiativeDao initiativeDao;

    @Resource
    private MunicipalityService municipalityService;

    @Cacheable("frontPageInitiatives")
    @Transactional(readOnly = true)
    public List<InitiativeListInfo> frontPageInitiatives() {
        InitiativeSearch search = new InitiativeSearch();
        search.setLimit(5);
        search.setShow(InitiativeSearch.Show.all);
        search.setOrderBy(InitiativeSearch.OrderBy.latest);

        return initiativeDao.findCached(search).list;

    }

    public List<Municipality> findAllMunicipalities(Locale locale) {
        return municipalityService.findAllMunicipalities(locale);
    }

    @Cacheable("municipality")
    public Optional<List<Municipality>> getMunicipalities(Optional<List<Long>> municipalityIds) {
        List<Municipality> municipalities = new ArrayList<>();
        if (!municipalityIds.isPresent()) {
            return Optional.empty();
        }
        for (Municipality o : municipalityService.findAllMunicipalities(Locales.LOCALE_FI)) {
            if (municipalityIds.get().contains(o.getId())) {
                municipalities.add(o);
            }
        }
        return Optional.ofNullable(municipalities);
    }

    @Cacheable("iframe")
    @Transactional(readOnly = true)
    public List<InitiativeListInfo> findIframeInitiatives(InitiativeSearch search) {
        search.setLimit(Math.min(search.getLimit(), Urls.MAX_IFRAME_INITIATIVE_COUNT));
        search.setShow(InitiativeSearch.Show.all);
        return initiativeDao.findCached(search).list;
    }
}
