package top.yueshushu.jep.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.yueshushu.jep.model.User;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2023-05-31
 */
@Configuration
public class BeanConfig {
    @Bean("iocUser")
    public User iocUser(){
        return new User("IOC用户");
    }
}
