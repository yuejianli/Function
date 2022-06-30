package top.yueshushu.easyrule.util;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @author 引擎执行
 */
public class EngineUtil {
    
    static final Map<String, String> comparatorMap = new HashMap() {{
        put("equal", "==");
        put("notEqual", "!=");
        put("greater", ">");
        put("greaterOrEqual", ">=");
        put("less", "<");
        put("lessOrEqual", "<=");
    }};
    
    public static boolean eval(String comparator, String leftValue, String rightValue, Class<?> fieldType) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        boolean eval;
        try {
            if (comparator.equals(Comparator.isNull.getValue())) {
                eval = !StringUtils.hasText(leftValue) || leftValue.equals("null");
            } else if (comparator.equals(Comparator.isNotNull.getValue())) {
                eval = StringUtils.hasText(leftValue) && !leftValue.equals("null");
            } else if (comparator.equals(Comparator.contain.getValue())) {
                eval = leftValue.contains(rightValue);
            } else if (comparator.equals(Comparator.notContain.getValue())) {
                eval = !leftValue.contains(rightValue);
            } else {
                String realComparator = comparatorMap.get(comparator);
                if (fieldType.equals(Integer.class) || fieldType.equals(Long.class) || fieldType.equals(BigDecimal.class)) {
                    eval = (boolean) engine.eval(leftValue + realComparator + rightValue);
                } else {
                    eval = (boolean) engine.eval("'" + leftValue + "'" + realComparator + "'" + rightValue + "'");
                }
            }
        } catch (ScriptException e) {
            eval = false;
        }
        return eval;
    }
}
