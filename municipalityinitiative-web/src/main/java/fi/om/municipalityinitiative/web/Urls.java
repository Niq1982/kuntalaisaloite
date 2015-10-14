package fi.om.municipalityinitiative.web;

import com.google.common.base.Strings;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.util.Maybe;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

import static fi.om.municipalityinitiative.util.Locales.LOCALE_FI;
import static fi.om.municipalityinitiative.util.Locales.LOCALE_SV;


public final class Urls {
    
    public static final String ID_PARAMETER = "{id}";

    public static final String PARAM_INITIATIVE_ID = "id";

    public static final String HELP_PAGE_PARAMETER = "{helpPage}";

    public static final String INFO_PAGE_PARAMETER = "{infoPage}";

    public static final String FILENAME_PARAMETER = "{fileName:.*}";

    public static final String FRONT =        "/";

    public static final String FRONT_FI =     "/fi";
    
    public static final String FRONT_SV =     "/sv";

    public static final String HELP_INDEX_FI =    "/fi/ohjeet";

    public static final String HELP_INDEX_SV =    "/sv/anvisningar";

    public static final String HELP_EDIT_INDEX_FI = "/fi/ohjemuokkaus";

    public static final String HELP_EDIT_INDEX_SV = "/sv/ohjemuokkaus";

    public static final String CONTENT_EDITOR_HELP = "/fi/sisallonsyoton-ohje";

    public static final String HELP_FI =    HELP_INDEX_FI + "/" + HELP_PAGE_PARAMETER;
    
    public static final String HELP_SV =    HELP_INDEX_SV + "/" + HELP_PAGE_PARAMETER;

    public static final String HELP_EDIT_FI =    HELP_EDIT_INDEX_FI + "/" + HELP_PAGE_PARAMETER;

    public static final String HELP_EDIT_SV =    HELP_EDIT_INDEX_SV + "/" + HELP_PAGE_PARAMETER;

    public static final String INFO_INDEX_FI =    "/fi/tietoa";
    
    public static final String INFO_INDEX_SV =    "/sv/om-tjansten";

    public static final String INFO_FI =    INFO_INDEX_FI + "/" + INFO_PAGE_PARAMETER;
    
    public static final String INFO_SV =    INFO_INDEX_SV + "/" + INFO_PAGE_PARAMETER;

    public static final String LOGIN_FI =        "/fi/login";

    public static final String LOGIN_SV =        "/sv/login";

    public static final String VETUMA_FI =        "/fi/vetuma";

    public static final String VETUMA_SV =        "/sv/vetuma";

    public static final String MODERATOR_LOGIN = "/om-login";

    public static final String LOGOUT_FI =       "/fi/logout";
    
    public static final String LOGOUT_SV =       "/sv/logout";

    public static final String TEST_DATA_GENERATION_FI =    "/fi/testdata";
    
    public static final String TEST_DATA_GENERATION_SV =    "/sv/testdata";

    public static final String API = "/api";
    
    public static final String INITIATIVES =  API + "/v1/initiatives";

    public static final String INITIATIVE =   INITIATIVES + "/" + ID_PARAMETER;

    public static final String MUNICIPALITIES = API + "/v1/municipalities";

    public static final String MUNICIPALITY = MUNICIPALITIES + "/" + ID_PARAMETER;

    public static final String SINGLE_MUNICIPALITY = "municipality";

    public static final String ERROR_404 = "/404";
    
    public static final String ERROR_500 = "/500";

    public static final String ERROR_410 = "/410";

    public static final String ERROR_409 = "/409";

    public static final String NEWS_FI = "/fi/uutiset";
    
    public static final String NEWS_SV = "/sv/nyheter";

    public static final String PARTICIPATING_CONFIRMATION_FI = "/fi/varmistus/"+ID_PARAMETER;

    public static final String PARTICIPATING_CONFIRMATION_SV = "/sv/bekraftelse/"+ID_PARAMETER;

    public static final String IMAGE_JSON =    "/imageJson";

    public static final String IMAGES = "/images";

    public static final String AUTHOR_MESSAGE_FI = "/fi/viesti";

    public static final String AUTHOR_MESSAGE_SV = "/sv/meddelande";

    public static final String VETUMA_ERROR_FI = "/fi/loginerror";

    public static final String VETUMA_ERROR_SV = "/sv/loginerror";

    public static final String VETUMA_AGE_ERROR_PARAMETER = "age";

    public static final String TARGET = "target";

