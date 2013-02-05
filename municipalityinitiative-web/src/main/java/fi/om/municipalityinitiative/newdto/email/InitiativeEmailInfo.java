package fi.om.municipalityinitiative.newdto.email;

import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeViewInfo;

public class InitiativeEmailInfo {

    public String name;

    public String proposal;

    public String municipalityName;

    public String url;

    public ContactInfo contactInfo;

    public static InitiativeEmailInfo parse(ContactInfo contactInfo, InitiativeViewInfo initiative, String url) {

        InitiativeEmailInfo initiativeEmailInfo = new InitiativeEmailInfo();

        initiativeEmailInfo.name = initiative.getName();
        initiativeEmailInfo.proposal = initiative.getProposal();
        initiativeEmailInfo.municipalityName = initiative.getMunicipalityName();
        initiativeEmailInfo.url = url;
        initiativeEmailInfo.contactInfo = contactInfo;

        return initiativeEmailInfo;

    }


}
