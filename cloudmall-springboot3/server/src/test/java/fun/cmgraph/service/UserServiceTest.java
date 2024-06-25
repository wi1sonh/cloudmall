package fun.cmgraph.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import fun.cmgraph.properties.WeChatProperties;
import fun.cmgraph.dto.*;
import fun.cmgraph.utils.HttpClientUtil;
import fun.cmgraph.entity.User;
import fun.cmgraph.mapper.UserMapper;
import fun.cmgraph.service.serviceImpl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserMapper userMapper;
    @Mock
    private WeChatProperties weChatProperties;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeAll
    static void setUp() {
        Mockito.mockStatic(HttpClientUtil.class);
    }

    @AfterAll
    static void tearDown() {
        Mockito.clearAllCaches();
    }

    @Test
    void wxLogin() {
        when(weChatProperties.getAppid()).thenReturn("appid");
        when(weChatProperties.getSecret()).thenReturn("secret");

        // Mockito.mockStatic(HttpClientUtil.class);
        when(HttpClientUtil.doGet(anyString(), any())).thenReturn("{\"openid\":\"openid\"}");

        User testUser = new User(1, "test", "openid", "12345678910", 1, "1", "pic", null);
        when(userMapper.getByOpenid("openid")).thenReturn(testUser);

        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setCode("code");

        User user = userService.wxLogin(userLoginDTO);
        assertEquals(user, testUser);
        // 清除所有缓存
        // Mockito.clearAllCaches();
    }

    @Test
    void getUser() {
        User testUser = new User(1, "test", "openid", "12345678910", 1, "1", "pic", null);
        when(userMapper.getById(1)).thenReturn(testUser);

        User user = userService.getUser(1);
        assertEquals(user, testUser);
    }

    @Test
    void update() {
        UserDTO userDTO = new UserDTO(1, "test", "12345678910", 1, "pic");

        userService.update(userDTO);
        verify(userMapper, times(1)).update(any());
    }
}