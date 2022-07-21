package top.yueshushu.itextpdf.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import top.yueshushu.itextpdf.service.PdfService;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-07-21
 */
@RestController
@Slf4j
public class PdfController {
	@Resource
	private PdfService pdfService;
	
	@RequestMapping("/saveFile")
	public String saveFile() {
		try{
			pdfService.saveFile();
			return "生成文件成功";
		}catch (Exception e){
			return "生成文件失败";
		}
	}
	
	@RequestMapping("/show")
	public void show(HttpServletResponse httpServletResponse) {
		try{
			pdfService.show(httpServletResponse);
		}catch (Exception e){
			log.error("异常 {}",e);
		}
	}
}
