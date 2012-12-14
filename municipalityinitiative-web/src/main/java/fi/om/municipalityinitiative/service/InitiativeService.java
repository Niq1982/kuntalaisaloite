package fi.om.municipalityinitiative.service;

import java.util.List;

import org.joda.time.Period;
import org.springframework.validation.Errors;

import fi.om.municipalityinitiative.dto.*;

public interface InitiativeService {

    InitiativePublic getInitiativeForPublic(Long id);

    InitiativeManagement getInitiativeForManagement(Long id);

    Long create(InitiativeManagement initiative, Errors errors);

    List<InitiativeInfo> findInitiatives(InitiativeSearch search);

    InitiativeManagement update(InitiativeManagement initiative, EditMode editMode, Errors errors);

    Author getAuthor(Long initiativeId, Long userId);
    
    boolean sendInvitations(Long initiativeId);

    Invitation getInvitation(Long initiativeId, String invitationCode);

    boolean declineInvitation(Long initiativeId, String invitationCode);

    boolean acceptInvitation(Long initiativeId, String invitationCode, Author author, Errors errors);

    void sendToOM(Long initiativeId);

    void respondByOm(Long initiativeId, boolean accept, String comment, String acceptanceIdentifier);

    void confirmCurrentAuthor(Long initiativeId);

    void deleteCurrentAuthor(Long initiativeId);

    boolean updateVRKResolution(InitiativeManagement initiative, Errors bindingResult);

    List<InitiativeInfo> findInitiativesWithUnremovedVotes(Period afterEndDate);

}