    public static final String HISTORY_ITEM_PARAMETER = "historyItem";

    public static Urls FI = null;
    
    public static Urls SV = null;
    
    public static final String ENCODING = "UTF-8";
    
    public static final String PARAM_INVITATION_CODE = "invitation";

    public static final String PARAM_MUNICIPALITY = "municipalities";

    public static final String PARAM_INITIATIVE = "initiative";

    public static final String OLD_PARAM_MUNICIPALITY = "municipality";

    public static final String PARAM_SENT_COMMENT = "comment";

    public static final String PARAM_MANAGEMENT_CODE = "management";
    
    public static final String PARAM_AUTHOR_ID = "authorId";

    public static final String PARAM_CONFIRMATION_CODE = "confirmation";
    
    public static final String PARAM_PARTICIPANT_ID = "participantId";

    public static final String PARAM_ATTACHMENT_ID = "attachmentId";

    public static final String ACTION_MODERATOR_ADD_COMMENT = "moderatorComment";

    public static final String ACTION_SAVE = "action-save";

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
    
    public static final String ACTION_DELETE_AUTHOR = "action-delete-author";
    
    public static final String ACTION_DELETE_PARTICIPANT = "action-delete-participant";

    public static final String ACTION_CONTACT_AUTHOR = "action-contact-author";

    public static final String ACTION_ADD_ATTACHMENT = "action-add-attachment";

    public static final String ACTION_DELETE_ATTACHMENT = "action-delete-attachment";

    public static final String ACTION_RENEW_MUNICIPALITY_MANAGEMENT_HASH = "action-renew-municipality-management-hash";

    // Actions for the content editor

    public static final String ACTION_EDITOR_SAVE_DRAFT = "action-editor-save-draft";

    public static final String ACTION_EDITOR_PUBLISH_DRAFT = "action-editor-publish-draft";

    public static final String ACTION_EDITOR_RESTORE_PUBLISHED = "action-editor-restore-published";

    public static final String ACTION_EDITOR_UPLOAD_IMAGE = "action-editor-upload-image";

    public static final String JSONP_CALLBACK = "jsonp";

    public static final String JSON_OFFSET = "offset";

    public static final String JSON_LIMIT = "limit";

    public static final String JSON_MUNICIPALITY = "municipality";

    public static final int MAX_INITIATIVE_JSON_RESULT_COUNT = 50;

    public static final int DEFAULT_INITIATIVE_JSON_RESULT_COUNT = 20;

    public static final int DEFAULT_INITIATIVE_SEARCH_LIMIT = 20;

    public static final int MAX_INITIATIVE_SEARCH_LIMIT = 500;

    public static final int MAX_PARTICIPANT_LIST_LIMIT = 50;

    public static final int MAX_IFRAME_INITIATIVE_COUNT = 10;

    // New uris

    public static final String SEARCH_FI = "/fi/hae";

    public static final String SEARCH_SV = "/sv/sok";
    
    public static final String PREPARE_FI =    "/fi/aloitteen-valmistelu";

    public static final String PREPARE_SV =    "/sv/utforma-initiativ";
    
    public static final String AUTHENTICATE_FI = "/fi/tunnistaudu";
    
    public static final String AUTHENTICATE_SV = "/sv/autentisera";

    public static final String PENDING_CONFIRMATION_FI =    "/fi/odottaa-vahvistusta" + "/" + ID_PARAMETER;

    public static final String PENDING_CONFIRMATION_SV  =    "/sv/vantar-pa-bekraftelse" + "/" + ID_PARAMETER;
    
    public static final String VIEW_FI = "/fi/aloite" + "/" + ID_PARAMETER;

    public static final String VIEW_SV = "/sv/initiativ" + "/" + ID_PARAMETER;

    public static final String PARTICIPANT_LIST_FI = "/fi/osallistujat/" + ID_PARAMETER;

    public static final String PARTICIPANT_LIST_SV = "/sv/deltagare/" + ID_PARAMETER;

    public static final String PARTICIPANT_LIST_MANAGE_FI = "/fi/osallistujahallinta/" + ID_PARAMETER;

    public static final String PARTICIPANT_LIST_MANAGE_SV = "/sv/deltagarhantering/" + ID_PARAMETER;

    public static final String MANAGEMENT_FI = "/fi/yllapito" + "/" + ID_PARAMETER;

    public static final String MANAGEMENT_SV = "/sv/administration" + "/" + ID_PARAMETER;

