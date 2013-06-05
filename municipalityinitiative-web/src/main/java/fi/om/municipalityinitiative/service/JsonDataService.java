package fi.om.municipalityinitiative.service;

import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.dto.Author;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.dto.ui.PublicAuthors;
import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dao.MunicipalityDao;
import fi.om.municipalityinitiative.dao.ParticipantDao;
import fi.om.municipalityinitiative.dto.InitiativeSearch;
import fi.om.municipalityinitiative.dto.json.InitiativeJson;
import fi.om.municipalityinitiative.dto.json.InitiativeListJson;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.service.Participant;
import fi.om.municipalityinitiative.dto.ui.InitiativeListInfo;
import fi.om.municipalityinitiative.dto.ui.ParticipantCount;
import fi.om.municipalityinitiative.web.controller.ApiController;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.Maybe;
import org.joda.time.LocalDate;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JsonDataService {

    @Resource
    InitiativeDao initiativeDao;

    @Resource
    ParticipantDao participantDao;

    @Resource
    MunicipalityDao municipalityDao;

    @Resource
    AuthorService authorService;

    public List<InitiativeListJson> findJsonInitiatives(InitiativeSearch search) {
        List<InitiativeListJson> result = Lists.newArrayList();
        for (InitiativeListInfo initiativeListInfo : initiativeDao.find(search)) {
            result.add(new InitiativeListJson(initiativeListInfo));
        }
        return result;
    }

    public InitiativeJson getInitiative(Long id) {
        return InitiativeJson.from(
                initiativeDao.get(id),
                participantDao.findPublicParticipants(id),
                participantDao.getParticipantCount(id),
                authorService.findPublicAuthors(id));

    }

    public List<Municipality> getMunicipalities() {
        return municipalityDao.findMunicipalities(true);
    }

    public Municipality getMunicipality(Long id) {
        return municipalityDao.getMunicipality(id);
    }

    public static List<InitiativeListJson> createInitiativeListJsonObject() {
        InitiativeListInfo initiative = new InitiativeListInfo();
        initiative.setMunicipality(ApiController.TAMPERE);
        initiative.setSentTime(Maybe.of(new LocalDate(2012, 12, 24)));
        initiative.setCollaborative(true);
        initiative.setCreateTime(new LocalDate(2012, 12, 1));
        initiative.setId(1L);
        initiative.setName("Koirat pois lähiöistä");
        initiative.setParticipantCount(2);
        return Collections.singletonList(new InitiativeListJson(initiative));
    }

    public static InitiativeJson createInitiativeJsonObject() {

        ParticipantCount participantCount = new ParticipantCount();
        participantCount.setPrivateNames(10);
        participantCount.setPublicNames(1);

        ArrayList<Participant> publicParticipants = Lists.<Participant>newArrayList();

        Participant participant = new Participant();
        participant.setParticipateDate(new LocalDate(2010, 1, 1));
        participant.setName("Teemu Teekkari");
        participant.setHomeMunicipality(ApiController.TAMPERE);

        publicParticipants.add(participant);

        Initiative initiativeInfo = new Initiative();
        initiativeInfo.setId(1L);
        initiativeInfo.setName("Koirat pois lähiöistä");
        initiativeInfo.setProposal("Kakkaa on joka paikassa");
        initiativeInfo.setMunicipality(ApiController.TAMPERE);
        initiativeInfo.setSentTime(Maybe.<LocalDate>fromNullable(null));
        initiativeInfo.setCreateTime(new LocalDate(2010, 1, 1));

        initiativeInfo.setType(InitiativeType.COLLABORATIVE);

        PublicAuthors authors = new PublicAuthors(createAuthors());

        InitiativeJson initiativeJson = InitiativeJson.from(initiativeInfo, publicParticipants, participantCount, authors);

        return initiativeJson;

    }

    private static List<Author> createAuthors() {
        List<Author> authors = Lists.newArrayList();

        Author author1 = new Author();
        ContactInfo contactInfo1 = new ContactInfo();
        contactInfo1.setName("Teemu Teekkari");
        contactInfo1.setShowName(true);
        author1.setContactInfo(contactInfo1);
        author1.setMunicipality(ApiController.TAMPERE);
        authors.add(author1);

        Author author2 = new Author();
        ContactInfo contactInfo2 = new ContactInfo();
        contactInfo2.setShowName(false);
        author2.setContactInfo(contactInfo2);
        author2.setMunicipality(ApiController.TAMPERE);
        authors.add(author2);

        return authors;
    }
}
