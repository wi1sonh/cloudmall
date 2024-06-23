package fun.cmgraph;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import fun.cmgraph.constant.StatusConstant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import fun.cmgraph.utils.HttpClientUtil;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class UserOperationTests {

    @Autowired
    private MockMvc mockMvc;

    private String token;
    private String serverToken;

    final private String productCategoryName = "Product Category";
    final private String bundleCategoryName = "Bundle Category";
    final private String productName = "Product 1";
    final private String bundleName = "Bundle 1";

    private int userId;
    private int productCategoryId;
    private int bundleCategoryId;
    private int productId;
    private int bundleId;
    private int addressId;

    @BeforeEach
    public void setUp() {
    }

    @Test
    @Order(2)
    public void userOperationTest() throws Exception {
        // prepare data
        register();
        addCategory();
        addProduct(productCategoryId);
        addBundle(bundleCategoryId, productCategoryId);
        setStatus();
        // user test
        userLogin();
        getStatus();
        getCategoryList();
        getProduct(productId);
        getBundle(bundleId);
        addAddressBook();
        addCart();
        submit();
    }

    public void userLogin() throws Exception {
        // test login
        mockStatic(HttpClientUtil.class);
        when(HttpClientUtil.doGet(anyString(), anyMap())).thenReturn("{\"openid\":\"test-openid\"}");
        String loginUrl = "/user/user/login";
        String loginRequestBody = "{\"code\":\"test-code\"}";
        mockMvc.perform(post(loginUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestBody))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    token = JsonPath.read(contentAsString, "$.data.token");
                    userId = JsonPath.read(contentAsString, "$.data.id");
                    assertNotNull(token);
                });
    }

    public void getStatus() throws Exception {
        String getStatusUrl = "/user/shop/status";
        mockMvc.perform(get(getStatusUrl)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    int status = JsonPath.read(contentAsString, "$.data");
                    assertEquals(StatusConstant.ENABLE, status);
                });
    }

    public void getCategoryList() throws Exception {
        String getCategoryListUrl = "/user/category/list";
        mockMvc.perform(get(getCategoryListUrl)
                        .header("Authorization", token)
                        .param("type", "1"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    List<Map<String, Object>> categoryList = JsonPath.read(contentAsString, "$.data");
                    assertEquals(1, categoryList.size());
                    Map<String, Object> category = categoryList.get(0);
                });
        mockMvc.perform(get(getCategoryListUrl)
                        .header("Authorization", token)
                        .param("type", "2"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    List<Map<String, Object>> categoryList = JsonPath.read(contentAsString, "$.data");
                    assertEquals(1, categoryList.size());
                    Map<String, Object> category = categoryList.get(0);
                    assertEquals(bundleCategoryName, category.get("name"));
                });
    }

    public void getProduct(int productId) throws Exception {
        String getProductUrl = "/user/dish/dish/" + productId;
        mockMvc.perform(get(getProductUrl)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    Map<String, Object> product = JsonPath.read(contentAsString, "$.data");
                    assertEquals(productName, product.get("name"));
                });
    }

    public void getBundle(int bundleId) throws Exception {
        String getBundleUrl = "/user/setmeal";
        mockMvc.perform(get(getBundleUrl + "/" + bundleId)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    Map<String, Object> bundle = JsonPath.read(contentAsString, "$.data");
                    assertEquals(bundleName, bundle.get("name"));
                });
    }

    public void addAddressBook() throws Exception {
        String addAddressBookUrl = "/user/address";
        String addAddressBookRequestBody = String.format("{\"id\":1,\"userId\":%d,\"consignee\":\"huang\",\"phone\":\"13400000000\"," +
                "\"gender\":1,\"provinceCode\":\"110000\",\"provinceName\":\"北京市\",\"cityCode\":\"110100\",\"cityName\":\"北京市\"," +
                "\"districtCode\":\"110101\",\"districtName\":\"东城区\",\"detail\":\"test\",\"label\":\"home\",\"isDefault\":1}",
                userId);
        // add address book
        mockMvc.perform(post(addAddressBookUrl)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addAddressBookRequestBody))
                .andExpect(status().isOk());
        // validate address book
        String addressBookListUrl = "/user/address/list";
        mockMvc.perform(get(addressBookListUrl)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseContent = result.getResponse().getContentAsString();
                    List<Map<String, Object>> addressBooks = JsonPath.read(responseContent, "$.data");
                    assertEquals(1, addressBooks.size());
                    Map<String, Object> addressBook = addressBooks.get(0);
                    assertEquals("huang", addressBook.get("consignee"));
                    addressId = (int) addressBook.get("id");
                });
    }

    public void addCart() throws Exception {
        String addCartUrl = "/user/cart/add";
        String addProductRequestBody = String.format("{\"dishId\":%d}", productId);
        // add cart
        mockMvc.perform(post(addCartUrl)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addProductRequestBody))
                .andExpect(status().isOk());
        mockMvc.perform(post(addCartUrl)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addProductRequestBody))
                .andExpect(status().isOk());
        // validate cart
        String cartListUrl = "/user/cart/list";
        mockMvc.perform(get(cartListUrl)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseContent = result.getResponse().getContentAsString();
                    List<Map<String, Object>> cartList = JsonPath.read(responseContent, "$.data");
                    assertEquals(1, cartList.size());
                    Map<String, Object> cart = cartList.get(0);
                    assertEquals(productName, cart.get("name"));
                    assertEquals(2, cart.get("number"));
                });
        String addBundleRequestBody = String.format("{\"setmealId\":%d}", bundleId);
        mockMvc.perform(post(addCartUrl)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addBundleRequestBody))
                .andExpect(status().isOk());
        // validate cart
        mockMvc.perform(get(cartListUrl)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseContent = result.getResponse().getContentAsString();
                    List<Map<String, Object>> cartList = JsonPath.read(responseContent, "$.data");
                    assertEquals(2, cartList.size());
                });
    }

    public void submit() throws Exception {
        String submitUrl = "/user/order/submit";
        String submitRequestBody = String.format("{\"addressId\":%d,\"payMethod\":1,\"remark\":\"test\",\"estimatedDeliveryTime\":\"2021-08-01 12:00:00\"," +
                "\"deliveryStatus\":1,\"tablewareNumber\":1,\"tablewareStatus\":1,\"packAmount\":1,\"amount\":30}", addressId);
        // submit order
        final String[] orderNumber = new String[1];
        mockMvc.perform(post(submitUrl)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(submitRequestBody))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseContent = result.getResponse().getContentAsString();
                    orderNumber[0] = JsonPath.read(responseContent, "$.data.orderNumber");
                    int amount = JsonPath.read(responseContent, "$.data.orderAmount");
                    assertEquals(30, amount);
                });
        // validate order
        String orderListUrl = "/user/order/unPayOrderCount";
        mockMvc.perform(get(orderListUrl)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseContent = result.getResponse().getContentAsString();
                    int count = JsonPath.read(responseContent, "$.data");
                    assertEquals(1, count);
                });
        // pay
        String paymentUrl = "/user/order/payment";
        String paymentRequestBody = String.format("{\"orderNumber\":\"%s\",\"payMethod\":1}", orderNumber[0]);
        mockMvc.perform(put(paymentUrl)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(paymentRequestBody))
                .andExpect(status().isOk());
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
                    serverToken = JsonPath.read(responseContent, "$.data.token");
                });
    }

    public void addCategory() throws Exception {
        String categoryUrl = "/admin/category";
        String addProductCategoryRequestBody = String.format("{\"id\":1,\"name\":\"%s\",\"type\":1,\"sort\":1}", productCategoryName);
        // add product category
        mockMvc.perform(post(categoryUrl)
                        .header("Authorization", serverToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addProductCategoryRequestBody))
                .andExpect(status().isOk());
        String addBundleCategoryRequestBody = String.format("{\"id\":2,\"name\":\"%s\",\"type\":2,\"sort\":2}", bundleCategoryName);
        // add bundle category

        mockMvc.perform(post(categoryUrl)
                        .header("Authorization", serverToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addBundleCategoryRequestBody))
                .andExpect(status().isOk());
        // validate category
        mockMvc.perform(get(categoryUrl + String.format("/name/%s", productCategoryName))
                        .header("Authorization", serverToken))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseContent = result.getResponse().getContentAsString();
                    int total = JsonPath.read(responseContent, "$.data.type");
                    assertEquals(1, total);
                    productCategoryId = JsonPath.read(responseContent, "$.data.id");
                });

        mockMvc.perform(get(categoryUrl + String.format("/name/%s", bundleCategoryName))
                        .header("Authorization", serverToken))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseContent = result.getResponse().getContentAsString();
                    int total = JsonPath.read(responseContent, "$.data.type");
                    assertEquals(2, total);
                    bundleCategoryId = JsonPath.read(responseContent, "$.data.id");
                });
    }

    public void addProduct(int categoryId) throws Exception {
        String productUrl = "/admin/dish";
        String addProductRequestBody = String.format("{\"id\":1,\"name\":\"%s\",\"detail\":\"this is a test product\",\"price\":10,\"categoryId\":%d}", productName, categoryId);
        // add product
        mockMvc.perform(post(productUrl)
                        .header("Authorization", serverToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addProductRequestBody))
                .andExpect(status().isOk());
        // validate product
        mockMvc.perform(get(productUrl + String.format("/name/%s", productName))
                        .header("Authorization", serverToken))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseContent = result.getResponse().getContentAsString();
                    String detail = JsonPath.read(responseContent, "$.data.detail");
                    assertEquals(detail, "this is a test product");
                    productId = JsonPath.read(responseContent, "$.data.id");
                });
    }

    public void addBundle(int bundleCategoryId, int productCategoryId) throws Exception {
        String bundleUrl = "/admin/setmeal";
        String addBundleRequestBody = String.format("{\"id\":0,\"name\":\"%s\",\"detail\":\"this is a test bundle\",\"price\":20,\"categoryId\":%d," +
                "\"bundleProducts\":[{\"id\":0,\"name\":\"%s\",\"price\":10,\"copies\":2,\"dishId\":%d}]}", bundleName, bundleCategoryId, productName, productCategoryId);
        // add bundle
        mockMvc.perform(post(bundleUrl)
                        .header("Authorization", serverToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addBundleRequestBody))
                .andExpect(status().isOk());
        // validate bundle
        mockMvc.perform(get(bundleUrl + String.format("/name/%s", bundleName))
                        .header("Authorization", serverToken))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseContent = result.getResponse().getContentAsString();
                    String detail = JsonPath.read(responseContent, "$.data.detail");
                    assertEquals(detail, "this is a test bundle");
                    bundleId = JsonPath.read(responseContent, "$.data.id");
                });
    }

    public void setStatus() throws Exception{
        String statusUrl = "/admin/shop";
        // set status
        mockMvc.perform(put(statusUrl + "/" + StatusConstant.ENABLE)
                        .header("Authorization", serverToken))
                .andExpect(status().isOk());
        // validate status
        mockMvc.perform(get(statusUrl + "/status")
                        .header("Authorization", serverToken))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseContent = result.getResponse().getContentAsString();
                    int status = JsonPath.read(responseContent, "$.data");
                    assertEquals(StatusConstant.ENABLE, status);
                });
    }
}
