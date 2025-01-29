package com.adobe.learning.core.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.adobe.learning.core.config.AlmEnvConfig.Config;

@ExtendWith(MockitoExtension.class)
class AlmEnvConfigTest {

    @Mock
    private Config config;

    private AlmEnvConfig almEnvConfig;

    @BeforeEach
    void setup() {
        almEnvConfig = new AlmEnvConfig();
    }

    @Test
    void testGetBusinessPartnerAndClientAccessKeySandbox() {
        Mockito.when(config.businessPartnerAndClientAccessKeySandbox()).thenReturn("test-bp-accesskey-sandbox");
        almEnvConfig.activate(config);
        assertEquals("test-bp-accesskey-sandbox", almEnvConfig.getBusinessPartnerAndClientAccessKeySandbox());
    }

    @Test
    void testGetBusinessPartnerAndClientAccessKeyStage() {
        Mockito.when(config.businessPartnerAndClientAccessKeyStage()).thenReturn("test-bp-accesskey-stage");
        almEnvConfig.activate(config);
        assertEquals("test-bp-accesskey-stage", almEnvConfig.getBusinessPartnerAndClientAccessKeyStage());
    }

    @Test
    void testGetBusinessPartnerAndClientApplicationIdSandbox() {
        Mockito.when(config.businessPartnerAndClientApplicationIdSandbox()).thenReturn("test-bp-appId-sandbox");
        almEnvConfig.activate(config);
        assertEquals("test-bp-appId-sandbox", almEnvConfig.getBusinessPartnerAndClientApplicationIdSandbox());
    }

    @Test
    void testGetBusinessPartnerAndClientApplicationIdStage() {
        Mockito.when(config.businessPartnerAndClientApplicationIdStage()).thenReturn("test-bp-appId-stage");
        almEnvConfig.activate(config);
        assertEquals("test-bp-appId-stage", almEnvConfig.getBusinessPartnerAndClientApplicationIdStage());
    }

    @Test
    void testGetBusinessPartnerAndClientApplicationSecretSandbox() {
        Mockito.when(config.businessPartnerAndClientApplicationSecretSandbox()).thenReturn("test-bp-appsecret-sandbox");
        almEnvConfig.activate(config);
        assertEquals("test-bp-appsecret-sandbox", almEnvConfig.getBusinessPartnerAndClientApplicationSecretSandbox());
    }

    @Test
    void testGetBusinessPartnerAndClientApplicationSecretStage() {
        Mockito.when(config.businessPartnerAndClientApplicationSecretStage()).thenReturn("test-bp-appsecret-stage");
        almEnvConfig.activate(config);
        assertEquals("test-bp-appsecret-stage", almEnvConfig.getBusinessPartnerAndClientApplicationSecretStage());
    }

    @Test
    void testGetBusinessPartnerAndClientIpIdSandbox() {
        Mockito.when(config.businessPartnerAndClientIpIdSandbox()).thenReturn("test-bp-ipId-sandbox");
        almEnvConfig.activate(config);
        assertEquals("test-bp-ipId-sandbox", almEnvConfig.getBusinessPartnerAndClientIpIdSandbox());
    }

    @Test
    void testGetBusinessPartnerAndClientIpIdStage() {
        Mockito.when(config.businessPartnerAndClientIpIdStage()).thenReturn("test-bp-ipId-stage");
        almEnvConfig.activate(config);
        assertEquals("test-bp-ipId-stage", almEnvConfig.getBusinessPartnerAndClientIpIdStage());
    }

    @Test
    void testGetEmployeeAccessKeySandbox() {
        Mockito.when(config.employeeAccessKeySandbox()).thenReturn("test-employee-accesskey-sandbox");
        almEnvConfig.activate(config);
        assertEquals("test-employee-accesskey-sandbox", almEnvConfig.getEmployeeAccessKeySandbox());
    }

    @Test
    void testGetEmployeeAccessKeyStage() {
        Mockito.when(config.employeeAccessKeyStage()).thenReturn("test-employee-accesskey-stage");
        almEnvConfig.activate(config);
        assertEquals("test-employee-accesskey-stage", almEnvConfig.getEmployeeAccessKeyStage());
    }

