package fi.om.municipalityinitiative.web;

import com.google.common.base.Strings;
import fi.om.municipalityinitiative.util.Locales;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

import static fi.om.municipalityinitiative.util.Locales.LOCALE_FI;
import static fi.om.municipalityinitiative.util.Locales.LOCALE_SV;


public final class Urls {
    
    public static final String ID_PARAMETER = "{id}";

    public static final String HELP_PAGE_PARAMETER = "{helpPage}";

    public static final String INFO_PAGE_PARAMETER = "{infoPage}";
    
    public static final String FILE_NAME_PARAMETER = "{fileName}";
    
    public static final String FRONT =        "/";

    public static final String FRONT_FI =     "/fi";
    
    public static final String FRONT_SV =     "/sv";

    public static final String HELP_INDEX_FI =    "/fi/ohjeet";
    
    public static final String HELP_INDEX_SV =    "/sv/anvisningar";

    public static final String HELP_FI =    HELP_INDEX_FI + "/" + HELP_PAGE_PARAMETER;
    
    public static final String HELP_SV =    HELP_INDEX_SV + "/" + HELP_PAGE_PARAMETER;

    public static final String INFO_INDEX_FI =    "/fi/tietoa";
    
    public static final String INFO_INDEX_SV =    "/sv/om-tjansten";

    public static final String INFO_FI =    INFO_INDEX_FI + "/" + INFO_PAGE_PARAMETER;
    
    public static final String INFO_SV =    INFO_INDEX_SV + "/" + INFO_PAGE_PARAMETER;

    public static final String LOGIN_FI =        "/fi/login";

    public static final String LOGIN_SV =        "/sv/login";

    public static final String LOGOUT_FI =       "/fi/logout";
    
    public static final String LOGOUT_SV =       "/sv/logout";

    public static final String SEARCH_OLD_FI =    "/fi/haeOLD";
    
    public static final String SEARCH_OLD_SV =    "/sv/sokOLD";
    
    public static final String MY_ACCOUNT_FI =    "/fi/omat-tiedot";
    
    public static final String MY_ACCOUNT_SV =    "/sv/egna-upgifter";

    public static final String TEST_DATA_GENERATION_FI =    "/fi/testdata";
    
    public static final String TEST_DATA_GENERATION_SV =    "/sv/testdata";
    
    public static final String DOWNLOAD_VOTES = "/support-votes/" + ID_PARAMETER + "/" + FILE_NAME_PARAMETER;
    
    public static final String API = "/api";
    
    public static final String INITIATIVES =  API + "/v1/initiatives";

    public static final String INITIATIVE =   INITIATIVES + "/" + ID_PARAMETER;

    public static final String MUNICIPALITIES = API + "/v1/municipalities";

    public static final String MUNICIPALITY = MUNICIPALITIES + "/" + ID_PARAMETER;

    public static final String SERVICES = "/services";
    
    public static final String KEEPALIVE =  SERVICES + "/keepalive";

    
    public static final String ERROR_404 = "/404";
    
    public static final String ERROR_500 = "/500";

    public static final String ERROR_410 = "/410";
    
    
    public static final String NEWS_FI = "/fi/uutiset";
    
    public static final String NEWS_SV = "/sv/nyheter";

    public static final String PARTICIPATING_CONFIRMATION_FI = "/fi/varmistus/"+ID_PARAMETER;

    public static final String PARTICIPATING_CONFIRMATION_SV = "/sv/forsakra/"+ID_PARAMETER;
    
    public static Urls FI = null;
    
    public static Urls SV = null;
    
    public static final String ENCODING = "UTF-8";
    
    public static final String PARAM_INVITATION_CODE = "invitation";

    public static final String PARAM_MUNICIPALITY = "municipality";

    public static final String PARAM_SENT_COMMENT = "sentComment";
    
    public static final String SEARCH_UNREMOVED_VOTES = "searchUnremovedVotes";
    
    public static final String SEARCH_OWN_ONLY = "includeOwn=true&includePublic=false";
    
    // Municipality initiative STARTS
    public static final String PARAM_MANAGEMENT_CODE = "management";

    public static final String PARAM_PARTICIPANT_CONFIRMATION_CODE = "confirmation";
    
    public static final String PARAM_PARTICIPANT_ID = "participantId";

    public static final String ACTION_SAVE = "action-save";
    
    public static final String ACTION_SAVE_AND_SEND = "action-save-and-send";
    
    public static final String ACTION_SEND_TO_REVIEW = "action-send-to-review";
    
    public static final String ACTION_SEND_TO_REVIEW_COLLECT = "action-send-to-review-collect";

    public static final String ACTION_SEND_FIX_TO_REVIEW = "action-send-fix-to-review";
    
