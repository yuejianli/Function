package top.yueshushu.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

import top.yueshushu.pay.pojo.PaymentInfo;

/**
 * <p>
 * 支付信息表 服务类
 * </p>
 *
 * @author yuejianli
 * @since 2022-08-30
 */
public interface PaymentInfoService extends IService<PaymentInfo> {
	/**
	 * 微信插入到 支付信息里面
	 *
	 * @param paramMap 微信对象参数
	 */
	void insertPaymentInfo(Map<String, Object> paramMap);
	
	/**
	 * 支付宝 插入到支付信息里面
	 *
	 * @param params 支付宝参数
	 */
	void insertAliPaymentInfo(Map<String, String> params);
}
