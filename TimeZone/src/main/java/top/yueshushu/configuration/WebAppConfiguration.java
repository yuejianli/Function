package top.yueshushu.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.yueshushu.interceptor.AuthorizationInterceptor;

/**
 * Created by zk on 2019/1/10.
 */
@Configuration
public class WebAppConfiguration implements WebMvcConfigurer {

    @Bean
    public HandlerInterceptor getAuthInterceptor() {
        //返回自定义的拦截类
        return new AuthorizationInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getAuthInterceptor()).addPathPatterns("/**").excludePathPatterns("/user/login", "/user/creatCode", "/user/getPublicKey", "/user/getNewPublicKey", "/v2/**", "/swagger-resources/configuration/**",
                "/swagger-resources/**", "/swagger-ui.html#/**", "/webjars/**", "/agency/", "/translate/getTanslateByType");
    }
}


