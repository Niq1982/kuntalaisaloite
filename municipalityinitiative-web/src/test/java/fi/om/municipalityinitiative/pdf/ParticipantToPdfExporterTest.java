package fi.om.municipalityinitiative.pdf;

import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.service.NormalParticipant;
import fi.om.municipalityinitiative.dto.service.VerifiedParticipant;
import fi.om.municipalityinitiative.util.InitiativeType;
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

    private static String NORMAL_FILE = System.getProperty("user.dir")+"/Osallistujat.pdf";
    private static String VETUMA_FILE = System.getProperty("user.dir")+"/OsallistujatVetuma.pdf";

    public static void main(String[] moimoiii) throws FileNotFoundException {

        Municipality municipality = new Municipality(1, "Helsinki", "Helsingfors", true);

        try (FileOutputStream outputStream = new FileOutputStream(NORMAL_FILE)) {
            new ParticipantToPdfExporter(createInitiative(municipality, false), createNormalParticipants(municipality)).createPdf(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (FileOutputStream outputStream = new FileOutputStream(VETUMA_FILE)) {
            new ParticipantToPdfExporter(createInitiative(municipality, true), createVerifiedParticipants(municipality)).createPdf(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Initiative createInitiative(Municipality municipality, boolean verified) {
        Initiative initiative = new Initiative();
        initiative.setName("Koirat pois lähiöistä");
        initiative.setType(verified ? InitiativeType.COLLABORATIVE_CITIZEN : InitiativeType.COLLABORATIVE);
        initiative.setMunicipality(municipality);
        initiative.setSentTime(Maybe.of(new LocalDate()));
        return initiative;
    }

    private static List<NormalParticipant> createNormalParticipants(Municipality municipality) {
        List<NormalParticipant> participants = Lists.newArrayList();
        Random random = new Random();
        for (int i = 0; i < 1000; ++i) {
            Municipality m =
                    random.nextInt(5) != 0
                            ? municipality
                            : new Municipality(random.nextLong(), RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10), false);
            NormalParticipant participant = new NormalParticipant();
            participant.setParticipateDate(new LocalDate());
            participant.setName(RandomStringUtils.randomAlphabetic(20));
            participant.setHomeMunicipality(Maybe.of(m));
            participant.setMunicipalityVerified(municipality == m);

            switch( m == municipality ? 3 : random.nextInt(3)) {
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
                    participant.setMembership(Membership.none); // Go here if municipality was the same
                    break;
            }
            participants.add(participant);
        }
        return participants;
    }

    private static List<VerifiedParticipant> createVerifiedParticipants(Municipality municipality) {
        List<VerifiedParticipant> participants = Lists.newArrayList();
        for (int i = 0; i < 1000; ++i) {
            VerifiedParticipant participant = new VerifiedParticipant();
            participant.setParticipateDate(new LocalDate());
            participant.setName(RandomStringUtils.randomAlphabetic(20));
            participant.setMunicipalityVerified(new Random().nextInt(100) % 10 == 0);
            participant.setHomeMunicipality(Maybe.of(municipality));
            participants.add(participant);
        }
        return participants;
    }

}
