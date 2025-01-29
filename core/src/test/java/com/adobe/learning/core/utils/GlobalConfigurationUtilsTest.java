package com.adobe.learning.core.utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.gson.JsonObject;

@ExtendWith({MockitoExtension.class})
class GlobalConfigurationUtilsTest {

  private JsonObject globalConfig;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    globalConfig = mock(JsonObject.class);
  }

  @Test
  public void testFilterAdminConfigs() {
    JsonObject globalConfig = getAdminConfigs();
    GlobalConfigurationUtils.filterAdminConfigs(globalConfig);
    assertNull(globalConfig.get("clientSecret"));
    assertNull(globalConfig.get("authorRefreshToken"));
    assertNull(globalConfig.get("refreshToken"));
    assertNotNull(globalConfig.get("almBaseURL"));
  }

  private JsonObject getAdminConfigs() {
    JsonObject adminConfigs = new JsonObject();
    adminConfigs.addProperty("almBaseURL", "https://learningmanagerstage1.adobe.com"); // INPUT_REQUIRED {almBaseURL}
    adminConfigs.addProperty("clientSecret", "xxxxx"); // INPUT_REQUIRED {clientSecret}
    adminConfigs.addProperty("clientId", "xxxxx"); // INPUT_REQUIRED {clientId}
    adminConfigs.addProperty("refreshToken", "xxxxx"); // INPUT_REQUIRED {refreshToken}
    adminConfigs.addProperty("commerceURL", "https://learningmanagerstage1.adobe.com"); // INPUT_REQUIRED {commerceURL}
    adminConfigs.addProperty("customerTokenLifetime", "3600"); // INPUT_REQUIRED {customerTokenLifetime}
    adminConfigs.addProperty("authorRefreshToken", "xxxxxx"); // INPUT_REQUIRED {authorRefreshToken}
    return adminConfigs;
  }

  @Test
  void testFilterAdminConfigsWithMocks() {
    GlobalConfigurationUtils.filterAdminConfigs(globalConfig);

    verify(globalConfig).remove(Constants.Config.CLIENT_SECRET);
    verify(globalConfig).remove(Constants.Config.SITES_AUTHOR_REFRESH_TOKEN_NAME);
    verify(globalConfig).remove(Constants.Config.COMMERCE_ADMIN_REFRESH_TOKEN);
  }
}