package fi.om.municipalityinitiative.web;

import static org.junit.Assert.assertNotNull;
import mockit.Delegate;
import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.joda.time.LocalDate;
import org.junit.Test;

import fi.om.municipalityinitiative.dto.InitiativeManagement;
import fi.om.municipalityinitiative.dto.Invitation;
import fi.om.municipalityinitiative.service.EmailServiceImpl;
import fi.om.municipalityinitiative.util.MutableObject;

public class AuthorWebTest extends WebTestBase {
    
//    private static final Municipality HELSINKI = new Municipality("90", "Helsinki", "Helsingfors");
    
    @Mocked EmailServiceImpl emailService; 

    
    /**
     * Localization keys as constants.
     */
    private static final String MSG_SITE_NAME = "siteName";
    private static final String MSG_DATE_FORMAT = "date.format";
    private static final String MSG_LOGIN = "common.login";
    private static final String MSG_LOGOUT = "common.logout";
    private static final String MSG_CLOSE = "action.close";
    private static final String MSG_CONTINUE_BROWSING = "modal.continueBrowsing";
    private static final String MSG_PAGE_FRONTPAGE = "page.frontpage";
    private static final String MSG_PAGE_BEFORECREATE = "page.beforeCreate";
    private static final String MSG_PAGE_SEARCH = "page.search";
    private static final String MSG_PAGE_OWN_INITIATIVES = "initiative.search.own.title";
    private static final String MSG_SEARCH_RESULTS_EMPTY = "searchResults.public.empty";
    private static final String MSG_AUTHENTICATE = "beforeCreate.authenticateAndCreate";
    private static final String MSG_SUCCESS_SAVE = "success.save";
    private static final String MSG_SUCCESS_SAVE_AND_SEND_INVITATIONS = "success.save-and-send-invitations.title";
    private static final String MSG_INVITTATION_TITLE = "systemMessage.invitation.title";
    private static final String MSG_SUCCESS_ACCEPT_INVITATION = "success.accept-invitation";
    private static final String MSG_INVITATION_ACCEPT_CONFIRM = "modal.invitationAccept.confirm.title";
    private static final String MSG_INVITATION_ACCEPT_MODAL= "invitation.accept";
    private static final String MSG_READY_FOR_OM = "initiative.readyForOm";
    private static final String MSG_SEND_TO_OM = "modal.sendToOm.confirm.title";
    private static final String MSG_SUCCESS_SEND_TO_OM = "success.send-to-om";
    private static final String MSG_SUCCESS_ACCEPT_BY_OM = "success.accept-by-om";
    private static final String MSG_ACCEPT_INITIATIVE = "initiative.acceptInitiative.btn";
    private static final String MSG_VOTE = "vote.btn";
    private static final String MSG_SUCCESS_VOTE = "success.confirm-vote.title";
    private static final String MSG_WARNING_VOTING_NOT_ALLOWED = "warning.voting-not-allowed";
    private static final String MSG_SEND_TO_VRK = "initiative.sendToVRK.title";
    private static final String MSG_SEND_TO_VRK_MODAL = "modal.sendToVRK.confirm.title";
    private static final String MSG_SUCCESS_SEND_TO_VRK = "success.send-to-vrk";
    private static final String MSG_SUCCESS_SAVE_VRK_RESOLUTION = "success.save-vrk-resolution";
    private static final String MSG_REMOVE_VOTES = "removeSupportVotes.btnTitle";
    private static final String MSG_SUCCESS_VOTES_REMOVED = "success.remove-support-votes";
    private static final String MSG_WARNING_CREATE_UNDER_AGED = "warning.adult-required-as-author";
    private static final String MSG_ERROR_VALIDATE_INITIATOR = "ValidAuthorRole.initiator";
    
    private static final String TEST_INITIATIVE_TITLE = "Testiotsake";
    
