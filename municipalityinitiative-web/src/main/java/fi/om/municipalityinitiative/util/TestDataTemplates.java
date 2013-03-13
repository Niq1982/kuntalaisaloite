package fi.om.municipalityinitiative.util;

import com.google.common.collect.Lists;

import fi.om.municipalityinitiative.newdao.MunicipalityDao;
import fi.om.municipalityinitiative.newdto.service.InitiativeCreateDto;
import fi.om.municipalityinitiative.newdto.service.Municipality;
import fi.om.municipalityinitiative.newdto.service.Participant;
import fi.om.municipalityinitiative.newdto.service.ParticipantCreateDto;
import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeUICreateDto;
import fi.om.municipalityinitiative.newdto.ui.ParticipantUICreateDto;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.List;

import javax.annotation.Resource;

import static fi.om.municipalityinitiative.util.Locales.asLocalizedString;
import static fi.om.municipalityinitiative.util.TestDataTemplateTexts.*;

public class TestDataTemplates {
    @Resource
    MunicipalityDao municipalityDao;
    
    private static String PARTICIPANT_1 = "Maija Meikäläinen";
    private static String PARTICIPANT_2 = "Anna Testi";
    private static String PARTICIPANT_3 = "Vanja Varhonen";
    private static String PARTICIPANT_4 = "Teemu Testaaja";

    static final Long INITIATIVE_MUNICIPALITY = 1L;
    
    private static final List<InitiativeUICreateDto> initiatives = createInitiativeTemplateList();
    
    private static final List<ParticipantUICreateDto> participants = createParticipantTemplateList();
    
    private static ContactInfo contactInfo(){
        ContactInfo contactInfo = new ContactInfo();
        
        contactInfo.setName("Teppo Testaaja");
        contactInfo.setEmail("teppo.testaaja@solita.fi");
        contactInfo.setPhone("012 3456 789");
        contactInfo.setAddress("Osoitekatu 1 A 10 11111 Kaupunki");
        
        return contactInfo;
    }
    
    public static List<InitiativeUICreateDto> getInitiativeTemplates() {
        return initiatives;
    }
    
    public static List<ParticipantUICreateDto> getParticipantTemplates() {
        return participants;
    }
   
    private static List<InitiativeUICreateDto> createInitiativeTemplateList() {
        List<InitiativeUICreateDto> initiatives = Lists.newArrayList();
        initiatives.add(createInitiativeTemplate(INITIATIVE_1_NAME, INITIATIVE_1_PROPOSAL, false));
        initiatives.add(createInitiativeTemplate(INITIATIVE_2_NAME, INITIATIVE_2_PROPOSAL, true));
        return initiatives;
    }

    private static List<ParticipantUICreateDto> createParticipantTemplateList() {
        List<ParticipantUICreateDto> participants = Lists.newArrayList();
        participants.add(createParticipantTemplate(PARTICIPANT_1, true, true));
        participants.add(createParticipantTemplate(PARTICIPANT_2, true, false));
        participants.add(createParticipantTemplate(PARTICIPANT_3, false, true));
        participants.add(createParticipantTemplate(PARTICIPANT_4, false, false));
        return participants;
    }
    
    private static InitiativeUICreateDto createInitiativeTemplate(String name, String proposal, boolean collectable) {
        InitiativeUICreateDto initiative = new InitiativeUICreateDto();

        ContactInfo contactInfo = contactInfo();
        
        initiative.setMunicipality(INITIATIVE_MUNICIPALITY);
        initiative.setHomeMunicipality(INITIATIVE_MUNICIPALITY);
        initiative.setName(name);
        initiative.setProposal(proposal);
        initiative.setCollectable(collectable);
        initiative.setMunicipalMembership(true);
        initiative.setFranchise(true);
        initiative.setShowName(true);
        initiative.setContactInfo(contactInfo);

        return initiative;
    }
    
    private static ParticipantUICreateDto createParticipantTemplate(String name, boolean franchise, boolean showName) {
        ParticipantUICreateDto participant = new ParticipantUICreateDto();

//        List<Municipality> municipalities = municipalityDao.findMunicipalities(true);
        
//      Municipality municipality = municipalities.get(municipalities.size() % new Random().nextInt()); 
//        Municipality municipality = municipalities.get(0);
        participant.setParticipantName(name);
//        participant.setHomeMunicipality(municipality.getId());
        participant.setHomeMunicipality(1L);
        participant.setFranchise(franchise);
        participant.setShowName(showName);
        return participant;
    }
}