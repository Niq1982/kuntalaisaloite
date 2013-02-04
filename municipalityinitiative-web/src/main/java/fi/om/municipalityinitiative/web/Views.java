package fi.om.municipalityinitiative.web;


public final class Views {

    private Views() {}
    
    public static final String INITIATIVE_AUTHOR = "initiative-author";
    
    public static final String INITIATIVE_OM = "initiative-om";
    
    public static final String INITIATIVE_VRK = "initiative-vrk";
    
    public static final String BEFORE_CREATE_VIEW = "before-create";
    
    public static final String PUBLIC_VIEW = "initiative-public";
    
    public static final String INVITATION_VIEW = "invitation-response";
    
    public static final String ACCEPT_INVITATION_VIEW = "invitation-accept";
    
    public static final String UNCONFIRMED_AUTHOR = "unconfirmed-author";
    
    public static final String VOTE_VIEW = "vote";
    
    public static final String DUMMY_LOGIN_VIEW = "dummy-login";
    
    public static final String VETUMA_LOGIN_VIEW = "vetuma-login";

    public static final String SEARCH_VIEW_OLD = "search_old";

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
    public static final String VIEW_VIEW = "public-view";
    public static final String SINGLE_VIEW = "single-view";
    public static final String COLLECT_VIEW = "collect-view";
    public static final String MANAGEMENT_VIEW = "management-view";
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
