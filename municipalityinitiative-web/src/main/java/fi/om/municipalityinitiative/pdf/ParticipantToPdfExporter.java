package fi.om.municipalityinitiative.pdf;

import com.google.common.collect.Lists;
import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import fi.om.municipalityinitiative.dto.service.*;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.util.Membership;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.util.List;

public class ParticipantToPdfExporter {

    public static final String DATETIME_FORMAT = "dd.MM.yyyy HH:mm:ss";
    public static final String DATE_FORMAT = "dd.MM.yyyy";

    public static final String COMMUNITY = "A";
    public static final String COMPANY = "B";
    public static final String PROPERTY = "C";

    private Document document;

    private final Initiative initiative;
    private final List<? extends Participant> participants;

    private static final Logger log = LoggerFactory.getLogger(ParticipantToPdfExporter.class);
    private final Font mainTitle;
    private final Font subTitle;
    private final Font bodyText;
    private final Font smallBold;


    public ParticipantToPdfExporter(Initiative initiative, List<? extends Participant> participants) {

        BaseFont fontFamily;
        try {
            fontFamily = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, true);
        } catch (Exception e) {
            log.error("Unable to initialize object", e);
            throw new RuntimeException(e);
        }

        mainTitle = new Font(fontFamily, 16, Font.BOLD);
        subTitle = new Font(fontFamily, 14, Font.BOLD);
        bodyText = new Font(fontFamily, 10, Font.NORMAL);
        smallBold = new Font(fontFamily, 10, Font.BOLD);

