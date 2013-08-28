package fi.om.municipalityinitiative.dto.service;

import fi.om.municipalityinitiative.dao.AuthorDao;
import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dao.ParticipantDao;
import fi.om.municipalityinitiative.dto.ui.InitiativeDraftUIEditDto;
import fi.om.municipalityinitiative.dto.ui.ParticipantUICreateDto;
import fi.om.municipalityinitiative.service.id.NormalAuthorId;
import fi.om.municipalityinitiative.util.*;
import fi.om.municipalityinitiative.web.Urls;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

public class TestDataService {

    @Resource
    InitiativeDao initiativeDao;

    @Resource
    AuthorDao authorDao;

    @Resource
    private ParticipantDao participantDao;

    @Transactional(readOnly = false)
    public Long createTestMunicipalityInitiative(TestDataTemplates.InitiativeTemplate template) {

        String managementHash = RandomHashGenerator.randomString(10);

        Long initiativeId = initiativeDao.prepareInitiative(template.initiative.getMunicipality().getId());
        Long participantId = participantDao.prepareParticipant(initiativeId, template.initiative.getMunicipality().getId(), null, Membership.community, template.getAuthor().getContactInfo().isShowName());
        NormalAuthorId authorId = authorDao.createAuthor(initiativeId, participantId, managementHash);

        InitiativeDraftUIEditDto editDto = new InitiativeDraftUIEditDto();
        editDto.setName(template.initiative.getName());
        editDto.setContactInfo(template.author.getContactInfo());
        editDto.setProposal(template.initiative.getProposal()
                + "\n\n"
                + "Linkki hallintasivulle: " + Urls.get(Locales.LOCALE_FI).loginAuthor(managementHash)
        );
        editDto.setExtraInfo(template.initiative.getExtraInfo());
        initiativeDao.editInitiativeDraft(initiativeId, editDto);
        authorDao.updateAuthorInformation(authorId, editDto.getContactInfo());

        initiativeDao.updateInitiativeType(initiativeId, template.initiative.getType());
        if (template.initiative.getType() == InitiativeType.SINGLE) {
            initiativeDao.markInitiativeAsSent(initiativeId);
        }
        initiativeDao.updateInitiativeState(initiativeId, template.initiative.getState());

        return initiativeId;
    }
    
    @Transactional(readOnly = false)
    public void createTestParticipant(Long initiativeId, ParticipantUICreateDto createDto) {
        Long participantId = participantDao.create(ParticipantCreateDto.parse(createDto, initiativeId), "confirmationCode");
        participantDao.confirmParticipation(participantId, "confirmationCode");
    }
    
}