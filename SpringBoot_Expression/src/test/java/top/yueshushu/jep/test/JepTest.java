package top.yueshushu.jep.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.nfunk.jep.JEP;

/**
 * 主要是数学公式
 *
 * @author yuejianli
 * @date 2023-05-31
 */
@Slf4j
public class JepTest {

    @Test
    public void oneTest() {
        // 1. 构建类
        JEP jep = new JEP();
        //2. 添加变量
        jep.addVariable("a",1);
        jep.addVariable("b",2);
        jep.addVariable("c",3);
        jep.addVariable("d",4);
        jep.addVariable("e",5);
        //3. 处理表达式
        jep.parseExpression("a+b-c+d-e");
        //4. 获取结果
        log.info(">>> 获取结果: {}", jep.getValue());
    }

    @Test
    public void twoTest() {
        // 1. 构建类
        JEP jep = new JEP();
        //2. 添加变量
        jep.addVariable("单价",1);
        jep.addVariable("数量",2);
        //3. 处理表达式
        jep.parseExpression("单价*数量");
        //4. 获取结果
        log.info(">>> 获取结果: {}", jep.getValue());
    }

}