        this.initiative = initiative;
        this.participants = participants;
    }

    public void createPdf(OutputStream outputStream) {
        if (document != null) {
            document.close();
        }
        try {
            document = new Document();

            PdfWriter.getInstance(document, outputStream);
            document.open();
            addMetaData();
            addTitlePage();
            addParticipants();

            document.close();
            document = null;

        } catch (Exception e) {
            log.error("Unable to create pdf", e);
            throw new RuntimeException(e);
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }

    private void addParticipants() throws DocumentException {
        //To change body of created methods use File | Settings | File Templates.

        Paragraph preface = new Paragraph();
        
        addEmptyLine(preface, 1);

        List<Participant> withCorrectHomeMunicipality = Lists.newArrayList();
        List<Participant> withSomeOtherHomeMunicipality = Lists.newArrayList();

        for (Participant participant : participants) {
            if (hasCorrectHomeMunicipality(participant)) {
                withCorrectHomeMunicipality.add(participant);
            }
            else {
                withSomeOtherHomeMunicipality.add(participant);
            }
        }

        preface.add(new Paragraph("Osallistujat (Kunnan asukkaat " + withCorrectHomeMunicipality.size() + " kpl, muut " + withSomeOtherHomeMunicipality.size() + " kpl)", new Font()));
        preface.add(new Paragraph("Deltagar", new Font()));

        addEmptyLine(preface, 1);
        createTable(preface, withCorrectHomeMunicipality);
        
        document.add(preface);

        if (initiative.getType().isNotVerifiable()) {

            document.newPage();
            preface = new Paragraph();
            createTable(preface, withSomeOtherHomeMunicipality);
            document.add(preface);

            document.newPage();

            Paragraph lastPage = new Paragraph();

            lastPage.add(new Paragraph("Jäsenyysperuste, jos osallistuja ei ole kunnan asukas", subTitle));

            com.lowagie.text.List list = new com.lowagie.text.List(true, 20);
            list.add(new ListItem(COMMUNITY + ": Nimenkirjoitusoikeus yhteisössä, laitoksessa tai säätiössä, jonka kotipaikka on aloitetta koskevassa kunnassa", bodyText));
            list.add(new ListItem(COMPANY + ": Nimenkirjoitusoikeus yrityksessä, jonka kotipaikka on aloitetta koskevassa kunnassa", bodyText));
            list.add(new ListItem(PROPERTY + ": Hallinto-oikeus tai omistus kiinteään omaisuuteen aloitetta koskevassa kunnassa", bodyText));

            lastPage.add(list);

            addEmptyLine(lastPage, 1);

            lastPage.add(new Paragraph("Villkor för medlemskap, ifall deltagaren inte är bosatt i kommunen", subTitle));

            list = new com.lowagie.text.List(true, 20);
            list.add(new ListItem(COMMUNITY + ": Har namnteckningsrätt i ett samfund, en institution eller stiftelse vars hemort finns i den kommun som initiativet gäller", bodyText));
            list.add(new ListItem(COMPANY + ": Har namnteckningsrätt i ett företag vars hemort finns i den kommun som initiativet gäller", bodyText));
            list.add(new ListItem(PROPERTY + ": Äger eller besitter egendom i den kommun som initiativet gäller", bodyText));

            lastPage.add(list);

            document.add(lastPage);
        }
    }

    private boolean hasCorrectHomeMunicipality(Participant p) {
        Maybe<Municipality> homeMunicipality = p.getHomeMunicipality();
        return homeMunicipality.isPresent() && homeMunicipality.get().getId().equals(initiative.getMunicipality().getId());
    }

    // iText allows to add metadata to the PDF which can be viewed in your Adobe
    // Reader
    // under File -> Properties
    private void addMetaData() {
        document.addTitle("Kuntalaisaloite " + initiative.getMunicipality().getLocalizedName(Locales.LOCALE_FI));
        document.addSubject(initiative.getName());
        document.addKeywords("Kuntalaisaloite"); // TODO: Remove
        document.addAuthor("Kuntalaisaloite.fi");
        document.addCreator("Kuntalaisaloite.fi");
    }

    private void addTitlePage()
            throws DocumentException {
        Paragraph preface = new Paragraph();
        
        String documentTitle = "";
        
        if (initiative.getType().equals(InitiativeType.COLLABORATIVE_COUNCIL)){
            documentTitle = "Valtuustokäsittelyyn tähtäävä aloite / Initiativ som syftar till behandling i fullmäktige";
        } else if (initiative.getType().equals(InitiativeType.COLLABORATIVE_CITIZEN)) {
            documentTitle = "Aloite kunnallisesta kansanäänestyksestä / Initiativ till kommunal folkomröstning";
        } else {
            documentTitle = "Kuntalaisaloite / Invånarinitiativ";
        }
        Municipality municipality = initiative.getMunicipality();
        
        preface.add(new Paragraph(documentTitle, mainTitle));
        preface.add(new Paragraph(municipality.getLocalizedName(Locales.LOCALE_FI) + " / " + municipality.getLocalizedName(Locales.LOCALE_SV), subTitle));
        preface.add(new Paragraph("Aloite lähetetty kuntaan / Initiativet skickats till kommun " + new DateTime().toString(DATETIME_FORMAT), bodyText));
        
        addEmptyLine(preface, 1);

        // Will create: Report generated by: _name, _date
        preface.add(new Paragraph("Aloitteen otsikko / Initiativets titel", subTitle));
        preface.add(new Paragraph(initiative.getName(), bodyText));

        document.add(preface);

    }

    private void createTable(Paragraph subCatPart, List<? extends Participant> participants)
            throws DocumentException {
        PdfPTable table = new PdfPTable(initiative.getType().isNotVerifiable() ? 6 : 4);

        table.setWidthPercentage(100);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);

        table.addCell(createCell(" ", true));
        table.addCell(createCell("Pvm\nDatum", true));
        table.addCell(createCell("Nimi\nNamn", true));
        if (initiative.getType().isNotVerifiable()) {
            table.setWidths(new int[] {6, 12, 42, 18, 12, 12});
            table.addCell(createCell("Kotikunta\nHemkommun", true));
            table.addCell(createCell("Jäsenyys\nMedlemskap", true));
        }
        else {
            table.setWidths(new int[] {6, 12, 46, 12});
        }

        table.addCell(createCell("Kotikunta vahvistettu\nHemkommun verifierad", true));

        table.setHeaderRows(1);

        int count = 0;
        for (Participant p : Lists.reverse(participants)) {
            if (initiative.getType().isNotVerifiable()) {

                ++count;
                table.addCell(createCell(String.valueOf(count), false));
                table.addCell(createCell(p.getParticipateDate().toString(DATE_FORMAT), false));
                table.addCell(createCell(p.getName(), false));
                Municipality homeMunicipality = (Municipality) p.getHomeMunicipality().get();
                table.addCell(createCell(homeMunicipality.getNameFi() + "\n" + homeMunicipality.getNameSv() + "\n", false));

                String membershipType = "";

                if (p.getMembership() == Membership.community) {
                    membershipType = COMMUNITY;
                } else if (p.getMembership() == Membership.company) {
                    membershipType = COMPANY;
                } else if (p.getMembership() == Membership.property) {
                    membershipType = PROPERTY;
                } else {
                    membershipType = "";
                }
            
                table.addCell(createCell(membershipType, false));
            }
            else {
                VerifiedParticipant participant = (VerifiedParticipant) p;
                ++count;
                table.addCell(createCell(String.valueOf(count), false));
                table.addCell(createCell(participant.getParticipateDate().toString(DATE_FORMAT), false));
                table.addCell(createCell(participant.getName(), false));
            }
            table.addCell(createCell(p.isMunicipalityVerified() ? "X" : "", false));
        }

        subCatPart.add(table);
    }

    private PdfPCell createCell(String header, boolean tableHead) {
        Font fontStyle = bodyText;

        if (tableHead) {
            fontStyle = smallBold;
        }

        PdfPCell c1 = new PdfPCell(new Phrase(header, fontStyle));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setVerticalAlignment(Element.ALIGN_TOP);
        c1.setPadding(4);

        return c1;
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
