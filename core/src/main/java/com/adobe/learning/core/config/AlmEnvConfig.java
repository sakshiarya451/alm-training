package com.adobe.learning.core.config;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@Component(service = AlmEnvConfig.class, immediate = true)
@Designate(ocd = AlmEnvConfig.Config.class)
public class AlmEnvConfig {

    @ObjectClassDefinition(name = "ALM Environment Configuration", description = "Configurations for managing Application Id and Application Secret for ALM Sandbox and Stage Environments")
    public @interface Config {

        @AttributeDefinition(name = "Redirect URL for Course Launch Page", description = "Redirect URL for Course Launch page")
        String redirectUrlforCourseLaunchPage();

        @AttributeDefinition(name = "Redirect URL for Course Detail Page", description = "Redirect URL for Course Detail page")
        String redirectUrlforCourseDetailPage();

        /* ALM Sanbox accounts config */

        @AttributeDefinition(name = "Employee Application Id Sandbox", description = "Application Id of Employee Sandbox ALM Env")
        String employeeApplicationIdSandbox();

        @AttributeDefinition(name = "Employee Application Secret Sandbox", description = "Application Secret of Employee Sandbox ALM Env")
        String employeeApplicationSecretSandbox();

        @AttributeDefinition(name = "Employee IP ID Sandbox", description = "IP ID of Employee Sandbox ALM Env")
        String employeeIpIdSandbox();

        @AttributeDefinition(name = "Employee Access Key Sandbox", description = "Access Key of Employee Sandbox ALM Env")
        String employeeAccessKeySandbox();

        @AttributeDefinition(name = "Employee Refresh Token Sandbox", description = "Refresh Token of Employee Sandbox ALM Env")
        String employeeRefreshTokenSandbox();

        @AttributeDefinition(name = "Business Partner and Client Application Id Sandbox", description = "Application Id of Business Partner and Client Sandbox ALM Env")
        String businessPartnerAndClientApplicationIdSandbox();

        @AttributeDefinition(name = "Business Partner and Client Application Secret Sandbox", description = "Application Secret of Business Partner and Client Sandbox ALM Env")
        String businessPartnerAndClientApplicationSecretSandbox();

        @AttributeDefinition(name = "Business Partner and Client IP ID Sandbox", description = "IP ID of Business Partner and Client Sandbox ALM Env")
        String businessPartnerAndClientIpIdSandbox();

        @AttributeDefinition(name = "Business Partner and Client Access Key Sandbox", description = "Access Key of Business Partner and Client Sandbox ALM Env")
        String businessPartnerAndClientAccessKeySandbox();

        @AttributeDefinition(name = "Business Partner and Client Refresh Token Sandbox", description = "Refresh Token of Business Partner and Client Sandbox ALM Env")
        String businessPartnerAndClientRefreshTokenSandbox();

        @AttributeDefinition(name = "SkillsBuild Application Id Sandbox", description = "Application Id of SkillsBuild Sandbox ALM Env")
        String skillsBuildApplicationIdSandbox();

        @AttributeDefinition(name = "SkillsBuild Application Secret Sandbox", description = "Application Secret of SkillsBuild Sandbox ALM Env")
        String skillsBuildApplicationSecretSandbox();

        @AttributeDefinition(name = "SkillsBuild IP ID Sandbox", description = "IP ID of SkillsBuild Sandbox ALM Env")
        String skillsBuildIpIdSandbox();

        @AttributeDefinition(name = "SkillsBuild Access Key Sandbox", description = "Access Key of SkillsBuild Sandbox ALM Env")
        String skillsBuildAccessKeySandbox();

        @AttributeDefinition(name = "SkillsBuild Refresh Token Sandbox", description = "Refresh Token of SkillsBuild Sandbox ALM Env")
        String skillsBuildRefreshTokenSandbox();

        @AttributeDefinition(name = "SkillsBuild Adult Application Id Sandbox", description = "Application Id of SkillsBuild Adult Sandbox ALM Env")
        String skillsBuildAdultApplicationIdSandbox();

        @AttributeDefinition(name = "SkillsBuild Adult Application Secret Sandbox", description = "Application Secret of SkillsBuild Adult Sandbox ALM Env")
        String skillsBuildAdultApplicationSecretSandbox();

        @AttributeDefinition(name = "SkillsBuild Adult IP ID Sandbox", description = "IP ID of SkillsBuild Adult Sandbox ALM Env")
        String skillsBuildAdultIpIdSandbox();

        @AttributeDefinition(name = "SkillsBuild Adult Access Key Sandbox", description = "Access Key of SkillsBuild Adult Sandbox ALM Env")
        String skillsBuildAdultAccessKeySandbox();

