package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dao.*;
import fi.om.municipalityinitiative.dto.YouthInitiativeCreateDto;
import fi.om.municipalityinitiative.dto.ui.InitiativeDraftUIEditDto;
import fi.om.municipalityinitiative.exceptions.AccessDeniedException;
import fi.om.municipalityinitiative.service.email.EmailService;
import fi.om.municipalityinitiative.util.Membership;
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
    public Long prepareYouthInitiative(YouthInitiativeCreateDto createDto) {

        Long municipality = createDto.getMunicipality();
        if (!municipalityDao.getMunicipality(municipality).isActive()) {
            throw new AccessDeniedException("Municipality is not active for initiatives: " + municipality);
        }

        Long youthInitiativeId = initiativeDao.prepareYouthInitiative(createDto.getYouthInitiativeId(), createDto.getName(), createDto.getProposal(), createDto.getExtraInfo(), createDto.getMunicipality());

        Long participantId = participantDao.prepareConfirmedParticipant(
                youthInitiativeId,
                createDto.getMunicipality(),
                createDto.getContactInfo().getEmail(),
                Membership.none,
                true);

        return youthInitiativeId;
    }
}