    @Test
    void testGetEmployeeApplicationIdSandbox() {
        Mockito.when(config.employeeApplicationIdSandbox()).thenReturn("test-employee-appId-sandbox");
        almEnvConfig.activate(config);
        assertEquals("test-employee-appId-sandbox", almEnvConfig.getEmployeeApplicationIdSandbox());
    }

    @Test
    void testGetEmployeeApplicationIdStage() {
        Mockito.when(config.employeeApplicationIdStage()).thenReturn("test-employee-appId-stage");
        almEnvConfig.activate(config);
        assertEquals("test-employee-appId-stage", almEnvConfig.getEmployeeApplicationIdStage());
    }

    @Test
    void testGetEmployeeApplicationSecretSandbox() {
        Mockito.when(config.employeeApplicationSecretSandbox()).thenReturn("test-employee-appsecret-sandbox");
        almEnvConfig.activate(config);
        assertEquals("test-employee-appsecret-sandbox", almEnvConfig.getEmployeeApplicationSecretSandbox());
    }

    @Test
    void testGetEmployeeApplicationSecretStage() {
        Mockito.when(config.employeeApplicationSecretStage()).thenReturn("test-employee-appsecret-stage");
        almEnvConfig.activate(config);
        assertEquals("test-employee-appsecret-stage", almEnvConfig.getEmployeeApplicationSecretStage());
    }

    @Test
    void testGetEmployeeIpIdSandbox() {
        Mockito.when(config.employeeIpIdSandbox()).thenReturn("test-employee-ipId-sandbox");
        almEnvConfig.activate(config);
        assertEquals("test-employee-ipId-sandbox", almEnvConfig.getEmployeeIpIdSandbox());
    }

    @Test
    void testGetEmployeeIpIdStage() {
        Mockito.when(config.employeeIpIdStage()).thenReturn("test-employee-ipId-stage");
        almEnvConfig.activate(config);
        assertEquals("test-employee-ipId-stage", almEnvConfig.getEmployeeIpIdStage());
    }

    @Test
    void testGetRedirectUrlforCourseLaunchPage() {
        Mockito.when(config.redirectUrlforCourseLaunchPage()).thenReturn("test-publish-url");
        almEnvConfig.activate(config);
        assertEquals("test-publish-url", almEnvConfig.getRedirectUrlforCourseLaunchPage());
    }

    @Test
    void testGetSkillsBuildAccessKeySandbox() {
        Mockito.when(config.skillsBuildAccessKeySandbox()).thenReturn("test-skillsbuild-accesskey-sandbox");
        almEnvConfig.activate(config);
        assertEquals("test-skillsbuild-accesskey-sandbox", almEnvConfig.getSkillsBuildAccessKeySandbox());
    }

    @Test
    void testGetSkillsBuildAccessKeyStage() {
        Mockito.when(config.skillsBuildAccessKeyStage()).thenReturn("test-skillsbuild-accesskey-sandbox");
        almEnvConfig.activate(config);
        assertEquals("test-skillsbuild-accesskey-sandbox", almEnvConfig.getSkillsBuildAccessKeyStage());
    }

    @Test
    void testGetSkillsBuildAdultAccessKeySandbox() {
        Mockito.when(config.skillsBuildAdultAccessKeySandbox()).thenReturn("test-skillsbuildAdult-accesskey-sandbox");
        almEnvConfig.activate(config);
        assertEquals("test-skillsbuildAdult-accesskey-sandbox", almEnvConfig.getSkillsBuildAdultAccessKeySandbox());
    }

    @Test
    void testGetSkillsBuildAdultAccessKeyStage() {
        Mockito.when(config.skillsBuildAdultAccessKeyStage()).thenReturn("test-skillsbuildAdult-accesskey-stage");
        almEnvConfig.activate(config);
        assertEquals("test-skillsbuildAdult-accesskey-stage", almEnvConfig.getSkillsBuildAdultAccessKeyStage());
    }

