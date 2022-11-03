package top.yueshushu.mapstruct.objdifferent;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import top.yueshushu.mapstruct.objdifferent.model.ErpItemRo;
import top.yueshushu.mapstruct.objdifferent.model.ItemRo;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-11-03
 */
// @SpringBootTest
@Slf4j
public class DiffTest {

    private List<ErpItemRo> newItemRoList = new ArrayList<>();
    private Map<String, ItemRo> itemCacheMap = new HashMap<>();


    private static final List<Pair<Field, Field>> FIELD_LIST = new ArrayList<>();

    @Before
    public void initData() {
        int maxItemCount = 1000000;
        // 模拟数据
        for (int i = 0; i < maxItemCount; i++) {

            newItemRoList.add(generateErpItem(i));

            ItemRo itemRo = generateCachItem(i);
            if (itemRo != null) {
                itemCacheMap.put(itemRo.getBarcode(), itemRo);
            }
        }

    }

    /**
     * 单个商品测试
     */
    @Test
    public void singleItemTest() {
        ErpItemRo erpItemRo = newItemRoList.get(0);
        // 获取对应的商品信息.

        ItemRo cacheItem = itemCacheMap.get(erpItemRo.getBarcode());

        TimeInterval timer = DateUtil.timer();
        timer.start();

        if (itemDifferent(erpItemRo, cacheItem)) {
            log.info(">>>> 商品不一致 {}", erpItemRo);
        } else {
            log.info(">>>> 商品一致");
        }
        log.info(">>>>> 比较结束 {}", timer.intervalMs());
    }

    // 10000 50
    // 1000000 2493
    @Test
    public void moreItemTest() {
        TimeInterval timer = DateUtil.timer();
        timer.start();
        newItemRoList.forEach(
                n -> {
                    if (itemDifferent(n, itemCacheMap.get(n.getBarcode()))) {
                        log.info(">>>> 商品不一致 {}", n);
                    } else {
                        //  log.info(">>>> 商品一致");
                    }
                }
        );
        log.info(">>>>> 比较结束 {}", timer.intervalMs());
    }

    // 10000 99
    // 1000000 6449
    @Test
    public void moreFieldItemTest() {
        TimeInterval timer = DateUtil.timer();
        timer.start();
        newItemRoList.forEach(
                n -> {
                    try {
                        if (getDifferent(n, itemCacheMap.get(n.getBarcode()))) {
                            log.info(">>>> 商品不一致 {}", n);
                        } else {
                            //  log.info(">>>> 商品一致");
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
        );
        log.info(">>>>> 比较结束 {}", timer.intervalMs());
    }

    private boolean getDifferent(ErpItemRo newItem, ItemRo cacheItem) throws IllegalAccessException {
        if (cacheItem == null) {
            return true;
        }
        for (Pair<Field, Field> fieldFieldPair : FIELD_LIST) {
            Field field = fieldFieldPair.getKey();
            Field field1 = fieldFieldPair.getValue();
            if (field.get(newItem) == null && field1.get(cacheItem) == null) {
                continue;
            }
            if (field.get(newItem) == null || field1.get(cacheItem) == null) {
                return false;
            }
            if (!field.get(newItem).toString().equals(field1.get(cacheItem).toString())) {
                return false;
            }
        }
        return true;
    }


    // @Bean
    private List<Pair<Field, Field>> getFieldList() {
        Field[] declaredFields = ErpItemRo.class.getDeclaredFields();
        Field[] declaredFields1 = ItemRo.class.getDeclaredFields();
        for (Field field : declaredFields) {
            for (Field field1 : declaredFields1) {
                if (field.getName().equals(field1.getName())) {
                    field.setAccessible(true);
                    field1.setAccessible(true);
                    FIELD_LIST.add(new Pair<>(field, field1));
                }
            }
        }
        return FIELD_LIST;
    }

    /**
     * 商品比较
     *
     * @param erpItemRo erp商品
     * @param cacheItem 缓存商品
     */
    private boolean itemDifferent(ErpItemRo erpItemRo, ItemRo cacheItem) {

        if (cacheItem == null || !erpItemRo.toString().equals(cacheItem.toString())) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 构建缓存数据
     */
    private ItemRo generateCachItem(int i) {
        //模拟缺少数据
        if ((i + 1) % 100 == 0) {
            return null;
        }
        ItemRo itemRo = new ItemRo();
        itemRo.setBarcode(i + "");
        itemRo.setName("商品" + i);

        if ((i + 1) % 100 == 10) {
            itemRo.setPrice(new BigDecimal(i * 2));
        } else {
            itemRo.setPrice(new BigDecimal(i));
        }
        if ((i + 1) % 100 == 20) {
            itemRo.setOriginPrice(new BigDecimal(i * 3));
        } else {
            itemRo.setOriginPrice(new BigDecimal(i));
        }
        itemRo.setClassLevel("级别:" + i);
        itemRo.setUnit("单位:" + i);
        itemRo.setDesc("");
        itemRo.setCustFeature1("自定义字段1:" + i);
        itemRo.setCustFeature2("自定义字段2:" + i);
        return itemRo;
    }

    /**
     * 构建 Erp 数据，即 传递过来的数据。
     */
    private ErpItemRo generateErpItem(int i) {
        //对商品数据进行处理
        ErpItemRo erpItemRo = new ErpItemRo();
        erpItemRo.setBarcode(i + "");
        erpItemRo.setName("商品" + i);
        erpItemRo.setPrice(new BigDecimal(i));
        erpItemRo.setOriginPrice(new BigDecimal(i));
        erpItemRo.setClassLevel("级别:" + i);
        erpItemRo.setUnit("单位:" + i);
        erpItemRo.setDesc("");
        erpItemRo.setCustFeature1("自定义字段1:" + i);
        erpItemRo.setCustFeature2("自定义字段2:" + i);
        return erpItemRo;
    }

}
