package top.yueshushu.pay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import cn.hutool.core.date.DateUtil;
import top.yueshushu.pay.enumtype.DataFlagEnum;
import top.yueshushu.pay.enumtype.wxpay.WxRefundStatus;
import top.yueshushu.pay.mapper.RefundInfoMapper;
import top.yueshushu.pay.pojo.OrderInfo;
import top.yueshushu.pay.pojo.RefundInfo;
import top.yueshushu.pay.service.RefundInfoService;
import top.yueshushu.pay.util.OrderNoUtils;

/**
 * <p>
 * 退款单信息 服务实现类
 * </p>
 *
 * @author yuejianli
 * @since 2022-08-30
 */
@Service
public class RefundInfoServiceImpl extends ServiceImpl<RefundInfoMapper, RefundInfo> implements RefundInfoService {
	
	@Override
	public RefundInfo createRefunds(OrderInfo orderInfo, String reason) {
		RefundInfo refundInfo = new RefundInfo();
		
		refundInfo.setOrderNo(orderInfo.getOrderNo());
		refundInfo.setRefundNo(OrderNoUtils.getRefundNo());
		refundInfo.setTotalFee(orderInfo.getTotalFee());
		refundInfo.setRefund(orderInfo.getTotalFee());
		refundInfo.setReason(reason);
		refundInfo.setRefundStatus(WxRefundStatus.PROCESSING.getCode());
		refundInfo.setCreateId(1);
		refundInfo.setCreateTime(DateUtil.date());
		refundInfo.setUpdateId(1);
		refundInfo.setUpdateTime(DateUtil.date());
		refundInfo.setFlag(DataFlagEnum.VALID.getValue());
		save(refundInfo);
		return refundInfo;
	}
	
	@Override
	public RefundInfo getByRefundNo(String refundNo) {
		return this.lambdaQuery()
				.eq(RefundInfo::getRefundNo, refundNo)
				.one();
	}
	
	@Override
	public void updateRefunds(Map<String, Object> paramMap) {
		String content = JSONObject.toJSONString(paramMap);
		String refundNo = paramMap.get("out_refund_no").toString();
		RefundInfo refundInfo = getByRefundNo(refundNo);
		String refundId = paramMap.get("refund_id").toString();
		refundInfo.setRefundId(refundId);
		// 查询退款和申请退款中的返回参数
		if (!ObjectUtils.isEmpty(paramMap.get("status"))) {
			refundInfo.setRefundStatus(WxRefundStatus.getByValue(paramMap.get("status").toString()).getCode());
			refundInfo.setContentReturn(content);
		}
		if (!ObjectUtils.isEmpty(paramMap.get("refund_status"))) {
			refundInfo.setRefundStatus(WxRefundStatus.getByValue(paramMap.get("refund_status").toString()).getCode());
			refundInfo.setContentNotify(content);
		}
		updateById(refundInfo);
	}
	
	@Override
	public List<RefundInfo> getNoRefundOrderByDuration(int minutes) {
		//minutes分钟之前的时间
		Instant instant = Instant.now().minus(Duration.ofMinutes(minutes));
		
		QueryWrapper<RefundInfo> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("refund_status", WxRefundStatus.PROCESSING.getCode());
		queryWrapper.le("create_time", instant);
		List<RefundInfo> refundInfoList = baseMapper.selectList(queryWrapper);
		return refundInfoList;
	}
}
