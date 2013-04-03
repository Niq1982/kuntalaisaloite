package fi.om.municipalityinitiative.pdf;

import com.google.common.collect.Lists;
import com.itextpdf.text.*;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.AcroFields;
import fi.om.municipalityinitiative.newdto.email.CollectableInitiativeEmailInfo;
import fi.om.municipalityinitiative.newdto.service.Participant;
import org.joda.time.DateTime;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class TestPdfStamper {

    
    /** The original PDF file. */
    public static final String DATASHEET
        = "/Users/mikkole/Documents/Kuntalaisaloite/Testi-PDF/Kannatusilmoitus_W.pdf";
    /** The resulting PDF file. */
    public static final String RESULT
        = "/Users/mikkole/Documents/Kuntalaisaloite/Testi-PDF/output.pdf";

    
    
//    public static void stampPDF()
    public static void main(String[] stamp)
            throws IOException, DocumentException{

        FileOutputStream outputStream = new FileOutputStream(RESULT);
            
        PdfReader reader = new PdfReader(DATASHEET);
        PdfStamper stamper = new PdfStamper(reader, outputStream);
//        stamper = new PdfStamper(reader, new FileOutputStream(String.format(RESULT, "test")));
        
//        fill(stamper.getAcroFields());
        
        stamper.setFormFlattening(true);
        stamper.close();
        reader.close();

    }
    
    
    public static void fill(AcroFields form)
            throws IOException, DocumentException {
        form.setField("title", "Otsikko");
        form.setField("date", "1");
    }
}
