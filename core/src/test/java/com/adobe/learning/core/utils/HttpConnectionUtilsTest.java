package com.adobe.learning.core.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HttpConnectionUtilsTest {

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetResponseFromHttpRequest_UnSuccessful() throws IOException {
        try (MockedStatic<StringUtils> mockedStringUtils = Mockito.mockStatic(StringUtils.class)) {
            mockedStringUtils.when(() -> StringUtils.isNotBlank(anyString())).thenReturn(true);
            mockedStringUtils.when(() -> StringUtils.isEmpty(anyString())).thenReturn(false);
            Map<String, String> requestProperties = new HashMap<>();
            requestProperties.put("Content-Type", "application/json");

            String response = HttpConnectionUtils.getResponseFromHttpRequest(
                    "https://enterpriseprofileproxy.c8f8f055.public.multi-containers.ibm.com/test-11",
                    "GET", requestProperties, "");
            assertNotNull(response);
        }
    }

    @Test
    void testIsValidRedirectUrl() {
        boolean isInValidUrl = HttpConnectionUtils.isValidRedirectUrl("http://test.com");
        assertFalse(isInValidUrl);

        boolean isValidUrl = HttpConnectionUtils.isValidRedirectUrl("https://learningmanagereu.adobe.com");
        assertTrue(isValidUrl);
    }
}
