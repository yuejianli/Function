package top.yueshushu.mapstruct.objdifferent.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 两个类中属性是否完全一致
 */
@Data
public class ErpItemRo implements Serializable {
    private String barcode;
    private String name;
    private BigDecimal price;
    private BigDecimal originPrice;
    private String classLevel;
    private String unit;
    private String desc;
    private String custFeature1;
    private String custFeature2;

    @Override
    public String toString() {
        return "{" +
                "barcode='" + barcode + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", originPrice=" + originPrice +
                ", classLevel='" + classLevel + '\'' +
                ", unit='" + unit + '\'' +
                ", desc='" + desc + '\'' +
                ", custFeature1='" + custFeature1 + '\'' +
                ", custFeature2='" + custFeature2 + '\'' +
                '}';
    }
}
