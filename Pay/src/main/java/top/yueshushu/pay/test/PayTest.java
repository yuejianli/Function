package top.yueshushu.pay.test;

import com.alibaba.fastjson.JSONObject;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;

import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileInputStream;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import top.yueshushu.pay.business.PayBusiness;
import top.yueshushu.pay.config.WxPayBean;
import top.yueshushu.pay.enumtype.OrderStatus;
import top.yueshushu.pay.enumtype.PayType;
import top.yueshushu.pay.pojo.OrderInfo;
import top.yueshushu.pay.response.OutputResult;
import top.yueshushu.pay.response.PageResponse;
import top.yueshushu.pay.service.OrderInfoService;
import top.yueshushu.pay.service.PaymentInfoService;
import top.yueshushu.pay.service.RefundInfoService;
import top.yueshushu.pay.vo.*;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-08-30
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class PayTest {
	@Resource
	private WxPayBean wxPayBean;
	@Resource
	private PayBusiness payBusiness;
	@Resource
	private OrderInfoService orderInfoService;
	@Resource
	private PaymentInfoService paymentInfoService;
	@Resource
	private RefundInfoService refundInfoService;
	
	/**
	 * 获取商家的私钥
	 */
	@Test
	public void getPrivateKey() throws Exception {
		String filepath = "apiclient_key.pem";
		
		PrivateKey privateKey = PemUtil.loadPrivateKey(new FileInputStream(filepath));
		
		log.info(">>> 获取私有 PrivateKey {}", privateKey);
		
	}
	
	/**
	 * 构建 http 验证签名对象
	 */
	@Test
	public void getWxPayClientTest() {
		CloseableHttpClient wxPayClient = wxPayBean.getWxPayClient(wxPayBean.getVerifier());
		log.info(">>>验证签名对象:{}", wxPayClient);
	}
	
	/**
	 * 支付测试
	 */
	@Test
	public void nativePayTest() {
		NativePayRequestVO nativePayRequestVO = new NativePayRequestVO();
		nativePayRequestVO.setProductId(1);
		nativePayRequestVO.setPayType(PayType.WXPAY.getCode());
		nativePayRequestVO.setApiType(3);
		
		OutputResult<NativePayResponseVO> nativePayResponseVOOutputResult = payBusiness.nativePay(nativePayRequestVO);
		
		log.info(">>>获取返回信息:{}", nativePayResponseVOOutputResult.getData());
	}
	
	@Test
	public void orderInfoList() {
		
		OrderInfoRequestVO orderInfoRequestVO = new OrderInfoRequestVO();
		orderInfoRequestVO.setCustomerId(1);
		orderInfoRequestVO.setPageSize(10);
		orderInfoRequestVO.setPageNum(1);
		
		PageResponse<OrderInfo> orderInfoPageResponse = orderInfoService.listAll(orderInfoRequestVO);
		
		log.info(">>>查询信息:{}", orderInfoPageResponse);
	}
	
	/**
	 * 支付成功后通知测试
	 */
	@Test
	public void nativeNotifyTest() {
		
		
		String plainText = "{\"amount\":{\"currency\":\"CNY\",\"payer_currency\":\"CNY\",\"payer_total\":1,\"total\":1}," +
				"\"appid\":\"wx74862e0dfcf69954\",\"attach\":\"\",\"bank_type\":\"OTHERS\",\"mchid\":\"1558950191\"," +
				"\"out_trade_no\":\"01564781607194226688\",\"payer\":{\"openid\":\"oHwsHuEpuA7tEM4GPGtBiKpjAcqM\"}," +
				"\"promotion_detail\":[],\"success_time\":\"2021-12-14T13:06:16+08:00\"," +
				"\"trade_state\":\"SUCCESS\",\"trade_state_desc\":\"支付成功\",\"trade_type\":\"NATIVE\"," +
				"\"transaction_id\":\"4200001335202112149645887495\"}";
		
		// 对信息进行转换，转换成 map
		Map<String, Object> paramMap = JSONObject.parseObject(plainText, HashMap.class);
		
		//获取编号
		String orderNo = paramMap.get("out_trade_no").toString();
		
		try {
			//处理重复的通知
			//接口调用的幂等性：无论接口被调用多少次，产生的结果是一致的。
			// 进行判断， 该编号是否是运行中.
			OrderInfo orderInfo = orderInfoService.getByOrder(orderNo);
			if (orderInfo == null || !orderInfo.getOrderStatus().equals(OrderStatus.NOTPAY.getCode())) {
				return;
			}
			
			//模拟通知并发
//			try {
//				TimeUnit.SECONDS.sleep(5);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
			
			//更新订单状态
			orderInfoService.updateStatus(orderNo, OrderStatus.SUCCESS);
			
			//记录支付日志
			paymentInfoService.insertPaymentInfo(paramMap);
		} finally {
		
		}
	}
	
	@Test
	public void orderInfoTest() {
		String orderNo = "ORDER_20220901114659163";
		
		OrderInfo orderInfo = orderInfoService.getByOrder(orderNo);
		
		log.info(">>> 查询订单的状态:{},{}", orderInfo.getOrderStatus(), OrderStatus.getByValue(orderInfo.getOrderStatus()).getName());
	}
	
	/**
	 * 取消支付
	 */
	@Test
	public void cancelOrderTest() {
		String orderNo = "ORDER_20220901114659163";
		CancelOrderRequestVO cancelOrderRequestVO = new CancelOrderRequestVO();
		cancelOrderRequestVO.setOrderNo(orderNo);
		OutputResult outputResult = payBusiness.cancelOrder(cancelOrderRequestVO);
		log.info("取消支付响应:{}", outputResult);
	}
	
	@Test
	public void queryOrderTest() {
		String orderNo = "ORDER_20220901114659163";
		OutputResult outputResult = payBusiness.queryOrder(orderNo);
		log.info("查询订单响应:{}", outputResult);
	}
	
	/**
	 * 退款操作
	 */
	@Test
	public void refundsTest() {
		String orderNo = "ORDER_20220901114659163";
		RefundsInfoRequestVO refundsInfoRequestVO = new RefundsInfoRequestVO();
		refundsInfoRequestVO.setOrderNo(orderNo);
		refundsInfoRequestVO.setReason("不想买了");
		OutputResult outputResult = payBusiness.refunds(refundsInfoRequestVO);
		log.info("查询退款响应:{}", outputResult);
		
		
	}
	
	/**
	 * 查询退款信息
	 */
	@Test
	public void queryRefundsTest() {
		RefundsInfoRequestVO refundsInfoRequestVO = new RefundsInfoRequestVO();
		String orderNo = "REFUND_20220901135030069";
		refundsInfoRequestVO.setRefundNo(orderNo);
		OutputResult outputResult = payBusiness.queryRefunds(refundsInfoRequestVO.getRefundNo());
		log.info("查询退款信息响应:{}", outputResult);
	}
	
	/**
	 * 退款响应信息
	 */
	@Test
	public void wxRefundsNotifyTest() {
		String plainText = "{\"amount\":{\"currency\":\"CNY\",\"discount_refund\":0,\"from\":[],\"payer_refund\":1,\"payer_total\":1,\"refund\":1,\"settlement_refund\":1,\"settlement_total\":1," +
				"\"total\":1},\"channel\":\"ORIGINAL\",\"create_time\":\"2021-12-14T18:48:18+08:00\",\"funds_account\":\"AVAILABLE\"," +
				"\"out_refund_no\":\"REFUND_20220901135030069\",\"out_trade_no\":\"ORDER_20220901114659163\",\"promotion_detail\":[],\"refund_id\":\"50301000072021121415364123490\"," +
				"\"status\":\"PROCESSING\",\"transaction_id\":\"4200001184202112147549577350\",\"user_received_account\":\"支付用户零钱\"}";
		// 对信息进行转换，转换成 map
		Map<String, Object> paramMap = JSONObject.parseObject(plainText, HashMap.class);
		
		//获取编号
		String orderNo = paramMap.get("out_trade_no").toString();
		// 进行判断， 该编号是否是运行中.
		OrderInfo orderInfo = orderInfoService.getByOrder(orderNo);
		if (orderInfo == null || !OrderStatus.REFUND_PROCESSING.getCode().equals(orderInfo.getOrderStatus())) {
		
		}
		//更新订单状态
		orderInfoService.updateStatus(orderNo, OrderStatus.REFUND_SUCCESS);
		
		//更新退款信息
		refundInfoService.updateRefunds(paramMap);
	}
	
	/**
	 * 查询订单url
	 */
	@Test
	public void wxQueryTradeBillTest() {
		BillRequestVO billRequestVO = new BillRequestVO();
		billRequestVO.setBillDate("2022-08-14");
		billRequestVO.setType("tradebill");
		String url = payBusiness.wxQueryTradeBill(billRequestVO);
		log.info(">>> url 地址:{}", url);
	}
	
	@Test
	public void wxDownloadTradeBillTest() {
		BillRequestVO billRequestVO = new BillRequestVO();
		billRequestVO.setBillDate("2022-08-14");
		billRequestVO.setType("tradebill");
		String url = payBusiness.wxDownloadTradeBill(billRequestVO);
		log.info(">>> url 地址:{}", url);
	}
}