    public static final String ACTION_UPDATE_INITIATIVE = "action-update-initiative";

    public static final String ACTION_ACCEPT_INITIATIVE = "action-accept-initiative";
    
    public static final String ACTION_REJECT_INITIATIVE = "action-reject-initiative";

    public static final String ACTION_SEND_TO_FIX = "action-send-to-fix";
    
    public static final String ACTION_ACCEPT_INVITATION = "action-accept-invitation";

    public static final String ACTION_REJECT_INVITATION = "action-decline-invitation";
    
    public static final String ACTION_START_COLLECTING = "action-start-collecting";
    
    public static final String ACTION_SEND_TO_MUNICIPALITY = "action-send-to-muninicipality";
    
    public static final String ACTION_DELETE_PARTICIPANT = "action-delete-participant";
    
    // Municipality initiative ENDS
    
    public static final String ACTION_SAVE_AND_SEND_INVITATIONS = "action-save-and-send-invitations";
    
    public static final String ACTION_SEND_INVITATIONS = "action-send-invitations";
    
    public static final String ACTION_UPDATE_VRK_RESOLUTION = "action-vrk-update-resolution";
    
    public static final String ACTION_VOTE = "action-vote";
    
    public static final String ACTION_SEND_TO_OM = "action-send-to-om";
    
    public static final String ACTION_ACCEPT_BY_OM = "action-accept-by-om";
    
    public static final String ACTION_REJECT_BY_OM = "action-reject-by-om";
    
    public static final String ACTION_SEND_TO_VRK = "action-send-to-vrk"; 

    public static final String ACTION_CONFIRM_CURRENT_AUTHOR = "action-confirm-current-author";

    public static final String ACTION_DELETE_CURRENT_AUTHOR = "action-delete-current-author";

    public static final String ACTION_REMOVE_SUPPORT_VOTES = "action-remove-support-votes";

    public static final String JSONP_CALLBACK = "jsonp";

    public static final String JSON_OFFSET = "offset";

    public static final String JSON_LIMIT = "limit";

    public static final String JSON_MUNICIPALITY = "municipality";

    public static final int MAX_INITIATIVE_JSON_RESULT_COUNT = 50;

    public static final int DEFAULT_INITIATIVE_JSON_RESULT_COUNT = 20;

    public static final int DEFAULT_INITIATIVE_SEARCH_LIMIT = 20;
    public static final int MAX_INITIATIVE_SEARCH_LIMIT = 500;

    // New uris

    public static final String SEARCH_FI = "/fi/hae";

    public static final String SEARCH_SV = "/sv/sok";
    
    public static final String PREPARE_FI =    "/fi/aloitteen-valmistelu";

    public static final String PREPARE_SV =    "/sv/initiativ-prepare";

    public static final String PENDING_CONFIRMATION_FI =    "/fi/odottaa-vahvistusta" + "/" + ID_PARAMETER;

    public static final String PENDING_CONFIRMATION_SV  =    "/sv/odottaa-vahvistusta-sv" + "/" + ID_PARAMETER;
    
    public static final String VIEW_FI = "/fi/aloite" + "/" + ID_PARAMETER;

    public static final String VIEW_SV = "/sv/initiativ" + "/" + ID_PARAMETER;
    
    public static final String PARITICIPANT_LIST_FI = VIEW_FI + "/osallistujat";

    public static final String PARITICIPANT_LIST_SV = VIEW_SV + "/deltagarna";
    
    public static final String PARITICIPANT_LIST_MANAGE_FI = VIEW_FI + "/osallistujahallinta";

    public static final String PARITICIPANT_LIST_MANAGE_SV = VIEW_SV + "/deltagaradministration";

    public static final String MANAGEMENT_FI = "/fi/hallinta" + "/" + ID_PARAMETER;

    public static final String MANAGEMENT_SV = "/sv/administration" + "/" + ID_PARAMETER;

    public static final String MODERATION_FI = "/fi/moderointi" + "/" + ID_PARAMETER;

    public static final String MODERATION_SV = "/sv/moderation" + "/" + ID_PARAMETER;

    public static final String MUNICIPALITY_MODERATION = "/fi/kuntahallinta";

    public static final String EDIT_FI = "/fi/muokkaa" + "/" + ID_PARAMETER;

    public static final String EDIT_SV = "/sv/bearbeta" + "/" + ID_PARAMETER;

    public static final String UPDATE_FI = "/fi/paivita" + "/" + ID_PARAMETER;

    public static final String UPDATE_SV = "/sv/paivita" + "/" + ID_PARAMETER;
    
    public static final String MANAGE_AUTHORS_FI = "/fi/vastuuhenkilot" + "/" + ID_PARAMETER;

    public static final String MANAGE_AUTHORS_SV = "/sv/ansvarpersoner" + "/" + ID_PARAMETER;
    
