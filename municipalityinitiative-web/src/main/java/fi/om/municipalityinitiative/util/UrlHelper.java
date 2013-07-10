package fi.om.municipalityinitiative.util;

import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;

public class UrlHelper extends UrlPathHelper {

    public String getOriginalRequestUriWithQueryString(HttpServletRequest request) {
        StringBuilder target = new StringBuilder(128);
        target.append(getOriginatingRequestUri(request));

        if (request.getQueryString() != null) {
            target.append("?");
            target.append(request.getQueryString());
        }

        return target.toString();
    }
}
