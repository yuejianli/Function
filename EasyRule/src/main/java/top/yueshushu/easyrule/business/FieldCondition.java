package top.yueshushu.easyrule.business;

import java.lang.reflect.Field;

/**
 * @author 字段条件
 */
public class FieldCondition<T> {
    
    /**
     * 类型
     */
    private final Class<T> type;
    /**
     * 字段属性
     */
    private String fieldName;
    /**
     * 运行符
     */
    private String comparator;
    /**
     * 值
     */
    private String value;
    /**
     * 字段属性 Field 对象
     */
    private Field field;
    /**
     * 字段属性  Integer, BigDecimal
     */
    private String fieldPart;
    
    
    public FieldCondition(Class<T> type) {
        this.type = type;
    }
    
    public String getFieldName() {
        return fieldName;
    }
    
    public boolean setFieldName(String fieldName) {
        
        try {
            this.field = type.getDeclaredField(fieldName);
            this.fieldName = field.getName();
            // 可以进行额外属性 转换处理
        } catch (NoSuchFieldException e) {
            return false;
        }
        return true;
    }
    
    public boolean setFieldName(String fieldName, String fieldPart) {
        try {
            this.field = type.getDeclaredField(fieldName);
            this.fieldName = field.getName();
            this.fieldPart = fieldPart;
        } catch (NoSuchFieldException e) {
            return false;
        }
        return true;
    }
    
    public String getComparator() {
        return comparator;
    }
    
    public void setComparator(String comparator) {
        this.comparator = comparator;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    public Field getField() {
        return field;
    }
    
    public String getFieldPart() {
        return fieldPart;
    }
    
}