    @Test
    void testGetSkillsBuildAdultApplicationIdSandbox() {
        Mockito.when(config.skillsBuildAdultApplicationIdSandbox()).thenReturn("test-skillsbuildAdult-appId-sandbox");
        almEnvConfig.activate(config);
        assertEquals("test-skillsbuildAdult-appId-sandbox", almEnvConfig.getSkillsBuildAdultApplicationIdSandbox());
    }

    @Test
    void testGetSkillsBuildAdultApplicationIdStage() {
        Mockito.when(config.skillsBuildAdultApplicationIdStage()).thenReturn("test-skillsbuildAdult-appId-stage");
        almEnvConfig.activate(config);
        assertEquals("test-skillsbuildAdult-appId-stage", almEnvConfig.getSkillsBuildAdultApplicationIdStage());
    }

    @Test
    void testGetSkillsBuildAdultApplicationSecretSandbox() {
        Mockito.when(config.skillsBuildAdultApplicationSecretSandbox())
                .thenReturn("test-skillsbuildAdult-appsecret-sandbox");
        almEnvConfig.activate(config);
        assertEquals("test-skillsbuildAdult-appsecret-sandbox",
                almEnvConfig.getSkillsBuildAdultApplicationSecretSandbox());
    }

    @Test
    void testGetSkillsBuildAdultApplicationSecretStage() {
        Mockito.when(config.skillsBuildAdultApplicationSecretStage())
                .thenReturn("test-skillsbuildAdult-appSecret-stage");
        almEnvConfig.activate(config);
        assertEquals("test-skillsbuildAdult-appSecret-stage", almEnvConfig.getSkillsBuildAdultApplicationSecretStage());
    }

    @Test
    void testGetSkillsBuildAdultIpIdSandbox() {
        Mockito.when(config.skillsBuildAdultIpIdSandbox()).thenReturn("test-skillsbuildAdult-ipId-sandbox");
        almEnvConfig.activate(config);
        assertEquals("test-skillsbuildAdult-ipId-sandbox", almEnvConfig.getSkillsBuildAdultIpIdSandbox());
    }

    @Test
    void testGetSkillsBuildAdultIpIdStage() {
        Mockito.when(config.skillsBuildAdultIpIdStage()).thenReturn("test-skillsbuildAdult-ipId-stage");
        almEnvConfig.activate(config);
        assertEquals("test-skillsbuildAdult-ipId-stage", almEnvConfig.getSkillsBuildAdultIpIdStage());
    }

    @Test
    void testGetSkillsBuildApplicationIdSandbox() {
        Mockito.when(config.skillsBuildApplicationIdSandbox()).thenReturn("test-skillsbuild-appId-sandbox");
        almEnvConfig.activate(config);
        assertEquals("test-skillsbuild-appId-sandbox", almEnvConfig.getSkillsBuildApplicationIdSandbox());
    }

    @Test
    void testGetSkillsBuildApplicationIdStage() {
        Mockito.when(config.skillsBuildApplicationIdStage()).thenReturn("test-skillsbuild-appId-stage");
        almEnvConfig.activate(config);
        assertEquals("test-skillsbuild-appId-stage", almEnvConfig.getSkillsBuildApplicationIdStage());
    }

    @Test
    void testGetSkillsBuildApplicationSecretSandbox() {
        Mockito.when(config.skillsBuildApplicationSecretSandbox()).thenReturn("test-skillsbuild-appsecret-sandbox");
        almEnvConfig.activate(config);
        assertEquals("test-skillsbuild-appsecret-sandbox", almEnvConfig.getSkillsBuildApplicationSecretSandbox());
    }

    @Test
    void testGetSkillsBuildApplicationSecretStage() {
        Mockito.when(config.skillsBuildApplicationSecretStage()).thenReturn("test-skillsbuild-appsecret-stage");
        almEnvConfig.activate(config);
        assertEquals("test-skillsbuild-appsecret-stage", almEnvConfig.getSkillsBuildApplicationSecretStage());
    }

