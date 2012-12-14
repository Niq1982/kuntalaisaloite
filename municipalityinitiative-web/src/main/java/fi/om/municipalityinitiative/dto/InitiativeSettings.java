package fi.om.municipalityinitiative.dto;

import org.joda.time.ReadablePeriod;


public class InitiativeSettings {

    private final int invitationExpirationDays;
    
    private final int minSupportCountForSearch;

    private final int requiredVoteCount;
    
    private final ReadablePeriod requiredMinSupportCountDuration;
    
    private final ReadablePeriod votingDuration;

    private final ReadablePeriod sendToVrkDuration;

    private final ReadablePeriod sendToParliamentDuration;

    private final ReadablePeriod votesRemovalDuration;

    private final ReadablePeriod omSearchBeforeVotesRemovalDuration;

    public InitiativeSettings(int invitationExpirationDays, int minSupportCountForSearch, 
            int requiredVoteCount, ReadablePeriod requiredMinSupportCountDuration, ReadablePeriod votingDuration,
            ReadablePeriod sendToVrkDuration, ReadablePeriod sendToParliamentDuration,
            ReadablePeriod votesRemovalDuration, ReadablePeriod omSearchBeforeVotesRemovalDuration) {
        this.invitationExpirationDays = invitationExpirationDays;
        this.minSupportCountForSearch = minSupportCountForSearch;
        this.requiredVoteCount = requiredVoteCount;
        this.requiredMinSupportCountDuration = requiredMinSupportCountDuration;
        this.votingDuration = votingDuration;
        this.sendToVrkDuration = sendToVrkDuration;
        this.sendToParliamentDuration = sendToParliamentDuration;
        this.votesRemovalDuration = votesRemovalDuration;
        this.omSearchBeforeVotesRemovalDuration = omSearchBeforeVotesRemovalDuration;
    }

    public ManagementSettings getManagementSettings(InitiativeManagement initiative, User currentUser) {
        return getManagementSettings(initiative, EditMode.NONE, currentUser);
    }
    
    public ManagementSettings getManagementSettings(InitiativeManagement initiative, EditMode editMode, User currentUser) {
        return new ManagementSettings(initiative, editMode, currentUser, requiredVoteCount, sendToVrkDuration);
    }

    public int getInvitationExpirationDays() {
        return invitationExpirationDays;
    }

    public int getMinSupportCountForSearch() {
        return minSupportCountForSearch;
    }

    public int getRequiredVoteCount() {
        return requiredVoteCount;
    }

    public ReadablePeriod getRequiredMinSupportCountDuration() {
        return requiredMinSupportCountDuration;
    }

    public ReadablePeriod getVotingDuration() {
        return votingDuration;
    }

    public ReadablePeriod getSendToVrkDuration() {
        return sendToVrkDuration;
    }

    public ReadablePeriod getSendToParliamentDuration() {
        return sendToParliamentDuration;
    }

    public ReadablePeriod getVotesRemovalDuration() {
        return votesRemovalDuration;
    }

    public ReadablePeriod getOmSearchBeforeVotesRemovalDuration() {
        return omSearchBeforeVotesRemovalDuration;
    }
}
