package com.adobe.learning.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.learning.core.config.AlmEnvConfig;
import com.adobe.learning.core.constants.CommonConstants;
import com.adobe.learning.core.utils.HttpConnectionUtils;

@Component(service = Servlet.class, immediate = true, property = {
        "sling.servlet.paths=/bin/startatibm/gettoken",
        "sling.servlet.methods=GET" })
public class StartAtIbmTokenServlet extends SlingSafeMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(StartAtIbmTokenServlet.class);

    @Reference
    private transient AlmEnvConfig almEnvConfig;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        Map<String, String> requestProperties = new HashMap<>();
        requestProperties.put(CommonConstants.ACCEPT, CommonConstants.APPLICATION_JSON);
        requestProperties.put(CommonConstants.CONTENT_TYPE, CommonConstants.APPLICATION_JSON);

        String body = "{\"functionalId\":\"" + almEnvConfig.getPrivacyAcceptanceFunctionalId() + "\",\"privateKey\":\""
                + almEnvConfig.getPrivacyAcceptancePrivateKey() + "\",\"expiration\":\"86400\"}";

        String responseString = HttpConnectionUtils.getResponseFromHttpRequest(
                almEnvConfig.getPrivacyAcceptanceTokenApi(), CommonConstants.POST_METHOD, requestProperties, body);
        if (StringUtils.isNotEmpty(responseString)) {
            response.setStatus(200);
            response.setContentType(CommonConstants.APPLICATION_JSON);
            response.getWriter().write(responseString);
        } else {
            response.setStatus(500);
            response.getWriter().write("Failed to get the token. Please check logs.");
        }
    }
}