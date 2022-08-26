package top.yueshushu.learn.qrcode.service.impl;

import net.bull.javamelody.MonitoredWithSpring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import top.yueshushu.learn.qrcode.service.QrCodeService;
import top.yueshushu.learn.qrcode.util.HutoolQrCodeUtil;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-06-28
 */
@Service
@Slf4j
@MonitoredWithSpring
public class QrCodeServiceImpl implements QrCodeService {
	@Value("${qrcode.saveDir}")
	private String saveDir;
	
	@Value("${qrcode.parseDir}")
	private String parseDir;
	
	@Override
	public boolean generateCode(String content) {
		
		//return ZxingQrCodeUtil.createCodeToFile(content,saveDir,null);
		return HutoolQrCodeUtil.createCodeToFile(content, saveDir, null);
	}
	
	@Override
	public boolean generateLogoCode(String content, String logoPath) {
		String savePath = saveDir + File.separator + "qr_logo.jpg";
		// return ZxingQrCodeUtil.createCodeToFile(content,savePath,logoPath,true);
		return HutoolQrCodeUtil.createCodeToFile(content, savePath, logoPath, true);
	}
	
	@Override
	public void generateCodeToOutputStream(String content, HttpServletResponse httpServletResponse) {
		try {
			//ZxingQrCodeUtil.createCodeToOutput(content,httpServletResponse.getOutputStream());
			HutoolQrCodeUtil.createCodeToOutput(content, httpServletResponse.getOutputStream());
		} catch (Exception e) {
			log.error("异常", e);
		}
	}
	
	@Override
	public void generateLogoCodeToOutputStream(String content, String logoPath, HttpServletResponse httpServletResponse) {
		try {
			// ZxingQrCodeUtil.createCodeToOutput(content,httpServletResponse.getOutputStream(),logoPath,true);
			HutoolQrCodeUtil.createCodeToOutput(content, httpServletResponse.getOutputStream(), logoPath, true);
		} catch (Exception e) {
			log.error("异常", e);
		}
	}
	
	@Override
	public String parseCode(String codePath) {
		String savePath = parseDir + File.separator + codePath;
		// return ZxingQrCodeUtil.parseCode(savePath);
		return HutoolQrCodeUtil.parseCode(savePath);
	}
}
