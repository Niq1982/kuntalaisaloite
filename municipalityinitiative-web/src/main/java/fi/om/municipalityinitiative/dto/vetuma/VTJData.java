package fi.om.municipalityinitiative.dto.vetuma;

import static fi.om.municipalityinitiative.util.Locales.asLocalizedString;
import static javax.xml.stream.XMLStreamConstants.CDATA;
import static javax.xml.stream.XMLStreamConstants.CHARACTERS;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

import java.io.StringReader;
import java.util.Locale;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import com.google.common.base.Strings;

import fi.om.municipalityinitiative.dto.LocalizedString;
import fi.om.municipalityinitiative.util.Locales;

public class VTJData {

    private static final Logger log = LoggerFactory.getLogger(VTJData.class); 
    
    private String firstNames;
    
    private String lastName;
    
    private String municipalityCode;
    
    private String municipalityNameFi;
    
    private String municipalityNameSv;
    
    private boolean finnishCitizen;
    
    private boolean dead;
    
    private String returnCodeDescription;
    
    public VTJData() {}

    public static VTJData parse(String xml, MessageSource messageSource) {
        VTJData vtjData = new VTJData();
        XMLInputFactory factory = XMLInputFactory.newInstance();
        try {
            XMLStreamReader parser = factory.createXMLStreamReader(new StringReader(xml));
            while (parser.hasNext()) {
                int event = parser.next();
                switch (event) {
                case (START_ELEMENT) :
                    String localName = parser.getLocalName();
                    if ("Paluukoodi".equals(localName)) {
                        vtjData.setReturnCodeDescription(parseText(parser));
                    }
                    else  if ("NykyinenSukunimi".equals(localName)) {
                        vtjData.setLastName(parseText(parser));
                    }
                    else if ("NykyisetEtunimet".equals(localName)) {
                        vtjData.setFirstNames(parseText(parser));
                    }
                    else if ("Kuntanumero".equals(localName)) {
                        vtjData.setMunicipalityCode(parseText(parser));
                    }
                    else if ("KuntaS".equals(localName)) {
                        vtjData.setMunicipalityNameFi(parseText(parser));
                    }
                    else if ("KuntaR".equals(localName)) {
                        vtjData.setMunicipalityNameSv(parseText(parser));
                    }
                    else if ("Kuolinpvm".equals(localName)) {
                        vtjData.setDead(!Strings.isNullOrEmpty(parseText(parser)));
                    }
                    else if ("SuomenKansalaisuusTietokoodi".equals(localName)) {
                        vtjData.setFinnishCitizen("1".equals(parseText(parser)));
                    }
                    break;
                }
            }

            if (Strings.isNullOrEmpty(vtjData.getMunicipalityCode())
             || Strings.isNullOrEmpty(vtjData.getMunicipalityNameFi())
             || Strings.isNullOrEmpty(vtjData.getMunicipalityNameSv())) {
                log.warn("Missing municipality code: " + vtjData.getMunicipalityCode() + ", " + vtjData.getMunicipalityNameFi() + ", " + vtjData.getMunicipalityNameSv() + ", " + vtjData.isFinnishCitizen() + ", " + vtjData.getReturnCodeDescription());
            }
            
            if (Strings.isNullOrEmpty(vtjData.getMunicipalityNameFi())) {
                vtjData.setMunicipalityNameFi(getMissingMunicipalityName(messageSource, Locales.LOCALE_FI));
            }
            if (Strings.isNullOrEmpty(vtjData.getMunicipalityNameSv())) {
                vtjData.setMunicipalityNameSv(getMissingMunicipalityName(messageSource, Locales.LOCALE_SV));
            }
            
            parser.close();
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
        
        return vtjData;
    }
    
    private static String getMissingMunicipalityName(MessageSource messageSource, Locale locale) {
        return messageSource.getMessage("VTJData.missingMunicipality", null, locale); 
    }
    
    private static String parseText(XMLStreamReader parser) throws XMLStreamException {
        StringBuilder sb = new StringBuilder(32);
        int depth = 1;
        while (parser.hasNext() && depth > 0) {
            switch (parser.next()) {
            case (START_ELEMENT) :
                depth++;
                break;
            case (END_ELEMENT) :
                depth--;
            break;
            case (CHARACTERS) :
            case (CDATA) :
                if (!parser.isWhiteSpace()) {
                    sb.append(parser.getText());
                }
            break;
            }
        }
        // Trim and return null for empty String
        String result = sb.toString().trim();
        return Strings.isNullOrEmpty(result) ? null : result;
    }
    
    public LocalizedString getHomeMunicipality() {
        return asLocalizedString(municipalityNameFi, municipalityNameSv);
    }

    public boolean isFinnishCitizen() {
        return finnishCitizen;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public void setFinnishCitizen(boolean finnishCitizen) {
        this.finnishCitizen = finnishCitizen;
    }

    public String getFirstNames() {
        return firstNames;
    }

    public void setFirstNames(String firstNames) {
        this.firstNames = firstNames;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMunicipalityCode() {
        return municipalityCode;
    }

    public void setMunicipalityCode(String municipalityCode) {
        this.municipalityCode = municipalityCode;
    }

    public String getMunicipalityNameFi() {
        return municipalityNameFi;
    }

    public void setMunicipalityNameFi(String municipalityNameFi) {
        this.municipalityNameFi = municipalityNameFi;
    }

    public String getMunicipalityNameSv() {
        return municipalityNameSv;
    }

    public void setMunicipalityNameSv(String municipalityNameSv) {
        this.municipalityNameSv = municipalityNameSv;
    }
    
    public String getReturnCodeDescription() {
        return returnCodeDescription;
    }

    public void setReturnCodeDescription(String returnCodeDescription) {
        this.returnCodeDescription = returnCodeDescription;
    }

}
