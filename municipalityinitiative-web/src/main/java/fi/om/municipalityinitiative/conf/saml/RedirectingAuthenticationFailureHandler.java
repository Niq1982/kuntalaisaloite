package fi.om.municipalityinitiative.conf.saml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RedirectingAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final Logger log = LoggerFactory.getLogger(RedirectingAuthenticationFailureHandler.class);

    private String baseUrl;

    public RedirectingAuthenticationFailureHandler(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        // IDP is not currently able to separate cancelled and failed login's.
        // Let's trust that IDP shows error message to user in error cases
        // so we'll just redirect the user back to previous page whether the login was cancelled by the user or failed for some other reason.

        log.warn("Login failed / cancelled", exception);

        String targetUri = TargetStoringFilter.popTarget(request, response);

        new DefaultRedirectStrategy()
                .sendRedirect(request, response, baseUrl + targetUri);

    }
}