        @AttributeDefinition(name = "SkillsBuild Adult Refresh Token Sandbox", description = "Refresh Token of SkillsBuild Adult Sandbox ALM Env")
        String skillsBuildAdultRefreshTokenSandbox();

        @AttributeDefinition(name = "Prehire Application ID Sandbox", description = "Application ID of Prehire  Sandbox ALM Env")
        String preHireApplicationIdSandbox();

        @AttributeDefinition(name = "Prehire Application Secret Sandbox", description = "Application Secret of Prehire  Sandbox ALM Env")
        String preHireApplicationSecretSandbox();

        @AttributeDefinition(name = "Prehire Refresh Token Sandbox", description = "Refresh token of Prehire  Sandbox ALM Env")
        String preHireRefreshTokenSandbox();

        /* ALM Stage accounts config */

        @AttributeDefinition(name = "Employee Application Id Stage", description = "Application Id of Employee Stage ALM Env")
        String employeeApplicationIdStage();

        @AttributeDefinition(name = "Employee Application Secret Stage", description = "Application Secret of Employee Stage ALM Env")
        String employeeApplicationSecretStage();

        @AttributeDefinition(name = "Employee IP ID Stage", description = "IP ID of Employee Stage ALM Env")
        String employeeIpIdStage();

        @AttributeDefinition(name = "Employee Access Key Stage", description = "Access Key of Employee Stage ALM Env")
        String employeeAccessKeyStage();

        @AttributeDefinition(name = "Employee Refresh Token Stage", description = "Refresh Token of Employee Stage ALM Env")
        String employeeRefreshTokenStage();

        @AttributeDefinition(name = "Business Partner and Client Application Id Stage", description = "Application Id of Business Partner and Client Stage ALM Env")
        String businessPartnerAndClientApplicationIdStage();

        @AttributeDefinition(name = "Business Partner and Client Application Secret Stage", description = "Application Secret of Business Partner and Client Stage ALM Env")
        String businessPartnerAndClientApplicationSecretStage();

        @AttributeDefinition(name = "Business Partner and Client IP ID Stage", description = "IP ID of Business Partner and Client Stage ALM Env")
        String businessPartnerAndClientIpIdStage();

        @AttributeDefinition(name = "Business Partner and Client Access Key Stage", description = "Access Key of Business Partner and Client Stage ALM Env")
        String businessPartnerAndClientAccessKeyStage();

        @AttributeDefinition(name = "Business Partner and Client Refresh Token Stage", description = "Refresh Token of Business Partner and Client Stage ALM Env")
        String businessPartnerAndClientRefreshTokenStage();

        @AttributeDefinition(name = "SkillsBuild Application Id Stage", description = "Application Id of SkillsBuild Stage ALM Env")
        String skillsBuildApplicationIdStage();

        @AttributeDefinition(name = "SkillsBuild Application Secret Stage", description = "Application Secret of SkillsBuild Stage ALM Env")
        String skillsBuildApplicationSecretStage();

        @AttributeDefinition(name = "SkillsBuild IP ID Stage", description = "IP ID of SkillsBuild Stage ALM Env")
        String skillsBuildIpIdStage();

        @AttributeDefinition(name = "SkillsBuild Access Key Stage", description = "Access Key of SkillsBuild Stage ALM Env")
        String skillsBuildAccessKeyStage();

        @AttributeDefinition(name = "SkillsBuild Refresh Token Stage", description = "Refresh Token of SkillsBuild Stage ALM Env")
        String skillsBuildRefreshTokenStage();

        @AttributeDefinition(name = "SkillsBuild Adult Application Id Stage", description = "Application Id of SkillsBuild Adult Stage ALM Env")
        String skillsBuildAdultApplicationIdStage();

        @AttributeDefinition(name = "SkillsBuild Adult Application Secret Stage", description = "Application Secret of SkillsBuild Adult Stage ALM Env")
        String skillsBuildAdultApplicationSecretStage();

        @AttributeDefinition(name = "SkillsBuild Adult IP ID Stage", description = "IP ID of SkillsBuild Adult Stage ALM Env")
        String skillsBuildAdultIpIdStage();

        @AttributeDefinition(name = "SkillsBuild Adult Access Key Stage", description = "Access Key of SkillsBuild Adult Stage ALM Env")
        String skillsBuildAdultAccessKeyStage();

        @AttributeDefinition(name = "SkillsBuild Adult Refresh Token Stage", description = "Refresh Token of SkillsBuild Adult Stage ALM Env")
        String skillsBuildAdultRefreshTokenStage();

