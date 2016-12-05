package fi.om.municipalityinitiative.conf.saml;

import fi.om.municipalityinitiative.service.EncryptionService;
import fi.om.municipalityinitiative.service.UserService;
import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.util.SsnValidator;
import fi.om.municipalityinitiative.web.Urls;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class SessionStoringAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private EncryptionService encryptionService;

    private String baseUri;

    public SessionStoringAuthenticationSuccessHandler(String baseUri) {
        this.baseUri = baseUri;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SamlUser user = (SamlUser) authentication.getPrincipal();

        if (SsnValidator.isAdult(LocalDate.now(), user.getSsn())) {
            userService.login(
                    encryptionService.registeredUserHash(user.getSsn()),
                    user.getFullName(), user.getAddress(), user.getMunicipality(), request
            );
            new DefaultRedirectStrategy()
                    .sendRedirect(request, response, baseUri + TargetStoringFilter.popTarget(request, response));
        } else {
            new DefaultRedirectStrategy()
                    .sendRedirect(request, response, Urls.get(Locales.LOCALE_FI).notAdultError());
        }

    }
}
