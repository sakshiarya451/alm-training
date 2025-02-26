package com.adobe.learning.core.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.adobe.learning.core.config.AlmEnvConfig;
import com.adobe.learning.core.constants.CommonConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class CourseLaunchUtilsTest {

    @Mock
    private SlingHttpServletRequest request;
    @Mock
    private SlingHttpServletResponse response;
    @Mock
    private AlmEnvConfig almEnvConfig;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private JsonNode rootNode;

    @SuppressWarnings("deprecation")
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAlmAdminAccessToken() {
        Map<String, String> accountCredentialMap = new HashMap<>();
        accountCredentialMap.put(CommonConstants.REFRESH_TOKEN, "refreshToken");
        accountCredentialMap.put(CommonConstants.APPLICATION_ID, "appId");
        accountCredentialMap.put(CommonConstants.APPLICATION_SECRET, "appSecret");

        try (MockedStatic<HttpConnectionUtils> utilities = Mockito.mockStatic(HttpConnectionUtils.class)) {
            utilities
                    .when(() -> HttpConnectionUtils.getResponseFromHttpRequest(anyString(), anyString(), any(Map.class),
                            any()))
                    .thenReturn("{\"access_token\":\"token\"}");
            String result = CourseLaunchUtils.getAlmAdminAccessToken(accountCredentialMap, "baseUrl");
            assertEquals("token", result);
        }
    }

    @Test
    void testGetAlmAdminAccessToken_WhenEmptyResponse() {
        Map<String, String> accountCredentialMap = new HashMap<>();
        accountCredentialMap.put(CommonConstants.REFRESH_TOKEN, "refreshToken");
        accountCredentialMap.put(CommonConstants.APPLICATION_ID, "appId");
        accountCredentialMap.put(CommonConstants.APPLICATION_SECRET, "appSecret");

        try (MockedStatic<HttpConnectionUtils> utilities = Mockito.mockStatic(HttpConnectionUtils.class)) {
            utilities
                    .when(() -> HttpConnectionUtils.getResponseFromHttpRequest(anyString(), anyString(), any(Map.class),
                            any()))
                    .thenReturn("");
            String result = CourseLaunchUtils.getAlmAdminAccessToken(accountCredentialMap, "almBaseUrl");
            assertEquals("", result);
        }
    }

    @Test
    void testIsUserEnrolled() {
        try (MockedStatic<HttpConnectionUtils> utilities = Mockito.mockStatic(HttpConnectionUtils.class)) {
            utilities
                    .when(() -> HttpConnectionUtils.getResponseFromHttpRequest(anyString(), anyString(), any(Map.class),
                            any()))
                    .thenReturn("response");
            boolean result = CourseLaunchUtils.isUserEnrolled("loId", "instanceId", "accessToken", "almBaseUrl");
            assertTrue(result);
        }
    }

    @Test
    void testIsUserEnrolled_WhenEmptyParam() {
        try (MockedStatic<HttpConnectionUtils> utilities = Mockito.mockStatic(HttpConnectionUtils.class)) {
            utilities
                    .when(() -> HttpConnectionUtils.getResponseFromHttpRequest(anyString(), anyString(), any(Map.class),
                            any()))
                    .thenReturn("response");

            boolean result = CourseLaunchUtils.isUserEnrolled("", "", "", "");
            assertFalse(result);
        }
    }

    @Test
    void testIsUserGroupAsUserType_WhenEmptyParams() {
        boolean result = CourseLaunchUtils.isUserGroupAsUserType("", "", "", "");
        assertFalse(result);
    }

    @Test
    void testIsUserGroupAsUserType_WhenEmptyResponse() {
        try (MockedStatic<HttpConnectionUtils> utilities = Mockito.mockStatic(HttpConnectionUtils.class)) {
            utilities
                    .when(() -> HttpConnectionUtils.getResponseFromHttpRequest(anyString(), anyString(), any(Map.class),
                            any()))
                    .thenReturn("");

            boolean result = CourseLaunchUtils.isUserGroupAsUserType("userType", "userId", "accessToken", "almBaseUrl");
            assertFalse(result);
        }
    }

    @Test
    void testIsUserGroupAsUserType_WhenInvalidResponse() {
        try (MockedStatic<HttpConnectionUtils> utilities = Mockito.mockStatic(HttpConnectionUtils.class)) {
            utilities
                    .when(() -> HttpConnectionUtils.getResponseFromHttpRequest(anyString(), anyString(), any(Map.class),
                            any()))
                    .thenReturn("{\"data\":[{\"attributes\":{\"name_invalid\":\"userType\"}}]}");

            boolean result = CourseLaunchUtils.isUserGroupAsUserType("userType", "userId", "accessToken", "almBaseUrl");
            assertFalse(result);
        }
    }

    @Test
    void testUpdateUserType() {
        Map<String, String> userMap = new HashMap<>();
        userMap.put(CommonConstants.USER_ID, "userId");
        userMap.put(CommonConstants.USER_EMAIL, "userEmail");
        userMap.put(CommonConstants.USER_TYPE, "userType");
        userMap.put(CommonConstants.USER_NAME, "userName");
        userMap.put(CommonConstants.UUID, "uuid");

        try (MockedStatic<HttpConnectionUtils> utilities = Mockito.mockStatic(HttpConnectionUtils.class);
                MockedStatic<BpUserValidationUtils> bpUtilStatic = Mockito.mockStatic(BpUserValidationUtils.class)) {
            utilities
                    .when(() -> HttpConnectionUtils.getResponseFromHttpRequest(anyString(), anyString(), any(Map.class),
                            any()))
                    .thenReturn("{\"data\":{\"attributes\":{\"fields\":{\"userType\":\"updatedUserType\"}}}}");

            bpUtilStatic.when(() -> BpUserValidationUtils.getBpStatus("uuid", almEnvConfig)).thenReturn("inactive");
            String result = CourseLaunchUtils.updateUserType(userMap, "accessToken", almEnvConfig, "almBaseUrl");
            assertEquals("updatedUserType", result);

            utilities
                    .when(() -> HttpConnectionUtils.getResponseFromHttpRequest(anyString(), anyString(), any(Map.class),
                            any()))
                    .thenReturn("");
            bpUtilStatic.when(() -> BpUserValidationUtils.getBpStatus("uuid", almEnvConfig)).thenReturn("inactive");
            String result_2 = CourseLaunchUtils.updateUserType(userMap, "accessToken", almEnvConfig, "almBaseUrl");
            assertEquals("userType", result_2);

            utilities
                    .when(() -> HttpConnectionUtils.getResponseFromHttpRequest(anyString(), anyString(), any(Map.class),
                            any()))
                    .thenReturn("{'json':'json'}");
            bpUtilStatic.when(() -> BpUserValidationUtils.getBpStatus("uuid", almEnvConfig)).thenReturn("inactive");
            String result_3 = CourseLaunchUtils.updateUserType(userMap, "accessToken", almEnvConfig, "almBaseUrl");
            assertEquals("userType", result_3);
        }
    }

    @Test
    void testUpdateUserType_WhenEmptyParam() {
        String result = CourseLaunchUtils.updateUserType(new HashMap<>(), "", almEnvConfig, "almBaseUrl");
        assertEquals("", result);
    }

    @Test
    void testDetermineUserType_WhenIbmer() {
        String result = CourseLaunchUtils.determineUserType("neelansh.bhatia@ibm.com", "IBMer", null);
        assertEquals("IBMer", result);
        String result_2 = CourseLaunchUtils.determineUserType("neelansh.bhatia@ibm.com", "Client", null);
        assertEquals("IBMer", result_2);
    }

    @Test
    void testDetermineUserType_WhenBp() {
        String result = CourseLaunchUtils.determineUserType("neelanshb@adob.com", "Client", "active");
        assertEquals("BusinessPartner", result);
        String result_2 = CourseLaunchUtils.determineUserType("neelanshb@adobe.com", "BusinessPartner", "active");
        assertEquals("BusinessPartner", result_2);
    }

    @Test
    void testDetermineUserType_WhenClient() {
        String result = CourseLaunchUtils.determineUserType("neelanshb@adobe.com", "Client", "");
        assertEquals("Client", result);
        String result_2 = CourseLaunchUtils.determineUserType("neelanshb@adobe.com", "BusinessPartner", "");
        assertEquals("Client", result_2);
    }

    @Test
    void testDetermineUserType_WhenOther() {
        String result = CourseLaunchUtils.determineUserType("neelanshb@adobe.com", "Other", "Other");
        assertEquals("Client", result);
    }

    @Test
    void testAccountCredential() {
        when(almEnvConfig.getEmployeeApplicationIdSandbox()).thenReturn("applicationId");
        Map<String, String> result = CourseLaunchUtils.accountCredential("121816", almEnvConfig);
        assertFalse(result.isEmpty());

        when(almEnvConfig.getSkillsBuildApplicationIdSandbox()).thenReturn("applicationId");
        Map<String, String> result_2 = CourseLaunchUtils.accountCredential("10770", almEnvConfig);
        assertFalse(result_2.isEmpty());

        when(almEnvConfig.getSkillsBuildAdultApplicationIdSandbox()).thenReturn("applicationId");
        Map<String, String> result_3 = CourseLaunchUtils.accountCredential("10771", almEnvConfig);
        assertFalse(result_3.isEmpty());

        when(almEnvConfig.getBusinessPartnerAndClientApplicationIdSandbox()).thenReturn("applicationId");
        Map<String, String> result_4 = CourseLaunchUtils.accountCredential("10768", almEnvConfig);
        assertFalse(result_4.isEmpty());

        when(almEnvConfig.getEmployeeApplicationIdStage()).thenReturn("applicationId");
        Map<String, String> result_5 = CourseLaunchUtils.accountCredential("10813", almEnvConfig);
        assertFalse(result_5.isEmpty());

        when(almEnvConfig.getSkillsBuildApplicationIdStage()).thenReturn("applicationId");
        Map<String, String> result_6 = CourseLaunchUtils.accountCredential("10815", almEnvConfig);
        assertFalse(result_6.isEmpty());

        when(almEnvConfig.getBusinessPartnerAndClientApplicationIdStage()).thenReturn("applicationId");
        Map<String, String> result_7 = CourseLaunchUtils.accountCredential("10814", almEnvConfig);
        assertFalse(result_7.isEmpty());

        when(almEnvConfig.getSkillsBuildAdultApplicationIdStage()).thenReturn("applicationId");
        Map<String, String> result_8 = CourseLaunchUtils.accountCredential("10816", almEnvConfig);
        assertFalse(result_8.isEmpty());

        when(almEnvConfig.getEmployeeApplicationId()).thenReturn("applicationId");
        Map<String, String> result_9 = CourseLaunchUtils.accountCredential("2133", almEnvConfig);
        assertFalse(result_9.isEmpty());

        when(almEnvConfig.getBusinessPartnerAndClientApplicationId()).thenReturn("applicationId");
        Map<String, String> result_10 = CourseLaunchUtils.accountCredential("2136", almEnvConfig);
        assertFalse(result_10.isEmpty());

        when(almEnvConfig.getSkillsBuildApplicationId()).thenReturn("applicationId");
        Map<String, String> result_11 = CourseLaunchUtils.accountCredential("2134", almEnvConfig);
        assertFalse(result_11.isEmpty());

        when(almEnvConfig.getSkillsBuildAdultApplicationId()).thenReturn("applicationId");
        Map<String, String> result_12 = CourseLaunchUtils.accountCredential("2135", almEnvConfig);
        assertFalse(result_12.isEmpty());

        Map<String, String> result_13 = CourseLaunchUtils.accountCredential("2196", almEnvConfig);
        assertFalse(result_13.isEmpty());

        Map<String, String> result_14 = CourseLaunchUtils.accountCredential("1926", almEnvConfig);
        assertFalse(result_14.isEmpty());

        when(almEnvConfig.getPreHireApplicationIdSandbox()).thenReturn("applicationId");
        Map<String, String> result_15 = CourseLaunchUtils.accountCredential("11129", almEnvConfig);
        assertFalse(result_15.isEmpty());

        when(almEnvConfig.getPreHireApplicationIdStage()).thenReturn("applicationId");
        Map<String, String> result_16 = CourseLaunchUtils.accountCredential("11130", almEnvConfig);
        assertFalse(result_16.isEmpty());

        Map<String, String> result_18 = CourseLaunchUtils.accountCredential("default", almEnvConfig);
        assertFalse(result_18.isEmpty());
    }

    // TODO : These pending Test methods

    /*
     * @Test
     * void testHandleBpUseCaseImplementation() {
     * Cookie[] cookies = {
     * new Cookie("loid", "loid"),
     * new Cookie("instance_id", "instance_id")
     * };
     * Map<String, String> userMap = new HashMap<>() {
     * {
     * put("uuid", "uuid");
     * put("userType", "userType");
     * put("userId", "userId");
     * 
     * }
     * };
     * try (MockedStatic<BpUserValidationUtils> bpUtils =
     * Mockito.mockStatic(BpUserValidationUtils.class);
     * MockedStatic<CourseLaunchUtils> courseLaunchUtils =
     * Mockito.mockStatic(CourseLaunchUtils.class)) {
     * // when(request.getCookies()).thenReturn(cookies);
     * bpUtils
     * .when(() ->
     * BpUserValidationUtils.getBpStatus(userMap.get(CommonConstants.UUID),
     * almEnvConfig))
     * .thenReturn("Active");
     * courseLaunchUtils
     * .when(() -> CourseLaunchUtils.updateUserType(userMap, "Active",
     * "almAccessToken"))
     * .thenReturn("BusinessPartner");
     * CourseLaunchUtils.handleBpUseCaseImplementation(userMap, almEnvConfig,
     * "almAdminaccessToken",
     * "almAccessToken", request, response, new HashMap<>());
     * }
     * }
     * 
     * @Test
     * void testHandleUserEnrollment() {
     * try (MockedStatic<CourseLaunchUtils> courseLaunchUtils =
     * Mockito.mockStatic(CourseLaunchUtils.class)) {
     * courseLaunchUtils.when(() ->
     * CourseLaunchUtils.readCookie(CommonConstants.COURSE_ID_REQ_PARAM,
     * request))
     * .thenReturn("loid");
     * 
     * }
     * CourseLaunchUtils.handleUserEnrollment(null, request, null, response, null);
     * }
     */

    @Test
    void testGetUserDetails() throws JsonProcessingException {
        String json = "{\"data\":{\"id\":\"17423309\",\"attributes\":{\"email\":\"rahul.rajan227@gmail.com\",\"name\":\"RahulRajan1\",\"fields\":{\"userType\":\"bp\",\"uuid\":\"uuid\"}}}}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);
        Map<String, String> result = CourseLaunchUtils.getUserDetails(jsonNode);
        assertEquals("bp", result.get("userType"));

    }

    @Test
    void testIsLoRetired_whenEmptyParam() {
        boolean result = CourseLaunchUtils.isLoRetired("");
        assertFalse(result);
    }

    @Test
    void testGetTrainingAccountErrorMessages() {
        String result = CourseLaunchUtils.getTrainingAccountErrorMessages(CommonConstants.IBMER);
        assertEquals(CommonConstants.IBMER_ACCESS_ERR, result);

        String result_2 = CourseLaunchUtils.getTrainingAccountErrorMessages(CommonConstants.BUSINESS_PARTNER);
        assertEquals(CommonConstants.BP_ACCESS_ERR, result_2);

        String result_3 = CourseLaunchUtils.getTrainingAccountErrorMessages(CommonConstants.CLIENT);
        assertEquals(CommonConstants.CLIENT_ACCESS_ERR, result_3);
    }

    @Test
    void testIsUserAlreadyEnrolled() {

        try (MockedStatic<HttpConnectionUtils> utilities = Mockito.mockStatic(HttpConnectionUtils.class)) {
            utilities
                    .when(() -> HttpConnectionUtils.getResponseFromHttpRequest(anyString(), anyString(), any(Map.class),
                            any()))
                    .thenReturn("non-empty-response");
            boolean isUserAlreadyEnrolled = CourseLaunchUtils.isUserAlreadyEnrolled("almBaseUrl", "userid",
                    "accessToken", "instanceid");
            assertTrue(isUserAlreadyEnrolled);

            isUserAlreadyEnrolled = CourseLaunchUtils.isUserAlreadyEnrolled("almBaseUrl", "", "", "");
            assertFalse(isUserAlreadyEnrolled);

            isUserAlreadyEnrolled = CourseLaunchUtils.isUserAlreadyEnrolled("almBaseUrl", "userid", "accessToken", "");
            assertFalse(isUserAlreadyEnrolled);

            isUserAlreadyEnrolled = CourseLaunchUtils.isUserAlreadyEnrolled("almBaseUrl", "userid", "", "");
            assertFalse(isUserAlreadyEnrolled);
        }
    }

    @Test
    void testIsManagerNominated() {
        boolean result_1 = CourseLaunchUtils.isManagerNominated(ApiResponseConstants.LO_RESPONSE_MANAGER_NOMINATED);
        assertTrue(result_1);

        boolean result_2 = CourseLaunchUtils.isManagerNominated(ApiResponseConstants.LO_RESPONSE_SELF_ENROLLED);
        assertFalse(result_2);

        boolean result_3 = CourseLaunchUtils.isManagerNominated("{'json':'json'}");
        assertFalse(result_3);
    }

    @Test
    void testGetUserEnrolledInstance() {
        String result_1 = CourseLaunchUtils.getUserEnrolledInstance(ApiResponseConstants.LO_RESPONSE_MANAGER_NOMINATED,
                ApiResponseConstants.LO_RESPONSE_MANAGER_NOMINATED);
        assertNotNull(result_1);
    }

    @Test
    void testIsLoRetired() {
        boolean result_1 = CourseLaunchUtils.isLoRetired(null);
        assertFalse(result_1);

        boolean result_2 = CourseLaunchUtils.isLoRetired(ApiResponseConstants.LO_RESPONSE_SELF_ENROLLED);
        assertTrue(result_2);

        boolean result_3 = CourseLaunchUtils.isLoRetired("{'json':'json'}");
        assertFalse(result_3);
    }

    @Test
    void testGetAllAuthor() {
        String result_1 = CourseLaunchUtils.getAllAuthor(ApiResponseConstants.LO_RESPONSE_MANAGER_NOMINATED);
        assertEquals("Neelanshibm, Neelansh", result_1);

        String result_2 = CourseLaunchUtils.getAllAuthor("{'json' : 'Json'}");
        assertEquals("", result_2);
    }

    @Test
    void testGetLoResponse() {
        String result = CourseLaunchUtils.getLOResponse("loId", "AdminToken", "BaseUrl", true);
        assertEquals("", result);

        try (MockedStatic<HttpConnectionUtils> utilities = Mockito.mockStatic(HttpConnectionUtils.class)) {
            utilities
                    .when(() -> HttpConnectionUtils.getResponseFromHttpRequest(anyString(), anyString(), any(Map.class),
                            any()))
                    .thenReturn("response");
            String result_2 = CourseLaunchUtils.getLOResponse("loId", "adminToken", "BaseUrl", false);
            assertEquals("response", result_2);

        }

        String result_3 = CourseLaunchUtils.getLOResponse("", "", "", true);
        assertEquals("", result_3);
    }

    @Test
    void testFilterLOResponseAsAdmin() {
        String result = CourseLaunchUtils.filterLOResponseAsAdmin(ApiResponseConstants.LO_RESPONSE_MANAGER_NOMINATED);
        assertEquals("{\"data\":{\"attributes\":{\"state\":\"Published\"}}}", result);

        String result_2 = CourseLaunchUtils.filterLOResponseAsAdmin("");
        assertEquals("", result_2);

        String result_3 = CourseLaunchUtils.filterLOResponseAsAdmin("{'json' : 'Json'}");
        assertEquals("", result_3);
    }

    @Test
    void testUpdateUserLocale() {
        Map<String, String> userMap = new HashMap<>();
        userMap.put("uiLocale", "en-US");
        userMap.put("contentLocale", "fr-FR");

        Map<String, String> accountCredentialMap = new HashMap<>();
        accountCredentialMap.put("almAccessToken", "almAccessToken");
        accountCredentialMap.put("languagecode", "en-US");

        try (MockedStatic<HttpConnectionUtils> utilities = Mockito.mockStatic(HttpConnectionUtils.class)) {
            utilities
                    .when(() -> HttpConnectionUtils.getResponseFromHttpRequest(anyString(), anyString(), any(Map.class),
                            any()))
                    .thenReturn("response");
            CourseLaunchUtils.updateUserLocale(userMap, accountCredentialMap);
        }

        CourseLaunchUtils.updateUserLocale(new HashMap<>(), new HashMap<>());
        CourseLaunchUtils.updateUserLocale(userMap, accountCredentialMap);
    }
}
