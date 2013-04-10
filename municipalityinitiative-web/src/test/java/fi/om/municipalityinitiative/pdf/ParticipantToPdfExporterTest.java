package fi.om.municipalityinitiative.pdf;

import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.newdto.email.CollectableInitiativeEmailInfo;
import fi.om.municipalityinitiative.newdto.email.InitiativeEmailInfo;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.Municipality;
import fi.om.municipalityinitiative.newdto.service.Participant;
import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.util.Maybe;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.LocalDate;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Random;

public class ParticipantToPdfExporterTest {

//    private static String FILE = "/Users/paulika/Documents/test.pdf";
    private static String FILE = "/Users/mikkole/Documents/Kuntalaisaloite/Testi-PDF/test.pdf";

    public static void main(String[] moimoiii) throws FileNotFoundException {
        FileOutputStream outputStream = new FileOutputStream(FILE);

        CollectableInitiativeEmailInfo emailInfo = createEmailInfo();
        ParticipantToPdfExporter.createPdf(emailInfo, outputStream);
    }

    private static CollectableInitiativeEmailInfo createEmailInfo() {

        Initiative initiative = new Initiative();
        initiative.setSentTime(Maybe.of(new LocalDate()));
        initiative.setMunicipality(new Municipality(1, "Helsinki", "Helsingfors"));

        initiative.setName("Koira pois lähiöistä");
        InitiativeEmailInfo emailInfo = InitiativeEmailInfo.parse(new ContactInfo(), InitiativeViewInfo.parse(initiative), "");

        List<Participant> participants = Lists.newArrayList();

        for (int i = 0; i < 1000; ++i) {
            Municipality municipality = new Municipality(new Random().nextLong(), RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10));
            Participant participant = new Participant(new LocalDate(), RandomStringUtils.randomAlphabetic(20), new Random().nextBoolean(), municipality);
            participants.add(participant);
        }

        return CollectableInitiativeEmailInfo.parse(emailInfo, "", participants);


    }
}
