package com.adobe.learning.core.servlets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.adobe.learning.core.config.AlmEnvConfig;
import com.adobe.learning.core.utils.CourseLaunchUtils;
import com.fasterxml.jackson.databind.JsonNode;

@ExtendWith(MockitoExtension.class)
class LOResponseValidationServletTest {

    @InjectMocks
    private LOResponseValidationServlet loResponseValidationServlet;

    @Mock
    private AlmEnvConfig almEnvConfig;

    @Mock
    private SlingHttpServletRequest request;

    @Mock
    private SlingHttpServletResponse response;
    
    @Mock
    private PrintWriter printWriter;

    @Test
    void testDoPost_WithTrainingAccount() throws ServletException, IOException {
        String JSON_INPUT = "{ \"accountId\": \"10768\", \"almBaseURL\": \"http://localhost\", \"loId\": \"lo123\", \"userData\": {} }";
        StringReader inputString = new StringReader(JSON_INPUT);
        BufferedReader reader = new BufferedReader(inputString);
        when(request.getReader()).thenReturn(reader);
        when(response.getWriter()).thenReturn(printWriter);

        Map<String, String> userMap = new HashMap<>();
        Map<String, String> accountCredentialMap = new HashMap<>();
        String almAdminAccessToken = "adminToken";
        String loResponse = "response";
        String filteredLoResponse = "filteredResponse";

        try (MockedStatic<CourseLaunchUtils> mockedUtils = mockStatic(CourseLaunchUtils.class)) {
            mockedUtils.when(() -> CourseLaunchUtils.getUserDetails(any(JsonNode.class))).thenReturn(userMap);
            mockedUtils.when(() -> CourseLaunchUtils.accountCredential(any(String.class), any(AlmEnvConfig.class))).thenReturn(accountCredentialMap);
            mockedUtils.when(() -> CourseLaunchUtils.getAlmAdminAccessToken(any(Map.class), any(String.class))).thenReturn(almAdminAccessToken);
            mockedUtils.when(() -> CourseLaunchUtils.updateUserType(any(Map.class), any(String.class), any(AlmEnvConfig.class), any(String.class))).thenReturn("updatedUser");
            mockedUtils.when(() -> CourseLaunchUtils.getLOResponse(any(String.class), any(String.class), any(String.class), any(Boolean.class))).thenReturn(loResponse);
            mockedUtils.when(() -> CourseLaunchUtils.filterLOResponseAsAdmin(any(String.class))).thenReturn(filteredLoResponse);

            loResponseValidationServlet.doPost(request, response);

            verify(response).setStatus(200);
            verify(printWriter).write("{\"updatedUserType\":\"updatedUser\",\"loResponseAsAdmin\":\"filteredResponse\"}");
        }
    }

    @Test
    void testDoPost_NonTrainingAccount() throws ServletException, IOException {
        String jsonInput = "{ \"accountId\": \"99999\", \"almBaseURL\": \"http://localhost\", \"loId\": \"lo123\", \"userData\": {} }";
        StringReader inputString = new StringReader(jsonInput);
        BufferedReader reader = new BufferedReader(inputString);
        when(request.getReader()).thenReturn(reader);
        when(response.getWriter()).thenReturn(printWriter);

        Map<String, String> userMap = new HashMap<>();
        Map<String, String> accountCredentialMap = new HashMap<>();
        String almAdminAccessToken = "adminToken";
        String loResponse = "response";
        String filteredLoResponse = "filteredResponse";

        try (MockedStatic<CourseLaunchUtils> mockedUtils = mockStatic(CourseLaunchUtils.class)) {
            mockedUtils.when(() -> CourseLaunchUtils.getUserDetails(any(JsonNode.class))).thenReturn(userMap);
            mockedUtils.when(() -> CourseLaunchUtils.accountCredential(any(String.class), any(AlmEnvConfig.class))).thenReturn(accountCredentialMap);
            mockedUtils.when(() -> CourseLaunchUtils.getAlmAdminAccessToken(any(Map.class), any(String.class))).thenReturn(almAdminAccessToken);
            mockedUtils.when(() -> CourseLaunchUtils.getLOResponse(any(String.class), any(String.class), any(String.class), any(Boolean.class))).thenReturn(loResponse);
            mockedUtils.when(() -> CourseLaunchUtils.filterLOResponseAsAdmin(any(String.class))).thenReturn(filteredLoResponse);

            loResponseValidationServlet.doPost(request, response);

            verify(response).setStatus(HttpServletResponse.SC_OK);
            verify(printWriter).write("{\"loResponseAsAdmin\":\"filteredResponse\"}");
        }
    }

    @Test
    void testDoPost_InvalidResponse() throws ServletException, IOException {
        String jsonInput = "{ \"accountId\": \"10768\", \"almBaseURL\": \"http://localhost\", \"loId\": \"lo123\", \"userData\": {} }";
        StringReader inputString = new StringReader(jsonInput);
        BufferedReader reader = new BufferedReader(inputString);
        when(request.getReader()).thenReturn(reader);

        Map<String, String> userMap = new HashMap<>();
        Map<String, String> accountCredentialMap = new HashMap<>();
        String almAdminAccessToken = "adminToken";

        try (MockedStatic<CourseLaunchUtils> mockedUtils = mockStatic(CourseLaunchUtils.class)) {
            mockedUtils.when(() -> CourseLaunchUtils.getUserDetails(any(JsonNode.class))).thenReturn(userMap);
            mockedUtils.when(() -> CourseLaunchUtils.accountCredential(any(String.class), any(AlmEnvConfig.class))).thenReturn(accountCredentialMap);
            mockedUtils.when(() -> CourseLaunchUtils.getAlmAdminAccessToken(any(Map.class), any(String.class))).thenReturn(almAdminAccessToken);
            mockedUtils.when(() -> CourseLaunchUtils.getLOResponse(any(String.class), any(String.class), any(String.class), any(Boolean.class))).thenReturn("");

            loResponseValidationServlet.doPost(request, response);

            verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}

