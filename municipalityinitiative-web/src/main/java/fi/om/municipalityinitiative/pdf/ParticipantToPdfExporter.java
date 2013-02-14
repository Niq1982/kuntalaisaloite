package fi.om.municipalityinitiative.pdf;

import com.google.common.collect.Lists;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import fi.om.municipalityinitiative.newdto.email.CollectableInitiativeEmailInfo;
import fi.om.municipalityinitiative.newdto.service.Participant;
import org.joda.time.DateTime;

import java.io.OutputStream;
import java.util.List;

public class ParticipantToPdfExporter {

    public static final String DATETIME_FORMAT = "dd.MM.yyyy HH:mm:ss";
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

    public static void createPdf(CollectableInitiativeEmailInfo emailInfo, OutputStream outputStream) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();
            addMetaData(document, emailInfo);
            addTitlePage(document, emailInfo);
//            addContent(document);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // iText allows to add metadata to the PDF which can be viewed in your Adobe
    // Reader
    // under File -> Properties
    private static void addMetaData(Document document, CollectableInitiativeEmailInfo emailInfo) {
        document.addTitle("Kuntalaisaloite " + emailInfo.getMunicipalityName());
        document.addSubject(emailInfo.getName());
        document.addKeywords("Java, PDF, iText"); // TODO: Remove
        document.addAuthor("Lars Vogel");
        document.addCreator("Lars Vogel");
    }

    private static void addTitlePage(Document document, CollectableInitiativeEmailInfo emailInfo)
            throws DocumentException {
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph(emailInfo.getMunicipalityName() + ", "+ new DateTime().toString(DATETIME_FORMAT), catFont));
        addEmptyLine(preface, 1);

        // Will create: Report generated by: _name, _date
        preface.add(new Paragraph(emailInfo.getName(), smallBold));
        addEmptyLine(preface, 3);

        List<Participant> participantsFranchise = Lists.newArrayList();
        List<Participant> participantsNoFranchise = Lists.newArrayList();

        parseParticipants(emailInfo.getParticipants(), participantsFranchise, participantsNoFranchise);

        preface.add(new Paragraph("Äänioikeutetut osallistujat:", smallBold));
        addEmptyLine(preface, 1);
        createTable(preface, participantsFranchise);

        preface.add(new Paragraph("Muut osallistujat:", smallBold));
        addEmptyLine(preface, 1);
        createTable(preface, participantsNoFranchise);

        document.add(preface);
    }

    private static void parseParticipants(List<Participant> allParticipants,
                                          List<Participant> participantsFranchise,
                                          List<Participant> participantsNoFranchise) {
        for (Participant participant : allParticipants) {
            if (participant.isFranchise()) {
                participantsFranchise.add(participant);
            }
            else {
                participantsNoFranchise.add(participant);
            }
        }
    }

    private static void createTable(Paragraph subCatPart, List<Participant> participants)
            throws DocumentException {
        PdfPTable table = new PdfPTable(3);
        table.setWidths(new int[] {40, 200, 100});

        table.addCell(createCell("#"));
        table.addCell(createCell("Nimi"));
        table.addCell(createCell("Kotikunta"));

        table.setHeaderRows(1);

        int count = 0;
        for (Participant participant : participants) {
            ++count;
            table.addCell(String.valueOf(count));
            table.addCell(participant.getName());
            table.addCell(participant.getHomeMunicipality());
        }

        subCatPart.add(table);

    }

    private static PdfPCell createCell(String header) {
        PdfPCell c1 = new PdfPCell(new Phrase(header));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        return c1;
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}