package fi.om.municipalityinitiative.pdf;

import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.service.NormalParticipant;
import fi.om.municipalityinitiative.dto.service.Participant;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.util.Membership;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.LocalDate;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class ParticipantToPdfExporterTest {

    private static String FILE = System.getProperty("user.dir")+"/Osallistujat.pdf";

    public static void main(String[] moimoiii) throws FileNotFoundException {

        try (FileOutputStream outputStream = new FileOutputStream(FILE)) {
            new ParticipantToPdfExporter(createInitiative(), createParticipants()).createPdf(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Initiative createInitiative() {
        Initiative initiative = new Initiative();
        initiative.setName("Koirat pois lähiöistä");
        initiative.setMunicipality(new Municipality(1, "Helsinki", "Helsingfors", false));
        initiative.setSentTime(Maybe.of(new LocalDate()));
        return initiative;
    }

    private static List<Participant> createParticipants() {
        List<Participant> participants = Lists.newArrayList();
        for (int i = 0; i < 1000; ++i) {
            Municipality municipality = new Municipality(new Random().nextLong(), RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10), false);
            NormalParticipant participant = new NormalParticipant();
            participant.setParticipateDate(new LocalDate());
            participant.setName(RandomStringUtils.randomAlphabetic(20));
            participant.setHomeMunicipality(Maybe.of(municipality));

            Random rnd = new Random();
            
            switch(rnd.nextInt(4)) {
                case 0:
                    participant.setMembership(Membership.community);
                    break;
                case 1:
                    participant.setMembership(Membership.company);
                    break;
                case 2:
                    participant.setMembership(Membership.property);
                    break;
                case 3:
                    participant.setMembership(Membership.none); // This should never end up in the list. Added here just for testing.
                    break;
            }
            participants.add(participant);
        }
        return participants;
    }

}
