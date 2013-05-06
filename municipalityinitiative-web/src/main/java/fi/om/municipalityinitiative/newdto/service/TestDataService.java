package fi.om.municipalityinitiative.newdto.service;

import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdao.ParticipantDao;
import fi.om.municipalityinitiative.newdto.ui.InitiativeDraftUIEditDto;
import fi.om.municipalityinitiative.newdto.ui.ParticipantUICreateDto;
import fi.om.municipalityinitiative.util.*;
import fi.om.municipalityinitiative.web.Urls;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

public class TestDataService {

    public static final String DEFAULT_MANAGEMENT_HASH = "managementHash";
    @Resource
    InitiativeDao initiativeDao;

    @Resource
    private ParticipantDao participantDao;

    @Transactional(readOnly = false)
    public Long createTestMunicipalityInitiative(Initiative initiative) {

        Long initiativeId = initiativeDao.prepareInitiative(initiative.getMunicipality().getId());
        Long participantId = participantDao.prepareParticipant(initiativeId, initiative.getMunicipality().getId(), null, Membership.community, false);
//        initiativeDao.assignAuthor(initiativeId, participantId, DEFAULT_MANAGEMENT_HASH);
        Long authorId = initiativeDao.createAuthor(initiativeId, participantId, DEFAULT_MANAGEMENT_HASH);
        initiativeDao.assignAuthor(initiativeId, authorId);

        InitiativeDraftUIEditDto editDto = new InitiativeDraftUIEditDto();
        editDto.setShowName(initiative.getAuthor().isShowName());
        editDto.setName(initiative.getName());
        editDto.setContactInfo(initiative.getAuthor().getContactInfo());
        editDto.setProposal(initiative.getProposal()
                + "\n\n"
                + "Linkki hallintasivulle: " + Urls.get(Locales.LOCALE_FI).loginAuthor(initiativeId, DEFAULT_MANAGEMENT_HASH)
        );
        initiativeDao.editInitiativeDraft(initiativeId, editDto);

        initiativeDao.updateInitiativeType(initiativeId, initiative.getType());
        if (initiative.getType() == InitiativeType.SINGLE) {
            initiativeDao.markInitiativeAsSent(initiativeId);
        }
        initiativeDao.updateInitiativeState(initiativeId, initiative.getState());

        return initiativeId;
    }
    
    @Transactional(readOnly = false)
    public void createTestParticipant(Long initiativeId, ParticipantUICreateDto createDto, int amount) {
        for (int i = 0; i < amount; ++i) {
            Long participantId = participantDao.create(ParticipantCreateDto.parse(createDto, initiativeId), "confirmationCode");
            participantDao.confirmParticipation(participantId, "confirmationCode");
        }
    }
    
}