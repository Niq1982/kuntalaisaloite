package fi.om.municipalityinitiative.service;

import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;

import fi.om.municipalityinitiative.dto.InitiativeBase;
import fi.om.municipalityinitiative.dto.InitiativeManagement;
import fi.om.municipalityinitiative.dto.SupportVoteBatch;
import fi.om.municipalityinitiative.dto.VotingInfo;

public interface SupportVoteService {
    
    int sendToVRK(Long initiativeId);
    
    void vote(Long initiativeId, Locale locale);

    DateTime getVotingTime(Long initiativeId);
    
    List<String> getVoteDetails(Long batchId);

    VotingInfo getVotingInfo(InitiativeBase initiative);

    List<SupportVoteBatch> getSupportVoteBatches(InitiativeManagement initiative);

    void removeSupportVotes(Long initiativeId);

}