        @AttributeDefinition(name = "Prehire Application ID Stage", description = "Application ID of Prehire  Stage ALM Env")
        String preHireApplicationIdStage();

        @AttributeDefinition(name = "Prehire Application Secret Stage", description = "Application Secret of Prehire  Stage ALM Env")
        String preHireApplicationSecretStage();

        @AttributeDefinition(name = "Prehire Refresh Token Stage", description = "Refresh token of Prehire  Stage ALM Env")
        String preHireRefreshTokenStage();

        /* ALM Prod accounts config */

        @AttributeDefinition(name = "Employee Application Id Prod", description = "Application Id of Employee Prod ALM Env")
        String employeeApplicationId();

        @AttributeDefinition(name = "Employee Application Secret Prod", description = "Application Secret of Employee Prod ALM Env")
        String employeeApplicationSecret();

        @AttributeDefinition(name = "Employee IP ID Prod", description = "IP ID of Employee Prod ALM Env")
        String employeeIpId();

        @AttributeDefinition(name = "Employee Access Key Prod", description = "Access Key of Employee Prod ALM Env")
        String employeeAccessKey();

        @AttributeDefinition(name = "Employee Refresh Token Prod", description = "Refresh Token of Employee Prod ALM Env")
        String employeeRefreshToken();

        @AttributeDefinition(name = "Business Partner and Client Application Id Prod", description = "Application Id of Business Partner and Client Prod ALM Env")
        String businessPartnerAndClientApplicationId();

        @AttributeDefinition(name = "Business Partner and Client Application Secret Prod", description = "Application Secret of Business Partner and Client Prod ALM Env")
        String businessPartnerAndClientApplicationSecret();

        @AttributeDefinition(name = "Business Partner and Client IP ID Prod", description = "IP ID of Business Partner and Client Prod ALM Env")
        String businessPartnerAndClientIpId();

        @AttributeDefinition(name = "Business Partner and Client Access Key Prod", description = "Access Key of Business Partner and Client Prod ALM Env")
        String businessPartnerAndClientAccessKey();

        @AttributeDefinition(name = "Business Partner and Client Refresh Token Prod", description = "Refresh Token of Business Partner and Client Prod ALM Env")
        String businessPartnerAndClientRefreshToken();

        @AttributeDefinition(name = "SkillsBuild Application Id Prod", description = "Application Id of SkillsBuild Prod ALM Env")
        String skillsBuildApplicationId();

        @AttributeDefinition(name = "SkillsBuild Application Secret Prod", description = "Application Secret of SkillsBuild Prod ALM Env")
        String skillsBuildApplicationSecret();

        @AttributeDefinition(name = "SkillsBuild IP ID Prod", description = "IP ID of SkillsBuild Prod ALM Env")
        String skillsBuildIpId();

        @AttributeDefinition(name = "SkillsBuild Access Key Prod", description = "Access Key of SkillsBuild Prod ALM Env")
        String skillsBuildAccessKey();

        @AttributeDefinition(name = "SkillsBuild Refresh Token Prod", description = "Refresh Token of SkillsBuild Prod ALM Env")
        String skillsBuildRefreshToken();

        @AttributeDefinition(name = "SkillsBuild Adult Application Id Prod", description = "Application Id of SkillsBuild Adult Prod ALM Env")
        String skillsBuildAdultApplicationId();

        @AttributeDefinition(name = "SkillsBuild Adult Application Secret Prod", description = "Application Secret of SkillsBuild Adult Prod ALM Env")
        String skillsBuildAdultApplicationSecret();

        @AttributeDefinition(name = "SkillsBuild Adult IP ID Prod", description = "IP ID of SkillsBuild Adult Prod ALM Env")
        String skillsBuildAdultIpId();

        @AttributeDefinition(name = "SkillsBuild Adult Access Key Prod", description = "Access Key of SkillsBuild Adult Prod ALM Env")
        String skillsBuildAdultAccessKey();

        @AttributeDefinition(name = "SkillsBuild Adult Refresh Token Prod", description = "Refresh Token of SkillsBuild Adult Prod ALM Env")
        String skillsBuildAdultRefreshToken();

        @AttributeDefinition(name = "Prehire Application ID", description = "Application ID of Prehire ALM Env")
        String preHireApplicationId();

        @AttributeDefinition(name = "Prehire Application Secret", description = "Application Secret of Prehire ALM Env")
        String preHireApplicationSecret();

        @AttributeDefinition(name = "Prehire Refresh Token", description = "Refresh token of Prehire ALM Env")
        String preHireRefreshToken();

        /** End of ALM configs */

        /* PRM Partner Configs */

        @AttributeDefinition(name = "JWT issuer", description = "JWT issuer for PRM partner access token")
        String jwtIssuer();

