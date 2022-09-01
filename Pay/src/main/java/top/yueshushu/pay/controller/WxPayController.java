package top.yueshushu.pay.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import top.yueshushu.pay.business.PayBusiness;
import top.yueshushu.pay.response.OutputResult;
import top.yueshushu.pay.vo.*;


@RestController
@RequestMapping("/wxPay")
@Api(value = "商品", tags = "商品信息")
public class WxPayController {
    
    @Resource
    private PayBusiness payBusiness;
    
    /**
     * 查询订单商品信息.
     * 数量不多， 全部查询。
     */
    @PostMapping("/nativePay")
    @ApiOperation("支付下单")
    public OutputResult<NativePayResponseVO> nativePay(@RequestBody NativePayRequestVO nativePayVO, HttpServletRequest httpServletRequest) {
        nativePayVO.setIpAddr(httpServletRequest.getRemoteAddr());
        return payBusiness.nativePay(nativePayVO);
    }
    
    @PostMapping("/wxNativeNotify")
    @ApiOperation("支付成功之后，进行回调")
    public OutputResult wxNativeNotify(HttpServletRequest httpServletRequest) {
        return payBusiness.wxNativeNotify(httpServletRequest);
    }
    
    @PostMapping("/wx2NativeNotify")
    @ApiOperation("支付成功之后，进行回调")
    public OutputResult wx2NativeNotify(HttpServletRequest httpServletRequest) {
        return payBusiness.wx2NativeNotify(httpServletRequest);
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
    
    
    @PostMapping("/wxRefundsNotify")
    @ApiOperation("退款成功之后，进行回调")
    public OutputResult wxRefundsNotify(HttpServletRequest httpServletRequest) {
        return payBusiness.wxRefundsNotify(httpServletRequest);
    }
    
    
    @ApiOperation("获取账单url")
    @PostMapping("/wxQueryTradeBill")
    public OutputResult wxQueryTradeBill(@RequestBody BillRequestVO billRequestVO) {
        
        String downloadUrl = payBusiness.wxQueryTradeBill(billRequestVO);
        return OutputResult.buildSucc(downloadUrl);
    }
    
    @ApiOperation("下载账单url")
    @PostMapping("/wxDownloadTradeBill")
    public OutputResult wxDownloadTradeBill(@RequestBody BillRequestVO billRequestVO) {
        
        String downResponseStr = payBusiness.wxDownloadTradeBill(billRequestVO);
        return OutputResult.buildSucc(downResponseStr);
    }
    
}