    private static final String underAgedSSN = new LocalDate().minusYears(10).toString("ddMMyy") + "A1234";
    private static final String normalSSN = new LocalDate().minusYears(20).toString("ddMMyy") + "-1234";
    
    
    /**
     * This test is to check that both web test client and web test server use the same database.
     * OM user has been created with client configuration and retrieved using server configuration. 
     */
    @Test
    public void Database_Configuration_OK() {
        System.out.println("=== TEST: Database_Configuration_OK");
        open(urls.frontpage());

        // NOTE: NO longer needed?
//        // Register OM user 
//        loginUser("Oikeus", "Ministeriö", OM_USER_SSN);
//        open(urls.myAccount());
//        assertTextContainedById("content", "User ID:"); //found only for persisted users
//        logoutUser();

    }
    
    
    /**
     * This test is to check initiative life cycle flow from create to end with minimum required data and with minimum operations.
     */
    @Test
    public void Initiative_Minimum_Flow_OK() {
        
        System.out.println("=== TEST: Initiative_Minimum_Flow_OK");
        
        // Anna Creates initiative and sends invitation
        startNewInitiative("Anna Testi");
        String viewUrl = fillAndSaveInitiativeInFullEdit(false, true);
        String invitationLink = addAndSendInvitation();
        logoutUser();

        // Maija accepts invitation and sends to OM
        openAndAcceptInvitation(invitationLink, "Maija Meikäläinen");
        sendToOM();
        logoutUser();
        
        // OM user accepts initiative
        open(viewUrl);
        loginUser("Oikeus", "Ministeriö", OM_USER_SSN, true, true);
        acceptByOM();
        logoutUser();
        
        // Underaged tries to vote
        open(viewUrl);
        voteByUserNotAllowed("Antti", "Alaikäinen", underAgedSSN, true);

        // Foreign citizen tries to vote
        open(viewUrl);
        voteByUserNotAllowed("Urho", "Ulkomaalainen", normalSSN, false);
        
        // Kalle supports inititative
        // NO initiatives found in public search page since vote count is under limit
        emptyPublicSearchResult();
        open(viewUrl);
        voteByUser("Kalle Kannattaja", true);

        // Anna supports inititative
        // Select initiative from public result list, assuming there is "initiative.minSupportCountForSearch=1" in test.properties
        navigateToInitiative(false);
        voteByUser("Anna Testi", true);
        
        // Maija supports inititative and sends to VRK
        open(viewUrl);
        voteByUser("Maija Meikäläinen", false);
        sendToVRK(); // assuming there is "required.vote.count=3" in test.properties
        logoutUser(); 

        // VRK user confirms votes
        open(viewUrl);
        loginUser("Väestö", "Rekisterikeskus", VRK_USER_SSN, true, true);
        confirmByVRK();
        logoutUser();

        timeMachine(viewUrl, "P6M");
        timeMachine(viewUrl, "P1D");
        
        loginUser("Anna", "Testi", INITIATOR_USER_SSN, true, true);
        navigateToInitiative(true);
        removeVotes();
        logoutUser();
        
        // TODO: next steps in the basic flow
    }
    
    /**
     * This test is to check that application catches exceptions like that under aged user cannot create initiative etc.
     * Some of these tests are included in Initiative_Minimum_Flow_OK
     */
    @Test
    public void Initiative_Exceptions_OK() {
        // Under aged user tries to create initiative but gets an exception
        System.out.println("--- startNewInitiative as an underaged user");
        open(urls.createNew());
        clickLinkContaining(getMessage(MSG_AUTHENTICATE)); // "Tunnistaudu ja tee aloite"
        loginUser("Antti", "Alaikäinen", underAgedSSN, true, false);
        assertMsgContainedByClass("msg-warning", MSG_WARNING_CREATE_UNDER_AGED); // "Alaikäinen ei voi tehdä kansalaisaloitetta."
        logoutUser();
        
        // Foreign citizen tries to create an initiative but gets a validation error on role "Initiator"
        System.out.println("--- startNewInitiative as a foreign user");
        open(urls.createNew());
        clickLinkContaining(getMessage(MSG_AUTHENTICATE)); // "Tunnistaudu ja tee aloite"
        loginUser("Urho", "Ulkomaalainen", normalSSN, false, false);
        fillAndSaveInitiativeInFullEdit(false, false);
        assertMsgContainedByClass("msg-error", MSG_ERROR_VALIDATE_INITIATOR); // "Vireillepanijan on oltava täysi-ikäinen Suomen kansalainen." 
        logoutUser();
    }
    
    private void removeVotes(){
        System.out.println("--- removeVotes");

        clickLinkContaining(getMessage(MSG_REMOVE_VOTES)); // Hävitä kannatusilmoitukset
        clickByName("confirm");
        clickByName("action-remove-support-votes");
        
        assertMsgContainedByClass("msg-success", MSG_SUCCESS_VOTES_REMOVED); // Kannatusilmoitukset hävitetty tästä palvelusta 
        
        System.out.println("--- removeVotes OK");
    }