    @Test
    void testGetSkillsBuildIpIdSandbox() {
        Mockito.when(config.skillsBuildIpIdSandbox()).thenReturn("test-skillsbuild-ipId-sandbox");
        almEnvConfig.activate(config);
        assertEquals("test-skillsbuild-ipId-sandbox", almEnvConfig.getSkillsBuildIpIdSandbox());
    }

    @Test
    void testGetSkillsBuildIpIdStage() {
        Mockito.when(config.skillsBuildIpIdStage()).thenReturn("test-skillsbuild-ipId-stage");
        almEnvConfig.activate(config);
        assertEquals("test-skillsbuild-ipId-stage", almEnvConfig.getSkillsBuildIpIdStage());
    }

    @Test
    void testGetBusinessPartnerAndClientRefreshTokenSandbox() {
        Mockito.when(config.businessPartnerAndClientRefreshTokenSandbox()).thenReturn("bp-refreshtoken-sandbox");
        almEnvConfig.activate(config);
        assertEquals("bp-refreshtoken-sandbox", almEnvConfig.getBusinessPartnerAndClientRefreshTokenSandbox());
    }

    @Test
    void testGetBusinessPartnerAndClientRefreshTokenStage() {
        Mockito.when(config.businessPartnerAndClientRefreshTokenStage()).thenReturn("bp-refreshtoken-stage");
        almEnvConfig.activate(config);
        assertEquals("bp-refreshtoken-stage", almEnvConfig.getBusinessPartnerAndClientRefreshTokenStage());
    }

    @Test
    void testGetJwtIssuer() {
        Mockito.when(config.jwtIssuer()).thenReturn("jwt-issuer");
        almEnvConfig.activate(config);
        assertEquals("jwt-issuer", almEnvConfig.getJwtIssuer());
    }

    @Test
    void testGetJwtSubject() {
        Mockito.when(config.jwtSubject()).thenReturn("jwt-subject");
        almEnvConfig.activate(config);
        assertEquals("jwt-subject", almEnvConfig.getJwtSubject());
    }

    @Test
    void testGetJwtAudience() {
        Mockito.when(config.jwtAudience()).thenReturn("jwt-audience");
        almEnvConfig.activate(config);
        assertEquals("jwt-audience", almEnvConfig.getJwtAudience());
    }

    @Test
    void jwtPrivateKey() {
        Mockito.when(config.jwtPrivateKey()).thenReturn("jwt-privatekey");
        almEnvConfig.activate(config);
        assertEquals("jwt-privatekey", almEnvConfig.jwtPrivateKey());
    }

    @Test
    void testGetEmployeeRefreshTokenSandbox() {
        Mockito.when(config.employeeRefreshTokenSandbox()).thenReturn("employeeRefreshtokenSandbox");
        almEnvConfig.activate(config);
        assertEquals("employeeRefreshtokenSandbox", almEnvConfig.getEmployeeRefreshTokenSandbox());
    }

    @Test
    void testGetSkillsBuildRefreshTokenSandbox() {
        Mockito.when(config.skillsBuildRefreshTokenSandbox()).thenReturn("skilsbuildRefreshtokenSandbox");
        almEnvConfig.activate(config);
        assertEquals("skilsbuildRefreshtokenSandbox", almEnvConfig.getSkillsBuildRefreshTokenSandbox());
    }

    @Test
    void testGetSkillsBuildAdultRefreshTokenSandbox() {
        Mockito.when(config.skillsBuildAdultRefreshTokenSandbox()).thenReturn("skilsbuildAdultRefreshtokenSandbox");
        almEnvConfig.activate(config);
        assertEquals("skilsbuildAdultRefreshtokenSandbox", almEnvConfig.getSkillsBuildAdultRefreshTokenSandbox());
    }

    @Test
    void testGetEmployeeRefreshTokenStage() {
        Mockito.when(config.employeeRefreshTokenStage()).thenReturn("employeeRefreshTokenStage");
        almEnvConfig.activate(config);
        assertEquals("employeeRefreshTokenStage", almEnvConfig.getEmployeeRefreshTokenStage());
    }

