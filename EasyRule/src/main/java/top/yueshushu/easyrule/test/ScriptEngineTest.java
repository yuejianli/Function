package top.yueshushu.easyrule.test;

import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import cn.hutool.script.ScriptUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 脚本引擎处理
 *
 * @author yuejianli
 * @date 2022-06-30
 */
@Slf4j
public class ScriptEngineTest {
	
	/**
	 * 构建对象的方式
	 */
	@Test
	public void initTest() throws Exception {
		// 第一种方式，  .getEngineByExtension("js")
		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		ScriptEngine jsEngine1 = scriptEngineManager.getEngineByExtension("js");
		String jsStr1 = "1 == 1";
		boolean eval1 = (boolean) jsEngine1.eval(jsStr1);
		log.info("{} 的结果是:{}", jsStr1, eval1);
		
		// 第二种方式   getEngineByName("JavaScript")
		ScriptEngine jsEngine2 = scriptEngineManager.getEngineByName("JavaScript");
		String jsStr2 = "1+2";
		int eval2 = (int) jsEngine2.eval(jsStr2);
		log.info("{} 的结果是:{}", jsStr2, eval2);
		
		// 第三种方式
		ScriptEngine jsEngine3 = scriptEngineManager.getEngineByMimeType("text/javascript");
		String jsStr3 = "8*6";
		int eval3 = (int) jsEngine3.eval(jsStr3);
		log.info("{} 的结果是:{}", jsStr3, eval3);
		
		
		// 采用 Hutool 的方式处理
		String jsStr4 = "3 > 2";
		boolean eval4 = (Boolean) ScriptUtil.eval(jsStr4);
		log.info("{} 的结果是:{}", jsStr4, eval4);
	}
	
	/**
	 * 各种常用的条件处理
	 */
	@Test
	public void tiaojianTest() throws Exception {
		
		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		ScriptEngine scriptEngine = scriptEngineManager.getEngineByExtension("js");
		
		//1. 等于
		String equalStr = "1==1";
		boolean equalResult = (boolean) scriptEngine.eval(equalStr);
		log.info("{} 的结果是:{}", equalStr, equalResult);
		
		// 不等于
		String noequalStr = "1!=1";
		boolean noequalResult = (boolean) scriptEngine.eval(noequalStr);
		log.info("{} 的结果是:{}", noequalStr, noequalResult);
		
		// 大于
		
		String gtStr = "2>1";
		boolean gtResult = (boolean) scriptEngine.eval(gtStr);
		log.info("{} 的结果是:{}", gtStr, gtResult);
		
		// 大于等于
		
		String gteStr = "2>=1";
		boolean gteResult = (boolean) scriptEngine.eval(gteStr);
		log.info("{} 的结果是:{}", gteStr, gteResult);
		
		// 小于
		
		String ltStr = "2<1";
		boolean ltResult = (boolean) scriptEngine.eval(ltStr);
		log.info("{} 的结果是:{}", ltStr, ltResult);
		
		// 小于等于
		String lteStr = "2<=1";
		boolean lteResult = (boolean) scriptEngine.eval(lteStr);
		log.info("{} 的结果是:{}", lteStr, lteResult);
		
		// 为空， 如果是为空, 字符串处理
		
		// 不为空， 字符串处理
		
		// 包含    字符串处理
		
		// 不包含    字符串处理
		
		
	}
}