        @AttributeDefinition(name = "JWT subject", description = "JWT subject for PRM partner access token")
        String jwtSubject();

        @AttributeDefinition(name = "JWT audience", description = "JWT audience for PRM partner access token")
        String jwtAudience();

        @AttributeDefinition(name = "JWT Private Key", description = "JWT Private key for PRM partner access token")
        String jwtPrivateKey();

        @AttributeDefinition(name = "PRM PPS AUTH API URL", description = "PRM PPS AUTH API URL")
        String prmPpsAuthApiUrl();

        @AttributeDefinition(name = "PRM PPS API URL", description = "PRM PPS API URL")
        String prmPpsApiUrl();

        /* ALM API Base URL */
        @AttributeDefinition(name = "ALM API Base URL", description = "ALM API Base URL")
        String almApiBaseUrl();

        /* UUID API Config */
        @AttributeDefinition(name = "UUID Access token username", description = "UUID Access token username")
        String uuidAccessTokenUsername();

        @AttributeDefinition(name = "UUID Access token password", description = "UUID Access token password")
        String uuidAccessTokenPassword();

        @AttributeDefinition(name = "UUID Access token API Url", description = "UUID Access token API Url")
        String uuidAccessTokenApiUrl();

        @AttributeDefinition(name = "UUID API Url")
        String uuidApiUrl();

        /* STart at IBM - Privacy acceptance */
        @AttributeDefinition(name = "Privacy acceptance token API")
        String privacyAcceptanceTokenApi();

        @AttributeDefinition(name = "Privacy accpetance API")
        String privacyAcceptanceApi();

        @AttributeDefinition(name = "Privacy acceptance Functional ID")
        String privacyAcceptanceFunctionalId();

        @AttributeDefinition(name = "Privacy acceptance Private Key")
        String privacyAcceptancePrivateKey();

    }

    private String redirectUrlforCourseLaunchPage;

    private String redirectUrlforCourseDetailPage;

    private String employeeApplicationIdSandbox;

    private String employeeApplicationSecretSandbox;

    private String employeeIpIdSandbox;

    private String employeeAccessKeySandbox;

    private String employeeRefreshTokenSandbox;

    private String businessPartnerAndClientApplicationIdSandbox;

    private String businessPartnerAndClientApplicationSecretSandbox;

    private String businessPartnerAndClientIpIdSandbox;

    private String businessPartnerAndClientAccessKeySandbox;

    private String businessPartnerAndClientRefreshTokenSandbox;

    private String skillsBuildApplicationIdSandbox;

    private String skillsBuildApplicationSecretSandbox;

    private String skillsBuildIpIdSandbox;

    private String skillsBuildAccessKeySandbox;

    private String skillsBuildRefreshTokenSandbox;

    private String skillsBuildAdultApplicationIdSandbox;

    private String skillsBuildAdultApplicationSecretSandbox;

    private String skillsBuildAdultIpIdSandbox;

    private String skillsBuildAdultAccessKeySandbox;

    private String skillsBuildAdultRefreshTokenSandbox;

    private String preHireApplicationIdSandbox;

    private String preHireApplicationSecretSandbox;

    private String preHireRefreshTokenSandbox;

    private String preHireApplicationIdStage;

    private String preHireApplicationSecretStage;

    private String preHireRefreshTokenStage;

    private String preHireApplicationId;

    private String preHireApplicationSecret;

    private String preHireRefreshToken;

    private String employeeApplicationIdStage;

    private String employeeApplicationSecretStage;

    private String employeeIpIdStage;

    private String employeeAccessKeyStage;

    private String employeeRefreshTokenStage;

    private String businessPartnerAndClientApplicationIdStage;

    private String businessPartnerAndClientApplicationSecretStage;

    private String businessPartnerAndClientIpIdStage;

    private String businessPartnerAndClientAccessKeyStage;

    private String businessPartnerAndClientRefreshTokenStage;

    private String skillsBuildApplicationIdStage;

    private String skillsBuildApplicationSecretStage;

    private String skillsBuildIpIdStage;

    private String skillsBuildAccessKeyStage;

    private String skillsBuildRefreshTokenStage;

    private String skillsBuildAdultApplicationIdStage;

    private String skillsBuildAdultApplicationSecretStage;

    private String skillsBuildAdultIpIdStage;

    private String skillsBuildAdultAccessKeyStage;

    private String skillsBuildAdultRefreshTokenStage;

    private String employeeApplicationId;

    private String employeeApplicationSecret;

    private String employeeIpId;

    private String employeeAccessKey;

    private String employeeRefreshToken;

    private String businessPartnerAndClientApplicationId;

