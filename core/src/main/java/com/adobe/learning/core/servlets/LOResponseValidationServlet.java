package com.adobe.learning.core.servlets;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.adobe.learning.core.config.AlmEnvConfig;
import com.adobe.learning.core.utils.CourseLaunchUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@Component(service = Servlet.class, immediate = true, property = {
        "sling.servlet.paths=/bin/alm/validateLOResponse",
        "sling.servlet.methods=POST", })

public class LOResponseValidationServlet extends SlingAllMethodsServlet {

    @Reference
    private transient AlmEnvConfig almEnvConfig;

    private static final List<String> TRAINING_ACCOUNTS = Arrays.asList("10768", "10814", "2136");

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(IOUtils.toString(request.getReader()));
        
        String accountId = jsonNode.path("accountId").asText();
        String almBaseUrl = jsonNode.path("almBaseURL").asText();
        String loId = jsonNode.path("loId").asText();
        Map<String, String> userMap = CourseLaunchUtils.getUserDetails(jsonNode.path("userData"));
        
        Map<String, String> accountCredentailMap = CourseLaunchUtils.accountCredential(accountId, almEnvConfig);
        String almAdminAccessToken = CourseLaunchUtils.getAlmAdminAccessToken(accountCredentailMap,
                jsonNode.path("almBaseURL").asText());
        
        Map<String, String> responseData = new HashMap<>();

        if (TRAINING_ACCOUNTS.contains(accountId)) {
            String updatedUserType = CourseLaunchUtils.updateUserType(userMap, almAdminAccessToken, almEnvConfig, almBaseUrl);
            responseData.put("updatedUserType", updatedUserType);
        }
        
        String loResponseAsAdmin = CourseLaunchUtils.getLOResponse(loId, almAdminAccessToken, almBaseUrl, false);
        String filteredLoResponseAsAdmin = CourseLaunchUtils.filterLOResponseAsAdmin(loResponseAsAdmin);

        responseData.put("loResponseAsAdmin", filteredLoResponseAsAdmin);
        JsonNode jsonNodeResponse = objectMapper.valueToTree(responseData);
        
        /* Set response */
        if (StringUtils.isNotEmpty(loResponseAsAdmin)) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(jsonNodeResponse.toString());
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

    }

}
