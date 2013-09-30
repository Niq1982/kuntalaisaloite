package fi.om.municipalityinitiative.util;

import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.dto.Author;
import fi.om.municipalityinitiative.dto.NormalAuthor;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.dto.ui.ParticipantUICreateDto;

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
        contactInfo.setShowName(true);
        
        return contactInfo;
    }
    
    public static List<InitiativeTemplate> getInitiativeTemplates(Long municipality, String email) {
        return createInitiativeTemplateList(municipality, email);
    }
    
    public static List<ParticipantUICreateDto> getParticipantTemplates(Long homeMunicipality) {
        return createParticipantTemplateList(homeMunicipality);
    }
   
    private static List<InitiativeTemplate> createInitiativeTemplateList(Long municipality, String email) {
        List<InitiativeTemplate> initiatives = Lists.newArrayList();
        initiatives.add(createInitiativeTemplate(municipality, INITIATIVE_1_NAME, INITIATIVE_1_PROPOSAL, INITIATIVE_1_EXTRAINFO, InitiativeType.SINGLE, email));
        initiatives.add(createInitiativeTemplate(municipality, INITIATIVE_2_NAME, INITIATIVE_2_PROPOSAL, INITIATIVE_2_EXTRAINFO, InitiativeType.COLLABORATIVE, email));
        initiatives.add(createInitiativeTemplate(municipality, INITIATIVE_3_NAME, INITIATIVE_3_PROPOSAL, INITIATIVE_3_EXTRAINFO, InitiativeType.COLLABORATIVE_COUNCIL, email));
        return initiatives;
    }

    private static List<ParticipantUICreateDto> createParticipantTemplateList(Long homeMunicipality) {
        List<ParticipantUICreateDto> participants = Lists.newArrayList();
        participants.add(createParticipantTemplate(homeMunicipality, PARTICIPANT_1_NAME, true));
        participants.add(createParticipantTemplate(homeMunicipality, PARTICIPANT_2_NAME, false));
        return participants;
    }
    
    private static InitiativeTemplate createInitiativeTemplate(Long municipality, String name, String proposal, String extraInfo, InitiativeType type, String email) {
        InitiativeTemplate initiativeTemplate = new InitiativeTemplate();
        initiativeTemplate.initiative = new Initiative();

        initiativeTemplate.initiative.setMunicipality(new Municipality(municipality, "", "", false));
        initiativeTemplate.initiative.setName(name);
        initiativeTemplate.initiative.setProposal(proposal);
        initiativeTemplate.initiative.setExtraInfo(extraInfo);
        initiativeTemplate.initiative.setType(type);
        initiativeTemplate.initiative.setState(InitiativeState.PUBLISHED);
        initiativeTemplate.author = new NormalAuthor();
        initiativeTemplate.author.setContactInfo(contactInfo());
        initiativeTemplate.author.getContactInfo().setEmail(email);
        initiativeTemplate.author.getContactInfo().setShowName(true);



        return initiativeTemplate;
    }
    
    private static ParticipantUICreateDto createParticipantTemplate(Long municipality, String name, boolean showName) {
        ParticipantUICreateDto participant = new ParticipantUICreateDto();

        participant.setParticipantName(name);
        participant.assignMunicipality(municipality);
        participant.setHomeMunicipality(municipality);
        participant.setMunicipalMembership(Membership.property);
        participant.setShowName(showName);
        return participant;
    }

    public static class InitiativeTemplate {
        public Initiative initiative;
        public Author author;

        public Initiative getInitiative() {
            return initiative;
        }

        public Author getAuthor() {
            return author;
        }
    }
}