    private String businessPartnerAndClientApplicationSecret;

    private String businessPartnerAndClientIpId;

    private String businessPartnerAndClientAccessKey;

    private String businessPartnerAndClientRefreshToken;

    private String skillsBuildApplicationId;

    private String skillsBuildApplicationSecret;

    private String skillsBuildIpId;

    private String skillsBuildAccessKey;

    private String skillsBuildRefreshToken;

    private String skillsBuildAdultApplicationId;

    private String skillsBuildAdultApplicationSecret;

    private String skillsBuildAdultIpId;

    private String skillsBuildAdultAccessKey;

    private String skillsBuildAdultRefreshToken;

    private String jwtIssuer;

    private String jwtSubject;

    private String jwtAudience;

    private String jwtPrivateKey;

    private String prmPpsAuthApiUrl;

    private String prmPpsApiUrl;

    private String almApiBaseUrl;

    private String uuidAccessTokenUsername;

    private String uuidAccessTokenPassword;

    private String uuidAccessTokenApiUrl;

    private String uuidApiUrl;

    private String privacyAcceptanceTokenApi;

    private String privacyAcceptanceApi;

    private String privacyAcceptanceFunctionalId;

    private String privacyAcceptancePrivateKey;

    @Activate
    @Modified
    protected void activate(final Config config) {
        this.redirectUrlforCourseLaunchPage = config.redirectUrlforCourseLaunchPage();
        this.redirectUrlforCourseDetailPage = config.redirectUrlforCourseDetailPage();
        /* ALM Prod */
        this.employeeApplicationIdSandbox = config.employeeApplicationIdSandbox();
        this.employeeApplicationSecretSandbox = config.employeeApplicationSecretSandbox();
        this.employeeIpIdSandbox = config.employeeIpIdSandbox();
        this.employeeAccessKeySandbox = config.employeeAccessKeySandbox();
        this.employeeRefreshTokenSandbox = config.employeeRefreshTokenSandbox();
        this.businessPartnerAndClientApplicationIdSandbox = config.businessPartnerAndClientApplicationIdSandbox();
        this.businessPartnerAndClientApplicationSecretSandbox = config
                .businessPartnerAndClientApplicationSecretSandbox();
        this.businessPartnerAndClientIpIdSandbox = config.businessPartnerAndClientIpIdSandbox();
        this.businessPartnerAndClientAccessKeySandbox = config.businessPartnerAndClientAccessKeySandbox();
        this.businessPartnerAndClientRefreshTokenSandbox = config.businessPartnerAndClientRefreshTokenSandbox();
        this.skillsBuildApplicationIdSandbox = config.skillsBuildApplicationIdSandbox();
        this.skillsBuildApplicationSecretSandbox = config.skillsBuildApplicationSecretSandbox();
        this.skillsBuildIpIdSandbox = config.skillsBuildIpIdSandbox();
        this.skillsBuildAccessKeySandbox = config.skillsBuildAccessKeySandbox();
        this.skillsBuildRefreshTokenSandbox = config.skillsBuildRefreshTokenSandbox();
        this.skillsBuildAdultApplicationIdSandbox = config.skillsBuildAdultApplicationIdSandbox();
        this.skillsBuildAdultApplicationSecretSandbox = config.skillsBuildAdultApplicationSecretSandbox();
        this.skillsBuildAdultIpIdSandbox = config.skillsBuildAdultIpIdSandbox();
        this.skillsBuildAdultAccessKeySandbox = config.skillsBuildAdultAccessKeySandbox();
        this.skillsBuildAdultRefreshTokenSandbox = config.skillsBuildAdultRefreshTokenSandbox();
        this.preHireApplicationIdSandbox = config.preHireApplicationIdSandbox();
        this.preHireApplicationSecretSandbox = config.preHireApplicationSecretSandbox();
        this.preHireRefreshTokenSandbox = config.preHireRefreshTokenSandbox();
        /* ALM Stage */
        this.preHireApplicationIdStage = config.preHireApplicationIdStage();
        this.preHireApplicationSecretStage = config.preHireApplicationSecretStage();
        this.preHireRefreshTokenStage = config.preHireRefreshTokenStage();
        this.employeeApplicationIdStage = config.employeeApplicationIdStage();
        this.employeeApplicationSecretStage = config.employeeApplicationSecretStage();
        this.employeeIpIdStage = config.employeeIpIdStage();
        this.employeeAccessKeyStage = config.employeeAccessKeyStage();
        this.employeeRefreshTokenStage = config.employeeRefreshTokenStage();
        this.businessPartnerAndClientApplicationIdStage = config.businessPartnerAndClientApplicationIdStage();
        this.businessPartnerAndClientApplicationSecretStage = config.businessPartnerAndClientApplicationSecretStage();
        this.businessPartnerAndClientIpIdStage = config.businessPartnerAndClientIpIdStage();
        this.businessPartnerAndClientAccessKeyStage = config.businessPartnerAndClientAccessKeyStage();
        this.businessPartnerAndClientRefreshTokenStage = config.businessPartnerAndClientRefreshTokenStage();
        this.skillsBuildApplicationIdStage = config.skillsBuildApplicationIdStage();
        this.skillsBuildApplicationSecretStage = config.skillsBuildApplicationSecretStage();
        this.skillsBuildIpIdStage = config.skillsBuildIpIdStage();
        this.skillsBuildAccessKeyStage = config.skillsBuildAccessKeyStage();
        this.skillsBuildRefreshTokenStage = config.skillsBuildRefreshTokenStage();
        this.skillsBuildAdultApplicationIdStage = config.skillsBuildAdultApplicationIdStage();
        this.skillsBuildAdultApplicationSecretStage = config.skillsBuildAdultApplicationSecretStage();
        this.skillsBuildAdultIpIdStage = config.skillsBuildAdultIpIdStage();
        this.skillsBuildAdultAccessKeyStage = config.skillsBuildAdultAccessKeyStage();
        this.skillsBuildAdultRefreshTokenStage = config.skillsBuildAdultRefreshTokenStage();
        /* ALM Prod */
        this.preHireApplicationId = config.preHireApplicationId();
        this.preHireApplicationSecret = config.preHireApplicationSecret();
        this.preHireRefreshToken = config.preHireRefreshToken();
        this.employeeApplicationId = config.employeeApplicationId();
        this.employeeApplicationSecret = config.employeeApplicationSecret();
        this.employeeIpId = config.employeeIpId();
        this.employeeAccessKey = config.employeeAccessKey();
        this.employeeRefreshToken = config.employeeRefreshToken();
        this.businessPartnerAndClientApplicationId = config.businessPartnerAndClientApplicationId();
        this.businessPartnerAndClientApplicationSecret = config.businessPartnerAndClientApplicationSecret();
        this.businessPartnerAndClientIpId = config.businessPartnerAndClientIpId();
        this.businessPartnerAndClientAccessKey = config.businessPartnerAndClientAccessKey();
        this.businessPartnerAndClientRefreshToken = config.businessPartnerAndClientRefreshToken();
        this.skillsBuildApplicationId = config.skillsBuildApplicationId();
        this.skillsBuildApplicationSecret = config.skillsBuildApplicationSecret();
        this.skillsBuildIpId = config.skillsBuildIpId();
        this.skillsBuildAccessKey = config.skillsBuildAccessKey();
        this.skillsBuildRefreshToken = config.skillsBuildRefreshToken();
        this.skillsBuildAdultApplicationId = config.skillsBuildAdultApplicationId();
        this.skillsBuildAdultApplicationSecret = config.skillsBuildAdultApplicationSecret();
        this.skillsBuildAdultIpId = config.skillsBuildAdultIpId();
        this.skillsBuildAdultAccessKey = config.skillsBuildAdultAccessKey();
        this.skillsBuildAdultRefreshToken = config.skillsBuildAdultRefreshToken();
        /* PRM Partner */
        this.jwtIssuer = config.jwtIssuer();
        this.jwtSubject = config.jwtSubject();
        this.jwtAudience = config.jwtAudience();
        this.jwtPrivateKey = config.jwtPrivateKey();
        this.prmPpsAuthApiUrl = config.prmPpsAuthApiUrl();
        this.prmPpsApiUrl = config.prmPpsApiUrl();
        /* ALM API Base Url */
        this.almApiBaseUrl = config.almApiBaseUrl();
        /* UUID API Config */
        this.uuidAccessTokenUsername = config.uuidAccessTokenUsername();
        this.uuidAccessTokenPassword = config.uuidAccessTokenPassword();
        this.uuidAccessTokenApiUrl = config.uuidAccessTokenApiUrl();
        this.uuidApiUrl = config.uuidApiUrl();
        /* STart at IBM - Privacy acceptance */
        this.privacyAcceptanceTokenApi = config.privacyAcceptanceTokenApi();
        this.privacyAcceptanceApi = config.privacyAcceptanceApi();
        this.privacyAcceptanceFunctionalId = config.privacyAcceptanceFunctionalId();
        this.privacyAcceptancePrivateKey = config.privacyAcceptancePrivateKey();
    }

