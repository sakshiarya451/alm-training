package com.adobe.learning.core.constants;

/**
 * Has all the constants used in different classes
 */
@SuppressWarnings({ "java:S2386", "java:S1075", "squid:S2386" })
public final class CommonConstants {

        /**
         * Initializes CommonConstants
         */
        private CommonConstants() {

        }

        public static final String CODE_REQ_PARAM = "code";

        public static final String ACCESS_TOKEN_API_PATH = "/oauth/token";

        public static final String OAUTH_REDIRECT_API_PATH = "/oauth/o/authorize";

        public static final String ACCESS_TOKEN = "access_token";

        public static final String ALM_ACCESS_TOKEN = "alm_access_token";

        public static final String ALM_ADMIN_ACCESS_TOKEN = "alm_admin_access_token";

        public static final String APPLICATION_ID = "applicationId";

        public static final String APPLICATION_SECRET = "applicationSecret";

        public static final String IP_ID = "ipId";

        public static final String ACCESS_KEY = "accesskey";

        public static final String COURSE_ID_REQ_PARAM = "loid";

        public static final String INSTANCE_ID_REQ_PARAM = "instanceid";

        public static final String ACCOUNT_ID_REQ_PARAM = "accountid";

        public static final String CLIENT_ID_REQ_PARAM = "client_id";

        public static final String REDIRECT_URI_REQ_PARAM = "redirect_uri";

        public static final String CLIENT_SECRET_REQ_PARAM = "client_secret";

        public static final String INSTANCE_ID_COOKIE = "instance_id";

        public static final String ACCOUNT_ID_COOKIE = "account_id";

        public static final String AMP = "&";

        public static final String EQUAL = "=";

        public static final String QUESTION_MARK = "?";

        public static final String EQUAL_ENCODED = "%3D";

        public static final String AND_ENCODED = "%26";

        public static final String STATE_SCOPE_RESPONSETYPE = "&state=prime_auth&scope=learner:read,learner:write&response_type=CODE";

        public static final String LOGOUTAFTERAUTH_LOGINURL = "&logoutAfterAuthorize=true&loginUrl=%2Faccountiplogin%3F";

        public static final String AUTHORIZATION = "Authorization";

        public static final String POST_METHOD = "POST";

        public static final String GET_METHOD = "GET";

        public static final String USER_ID = "userId";

        public static final String USER_TYPE = "userType";

        public static final String USER_EMAIL = "userEmail";

        public static final String USER_NAME = "userName";

        public static final String ACCEPT = "Accept";

        public static final String OAUTH = "oauth";

        public static final String DATA = "data";

        public static final String ATTRIBUTES = "attributes";

        public static final String NAME = "name";

        public static final String CONTENT_TYPE = "Content-Type";

        public static final String USERS_API_PATH = "/primeapi/v2/users/";

        public static final String APPLICATION_VND_API_JSON = "application/vnd.api+json";

        public static final String X_HTTP_METHOD_OVERRIDE = "X-HTTP-Method-Override";

        public static final String PATCH = "PATCH";

        public static final String DOMAIN_IBM = "ibm.com";

        public static final String IBMER = "IBMer";

        public static final String BUSINESS_PARTNER = "BusinessPartner";

        public static final String CLIENT = "Client";

        public static final String ACTIVE = "Active";

        public static final String USER_ENROLLMENT_API_PATH = "/primeapi/v2/enrollments";

        public static final String BEARER = "Bearer";

        public static final String APPLICATION_JSON = "application/json";

        public static final String ID = "id";

        public static final String EMAIL = "email";

        public static final String FIELDS = "fields";

        public static final String TOKEN_TYPE = "token_type";

        public static final String PRM_PPS_API = "https://ibmsc--staging.sandbox.my.salesforce.com/services/apexrest/countryenterprises/employee/";

        public static final String PRM_PPS_AUTH_API = "https://test.salesforce.com/services/oauth2/token?grant_type=urn:ietf:params:oauth:grant-type:jwt-bearer&assertion=";

        public static final String SINGLE_USER_API_PATH = "/primeapi/v2/user";

        public static final String REFRESH_TOKEN = "refresh_Token";

        public static final String JWT_TOKEN_HEADER = "{\"alg\":\"RS256\"}";

        public static final String JWT_TOKEN_CLAIM_TEMPLATE = "'{'\"iss\": \"{0}\", \"sub\": \"{1}\", \"aud\": \"{2}\", \"exp\": \"{3}\"'}'";

        public static final String UUID = "UUID";

        public static final String LO_API_PATH = "/primeapi/v2/learningObjects/";

        public static final String EMPLOYEE_RETIRED_MESSAGE = "<p>This course is no longer available as it may have been retired. Please go to <a href=\"https://yourlearning.ibm.com/\" style=\"display: inline;\">https://yourlearning.ibm.com/</a> where you will be able to search for a similar course to the one you are trying to access.</p>";

