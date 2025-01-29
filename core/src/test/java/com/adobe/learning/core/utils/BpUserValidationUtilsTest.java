package com.adobe.learning.core.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.adobe.learning.core.config.AlmEnvConfig;
import com.adobe.learning.core.constants.CommonConstants;

@ExtendWith(MockitoExtension.class)
class BpUserValidationUtilsTest {

        @Mock
        private AlmEnvConfig almEnvConfig;

        @InjectMocks
        private BpUserValidationUtils bpUserValidationUtils;

        @BeforeEach
        void setUp() {
                MockitoAnnotations.initMocks(this);
        }

        @Test
        void testGetBpStatus() throws Exception {
                try (MockedStatic<HttpConnectionUtils> httpConnectionUtilsMockedStatic = Mockito
                                .mockStatic(HttpConnectionUtils.class);
                                MockedStatic<ObjectMapper> objectMapperMockedStatic = Mockito
                                                .mockStatic(ObjectMapper.class)) {
                        httpConnectionUtilsMockedStatic
                                        .when(() -> HttpConnectionUtils.getResponseFromHttpRequest(anyString(),
                                                        anyString(), any(Map.class), any()))
                                        .thenReturn("[{\"countryEnterprises\":[{\"BPStatus\":\"Active\"}]}]");
                        String result = BpUserValidationUtils.getBpStatus("uuid", almEnvConfig);
                        assertEquals("Active", result);
                }
        }

        @Test
        void testGetBpStatus_WhenEmptyResponse() throws Exception {
                try (MockedStatic<HttpConnectionUtils> httpConnectionUtilsMockedStatic = Mockito
                                .mockStatic(HttpConnectionUtils.class);
                                MockedStatic<ObjectMapper> objectMapperMockedStatic = Mockito
                                                .mockStatic(ObjectMapper.class)) {
                        httpConnectionUtilsMockedStatic
                                        .when(() -> HttpConnectionUtils.getResponseFromHttpRequest(anyString(),
                                                        anyString(), any(Map.class),
                                                        any()))
                                        .thenReturn("");
                        String result = BpUserValidationUtils.getBpStatus("uuid", almEnvConfig);
                        assertEquals("", result);
                }
        }

        @Test
        void testGetBpStatus_WhenNull() throws Exception {

                String result = BpUserValidationUtils.getBpStatus("", almEnvConfig);
                assertEquals("", result);

                try (MockedStatic<HttpConnectionUtils> httpConnectionUtilsMockedStatic = Mockito
                                .mockStatic(HttpConnectionUtils.class);
                                MockedStatic<ObjectMapper> objectMapperMockedStatic = Mockito
                                                .mockStatic(ObjectMapper.class)) {
                        httpConnectionUtilsMockedStatic
                                        .when(() -> HttpConnectionUtils.getResponseFromHttpRequest(anyString(),
                                                        anyString(), any(Map.class),
                                                        any()))
                                        .thenReturn(null);
                        String result_2 = BpUserValidationUtils.getBpStatus("uuid", almEnvConfig);
                        assertEquals("", result_2);
                }
        }

        @Test
        void testGetPrmPartnerAccessToken() throws Exception {
                try (MockedStatic<HttpConnectionUtils> httpConnectionUtilsMockedStatic = Mockito
                                .mockStatic(HttpConnectionUtils.class);
                                MockedStatic<ObjectMapper> objectMapperMockedStatic = Mockito
                                                .mockStatic(ObjectMapper.class)) {
                        httpConnectionUtilsMockedStatic
                                        .when(() -> HttpConnectionUtils.getResponseFromHttpRequest(anyString(),
                                                        anyString(), any(Map.class),
                                                        any()))
                                        .thenReturn("{\"access_token\":\"token\",\"token_type\":\"bearer\"}");
                        Map<String, String> result = BpUserValidationUtils.getPrmPartnerAccessToken(almEnvConfig);
                        assertEquals("bearer", result.get(CommonConstants.TOKEN_TYPE));
                        assertEquals("token", result.get(CommonConstants.ACCESS_TOKEN));
                }
        }

        @Test
        void testGetJwtToken() {
                try (MockedStatic<Base64> base64MockedStatic = Mockito.mockStatic(Base64.class)) {
                        base64MockedStatic.when(() -> Base64.encodeBase64URLSafeString(any(byte[].class)))
                                        .thenReturn("encodedString");
                        Mockito.when(almEnvConfig.getJwtIssuer()).thenReturn("issuer");
                        Mockito.when(almEnvConfig.getJwtSubject()).thenReturn("subject");
                        Mockito.when(almEnvConfig.getJwtAudience()).thenReturn("audience");
                        Mockito.when(almEnvConfig.jwtPrivateKey()).thenReturn("privateKey");

                        String result = BpUserValidationUtils.getJwtToken(almEnvConfig);
                        assertEquals("", result);
                }
        }

        @Test
        void testGetUUID() throws Exception {
                try (MockedStatic<HttpConnectionUtils> httpConnectionUtilsMockedStatic = Mockito
                                .mockStatic(HttpConnectionUtils.class);
                                MockedStatic<ObjectMapper> objectMapperMockedStatic = Mockito
                                                .mockStatic(ObjectMapper.class)) {
                        httpConnectionUtilsMockedStatic
                                        .when(() -> HttpConnectionUtils.getResponseFromHttpRequest(anyString(),
                                                        anyString(), any(Map.class),
                                                        any()))
                                        .thenReturn("");
                        String result = BpUserValidationUtils.getUUID("userEmail", almEnvConfig);
                        assertEquals("", result);

                        httpConnectionUtilsMockedStatic
                                        .when(() -> HttpConnectionUtils.getResponseFromHttpRequest(anyString(),
                                                        anyString(), any(Map.class),
                                                        any()))
                                        .thenReturn("{\"iui\":\"uuid\"}");
                        result = BpUserValidationUtils.getUUID("userEmail", almEnvConfig);
                        assertNotNull(result);
                }
        }

        @Test
        void testGetAccessTokenForUUID() throws Exception {
                try (MockedStatic<HttpConnectionUtils> httpConnectionUtilsMockedStatic = Mockito
                                .mockStatic(HttpConnectionUtils.class);
                                MockedStatic<ObjectMapper> objectMapperMockedStatic = Mockito
                                                .mockStatic(ObjectMapper.class)) {
                        httpConnectionUtilsMockedStatic
                                        .when(() -> HttpConnectionUtils.getResponseFromHttpRequest(anyString(),
                                                        anyString(), any(Map.class),
                                                        any()))
                                        .thenReturn("");
                        String result = BpUserValidationUtils.getAccessTokenForUUID(almEnvConfig);
                        assertEquals("", result);
                }
        }

}
