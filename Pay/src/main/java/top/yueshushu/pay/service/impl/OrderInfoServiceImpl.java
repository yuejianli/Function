package top.yueshushu.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import javax.annotation.Resource;

import cn.hutool.core.date.DateUtil;
import top.yueshushu.pay.dto.OrderInfoCondition;
import top.yueshushu.pay.enumtype.DataFlagEnum;
import top.yueshushu.pay.enumtype.OrderStatus;
import top.yueshushu.pay.mapper.OrderInfoMapper;
import top.yueshushu.pay.mapper.ProductMapper;
import top.yueshushu.pay.pojo.OrderInfo;
import top.yueshushu.pay.pojo.Product;
import top.yueshushu.pay.response.PageResponse;
import top.yueshushu.pay.service.OrderInfoService;
import top.yueshushu.pay.util.OrderNoUtils;
import top.yueshushu.pay.vo.OrderInfoRequestVO;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author yuejianli
 * @since 2022-08-30
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {
	@Resource
	private ProductMapper productMapper;
	
	@Override
	public OrderInfo getOrderInfoByCondition(OrderInfoCondition nativeOrderInfo) {
		//1. 查询信息
		
		OrderInfo orderInfo = this.lambdaQuery()
				.eq(OrderInfo::getCustomerId, nativeOrderInfo.getCustomerId())
				.eq(OrderInfo::getProductId, nativeOrderInfo.getProductId())
				.eq(OrderInfo::getPaymentType, nativeOrderInfo.getPaymentType())
				.eq(OrderInfo::getOrderStatus, nativeOrderInfo.getOrderStatus().getCode())
				.one();
		
		if (orderInfo != null) {
			return orderInfo;
		}
		if (nativeOrderInfo.getEmptyInsert()) {
			return insertOrderInfo(nativeOrderInfo);
		}
		return null;
	}
	
	@Override
	public void updateCodeUrl(String orderNo, String codeUrl) {
		
		LambdaUpdateWrapper<OrderInfo> lambdaUpdateWrapper = new LambdaUpdateWrapper();
		lambdaUpdateWrapper.set(OrderInfo::getCodeUrl, codeUrl)
				.eq(OrderInfo::getOrderNo, orderNo);
		update(lambdaUpdateWrapper);
	}
	
	@Override
	public void updateStatus(String orderNo, OrderStatus orderStatus) {
		LambdaUpdateWrapper<OrderInfo> lambdaUpdateWrapper = new LambdaUpdateWrapper();
		lambdaUpdateWrapper.set(OrderInfo::getOrderStatus, orderStatus.getCode())
				.eq(OrderInfo::getOrderNo, orderNo);
		update(lambdaUpdateWrapper);
	}
	
	@Override
	public PageResponse<OrderInfo> listAll(OrderInfoRequestVO orderInfoRequestVO) {
		// 处理信息
		Page<OrderInfo> pageInfo = PageHelper.startPage(orderInfoRequestVO.getPageNum(), orderInfoRequestVO.getPageSize());
		// 查询全部信息
		
		List<OrderInfo> list = this.lambdaQuery()
				.eq(OrderInfo::getCustomerId, orderInfoRequestVO.getCustomerId())
				.eq(OrderInfo::getFlag, DataFlagEnum.VALID.getValue())
				.list();
		return new PageResponse<>(pageInfo.getTotal(), list);
	}
	
	@Override
	public OrderInfo getByOrder(String orderNo) {
		return this.lambdaQuery()
				.eq(OrderInfo::getOrderNo, orderNo)
				.one();
	}
	
	@Override
	public List<OrderInfo> getNoPayOrderByDuration(int minute, Integer payType) {
		Instant instant = Instant.now().minus(Duration.ofMinutes(minute));
		
		QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("order_status", OrderStatus.NOTPAY.getCode());
		queryWrapper.le("create_time", instant);
		queryWrapper.eq("payment_type", payType);
		return baseMapper.selectList(queryWrapper);
	}
	
	private OrderInfo insertOrderInfo(OrderInfoCondition nativeOrderInfo) {
		
		Product product = productMapper.selectById(nativeOrderInfo.getProductId());
		
		
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setOrderNo(OrderNoUtils.getOrderNo());
		orderInfo.setTitle(product.getTitle());
		orderInfo.setProductId(nativeOrderInfo.getProductId());
		orderInfo.setCustomerId(nativeOrderInfo.getCustomerId());
		orderInfo.setPaymentType(nativeOrderInfo.getPaymentType());
		orderInfo.setTotalFee(product.getPrice());
		orderInfo.setOrderStatus(nativeOrderInfo.getOrderStatus().getCode());
		orderInfo.setIpAddr(nativeOrderInfo.getIpAddr());
		orderInfo.setCreateId(nativeOrderInfo.getCustomerId());
		orderInfo.setCreateTime(DateUtil.date());
		orderInfo.setUpdateId(nativeOrderInfo.getCustomerId());
		orderInfo.setUpdateTime(DateUtil.date());
		orderInfo.setFlag(DataFlagEnum.VALID.getValue());
		
		save(orderInfo);
		
		return orderInfo;
	}
}
