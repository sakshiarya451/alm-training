package com.adobe.learning.core.servlets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.adobe.learning.core.config.AlmEnvConfig;
import com.adobe.learning.core.utils.HttpConnectionUtils;

@ExtendWith({ MockitoExtension.class })
class StartAtIbmTokenServletTest {

    @InjectMocks
    private StartAtIbmTokenServlet servlet;

    @Mock
    private AlmEnvConfig almEnvConfig;

    @Mock
    private SlingHttpServletRequest request;

    @Mock
    private SlingHttpServletResponse response;

    @Mock
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testDoGet_whenResponseIsNotEmpty() throws ServletException, IOException {
        when(almEnvConfig.getPrivacyAcceptanceFunctionalId()).thenReturn("functionalId");
        when(almEnvConfig.getPrivacyAcceptancePrivateKey()).thenReturn("privateKey");
        when(almEnvConfig.getPrivacyAcceptanceTokenApi()).thenReturn("http://api.example.com/token");
        when(response.getWriter()).thenReturn(printWriter);

        try (MockedStatic<HttpConnectionUtils> mockedStatic = mockStatic(HttpConnectionUtils.class)) {
            mockedStatic.when(() -> HttpConnectionUtils.getResponseFromHttpRequest(any(), any(), any(), any()))
                    .thenReturn("{\"token\":\"12345\"}");
            when(response.getStatus()).thenReturn(200);
            servlet.doGet(request, response);

            assertEquals(200, response.getStatus());
        }
    }

    @Test
    void testDoGet_whenResponseIsEmpty() throws ServletException, IOException {
        when(almEnvConfig.getPrivacyAcceptanceFunctionalId()).thenReturn("functionalId");
        when(almEnvConfig.getPrivacyAcceptancePrivateKey()).thenReturn("privateKey");
        when(almEnvConfig.getPrivacyAcceptanceTokenApi()).thenReturn("http://api.example.com/token");
        when(response.getWriter()).thenReturn(printWriter);

        try (MockedStatic<HttpConnectionUtils> mockedStatic = mockStatic(HttpConnectionUtils.class)) {
            mockedStatic.when(() -> HttpConnectionUtils.getResponseFromHttpRequest(any(), any(), any(), any()))
                    .thenReturn("");
            when(response.getStatus()).thenReturn(500);
            servlet.doGet(request, response);

            assertEquals(500, response.getStatus());
        }
    }
}