    @Test
    void testGetSkillsBuildRefreshTokenStage() {
        Mockito.when(config.skillsBuildRefreshTokenStage()).thenReturn("skilsbuildRefreshtokenStage");
        almEnvConfig.activate(config);
        assertEquals("skilsbuildRefreshtokenStage", almEnvConfig.getSkillsBuildRefreshTokenStage());
    }

    @Test
    void testGetSkillsBuildAdultRefreshTokenStage() {
        Mockito.when(config.skillsBuildAdultRefreshTokenStage()).thenReturn("skilsbuildAdultRefreshtokenStage");
        almEnvConfig.activate(config);
        assertEquals("skilsbuildAdultRefreshtokenStage", almEnvConfig.getSkillsBuildAdultRefreshTokenStage());
    }

    @Test
    void testGetPrmPpsAuthApiUrl() {
        Mockito.when(config.prmPpsAuthApiUrl()).thenReturn("prm-pps-auth-api-url");
        almEnvConfig.activate(config);
        assertEquals("prm-pps-auth-api-url", almEnvConfig.getPrmPpsAuthApiUrl());
    }

    @Test
    void testGetPrmPpsApiUrl() {
        Mockito.when(config.prmPpsApiUrl()).thenReturn("prm-pps-api-url");
        almEnvConfig.activate(config);
        assertEquals("prm-pps-api-url", almEnvConfig.getPrmPpsApiUrl());
    }

    @Test
    void testGetEmployeeApplicationId() {
        Mockito.when(config.employeeApplicationId()).thenReturn("employeeApplicationId");
        almEnvConfig.activate(config);
        assertEquals("employeeApplicationId", almEnvConfig.getEmployeeApplicationId());
    }

    @Test
    void testGetEmployeeApplicationSecret() {
        Mockito.when(config.employeeApplicationSecret()).thenReturn("employeeApplicationSecret");
        almEnvConfig.activate(config);
        assertEquals("employeeApplicationSecret", almEnvConfig.getEmployeeApplicationSecret());
    }

    @Test
    void testGetEmployeeIpId() {
        Mockito.when(config.employeeIpId()).thenReturn("employeeIpId");
        almEnvConfig.activate(config);
        assertEquals("employeeIpId", almEnvConfig.getEmployeeIpId());
    }

    @Test
    void testGetEmployeeAccessKey() {
        Mockito.when(config.employeeAccessKey()).thenReturn("employeeAccessKey");
        almEnvConfig.activate(config);
        assertEquals("employeeAccessKey", almEnvConfig.getEmployeeAccessKey());
    }

    @Test
    void testGetEmployeeRefreshToken() {
        Mockito.when(config.employeeRefreshToken()).thenReturn("employeeRefreshToken");
        almEnvConfig.activate(config);
        assertEquals("employeeRefreshToken", almEnvConfig.getEmployeeRefreshToken());
    }

    @Test
    void testGetBusinessPartnerAndClientApplicationId() {
        Mockito.when(config.businessPartnerAndClientApplicationId()).thenReturn("test-bp-appId");
        almEnvConfig.activate(config);
        assertEquals("test-bp-appId", almEnvConfig.getBusinessPartnerAndClientApplicationId());
    }

    @Test
    void testGetBusinessPartnerAndClientApplicationSecret() {
        Mockito.when(config.businessPartnerAndClientApplicationSecret()).thenReturn("test-bp-app-secret");
        almEnvConfig.activate(config);
        assertEquals("test-bp-app-secret", almEnvConfig.getBusinessPartnerAndClientApplicationSecret());
    }

    @Test
    void testGetBusinessPartnerAndClientIpId() {
        Mockito.when(config.businessPartnerAndClientIpId()).thenReturn("test-bp-ipId");
        almEnvConfig.activate(config);
        assertEquals("test-bp-ipId", almEnvConfig.getBusinessPartnerAndClientIpId());
    }

    @Test
    void testGetBusinessPartnerAndClientAccessKey() {
        Mockito.when(config.businessPartnerAndClientAccessKey()).thenReturn("test-bp-accessKey");
        almEnvConfig.activate(config);
        assertEquals("test-bp-accessKey", almEnvConfig.getBusinessPartnerAndClientAccessKey());
    }