    public String getRedirectUrlforCourseLaunchPage() {
        return redirectUrlforCourseLaunchPage;
    }

    public String getRedirectUrlforCourseDetailPage() {
        return redirectUrlforCourseDetailPage;
    }

    /* ALM Sandbox */
    public String getEmployeeApplicationIdSandbox() {
        return employeeApplicationIdSandbox;
    }

    public String getEmployeeApplicationSecretSandbox() {
        return employeeApplicationSecretSandbox;
    }

    public String getEmployeeIpIdSandbox() {
        return employeeIpIdSandbox;
    }

    public String getEmployeeAccessKeySandbox() {
        return employeeAccessKeySandbox;
    }

    public String getEmployeeRefreshTokenSandbox() {
        return employeeRefreshTokenSandbox;
    }

    public String getBusinessPartnerAndClientApplicationIdSandbox() {
        return businessPartnerAndClientApplicationIdSandbox;
    }

    public String getBusinessPartnerAndClientApplicationSecretSandbox() {
        return businessPartnerAndClientApplicationSecretSandbox;
    }

    public String getBusinessPartnerAndClientIpIdSandbox() {
        return businessPartnerAndClientIpIdSandbox;
    }

    public String getBusinessPartnerAndClientAccessKeySandbox() {
        return businessPartnerAndClientAccessKeySandbox;
    }

