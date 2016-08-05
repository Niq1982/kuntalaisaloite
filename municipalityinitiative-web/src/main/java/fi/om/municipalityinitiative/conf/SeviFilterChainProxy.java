package fi.om.municipalityinitiative.conf;

import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.List;

public class SeviFilterChainProxy extends FilterChainProxy {

    public SeviFilterChainProxy() {
        super();
    }

    public SeviFilterChainProxy(SecurityFilterChain chain) {
        super(chain);
    }

    public SeviFilterChainProxy(List<SecurityFilterChain> filterChains) {
        super(filterChains);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String redirect_uri = request.getParameter("redirect_uri");
        super.doFilter(request, response, chain);
    }

}
