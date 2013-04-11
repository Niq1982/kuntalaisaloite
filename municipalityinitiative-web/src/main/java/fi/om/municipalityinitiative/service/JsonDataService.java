package fi.om.municipalityinitiative.service;

import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdao.MunicipalityDao;
import fi.om.municipalityinitiative.newdao.ParticipantDao;
import fi.om.municipalityinitiative.newdto.InitiativeSearch;
import fi.om.municipalityinitiative.newdto.json.InitiativeJson;
import fi.om.municipalityinitiative.newdto.json.InitiativeListJson;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.Municipality;
import fi.om.municipalityinitiative.newdto.service.Participant;
import fi.om.municipalityinitiative.newdto.ui.InitiativeListInfo;
import fi.om.municipalityinitiative.newdto.ui.ParticipantCount;
import fi.om.municipalityinitiative.newweb.ApiController;
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

    public List<InitiativeListJson> findJsonInitiatives(InitiativeSearch search) {
        List<InitiativeListJson> result = Lists.newArrayList();
        for (InitiativeListInfo initiativeListInfo : initiativeDao.find(search)) {
            result.add(new InitiativeListJson(initiativeListInfo));
        }
        return result;
    }

    public InitiativeJson getInitiative(Long id) {
        return InitiativeJson.from(
                initiativeDao.getByIdWithOriginalAuthor(id),
                participantDao.findPublicParticipants(id),
                participantDao.getParticipantCount(id));

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
        initiative.setCollectable(true);
        initiative.setCreateTime(new LocalDate(2012, 12, 1));
        initiative.setId(1L);
        initiative.setName("Koirat pois lähiöistä");
        initiative.setParticipantCount(2);
        return Collections.singletonList(new InitiativeListJson(initiative));
    }

    public static InitiativeJson createInitiativeJsonObject() {

        ParticipantCount participantCount = new ParticipantCount();
        participantCount.getFranchise().setPrivateNames(10);
        participantCount.getFranchise().setPublicNames(1);
        participantCount.getNoFranchise().setPrivateNames(12);
        participantCount.getFranchise().setPublicNames(1);

        ArrayList<Participant> publicParticipants = Lists.<Participant>newArrayList();
        publicParticipants.add(new Participant(new LocalDate(2010, 1, 1), "Teemu Teekkari", true, ApiController.TAMPERE));

        Initiative initiativeInfo = new Initiative();
        initiativeInfo.setId(1L);
        initiativeInfo.setName("Koirat pois lähiöistä");
        initiativeInfo.setProposal("Kakkaa on joka paikassa");
        initiativeInfo.setMunicipality(ApiController.TAMPERE);
        initiativeInfo.setSentTime(Maybe.<LocalDate>fromNullable(null));
        initiativeInfo.setCreateTime(new LocalDate(2010, 1, 1));
        initiativeInfo.setAuthorName("Teemu Teekkari");
        initiativeInfo.setShowName(true);
        initiativeInfo.setType(InitiativeType.COLLABORATIVE);

        InitiativeJson initiativeJson = InitiativeJson.from(initiativeInfo, publicParticipants, participantCount);

        return initiativeJson;

    }
}
