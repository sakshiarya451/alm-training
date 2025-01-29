package com.adobe.learning.core.servlets;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.Cookie;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestPathInfo;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.adobe.learning.core.utils.Constants;
import com.google.gson.JsonObject;

@ExtendWith({ MockitoExtension.class })
class AlmCommerceLoginServletTest {

    @InjectMocks
    private AlmCommerceLoginServlet almCommerceLoginServlet;

    @Mock
    private SlingHttpServletRequest request;

    @Mock
    private SlingHttpServletResponse response;

    @Mock
    private ResourceResolver resourceResolver;

    @Mock
    private Resource resource;

    @Mock
    private RequestPathInfo requestPathInfo;

    @Mock
    private PrintWriter printWriter;

    private StringWriter stringWriter;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(printWriter);
        doAnswer(invocation -> {
            stringWriter.write(invocation.getArgument(0, String.class));
            return null;
        }).when(printWriter).write(anyString());
    }

    @Test
    void testDoPostWithValidCookie() throws Exception {
        Cookie cookie = new Cookie(Constants.Config.CUSTOMER_TOKEN_COOKIE_NAME, "validToken");
        when(request.getCookie(Constants.Config.CUSTOMER_TOKEN_COOKIE_NAME)).thenReturn(cookie);
        
        almCommerceLoginServlet.doPost(request, response);
        
        verify(response).setStatus(SlingHttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        JsonObject expectedJson = new JsonObject();
        expectedJson.addProperty("access_token", "validToken");
        verify(response.getWriter()).write(expectedJson.toString());
    }

    // @Test
    // void testDoPostWithInvalidCookie() throws IOException {
    //     when(request.getCookie(Constants.Config.CUSTOMER_TOKEN_COOKIE_NAME)).thenReturn(null);
        
    //     almCommerceLoginServlet.doPost(request, response);
        
    //     verify(response).setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    // }

    // @Test
    // void testDoPostWithException() throws Exception {
    //     when(request.getCookie(Constants.Config.CUSTOMER_TOKEN_COOKIE_NAME)).thenThrow(new RuntimeException("Exception"));
        
    //     almCommerceLoginServlet.doPost(request, response);
        
    //     verify(response).setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    //     verify(response, never()).setContentType(anyString());
    //     verify(response.getWriter(), never()).write(anyString());
    // }
}

