package top.yueshushu.translate.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName:RestTemplateUtils
 * @Description TODO
 * @Author zk_yjl
 * @Date 2021/9/15 10:20
 * @Version 1.0
 * @Since 1.0
 **/
@Component
@Slf4j
public class RestTemplateUtil {
    @Autowired
    private RestTemplate restTemplate;
    
    public String getTranslate(String url, Map<String, String> params) {
        String realUrl = QueryParamUtil.getUrlWithQueryString(url, params);
        HttpEntity<String> httpEntity = restTemplate.getForEntity(realUrl, String.class, params);
        String content = httpEntity.getBody();
        return content;
    }
}