    public static final String INVITATION_FI = "/fi/kutsu" + "/" + ID_PARAMETER;
    
    public static final String INVITATION_SV = "/sv/inbjudan" + "/" + ID_PARAMETER;

    public static final String IFRAME_FI = "/fi/iframe";

    public static final String IFRAME_SV = "/sv/iframe";

    public static final String STATUS =  "/status";

    private final String baseUrl;
    
    private final Locale locale;

    public static void initUrls(String baseUrl) {
        FI = new Urls(baseUrl, LOCALE_FI);
        SV = new Urls(baseUrl, LOCALE_SV);
    }
    
    private Urls(String baseUrl, Locale locale) {
        this.baseUrl = baseUrl;
        this.locale = locale;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    private String getLocalizedPageUrl(String fiSuffix, String svSuffix) {
        StringBuilder stringBuilder = new StringBuilder(baseUrl);
        if (this.equals(FI)) {
            return stringBuilder.append(fiSuffix).toString();
        } else {
            return stringBuilder.append(svSuffix).toString();
        }
    }

    public String frontpage() {
        return getLocalizedPageUrl(FRONT_FI, FRONT_SV);
    }

    public String helpIndex() {
        return getLocalizedPageUrl(HELP_INDEX_FI, HELP_INDEX_SV);
    }

    public String help(String localizedPageName) {
        return getLocalizedPageUrl(HELP_FI, HELP_SV).replace(HELP_PAGE_PARAMETER, localizedPageName);
    }

    public String infoIndex() {
        return getLocalizedPageUrl(INFO_INDEX_FI, INFO_INDEX_SV);
    }

    public String info(String localizedPageName) {
        return getLocalizedPageUrl(INFO_FI, INFO_SV).replace(INFO_PAGE_PARAMETER, localizedPageName);
    }

    public String view(Long initiativeId) {
        return getLocalizedPageUrl(VIEW_FI, VIEW_SV).replace(ID_PARAMETER, initiativeId.toString());
    }
    
    public String participantList(Long initiativeId) {
        return getLocalizedPageUrl(PARITICIPANT_LIST_FI, PARITICIPANT_LIST_SV).replace(ID_PARAMETER, initiativeId.toString());
    }
    
    public String participantListManage(Long initiativeId) {
        return getLocalizedPageUrl(PARITICIPANT_LIST_MANAGE_FI, PARITICIPANT_LIST_MANAGE_SV).replace(ID_PARAMETER, initiativeId.toString());
    }

    public String management(Long initiativeId) {
        return getManagement(initiativeId);
    }
    
    public String moderation(Long initiativeId) {
        return getModeration(initiativeId);
    }

    public String edit(Long initiativeId) {
        return getEdit(initiativeId);
    }
    
    public String manageAuthors(Long initiativeId) {
        return getManageAuthors(initiativeId);
    }

    public String confirmParticipant(Long participantId, String confirmCode) {
        return getLocalizedPageUrl(PARTICIPATING_CONFIRMATION_FI, PARTICIPATING_CONFIRMATION_SV).replace(ID_PARAMETER, participantId.toString())
                + "?" + PARAM_PARTICIPANT_CONFIRMATION_CODE + "=" + confirmCode;
    }

    public String getEdit(Long initiativeId) {
        return getLocalizedPageUrl(EDIT_FI, EDIT_SV).replace(ID_PARAMETER, initiativeId.toString());
    }

    public String update(Long initiativeId) {
        return getUpdate(initiativeId);
    }

    private String getUpdate(Long initiativeId) {
        return getLocalizedPageUrl(UPDATE_FI, UPDATE_SV).replace(ID_PARAMETER, initiativeId.toString());
    }

    public String vote(Long initiativeId) {
        return view(initiativeId) + "?" + ACTION_VOTE;
    }

    public String initiative(Long initiativeId) {
        return baseUrl + INITIATIVE.replace(ID_PARAMETER, initiativeId.toString());
    }

    public String getStatusPage() {
        return baseUrl + STATUS;
    }

    public String iframe() {
        return getLocalizedPageUrl(IFRAME_FI, IFRAME_SV);
    }

    public String iframe(Long municipalityId) {
        return iframe() + "?" + PARAM_MUNICIPALITY + "=" + municipalityId;
    }

    public String getManagement(Long id) {
        return getLocalizedPageUrl(MANAGEMENT_FI, MANAGEMENT_SV).replace(ID_PARAMETER, id.toString());
    }
    
    public String getModeration(Long id) {
        return getLocalizedPageUrl(MODERATION_FI, MODERATION_SV).replace(ID_PARAMETER, id.toString());
    }
    
    public String getManageAuthors(Long id) {
        return getLocalizedPageUrl(MANAGE_AUTHORS_FI, MANAGE_AUTHORS_SV).replace(ID_PARAMETER, id.toString());
    }

    public String login() {
        return getLocalizedPageUrl(LOGIN_FI, LOGIN_SV);
    }

    public String loginAuthor(String managementHash) {
        return login() + "?" + PARAM_MANAGEMENT_CODE + "=" + managementHash;
    }

    public String initiatives() {
        return baseUrl + INITIATIVES;
    }

    public String municipalities() {
        return baseUrl + MUNICIPALITIES;
    }
    
    public String voteAction(Long initiativeId) {
        return view(initiativeId);
    }

    public String invitation(Long initiativeId, String confirmationCode) {
        return getLocalizedPageUrl(INVITATION_FI, INVITATION_SV).replace(ID_PARAMETER, initiativeId.toString()) + "?" + PARAM_INVITATION_CODE + "=" + confirmationCode;
    }

    public String confirmAcceptInvitation(Long initiativeId) {
        return view(initiativeId) + "?" + ACTION_ACCEPT_INVITATION;
    }
    
    public String search_old() {
        return getLocalizedPageUrl(SEARCH_OLD_FI, SEARCH_OLD_SV);
    }

    public String search() {
        return getLocalizedPageUrl(SEARCH_FI, SEARCH_SV);
    }
    
    public String news() {
        return getLocalizedPageUrl(NEWS_FI, NEWS_SV);
    }

    public String searchUnremovedVotes() {
        return searchUnremovedVotes(""); // default search_old
    }
    
    public String searchUnremovedVotes(String periodBeforeDeadLine) {
        return search_old() +  "?" + SEARCH_UNREMOVED_VOTES + "=" + periodBeforeDeadLine;
    }
    
    public String searchOwnOnly() {
        return search_old() + "?" + SEARCH_OWN_ONLY;
    }

    public String prepare() {
        return getLocalizedPageUrl(PREPARE_FI, PREPARE_SV);
    }

    public String pendingConfirmation(Long initiativeId) {
        return getLocalizedPageUrl(PENDING_CONFIRMATION_FI, PENDING_CONFIRMATION_SV).replace(ID_PARAMETER, initiativeId.toString());
    }

    public String paramSendInvitations() {
        return ACTION_SEND_INVITATIONS;
    }

    public String login(String target) {
        if (Strings.isNullOrEmpty(target)) {
            target = baseUrl;
        }
        
        return login() + "?target=" + urlEncode(target);
    }

    public String testDataGeneration() {
        return getLocalizedPageUrl(TEST_DATA_GENERATION_FI, TEST_DATA_GENERATION_SV);
    }
    
    public String downloadVotes(Long batchId, String fileName) {
        String url = DOWNLOAD_VOTES
                .replace(ID_PARAMETER, batchId.toString())
                .replace(FILE_NAME_PARAMETER, urlEncode(fileName));
        return baseUrl + url;
    }
    
    public String logout() {
        return getLocalizedPageUrl(LOGOUT_FI, LOGOUT_SV);
    }
    
    public Locale getLocale() {
        return locale;
    }
    
    public String getLang() {
        return locale.getLanguage();
    }
    
    public Urls alt() {
        if (this.equals(FI)) {
            return SV;
        } else {
            return FI;
        }
    }
    
    public String myAccount() {
        return getLocalizedPageUrl(MY_ACCOUNT_FI, MY_ACCOUNT_SV);
    }
    
    public Locale getAltLocale() {
        return alt().getLocale();
    }
    
    public String getAltLang() {
        return getAltLocale().getLanguage();
    }
    
    public static Urls get(Locale locale) {
        if (Locales.LOCALE_SV.equals(locale)) {
            return SV;
        } else {
            return FI;
        }
    }

    public static String urlPercentEncode(String s) {
        String ret = s;
        ret = ret.replaceAll("%", "%25");
        ret = ret.replaceAll(" ", "%20");
        ret = ret.replaceAll(":", "%3A");
        ret = ret.replaceAll("ä|\u00E4|&auml;",  "%C3%A4");
        ret = ret.replaceAll("å|\u00E5|&aring;", "%C3%A5");
        ret = ret.replaceAll("ö|\u00F6|&ouml;",  "%C3%B6");

        //TODO: add other chars or find some existing converter ...
        return ret;
    }
    
    private static String urlEncode(String s) {
        if (s == null) {
            return "";
        } else {
            try {
                return URLEncoder.encode(s, ENCODING);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean isLoginPage(String target) {
        return target.startsWith(login());
    }


    public String municipalityModeration() {
        return getLocalizedPageUrl(MUNICIPALITY_MODERATION, MUNICIPALITY_MODERATION);

    }
}
