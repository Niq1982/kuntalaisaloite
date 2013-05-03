package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.newdao.AuthorDao;
import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdto.LoginUserHolder;
import fi.om.municipalityinitiative.newdto.service.AuthorInvitation;
import fi.om.municipalityinitiative.newdto.service.ManagementSettings;
import fi.om.municipalityinitiative.util.RandomHashGenerator;
import fi.om.municipalityinitiative.util.SecurityUtil;
import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

public class AuthorService {

    @Resource
    AuthorDao authorDao;

    @Resource
    InitiativeDao initiativeDao;

    @Transactional(readOnly = false)
    public void createAuthorInvitation(Long initiativeId, LoginUserHolder loginUserHolder, String email, String name) {

        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        ManagementSettings managementSettings = ManagementSettings.of(initiativeDao.getByIdWithOriginalAuthor(initiativeId));
        SecurityUtil.assertAllowance("Invite authors", managementSettings.isAllowInviteAuthors());

        AuthorInvitation authorInvitation = new AuthorInvitation();
        authorInvitation.setInitiativeId(initiativeId);
        authorInvitation.setEmail(email);
        authorInvitation.setName(name);
        authorInvitation.setConfirmationCode(RandomHashGenerator.randomString(20));
        authorInvitation.setInvitationTime(new DateTime());

        authorDao.addAuthorInvitation(authorInvitation);

    }


}
