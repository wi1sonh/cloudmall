package fun.cmgraph;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import fun.cmgraph.constant.StatusConstant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicInteger;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class SeverOperationTests {

    @Autowired
    private MockMvc mockMvc;

    private String token;

    final private String productCategoryName = "商品类";
    final private String bundleCategoryName = "套餐类";
    final private String productName = "商品1";

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void serverOperationTest() throws Exception {
        register();
        addCategory();
        setStatus();
    }

    public void register() throws Exception {
        String registerUrl = "/admin/employee/register";
        String registerRequestBody = "{\"account\":\"test\",\"password\":\"123456\"}";
        mockMvc.perform(post(registerUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequestBody))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    serverLogin(registerRequestBody);
                });
    }

    public void serverLogin(String loginRequestBody) throws Exception {
        // test login
        String loginUrl = "/admin/employee/login";
        mockMvc.perform(post(loginUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestBody))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseContent = result.getResponse().getContentAsString();
                    token = JsonPath.read(responseContent, "$.data.token");
                });

    }

    public void addCategory() throws Exception {
        String categoryUrl = "/admin/category";
        String addProductCategoryRequestBody = String.format("{\"id\":1,\"name\":\"%s\",\"type\":1,\"sort\":1}", productCategoryName);
        // add product category
        mockMvc.perform(post(categoryUrl)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addProductCategoryRequestBody))
                .andExpect(status().isOk());
        String addBundleCategoryRequestBody = String.format("{\"id\":2,\"name\":\"%s\",\"type\":2,\"sort\":2}", bundleCategoryName);
        // add bundle category
        AtomicInteger productCategoryId = new AtomicInteger();
        mockMvc.perform(post(categoryUrl)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addBundleCategoryRequestBody))
                .andExpect(status().isOk());
        // validate category
        mockMvc.perform(get(categoryUrl + String.format("/name/%s", productCategoryName))
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseContent = result.getResponse().getContentAsString();
                    int total = JsonPath.read(responseContent, "$.data.type");
                    assertEquals(1, total);
                    productCategoryId.set(JsonPath.read(responseContent, "$.data.id"));
                    addProduct(productCategoryId.get());
                });
        AtomicInteger bundleCategoryId = new AtomicInteger();
        mockMvc.perform(get(categoryUrl + String.format("/name/%s", bundleCategoryName))
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseContent = result.getResponse().getContentAsString();
                    int total = JsonPath.read(responseContent, "$.data.type");
                    assertEquals(2, total);
                    bundleCategoryId.set(JsonPath.read(responseContent, "$.data.id"));
                    addBundle(bundleCategoryId.get(), productCategoryId.get());
                });
    }

    public void addProduct(int categoryId) throws Exception {
        String productUrl = "/admin/dish";
        String addProductRequestBody = String.format("{\"id\":1,\"name\":\"%s\",\"detail\":\"this is a test product\",\"price\":10,\"categoryId\":%d}", productName, categoryId);
        // add product
        mockMvc.perform(post(productUrl)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addProductRequestBody))
                .andExpect(status().isOk());
        // validate product
        mockMvc.perform(get(productUrl + String.format("/name/%s", productName))
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseContent = result.getResponse().getContentAsString();
                    String detail = JsonPath.read(responseContent, "$.data.detail");
                    assertEquals(detail, "this is a test product");
                });
    }

    public void addBundle(int bundleCategoryId, int productCategoryId) throws Exception {
        String bundleUrl = "/admin/setmeal";
        String addBundleRequestBody = String.format("{\"id\":0,\"name\":\"%s\",\"detail\":\"this is a test bundle\",\"price\":20,\"categoryId\":%d," +
                "\"bundleProducts\":[{\"id\":0,\"name\":\"%s\",\"price\":10,\"copies\":2,\"dishId\":%d}]}", bundleCategoryName, bundleCategoryId, productName, productCategoryId);
        // add bundle
        mockMvc.perform(post(bundleUrl)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addBundleRequestBody))
                .andExpect(status().isOk());
        // validate bundle
        mockMvc.perform(get(bundleUrl + String.format("/name/%s", bundleCategoryName))
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseContent = result.getResponse().getContentAsString();
                    String detail = JsonPath.read(responseContent, "$.data.detail");
                    assertEquals(detail, "this is a test bundle");
                });
    }

    public void setStatus() throws Exception {
        String statusUrl = "/admin/shop";
        // set status
        mockMvc.perform(put(statusUrl + "/" + StatusConstant.ENABLE)
                        .header("Authorization", token))
                .andExpect(status().isOk());
        // validate status
        mockMvc.perform(get(statusUrl + "/status")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseContent = result.getResponse().getContentAsString();
                    int status = JsonPath.read(responseContent, "$.data");
                    assertEquals(StatusConstant.ENABLE, status);
                });
    }
}
