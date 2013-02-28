package fi.om.municipalityinitiative.web;


public final class Views {

    private Views() {}

    public static final String DUMMY_LOGIN_VIEW = "dummy-login";
    
    public static final String VETUMA_LOGIN_VIEW = "vetuma-login";

    public static final String INDEX_VIEW = "index";

    public static final String REGISTERED_USER = "registered-user";

    public static final String TEST_DATA_GENERATION = "test-data-generation";
    
    public static final String ERROR_500_VIEW = "error/500";
    
    public static final String ERROR_404_VIEW = "error/404";

    public static final String API_VIEW = "api";
    
    public static final String NEWS_VIEW = "pages/news";
    
    public static final String HELP_VIEW = "pages/help";

    public static final String INFO_VIEW = "pages/info";

    // New views

    public static final String SEARCH_VIEW = "find";
    public static final String CREATE_VIEW = "create";
    public static final String VIEW_VIEW = "initiative";
    public static final String SINGLE_VIEW = "single-view";
    public static final String COLLECT_VIEW = "collect-view";
    public static final String MANAGEMENT_VIEW = "management-view";
    public static final String PARTICIPANT_LIST = "participant-list";
    public static final String IFRAME_VIEW = "iframe";
    public static final String STATUS_VIEW = "status";
    
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
