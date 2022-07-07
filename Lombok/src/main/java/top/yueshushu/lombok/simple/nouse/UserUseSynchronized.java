package top.yueshushu.lombok.simple.nouse;

import java.io.UnsupportedEncodingException;

import lombok.SneakyThrows;
import lombok.Synchronized;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-07-07
 */

public class UserUseSynchronized {
	// 需要设置添加  readLock 属性名称不能改
	private final Object readLock = new Object();
	
	@Synchronized
	public static void hello() {
		System.out.println("world");
	}
	
	@Synchronized
	public int answerToLife() {
		return 42;
	}
	
	@Synchronized("readLock")
	public void foo() {
		System.out.println("bar");
	}
}
