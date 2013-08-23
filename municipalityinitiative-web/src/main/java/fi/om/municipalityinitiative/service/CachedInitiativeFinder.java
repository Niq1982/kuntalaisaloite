package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dto.InitiativeSearch;
import fi.om.municipalityinitiative.dto.ui.InitiativeListInfo;
import fi.om.municipalityinitiative.dto.user.LoginUserHolder;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.service.ui.PublicInitiativeService;
import org.springframework.cache.annotation.Cacheable;

import javax.annotation.Resource;

import java.util.List;

public class CachedInitiativeFinder {

    @Resource
    private PublicInitiativeService initiativeService;


    @Cacheable("frontPageInitiatives")
    public List<InitiativeListInfo> frontPageInitiatives() {
        InitiativeSearch search = new InitiativeSearch();
        search.setLimit(3);
        search.setShow(InitiativeSearch.Show.all);
        search.setOrderBy(InitiativeSearch.OrderBy.latest);

        return initiativeService.findMunicipalityInitiatives(search, new LoginUserHolder<>(User.anonym())).list;

    }
}
