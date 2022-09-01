package top.yueshushu.pay.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import top.yueshushu.pay.pojo.OrderInfo;
import top.yueshushu.pay.pojo.RefundInfo;
import top.yueshushu.pay.vo.BillRequestVO;

/**
 * 支付接口信息
 *
 * @author yuejianli
 * @date 2022-08-30
 */

public interface IPayService {
	/**
	 * 根据订单对象，进行交易。 返回对应的二维码信息
	 *
	 * @param orderInfo 订单对象
	 */
	String nativePay(OrderInfo orderInfo) throws Exception;
	
	/**
	 * 支付之后，通知信息
	 *
	 * @param httpServletRequest 请求信息
	 */
	Map<String, Object> nativeNotify(HttpServletRequest httpServletRequest);
	
	/**
	 * 取消支付
	 *
	 * @param orderNo 订单编号
	 */
	boolean cancelOrder(String orderNo);
	
	/**
	 * 根据订单编号，查询订单信息
	 *
	 * @param orderNo 订单编号
	 */
	String queryOrder(String orderNo);
	
	/**
	 * 进行退款处理
	 *
	 * @param refundInfo 退款单信息
	 */
	String refunds(RefundInfo refundInfo);
	
	/**
	 * 根据退款单编号 查询退款信息
	 *
	 * @param refundNo 退款单编号
	 */
	String queryRefunds(String refundNo);
	
	/**
	 * 退款成功之后，进行通信。
	 *
	 * @param httpServletRequest 请求通知信息
	 */
	Map<String, Object> refundsNotify(HttpServletRequest httpServletRequest);
	
	/**
	 * 查询账单的url
	 *
	 * @param billRequestVO 账单url
	 */
	String queryTradeBill(BillRequestVO billRequestVO);
	
	/**
	 * 下载账单的url
	 *
	 * @param billRequestVO 账单url
	 */
	String downloadTradeBill(BillRequestVO billRequestVO);
	
	/**
	 * 支付宝验证
	 *
	 * @param params 参数验证
	 */
	boolean rsaCheckV1(Map<String, String> params);
}
