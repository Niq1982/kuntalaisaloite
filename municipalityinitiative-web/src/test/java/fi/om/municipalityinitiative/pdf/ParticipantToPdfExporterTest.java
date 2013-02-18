package fi.om.municipalityinitiative.pdf;

import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.newdto.email.CollectableInitiativeEmailInfo;
import fi.om.municipalityinitiative.newdto.email.InitiativeEmailInfo;
import fi.om.municipalityinitiative.newdto.service.Participant;
import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeViewInfo;
import fi.om.municipalityinitiative.util.Maybe;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
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

        InitiativeViewInfo initiative = new InitiativeViewInfo();
        initiative.setSentTime(Maybe.of(new DateTime()));
        initiative.setMunicipalityName("Helsinki");

        initiative.setName("Koira pois lähiöistä");
        InitiativeEmailInfo emailInfo = InitiativeEmailInfo.parse(new ContactInfo(), initiative, "");

        List<Participant> participants = Lists.newArrayList();

        for (int i = 0; i < 1000; ++i) {
            Participant participant = new Participant(new LocalDate(), RandomStringUtils.randomAlphabetic(20), new Random().nextBoolean(), RandomStringUtils.randomAlphabetic(10));
            participants.add(participant);
        }

        return CollectableInitiativeEmailInfo.parse(emailInfo, "", participants);


    }
}
