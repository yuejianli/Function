package top.yueshushu.pay.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import top.yueshushu.pay.pojo.OrderInfo;
import top.yueshushu.pay.response.OutputResult;
import top.yueshushu.pay.response.PageResponse;
import top.yueshushu.pay.service.OrderInfoService;
import top.yueshushu.pay.vo.OrderInfoRequestVO;


@RestController
@RequestMapping("/orderInfo")
@Api(value = "订单信息", tags = "订单信息")
public class OrderInfoController {
    
    @Resource
    private OrderInfoService orderInfoService;
    
    /**
     * 查询订单商品信息.
     * 数量不多， 全部查询。
     */
    @PostMapping("/list")
    @ApiOperation("查询所有的订单信息")
    public OutputResult<PageResponse<OrderInfo>> list(@RequestBody OrderInfoRequestVO orderInfoRequestVO) {
        
        return OutputResult.buildSucc(orderInfoService.listAll(orderInfoRequestVO));
    }
}
