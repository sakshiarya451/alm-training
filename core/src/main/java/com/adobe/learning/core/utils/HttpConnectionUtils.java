package com.adobe.learning.core.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.learning.core.constants.CommonConstants;

/**
 * Makes Http calls and get the response
 */
public class HttpConnectionUtils {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(HttpConnectionUtils.class);

    /**
     * Constructor
     */
    private HttpConnectionUtils() {

    }

    /**
     * Builds Http Connection and return the response
     * 
     * @param String      url
     * @param String      requestMethod
     * @param Map<String, String> requestProperties
     * @param String      requestBody
     * @return String responseString
     */
    public static String getResponseFromHttpRequest(String url, String requestMethod,
            Map<String, String> requestProperties, String requestBody) {

        String responseString = StringUtils.EMPTY;
        if (StringUtils.isEmpty(url) || !isValidRedirectUrl(url)) {
            LOG.info("URL is empty or invalid : {}", url);
            return responseString;
        }
        try {
            URL requestUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setConnectTimeout(500000);
            connection.setReadTimeout(500000);
            connection.setRequestMethod(requestMethod.toUpperCase());

            requestProperties.entrySet()
                    .forEach(entry -> connection.setRequestProperty(entry.getKey(), entry.getValue()));

            if (StringUtils.isNotBlank(requestBody)) {
                connection.setDoOutput(true);
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }
            }

            int responseCode = connection.getResponseCode();
            InputStream inputStream = responseCode == 200 ? connection.getInputStream()
                    : connection.getErrorStream();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream))) {
                StringBuilder responseBuilder = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    responseBuilder.append(inputLine);
                }
                responseString = responseBuilder.toString();
                if (responseCode != 200) {
                    LOG.error("Failed to get the successful response");
                    LOG.error(
                            "URL : {} Request Method : {} Request Properties : {}, Request Body : {}, Response Code : {}, Response: {}",
                            url, requestMethod, requestProperties, requestBody, responseCode, responseBuilder);
                    responseString = StringUtils.EMPTY;
                }
            }
            connection.disconnect();
        } catch (IOException e) {
            LOG.error("Error is making HttpConnection : {}", e.getMessage(), e);
        }
        return responseString;
    }

    /**
     * 
     * @param redirectUrl
     * @return boolean
     */
    public static boolean isValidRedirectUrl(String redirectUrl) {
        try {
            URL url = new URL(redirectUrl);
            String host = url.getHost();
            for (String domain : CommonConstants.TRUSTED_DOMAINS) {
                if (host.equals(new URL(domain).getHost())) {
                    return true;
                }
            }
        } catch (MalformedURLException e) {
            LOG.error("Invalid URL format: {} {}", redirectUrl, e);
        }
        return false;
    }
}