    public static final String MODERATION_FI = "/fi/moderointi" + "/" + ID_PARAMETER;

    public static final String MODERATION_SV = "/sv/moderering" + "/" + ID_PARAMETER;

    public static final String MUNICIPALITY_MODERATION = "/fi/kuntahallinta";

    public static final String MUNICIPALITY_DECISION_FI_BASE_URL = "/fi/kunnanvastaus" + "/";

    public static final String MUNICIPALITY_DECISION_SV_BASE_URL = "/sv/kunnanvastaus" + "/";

    public static final String MUNICIPALITY_DECISION_FI = MUNICIPALITY_DECISION_FI_BASE_URL + ID_PARAMETER;

    public static final String MUNICIPALITY_DECISION_SV = MUNICIPALITY_DECISION_SV_BASE_URL + ID_PARAMETER;

    public static final String EDIT_MUNICIPALITY_DECISION_FI = MUNICIPALITY_DECISION_FI_BASE_URL + "edit/" + ID_PARAMETER;

    public static final String EDIT_MUNICIPALITY_DECISION_SV = MUNICIPALITY_DECISION_SV_BASE_URL + "edit/" + ID_PARAMETER;

    public static final String EDIT_MUNICIPALITY_DECISION_ATTACHMENTS_FI = MUNICIPALITY_DECISION_FI_BASE_URL + "attachments/" + ID_PARAMETER;

    public static final String EDIT_MUNICIPALITY_DECISION_ATTACHMENTS_SV = MUNICIPALITY_DECISION_SV_BASE_URL + "attachments/" + ID_PARAMETER;

    public static final String MUNICIPALITY_LOGIN_FI = "/fi/municipality-login";

    public static final String MUNICIPALITY_LOGIN_SV = "/sv/municipality-login";

    public static final String EDIT_FI = "/fi/muokkaa" + "/" + ID_PARAMETER;

    public static final String EDIT_SV = "/sv/redigera" + "/" + ID_PARAMETER;

    public static final String UPDATE_FI = "/fi/paivita" + "/" + ID_PARAMETER;

    public static final String UPDATE_SV = "/sv/uppdatera" + "/" + ID_PARAMETER;
    
    public static final String MANAGE_AUTHORS_FI = "/fi/vastuuhenkilot" + "/" + ID_PARAMETER;

    public static final String MANAGE_AUTHORS_SV = "/sv/ansvarpersoner" + "/" + ID_PARAMETER;
    
    public static final String MANAGE_ATTACHMENTS_FI = "/fi/liitteet" + "/";

    public static final String MANAGE_ATTACHMENTS_SV = "/sv/bilagor" + "/";

    public static final String INVITATION_FI = "/fi/kutsu" + "/" + ID_PARAMETER;

    public static final String INVITATION_SV = "/sv/inbjudan" + "/" + ID_PARAMETER;

    public static final String INVITATION_REJECTED_FI =    "/fi/kutsu-hylatty" + "/" + ID_PARAMETER;

    public static final String INVITATION_REJECTED_SV  =    "/sv/inbjudan-avbojts" + "/" + ID_PARAMETER;

    public static final String IFRAME_GENERATOR_FI = "/fi/leijuke";

    public static final String IFRAME_GENERATOR_SV = "/sv/widget";

    public static final String IFRAME_OLD_FI = "/fi/iframe";

    public static final String IFRAME_OLD_SV = "/sv/iframe";

    public static final String IFRAME_FI = "/iframe/fi";

    public static final String IFRAME_SV = "/iframe/sv";

    public static final String GRAPH_IFRAME_BASE_FI = "/graph-iframe/fi/";

    public static final String GRAPH_IFRAME_BASE_SV = "/graph-iframe/sv/";

    public static final String GRAPH_IFRAME_FI = GRAPH_IFRAME_BASE_FI + ID_PARAMETER;

    public static final String GRAPH_IFRAME_SV = GRAPH_IFRAME_BASE_SV + ID_PARAMETER;

    public static final String GRAPH_IFRAME_GENERATOR_FI = "/fi/graafi-leijuke";

    public static final String GRAPH_IFRAME_GENERATOR_SV = "/sv/graph-iframe";

    public static final String OWN_INITIATIVES_FI = "/fi/omat";

    public static final String OWN_INITIATIVES_SV = "/sv/egen";

    public static final String STATUS =  "/status";

    public static final String ATTACHMENT = "/attachment/" + ID_PARAMETER + "/" + FILENAME_PARAMETER;

