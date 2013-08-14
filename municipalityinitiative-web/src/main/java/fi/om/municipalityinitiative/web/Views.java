package fi.om.municipalityinitiative.web;


public final class Views {

    private Views() {}

    public static final String ERROR_VETUMA_VIEW ="error/vetuma";

    public static final String MODERATOR_LOGIN_VIEW = "om-login";

    public static final String SINGLE_LOGIN_VIEW = "single-login";

    public static final String VETUMA_LOGIN_VIEW = "vetuma-login";

    public static final String INDEX_VIEW = "index";

    public static final String TEST_DATA_GENERATION = "test-data-generation";
    
    public static final String ERROR_500_VIEW = "error/500";
    
    public static final String ERROR_404_VIEW = "error/404";

    public static final String ERROR_410_VIEW = "error/410";

    public static final String API_VIEW = "api";
    
    public static final String NEWS_VIEW = "pages/news";
    
    public static final String HELP_VIEW = "pages/help";

    public static final String HELP_EDIT_VIEW = "pages/edit_help";

    public static final String CONTENT_EDITOR_HELP_VIEW = "pages/content-editor-help";

    // New views

    public static final String SEARCH_VIEW = "find";
    public static final String PREPARE_VIEW = "prepare";
    public static final String AUTHENTICATE_VIEW = "authenticate";
    public static final String PENDING_CONFIRMATION = "pending-confirmation";
    public static final String PUBLIC_SINGLE_VIEW = "public-single-view";
    public static final String PUBLIC_COLLECT_VIEW = "public-collect-view";
    public static final String MANAGEMENT_VIEW = "management-view";
    public static final String MODERATION_VIEW = "moderation-view";
    public static final String MUNICIPALITY_MODERATION = "municipality-moderation-view";
    public static final String EDIT_VIEW = "edit-view";
    public static final String UPDATE_VIEW = "update-view";
    public static final String MANAGE_AUTHORS_VIEW = "manage-authors-view";
    public static final String INVITATION_VIEW = "invitation-view";
    public static final String INVITATION_REJECTED = "invitation-rejected";
    public static final String PARTICIPANT_LIST = "participant-list";
    public static final String PARTICIPANT_LIST_MANAGE = "participant-list-manage";
    public static final String IFRAME_GENERATOR_VIEW = "iframe-generator";
    public static final String IFRAME_VIEW = "iframe-view";
    public static final String STATUS_VIEW = "status";
    public static final String OWN_INITIATIVES = "own-initiatives";

    /**
     * Context relative redirect: context is prefixed to relative URLs.
     *
     * @param targetUri
     * @return
     */
    public static String contextRelativeRedirect(String targetUri) {
        return "redirect:" + targetUri;
    }
}
