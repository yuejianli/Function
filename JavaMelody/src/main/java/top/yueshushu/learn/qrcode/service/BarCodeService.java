package top.yueshushu.learn.qrcode.service;

import javax.servlet.http.HttpServletResponse;

/**
 * 条形码
 *
 * @author yuejianli
 * @date 2022-06-28
 */

public interface BarCodeService {
	/**
	 * 保存内容到文件
	 *
	 * @param content 内容
	 */
	boolean saveToFile(String content);
	
	/**
	 * 转换成 字节流处理
	 *
	 * @param content 内容
	 */
	boolean saveToFileByArray(String content);
	
	/**
	 * 保存内容到 输出流
	 *
	 * @param content             内容
	 * @param httpServletResponse 输出流响应
	 */
	boolean saveToFileToOutputStream(String content, HttpServletResponse httpServletResponse);
}