    public String getBusinessPartnerAndClientRefreshTokenSandbox() {
        return businessPartnerAndClientRefreshTokenSandbox;
    }

    public String getSkillsBuildApplicationIdSandbox() {
        return skillsBuildApplicationIdSandbox;
    }

    public String getSkillsBuildApplicationSecretSandbox() {
        return skillsBuildApplicationSecretSandbox;
    }

    public String getSkillsBuildIpIdSandbox() {
        return skillsBuildIpIdSandbox;
    }

    public String getSkillsBuildAccessKeySandbox() {
        return skillsBuildAccessKeySandbox;
    }

    public String getSkillsBuildRefreshTokenSandbox() {
        return skillsBuildRefreshTokenSandbox;
    }

    public String getSkillsBuildAdultApplicationIdSandbox() {
        return skillsBuildAdultApplicationIdSandbox;
    }

    public String getSkillsBuildAdultApplicationSecretSandbox() {
        return skillsBuildAdultApplicationSecretSandbox;
    }

    public String getSkillsBuildAdultIpIdSandbox() {
        return skillsBuildAdultIpIdSandbox;
    }

    public String getSkillsBuildAdultAccessKeySandbox() {
        return skillsBuildAdultAccessKeySandbox;
    }

    public String getSkillsBuildAdultRefreshTokenSandbox() {
        return skillsBuildAdultRefreshTokenSandbox;
    }

    public String getPreHireApplicationIdSandbox() {
        return preHireApplicationIdSandbox;
    }

    public String getPreHireApplicationSecretSandbox() {
        return preHireApplicationSecretSandbox;
    }

    public String getPreHireRefreshTokenSandbox() {
        return preHireRefreshTokenSandbox;
    }

    /* ALM Stage */

    public String getPreHireApplicationIdStage() {
        return preHireApplicationIdStage;
    }

    public String getPreHireApplicationSecretStage() {
        return preHireApplicationSecretStage;
    }

    public String getPreHireRefreshTokenStage() {
        return preHireRefreshTokenStage;
    }

    public String getEmployeeApplicationIdStage() {
        return employeeApplicationIdStage;
    }

    public String getEmployeeApplicationSecretStage() {
        return employeeApplicationSecretStage;
    }

    public String getEmployeeIpIdStage() {
        return employeeIpIdStage;
    }

    public String getEmployeeAccessKeyStage() {
        return employeeAccessKeyStage;
    }

    public String getEmployeeRefreshTokenStage() {
        return employeeRefreshTokenStage;
    }

    public String getBusinessPartnerAndClientApplicationIdStage() {
        return businessPartnerAndClientApplicationIdStage;
    }

    public String getBusinessPartnerAndClientApplicationSecretStage() {
        return businessPartnerAndClientApplicationSecretStage;
    }

    public String getBusinessPartnerAndClientIpIdStage() {
        return businessPartnerAndClientIpIdStage;
    }

    public String getBusinessPartnerAndClientAccessKeyStage() {
        return businessPartnerAndClientAccessKeyStage;
    }

    public String getBusinessPartnerAndClientRefreshTokenStage() {
        return businessPartnerAndClientRefreshTokenStage;
    }

    public String getSkillsBuildApplicationIdStage() {
        return skillsBuildApplicationIdStage;
    }

    public String getSkillsBuildApplicationSecretStage() {
        return skillsBuildApplicationSecretStage;
    }

