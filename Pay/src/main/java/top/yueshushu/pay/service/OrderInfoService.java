package top.yueshushu.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

import top.yueshushu.pay.dto.OrderInfoCondition;
import top.yueshushu.pay.enumtype.OrderStatus;
import top.yueshushu.pay.pojo.OrderInfo;
import top.yueshushu.pay.response.PageResponse;
import top.yueshushu.pay.vo.OrderInfoRequestVO;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author yuejianli
 * @since 2022-08-30
 */
public interface OrderInfoService extends IService<OrderInfo> {
	/**
	 * 查询信息
	 *
	 * @param nativeOrderInfo 查询对象
	 */
	OrderInfo getOrderInfoByCondition(OrderInfoCondition nativeOrderInfo);
	
	/**
	 * 更新二维码信息
	 *
	 * @param orderNo 订单号
	 * @param codeUrl 路径
	 */
	void updateCodeUrl(String orderNo, String codeUrl);
	
	/**
	 * 更新状态
	 *
	 * @param orderNo     订单号
	 * @param orderStatus 更新状态
	 */
	void updateStatus(String orderNo, OrderStatus orderStatus);
	
	/**
	 * 查询所有的订单信息
	 *
	 * @param orderInfoRequestVO 查询对象
	 */
	PageResponse<OrderInfo> listAll(OrderInfoRequestVO orderInfoRequestVO);
	
	/**
	 * 查询订单信息
	 *
	 * @param orderNo 订单编号
	 */
	OrderInfo getByOrder(String orderNo);
	
	/**
	 * 查询在 minute 分钟内未支付的订单
	 *
	 * @param minute  分钟
	 * @param payType 类型
	 */
	List<OrderInfo> getNoPayOrderByDuration(int minute, Integer payType);
}
