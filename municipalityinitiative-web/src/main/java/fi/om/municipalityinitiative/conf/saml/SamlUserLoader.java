package fi.om.municipalityinitiative.conf.saml;

import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.util.SsnValidator;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;

import java.util.Optional;


public class SamlUserLoader implements SAMLUserDetailsService {

    @Override
    public SamlUser loadUserBySAML(SAMLCredential credential) throws UsernameNotFoundException {

        /**
         * Test-environment response with nordea details. name | friendlyname | value
         http://eidas.europa.eu/attributes/naturalperson/CurrentGivenName | firstName | Portaalia Nordea
         urn:oid:1.2.246.517.2002.2.7 | VakinainenKotimainenLahiosoitePostitoimipaikkaS | Helsinki
         urn:oid:2.16.840.1.113730.3.1.241 | displayName | Nordea
         urn:oid:1.2.246.517.3002.111.2 | null | true
         urn:oid:1.2.246.517.2002.2.6 | VakinainenKotimainenLahiosoitePostinumero | 00120
         urn:oid:2.5.4.42 | givenName | Nordea
         urn:oid:1.2.246.517.2002.2.19 | KotikuntaKuntaS | Helsinki
         urn:oid:2.5.4.3 | cn | Testaaja Portaalia Nordea
         urn:oid:2.5.4.4 | sn | Testaaja
         urn:oid:1.2.246.517.2002.2.18 | KotikuntaKuntanumero | 019
         urn:oid:1.2.246.517.2002.2.4 | VakinainenKotimainenLahiosoiteS | Nordeatie 2
         urn:oid:1.2.246.21 | nationalIdentificationNumber | 210281-9988
         */

        String municipalityNumber = credential.getAttributeAsString("urn:oid:1.2.246.517.2002.2.18");
        String municipalityName = credential.getAttributeAsString("urn:oid:1.2.246.517.2002.2.19");
        String municipalityNameSv = credential.getAttributeAsString("urn:oid:1.2.246.517.2002.2.20");
        String firstName = credential.getAttributeAsString("urn:oid:2.5.4.42");
        String lastName = credential.getAttributeAsString("urn:oid:2.5.4.4");
        String ssn = credential.getAttributeAsString("urn:oid:1.2.246.21");

        String address = "";
        String fullName = firstName + " " + lastName;

        Optional<Municipality> municipality = municipalityNumber != null
                ? Optional.of(new Municipality(Long.valueOf(municipalityNumber), municipalityName, municipalityNameSv, true))
                : Optional.empty();

        return new SamlUser(fullName, SsnValidator.validateSsn(ssn), address, municipality);
    }
}
