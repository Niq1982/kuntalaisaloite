package fi.om.municipalityinitiative.web;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import fi.om.municipalityinitiative.service.AuthenticationRequiredException;
import fi.om.municipalityinitiative.service.CookiesRequiredException;
import fi.om.municipalityinitiative.service.EncryptionService;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.NestedServletException;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.WebUtils;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

import java.io.IOException;

public class SecurityFilter implements Filter {

    private static final String CSRF_TOKEN_NAME = "CSRFToken";
    private static final int CSRF_TOKEN_LENGTH = 24;
    private static final String COOKIE_ERROR = "cookieError";
    public static final String UNWANTED_HIDDEN_EMAIL_FIELD = "email";
    private UrlPathHelper urlPathHelper = new UrlPathHelper();

    @Resource
    private EncryptionService encryptionService;
    
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
            } else if (t instanceof CSRFException) {
                csrfException(e, request, response);
            } else {
                propagateException(e);
            }
        }
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
//        response.sendRedirect(Urls.FI.login(target.toString()));
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

        String csrfToken;

        if (IS_GET(request)) {
            try {
                csrfToken = verifyAndGetCurrentCSRFToken(request);
            } catch (CSRFException e) {
                request.setAttribute(COOKIE_ERROR, true);
                csrfToken = initializeCSRFToken(request, response);
            }
        }
        else if (IS_POST(request)) {
            csrfToken = verifyAndGetCurrentCSRFToken(request);
        }
        else {
            throw new RuntimeException("Invalid method: " + request.getMethod());
        }

        request.setAttribute(CSRF_TOKEN_NAME, csrfToken);
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


    private String verifyAndGetCurrentCSRFToken(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, CSRF_TOKEN_NAME);

        if (cookie == null) {
            throw new CSRFException("CSRF cookie missing");
        }

        String cookieToken = cookie.getValue();
        String sessionToken = (String) getExistingSession(request).getAttribute(CSRF_TOKEN_NAME);

        // Just to be sure no one has hijacked our session
        if (cookieToken == null || !cookieToken.equals(sessionToken)) {
            throw new CSRFException("CSRF session token missing or doesn't match cookie");
        }

        // Double Submit Cookie
        if (IS_POST(request)) {
            String requestToken = request.getParameter(CSRF_TOKEN_NAME);
            if (requestToken == null || !requestToken.equals(sessionToken)) {
                throw new CSRFException("CSRFToken -request parameter missing or doesn't match session");
            }
        }
        return sessionToken;
    }

    private @NotNull HttpSession getExistingSession(HttpServletRequest request) {
        HttpSession session = getOptionalSession(request);
        if (session == null) {
            throw new CookiesRequiredException();
        } else {
            return session;
        }
    }

    private @Nullable
    HttpSession getOptionalSession(HttpServletRequest request) {
        return getRequest().getSession(true);
    }

    private HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }

    private void csrfException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        propagateException(e);
    }

    private void setCookie(String name, String value, HttpServletRequest request, HttpServletResponse response) {
        // httpOnly would be ideal, but it requires either writing raw cookie header or using WAS specific Cookies
        Cookie cookie = new Cookie(name, value);

        String contextPath = request.getContextPath();
        if (Strings.isNullOrEmpty(contextPath)) {
            contextPath = "/";
        }
        cookie.setPath(contextPath);
        cookie.setSecure(true);
        response.addCookie(cookie);
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
