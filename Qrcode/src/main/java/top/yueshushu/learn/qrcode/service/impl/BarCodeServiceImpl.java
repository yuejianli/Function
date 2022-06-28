package top.yueshushu.learn.qrcode.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import top.yueshushu.learn.qrcode.service.BarCodeService;
import top.yueshushu.learn.qrcode.util.BarcodeUtil;

/**
 * 条形码处理
 *
 * @author yuejianli
 * @date 2022-06-28
 */
@Service
@Slf4j
public class BarCodeServiceImpl implements BarCodeService {
	private final int WIDTH = 240;
	private final int HEIGHT = 80;
	@Value("${qrcode.saveDir}")
	private String saveDir;

	@Override
	public boolean saveToFile(String content) {
		try{
			String savePath = saveDir + File.separator +"code1.jpg";
			BarcodeUtil.createCodeToFile(content,WIDTH,HEIGHT,null,null,(byte)0,savePath);
			return true;
		}catch (Exception e){
			log.error("error ",e);
			return false;
		}
	}
	
	@Override
	public boolean saveToFileByArray(String content) {
		try{
			byte[] codeToArray = BarcodeUtil.createCodeToArray(content,WIDTH,HEIGHT,null,null,(byte)0);
			String savePath = saveDir + File.separator +"code2.jpg";
			FileUtil.writeBytes(codeToArray,savePath);
			return true;
		}catch (Exception e){
			log.error("error ",e);
			return false;
		}
	}
	
	@Override
	public boolean saveToFileToOutputStream(String content, HttpServletResponse httpServletResponse) {
		try{
			BarcodeUtil.createCodeToOutput(content, WIDTH,HEIGHT,null,null,(byte)0, httpServletResponse.getOutputStream());
			return true;
		}catch (Exception e){
			log.error("error ",e);
			return false;
		}
	}
}
