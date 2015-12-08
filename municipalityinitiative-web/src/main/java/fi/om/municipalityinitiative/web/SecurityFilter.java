package fi.om.municipalityinitiative.web;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import fi.om.municipalityinitiative.exceptions.AuthenticationRequiredException;
import fi.om.municipalityinitiative.exceptions.CookiesRequiredException;
import fi.om.municipalityinitiative.exceptions.VerifiedLoginRequiredException;
import fi.om.municipalityinitiative.service.EncryptionService;
import fi.om.municipalityinitiative.util.UrlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.NestedServletException;
import org.springframework.web.util.WebUtils;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class SecurityFilter implements Filter {

    private static final String CSRF_TOKEN_NAME = "CSRFToken";
    private static final int CSRF_TOKEN_LENGTH = 24;
    private static final String COOKIE_ERROR = "cookieError";
    public static final String UNWANTED_HIDDEN_EMAIL_FIELD = "email";

    protected final Logger log = LoggerFactory.getLogger(SecurityFilter.class);

    private UrlHelper urlPathHelper = new UrlHelper();

    @Resource
    private EncryptionService encryptionService;

    private final boolean disableSecureCookie;

    public SecurityFilter(boolean disableSecureCookie) {
        this.disableSecureCookie = disableSecureCookie;
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }


    public static void setNoCache(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
        response.setHeader("Pragma", "no-cache");                                   // HTTP 1.0
        response.setDateHeader("Expires", 0);                                       // Proxies
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        log.info("REQ: " + request.getRequestURI() + "?" + request.getQueryString());

        setNoCache(response);

        try {
            verifyOrInitializeCsrfToken(request, response);
            checkInvalidParameters(request);
            chain.doFilter(servletRequest, servletResponse);
        } catch (CSRFException e) {
            csrfException(e, request, response);
        } catch (NestedServletException e) {
            Throwable t = e.getCause();
            if (t instanceof AuthenticationRequiredException) {
                authenticationRequired((AuthenticationRequiredException) t, request, response);
            } else if (t instanceof VerifiedLoginRequiredException) {
                verifiedLoginRequired(request, response);
            }
            else if (t instanceof CSRFException) {
                csrfException(e, request, response);
            } else {
                propagateException(e);
            }
        }
    }

    private void verifiedLoginRequired(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(Urls.FI.vetumaLogin(urlPathHelper.getOriginalRequestUriWithQueryString(request)));
    }

    private void authenticationRequired(AuthenticationRequiredException e, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//        StringBuilder target = new StringBuilder(128);
//        target.append(this.urlPathHelper.getOriginatingRequestUri(request));
//
//        if (request.getQueryString() != null) {
//            target.append("?");
//            target.append(request.getQueryString());
//        }
//
//        response.sendRedirect(Urls.FI.vetumaLogin(target.toString()));
        propagateException(e);
    }

    private void checkInvalidParameters(HttpServletRequest request) {
        if (IS_POST(request) && !Strings.isNullOrEmpty(request.getParameter(UNWANTED_HIDDEN_EMAIL_FIELD))) {
            throw new IllegalArgumentException("Unwanted information given.");
        }
    }

    private static boolean IS_POST(HttpServletRequest request) {
        return request.getMethod().equals("POST");
    }

    private void verifyOrInitializeCsrfToken(HttpServletRequest request, HttpServletResponse response) {

        if (IS_GET(request)) {
            try {
                verifyAndGetCurrentCSRFToken(request);
            } catch (CSRFException e) {
                request.getSession().invalidate();
                request.setAttribute(COOKIE_ERROR, true);
                request.setAttribute(CSRF_TOKEN_NAME, initializeCSRFToken(request, response));
            }
        }
        else if (IS_POST(request) && !isSkipCsrfURI(request.getRequestURI())) {
            request.setAttribute(CSRF_TOKEN_NAME, verifyAndGetCurrentCSRFToken(request));
        }

    }

    private static boolean IS_GET(HttpServletRequest request) {
        return request.getMethod().equals("GET");
    }

    private String initializeCSRFToken(HttpServletRequest request, HttpServletResponse response) {
        String csrfToken;
        csrfToken = encryptionService.randomToken(CSRF_TOKEN_LENGTH);
        setCookie(CSRF_TOKEN_NAME, csrfToken, request, response);
        getExistingSession(request).setAttribute(CSRF_TOKEN_NAME, csrfToken);
        return csrfToken;
    }


    public static String verifyAndGetCurrentCSRFToken(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, CSRF_TOKEN_NAME);

        if (cookie == null) {
            throw new CSRFException("CSRF cookie missing");
        }

        String cookieToken = cookie.getValue();
        String sessionToken = (String) getExistingSession(request).getAttribute(CSRF_TOKEN_NAME);

        // Just to be sure no one has hijacked our session
        if (cookieToken == null) {
            throw new CSRFException("CSRF session token missing ");
        } else if (!cookieToken.equals(sessionToken)) {
            throw new CSRFException("CSRF session doesn't match cookie");
        }

        // Double Submit Cookie
        if (IS_POST(request)) {
            String requestToken = request.getParameter(CSRF_TOKEN_NAME);
            if (requestToken == null || !requestToken.equals(sessionToken)) {
                System.out.println(cookieToken + " = " + sessionToken);
                System.out.println(cookieToken.length() + "=" + sessionToken.length());
                throw new CSRFException("CSRFToken -request parameter missing or doesn't match session");
            }
        }
        return sessionToken;
    }

    private static final String[] skipCsrfURIs = new String[] {
            // CSRF-check is skipped when returning from vetuma
            Urls.VETUMA_FI,
            Urls.VETUMA_SV,

            // Regular CSRF-check is skipped when adding attachments
            // @see fi.om.municipalityinitiative.web.controller.InitiativeManagementController#addAttachment
            Urls.MANAGE_ATTACHMENTS_FI,
            Urls.MANAGE_ATTACHMENTS_SV,

            Urls.MUNICIPALITY_DECISION_FI_BASE_URL,
            Urls.MUNICIPALITY_DECISION_SV_BASE_URL,

            Urls.EDIT_MUNICIPALITY_DECISION_ATTACHMENTS_FI,
            Urls.EDIT_MUNICIPALITY_DECISION_ATTACHMENTS_SV


    };

    private static boolean isSkipCsrfURI(String requestURI) {

        for (String skipCsrfURI : skipCsrfURIs) {
            if (requestURI.startsWith(skipCsrfURI)) {
                return true;
            }
        }
        return false;
    }

    private static HttpSession getExistingSession(HttpServletRequest request) {
        HttpSession session = getOptionalSession(request);
        if (session == null) {
            throw new CookiesRequiredException();
        } else {
            return session;
        }
    }

    private @Nullable
    static HttpSession getOptionalSession(HttpServletRequest request) {
        return request.getSession(true);
    }

    private void csrfException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        propagateException(e);
    }

    private void setCookie(String name, String value, HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);

        String contextPath = request.getContextPath();
        if (Strings.isNullOrEmpty(contextPath)) {
            contextPath = "/";
        }
        cookie.setPath(contextPath);
        cookie.setSecure(request.isSecure());


        response.setHeader("Set-Cookie", name + "=" + value + "; Path=" + contextPath + (disableSecureCookie ? "" : "Secure; ")+ "HttpOnly");

        if (cookie.getSecure()) {
            // For enabling httpOnly we need to write the raw cookie data instead of response.addCookie(cookie)
            response.setHeader("SET-COOKIE", name + "=" + value + "; Path=" + contextPath + "; Secure; HttpOnly");
        }
        else {
            response.setHeader("SET-COOKIE", name + "=" + value + "; Path=" + contextPath + "; HttpOnly");
        }
    }

    private void propagateException(Exception e) throws IOException, ServletException {
        Throwables.propagateIfInstanceOf(e, IOException.class);
        Throwables.propagateIfInstanceOf(e, ServletException.class);
        Throwables.propagate(e);
    }

    @Override
    public void destroy() {
    }

}
