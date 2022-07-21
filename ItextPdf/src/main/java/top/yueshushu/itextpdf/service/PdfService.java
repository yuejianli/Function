package top.yueshushu.itextpdf.service;

import javax.servlet.http.HttpServletResponse;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-07-21
 */

public interface PdfService {
	
	/**
	  保存到文件里面
	 */
	 void saveFile () throws Exception;
	
	/**
	 展示到页面浏览器上
	 */
	 void show(HttpServletResponse httpServletResponse) throws Exception;
}
