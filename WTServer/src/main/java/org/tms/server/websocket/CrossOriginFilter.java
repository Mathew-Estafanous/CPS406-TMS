package org.tms.server.websocket;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CrossOriginFilter implements Filter {

    public static final String ALLOWED_ORIGINS_PARAM = "allowedOrigins";
    public static final String ALLOWED_METHODS_PARAM = "allowedMethods";
    public static final String ALLOWED_HEADERS_PARAM = "allowedHeaders";
    public static final String ALLOWED_CREDENTIALS_PARAM = "allowedCredentials";

    private String allowedOrigins = "*";
    private String allowedMethods = "GET,POST,HEAD,OPTIONS";
    private String allowedHeaders = "X-Requested-With,Content-Type,Accept,Origin";
    private String allowedCredentials = "true";


    @Override
    public void init(FilterConfig filterConfig) {
        String allowedOriginsParam = filterConfig.getInitParameter(ALLOWED_ORIGINS_PARAM);
        if (allowedOriginsParam != null) {
            allowedOrigins = allowedOriginsParam;
        }

        String allowedMethodsParam = filterConfig.getInitParameter(ALLOWED_METHODS_PARAM);
        if (allowedMethodsParam != null) {
            allowedMethods = allowedMethodsParam;
        }

        String allowedHeadersParam = filterConfig.getInitParameter(ALLOWED_HEADERS_PARAM);
        if (allowedHeadersParam != null) {
            allowedHeaders = allowedHeadersParam;
        }
        String allowedCredentialsParam = filterConfig.getInitParameter(ALLOWED_CREDENTIALS_PARAM);
        if (allowedCredentialsParam != null) {
            allowedCredentials = allowedCredentialsParam;
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-Control-Allow-Origin", allowedOrigins);
        httpServletResponse.setHeader("Access-Control-Allow-Methods", allowedMethods);
        httpServletResponse.setHeader("Access-Control-Allow-Headers", allowedHeaders);
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", allowedCredentials);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
