package top.yueshushu.pay.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import top.yueshushu.pay.business.PayBusiness;
import top.yueshushu.pay.enumtype.PayType;
import top.yueshushu.pay.pojo.OrderInfo;
import top.yueshushu.pay.service.OrderInfoService;

@Slf4j
@Component
public class AliPayTask {
    
    @Resource
    private OrderInfoService orderInfoService;
    
    @Resource
    private PayBusiness payBusiness;
    
    /**
     * 从第0秒开始每隔30秒执行1次，查询创建超过5分钟，并且未支付的订单
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void orderConfirm() {
        
        log.info("orderConfirm 被执行......");
        
        List<OrderInfo> orderInfoList = orderInfoService.getNoPayOrderByDuration(1, PayType.ALIPAY.getCode());
        
        for (OrderInfo orderInfo : orderInfoList) {
            log.warn("超时订单 ===> {}", orderInfo.getOrderNo());
            
            //核实订单状态：调用支付宝查单接口
            payBusiness.handlerAliNotPay(orderInfo);
        }
    }
}