    private void confirmByVRK() {
        System.out.println("--- confirmByVRK");

        inputText("verifiedSupportCount", "3");
        
        // Select today
        clickByName("verified");
        clickById("calcurrent");
        assertValue("verified", new LocalDate().toString(getMessage(MSG_DATE_FORMAT)));
        
        inputText("verificationIdentifier", "XXX");
        
        clickByName("action-vrk-update-resolution");
        
        assertMsgContainedByClass("msg-success", MSG_SUCCESS_SAVE_VRK_RESOLUTION); // Päätös kannatusilmoitusten vahvistamisesta tallennettu onnistuneesti
        
        System.out.println("--- confirmByVRK OK");
    }
    
    private void acceptByOM() {
        System.out.println("--- acceptByOM");

        clickLinkContaining(getMessage(MSG_ACCEPT_INITIATIVE)); // "Lähetä hyväksyntä"
        inputText("comment", "Testisaate");
        inputText("acceptanceIdentifier", "OM 1/52/2012");
        
        clickByName("action-accept-by-om");
         
        assertMsgContainedByClass("msg-success", MSG_SUCCESS_ACCEPT_BY_OM); // Kansalaisaloite on hyväksytty
    }
    
    private void voteByUser(String userName, boolean logout) {
        System.out.println("--- voteByUser " + userName);

        clickLinkContaining(getMessage(MSG_VOTE)); // "Kannata aloitetta"
        clickLinkContaining(userName);
        clickByName("confirm"); // Clicks the checkbox. Should we test clicking of the label?
        clickByName("action-vote");
        
        assertMsgContainedByClass("modal-title", MSG_SUCCESS_VOTE); // "Uusi kansalaisaloite luotu ja kutsut lähetetty"
        
        // Click logout-button in modal
        if (logout){
            clickLinkContaining(getMessage(MSG_LOGOUT)); // "Kirjaudu ulos"
        
        // Close modal without an action
        } else {
            clickLinkContaining(getMessage(MSG_CONTINUE_BROWSING)); // "Jatka kirjautuneena"
        }
    }
    
    private void voteByUserNotAllowed(String firstName, String lastName, String ssn, boolean isFinnish) {
        System.out.println("--- voteByUser " + firstName + ", " + lastName + ", " + ssn);

        clickLinkContaining(getMessage(MSG_VOTE)); // "Kannata aloitetta"
        
        // --- uses dummyLogin for test, not VETUMA:
        inputText("firstName", firstName);
        inputText("lastName", lastName);
        inputText("ssn", ssn);
        if (!isFinnish){
            clickByName("finnishCitizen");
        }
        clickByName("Login");
        
        assertMsgContainedByClass("msg-warning", MSG_WARNING_VOTING_NOT_ALLOWED); // "Kannattaminen epäonnistui. Tämä voi johtua siitä, että et ole äänioikeutettu Suomen kansalainen tai aloitetta ei voi kannattaa."

        clickLinkContaining(getMessage(MSG_LOGOUT)); // "Kirjaudu ulos"
    }

    private void startNewInitiative(String userName) {
        System.out.println("--- startNewInitiative " + userName);
        open(urls.createNew());
        
        clickLinkContaining(getMessage(MSG_AUTHENTICATE)); // "Tunnistaudu ja tee aloite"
        clickLinkContaining(userName); // uses dummyLogin for test, not VETUMA

        assertTextByTag("h1", getMessage(MSG_PAGE_BEFORECREATE)); //"Tee uusi kansalaisaloite"
    }

    private String fillAndSaveInitiativeInFullEdit(boolean fillAllData, boolean success) {
        System.out.println("--- fillAndSaveInitiativeInFullEdit " + fillAllData);
        inputText("name.fi", TEST_INITIATIVE_TITLE);
        
        // Select today
        clickByName("startDate");
        clickById("calcurrent");
        assertValue("startDate", new LocalDate().toString(getMessage(MSG_DATE_FORMAT)));
        
        inputText("proposal.fi", "Sisältö");
        inputText("rationale.fi", "Perustelut");
        
        inputText("currentAuthor.contactInfo.email", "developer@solita.fi");

        clickByName(Urls.ACTION_SAVE);
        if (success) {
            assertMsgContainedByClass("msg-success", MSG_SUCCESS_SAVE); // "Lomakkeen tiedot tallennettu onnistuneesti"
        }
        return getPageUrl();
    }