    public static final String ATTACHMENT_THUMBNAIL = "/thumbnail/" + ID_PARAMETER;

    public static final String DECISION_ATTACHMENT = "/decision/attachment/" + ID_PARAMETER + "/" + FILENAME_PARAMETER;

    public static final String DECISION_ATTACHMENT_THUMBNAIL = "/decision/thumbnail/" + ID_PARAMETER;

    public static final String SUPPORTS_BY_DATE = API+"/v1/supports-by-date/" + ID_PARAMETER;

    private final String baseUrl;

    private final String iframeBaseUrl;

    private final String apiBaseUrl;

    private final Locale locale;

    private final String youthInitiativeBaseUrl;

    public static void initUrls(String baseUrl, String iframeBaseUrl, String apiBaseUrl, String youthInitiativeUrl) {
        FI = new Urls(baseUrl, iframeBaseUrl, apiBaseUrl, LOCALE_FI, youthInitiativeUrl);
        SV = new Urls(baseUrl, iframeBaseUrl, apiBaseUrl, LOCALE_SV, youthInitiativeUrl);
    }

    private Urls(String baseUrl, String iframeBaseUrl, String apiBaseUrl, Locale locale, String youthInitiativeBaseUrl) {
        this.baseUrl = baseUrl;
        this.iframeBaseUrl = iframeBaseUrl;
        this.apiBaseUrl = apiBaseUrl;
        this.locale = locale;
        this.youthInitiativeBaseUrl = youthInitiativeBaseUrl;
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
        return getLocalizedPageUrl(PARTICIPANT_LIST_FI, PARTICIPANT_LIST_SV).replace(ID_PARAMETER, initiativeId.toString());
    }

