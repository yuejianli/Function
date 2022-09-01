package top.yueshushu.pay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

import top.yueshushu.pay.mapper.ProductMapper;
import top.yueshushu.pay.pojo.Product;
import top.yueshushu.pay.service.ProductService;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author yuejianli
 * @since 2022-08-30
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

}