    @Test
    void testGetBusinessPartnerAndClientRefreshToken() {
        Mockito.when(config.businessPartnerAndClientRefreshToken()).thenReturn("test-bp-refresh-token");
        almEnvConfig.activate(config);
        assertEquals("test-bp-refresh-token", almEnvConfig.getBusinessPartnerAndClientRefreshToken());
    }

    @Test
    void testGetSkillsBuildApplicationId() {
        Mockito.when(config.skillsBuildApplicationId()).thenReturn("test-skillsbuild-appId");
        almEnvConfig.activate(config);
        assertEquals("test-skillsbuild-appId", almEnvConfig.getSkillsBuildApplicationId());
    }

    @Test
    void testGetSkillsBuildApplicationSecret() {
        Mockito.when(config.skillsBuildApplicationSecret()).thenReturn("test-skillsbuild-applicationSecret");
        almEnvConfig.activate(config);
        assertEquals("test-skillsbuild-applicationSecret", almEnvConfig.getSkillsBuildApplicationSecret());
    }

    @Test
    void testGetSkillsBuildIpId() {
        Mockito.when(config.skillsBuildIpId()).thenReturn("test-skillsbuild-ipId");
        almEnvConfig.activate(config);
        assertEquals("test-skillsbuild-ipId", almEnvConfig.getSkillsBuildIpId());
    }

    @Test
    void testGetSkillsBuildAccessKey() {
        Mockito.when(config.skillsBuildAccessKey()).thenReturn("test-skillsbuild-accesskey");
        almEnvConfig.activate(config);
        assertEquals("test-skillsbuild-accesskey", almEnvConfig.getSkillsBuildAccessKey());
    }

    @Test
    void testGetSkillsBuildRefreshToken() {
        Mockito.when(config.skillsBuildRefreshToken()).thenReturn("test-skillsbuild-refreshToken");
        almEnvConfig.activate(config);
        assertEquals("test-skillsbuild-refreshToken", almEnvConfig.getSkillsBuildRefreshToken());
    }

    @Test
    void testGetSkillsBuildAdultApplicationId() {
        Mockito.when(config.skillsBuildAdultApplicationId()).thenReturn("test-skillsbuildAdult-applicationId");
        almEnvConfig.activate(config);
        assertEquals("test-skillsbuildAdult-applicationId", almEnvConfig.getSkillsBuildAdultApplicationId());
    }

    @Test
    void testGetSkillsBuildAdultApplicationSecret() {
        Mockito.when(config.skillsBuildAdultApplicationSecret()).thenReturn("test-skillsbuildAdult-applicationSecret");
        almEnvConfig.activate(config);
        assertEquals("test-skillsbuildAdult-applicationSecret", almEnvConfig.getSkillsBuildAdultApplicationSecret());
    }

    @Test
    void testGetSkillsBuildAdultIpId() {
        Mockito.when(config.skillsBuildAdultIpId()).thenReturn("test-skillsbuildAdult-ipId");
        almEnvConfig.activate(config);
        assertEquals("test-skillsbuildAdult-ipId", almEnvConfig.getSkillsBuildAdultIpId());
    }

    @Test
    void testGetSkillsBuildAdultAccessKey() {
        Mockito.when(config.skillsBuildAdultAccessKey()).thenReturn("test-skillsbuildAdult-accessKey");
        almEnvConfig.activate(config);
        assertEquals("test-skillsbuildAdult-accessKey", almEnvConfig.getSkillsBuildAdultAccessKey());
    }

    @Test
    void testGetSkillsBuildAdultRefreshToken() {
        Mockito.when(config.skillsBuildAdultRefreshToken()).thenReturn("test-skillsbuildAdult-refreshToken");
        almEnvConfig.activate(config);
        assertEquals("test-skillsbuildAdult-refreshToken", almEnvConfig.getSkillsBuildAdultRefreshToken());
    }

    @Test
    void testAlmApiBaseUrl() {
        Mockito.when(config.almApiBaseUrl()).thenReturn("test-url");
        almEnvConfig.activate(config);
        assertEquals("test-url", almEnvConfig.getAlmApiBaseUrl());
    }