    public String participantListManage(Long initiativeId) {
        return getLocalizedPageUrl(PARTICIPANT_LIST_MANAGE_FI, PARTICIPANT_LIST_MANAGE_SV).replace(ID_PARAMETER, initiativeId.toString());
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
    
    public String manageAttachments(Long initiativeId) {
        return getManageAttachments(initiativeId);
    }

    public String confirmParticipant(Long participantId, String confirmCode) {
        return getLocalizedPageUrl(PARTICIPATING_CONFIRMATION_FI, PARTICIPATING_CONFIRMATION_SV).replace(ID_PARAMETER, participantId.toString())
                + "?" + PARAM_CONFIRMATION_CODE + "=" + confirmCode;
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

    public String youthInitiativeWebUrl(Long youthInitiativeId) {
        return youthInitiativeBaseUrl + (this.equals(FI) ? "/fi" : "/sv") + "/ideat/" + youthInitiativeId;
    }

    public String initiative(Long initiativeId) {
        return baseUrl + INITIATIVE.replace(ID_PARAMETER, initiativeId.toString());
    }

    public String getStatusPage() {
        return baseUrl + STATUS;
    }

    public String iframe() {
        return iframeBaseUrl + (this.equals(FI) ? IFRAME_FI : IFRAME_SV);
    }

    public String iframe(Long municipalityId) {
        return iframe() + "?" + PARAM_MUNICIPALITY + "=" + municipalityId;
    }

    public String iframe(Long ...municipalityIds) {
        String url =  iframe() + "?" + PARAM_MUNICIPALITY + "=" ;
        for (int i = 0; i <municipalityIds.length; i++) {
            if (i != 0) {
                url +=",";
            }
            url +=""+municipalityIds[i];
        }
        return url;
    }
    public String iframeWithOldApiMunicipality(Long municipalityId) {
        return iframe() + "?" + OLD_PARAM_MUNICIPALITY + "=" + municipalityId;
    }
    public String iframeWithOldestApiMunicipality(Long municipalityId) {
        return iframeBaseUrl + (this.equals(FI) ? IFRAME_OLD_FI : IFRAME_OLD_SV) + "?" + OLD_PARAM_MUNICIPALITY + "=" + municipalityId;
    }
    public String iframeWithOldestApiMunicipality() {
        return iframeBaseUrl + (this.equals(FI) ? IFRAME_OLD_FI : IFRAME_OLD_SV) + "?" + OLD_PARAM_MUNICIPALITY;
    }
    public String iframeGenerator() {
        return getLocalizedPageUrl(IFRAME_GENERATOR_FI, IFRAME_GENERATOR_SV);
    }

    public String graphIFrame(Long initiativeId) {
        return iframeBaseUrl + (this.equals(FI) ? GRAPH_IFRAME_BASE_FI : GRAPH_IFRAME_BASE_SV) + initiativeId;
    }

    public String graphIFrameGenerator() {return getLocalizedPageUrl(GRAPH_IFRAME_GENERATOR_FI, GRAPH_IFRAME_GENERATOR_SV);}


    public String getManagement(Long id) {
        return getLocalizedPageUrl(MANAGEMENT_FI, MANAGEMENT_SV).replace(ID_PARAMETER, id.toString());
    }

    public String getModeration(Long id) {
        return getLocalizedPageUrl(MODERATION_FI, MODERATION_SV).replace(ID_PARAMETER, id.toString());
    }

    public String moderation(Long id, Long historyItemId) {
        return getModeration(id) + "?" + HISTORY_ITEM_PARAMETER + "=" + historyItemId.toString();
    }

    public String getManageAuthors(Long id) {
        return getLocalizedPageUrl(MANAGE_AUTHORS_FI, MANAGE_AUTHORS_SV).replace(ID_PARAMETER, id.toString());
    }
    
    public String getManageAttachments(Long id) {
        return getLocalizedPageUrl(MANAGE_ATTACHMENTS_FI+ID_PARAMETER, MANAGE_ATTACHMENTS_SV+ID_PARAMETER).replace(ID_PARAMETER, id.toString());
    }

    public String vetumaLogin() {
        return getLocalizedPageUrl(VETUMA_FI, VETUMA_SV);
    }

    public String loginAuthor(String managementHash) {
        return loginAuthor() + "?" + PARAM_MANAGEMENT_CODE + "=" + managementHash;
    }

    public String loginAuthor() {
        return getLocalizedPageUrl(LOGIN_FI, LOGIN_SV);
    }

    public String api() {
        return apiBaseUrl+API;
    }

    public String initiatives() {
        return apiBaseUrl + INITIATIVES;
    }

    public String municipalities() {
        return apiBaseUrl + MUNICIPALITIES;
    }

    public String invitation(Long initiativeId, String confirmationCode) {
        return getLocalizedPageUrl(INVITATION_FI, INVITATION_SV).replace(ID_PARAMETER, initiativeId.toString()) + "?" + PARAM_INVITATION_CODE + "=" + confirmationCode;
    }

    public String invitationRejected(Long initiativeId) {
        return getLocalizedPageUrl(INVITATION_REJECTED_FI, INVITATION_REJECTED_SV).replace(ID_PARAMETER, initiativeId.toString());
    }

    public String getIframeBaseUrl() {
        return iframeBaseUrl;
    }

    public String search() {
        return getLocalizedPageUrl(SEARCH_FI, SEARCH_SV);
    }

    public String news() {
        return getLocalizedPageUrl(NEWS_FI, NEWS_SV);
    }

    public String prepare() {
        return getLocalizedPageUrl(PREPARE_FI, PREPARE_SV);
    }

    public String prepare(Maybe<List<Municipality>> municipalities) {
        String url = getLocalizedPageUrl(PREPARE_FI, PREPARE_SV);
        if (municipalities.isPresent() && municipalities.getValue().size() == 1) {
            url+="?" + SINGLE_MUNICIPALITY + "=" + municipalities.getValue().get(0).getId();
        }
        return url;
    }


    public String authenticate() {
        return getLocalizedPageUrl(AUTHENTICATE_FI, AUTHENTICATE_SV);
    }

    public String authenticate(String target) {
        return getLocalizedPageUrl(AUTHENTICATE_FI, AUTHENTICATE_SV) + "?" + TARGET + "=" + urlEncode(Strings.isNullOrEmpty(target) ? baseUrl : target);
    }

    public String pendingConfirmation(Long initiativeId) {
        return getLocalizedPageUrl(PENDING_CONFIRMATION_FI, PENDING_CONFIRMATION_SV).replace(ID_PARAMETER, initiativeId.toString());
    }

    public String contentEditorHelp() {
        return baseUrl + CONTENT_EDITOR_HELP;
    }

    public String vetumaLogin(String target) {
        return vetumaLogin() + "?"+ TARGET +"=" + urlEncode(Strings.isNullOrEmpty(target) ? baseUrl : target);
    }

    public String moderatorLogin() {
        return getLocalizedPageUrl(MODERATOR_LOGIN, MODERATOR_LOGIN);
    }

    public String moderatorLogin(Long initiativeId) {
        return getLocalizedPageUrl(MODERATOR_LOGIN, MODERATOR_LOGIN) + "?" + TARGET + "=" + urlEncode(MODERATION_FI.replace(ID_PARAMETER, initiativeId.toString()));
    }

    public String attachment(Long id, String fileName, boolean municipality) {
        if (municipality) {
            return baseUrl + DECISION_ATTACHMENT.replace(ID_PARAMETER, id.toString()).replace(FILENAME_PARAMETER, fileName);
        }
        return baseUrl + ATTACHMENT.replace(ID_PARAMETER, id.toString()).replace(FILENAME_PARAMETER, fileName);
    }

    public String getAttachmentThumbnail(Long id, boolean municipality) {
        if (municipality) {
            return baseUrl + DECISION_ATTACHMENT_THUMBNAIL.replace(ID_PARAMETER, id.toString());
        }
        return baseUrl + ATTACHMENT_THUMBNAIL.replace(ID_PARAMETER, id.toString());
    }

    public String widget(Long initiativeId) {
        return getLocalizedPageUrl(GRAPH_IFRAME_GENERATOR_FI, GRAPH_IFRAME_GENERATOR_SV)+ "?" + PARAM_INITIATIVE_ID + "=" + initiativeId;
    }

    public String testDataGeneration() {
        return getLocalizedPageUrl(TEST_DATA_GENERATION_FI, TEST_DATA_GENERATION_SV);
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

    public static String urlPercentEncode(String s) {
        String ret = s;
        ret = ret.replaceAll("%", "%25");
        ret = ret.replaceAll("&", "%26");
        ret = ret.replaceAll(" ", "%20");
        ret = ret.replaceAll(":", "%3A");
        ret = ret.replaceAll("ä|\u00E4|&auml;",  "%C3%A4");
        ret = ret.replaceAll("å|\u00E5|&aring;", "%C3%A5");
        ret = ret.replaceAll("ö|\u00F6|&ouml;",  "%C3%B6");

        //TODO: add other chars or find some existing converter ...
        return ret;
    }

    public boolean isVetumaLoginPage(String target) {
        return target.startsWith(vetumaLogin());
    }

    public String municipalityModeration() {
        return getLocalizedPageUrl(MUNICIPALITY_MODERATION, MUNICIPALITY_MODERATION);

    }


    public String helpEdit(String localizedPageName) {
        return getLocalizedPageUrl(HELP_EDIT_FI, HELP_EDIT_SV).replace(HELP_PAGE_PARAMETER, localizedPageName);
    }

    public String images() {
        return IMAGES;
    }

    public String confirmAuthorMessage(String confirmationCode) {
        return getLocalizedPageUrl(AUTHOR_MESSAGE_FI, AUTHOR_MESSAGE_SV) + "?" + PARAM_CONFIRMATION_CODE + "=" + confirmationCode;
    }

    public String vetumaError() {
        return getLocalizedPageUrl(VETUMA_ERROR_FI, VETUMA_ERROR_SV);
    }

    public String ownInitiatives() {
        return getLocalizedPageUrl(OWN_INITIATIVES_FI, OWN_INITIATIVES_SV);
    }

    public String loginToManagement(Long initiativeId) {
        return vetumaLogin((this.equals(FI) ? MANAGEMENT_FI : MANAGEMENT_SV).replace(ID_PARAMETER, initiativeId.toString()));
    }

    public String notAdultError() {
        return getLocalizedPageUrl(VETUMA_ERROR_FI, VETUMA_ERROR_SV) + "?"+ VETUMA_AGE_ERROR_PARAMETER;
    }

    public String getMunicipalityDecisionView(Long initiativeId) {
        return getLocalizedPageUrl(MUNICIPALITY_DECISION_FI, MUNICIPALITY_DECISION_SV).replace(ID_PARAMETER, initiativeId.toString());
    }

    public String openDecisionForEdit(Long initiativeId) {
        return getLocalizedPageUrl(EDIT_MUNICIPALITY_DECISION_FI, EDIT_MUNICIPALITY_DECISION_SV).replace(ID_PARAMETER, initiativeId.toString());
    }

    public String openDecisionAttachmentsForEdit(Long initiativeId) {
        return getLocalizedPageUrl(EDIT_MUNICIPALITY_DECISION_ATTACHMENTS_FI, EDIT_MUNICIPALITY_DECISION_ATTACHMENTS_SV).replace(ID_PARAMETER, initiativeId.toString());
    }
}
