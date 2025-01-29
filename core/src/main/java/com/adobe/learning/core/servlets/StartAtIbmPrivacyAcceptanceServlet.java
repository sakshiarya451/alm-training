package com.adobe.learning.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.adobe.learning.core.config.AlmEnvConfig;
import com.adobe.learning.core.constants.CommonConstants;
import com.adobe.learning.core.utils.HttpConnectionUtils;

@Component(service = Servlet.class, immediate = true, property = {
        "sling.servlet.paths=/bin/startatibm/privacyacceptance",
        "sling.servlet.methods=GET" })
public class StartAtIbmPrivacyAcceptanceServlet extends SlingAllMethodsServlet {

    @Reference
    private transient AlmEnvConfig almEnvConfig;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getRequestParameter("email").getString();
        String token = request.getRequestParameter("token").getString();
        if (StringUtils.isAnyEmpty(email, token)) {
            response.setStatus(400);
            response.getWriter().write("Empty token or email");
            return;
        }

        String url = almEnvConfig.getPrivacyAcceptanceApi() + "?email=" + email;
        Map<String, String> requestProperties = new HashMap<>();
        requestProperties.put(CommonConstants.ACCEPT, CommonConstants.APPLICATION_JSON);
        requestProperties.put(CommonConstants.AUTHORIZATION, CommonConstants.BEARER + " " + token);

        String responseString = HttpConnectionUtils.getResponseFromHttpRequest(url, CommonConstants.GET_METHOD,
                requestProperties, null);
        if (StringUtils.isNotEmpty(responseString)) {
            response.setStatus(200);
            response.setContentType(CommonConstants.APPLICATION_JSON);
            response.getWriter().write(responseString);
        } else {
            response.setStatus(500);
            response.getWriter().write("Failed to get the privacy status");
        }
    }

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        String token = request.getRequestParameter("token").getString();
        if (StringUtils.isEmpty(token)) {
            response.setStatus(400);
            response.getWriter().write("Empty token.");
            return;
        }

        Map<String, String> requestProperties = new HashMap<>();
        requestProperties.put(CommonConstants.ACCEPT, CommonConstants.APPLICATION_JSON);
        requestProperties.put(CommonConstants.CONTENT_TYPE, CommonConstants.APPLICATION_JSON);
        requestProperties.put(CommonConstants.AUTHORIZATION, CommonConstants.BEARER + " " + token);

        String body = IOUtils.toString(request.getReader());
        String responseString = HttpConnectionUtils.getResponseFromHttpRequest(almEnvConfig.getPrivacyAcceptanceApi(),
                CommonConstants.POST_METHOD, requestProperties, body);
        if (StringUtils.isNotEmpty(responseString)) {
            response.setStatus(200);
            response.setContentType(CommonConstants.APPLICATION_JSON);
            response.getWriter().write(responseString);
        } else {
            response.setStatus(500);
            response.getWriter().write("Failed to set the privacy status. Please check the logs.");
        }
    }
}