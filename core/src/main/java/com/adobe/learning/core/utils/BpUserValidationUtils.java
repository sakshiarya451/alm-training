package com.adobe.learning.core.utils;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.learning.core.config.AlmEnvConfig;
import com.adobe.learning.core.constants.CommonConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class for validation of BP status of a user
 */
public class BpUserValidationUtils {

    /**
     * ObjectMapper instance variable
     */
    private static ObjectMapper objectMapper;

    /**
     * JsonNode instance variable
     */
    private static JsonNode rootNode;

    /**
     * Constructor
     */
    private BpUserValidationUtils() {

    }

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(BpUserValidationUtils.class);

    /**
     * 
     * @param uuid         String
     * @param almEnvConfig AlmEnvConfig
     * @return bpStatus String
     */
    public static String getBpStatus(String uuid, AlmEnvConfig almEnvConfig) {
        String bpStatus = StringUtils.EMPTY;
        if (StringUtils.isNotEmpty(uuid)) {
            Map<String, String> accessTokenMap = getPrmPartnerAccessToken(almEnvConfig);
            Map<String, String> requestProperties = new HashMap<>();
            requestProperties.put(CommonConstants.AUTHORIZATION,
                    accessTokenMap.get(CommonConstants.TOKEN_TYPE) + " "
                            + accessTokenMap.get(CommonConstants.ACCESS_TOKEN));
            String bpStatusResponseString = HttpConnectionUtils.getResponseFromHttpRequest(
                    almEnvConfig.getPrmPpsApiUrl() + uuid, CommonConstants.GET_METHOD, requestProperties, null);
            LOG.info("PRM Partner response -> {}", bpStatusResponseString);
            if (StringUtils.isNotBlank(bpStatusResponseString)) {
                try {
                    objectMapper = new ObjectMapper();
                    rootNode = objectMapper.readTree(bpStatusResponseString);
                    bpStatus = rootNode.get(0)
                            .path("countryEnterprises")
                            .get(0).path("BPStatus")
                            .asText();
                } catch (JsonProcessingException e) {
                    LOG.error("Error in processing JSON in getBpStatus() : {} {}", e.getMessage(), e);
                }
            }
        }
        return bpStatus != null ? bpStatus : StringUtils.EMPTY;
    }

    /**
     * Gets Acess token from PRM partner portal
     * 
     * @param almEnvConfig AlmEnvConfig
     * @return prmAccessTokenMap Map<String, String>
     */
    public static Map<String, String> getPrmPartnerAccessToken(AlmEnvConfig almEnvConfig) {
        Map<String, String> prmAccessTokenMap = new HashMap<>();
        String requestUrl = almEnvConfig.getPrmPpsAuthApiUrl() + getJwtToken(almEnvConfig);
        String prmPartnerAcessTokenResponseString = HttpConnectionUtils.getResponseFromHttpRequest(requestUrl,
                CommonConstants.GET_METHOD,
                new HashMap<>(), null);
        if (StringUtils.isNotBlank(prmPartnerAcessTokenResponseString)) {
            try {
                objectMapper = new ObjectMapper();
                rootNode = objectMapper.readTree(prmPartnerAcessTokenResponseString);
                prmAccessTokenMap.put(CommonConstants.TOKEN_TYPE, rootNode.path(CommonConstants.TOKEN_TYPE).asText());
                prmAccessTokenMap.put(CommonConstants.ACCESS_TOKEN,
                        rootNode.path(CommonConstants.ACCESS_TOKEN).asText());
                return prmAccessTokenMap;
            } catch (JsonProcessingException e) {
                LOG.error("Error in processing JSON in getPrmPartnerAccessToken() : {} {}", e.getMessage(), e);
            }
        }
        LOG.info("Failed to get PRM partner access token");
        return prmAccessTokenMap;
    }