    @Test
    void testUuidAccessTokenUsername() {
        Mockito.when(config.uuidAccessTokenUsername()).thenReturn("test-uuid-username");
        almEnvConfig.activate(config);
        assertEquals("test-uuid-username", almEnvConfig.getUuidAccessTokenUsername());
    }

    @Test
    void testUuidAccessTokenPassword() {
        Mockito.when(config.uuidAccessTokenPassword()).thenReturn("test-uuid-password");
        almEnvConfig.activate(config);
        assertEquals("test-uuid-password", almEnvConfig.getUuidAccessTokenPassword());
    }

    @Test
    void testUuidAccessTokenApiurl() {
        Mockito.when(config.uuidAccessTokenApiUrl()).thenReturn("test-uuid-api-url");
        almEnvConfig.activate(config);
        assertEquals("test-uuid-api-url", almEnvConfig.getUuidAccessTokenApiUrl());
    }

    @Test
    void testUuidApiUrl() {
        Mockito.when(config.uuidApiUrl()).thenReturn("test-uuid-api-url");
        almEnvConfig.activate(config);
        assertEquals("test-uuid-api-url", almEnvConfig.getUuidApiUrl());
    }

    @Test
    void testGetRedirectUrlforCourseDetailPage() {
        Mockito.when(config.redirectUrlforCourseDetailPage()).thenReturn("test-url");
        almEnvConfig.activate(config);
        assertEquals("test-url", almEnvConfig.getRedirectUrlforCourseDetailPage());
    }

    @Test
    void testGetPreHireApplicationIdSandbox() {
        Mockito.when(config.preHireApplicationIdSandbox()).thenReturn("app-id");
        almEnvConfig.activate(config);
        assertEquals("app-id", almEnvConfig.getPreHireApplicationIdSandbox());
    }

    @Test
    void testGetPreHireApplicationSecretSandbox() {
        Mockito.when(config.preHireApplicationSecretSandbox()).thenReturn("app-secret");
        almEnvConfig.activate(config);
        assertEquals("app-secret", almEnvConfig.getPreHireApplicationSecretSandbox());
    }

    @Test
    void testGetPreHireRefreshTokenSandbox() {
        Mockito.when(config.preHireRefreshTokenSandbox()).thenReturn("refresh-token");
        almEnvConfig.activate(config);
        assertEquals("refresh-token", almEnvConfig.getPreHireRefreshTokenSandbox());
    }

    @Test
    void testGetPreHireApplicationIdStage() {
        Mockito.when(config.preHireApplicationIdStage()).thenReturn("app-id");
        almEnvConfig.activate(config);
        assertEquals("app-id", almEnvConfig.getPreHireApplicationIdStage());
    }

    @Test
    void testGetPreHireApplicationSecretStage() {
        Mockito.when(config.preHireApplicationSecretStage()).thenReturn("app-secret");
        almEnvConfig.activate(config);
        assertEquals("app-secret", almEnvConfig.getPreHireApplicationSecretStage());
    }

    @Test
    void testGetPreHireRefreshTokenStage() {
        Mockito.when(config.preHireRefreshTokenStage()).thenReturn("refresh-token");
        almEnvConfig.activate(config);
        assertEquals("refresh-token", almEnvConfig.getPreHireRefreshTokenStage());
    }

    @Test
    void testGetPreHireApplicationId() {
        Mockito.when(config.preHireApplicationId()).thenReturn("app-id");
        almEnvConfig.activate(config);
        assertEquals("app-id", almEnvConfig.getPreHireApplicationId());
    }

    @Test
    void testGetPreHireApplicationSecret() {
        Mockito.when(config.preHireApplicationSecret()).thenReturn("app-secret");
        almEnvConfig.activate(config);
        assertEquals("app-secret", almEnvConfig.getPreHireApplicationSecret());
    }

    @Test
    void testGetPreHireRefreshToken() {
        Mockito.when(config.preHireRefreshToken()).thenReturn("refresh-token");
        almEnvConfig.activate(config);
        assertEquals("refresh-token", almEnvConfig.getPreHireRefreshToken());
    }

}
