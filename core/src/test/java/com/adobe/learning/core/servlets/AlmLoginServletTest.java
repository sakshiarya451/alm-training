package com.adobe.learning.core.servlets;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;

import javax.servlet.http.Cookie;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.learning.core.utils.Constants;
import com.google.gson.JsonObject;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith({AemContextExtension.class})
class AlmLoginServletTest {

  @InjectMocks private AlmLoginServlet almLoginServlet;

  @Mock private SlingHttpServletRequest request;

  @Mock private SlingHttpServletResponse response;

  @Mock private Cookie cookie;

  @Mock private PrintWriter printWriter;

  private static final Logger LOGGER = LoggerFactory.getLogger(AlmLoginServletTest.class);

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void testDoPost_withCookie() throws Exception {
    when(request.getCookie(Constants.Config.ACCESS_TOKEN_COOKIE_NAME)).thenReturn(cookie);
    when(cookie.getValue()).thenReturn("test-token");
    when(response.getWriter()).thenReturn(printWriter);

    almLoginServlet.doPost(request, response);

    verify(response).setStatus(SlingHttpServletResponse.SC_OK);
    verify(response).setContentType("application/json");
    JsonObject expectedJson = new JsonObject();
    expectedJson.addProperty("access_token", "test-token");
    verify(printWriter).write(expectedJson.toString());
  }

  @Test
  void testDoPost_withoutCookie() throws Exception {
    when(request.getCookie(Constants.Config.ACCESS_TOKEN_COOKIE_NAME)).thenReturn(null);

    almLoginServlet.doPost(request, response);

    verify(response).setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
  }

  @Test
  void testDoPost_withException() throws Exception {
    when(request.getCookie(Constants.Config.ACCESS_TOKEN_COOKIE_NAME)).thenThrow(new RuntimeException());

    almLoginServlet.doPost(request, response);

    verify(response).setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    // Ensure that the exception is logged
    // Ideally, we would capture the log output and assert on it, but for simplicity, we are just ensuring the error path is tested.
  }
}

