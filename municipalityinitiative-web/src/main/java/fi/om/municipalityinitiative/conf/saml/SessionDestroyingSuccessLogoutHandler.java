package fi.om.municipalityinitiative.conf.saml;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class SessionDestroyingSuccessLogoutHandler implements LogoutSuccessHandler {

    private String baseUri;

    public SessionDestroyingSuccessLogoutHandler(String baseUri) {
        this.baseUri = baseUri;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        if (response.isCommitted()) {
            return;
        }

        String targetUri = TargetStoringFilter.popTarget(request);
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        new DefaultRedirectStrategy()
                .sendRedirect(request, response, baseUri + targetUri);
    }
}