    /**
     * Gets JWT token
     * 
     * @param almEnvConfig
     * @return jwtToken String
     */
    public static String getJwtToken(AlmEnvConfig almEnvConfig) {

        String header = CommonConstants.JWT_TOKEN_HEADER;
        String claimTemplate = CommonConstants.JWT_TOKEN_CLAIM_TEMPLATE;

        try {
            StringBuilder token = new StringBuilder();
            // Encode the JWT Header and add it to our string to sign
            token.append(Base64.encodeBase64URLSafeString(header.getBytes(StandardCharsets.UTF_8)));

            // Separate with a period
            token.append(".");

            // Create the JWT Claims Object
            String[] claimArray = new String[5];
            claimArray[0] = almEnvConfig.getJwtIssuer();
            claimArray[1] = almEnvConfig.getJwtSubject();
            claimArray[2] = almEnvConfig.getJwtAudience();
            claimArray[3] = Long.toString((System.currentTimeMillis() / 1000) + 30000);
            MessageFormat claims;
            claims = new MessageFormat(claimTemplate);
            String payload = claims.format(claimArray);

            // Add the encoded claims object
            token.append(Base64.encodeBase64URLSafeString(payload.getBytes(StandardCharsets.UTF_8)));

            // Load the private key from a keystore
            String privateKeynew = almEnvConfig.jwtPrivateKey();

            // Base64 decode the result
            byte[] pkcs8EncodedBytes = Base64.decodeBase64(privateKeynew);

            // extract the private key
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey privKey = kf.generatePrivate(keySpec);

            // Sign the JWT Header + "." + JWT Claims Object
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privKey);
            signature.update(token.toString().getBytes(StandardCharsets.UTF_8));
            String signedPayload = Base64.encodeBase64URLSafeString(signature.sign());

            // Separate with a period
            token.append(".");

            // Add the encoded signature
            token.append(signedPayload);

            return token.toString();

        } catch (Exception e) {
            LOG.error("Error in generating JWT token for PRM Partner : {} {}", e.getMessage(), e);
        }
        return StringUtils.EMPTY;
    }

    /**
     * 
     * @param userEmail
     * @param almEnvConfig
     * @return String
     */
    public static String getUUID(String userEmail, AlmEnvConfig almEnvConfig) {

        Map<String, String> requestProperties = new HashMap<>();
        requestProperties.put(CommonConstants.AUTHORIZATION,
                CommonConstants.BEARER + " " + getAccessTokenForUUID(almEnvConfig));

        String uuidResponseString = HttpConnectionUtils.getResponseFromHttpRequest(
                almEnvConfig.getUuidApiUrl() + userEmail, CommonConstants.GET_METHOD, requestProperties, null);
        LOG.info("UUID Response String -> {}", uuidResponseString);

        if (StringUtils.isNotEmpty(uuidResponseString)) {
            try {
                objectMapper = new ObjectMapper();
                if (objectMapper.readTree(uuidResponseString).has("iui"))
                    return objectMapper.readTree(uuidResponseString)
                            .path("iui")
                            .asText();
            } catch (JsonProcessingException e) {
                LOG.error("Error in processing JSON in getUUID() : {} {}", e.getMessage(), e);
            }
        }
        return StringUtils.EMPTY;
    }

    /**
     * 
     * @param almEnvConfig
     * @return String
     */
    public static String getAccessTokenForUUID(AlmEnvConfig almEnvConfig) {

        String auth = new StringBuilder().append(almEnvConfig.getUuidAccessTokenUsername())
                .append(":")
                .append(almEnvConfig.getUuidAccessTokenPassword())
                .toString();
        String encodedAuth = java.util.Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

        Map<String, String> requestProperties = new HashMap<>();
        requestProperties.put(CommonConstants.AUTHORIZATION, "Basic " + encodedAuth);
        requestProperties.put(CommonConstants.CONTENT_TYPE, CommonConstants.APPLICATION_JSON);

        String requestBody = "{\"grant_type\": \"client_credentials\"}";

        String accessTokenResponseForUUID = HttpConnectionUtils.getResponseFromHttpRequest(
                almEnvConfig.getUuidAccessTokenApiUrl(), CommonConstants.POST_METHOD, requestProperties, requestBody);
        if (StringUtils.isNotEmpty(accessTokenResponseForUUID)) {
            try {
                objectMapper = new ObjectMapper();
                return objectMapper.readTree(accessTokenResponseForUUID)
                        .path(CommonConstants.ACCESS_TOKEN)
                        .asText();
            } catch (JsonProcessingException e) {
                LOG.error("Error in processing JSON in getAccessTokenForUUID() : {} {}", e.getMessage(), e);
            }
        }
        LOG.info("Failed to get UUID access token");
        return StringUtils.EMPTY;
    }
}
