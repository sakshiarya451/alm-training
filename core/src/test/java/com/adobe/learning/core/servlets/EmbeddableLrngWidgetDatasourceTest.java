package com.adobe.learning.core.servlets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestPathInfo;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.learning.core.services.GlobalConfigurationService;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.gson.JsonObject;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class EmbeddableLrngWidgetDatasourceTest {

  private final AemContext ctx = new AemContext();

  @InjectMocks
  private EmbeddableLrngWidgetDatasource dsServlet;

  @Mock private GlobalConfigurationService configService;

  @Mock private HttpClientBuilderFactory clientBuilderFactory;

  @Mock private SlingHttpServletRequest request;

  @Mock private SlingHttpServletResponse response;

  @Mock private RequestPathInfo requestPathInfo;

  @Mock private ResourceResolver resourceResolver;

  @Mock private Resource resource;

  @Mock private PageManager pageManager;

  @Mock private Page currentPage;

  @Mock private ValueMap valueMap;

  @Mock private JsonObject adminObj;

  @BeforeEach
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    HttpClientBuilderFactory clientBuilderFactory = mock(HttpClientBuilderFactory.class);
    dsServlet = new EmbeddableLrngWidgetDatasource();

    JsonObject adminConfigs = new JsonObject();
    adminConfigs.addProperty("almBaseURL", "https://learningmanagerstage1.adobe.com"); // INPUT_REQUIRED {almBaseURL}
    adminConfigs.addProperty("theme.background", "transparent");
    lenient().when(configService.getAdminConfigs(isNull())).thenReturn(adminConfigs);

    Field replicatorField = EmbeddableLrngWidgetDatasource.class.getDeclaredField("configService");
    replicatorField.setAccessible(true);
    replicatorField.set(dsServlet, configService);

    Field replicatorField1 =
        EmbeddableLrngWidgetDatasource.class.getDeclaredField("clientBuilderFactory");
    replicatorField1.setAccessible(true);
    replicatorField1.set(dsServlet, clientBuilderFactory);

    ctx.registerService(HttpClientBuilderFactory.class, clientBuilderFactory);

    ctx.registerService(
        GlobalConfigurationService.class,
        configService,
        org.osgi.framework.Constants.SERVICE_RANKING,
        Integer.MAX_VALUE);

    ctx.create().page("/content/mypage");
    ctx.create().page("/content/mypage/widgetSelect");
    ctx.currentResource("/content/mypage");

    ctx.requestPathInfo().setSuffix("/widget/page");
    ctx.load().json("/files/AdminConfigRsrc.json", "/widget/page");
  }

  // @Test
  // void testDoGetWithValidSuffix() throws Exception {
  //   when(request.getRequestPathInfo()).thenReturn(requestPathInfo);
  //   when(requestPathInfo.getSuffix()).thenReturn("/content/suffix");
  //   when(request.getResourceResolver()).thenReturn(resourceResolver);
  //   when(resourceResolver.getResource("/content/suffix")).thenReturn(resource);
  //   when(resource.getResourceResolver()).thenReturn(resourceResolver);
  //   when(resourceResolver.adaptTo(PageManager.class)).thenReturn(pageManager);
  //   when(pageManager.getContainingPage(resource)).thenReturn(currentPage);
  //   when(resource.getValueMap()).thenReturn(valueMap);
  //   when(configService.getAdminConfigs(currentPage)).thenReturn(adminObj);
  //   when(adminObj.get(Constants.Config.ALM_BASE_URL)).thenReturn(new JsonObject().getAsJsonPrimitive("http://localhost")); // INPUT_REQUIRED {almBaseURL}

  //   List<EmbeddableLrngWidgetConfig> widgetConfigs = new ArrayList<>();
  //   EmbeddableLrngWidgetConfig widgetConfig = new EmbeddableLrngWidgetConfig();
  //   widgetConfig.setWidgetRef("widgetRef");
  //   widgetConfig.setOptions(new ArrayList<>());
  //   widgetConfigs.add(widgetConfig);

  //   mockStatic(EmbeddableLrngWidgetUtils.class);
  //   when(EmbeddableLrngWidgetUtils.getEmbeddableWidgetsConfig(anyString(), eq(clientBuilderFactory))).thenReturn(widgetConfigs);

  //   dsServlet.doGet(request, response);

  //   verify(request).setAttribute(eq(DataSource.class.getName()), any(SimpleDataSource.class));
  // }

  @Test
  void testDoGetWithNullSuffix() throws Exception {
    when(request.getRequestPathInfo()).thenReturn(requestPathInfo);
    when(requestPathInfo.getSuffix()).thenReturn(null);

    dsServlet.doGet(request, response);

    verify(request).setAttribute(eq(DataSource.class.getName()), any(SimpleDataSource.class));
  }
}

