package fi.om.municipalityinitiative.json;

import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.dto.json.InitiativeJson;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.service.NormalParticipant;
import fi.om.municipalityinitiative.dto.service.Participant;
import fi.om.municipalityinitiative.dto.ui.ParticipantCount;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.util.Membership;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.util.ArrayList;

public class JsonStringParserTest {

    // Use for debugging generated json-data
    public static void main(String[] args) throws IOException {

        final Municipality TAMPERE = new Municipality(1, "Tampere", "Tammerfors", false);

        ParticipantCount participantCount = new ParticipantCount();
        participantCount.setPrivateNames(10);
        participantCount.setPublicNames(1);

        ArrayList<Participant> publicParticipants = Lists.<Participant>newArrayList();

        publicParticipants.add(participant(TAMPERE, "Teemu Teekkari"));
        publicParticipants.add(participant(TAMPERE, "Taina Teekkari"));

        Initiative initiative = new Initiative();
        initiative.setId(1L);
        initiative.setName("Koirat pois lähiöistä");
        initiative.setProposal("Kakkaa on joka paikassa");
        initiative.setMunicipality(TAMPERE);
        initiative.setType(InitiativeType.UNDEFINED);
        initiative.setSentTime(Maybe.of(new LocalDate(2010, 5, 5))); // Cannot be absent at this point, mapper tries to get it's value
        initiative.setCreateTime(new LocalDate(2010, 1, 1));

        InitiativeJson initiativeJson = InitiativeJson.from(initiative, publicParticipants, participantCount, null);

        new MappingJackson2HttpMessageConverter().getObjectMapper().writeValueAsString(initiative);

        String json = ObjectSerializer.objectToString(initiativeJson); // NOTE: Real api-controller uses MappingJackson2HttpMessageConverter initialized by WebConfiguration

        for (JsonStringParser.IndentedString s : JsonStringParser.toParts(json)) {
            System.out.println(s.getIndent() + ": " + StringUtils.repeat(" ", 3 * s.getIndent()) + s.getValue() + s.getLocalizationKey());
        }

    }

    private static Participant participant(Municipality TAMPERE, String name) {
        NormalParticipant participant = new NormalParticipant();
        participant.setParticipateDate(new LocalDate(2010, 1, 1));
        participant.setName(name);
        participant.setHomeMunicipality(Maybe.of(TAMPERE));
        participant.setMembership(Membership.community);
        return participant;
    }
}
