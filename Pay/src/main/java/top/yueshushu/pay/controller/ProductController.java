package top.yueshushu.pay.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.annotation.Resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import top.yueshushu.pay.pojo.Product;
import top.yueshushu.pay.response.OutputResult;
import top.yueshushu.pay.service.ProductService;


@RestController
@RequestMapping("/product")
@Api(value = "商品", tags = "商品信息")
public class ProductController {
    
    @Resource
    private ProductService productService;
    
    /**
     * 查询订单商品信息.
     * 数量不多， 全部查询。
     */
    @GetMapping("/list")
    @ApiOperation("展示全部的商品列表")
    public OutputResult<List<Product>> list() {
        return OutputResult.buildSucc(productService.list());
    }
    
    
}
