package fi.om.municipalityinitiative.service.email;

import com.google.common.collect.Maps;
import fi.om.municipalityinitiative.dao.*;
import fi.om.municipalityinitiative.dto.Author;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.MunicipalityLoginDetails;
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

    @Resource
    AttachmentDao attachmentDao;

    @Resource
    LocationDao locationDao;

    @Resource
    MunicipalityUserDao municipalityUserDao;

    @Resource
    FollowInitiativeDao followInitiativeDao;

    public Initiative get(Long initiativeId) {
        return get(initiativeId, false);
    }

    public Initiative get(Long initiativeId, boolean getDeleted) {
        return initiativeDao.get(initiativeId, getDeleted);
    }

    public List<? extends Author> findAuthors(Long id) {
        return authorDao.findAllAuthors(id);
    }

    public List<String> getAuthorEmails(Long initiativeId) {
        return authorDao.findAuthorEmails(initiativeId);
    }

    public String getMunicipalityEmail(Long id) {
        return municipalityDao.getMunicipalityInfo(id).getEmail();
    }

    public Author getAuthor(NormalAuthorId authorId) {
        return authorDao.getNormalAuthor(authorId);
    }

    public Map<String, String> getManagementLinksByAuthorEmails(Long initiativeId) {

        Map<String, String> managementLinks = authorDao.getManagementLinksByAuthorEmails(initiativeId);

        authorDao.findVerifiedAuthors(initiativeId)
                .forEach(a -> managementLinks.put(a.getContactInfo().getEmail(), null));

        return managementLinks;
    }

    public int getAcceptedAttachmentCount(Long initiativeId) {
        return attachmentDao.findAcceptedAttachments(initiativeId).size();
    }

    public boolean hasLocationAttached(Long initiativeId){
        return locationDao.getLocations(initiativeId).size() > 0;
    }

    public MunicipalityLoginDetails getMunicipalityDecisionHash(Long initiativeId) {
        return municipalityUserDao.getMunicipalityUserHashAttachedToInitiative(initiativeId);
    }

    public Map<String, String> getFollowers(Long initiativeId) {
        return followInitiativeDao.listFollowers(initiativeId);
    }
}
