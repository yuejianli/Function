package top.yueshushu.pay.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import top.yueshushu.pay.business.PayBusiness;
import top.yueshushu.pay.response.OutputResult;
import top.yueshushu.pay.vo.*;


@RestController
@RequestMapping("/aliPay")
@Api(value = "商品", tags = "商品信息")
public class AliPayController {
    
    @Resource
    private PayBusiness payBusiness;
    
    /**
     * 查询订单商品信息.
     * 数量不多， 全部查询。
     */
    @PostMapping("/nativePay")
    @ApiOperation("支付下单")
    public OutputResult<NativePayResponseVO> nativePay(@RequestBody NativePayRequestVO nativePayVO,
                                                       HttpServletRequest httpServletRequest) {
        nativePayVO.setIpAddr(httpServletRequest.getRemoteAddr());
        return payBusiness.nativePay(nativePayVO);
    }
    
    @PostMapping("/aliNativeNotify")
    @ApiOperation("支付成功之后，进行回调")
    public OutputResult aliNativeNotify(@RequestParam Map<String, String> params) {
        return payBusiness.aliNativeNotify(params);
    }
    
    @PostMapping("/cancel")
    @ApiOperation(("取消支付"))
    public OutputResult cancelOrder(@RequestBody CancelOrderRequestVO cancelOrderRequestVO) {
        return payBusiness.cancelOrder(cancelOrderRequestVO);
    }
    
    @PostMapping("/queryOrder")
    @ApiOperation(("查询订单"))
    public OutputResult queryOrder(@RequestBody OrderInfoRequestVO orderInfoRequestVO) {
        return payBusiness.queryOrder(orderInfoRequestVO.getOrderNo());
    }
    
    
    @PostMapping("/refunds")
    @ApiOperation(("退款"))
    public OutputResult refunds(@RequestBody RefundsInfoRequestVO refundsInfoRequestVO) {
        return payBusiness.refunds(refundsInfoRequestVO);
    }
    
    
    @PostMapping("/queryRefunds")
    @ApiOperation(("查询退款信息"))
    public OutputResult queryRefunds(@RequestBody RefundsInfoRequestVO refundsInfoRequestVO) {
        return payBusiness.queryRefunds(refundsInfoRequestVO.getRefundNo());
    }
    
    
    @ApiOperation("获取账单url")
    @PostMapping("/aliQueryTradeBill")
    public OutputResult aliQueryTradeBill(@RequestBody BillRequestVO billRequestVO) {
        
        String downloadUrl = payBusiness.aliQueryTradeBill(billRequestVO);
        return OutputResult.buildSucc(downloadUrl);
    }
}

