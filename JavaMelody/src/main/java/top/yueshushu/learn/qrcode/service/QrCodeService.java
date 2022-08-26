package top.yueshushu.learn.qrcode.service;

import javax.servlet.http.HttpServletResponse;

/**
 * 创建生成二维码
 *
 * @author Yue Jianli
 * @date 2022-06-28
 */

public interface QrCodeService {
	/**
	 * 生成普通的二维码
	 *
	 * @param content 内容
	 */
	boolean generateCode(String content);
	
	/**
	 * 生成带 logo的二维码
	 *
	 * @param content  内容
	 * @param logoPath logo地址
	 */
	boolean generateLogoCode(String content, String logoPath);
	
	/**
	 * 响应普通二维码到 输出流
	 *
	 * @param content             内容
	 * @param httpServletResponse 输出流
	 */
	void generateCodeToOutputStream(String content, HttpServletResponse httpServletResponse);
	
	/**
	 * 响应带 logo 的二维码到 输出流
	 *
	 * @param content             内容
	 * @param logoPath            logo地址
	 * @param httpServletResponse 输出流
	 */
	void generateLogoCodeToOutputStream(String content, String logoPath, HttpServletResponse httpServletResponse);
	
	/**
	 * 解析二维码
	 *
	 * @param codePath 二维码文件路径
	 */
	String parseCode(String codePath);
}