    private String addAndSendInvitation() {
        System.out.println("--- addAndSendInvitation ");
        inputTextByCSS(".initiative-authors-area .last textarea", "developer@solita.fi");

        final MutableObject<String> invitationLink = captureInvitationLink();
        
        wait100(); // mikkole needs this wait
        clickByName(Urls.ACTION_SAVE_AND_SEND_INVITATIONS);
        //FIXME: use xpath or css to find more exact target ("system-msg" inside "modal-content")
        assertMsgContainedByClass("modal-title", MSG_SUCCESS_SAVE_AND_SEND_INVITATIONS); // "Uusi kansalaisaloite luotu ja kutsut l\u00E4hetetty"
        wait100(); // mikkole needs this wait
        clickLinkContaining(getMessage(MSG_CLOSE)); // "Sulje"
        waitms(500); // must wait modal to close properly to be sure that underlying functionality is enabled again 

        assertNotNull(invitationLink.get()); // invitation link captured from email
//        System.out.println(invitationLink.get());
        return invitationLink.get();
    }

    private void openAndAcceptInvitation(String invitationLink, String userName) {
        System.out.println("--- openAndAcceptInvitation " + userName  + " " + invitationLink);
        open(invitationLink);
        
        assertTextByTag("h4", getMessage(MSG_INVITTATION_TITLE)); // "Sinut on kutsuttu vastuuhenkilöksi tähän aloitteeseen"
        
        clickLinkContaining(getMessage(MSG_INVITATION_ACCEPT_MODAL)); // "Hyväksy kutsu"
        clickLinkContaining(userName);
        assertMsgContainedByClass("modal-title", MSG_INVITATION_ACCEPT_CONFIRM); // "Vahvista kutsun hyväksyminen"
        
        clickByName(Urls.ACTION_ACCEPT_INVITATION);
        
        assertMsgContainedByClass("msg-success", MSG_SUCCESS_ACCEPT_INVITATION); // "Sinut on tallennettu vastuuhenkilöksi tähän aloitteeseen."
    }

    private void sendToOM() {
        System.out.println("--- sendToOM");
        assertTextByTag("h4", getMessage(MSG_READY_FOR_OM)); // "Aloite on valmis lähetettäväksi Oikeusministeriön tarkastukseen"
        
        clickById("page-"+Urls.ACTION_SEND_TO_OM);
        assertMsgContainedByClass("modal-title", MSG_SEND_TO_OM); //  "Haluatko varmasti lähettää aloitteen oikeusministeriön tarkastettavaksi?"
        
        clickById("modal-"+Urls.ACTION_SEND_TO_OM);
        assertMsgContainedByClass("msg-success", MSG_SUCCESS_SEND_TO_OM); // "Aloite lähetetty oikeusministeriön tarkastettavaksi"
    }
    
    private void sendToVRK() {
        System.out.println("--- sendToVRK");
        assertTextByTag("h4", getMessage(MSG_SEND_TO_VRK, 3)); // "3 kannatusilmoitusta odottaa lähetystä Väestörekisterikeskukseen"            

        clickById("page-"+Urls.ACTION_SEND_TO_VRK);
        assertMsgContainedByClass("modal-title", MSG_SEND_TO_VRK_MODAL); //  "Lähetä Väestörekisterikeskukseen"
        
        clickById("modal-"+Urls.ACTION_SEND_TO_VRK);
        assertMsgContainedByClass("msg-success", MSG_SUCCESS_SEND_TO_VRK); // "Kannatusilmoitukset lähetetty Väestörekisterikeskukseen tarkastettavaksi"
    }
    
    private String loginUser(String firstName, String lastName, String ssn, boolean isFinnish, boolean useLoginLink) {
        System.out.println("--- loginUser: " + firstName + ", " + lastName  + ", " + ssn);
        
        if ( useLoginLink ) {
            clickLinkContaining(getMessage(MSG_LOGIN)); // "Kirjaudu sisään"
        }

        // --- uses dummyLogin for test, not VETUMA:
        inputText("firstName", firstName);
        inputText("lastName", lastName);
        inputText("ssn", ssn);
        //inputText("homeMunicipality", "Vantaa"); //default "Helsinki"
        if (!isFinnish){
            clickByName("finnishCitizen");
        }
        clickByName("Login");
        // ---

        String userName = firstName + " " + lastName;
        assertTextContainedByClass("logged-in-info", userName);
        return userName;
    }
    
