package top.yueshushu.lombok.simple.test;

import org.junit.Test;

import top.yueshushu.lombok.simple.nouse.UserUseBuilder;
import top.yueshushu.lombok.simple.nouse.UserUseNonNull;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-07-07
 */
public class LombokTest {
	@Test
	public void nonNullTest() {
		
		UserUseNonNull userUseNonNull = new UserUseNonNull("名称");
		System.out.println(">>>"+userUseNonNull.toString());
	}
	
	@Test
	public void buildTest(){
		UserUseBuilder userUseBuilder = UserUseBuilder.builder()
										.id(1)
										.name("builder 建造者")
										.description("描述")
										.build();
		
		System.out.println(">>>"+userUseBuilder.toString());
	}
}
