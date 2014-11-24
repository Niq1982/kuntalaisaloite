package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dao.*;
import fi.om.municipalityinitiative.dto.YouthInitiativeCreateDto;
import fi.om.municipalityinitiative.dto.ui.InitiativeDraftUIEditDto;
import fi.om.municipalityinitiative.exceptions.AccessDeniedException;
import fi.om.municipalityinitiative.service.email.EmailService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

public class YouthInitiativeService {

    @Resource
    private ParticipantDao participantDao;

    @Resource
    private AuthorDao authorDao;

    @Resource
    private InitiativeDao initiativeDao;

    @Resource
    private EmailService emailService;

    @Resource
    private AuthorMessageDao authorMessageDao;

    @Resource
    private MunicipalityDao municipalityDao;

    @Transactional
    public String prepareYouthInitiative(YouthInitiativeCreateDto createDto, Long youthInitiativeId) {

        Long municipality = createDto.getMunicipality();
        if (!municipalityDao.getMunicipality(municipality).isActive()) {
            throw new AccessDeniedException("Municipality is not active for initiatives: " + municipality);
        }
        return "ok";
    }
}
