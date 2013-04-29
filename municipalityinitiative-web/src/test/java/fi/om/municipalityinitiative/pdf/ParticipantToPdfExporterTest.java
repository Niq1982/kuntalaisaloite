package fi.om.municipalityinitiative.pdf;

import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.Municipality;
import fi.om.municipalityinitiative.newdto.service.Participant;
import fi.om.municipalityinitiative.util.Maybe;
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
        initiative.setMunicipality(new Municipality(1, "Helsinki", "Helsingfors"));
        initiative.setSentTime(Maybe.of(new LocalDate()));
        return initiative;
    }

    private static List<Participant> createParticipants() {
        List<Participant> participants = Lists.newArrayList();
        for (int i = 0; i < 1000; ++i) {
            Municipality municipality = new Municipality(new Random().nextLong(), RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10));
            Participant participant = new Participant(new LocalDate(), RandomStringUtils.randomAlphabetic(20), new Random().nextBoolean(), municipality, "");
            participants.add(participant);
        }
        return participants;
    }

}
