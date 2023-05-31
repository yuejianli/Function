package top.yueshushu.jep.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.expression.BeanExpressionContextAccessor;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.test.context.junit4.SpringRunner;
import top.yueshushu.jep.config.BeanConfig;
import top.yueshushu.jep.model.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2023-05-31
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class ELTest {
    /**
      简单的处理
     */
    @Test
    public void oneTest() {
        //1. 构建对象
        ExpressionParser expressionParser = new SpelExpressionParser();
        //2. 解析表达式
        Expression expression = expressionParser.parseExpression("1+2+3");
        //3. 获取结果
        log.info("获取结果: {}", expression.getValue());
    }
    /**
     获取 boolean 等相应的信息
     */
    @Test
    public void twoTest() {
        ExpressionParser expressionParser = new SpelExpressionParser();
        log.info("3>2 为: {}", expressionParser.parseExpression("3>2").getValue());
        log.info("1!=1 为: {}", expressionParser.parseExpression("1!=1").getValue());
        log.info("3>2 && 2>3 为: {}", expressionParser.parseExpression("3>2 && 2>3").getValue());
        log.info("3>2 || 2>3 为: {}", expressionParser.parseExpression("3>2 || 2>3").getValue());

        // 普通字符串方法调用
        log.info("hello.substring(3): {}", expressionParser.parseExpression("'hello'.substring(3)").getValue());

    }
    /**
     表达式处理
     */
    @Test
    public void threeTestError() {
        ExpressionParser expressionParser = new SpelExpressionParser();
        // 创建数据上下文
        StandardEvaluationContext standardEvaluationContext = new StandardEvaluationContext();

        // 数据上下文设置变量
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("a",1);
        paramMap.put("b",2);
        paramMap.put("c",3);
        paramMap.put("d",4);
        paramMap.put("e",5);
        standardEvaluationContext.setVariables(paramMap);

        // 解析表达式，获取数据
        Object value = expressionParser.parseExpression("a+b-c+d-e").getValue(standardEvaluationContext);
        log.info(">>> 获取结果: {}", value);
    }

    /**
     表达式处理
     */
    @Test
    public void threeTestTrue() {
        ExpressionParser expressionParser = new SpelExpressionParser();
        // 创建数据上下文
        StandardEvaluationContext standardEvaluationContext = new StandardEvaluationContext();

        // 数据上下文设置变量
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("a",1);
        paramMap.put("b",2);
        paramMap.put("c",3);
        paramMap.put("d",4);
        paramMap.put("e",5);
        standardEvaluationContext.setVariables(paramMap);

        // 解析表达式，获取数据,  注意，传入的变量前面需要加入 # 号， 指定这是一个变量
        Object value = expressionParser.parseExpression("#a+#b-#c+#d-#e").getValue(standardEvaluationContext);
        log.info(">>> 获取结果: {}", value);
    }

    /**
    处理对象， 获取对象里面的属性和方法
     */
    @Test
    public void beanTest() {
        ExpressionParser expressionParser = new SpelExpressionParser();
        // 需要创建上下文
        StandardEvaluationContext standardEvaluationContext = new StandardEvaluationContext();
        standardEvaluationContext.setVariable("user",new User("岳泽霖"));

        // 执行信息
        log.info("获取name的值: {}", expressionParser.parseExpression("#user.name").getValue(standardEvaluationContext));
        log.info("获取name的值,方法: {}", expressionParser.parseExpression("#user.getName()").getValue(standardEvaluationContext));

    }
    /**
      集合信息相关的调用
     */
    @Test
    public void collectionTest() {
        //创建表达式解析器
        ExpressionParser expressionParser = new SpelExpressionParser();
        //创建数据上下文
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
        //设置数组变量
        evaluationContext.setVariable("users", new User[]{new User("岳泽霖")});
        //设置集合变量
        evaluationContext.setVariable("userList", Collections.singletonList(new User("岳泽霖")));
        //设置map变量
        evaluationContext.setVariable("userMap", Collections.singletonMap("name", new User("岳泽霖")));
        log.info("数组 {}",expressionParser.parseExpression("#users[0].name").getValue(evaluationContext));
        log.info("集合 {}",expressionParser.parseExpression("#userList[0].name").getValue(evaluationContext));
        log.info("map {}",expressionParser.parseExpression("#userMap['name'].name").getValue(evaluationContext));
    }

    @Test
    public void iocTest() {
        //创建表达式解析器
        ExpressionParser expressionParser = new SpelExpressionParser();
        //创建数据上下文
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext();

        //创建IOC容器上下文
        ApplicationContext context = new AnnotationConfigApplicationContext(BeanConfig.class);
        //创建bean表达式上下文
        BeanExpressionContext beanExpressionContext = new BeanExpressionContext((ConfigurableBeanFactory) context.getAutowireCapableBeanFactory(), null);
        evaluationContext.setRootObject(beanExpressionContext);
        //添加属性访问器 从IOC容器中获取bean
        evaluationContext.addPropertyAccessor(new BeanExpressionContextAccessor());
        System.out.println(expressionParser.parseExpression("#{iocUser.name}", new TemplateParserContext()).getValue(evaluationContext));
    }


}