        public static final String TRAINING_RETIRED_MESSAGE = "<p>This course is no longer available as it may have been retired. Please go to <a href=\"https://www.ibm.com/training/search\" style=\"display: inline;\">www.ibm.com/training/search</a> where you will be able to search for a similar course to the one you are trying to access.</p>";

        public static final String SKILLLSBUILD_ADULT_RETIRED_MESSAGE = "<p>This course is no longer available as it may have been retired. Please go to <a href=\"https://skills.yourlearning.ibm.com/\" style=\"display: inline;\">https://skills.yourlearning.ibm.com/</a> where you will be able to search for a similar course to the one you are trying to access.</p>";

        public static final String SKILLLSBUILD_RETIRED_MESSAGE = "<p>This course is no longer available as it may have been retired. Please go to <a  href=\"https://ptech.yourlearning.ibm.com/\" style=\"display: inline;\">https://ptech.yourlearning.ibm.com/</a> where you will be able to search for a similar course to the one you are trying to access.</p>";

        public static final String IBMER_ACCESS_ERR = "<p>For IBM Course assistance,<a href=\"https://ibmcpsprod.service-now.com/its\" style=\"display: inline;\"> open a self-service support ticket.</a></p>";

        public static final String BP_ACCESS_ERR = "<h3 class=\"modal-title\">This content is exclusive to IBM Employees.</h3><p>If you consider that you have received this message in error, please <a href=\"https://ibmcpsprod.service-now.com/its\" style=\"display: inline;\">open a self-service support ticket.</a></p>";

        public static final String CLIENT_ACCESS_ERR = "<h3 class=\"modal-title\">This content is exclusive to IBM Employees or IBM Business Partners.</h3><p>Here's how to get access to the training content for Partners.</p><p class=\"bold\">Join our Partner program</p><p>If you're new to IBM, your first step is to join our<a href=\"https://www.ibm.com/partnerplus\" style=\"display: inline;\"> Partner program.</a></p><p class=\"bold\">Existing partners</p><p>If you are a part of a registered Business Partner company, and cannot access the training, reach out to your Authorized Partner Admin.</p><p><strong>For IBM Course assistance,</strong><a href=\"https://ibmcpsprod.service-now.com/its\" style=\"display: inline;\"> open a self-service support ticket.</a></p>";

        public static final String LO_ACCESS_ERR_MSG = "loAccessErrorMessage";

        public static final String GENERIC_ERROR_MSG = "This course is no longer available";

        public static final String LO_RETIRED_MSG = "loRetiredMessage";

        public static final String ERROR = "error";

        public static final String LANGUAGE_CODE = "languagecode";

        public static final String[] LOCALE = { "en-US", "fr-FR", "de-DE", "zh-CN", "es-ES", "it-IT", "ja-JP",
                        "pt-BR", "nl-NL", "pl-PL", "tr-TR", "ko-KR", "sv-SE", "ru-RU", "id-ID", "da-DK", "hi-IN",
                        "hu-HU",
                        "fi-FI", "nb-NO", "th-TH", "zh-TW", "no-NO", "ro-RO", "ar-SA", "am-ET", "cs-CZ", "en-GB",
                        "vi-VN",
                        "el-GR", "fr-CA", "en-AU", "es-XL", "sr-SP", "sl-SL", "sk-SK", "hr-HR", "uk-UA", "he-IL",
                        "bg-BG",
                        "nl-BE", "pt-PT", "sl-SL", "am-ET", "en-AU", "sr-SP", "zh-HK" };

        public static final String MANAGER_NOMINATED_TRYING_SELF_ENROLL_ERR = "Access to the course is limited to employees who had been onboarded by the course team. If you are interested in accessing the course, or have other questions contact <Author>.";

        public static final String INSTANCE_EXPIRED_ERR = "Please note that the timeframe for you to complete this course has expired. Please reach out to <Author> for further guidance.";

        public static final String[] TRUSTED_DOMAINS = {
                        "https://learningmanager.adobe.com", "https://ibmsc--staging.sandbox.my.salesforce.com",
                        "https://test.salesforce.com", "https://learningmanagereu.adobe.com",
                        "https://login.salesforce.com",
                        "https://ibmsc.my.salesforce.com", "https://us-south.appid.cloud.ibm.com",
                        "https://enterpriseprofileproxy.c8f8f055.public.multi-containers.ibm.com",
                        "https://learningmanagerstage.adobe.com", "https://api.test.yourlearning.ibm.com",
                        "https://api.yourlearning.ibm.com", "https://skillsbuild-highschool-adobeqa.alm.ibm.com"

        };
}