package fi.om.municipalityinitiative.json;

import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.newdto.json.InitiativeJson;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.Municipality;
import fi.om.municipalityinitiative.newdto.service.Participant;
import fi.om.municipalityinitiative.newdto.ui.ParticipantCount;
import fi.om.municipalityinitiative.util.Maybe;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.util.ArrayList;

public class JsonJokuParseriTest {


    @Test
    @Ignore("Not a test. Use for debugging the parsed json data")
    public void jotain() throws IOException {

//        String json = "{\"participantCount\":{\"franchise\":{\"publicNames\":0,\"privateNames\":1,\"total\":1},\"noFranchise\":{\"publicNames\":2,\"privateNames\":0,\"total\":2},\"total\":3},\"publicParticipants\":{\"franchise\":[],\"noFranchise\":[{\"name\":\"Kikka kokko\",\"participateDate\":\"2013-02-14\",\"homeMunicipality\":{\"name\":\"Hartola\",\"id\":29}},{\"name\":\"Pauli Kärpänoja\",\"participateDate\":\"2013-02-14\",\"homeMunicipality\":{\"name\":\"Tampere\",\"id\":289}}]},\"name\":\"asdsdFADSFADFGAD\",\"id\":\"https://localhost:8443/api/v1/initiatives/1\",\"collectable\":true,\"municipality\":{\"name\":\"Helsinki\",\"id\":35},\"sentTime\":\"2013-02-14\",\"proposal\":\"Kuntalaisaloitteen otsikko\\r\\n\\r\\nKirjoita kuntalaisaloitteen otsikko. Otsikon tulee olla selkeä ja varsinaista sisältöä kuvaava.\\r\\n\\r\\nAloitteen sisältö\\r\\n\\r\\nKirjoita tähän varsinainen aloiteteksti - ...Kuntalaisaloitteen otsikko\\r\\n\\r\\nKirjoita kuntalaisaloitteen otsikko. Otsikon tulee olla selkeä ja varsinaista sisältöä kuvaava.\\r\\n\\r\\nAloitteen sisältö\\r\\n\\r\\nKirjoita tähän varsinainen aloiteteksti - ...Kuntalaisaloitteen otsikko\\r\\n\\r\\nKirjoita kuntalaisaloitteen otsikko. Otsikon tulee olla selkeä ja varsinaista sisältöä kuvaava.\\r\\n\\r\\nAloitteen sisältö\\r\\n\\r\\nKirjoita tähän varsinainen aloiteteksti - ...\",\"authorName\":\"Pauli Kärpänoja\",\"createTime\":\"2013-02-14\"}";

        final Municipality TAMPERE = new Municipality(1, "Tampere", "Tammerfors");

        ParticipantCount participantCount = new ParticipantCount();
        participantCount.getFranchise().setPrivateNames(10);
        participantCount.getFranchise().setPublicNames(1);
        participantCount.getNoFranchise().setPrivateNames(12);
        participantCount.getFranchise().setPublicNames(1);

        ArrayList<Participant> publicParticipants = Lists.<Participant>newArrayList();
        publicParticipants.add(new Participant(new LocalDate(2010, 1, 1), "Teemu Teekkari", true, TAMPERE));
        publicParticipants.add(new Participant(new LocalDate(2010, 1, 1), "Taina Teekkari", false, new Municipality(2L, "Tampere", "Tammerfors")));

        Initiative initiativeInfo = new Initiative();
        initiativeInfo.setId(1L);
        initiativeInfo.setName("Koirat pois lähiöistä");
        initiativeInfo.setProposal("Kakkaa on joka paikassa");
        initiativeInfo.setMunicipality(TAMPERE);
        initiativeInfo.setSentTime(Maybe.of(new LocalDate(2010, 5, 5))); // Cannot be absent at this point, mapper tries to get it's value
        initiativeInfo.setCreateTime(new LocalDate(2010, 1, 1));
        initiativeInfo.setAuthorName("Teemu Teekkari");
        initiativeInfo.setShowName(true);
        initiativeInfo.setManagementHash(Maybe.of("any"));

        InitiativeJson initiativeJson = InitiativeJson.from(initiativeInfo, publicParticipants, participantCount);

        new MappingJackson2HttpMessageConverter().getObjectMapper().writeValueAsString(initiativeInfo);

        String json = ObjectSerializer.objectToString(initiativeJson); // NOTE: Real api-controller uses MappingJackson2HttpMessageConverter initialized by WebConfiguration

        for (JsonJokuParseri.IndentedString s : JsonJokuParseri.toParts(json)) {
            System.out.println(s.getIndent() + ": " + StringUtils.repeat(" ", 3 * s.getIndent()) + s.getValue() + s.getLocalizationKey());
        }

    }
}
