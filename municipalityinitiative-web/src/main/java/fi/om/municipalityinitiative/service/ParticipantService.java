package fi.om.municipalityinitiative.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import fi.om.municipalityinitiative.dao.AuthorDao;
import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dao.ParticipantDao;
import fi.om.municipalityinitiative.dto.service.*;
import fi.om.municipalityinitiative.dto.ui.ParticipantListInfo;
import fi.om.municipalityinitiative.dto.ui.ParticipantUICreateDto;
import fi.om.municipalityinitiative.dto.user.LoginUserHolder;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.exceptions.InvalidParticipationConfirmationException;
import fi.om.municipalityinitiative.service.email.EmailService;
import fi.om.municipalityinitiative.service.id.NormalAuthorId;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import fi.om.municipalityinitiative.service.ui.MunicipalMembershipSolver;
import fi.om.municipalityinitiative.service.ui.VerifiedInitiativeService;
import fi.om.municipalityinitiative.util.hash.RandomHashGenerator;
import fi.om.municipalityinitiative.web.Urls;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
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

        return toListInfo(initiativeId,
                participantDao.findAllParticipants(
                        initiativeId,
                        true,
                        offset,
                        Urls.MAX_PARTICIPANT_LIST_LIMIT)
        );

    }

    private List<ParticipantListInfo> toListInfo(Long initiativeId, List<Participant> allParticipants) {

        Set<VerifiedUserId> verifiedAuthorIds = Sets.newHashSet();
        Set<NormalAuthorId> normalAuthorIds = Sets.newHashSet();

        authorDao.findVerifiedAuthors(initiativeId).forEach(verifiedAuthor -> verifiedAuthorIds.add(verifiedAuthor.getId()));
        authorDao.findNormalAuthors(initiativeId).forEach(normalAuthor -> normalAuthorIds.add(normalAuthor.getId()));

        List<ParticipantListInfo> result = Lists.newArrayList();

        allParticipants.forEach(participant -> {
            result.add(new ParticipantListInfo(
                    participant,
                    participant.isVerified() ? verifiedAuthorIds.contains(participant.getId()) : normalAuthorIds.contains(new NormalAuthorId(participant.getId().toLong()))
            ));

        });

        return result;


    }

    @Transactional(readOnly = true)
    public List<ParticipantListInfo> findAllParticipants(Long initiativeId, LoginUserHolder loginUserHolder, int offset) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);

        return toListInfo(initiativeId,
                participantDao.findAllParticipants(
                        initiativeId,
                        false,
                        offset,
                        Urls.MAX_PARTICIPANT_LIST_LIMIT)
        );

    }

    @Transactional(readOnly = false)
    public void deleteParticipant(Long initiativeId, LoginUserHolder loginUserHolder, Long participantId, boolean isVerifiedParticipant) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        assertAllowance("Delete participant", ManagementSettings.of(initiativeDao.get(initiativeId)).isAllowParticipation());

        if (isVerifiedParticipant) {
            participantDao.deleteVerifiedParticipant(initiativeId, participantId);
        }
        else {
            participantDao.deleteParticipant(initiativeId, participantId);
        }
        initiativeDao.denormalizeParticipantCounts(initiativeId);

    }

    @Transactional(readOnly = false)
    public Long confirmParticipation(Long participantId, String confirmationCode) {
        Optional<Long> initiativeId = participantDao.getInitiativeIdByParticipant(participantId);
        if (!initiativeId.isPresent()) {
            throw new InvalidParticipationConfirmationException("No participant with id: " + participantId);
        }

        Initiative initiative = initiativeDao.get(initiativeId.get());
        assertAllowance("Confirm participation", ManagementSettings.of(initiative).isAllowParticipation());
        NormalParticipant normalParticipant = participantDao.confirmParticipation(participantId, confirmationCode);

        participantDao.increaseParticipantCountFor(initiativeId.get(),
                normalParticipant.isShowName(),
                normalParticipant.getHomeMunicipality().isPresent() && normalParticipant.getHomeMunicipality().get().getId().equals(initiative.getMunicipality().getId())
                );

        return initiativeId.get();
    }

    @Transactional(readOnly = false)
    public Long createParticipant(ParticipantUICreateDto participant, Long initiativeId, Locale locale) {

        Initiative initiative = initiativeDao.get(initiativeId);
        assertAllowance("Allowed to participate", ManagementSettings.of(initiative).isAllowParticipation());

        MunicipalMembershipSolver municipalMembershipSolver = new MunicipalMembershipSolver(User.anonym(), initiative.getMunicipality().getId(), participant);

        municipalMembershipSolver.assertMunicipalityOrMembershipForNormalInitiative();

        ParticipantCreateDto participantCreateDto = ParticipantCreateDto.parse(participant, initiativeId);
        participantCreateDto.setMunicipalityInitiativeId(initiativeId);

        String confirmationCode = RandomHashGenerator.shortHash();

        Long participantId = participantDao.create(initiative.getId(),
                participant.getParticipantName(),
                participant.getShowName(),
                participant.getParticipantEmail(),
                confirmationCode,
                municipalMembershipSolver.getHomeMunicipality(),
                municipalMembershipSolver.getMunicipalMembership());

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