    private void navigateToInitiative(boolean own) {
        clickLinkContaining(getMessage(MSG_PAGE_SEARCH)); // Selaa kansalaisaloitteita
        if (own) {
            clickLinkContaining(getMessage(MSG_PAGE_OWN_INITIATIVES)); // Omat kansalaisaloitteeni
        }
        clickLinkContaining(TEST_INITIATIVE_TITLE);
    }
    
    private void emptyPublicSearchResult() {
        clickLinkContaining(getMessage(MSG_PAGE_SEARCH)); // Selaa kansalaisaloitteita
        
        assertTextContainedByClass("msg-info", getMessage(MSG_SEARCH_RESULTS_EMPTY));
    }

    private void logoutUser() {
        System.out.println("--- logoutUser");
        clickLinkContaining(getMessage(MSG_LOGOUT)); // "Kirjaudu ulos"
        //open(urls.logout());
        wait100(); // mikkole needs this wait
        assertTitle(getMessage(MSG_PAGE_FRONTPAGE)+" - "+getMessage(MSG_SITE_NAME)); // "Etusivu - Kansalaisaloitepalvelu"
    }
    
//    
//    @Test
//    public void Voting_By_Under_Aged_Not_Allowed() {
//        final InitiativePublic initiative = new InitiativePublic(InitiativeDaoTest.createIntiative(1234l));
//
//        String ssn = new LocalDate().minusYears(10).toString("ddMMyy") + "A1234";
//        final User user = new User(ssn, null, "Antti", "Alaikäinen", true, HELSINKI);
//        
//        new NonStrictExpectations() {{
//            // BaseController.addModelDefaults
//            userService.getCurrentUser(false);
//            result = user;
//            // InitiativeController.view
//            userService.getCurrentUser();
//            result = user;
//
//            initiativeService.getInitiativeForPublic(1234l);
//            result = initiative;
//            
//            supportVoteService.getVotingInfo(initiative);
//            result = new VotingInfo(initiative, user, new DateTime(), null, 50, Months.ONE);
//         }};
//        open(urls.view(1234l));
//        
//        assertTextContainedByClass("msg-warning", getMessage("warning.voting-not-allowed"));
//    }

    
//    @Test
//    public void Create_By_Under_Aged_Not_Allowed() {
//        open(urls.createNew());
//        
//        clickLinkContaining(getMessage(MSG_AUTHENTICATE)); // "Tunnistaudu ja tee aloite"
//
//        String ssn = new LocalDate().minusYears(10).toString("ddMMyy") + "A1234";
//        final User user = new User(ssn, null, "Antti", "Alaikäinen", true, HELSINKI);
//        
//        new NonStrictExpectations() {{
//            // BaseController.addModelDefaults
//            userService.getCurrentUser(false);
//            result = user;
//            // InitiativeController.create
//            userService.getUserInRole(Role.AUTHENTICATED);
//            times = 2;
//            result = user;
//            // InitiativeController.managementView -> addVotingInfo
//            userService.getCurrentUser();
//            result = user;
////            supportVoteService.getVotingInfo((InitiativeBase)any);
////            result = new VotingInfo(initiative, user, new DateTime(), null, 50, Months.ONE);
//         }};
//         clickLinkContaining("Anna Testi"); // uses dummyLogin for test, not VETUMA
//        
//         assertTextContainedByClass("msg-warning", getMessage("warning.adult-required-as-author"));
//    }

    @SuppressWarnings("rawtypes")
    private MutableObject<String> captureInvitationLink() {
        final MutableObject<String> invitationLink = MutableObject.create();
        
        new NonStrictExpectations() {{
            emailService.sendInvitation((InitiativeManagement) withNotNull(), (Invitation) withNotNull()); 
            result = new Delegate() {
                @SuppressWarnings("unused")
                void sendInvitation(InitiativeManagement initiative, Invitation invitation) {
                    System.out.println("* delegate sendInvitation start");
                    invitationLink.set(urls.invitation(initiative.getId(), invitation.getInvitationCode()));
                    System.out.println("* delegate sendInvitation end");
                }
            };
         }};
         return invitationLink;
    }
    
}
