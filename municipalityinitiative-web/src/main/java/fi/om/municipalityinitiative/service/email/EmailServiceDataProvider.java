package fi.om.municipalityinitiative.service.email;

import fi.om.municipalityinitiative.dao.AuthorDao;
import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dao.MunicipalityDao;
import fi.om.municipalityinitiative.dao.ParticipantDao;
import fi.om.municipalityinitiative.dto.Author;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.Participant;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

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

    public List<Author> findAuthors(Long id) {
        return authorDao.findAuthors(id);
    }

    public List<String> getAuthorEmails(Long initiativeId) {
        return authorDao.getAuthorEmails(initiativeId);
    }

    public List<Participant> findAllParticipants(Long initiativeId) {
        return participantDao.findAllParticipants(initiativeId);
    }

    public String getMunicipalityEmail(Long id) {
        return municipalityDao.getMunicipalityEmail(id);
    }

    public Author getAuthor(Long authorId) {
        return authorDao.getAuthor(authorId);
    }

    public Map<String, String> getManagementLinksByAuthorEmails(Long initiativeId) {
        return authorDao.getManagementLinksByAuthorEmails(initiativeId);
    }
}
