package top.yueshushu.learn.qrcode.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import top.yueshushu.learn.qrcode.service.BarCodeService;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-06-28
 */
@RestController
public class CodeController {
	@Resource
	private BarCodeService barCodeService;
	
	// A0A3B8279266
	@GetMapping("/code")
	public String code(String content) {
		return barCodeService.saveToFile(content) ? "生成一维码成功" : "生成一维码失败";
	}
	
	@GetMapping("/code2")
	public String code2(String content) {
		//可以放置 href 链接，可以添加 id标识
		content = "https://www.yueshushu.top/dev/base64";
		return barCodeService.saveToFileByArray(content) ? "生成一维码成功" : "生成一维码失败";
	}
	
	@GetMapping("/codeOutputStream")
	public void codeOutputStream(String content, HttpServletResponse httpServletResponse) {
		barCodeService.saveToFileToOutputStream(content, httpServletResponse);
	}
}
