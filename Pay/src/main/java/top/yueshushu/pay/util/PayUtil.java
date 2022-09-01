package top.yueshushu.pay.util;

import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-08-31
 */
@Slf4j
public class PayUtil {
	
	public static int getFee(BigDecimal price) {
		return price.multiply(new BigDecimal(100)).intValue();
	}
	
	public static BigDecimal formatFee(int fee) {
		return new BigDecimal(fee).divide(new BigDecimal(100));
	}
	
	
	/**
	 * 对称解密
	 *
	 * @param bodyMap
	 * @return
	 */
	public static String decryptFromResource(Map<String, Object> bodyMap, String apiV3Key) throws GeneralSecurityException {
		
		log.info("密文解密");
		
		//通知数据
		Map<String, String> resourceMap = (Map) bodyMap.get("resource");
		//数据密文
		String ciphertext = resourceMap.get("ciphertext");
		//随机串
		String nonce = resourceMap.get("nonce");
		//附加数据
		String associatedData = resourceMap.get("associated_data");
		
		log.info("密文 ===> {}", ciphertext);
		AesUtil aesUtil = new AesUtil(apiV3Key.getBytes(StandardCharsets.UTF_8));
		String plainText = aesUtil.decryptToString(associatedData.getBytes(StandardCharsets.UTF_8),
				nonce.getBytes(StandardCharsets.UTF_8),
				ciphertext);
		
		log.info("明文 ===> {}", plainText);
		
		return plainText;
	}
}
