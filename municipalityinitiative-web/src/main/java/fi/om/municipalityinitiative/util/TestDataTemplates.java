package fi.om.municipalityinitiative.util;

import com.google.common.collect.Lists;

import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeUICreateDto;
import fi.om.municipalityinitiative.newdto.ui.ParticipantUICreateDto;

import java.util.List;

import static fi.om.municipalityinitiative.util.TestDataTemplateTexts.*;

public class TestDataTemplates {
    
    private static String CONTACT_NAME = "Teppo Testaaja";
    private static String PARTICIPANT_1_NAME = "Maija Meikäläinen";
    private static String PARTICIPANT_2_NAME = "Anna Testi";
    private static String PARTICIPANT_3_NAME = "Vanja Varhonen";
    private static String PARTICIPANT_4_NAME = "Teemu Testaaja";

        
    private static ContactInfo contactInfo(){
        ContactInfo contactInfo = new ContactInfo();
        
        contactInfo.setName(CONTACT_NAME);
        contactInfo.setEmail("teppo.testaaja@solita.fi");
        contactInfo.setPhone("012 3456 789");
        contactInfo.setAddress("Osoitekatu 1 A 10 11111 Kaupunki");
        
        return contactInfo;
    }
    
    public static List<InitiativeUICreateDto> getInitiativeTemplates(Long municipality) {
        return createInitiativeTemplateList(municipality);
    }
    
    public static List<ParticipantUICreateDto> getParticipantTemplates(Long municipality, Long homeMunicipality) {
        return createParticipantTemplateList(municipality, homeMunicipality); 
    }
   
    private static List<InitiativeUICreateDto> createInitiativeTemplateList(Long municipality) {
        List<InitiativeUICreateDto> initiatives = Lists.newArrayList();
        initiatives.add(createInitiativeTemplate(municipality, INITIATIVE_1_NAME, INITIATIVE_1_PROPOSAL, false));
        initiatives.add(createInitiativeTemplate(municipality, INITIATIVE_2_NAME, INITIATIVE_2_PROPOSAL, true));
        return initiatives;
    }

    private static List<ParticipantUICreateDto> createParticipantTemplateList(Long municipality, Long homeMunicipality) {
        List<ParticipantUICreateDto> participants = Lists.newArrayList();
        participants.add(createParticipantTemplate(municipality, PARTICIPANT_1_NAME, true, true));
        participants.add(createParticipantTemplate(municipality, PARTICIPANT_2_NAME, true, false));
        participants.add(createParticipantTemplate(homeMunicipality, PARTICIPANT_3_NAME, false, true));
        participants.add(createParticipantTemplate(homeMunicipality, PARTICIPANT_4_NAME, false, false));
        return participants;
    }
    
    private static InitiativeUICreateDto createInitiativeTemplate(Long municipality, String name, String proposal, boolean collectable) {
        InitiativeUICreateDto initiative = new InitiativeUICreateDto();

        ContactInfo contactInfo = contactInfo();
        
        initiative.setMunicipality(municipality);
        initiative.setHomeMunicipality(municipality);
        initiative.setName(name);
        initiative.setProposal(proposal);
        initiative.setCollectable(collectable);
        initiative.setMunicipalMembership(true);
        initiative.setFranchise(true);
        initiative.setShowName(true);
        initiative.setContactInfo(contactInfo);

        return initiative;
    }
    
    private static ParticipantUICreateDto createParticipantTemplate(Long municipality, String name, boolean franchise, boolean showName) {
        ParticipantUICreateDto participant = new ParticipantUICreateDto();

        participant.setParticipantName(name);
        participant.setMunicipality(municipality);
        participant.setHomeMunicipality(municipality);
        participant.setFranchise(franchise);
        participant.setShowName(showName);
        return participant;
    }
}