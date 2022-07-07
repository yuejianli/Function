package top.yueshushu.lombok.simple.nouse;

import java.io.*;

import lombok.Cleanup;
import lombok.SneakyThrows;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-07-07
 */

public class UserUseSneakyThrows {
	
	// 指定具体的异常
	@SneakyThrows(UnsupportedEncodingException.class)
	public String utf8ToString(byte[] bytes) {
		return new String(bytes, "UTF-8");
	}
	
	// 抛出异常
	@SneakyThrows
	public void run() {
		throw new Throwable();
	}
}
