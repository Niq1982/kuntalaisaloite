package fi.om.municipalityinitiative.service.email;

import com.google.common.collect.Maps;
import fi.om.municipalityinitiative.dao.AuthorDao;
import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dao.MunicipalityDao;
import fi.om.municipalityinitiative.dao.ParticipantDao;
import fi.om.municipalityinitiative.dto.Author;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.Participant;
import fi.om.municipalityinitiative.service.id.NormalAuthorId;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional(readOnly = true)
public class EmailServiceDataProvider {

    @Resource
    InitiativeDao initiativeDao;

    @Resource
    AuthorDao authorDao;

    @Resource
    ParticipantDao participantDao;

    @Resource
    MunicipalityDao municipalityDao;

    public Initiative get(Long initiativeId) {
        return initiativeDao.get(initiativeId);
    }

    public List<? extends Author> findAuthors(Long id) {
        if (initiativeDao.isVerifiableInitiative(id)) {
            return authorDao.findVerifiedAuthors(id);
        }
        return authorDao.findNormalAuthors(id);
    }

    public List<String> getAuthorEmails(Long initiativeId) {
        if (initiativeDao.isVerifiableInitiative(initiativeId)) {
            return authorDao.findVerifiedAuthorEmails(initiativeId);
        }
        return authorDao.findNormalAuthorEmails(initiativeId);
    }

    public List<? extends Participant> findAllParticipants(Long initiativeId) {
        if (initiativeDao.isVerifiableInitiative(initiativeId)) {
            return participantDao.findVerifiedAllParticipants(initiativeId, 0, fi.om.municipalityinitiative.web.Urls.MAX_PARTICIPANT_LIST_LIMIT);
        }
        return participantDao.findNormalAllParticipants(initiativeId, 0, fi.om.municipalityinitiative.web.Urls.MAX_PARTICIPANT_LIST_LIMIT);
    }

    public String getMunicipalityEmail(Long id) {
        return municipalityDao.getMunicipalityEmail(id);
    }

    public Author getAuthor(NormalAuthorId authorId) {
        return authorDao.getNormalAuthor(authorId);
    }

    public Map<String, String> getManagementLinksByAuthorEmails(Long initiativeId) {
        if (initiativeDao.isVerifiableInitiative(initiativeId)) {
            HashMap<String, String> authorEmails = Maps.newHashMap();
            for (String email : authorDao.findVerifiedAuthorEmails(initiativeId)) {
                authorEmails.put(email, null);
            }
            return authorEmails;
        }
        return authorDao.getManagementLinksByAuthorEmails(initiativeId);
    }
}
