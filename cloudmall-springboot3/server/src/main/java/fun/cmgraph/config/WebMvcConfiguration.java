package fun.cmgraph.config;

import fun.cmgraph.interceptor.JwtTokenAdminInterceptor;
import fun.cmgraph.interceptor.JwtTokenUserInterceptor;
import fun.cmgraph.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import java.util.List;

@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;
    @Autowired
    private JwtTokenUserInterceptor jwtTokenUserInterceptor;
    /**
     * 配置，添加自定义拦截器
     * @param registry
     */
    public void addInterceptors(InterceptorRegistry registry){
        log.info("自定义好了拦截器，还要在这个WebMvc配置类里注册");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/employee/login")
                .excludePathPatterns("/admin/employee/register");
        registry.addInterceptor(jwtTokenUserInterceptor)
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/user/login")
                .excludePathPatterns("/user/shop/status");

    }

    /**
     * 静态资源配置
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

   /**
    * 扩展Spring MVC框架的消息转化器，用于格式化时间等
    * @param converters
    */
   @Override
   protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
       log.info("扩展消息转换器...");
       //创建一个消息转换器对象
       MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
       //需要为消息转换器设置一个对象转换器，对象转换器可以将Java对象序列化为json数据
       converter.setObjectMapper(new JacksonObjectMapper());
       //将自己的消息转化器加入容器中
       converters.add(0, converter);
   }
}
