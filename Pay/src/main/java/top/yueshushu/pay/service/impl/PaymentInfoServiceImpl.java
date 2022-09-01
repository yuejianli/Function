package top.yueshushu.pay.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

import cn.hutool.core.date.DateUtil;
import top.yueshushu.pay.enumtype.DataFlagEnum;
import top.yueshushu.pay.enumtype.PayType;
import top.yueshushu.pay.enumtype.TradeStateEnum;
import top.yueshushu.pay.enumtype.TradeTypeEnum;
import top.yueshushu.pay.mapper.PaymentInfoMapper;
import top.yueshushu.pay.pojo.PaymentInfo;
import top.yueshushu.pay.service.PaymentInfoService;
import top.yueshushu.pay.util.PayUtil;

/**
 * <p>
 * 支付信息表 服务实现类
 * </p>
 *
 * @author yuejianli
 * @since 2022-08-30
 */
@Service
public class PaymentInfoServiceImpl extends ServiceImpl<PaymentInfoMapper, PaymentInfo> implements PaymentInfoService {
	
	@Override
	public void insertPaymentInfo(Map<String, Object> plainTextMap) {
		
		
		//订单号
		String orderNo = (String) plainTextMap.get("out_trade_no");
		//业务编号
		String transactionId = (String) plainTextMap.get("transaction_id");
		//支付类型
		String tradeType = (String) plainTextMap.get("trade_type");
		//交易状态
		String tradeState = (String) plainTextMap.get("trade_state");
		//用户实际支付金额
		Map<String, Object> amount = (Map) plainTextMap.get("amount");
		Integer payerTotal = Integer.parseInt(amount.get("payer_total").toString());
		
		PaymentInfo paymentInfo = new PaymentInfo();
		paymentInfo.setOrderNo(orderNo);
		paymentInfo.setPaymentType(PayType.WXPAY.getCode());
		paymentInfo.setTransactionId(transactionId);
		paymentInfo.setTradeType(TradeTypeEnum.getByValue(tradeType).getValue());
		paymentInfo.setTradeState(TradeStateEnum.getByValue(tradeState).getValue());
		paymentInfo.setPayerTotal(PayUtil.formatFee(payerTotal));
		paymentInfo.setContent(JSONObject.toJSONString(plainTextMap));
		paymentInfo.setCreateId(1);
		paymentInfo.setCreateTime(DateUtil.date());
		paymentInfo.setUpdateId(1);
		paymentInfo.setUpdateTime(DateUtil.date());
		paymentInfo.setFlag(DataFlagEnum.VALID.getValue());
		baseMapper.insert(paymentInfo);
	}
	
	@Override
	public void insertAliPaymentInfo(Map<String, String> plainTextMap) {
		//订单号
		String orderNo = plainTextMap.get("out_trade_no");
		//业务编号
		String transactionId = plainTextMap.get("trade_no");
		//支付类型
		//交易状态
		String tradeState = plainTextMap.get("trade_status");
		//用户实际支付金额
		String totalAmount = plainTextMap.get("total_amount");
		int totalAmountInt = new BigDecimal(totalAmount).multiply(new BigDecimal("100")).intValue();
		
		PaymentInfo paymentInfo = new PaymentInfo();
		paymentInfo.setOrderNo(orderNo);
		paymentInfo.setPaymentType(PayType.WXPAY.getCode());
		paymentInfo.setTransactionId(transactionId);
		paymentInfo.setTradeType(TradeTypeEnum.WEB.getValue());
		paymentInfo.setTradeState(TradeStateEnum.getByValue(tradeState).getValue());
		paymentInfo.setPayerTotal(PayUtil.formatFee(totalAmountInt));
		paymentInfo.setContent(JSONObject.toJSONString(plainTextMap));
		paymentInfo.setCreateId(1);
		paymentInfo.setCreateTime(DateUtil.date());
		paymentInfo.setUpdateId(1);
		paymentInfo.setUpdateTime(DateUtil.date());
		paymentInfo.setFlag(DataFlagEnum.VALID.getValue());
		baseMapper.insert(paymentInfo);
	}
}
