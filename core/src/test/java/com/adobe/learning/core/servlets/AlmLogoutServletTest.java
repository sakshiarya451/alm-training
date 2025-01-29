package com.adobe.learning.core.servlets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import javax.servlet.http.Cookie;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AlmLogoutServletTest {

    @InjectMocks
    private AlmLogoutServlet almLogoutServlet;

    @Mock
    private SlingHttpServletRequest request;

    @Mock
    private SlingHttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testDoPostSuccess() throws IOException {
        almLogoutServlet.doPost(request, response);

        verify(response, times(2)).addCookie(any(Cookie.class));
        verify(response, times(1)).setStatus(SlingHttpServletResponse.SC_OK);
    }

    // @Test
    // void testDoPostException() throws IOException {
    //     doThrow(new IOException()).when(response).addCookie(any(Cookie.class));

    //     almLogoutServlet.doPost(request, response);

    //     verify(response, times(1)).setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    // }
}

