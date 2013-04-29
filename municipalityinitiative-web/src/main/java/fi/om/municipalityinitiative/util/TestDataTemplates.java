package fi.om.municipalityinitiative.util;

import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.newdto.Author;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.Municipality;
import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newdto.ui.ParticipantUICreateDto;

import java.util.List;

import static fi.om.municipalityinitiative.util.TestDataTemplateTexts.*;

public class TestDataTemplates {
    
    private static String CONTACT_NAME = "Teppo Testaaja";
    private static String PARTICIPANT_1_NAME = "Maija Meikäläinen";
    private static String PARTICIPANT_2_NAME = "Anna Testi";

        
    private static ContactInfo contactInfo(){
        ContactInfo contactInfo = new ContactInfo();
        
        contactInfo.setName(CONTACT_NAME);
        contactInfo.setPhone("012 3456 789");
        contactInfo.setAddress("Osoitekatu 1 A 10 11111 Kaupunki");
        
        return contactInfo;
    }
    
    public static List<Initiative> getInitiativeTemplates(Long municipality, String email) {
        return createInitiativeTemplateList(municipality, email);
    }
    
    public static List<ParticipantUICreateDto> getParticipantTemplates(Long homeMunicipality) {
        return createParticipantTemplateList(homeMunicipality);
    }
   
    private static List<Initiative> createInitiativeTemplateList(Long municipality, String email) {
        List<Initiative> initiatives = Lists.newArrayList();
        initiatives.add(createInitiativeTemplate(municipality, INITIATIVE_1_NAME, INITIATIVE_1_PROPOSAL, false, email));
        initiatives.add(createInitiativeTemplate(municipality, INITIATIVE_2_NAME, INITIATIVE_2_PROPOSAL, true, email));
        return initiatives;
    }

    private static List<ParticipantUICreateDto> createParticipantTemplateList(Long homeMunicipality) {
        List<ParticipantUICreateDto> participants = Lists.newArrayList();
        participants.add(createParticipantTemplate(homeMunicipality, PARTICIPANT_1_NAME, true));
        participants.add(createParticipantTemplate(homeMunicipality, PARTICIPANT_2_NAME, false));
        return participants;
    }
    
    private static Initiative createInitiativeTemplate(Long municipality, String name, String proposal, boolean collaborative, String email) {
        Initiative initiative = new Initiative();

        initiative.setMunicipality(new Municipality(municipality, "", ""));
        initiative.setName(name);
        initiative.setProposal(proposal);
        initiative.setType(collaborative ? InitiativeType.COLLABORATIVE : InitiativeType.SINGLE);
        initiative.setState(InitiativeState.PUBLISHED);
        initiative.setShowName(true);
        Author author = new Author();
        author.setContactInfo(contactInfo());
        author.getContactInfo().setEmail(email);
        initiative.setAuthor(author);

        return initiative;
    }
    
    private static ParticipantUICreateDto createParticipantTemplate(Long municipality, String name, boolean showName) {
        ParticipantUICreateDto participant = new ParticipantUICreateDto();

        participant.setParticipantName(name);
        participant.setMunicipality(municipality);
        participant.setHomeMunicipality(municipality);
        participant.setMunicipalMembership(Membership.property);
        participant.setShowName(showName);
        return participant;
    }
}