package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.newdto.InfoTextFooterLink;

import java.util.List;
import java.util.Locale;

public interface FooterLinkProvider {
    List<InfoTextFooterLink> getFooterLinks(Locale locale);
}
