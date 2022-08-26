package top.yueshushu.learn.qrcode.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.OutputStream;

import javax.annotation.PostConstruct;
import javax.swing.filechooser.FileSystemView;

import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * 二维码生成工具
 *
 * @author yuejianli
 * @date 2022-06-28
 */
@Slf4j(topic = "HutoolQrCode")
@Component
public class HutoolQrCodeUtil {
	// 格式化 JPG 格式
	public static final String FORMAT = "JPG";
	private static QrConfig qrConfigImpl;
	@Autowired
	private QrConfig qrConfig;
	
	/**
	 * 保存二维码到本地文件
	 *
	 * @param content            二维码内容
	 * @param codeImgFileSaveDir 保存的目录路径， 为空，则 放置到用户目录
	 * @param fileName           保存的文件名称， 为空，则为时间戳信息.
	 */
	public static boolean createCodeToFile(String content, String codeImgFileSaveDir, String fileName) {
		if (StringUtils.isEmpty(content)) {
			log.error("No content, no QR code generation");
			return false;
		}
		File dirFile;
		if (!StringUtils.isEmpty(codeImgFileSaveDir)) {
			log.debug("The default directory is saved to the desktop");
			//桌面
			dirFile = FileSystemView.getFileSystemView().getHomeDirectory();
		} else {
			dirFile = new File(codeImgFileSaveDir);
			if (dirFile == null || !dirFile.isDirectory()) {
				log.debug("An invalid directory is passed in. Use the default desktop directory");
				dirFile = FileSystemView.getFileSystemView().getHomeDirectory();
			}
		}
		if (!dirFile.exists()) {
			dirFile.mkdirs();
			log.info("Directory does not exist, recursively create directory");
		}
		// 对文件的名称进行处理
		if (StringUtils.isEmpty(fileName)) {
			fileName = DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss").concat(".").concat(FORMAT.toLowerCase());
		}
		return createCodeToFile(content, codeImgFileSaveDir + File.separator + fileName);
	}
	
	/**
	 * 保存二维码到本地文件
	 *
	 * @param content  二维码内容
	 * @param filePath 文件路径 ，没有后缀
	 */
	public static boolean createCodeToFile(String content, String filePath) {
		return createCodeToFile(content, filePath, null, false);
	}
	
	/**
	 * 保存二维码到本地文件
	 *
	 * @param content  二维码内容
	 * @param filePath 文件路径 ，没有后缀
	 */
	public static boolean createCodeToFile(String content, String filePath, String logoPath, Boolean needCompress) {
		if (StringUtils.isEmpty(content)) {
			log.error("No content, no QR code generation");
			return false;
		}
		try {
			mkdirs(filePath);
			
			if (!StringUtils.isEmpty(logoPath)) {
				// 设置 Logo
				qrConfigImpl.setImg(logoPath);
			}
			QrCodeUtil.generate(content, qrConfigImpl, new File(filePath));
			return true;
		} catch (Exception e) {
			log.error("generate qrcode is error ", e);
			return false;
		}
	}
	
	/**
	 * 生成二维码(内嵌LOGO),回显到 OutputStream 中
	 *
	 * @param content 内容
	 * @param output  输出流
	 * @throws Exception
	 */
	public static boolean createCodeToOutput(String content, OutputStream output) {
		return createCodeToOutput(content, output, null, false);
	}
	
	/**
	 * 生成二维码(内嵌LOGO),回显到 OutputStream 中
	 *
	 * @param content      内容
	 * @param output       输出流
	 * @param logoPath     LOGO地址
	 * @param needCompress 是否压缩LOGO
	 * @throws Exception
	 */
	public static boolean createCodeToOutput(String content, OutputStream output, String logoPath, boolean needCompress) {
		try {
			if (!StringUtils.isEmpty(logoPath)) {
				// 设置 Logo
				qrConfigImpl.setImg(logoPath);
			}
			//文件类型
			QrCodeUtil.generate(content, qrConfigImpl, FORMAT, output);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 解析二维码
	 *
	 * @param qrcodePath 二维码图片地址
	 * @return
	 * @throws Exception
	 */
	public static String parseCode(String qrcodePath) {
		return parseCode(new File(qrcodePath));
	}
	
	/**
	 * 解析二维码
	 *
	 * @param qrcodeFile 二维码文件
	 * @return
	 * @throws Exception
	 */
	public static String parseCode(File qrcodeFile) {
		try {
			return QrCodeUtil.decode(qrcodeFile);
		} catch (Exception e) {
			log.error("Parsing two-dimensional code exception", e);
			return null;
		}
	}
	
	/**
	 * 递归创建文件
	 */
	public static void mkdirs(String destPath) {
		File file = new File(destPath);
		// 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
		if (!file.exists() && !file.isDirectory()) {
			file.mkdirs();
		}
	}
	
	@PostConstruct
	public void init() {
		qrConfigImpl = qrConfig;
	}
}
