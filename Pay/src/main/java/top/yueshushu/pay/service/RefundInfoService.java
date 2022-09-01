package top.yueshushu.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

import top.yueshushu.pay.pojo.OrderInfo;
import top.yueshushu.pay.pojo.RefundInfo;

/**
 * <p>
 * 退款单信息 服务类
 * </p>
 *
 * @author yuejianli
 * @since 2022-08-30
 */
public interface RefundInfoService extends IService<RefundInfo> {
	
	/**
	 * 创建退款单
	 *
	 * @param orderInfo 订单对象
	 * @param reason    退款原因
	 */
	RefundInfo createRefunds(OrderInfo orderInfo, String reason);
	
	/**
	 * 根据退款单编号 查询退款信息
	 *
	 * @param refundNo 退款单编号
	 */
	RefundInfo getByRefundNo(String refundNo);
	
	/**
	 * 退款通知之后，更新信息。
	 *
	 * @param paramMap 参数信息
	 */
	void updateRefunds(Map<String, Object> paramMap);
	
	/**
	 * 查询分钟内未成功的 退款单
	 *
	 * @param minutes 分钟
	 */
	List<RefundInfo> getNoRefundOrderByDuration(int minutes);
}
