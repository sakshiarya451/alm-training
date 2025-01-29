package com.adobe.learning.core.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.learning.core.config.AlmEnvConfig;
import com.adobe.learning.core.constants.CommonConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utility class for CourseLaunch.java
 */
public class CourseLaunchUtils {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(CourseLaunchUtils.class);

    /**
     * Constructor
     */
    private CourseLaunchUtils() {

    }

    /**
     * 
     * @param accountCredentialMap
     * @param almBaseUrl
     * @return
     */
    public static String getAlmAdminAccessToken(Map<String, String> accountCredentialMap, String almBaseUrl) {
        String adminAccessTokenUrl = new StringBuilder().append(almBaseUrl)
                .append(CommonConstants.ACCESS_TOKEN_API_PATH)
                .append("/refresh?")
                .append("refresh_token=" + accountCredentialMap.get(CommonConstants.REFRESH_TOKEN))
                .append("&client_id=" + accountCredentialMap.get(CommonConstants.APPLICATION_ID))
                .append("&client_secret=" + accountCredentialMap.get(CommonConstants.APPLICATION_SECRET))
                .toString();
        String adminAcessTokenResponse = HttpConnectionUtils.getResponseFromHttpRequest(adminAccessTokenUrl,
                CommonConstants.POST_METHOD, new HashMap<>(), null);
        if (StringUtils.isNotEmpty(adminAcessTokenResponse)) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(adminAcessTokenResponse);
                return rootNode.path(CommonConstants.ACCESS_TOKEN).asText();
            } catch (JsonProcessingException e) {
                LOG.error("Error in processing json in getAdminAccessToken() : {} {}", e.getMessage(), e);
            }
        }
        LOG.info("Failed to get ALM admin access token");
        return StringUtils.EMPTY;
    }

    /**
     * 
     * @param loId
     * @param instanceId
     * @param accessToken
     * @param almBaseUrl
     * @return
     */
    public static boolean isUserEnrolled(String loId, String instanceId, String accessToken,
            String almBaseUrl) {
        if (!StringUtils.isAnyBlank(loId, instanceId, accessToken)) {
            String requestUrl = new StringBuilder().append(almBaseUrl)
                    .append(CommonConstants.USER_ENROLLMENT_API_PATH)
                    .append("?loId=" + loId)
                    .append("&loInstanceId=" + instanceId)
                    .toString();
            Map<String, String> requestProperties = new HashMap<>();
            requestProperties.put(CommonConstants.AUTHORIZATION, CommonConstants.BEARER + " " + accessToken);
            requestProperties.put(CommonConstants.CONTENT_TYPE, CommonConstants.APPLICATION_JSON);
            String userEnrollmentResponseString = HttpConnectionUtils.getResponseFromHttpRequest(requestUrl,
                    CommonConstants.POST_METHOD,
                    requestProperties, null);
            LOG.info("User Enrollment Response : {}", userEnrollmentResponseString);
            return StringUtils.isNotEmpty(userEnrollmentResponseString);
        }
        return false;
    }

    /**
     * 
     * @param userType
     * @param userId
     * @param accessToken
     * @param almEnvConfig
     * @return
     */
    public static boolean isUserGroupAsUserType(String userType, String userId, String accessToken,
            String almBaseUrl) {
        if (!StringUtils.isAnyEmpty(userType, userId, accessToken)) {
            String requestUrl = new StringBuilder().append(almBaseUrl)
                    .append(CommonConstants.USERS_API_PATH)
                    .append(userId)
                    .append("/userGroups?page[offset]=0&page[limit]=100")
                    .toString();
            Map<String, String> requestProperties = new HashMap<>();
            requestProperties.put(CommonConstants.ACCEPT, CommonConstants.APPLICATION_VND_API_JSON);
            requestProperties.put(CommonConstants.AUTHORIZATION, CommonConstants.OAUTH + " " + accessToken);
            String userGroupResponseString = HttpConnectionUtils.getResponseFromHttpRequest(requestUrl,
                    CommonConstants.GET_METHOD,
                    requestProperties, null);
            if (StringUtils.isNotEmpty(userGroupResponseString)) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode rootNode = objectMapper.readTree(userGroupResponseString);
                    JsonNode dataArray = rootNode.path(CommonConstants.DATA);
                    for (JsonNode obj : dataArray) {
                        JsonNode attributes = obj.path(CommonConstants.ATTRIBUTES);
                        if (attributes.path(CommonConstants.NAME).asText().contains(userType))
                            return true;
                    }
                } catch (JsonProcessingException e) {
                    LOG.error("Error in processing json in isUserGroupAsUserType() : {} {}", e.getMessage(), e);
                }
            }
        }
        return false;
    }

    /**
     * 
     * @param userMap
     * @param accessToken
     * @param almEnvConfig
     * @param almBaseUrl
     * @return
     */
    public static String updateUserType(Map<String, String> userMap, String accessToken,
            AlmEnvConfig almEnvConfig, String almBaseUrl) {

        if (userMap.isEmpty() || StringUtils.isEmpty(accessToken))
            return StringUtils.EMPTY;

        String userId = userMap.get(CommonConstants.USER_ID);
        String userEmail = userMap.get(CommonConstants.USER_EMAIL);
        String userType = userMap.get(CommonConstants.USER_TYPE) != null ? userMap.get(CommonConstants.USER_TYPE) : "";
        String userName = userMap.get(CommonConstants.USER_NAME);
        String uuid = userMap.get(CommonConstants.UUID);

        String updatedUUID = StringUtils.isNotEmpty(uuid) ? uuid
                : BpUserValidationUtils.getUUID(userEmail, almEnvConfig);
        String bpStatus = userEmail.contains(CommonConstants.DOMAIN_IBM) ? StringUtils.EMPTY
                : BpUserValidationUtils.getBpStatus(updatedUUID, almEnvConfig);
        String determinedUserType = determineUserType(userEmail, userType, bpStatus);

        String requestUrl = new StringBuilder().append(almBaseUrl)
                .append(CommonConstants.USERS_API_PATH)
                .append(userId)
                .toString();

        Map<String, String> requestProperties = new HashMap<>();
        requestProperties.put(CommonConstants.CONTENT_TYPE,
                CommonConstants.APPLICATION_VND_API_JSON + ";charset=UTF-8");
        requestProperties.put(CommonConstants.ACCEPT, CommonConstants.APPLICATION_VND_API_JSON);
        requestProperties.put(CommonConstants.AUTHORIZATION, CommonConstants.OAUTH + " " + accessToken);
        requestProperties.put(CommonConstants.X_HTTP_METHOD_OVERRIDE, CommonConstants.PATCH);

        if (uuid.equals(updatedUUID) && userType.equals(determinedUserType))
            return userType;

        String requestBody = "{\"data\":{\"id\":\"" + userId +
                "\",\"type\":\"user\",\"attributes\":{\"email\":\"" + userEmail +
                "\",\"name\":\"" + userName +
                "\",\"fields\":{\"userType\":\"" + determinedUserType + "\"},\"metadata\":{\"uuid\":\"" + updatedUUID +
                "\"}}}}";

        String updateUserTypeResponse = HttpConnectionUtils.getResponseFromHttpRequest(requestUrl,
                CommonConstants.POST_METHOD, requestProperties, requestBody);
        LOG.info("Update User Type response : {}", updateUserTypeResponse);

        if (StringUtils.isNotEmpty(updateUserTypeResponse)) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(updateUserTypeResponse);
                return rootNode.path(CommonConstants.DATA)
                        .path(CommonConstants.ATTRIBUTES)
                        .path(CommonConstants.FIELDS)
                        .path(CommonConstants.USER_TYPE)
                        .asText();
            } catch (JsonProcessingException e) {
                LOG.error("Error in process json in updateUserType : {} {}", e.getMessage(), e);
            }
        }
        return userType;
    }

    /**
     * 
     * @param userEmail
     * @param userType
     * @param bpStatus
     * @return
     */
    public static String determineUserType(String userEmail, String userType, String bpStatus) {

        if (userEmail.contains(CommonConstants.DOMAIN_IBM)) {
            if (!userType.equalsIgnoreCase(CommonConstants.IBMER) || userType.equalsIgnoreCase(CommonConstants.IBMER))
                return CommonConstants.IBMER;
        } else if (bpStatus.equalsIgnoreCase(CommonConstants.ACTIVE)) {
            if (!userType.equalsIgnoreCase(CommonConstants.BUSINESS_PARTNER) ||
                    userType.equalsIgnoreCase(CommonConstants.BUSINESS_PARTNER))
                return CommonConstants.BUSINESS_PARTNER;
        } else if (!bpStatus.equalsIgnoreCase(CommonConstants.ACTIVE)) {
            if (!userType.equalsIgnoreCase(CommonConstants.CLIENT) || userType.equalsIgnoreCase(CommonConstants.CLIENT))
                return CommonConstants.CLIENT;
        } else
            return CommonConstants.CLIENT;

        return CommonConstants.CLIENT;
    }

    /**
     * 
     * @param accountId
     * @param almEnvConfig
     * @return
     */
    public static Map<String, String> accountCredential(String accountId, AlmEnvConfig almEnvConfig) {
        String applicationId = "";
        String applicationSecret = "";
        String ipId = "";
        String accessKey = "";
        String refreshToken = "";
        String loAccessErrorMessage = "";
        String loRetiredMessage = "";

        switch (accountId) {
            case "10738":
                applicationId = almEnvConfig.getEmployeeApplicationIdSandbox();
                applicationSecret = almEnvConfig.getEmployeeApplicationSecretSandbox();
                ipId = almEnvConfig.getEmployeeIpIdSandbox();
                accessKey = almEnvConfig.getEmployeeAccessKeySandbox();
                refreshToken = almEnvConfig.getEmployeeRefreshTokenSandbox();
                loAccessErrorMessage = CommonConstants.GENERIC_ERROR_MSG;
                loRetiredMessage = CommonConstants.EMPLOYEE_RETIRED_MESSAGE;
                break;
            case "10770":
                applicationId = almEnvConfig.getSkillsBuildApplicationIdSandbox();
                applicationSecret = almEnvConfig.getSkillsBuildApplicationSecretSandbox();
                ipId = almEnvConfig.getSkillsBuildIpIdSandbox();
                accessKey = almEnvConfig.getSkillsBuildAccessKeySandbox();
                refreshToken = almEnvConfig.getSkillsBuildRefreshTokenSandbox();
                loAccessErrorMessage = CommonConstants.GENERIC_ERROR_MSG;
                loRetiredMessage = CommonConstants.SKILLLSBUILD_RETIRED_MESSAGE;
                break;
            case "10771":
                applicationId = almEnvConfig.getSkillsBuildAdultApplicationIdSandbox();
                applicationSecret = almEnvConfig.getSkillsBuildAdultApplicationSecretSandbox();
                ipId = almEnvConfig.getSkillsBuildAdultIpIdSandbox();
                accessKey = almEnvConfig.getSkillsBuildAdultAccessKeySandbox();
                refreshToken = almEnvConfig.getSkillsBuildAdultRefreshTokenSandbox();
                loAccessErrorMessage = CommonConstants.GENERIC_ERROR_MSG;
                loRetiredMessage = CommonConstants.SKILLLSBUILD_ADULT_RETIRED_MESSAGE;
                break;
            case "10768":
                applicationId = almEnvConfig.getBusinessPartnerAndClientApplicationIdSandbox();
                applicationSecret = almEnvConfig.getBusinessPartnerAndClientApplicationSecretSandbox();
                ipId = almEnvConfig.getBusinessPartnerAndClientIpIdSandbox();
                accessKey = almEnvConfig.getBusinessPartnerAndClientAccessKeySandbox();
                refreshToken = almEnvConfig.getBusinessPartnerAndClientRefreshTokenSandbox();
                loRetiredMessage = CommonConstants.TRAINING_RETIRED_MESSAGE;
                break;
            case "10813":
                applicationId = almEnvConfig.getEmployeeApplicationIdStage();
                applicationSecret = almEnvConfig.getEmployeeApplicationSecretStage();
                ipId = almEnvConfig.getEmployeeIpIdStage();
                accessKey = almEnvConfig.getEmployeeAccessKeyStage();
                loAccessErrorMessage = CommonConstants.GENERIC_ERROR_MSG;
                loRetiredMessage = CommonConstants.EMPLOYEE_RETIRED_MESSAGE;
                refreshToken = almEnvConfig.getEmployeeRefreshTokenStage();
                break;
            case "10814":
                applicationId = almEnvConfig.getBusinessPartnerAndClientApplicationIdStage();
                applicationSecret = almEnvConfig.getBusinessPartnerAndClientApplicationSecretStage();
                ipId = almEnvConfig.getBusinessPartnerAndClientIpIdStage();
                accessKey = almEnvConfig.getBusinessPartnerAndClientAccessKeyStage();
                refreshToken = almEnvConfig.getBusinessPartnerAndClientRefreshTokenStage();
                loRetiredMessage = CommonConstants.TRAINING_RETIRED_MESSAGE;
                break;
            case "10815":
                applicationId = almEnvConfig.getSkillsBuildApplicationIdStage();
                applicationSecret = almEnvConfig.getSkillsBuildApplicationSecretStage();
                ipId = almEnvConfig.getSkillsBuildIpIdStage();
                accessKey = almEnvConfig.getSkillsBuildAccessKeyStage();
                loAccessErrorMessage = CommonConstants.GENERIC_ERROR_MSG;
                loRetiredMessage = CommonConstants.SKILLLSBUILD_RETIRED_MESSAGE;
                refreshToken = almEnvConfig.getSkillsBuildRefreshTokenStage();
                break;
            case "10816":
                applicationId = almEnvConfig.getSkillsBuildAdultApplicationIdStage();
                applicationSecret = almEnvConfig.getSkillsBuildAdultApplicationSecretStage();
                ipId = almEnvConfig.getSkillsBuildAdultIpIdStage();
                accessKey = almEnvConfig.getSkillsBuildAdultAccessKeyStage();
                loAccessErrorMessage = CommonConstants.GENERIC_ERROR_MSG;
                loRetiredMessage = CommonConstants.SKILLLSBUILD_ADULT_RETIRED_MESSAGE;
                refreshToken = almEnvConfig.getSkillsBuildAdultRefreshTokenStage();
                break;
            case "2133":
                applicationId = almEnvConfig.getEmployeeApplicationId();
                applicationSecret = almEnvConfig.getEmployeeApplicationSecret();
                ipId = almEnvConfig.getEmployeeIpId();
                accessKey = almEnvConfig.getEmployeeAccessKey();
                loAccessErrorMessage = CommonConstants.GENERIC_ERROR_MSG;
                loRetiredMessage = CommonConstants.EMPLOYEE_RETIRED_MESSAGE;
                refreshToken = almEnvConfig.getEmployeeRefreshToken();
                break;
            case "2136":
                applicationId = almEnvConfig.getBusinessPartnerAndClientApplicationId();
                applicationSecret = almEnvConfig.getBusinessPartnerAndClientApplicationSecret();
                ipId = almEnvConfig.getBusinessPartnerAndClientIpId();
                accessKey = almEnvConfig.getBusinessPartnerAndClientAccessKey();
                refreshToken = almEnvConfig.getBusinessPartnerAndClientRefreshToken();
                loRetiredMessage = CommonConstants.TRAINING_RETIRED_MESSAGE;
                break;
            case "2134":
                applicationId = almEnvConfig.getSkillsBuildApplicationId();
                applicationSecret = almEnvConfig.getSkillsBuildApplicationSecret();
                ipId = almEnvConfig.getSkillsBuildIpId();
                accessKey = almEnvConfig.getSkillsBuildAccessKey();
                loAccessErrorMessage = CommonConstants.GENERIC_ERROR_MSG;
                loRetiredMessage = CommonConstants.SKILLLSBUILD_RETIRED_MESSAGE;
                refreshToken = almEnvConfig.getSkillsBuildRefreshToken();
                break;
            case "2135":
                applicationId = almEnvConfig.getSkillsBuildAdultApplicationId();
                applicationSecret = almEnvConfig.getSkillsBuildAdultApplicationSecret();
                ipId = almEnvConfig.getSkillsBuildAdultIpId();
                accessKey = almEnvConfig.getSkillsBuildAdultAccessKey();
                loAccessErrorMessage = CommonConstants.GENERIC_ERROR_MSG;
                loRetiredMessage = CommonConstants.SKILLLSBUILD_ADULT_RETIRED_MESSAGE;
                refreshToken = almEnvConfig.getSkillsBuildAdultRefreshToken();
                break;
            case "2196": // performace instance
                applicationId = "1904a6b0-609d-4065-87ed-f7554c2f7155";
                applicationSecret = "3fb9ed13-4e5d-4ec6-ae3d-cd8ebc637048";
                refreshToken = "54cb853fd184169c31c7fff957f4000c";
                loAccessErrorMessage = CommonConstants.GENERIC_ERROR_MSG;
                loRetiredMessage = "Course retired";
                break;
            case "1926": // performace instance
                applicationId = "53f378ba-5125-40fe-89e3-ec683258137e";
                applicationSecret = "6b18c557-0230-461f-8240-da379c1533f2";
                refreshToken = "d9479e62a33454990dea72d02d5cfe19";
                loAccessErrorMessage = CommonConstants.GENERIC_ERROR_MSG;
                loRetiredMessage = "Course retired";
                break;
            case "11129": // pre-hire dev
                applicationId = almEnvConfig.getPreHireApplicationIdSandbox();
                // "17442acd-db2b-4969-9acf-a864da7b93af";
                applicationSecret = almEnvConfig.getPreHireApplicationSecretSandbox();
                // "e3c918f9-6d9e-40d9-8558-0b883d76ce9d";
                refreshToken = almEnvConfig.getPreHireRefreshTokenSandbox();
                // "1226a0cddf11709685d7d07ab8c58823";
                loAccessErrorMessage = CommonConstants.GENERIC_ERROR_MSG;
                loRetiredMessage = "Course retired";
                break;
            case "11130": // pre-hire acceptance
                applicationId = almEnvConfig.getPreHireApplicationIdStage();
                // "d383d8fd-ddb7-4b93-b750-311bfecb39cb";
                applicationSecret = almEnvConfig.getPreHireApplicationSecretStage();
                // "af8ffab6-dfcf-4a26-9c1e-395b1515d29e";
                refreshToken = almEnvConfig.getPreHireRefreshTokenStage();
                // "752ad0da6b9298f8126a01ddd7093ae9";
                loAccessErrorMessage = CommonConstants.GENERIC_ERROR_MSG;
                loRetiredMessage = "Course retired";
                break;
            case "2220": // pre-hire prod
                applicationId = almEnvConfig.getPreHireApplicationId();
                applicationSecret = almEnvConfig.getPreHireApplicationSecret();
                refreshToken = almEnvConfig.getPreHireRefreshToken();
                loAccessErrorMessage = CommonConstants.GENERIC_ERROR_MSG;
                loRetiredMessage = "Course retired";
                break;
            default:
                applicationId = "";
                applicationSecret = "";
                ipId = "";
                accessKey = "";
                refreshToken = "";
                loAccessErrorMessage = "";
                loRetiredMessage = "";
                break;
        }
        Map<String, String> accountCredentialMap = new HashMap<>();
        accountCredentialMap.put(CommonConstants.APPLICATION_ID, applicationId);
        accountCredentialMap.put(CommonConstants.APPLICATION_SECRET, applicationSecret);
        accountCredentialMap.put(CommonConstants.IP_ID, ipId);
        accountCredentialMap.put(CommonConstants.ACCESS_KEY, accessKey);
        accountCredentialMap.put(CommonConstants.REFRESH_TOKEN, refreshToken);
        accountCredentialMap.put(CommonConstants.LO_ACCESS_ERR_MSG, loAccessErrorMessage);
        accountCredentialMap.put(CommonConstants.LO_RETIRED_MSG, loRetiredMessage);
        return accountCredentialMap;

    }

    /**
     * 
     * @param userMap
     * @param almEnvConfig
     * @param accountCredentialMap
     * @return
     */
    public static String handleBpUseCaseImplementation(Map<String, String> userMap,
            AlmEnvConfig almEnvConfig, Map<String, String> accountCredentialMap) {

        String loId = accountCredentialMap.get("loId");
        String instanceId = accountCredentialMap.get("instanceId");
        String almAccessToken = accountCredentialMap.get("almAccessToken");
        String almAdminAccessToken = accountCredentialMap.get("almAdminToken");
        String almBaseUrl = accountCredentialMap.get("almBaseURL");
        String loResponseAsLearner = getLOResponse(loId, almAccessToken, almBaseUrl, true);
        String loResponseAsAdmin = getLOResponse(loId, almAdminAccessToken, almBaseUrl, true);
        String updatedUserType = updateUserType(userMap, almAdminAccessToken, almEnvConfig, almBaseUrl);

        if (StringUtils.isEmpty(loResponseAsAdmin)) {
            LOG.info("Course not accessible");
            return accountCredentialMap.get(CommonConstants.LO_ACCESS_ERR_MSG);
        }

        if (isLoRetired(loResponseAsAdmin)) {
            LOG.info("Course is retired");
            return accountCredentialMap.get(CommonConstants.LO_RETIRED_MSG);
        }

        if (StringUtils.isEmpty(loResponseAsLearner)) {
            LOG.info("Course not accessible");
            return getTrainingAccountErrorMessages(updatedUserType);
        }

        if (isUserAlreadyEnrolled(almBaseUrl, userMap.get(CommonConstants.USER_ID), almAccessToken,
                instanceId)) {
            LOG.info("User Already Enrolled");
            return StringUtils.EMPTY;
        }

        if (isUserEnrolled(loId, instanceId, almAccessToken, almBaseUrl)) {
            LOG.info("User Enrolled");
            return StringUtils.EMPTY;
        }

        boolean isUserGroupAsUserType = isUserGroupAsUserType(userMap.get(CommonConstants.USER_TYPE),
                userMap.get(CommonConstants.USER_ID), almAccessToken, almBaseUrl);

        if (!isUserGroupAsUserType || !isUserEnrolled(loId, instanceId, almAccessToken, almBaseUrl)) {
            return getTrainingAccountErrorMessages(updatedUserType);
        }

        return StringUtils.EMPTY;
    }

    /**
     * 
     * @param userMap
     * @param accountCredentialMap
     * @return
     */
    public static String handleUserEnrollment(Map<String, String> userMap, Map<String, String> accountCredentialMap) {

        String loId = accountCredentialMap.get("loId");
        String instanceId = accountCredentialMap.get("instanceId");
        String almAccessToken = accountCredentialMap.get("almAccessToken");
        String almAdminAccessToken = accountCredentialMap.get("almAdminToken");
        String almBaseUrl = accountCredentialMap.get("almBaseURL");
        String loResponseAsLearner = getLOResponse(loId, almAccessToken, almBaseUrl, true);
        String loResponseAsAdmin = getLOResponse(loId, almAdminAccessToken, almBaseUrl, true);

        if (StringUtils.isEmpty(loResponseAsAdmin)) {
            LOG.info("Course not accessible");
            return accountCredentialMap.get(CommonConstants.LO_ACCESS_ERR_MSG);
        }

        if (isLoRetired(loResponseAsAdmin)) {
            LOG.info("Course is retired");
            return accountCredentialMap.get(CommonConstants.LO_RETIRED_MSG);
        }

        if (isManagerNominated(loResponseAsAdmin)) {
            return getUserEnrolledInstance(loResponseAsLearner, loResponseAsAdmin);
        }

        if (StringUtils.isEmpty(loResponseAsLearner)) {
            LOG.info("Course not accessible");
            return accountCredentialMap.get(CommonConstants.LO_ACCESS_ERR_MSG);
        }

        if (isUserAlreadyEnrolled(almBaseUrl, userMap.get(CommonConstants.USER_ID), almAccessToken,
                instanceId)) {
            LOG.info("User Already Enrolled");
            return StringUtils.EMPTY;
        }

        if (isUserEnrolled(loId, instanceId, almAccessToken, almBaseUrl)) {
            LOG.info("User Enrolled");
            return StringUtils.EMPTY;
        }

        return accountCredentialMap.get(CommonConstants.LO_ACCESS_ERR_MSG);
    }

    /**
     * 
     * @param user
     * @return
     */
    public static Map<String, String> getUserDetails(JsonNode user) {
        JsonNode dataNode = user.path(CommonConstants.DATA);
        Map<String, String> userDetailsMap = new HashMap<>();
        userDetailsMap.put(CommonConstants.USER_ID, dataNode.path(CommonConstants.ID).asText());
        userDetailsMap.put(CommonConstants.USER_NAME,
                dataNode.path(CommonConstants.ATTRIBUTES)
                        .path(CommonConstants.NAME)
                        .asText());
        userDetailsMap.put(CommonConstants.USER_EMAIL,
                dataNode.path(CommonConstants.ATTRIBUTES)
                        .path(CommonConstants.EMAIL)
                        .asText());
        userDetailsMap.put(CommonConstants.USER_TYPE,
                dataNode.path(CommonConstants.ATTRIBUTES)
                        .path(CommonConstants.FIELDS)
                        .path(CommonConstants.USER_TYPE)
                        .asText());
        userDetailsMap.put(CommonConstants.UUID,
                dataNode.path(CommonConstants.ATTRIBUTES)
                        .path("metadata")
                        .path("uuid")
                        .asText());
        userDetailsMap.put("contentLocale",
                dataNode.path(CommonConstants.ATTRIBUTES)
                        .path("contentLocale")
                        .asText());
        userDetailsMap.put("uiLocale",
                dataNode.path(CommonConstants.ATTRIBUTES)
                        .path("uiLocale")
                        .asText());

        return userDetailsMap;
    }

    /**
     * 
     * @param loResponse
     * @return
     */
    public static boolean isLoRetired(String loResponse) {
        if (StringUtils.isEmpty(loResponse))
            return false;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(loResponse);
            String loState = rootNode.path(CommonConstants.DATA)
                    .path(CommonConstants.ATTRIBUTES)
                    .path("state")
                    .asText();
            LOG.info("LO State -> {}", loState);
            return loState.equalsIgnoreCase("Expired");
        } catch (JsonProcessingException e) {
            LOG.error("Error in processing JSON in isLoRetired() : {} {}", e.getMessage(), e);
        }
        return false;
    }

    /**
     * 
     * @param updatedUserType
     * @return
     */
    public static String getTrainingAccountErrorMessages(String updatedUserType) {
        if (updatedUserType.equalsIgnoreCase(CommonConstants.IBMER))
            return CommonConstants.IBMER_ACCESS_ERR;
        else if (updatedUserType.equalsIgnoreCase(CommonConstants.BUSINESS_PARTNER))
            return CommonConstants.BP_ACCESS_ERR;
        else
            return CommonConstants.CLIENT_ACCESS_ERR;
    }

    /**
     * 
     * @param almBaseUrl
     * @param userId
     * @param almAccessToken
     * @param instanceId
     * @return
     */
    public static boolean isUserAlreadyEnrolled(String almBaseUrl, String userId, String almAccessToken,
            String instanceId) {
        if (StringUtils.isAnyEmpty(instanceId, userId, almAccessToken)) {
            LOG.info("Either instanceId : {} userId : {} almAccessToken : {} are null or empty", instanceId, userId,
                    almAccessToken);
            return false;
        }

        Map<String, String> requestProperties = new HashMap<>();
        requestProperties.put(CommonConstants.ACCEPT, CommonConstants.APPLICATION_VND_API_JSON);
        requestProperties.put(CommonConstants.AUTHORIZATION, CommonConstants.OAUTH + " " + almAccessToken);

        String requestUrl = new StringBuilder().append(almBaseUrl)
                .append(CommonConstants.USER_ENROLLMENT_API_PATH)
                .append("/")
                .append(instanceId)
                .append("_")
                .append(userId)
                .toString();
        String isUserAlreadyEnrolled = HttpConnectionUtils.getResponseFromHttpRequest(requestUrl,
                CommonConstants.GET_METHOD, requestProperties, null);
        return StringUtils.isNotEmpty(isUserAlreadyEnrolled);
    }

    /**
     * 
     * @param loResponse
     * @return
     */
    public static boolean isManagerNominated(String loResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(loResponse);
            return rootNode.path("data")
                    .path("attributes")
                    .path("enrollmentType")
                    .asText()
                    .equalsIgnoreCase("Manager Nominated");
        } catch (JsonProcessingException e) {
            LOG.error("Error in processing JSON in isManagerNominated() : {} {}", e.getMessage(), e);
        }
        return false;
    }

    /**
     * 
     * @param loResponse
     * @param response
     * @return
     */
    public static String getUserEnrolledInstance(String loResponseAsLearner, String loResponseAsAdmin) {
        String authors = getAllAuthor(loResponseAsAdmin);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(loResponseAsLearner);
            String enrollmentId = rootNode.path("data").path("relationships").path("enrollment").path("data").path("id")
                    .asText();
            for (JsonNode instanceObj : rootNode.path("included")) {
                if (instanceObj.path("relationships").has("enrollment") && instanceObj.path("relationships")
                        .path("enrollment").path("data").path("id").asText().equals(enrollmentId)) {
                    if (instanceObj.path("attributes").path("state").asText().equalsIgnoreCase("Active")) {
                        return StringUtils.EMPTY;
                    } else {
                        return CommonConstants.INSTANCE_EXPIRED_ERR.replace("<Author>", authors);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Error in processing JSON in getUserEnrolledInstance() : {} {}", e.getMessage(), e);
        }
        return CommonConstants.MANAGER_NOMINATED_TRYING_SELF_ENROLL_ERR.replace("<Author>", authors);
    }

    /**
     * 
     * @param loResponse
     * @return
     */
    public static String getAllAuthor(String loResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(loResponse);
            JsonNode authorNameArrayNode = rootNode.path("data").path("attributes").path("authorNames");
            String[] authorNamesArray = new String[authorNameArrayNode.size()];
            if (authorNameArrayNode.size() == 1) {
                return authorNameArrayNode.get(0).asText();
            } else {
                for (int i = 0; i < authorNameArrayNode.size(); i++)
                    authorNamesArray[i] = authorNameArrayNode.get(i).asText();

                return String.join(", ", authorNamesArray);
            }
        } catch (Exception e) {
            LOG.error("Error in processing JSON in getAllAuthor() : {} {}", e.getMessage(), e);
        }
        return StringUtils.EMPTY;
    }

    /**
     * Returns response from LO API
     * 
     * @param loId          String
     * @param almAdminToken String
     * @param almEnvConfig  AlmEnvConfig
     * @return String
     */
    public static String getLOResponse(String loId, String almAdminToken, String almBaseUrl, Boolean included) {

        if (StringUtils.isAnyEmpty(loId, almAdminToken)) {
            LOG.info("Empty loid or Access token : loid -> {}, almAccessToken -> {}", loId, almAdminToken);
            return StringUtils.EMPTY;
        }
        Map<String, String> requestProperties = new HashMap<>();
        requestProperties.put(CommonConstants.ACCEPT, CommonConstants.APPLICATION_VND_API_JSON);
        requestProperties.put(CommonConstants.AUTHORIZATION, CommonConstants.OAUTH + " " + almAdminToken);

        String requestUrl = new StringBuilder().append(almBaseUrl)
                .append(CommonConstants.LO_API_PATH).append(loId)
                .toString();
        if (included) {
            requestUrl = new StringBuilder().append(requestUrl)
                    .append("?include=instances.loResources.resources")
                    .toString();
        }
        String loDataResponseString = HttpConnectionUtils.getResponseFromHttpRequest(requestUrl,
                CommonConstants.GET_METHOD, requestProperties, null);

        if (StringUtils.isNotEmpty(loDataResponseString))
            return loDataResponseString;
        else
            LOG.info("Failed to get LO data");

        return StringUtils.EMPTY;
    }

    /**
     * Returns only the desired fields from the LO response As Admin
     * 
     * @param loDataResponseString String
     * @return String
     */
    public static String filterLOResponseAsAdmin(String loDataResponseString) {

        if (StringUtils.isAnyEmpty(loDataResponseString)) {
            LOG.info("Empty loDataResponseString : loDataResponseString -> {}", loDataResponseString);
            return StringUtils.EMPTY;
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(loDataResponseString);
            JsonNode dataNode = rootNode.path(CommonConstants.DATA);
            JsonNode attributesNode = dataNode.path(CommonConstants.ATTRIBUTES);
            JsonNode stateNode = attributesNode.path("state");

            return new StringBuilder().append("{\"data\":{\"attributes\":{\"state\":")
                    .append(stateNode)
                    .append("}}}")
                    .toString();
        } catch (JsonProcessingException e) {
            LOG.error("Error in processing JSON in filterLOResponseAsAdmin() : {} {}", e.getMessage(), e);
        }
        return StringUtils.EMPTY;
    }

    /**
     * 
     * @param userMap
     * @param accountCredentialMap
     */
    public static void updateUserLocale(Map<String, String> userMap, Map<String, String> accountCredentialMap) {

        String languageCode = accountCredentialMap.get("languagecode");
        if (StringUtils.isAnyEmpty(accountCredentialMap.get("almAccessToken"), languageCode)
                || (userMap.get("uiLocale").equals(languageCode) && userMap.get("contentLocale").equals(languageCode))
                || !Arrays.asList(CommonConstants.LOCALE).contains(languageCode))
            return;

        String requestUrl = new StringBuilder().append(accountCredentialMap.get("almBaseURL"))
                .append(CommonConstants.USERS_API_PATH)
                .append(userMap.get(CommonConstants.USER_ID))
                .toString();

        Map<String, String> requestProperties = new HashMap<>();
        requestProperties.put(CommonConstants.CONTENT_TYPE,
                CommonConstants.APPLICATION_VND_API_JSON + ";charset=UTF-8");
        requestProperties.put(CommonConstants.ACCEPT, CommonConstants.APPLICATION_VND_API_JSON);
        requestProperties.put(CommonConstants.AUTHORIZATION,
                CommonConstants.OAUTH + " " + accountCredentialMap.get("almAccessToken"));
        requestProperties.put(CommonConstants.X_HTTP_METHOD_OVERRIDE, CommonConstants.PATCH);

        String requestBody = "{\"data\":{\"id\":\"" + userMap.get(CommonConstants.USER_ID)
                + "\",\"type\":\"user\",\"attributes\":{\"contentLocale\":\""
                + languageCode + "\",\"uiLocale\":\"" + languageCode + "\"}}}";

        String updateUserLocaleResponse = HttpConnectionUtils.getResponseFromHttpRequest(requestUrl,
                CommonConstants.POST_METHOD, requestProperties, requestBody);

        if (updateUserLocaleResponse.isEmpty())
            LOG.info("Failed to update user locale");
    }

}
