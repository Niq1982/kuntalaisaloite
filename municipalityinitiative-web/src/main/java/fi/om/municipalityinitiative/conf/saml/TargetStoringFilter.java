package fi.om.municipalityinitiative.conf.saml;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;


class TargetStoringFilter implements Filter {

    public static String SESSION_PARAM_NAME = TargetStoringFilter.class.getName() + ".target";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String[] targets = request.getParameterMap().get("target");
        if (targets != null && targets.length > 0) {
            String target = targets[0];
            storeTarget((HttpServletRequest) request, target);
        }

        chain.doFilter(request, response);

    }

    private static void storeTarget(HttpServletRequest request, String target) {
        request.getSession(false).setAttribute(SESSION_PARAM_NAME, target);
    }

    public static String popTarget(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object attribute = session.getAttribute(SESSION_PARAM_NAME);

        if (attribute == null) {
            return "";
        }
        else {
            session.removeAttribute(SESSION_PARAM_NAME);
            return (String) attribute;
        }
    }

    @Override
    public void destroy() {

    }
}
