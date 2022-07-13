package top.yueshushu.translate.util.bd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import top.yueshushu.translate.util.RestTemplateUtil;

/**
 * @ClassName:BdTransApiUtil
 * @Description 百度翻译
 * @Author zk_yjl
 * @Date 2021/9/15 10:17
 * @Version 1.0
 * @Since 1.0
 **/
@Component
public class BdTransApiUtil {
    private static final String TRANS_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/translate";
    @Autowired
    private RestTemplateUtil restTemplateUtil;
    
    @Value("${bd.appId}")
    private String appid;
    @Value("${bd.securityKey}")
    private String securityKey;
    
    public BdTransApiUtil() {
    
    }
    
    public BdTransApiUtil(String appid, String securityKey) {
        this.appid = appid;
        this.securityKey = securityKey;
    }
    
    public String getTranslate(String query, String from, String to) {
        Map<String, String> params = buildParams(query, from, to);
        return restTemplateUtil.getTranslate(TRANS_API_HOST, params);
    }
    
    private Map<String, String> buildParams(String query, String from, String to) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("q", query);
        params.put("from", from);
        params.put("to", to);
        
        params.put("appid", appid);
        // 随机数
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);
        // 签名
        // 加密前的原文
        String src = appid + query + salt + securityKey;
        params.put("sign", MD5.md5(src));
        return params;
    }
}
