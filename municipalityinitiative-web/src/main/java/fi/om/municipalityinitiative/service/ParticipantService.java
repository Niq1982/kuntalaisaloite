package fi.om.municipalityinitiative.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import fi.om.municipalityinitiative.dao.AuthorDao;
import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dao.ParticipantDao;
import fi.om.municipalityinitiative.dto.NormalAuthor;
import fi.om.municipalityinitiative.dto.VerifiedAuthor;
import fi.om.municipalityinitiative.dto.service.*;
import fi.om.municipalityinitiative.dto.ui.ParticipantListInfo;
import fi.om.municipalityinitiative.dto.ui.ParticipantUICreateDto;
import fi.om.municipalityinitiative.dto.user.LoginUserHolder;
import fi.om.municipalityinitiative.dto.user.VerifiedUser;
import fi.om.municipalityinitiative.exceptions.InvalidParticipationConfirmationException;
import fi.om.municipalityinitiative.service.email.EmailService;
import fi.om.municipalityinitiative.service.id.NormalAuthorId;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import fi.om.municipalityinitiative.service.ui.VerifiedInitiativeService;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.util.hash.RandomHashGenerator;
import fi.om.municipalityinitiative.web.Urls;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static fi.om.municipalityinitiative.util.SecurityUtil.assertAllowance;

public class ParticipantService {

    @Resource
    private ParticipantDao participantDao;

    @Resource
    private AuthorDao authorDao;

    @Resource
    private InitiativeDao initiativeDao;

    @Resource
    private EmailService emailService;

    @Resource
    private VerifiedInitiativeService verifiedInitiativeService;

    public ParticipantService() {
    }

    @Transactional(readOnly = true)
    public List<ParticipantListInfo> findPublicParticipants(int offset, Long initiativeId) {

        if (initiativeDao.isVerifiableInitiative(initiativeId)) {
            return toVerifiedListInfo(participantDao.findVerifiedPublicParticipants(initiativeId, offset, Urls.MAX_PARTICIPANT_LIST_LIMIT), initiativeId);
        }
        else {
            return toNormalListInfo(participantDao.findNormalPublicParticipants(initiativeId, offset, Urls.MAX_PARTICIPANT_LIST_LIMIT), initiativeId);
        }

    }

    @Transactional(readOnly = true)
    public List<ParticipantListInfo> findAllParticipants(Long initiativeId, LoginUserHolder loginUserHolder, int offset) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);

        if (initiativeDao.isVerifiableInitiative(initiativeId)) {
            return toVerifiedListInfo(participantDao.findVerifiedAllParticipants(initiativeId, offset, Urls.MAX_PARTICIPANT_LIST_LIMIT), initiativeId);
        }
        else {
            return toNormalListInfo(participantDao.findNormalAllParticipants(initiativeId, offset, Urls.MAX_PARTICIPANT_LIST_LIMIT), initiativeId);
        }
    }

    private List<ParticipantListInfo> toVerifiedListInfo(List<VerifiedParticipant> participants, Long initiativeId) {
        Set<VerifiedUserId> authorIds = Sets.newHashSet();

        for (VerifiedAuthor author : authorDao.findVerifiedAuthors(initiativeId)) {
            authorIds.add(author.getId());
        }

        ArrayList<ParticipantListInfo> participantList = Lists.newArrayList();
        for (VerifiedParticipant participant : participants) {
            participantList.add(new ParticipantListInfo<>(participant, authorIds.contains(participant.getId())));
        }
        return participantList;
    }

    private List<ParticipantListInfo> toNormalListInfo(List<NormalParticipant> participants, Long initiativeId) {
        Set<NormalAuthorId> authorIds = Sets.newHashSet();

        for (NormalAuthor author : authorDao.findNormalAuthors(initiativeId)) {
            authorIds.add(author.getId());
        }

        ArrayList<ParticipantListInfo> participantList = Lists.newArrayList();
        for (NormalParticipant participant : participants) {
            participantList.add(new ParticipantListInfo<>(participant, authorIds.contains(new NormalAuthorId(participant.getId().toLong()))));
        }
        return participantList;
    }

    @Transactional(readOnly = false)
    public void deleteParticipant(Long initiativeId, LoginUserHolder loginUserHolder, Long participantId) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        assertAllowance("Delete participant", ManagementSettings.of(initiativeDao.get(initiativeId)).isAllowParticipation());

        if (initiativeDao.get(initiativeId).getType().isNotVerifiable()) {
            participantDao.deleteParticipant(initiativeId, participantId);
            initiativeDao.denormalizeParticipantCountForNormalInitiative(initiativeId);
        }
        else {
            participantDao.deleteVerifiedParticipant(initiativeId, participantId);
            initiativeDao.denormalizeParticipantCountForVerifiedInitiative(initiativeId);
        }


    }

    @Transactional(readOnly = false)
    public Long confirmParticipation(Long participantId, String confirmationCode) {
        Maybe<Long> initiativeId = participantDao.getInitiativeIdByParticipant(participantId);
        if (initiativeId.isNotPresent()) {
            throw new InvalidParticipationConfirmationException("No participant with id: " + participantId);
        }

        Initiative initiative = initiativeDao.get(initiativeId.get());
        assertAllowance("Confirm participation", ManagementSettings.of(initiative).isAllowParticipation());
        NormalParticipant normalParticipant = participantDao.confirmParticipation(participantId, confirmationCode);

        participantDao.increaseParticipantCountFor(initiativeId.get(), normalParticipant.isShowName(),
                normalParticipant.getHomeMunicipality().isPresent() && normalParticipant.getHomeMunicipality().get().getId().equals(initiative.getMunicipality().getId())
                );

        return initiativeId.get();
    }

    @Transactional(readOnly = false)
    public Long createParticipant(ParticipantUICreateDto participant, Long initiativeId, Locale locale) {

        assertAllowance("Allowed to participate", ManagementSettings.of(initiativeDao.get(initiativeId)).isAllowParticipation());

        ParticipantCreateDto participantCreateDto = ParticipantCreateDto.parse(participant, initiativeId);
        participantCreateDto.setMunicipalityInitiativeId(initiativeId);


        String confirmationCode = RandomHashGenerator.shortHash();
        Long participantId = participantDao.create(participantCreateDto, confirmationCode);

        emailService.sendParticipationConfirmation(
                initiativeId,
                participant.getParticipantEmail(),
                participantId,
                confirmationCode,
                locale
        );

        return participantId;
    }

}
