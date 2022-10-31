package top.yueshushu;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yuejianli
 * @date 2022-10-31
 */
@SpringBootApplication
@Slf4j
@MapperScan(value = "top.yueshushu.mapper")
public class TimeZoneApp {
    public static void main(String[] args) {
        SpringApplication.run(TimeZoneApp.class, args);
        log.info(">>> 时区转换");
    }
}
