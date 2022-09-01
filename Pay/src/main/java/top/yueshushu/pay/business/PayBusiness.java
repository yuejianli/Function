package top.yueshushu.pay.business;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import top.yueshushu.pay.pojo.OrderInfo;
import top.yueshushu.pay.pojo.RefundInfo;
import top.yueshushu.pay.response.OutputResult;
import top.yueshushu.pay.vo.*;

/**
 * TODO 用途描述
 *
 * @author Yue Jianli
 * @date 2022-08-30
 */

public interface PayBusiness {
	/**
	 * 下单请求
	 *
	 * @param nativePayVO 下单请求
	 */
	OutputResult<NativePayResponseVO> nativePay(NativePayRequestVO nativePayVO);
	
	/**
	 * 下单之后，回调函数
	 *
	 * @param httpServletRequest 请求信息
	 */
	OutputResult wxNativeNotify(HttpServletRequest httpServletRequest);
	
	/**
	 * 支付宝参数信息
	 *
	 * @param params 支付宝支付成功后参数
	 */
	OutputResult aliNativeNotify(Map<String, String> params);
	
	/**
	 * 下单之后，回调函数。
	 *
	 * @param httpServletRequest 微信 apiv2 的回调
	 */
	OutputResult wx2NativeNotify(HttpServletRequest httpServletRequest);
	
	/**
	 * 取消支付
	 *
	 * @param cancelOrderRequestVO 支付对象
	 */
	OutputResult cancelOrder(CancelOrderRequestVO cancelOrderRequestVO);
	
	/**
	 * 查询订单
	 *
	 * @param orderNo 订单编号
	 */
	OutputResult queryOrder(String orderNo);
	
	/**
	 * 进行退款
	 *
	 * @param refundsInfoRequestVO 退款
	 */
	OutputResult refunds(RefundsInfoRequestVO refundsInfoRequestVO);
	
	/**
	 * 根据退款编号，查询退款信息
	 *
	 * @param refundNo 退款信息
	 */
	OutputResult queryRefunds(String refundNo);
	
	/**
	 * 退款成功之后，进行回调。
	 *
	 * @param httpServletRequest 退款请求
	 */
	OutputResult wxRefundsNotify(HttpServletRequest httpServletRequest);
	
	/**
	 * 查询账单的 url 地址
	 *
	 * @param billRequestVO 账单对象
	 */
	String wxQueryTradeBill(BillRequestVO billRequestVO);
	
	/**
	 * 下载账单
	 *
	 * @param billRequestVO 账单对象
	 */
	String wxDownloadTradeBill(BillRequestVO billRequestVO);
	
	/**
	 * 处理未支付的订单信息
	 *
	 * @param orderInfo 订单对象
	 */
	void handlerNotPay(OrderInfo orderInfo);
	
	/**
	 * 处理 退款中的订单信息
	 *
	 * @param refundInfo 退款对象
	 */
	void handlerRefundProcessing(RefundInfo refundInfo);
	
	/**
	 * 查询支付宝下载单子
	 *
	 * @param billRequestVO 下载单子
	 */
	String aliQueryTradeBill(BillRequestVO billRequestVO);
	
	/**
	 * 处理阿里未支付的信息
	 *
	 * @param orderInfo 订单信息
	 */
	void handlerAliNotPay(OrderInfo orderInfo);
}
