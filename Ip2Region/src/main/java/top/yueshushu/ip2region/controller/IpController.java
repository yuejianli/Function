package top.yueshushu.ip2region.controller;

import com.alibaba.fastjson.JSONObject;

import net.dreamlu.mica.ip2region.core.Ip2regionSearcher;
import net.dreamlu.mica.ip2region.core.IpInfo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import top.yueshushu.ip2region.model.TaoBaoIp;

/**
 * IP地址处理信息
 *
 * @author yuejianli
 * @date 2022-07-25
 */
@RestController
@Slf4j
public class IpController {
	private static final String UNKNOWN = "unknown";
	private static final String TAOBAO_IP = "https://ip.taobao.com/outGetIpInfo?ip=";
	
	@Resource
	private RestTemplate restTemplate;
	
	@Resource
	Ip2regionSearcher ip2regionSearcher;
	
	/**
	 获取IP
	 */
	@RequestMapping("/getIp")
	public String getIp(HttpServletRequest httpServletRequest){
		String ip = getIpByRequest(httpServletRequest);
		log.info(">>> ip is:{}",ip);
		return ip;
	}
	
	/**
	 aoBaoIp(ip=139.155.90.176, country=中国, area=, region=北京, city=北京, isp=XX, country_id=CN, area_id=,
	 region_id=110000, city_id=110100, isp_id=xx)
	 还不稳定，还不正确。
	 */
	// 139.155.90.176
	@RequestMapping("/getAddress/{ip}")
	public String getAddressByTao(@PathVariable("ip") String ip){
		log.info("get address by ip:{}",ip);
		ResponseEntity<String> ipEntity = restTemplate.getForEntity(TAOBAO_IP + ip, String.class);
		// 进行解析
		JSONObject jsonObject = JSONObject.parseObject(ipEntity.getBody());
		//判断
		
		if (jsonObject.getIntValue("code")==0){
			// 将信息转换
			TaoBaoIp taoBaoIp = JSONObject.parseObject(jsonObject.get("data").toString(), TaoBaoIp.class);
			return taoBaoIp.toString();
		}else{
			log.info(">>> get result is fail,code value is :{}",jsonObject.getIntValue("code"));
			return UNKNOWN;
		}
	}
	
	/**
	 139.155.90.176
	 IpInfo(cityId=null, country=中国, region=null, province=四川, city=成都, isp=电信, dataPtr=153719)
	 稳定，且正确。
	 */
	@RequestMapping("/getAddress2/{ip}")
	public String getAddressByIp2(@PathVariable("ip") String ip){
		log.info("get address by ip:{}",ip);
		
		IpInfo ipInfo = ip2regionSearcher.memorySearch(ip);
		if (null == ipInfo){
			return UNKNOWN;
		}
		return ipInfo.toString();
	}
	
	
	
	/**
	 * 根据请求获取ip地址
	 */
	private  String getIpByRequest(HttpServletRequest request) {
		// x-forwarded-for 代理客户端， HTTP请求的真实ip. 通过 http 代理或者负载均衡时才会添加
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		String comma = ",";
		String localhost = "127.0.0.1";
		if (ip.contains(comma)) {
			ip = ip.split(",")[0];
		}
		if (localhost.equals(ip)) {
			// 获取本机真正的ip地址
			try {
				ip = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
			}
		}
		return ip;
	}
}
