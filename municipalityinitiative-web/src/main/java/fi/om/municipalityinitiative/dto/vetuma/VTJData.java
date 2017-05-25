package fi.om.municipalityinitiative.dto.vetuma;

import com.google.common.base.Strings;
import fi.om.municipalityinitiative.dto.service.Municipality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
import java.util.Optional;

import static javax.xml.stream.XMLStreamConstants.*;

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

    private String streetAddressFi;

    private String postalCode;
    private String streetAddressSv;

    public VTJData() {}

    public static VTJData parse(String xml) {
        VTJData vtjData = new VTJData();
        XMLInputFactory factory = XMLInputFactory.newInstance();
        try {
            XMLStreamReader parser = factory.createXMLStreamReader(new StringReader(xml));
            while (parser.hasNext()) {
                int event = parser.next();
                switch (event) {
                    case (START_ELEMENT):
                        String localName = parser.getLocalName();
                        if ("Paluukoodi".equals(localName)) {
                            vtjData.setReturnCodeDescription(parseText(parser));
                        } else if ("NykyinenSukunimi".equals(localName)) {
                            vtjData.setLastName(parseText(parser));
                        } else if ("NykyisetEtunimet".equals(localName)) {
                            vtjData.setFirstNames(parseText(parser));
                        } else if ("Kuntanumero".equals(localName)) {
                            vtjData.setMunicipalityCode(parseText(parser));
                        } else if ("KuntaS".equals(localName)) {
                            vtjData.setMunicipalityNameFi(parseText(parser));
                        } else if ("KuntaR".equals(localName)) {
                            vtjData.setMunicipalityNameSv(parseText(parser));
                        } else if ("Kuolinpvm".equals(localName)) {
                            vtjData.setDead(!Strings.isNullOrEmpty(parseText(parser)));
                        } else if ("SuomenKansalaisuusTietokoodi".equals(localName)) {
                            vtjData.setFinnishCitizen("1".equals(parseText(parser)));
                        } else if ("LahiosoiteS".equals(localName)) {
                            vtjData.setStreetAddressFi(parseText(parser));
                        } else if ("LahiosoiteR".equals(localName)) {
                            vtjData.setStreetAddressSv(parseText(parser));
                        } else if ("Postinumero".equals(localName)) {
                            vtjData.setPostalCode(parseText(parser));
                        }
                        break;
                }
            }

            if (!vtjData.getMunicipality().isPresent()) {
                log.warn("Missing municipality code" + vtjData.isFinnishCitizen() + ", " + vtjData.getReturnCodeDescription());
            }

            parser.close();
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }

        return vtjData;
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

    public String getFullName() {
        return firstNames + " " + lastName;
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

    public Optional<Municipality> getMunicipality() {
        if (Strings.isNullOrEmpty(municipalityCode)) {
            return Optional.empty();
        }
        return Optional.of(new Municipality(Long.valueOf(municipalityCode), municipalityNameFi, municipalityNameSv, Boolean.FALSE));
    }

    public void setMunicipalityCode(String municipalityCode) {
        this.municipalityCode = municipalityCode;
    }

    public void setMunicipalityNameFi(String municipalityNameFi) {
        this.municipalityNameFi = municipalityNameFi;
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

    public void setStreetAddressFi(String streetAddressFi) {
        this.streetAddressFi = streetAddressFi;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddressFi() {
        return streetAddressFi + "\n" + postalCode + " " + municipalityNameFi;
    }

    public String getAddressSv() {
        return streetAddressSv + "\n" + postalCode + " " + municipalityNameSv;
    }

    public void setStreetAddressSv(String streetAddressSv) {
        this.streetAddressSv = streetAddressSv;
    }

}
