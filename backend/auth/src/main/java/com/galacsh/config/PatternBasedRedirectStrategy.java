package com.galacsh.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.util.UrlUtils;

import java.io.IOException;

public class PatternBasedRedirectStrategy implements RedirectStrategy {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
        String redirectUrl = calculateRedirectUrl(request.getContextPath(), url);
        redirectUrl = response.encodeRedirectURL(redirectUrl);

        log.debug("Redirecting to {}", redirectUrl);
        response.sendRedirect(redirectUrl);
    }

    private String calculateRedirectUrl(String contextPath, String url) {
        url = (url != null) ? url : "/";

        if (UrlUtils.isAbsoluteUrl(url)) {
            return url;
        } else if (url.startsWith("/")) {
            return url;
        } else {
            return contextPath + "/" + url;
        }
    }
}
