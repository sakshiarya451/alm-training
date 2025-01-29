package com.adobe.learning.core.servlets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.adobe.learning.core.config.AlmEnvConfig;
import com.adobe.learning.core.constants.CommonConstants;
import com.adobe.learning.core.utils.HttpConnectionUtils;

@ExtendWith(MockitoExtension.class)
class StartAtIbmPrivacyAcceptanceServletTest {

    @InjectMocks
    private StartAtIbmPrivacyAcceptanceServlet servlet;

    @Mock
    private AlmEnvConfig almEnvConfig;

    @Mock
    private SlingHttpServletRequest request;

    @Mock
    private SlingHttpServletResponse response;

    @Mock
    private RequestParameter requestParameter;

    @Mock
    private PrintWriter printWriter;

    @Mock
    private ResourceResolver resourceResolver;

    @Mock
    private Resource resource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testDoGet_Success() throws ServletException, IOException {
        when(request.getRequestParameter("email")).thenReturn(requestParameter);
        when(requestParameter.getString()).thenReturn("test@example.com");
        when(request.getRequestParameter("token")).thenReturn(requestParameter);
        when(requestParameter.getString()).thenReturn("valid-token");
        when(almEnvConfig.getPrivacyAcceptanceApi()).thenReturn("http://example.com/api"); // INPUT_REQUIRED {set the
                                                                                           // correct API URL}
        when(response.getWriter()).thenReturn(printWriter);

        Map<String, String> requestProperties = new HashMap<>();
        requestProperties.put(CommonConstants.ACCEPT, CommonConstants.APPLICATION_JSON);
        requestProperties.put(CommonConstants.AUTHORIZATION, CommonConstants.BEARER + " valid-token");

        try (MockedStatic<HttpConnectionUtils> mockedStatic = mockStatic(HttpConnectionUtils.class)) {
            mockedStatic
                    .when(() -> HttpConnectionUtils.getResponseFromHttpRequest(any(String.class), any(String.class),
                            any(Map.class), any(String.class)))
                    .thenReturn("{\"status\":\"accepted\"}");
            when(response.getStatus()).thenReturn(200);
            servlet.doGet(request, response);
        }

        assertEquals(200, response.getStatus());
    }

    @Test
    void testDoGet_Failure() throws ServletException, IOException {
        when(request.getRequestParameter("email")).thenReturn(requestParameter);
        when(requestParameter.getString()).thenReturn("test@example.com");
        when(request.getRequestParameter("token")).thenReturn(requestParameter);
        when(requestParameter.getString()).thenReturn("valid-token");
        when(almEnvConfig.getPrivacyAcceptanceApi()).thenReturn("http://example.com/api"); // INPUT_REQUIRED {set the
                                                                                           // correct API URL}
        when(response.getWriter()).thenReturn(printWriter);

        try (MockedStatic<HttpConnectionUtils> mockedStatic = mockStatic(HttpConnectionUtils.class)) {
            mockedStatic
                    .when(() -> HttpConnectionUtils.getResponseFromHttpRequest(any(String.class), any(String.class),
                            any(Map.class), any(String.class)))
                    .thenReturn(null);
            when(response.getStatus()).thenReturn(500);
            servlet.doGet(request, response);
        }

        assertEquals(500, response.getStatus());
    }

    @Test
    void testDoPost_Success() throws ServletException, IOException {
        when(request.getRequestParameter("token")).thenReturn(requestParameter);
        when(requestParameter.getString()).thenReturn("valid-token");
        when(almEnvConfig.getPrivacyAcceptanceApi()).thenReturn("http://example.com/api"); // INPUT_REQUIRED {set the
                                                                                           // correct API URL}
        StringReader inputString = new StringReader("");
        BufferedReader reader = new BufferedReader(inputString);
        when(request.getReader()).thenReturn(reader);
        when(response.getWriter()).thenReturn(printWriter);

        Map<String, String> requestProperties = new HashMap<>();
        requestProperties.put(CommonConstants.ACCEPT, CommonConstants.APPLICATION_JSON);
        requestProperties.put(CommonConstants.CONTENT_TYPE, CommonConstants.APPLICATION_JSON);
        requestProperties.put(CommonConstants.AUTHORIZATION, CommonConstants.BEARER + " valid-token");

        try (MockedStatic<HttpConnectionUtils> mockedStatic = mockStatic(HttpConnectionUtils.class)) {
            mockedStatic
                    .when(() -> HttpConnectionUtils.getResponseFromHttpRequest(any(String.class), any(String.class),
                            any(Map.class), any(String.class)))
                    .thenReturn("{\"status\":\"accepted\"}");
            when(response.getStatus()).thenReturn(200);
            servlet.doPost(request, response);
        }

        assertEquals(200, response.getStatus());
    }

    @Test
    void testDoPost_Failure() throws ServletException, IOException {
        when(request.getRequestParameter("token")).thenReturn(requestParameter);
        when(requestParameter.getString()).thenReturn("valid-token");
        when(almEnvConfig.getPrivacyAcceptanceApi()).thenReturn("http://example.com/api"); // INPUT_REQUIRED {set the
                                                                                           // correct API URL}
        StringReader inputString = new StringReader("");
        BufferedReader reader = new BufferedReader(inputString);
        when(request.getReader()).thenReturn(reader);
        when(response.getWriter()).thenReturn(printWriter);

        try (MockedStatic<HttpConnectionUtils> mockedStatic = mockStatic(HttpConnectionUtils.class)) {
            mockedStatic
                    .when(() -> HttpConnectionUtils.getResponseFromHttpRequest(any(String.class), any(String.class),
                            any(Map.class), any(String.class)))
                    .thenReturn(null);
            when(response.getStatus()).thenReturn(500);
            servlet.doPost(request, response);
        }

        assertEquals(500, response.getStatus());
    }
}
