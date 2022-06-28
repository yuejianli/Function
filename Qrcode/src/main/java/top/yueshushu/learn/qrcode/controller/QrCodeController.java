package top.yueshushu.learn.qrcode.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import top.yueshushu.learn.qrcode.service.QrCodeService;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-06-28
 */
@RestController
public class QrCodeController {
	@Resource
	private QrCodeService qrCodeService;
	@Value("${qrcode.logoPath}")
	private String logoPath;
	
	
	@GetMapping("/qrcode")
	public String qrcode (String content){
		return qrCodeService.generateCode(content) ?"生成二维码成功":"生成二维码失败";
	}
	
	@GetMapping("/qrcodeHref")
	public String qrcodeHref (String content){
		//可以放置 href 链接，可以添加 id标识
		content = "https://www.yueshushu.top/dev/base64";
		return qrCodeService.generateCode(content) ?"生成二维码成功":"生成二维码失败";
	}
	
	
	@GetMapping("/qrcodeLogo")
	public String qrcodeLogo (String content){
		return qrCodeService.generateLogoCode(content,logoPath) ?"生成Logo二维码成功":"生成Logo二维码失败";
	}
	
	
	@GetMapping("/qrcodeOutputStream")
	public void qrcodeOutputStream (String content, HttpServletResponse httpServletResponse){
		 qrCodeService.generateCodeToOutputStream(content,httpServletResponse);
	}
	
	@GetMapping("/qrcodeLogoOutputStream")
	public void qrcodeLogoOutputStream (String content,HttpServletResponse httpServletResponse){
		 qrCodeService.generateLogoCodeToOutputStream(content,logoPath,httpServletResponse);
	}
	
	/**
	 解析二维码
	 */
	@GetMapping("/parseCode")
	public String parseCode (String codePath){
		return qrCodeService.parseCode(codePath);
	}
	
}
