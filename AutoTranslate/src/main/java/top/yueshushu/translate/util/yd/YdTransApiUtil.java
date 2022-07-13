package top.yueshushu.translate.util.yd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import top.yueshushu.translate.util.RestTemplateUtil;

/**
 * @ClassName:YdTransApiUtil
 * @Description: 有道翻译
 * @Author zk_yjl
 * @Date 2021/9/15 10:17
 * @Version 1.0
 * @Since 1.0
 **/
@Component
public class YdTransApiUtil {
    private static final String TRANS_API_HOST = "https://openapi.youdao.com/api";
    
    @Autowired
    private RestTemplateUtil restTemplateUtil;
    
    @Value("${yd.appId}")
    private String appid;
    @Value("${yd.securityKey}")
    private String securityKey;
    
    public YdTransApiUtil() {
    
    }
    
    public YdTransApiUtil(String appid, String securityKey) {
        this.appid = appid;
        this.securityKey = securityKey;
    }
    
    /**
     * 生成加密字段
     */
    public static String getDigest(String string) {
        if (string == null) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        byte[] btInput = string.getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest mdInst = MessageDigest.getInstance("SHA-256");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
    
    public static String truncate(String q) {
        if (q == null) {
            return null;
        }
        int len = q.length();
        String result;
        return len <= 20 ? q : (q.substring(0, 10) + len + q.substring(len - 10, len));
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
        params.put("signType", "v3");
        params.put("curtime", String.valueOf(System.currentTimeMillis() / 1000));
        params.put("appKey", appid);
        // 随机数
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);
        // 签名
        setSign(params, securityKey);
        return params;
    }
    
    /**
     * 获取标识
     *
     * @return java.lang.String
     * @date 2021/9/15 14:03
     * @author zk_yjl
     */
    private void setSign(Map<String, String> params, String securityKey) {
        
        // 加密前的原文
        String signStr = params.get("appKey") + truncate(
                params.get("q")
        ) + params.get("salt") + params.get("curtime") + securityKey;
        String sign = getDigest(signStr);
        params.put("sign", sign);
    }
}