    public String getSkillsBuildIpIdStage() {
        return skillsBuildIpIdStage;
    }

    public String getSkillsBuildAccessKeyStage() {
        return skillsBuildAccessKeyStage;
    }

    public String getSkillsBuildRefreshTokenStage() {
        return skillsBuildRefreshTokenStage;
    }

    public String getSkillsBuildAdultApplicationIdStage() {
        return skillsBuildAdultApplicationIdStage;
    }

    public String getSkillsBuildAdultApplicationSecretStage() {
        return skillsBuildAdultApplicationSecretStage;
    }

    public String getSkillsBuildAdultIpIdStage() {
        return skillsBuildAdultIpIdStage;
    }

    public String getSkillsBuildAdultAccessKeyStage() {
        return skillsBuildAdultAccessKeyStage;
    }

    public String getSkillsBuildAdultRefreshTokenStage() {
        return skillsBuildAdultRefreshTokenStage;
    }

    /* ALM Prod */
    public String getPreHireApplicationId() {
        return preHireApplicationId;
    }

    public String getPreHireApplicationSecret() {
        return preHireApplicationSecret;
    }

    public String getPreHireRefreshToken() {
        return preHireRefreshToken;
    }

    public String getEmployeeApplicationId() {
        return employeeApplicationId;
    }

    public String getEmployeeApplicationSecret() {
        return employeeApplicationSecret;
    }

    public String getEmployeeIpId() {
        return employeeIpId;
    }

    public String getEmployeeAccessKey() {
        return employeeAccessKey;
    }

    public String getEmployeeRefreshToken() {
        return employeeRefreshToken;
    }

    public String getBusinessPartnerAndClientApplicationId() {
        return businessPartnerAndClientApplicationId;
    }

    public String getBusinessPartnerAndClientApplicationSecret() {
        return businessPartnerAndClientApplicationSecret;
    }

    public String getBusinessPartnerAndClientIpId() {
        return businessPartnerAndClientIpId;
    }

    public String getBusinessPartnerAndClientAccessKey() {
        return businessPartnerAndClientAccessKey;
    }

    public String getBusinessPartnerAndClientRefreshToken() {
        return businessPartnerAndClientRefreshToken;
    }

    public String getSkillsBuildApplicationId() {
        return skillsBuildApplicationId;
    }

    public String getSkillsBuildApplicationSecret() {
        return skillsBuildApplicationSecret;
    }

    public String getSkillsBuildIpId() {
        return skillsBuildIpId;
    }

    public String getSkillsBuildAccessKey() {
        return skillsBuildAccessKey;
    }

    public String getSkillsBuildRefreshToken() {
        return skillsBuildRefreshToken;
    }

    public String getSkillsBuildAdultApplicationId() {
        return skillsBuildAdultApplicationId;
    }

    public String getSkillsBuildAdultApplicationSecret() {
        return skillsBuildAdultApplicationSecret;
    }

    public String getSkillsBuildAdultIpId() {
        return skillsBuildAdultIpId;
    }

    public String getSkillsBuildAdultAccessKey() {
        return skillsBuildAdultAccessKey;
    }

    public String getSkillsBuildAdultRefreshToken() {
        return skillsBuildAdultRefreshToken;
    }

    /* PRM Partner */
    public String getJwtIssuer() {
        return jwtIssuer;
    }

    public String getJwtSubject() {
        return jwtSubject;
    }

    public String getJwtAudience() {
        return jwtAudience;
    }

    public String jwtPrivateKey() {
        return jwtPrivateKey;
    }

    public String getPrmPpsAuthApiUrl() {
        return prmPpsAuthApiUrl;
    }

    public String getPrmPpsApiUrl() {
        return prmPpsApiUrl;
    }

    public String getAlmApiBaseUrl() {
        return almApiBaseUrl;
    }

    /* UUID */

    public String getUuidAccessTokenUsername() {
        return uuidAccessTokenUsername;
    }

    public String getUuidAccessTokenPassword() {
        return uuidAccessTokenPassword;
    }

    public String getUuidAccessTokenApiUrl() {
        return uuidAccessTokenApiUrl;
    }

    public String getUuidApiUrl() {
        return uuidApiUrl;
    }

    /* STart at IBM - Privacy acceptance */
    public String getPrivacyAcceptanceTokenApi() {
        return privacyAcceptanceTokenApi;
    }

    public String getPrivacyAcceptanceApi() {
        return privacyAcceptanceApi;
    }

    public String getPrivacyAcceptanceFunctionalId() {
        return privacyAcceptanceFunctionalId;
    }

    public String getPrivacyAcceptancePrivateKey() {
        return privacyAcceptancePrivateKey;
    }
}