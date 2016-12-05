package fi.om.municipalityinitiative.conf.saml;

import fi.om.municipalityinitiative.web.Urls;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;


class TargetStoringFilter implements Filter {

    private static String TARGET_COOKIE_NAME = "logoutTarget";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        String[] targets = request.getParameterMap().get("target");
        if (targets != null) {
            Arrays.stream(targets)
                    .findFirst()
                    .ifPresent(target -> ((HttpServletResponse) response).addCookie(targetCookie(target)));
        }

        chain.doFilter(request, response);

    }

    private static Cookie targetCookie(String target) {
        Cookie cookie = new Cookie(TARGET_COOKIE_NAME, target);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(300);
        return cookie;
    }

    private static Cookie deleteCookie() {
        Cookie cookie = targetCookie(null);
        cookie.setMaxAge(0);
        return cookie;
    }

    public static String popTarget(HttpServletRequest request, HttpServletResponse response) {
        Optional<String> optionalTarget = optionalTarget(request);
        optionalTarget.ifPresent((__) -> response.addCookie(deleteCookie()));
        return optionalTarget.orElse(Urls.FRONT_FI);
    }

    @Override
    public void destroy() {

    }

    public static String peekTarget(HttpServletRequest request) {
        return optionalTarget(request)
                .orElse(Urls.FRONT_FI);
    }

    private static Optional<String> optionalTarget(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(a -> a.getName().equals(TARGET_COOKIE_NAME))
                .findFirst()
                .map(Cookie::getValue);
    }
}
