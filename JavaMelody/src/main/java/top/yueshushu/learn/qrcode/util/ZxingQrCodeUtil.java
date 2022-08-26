package top.yueshushu.learn.qrcode.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import org.springframework.util.StringUtils;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 二维码生成工具
 *
 * @author yuejianli
 * @date 2022-06-28
 */
@Slf4j(topic = "ZxingQrCode")
public class ZxingQrCodeUtil {
	// 格式化 JPG 格式
	public static final String FORMAT = "JPG";
	private static final String CHARSET = "utf-8";
	// 二维码尺寸, 通常是正方形, 宽度
	private static final int QRCODE_WIDTH = 300;
	private static final int QRCODE_HEIGHT = 300;
	
	// LOGO宽度
	private static final int LOGO_WIDTH = 100;
	// LOGO高度
	private static final int LOGO_HEIGHT = 100;
	
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
			BufferedImage image = createImage(content, logoPath, needCompress);
			ImageIO.write(image, FORMAT, new File(filePath));
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
			BufferedImage image = createImage(content, logoPath, needCompress);
			ImageIO.write(image, FORMAT, output);
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
			BufferedImage image;
			image = ImageIO.read(qrcodeFile);
			if (image == null) {
				return null;
			}
			BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
			Result result;
			Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
			hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
			result = new MultiFormatReader().decode(bitmap, hints);
			String resultStr = result.getText();
			return resultStr;
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
	
	/**
	 * 生成二维码
	 *
	 * @param content      二维码内容
	 * @param logoPath     logo地址, 不传入log时，为空
	 * @param needCompress 是否压缩logo，不传入log时，为 false
	 * @return 图片
	 * @throws Exception
	 */
	private static BufferedImage createImage(String content, String logoPath, boolean needCompress) throws Exception {
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
		hints.put(EncodeHintType.MARGIN, 1);
		BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_WIDTH, QRCODE_HEIGHT,
				hints);
		int width = bitMatrix.getWidth();
		int height = bitMatrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
			}
		}
		if (StringUtils.isEmpty(logoPath)) {
			return image;
		}
		// 插入图片
		insertLogoImage(image, logoPath, needCompress);
		return image;
	}
	
	/**
	 * 插入LOGO
	 *
	 * @param source       二维码图片
	 * @param logoPath     LOGO图片地址
	 * @param needCompress 是否压缩
	 * @throws IOException 抛出异常
	 */
	private static void insertLogoImage(BufferedImage source, String logoPath,
										boolean needCompress) throws IOException {
		File file = new File(logoPath);
		if (!file.exists()) {
			log.error("logo file path {} is error", logoPath);
			return;
		}
		Image src = ImageIO.read(new File(logoPath));
		int width = src.getWidth(null);
		int height = src.getHeight(null);
		// 压缩LOGO
		if (needCompress) {
			if (width > LOGO_WIDTH) {
				width = LOGO_WIDTH;
			}
			if (height > LOGO_HEIGHT) {
				height = LOGO_HEIGHT;
			}
			Image image = src.getScaledInstance(width, height,
					Image.SCALE_SMOOTH);
			BufferedImage tag = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			// 绘制缩小后的图
			g.drawImage(image, 0, 0, null);
			g.dispose();
			src = image;
		}
		// 插入LOGO
		Graphics2D graph = source.createGraphics();
		int x = (QRCODE_WIDTH - width) / 2;
		int y = (QRCODE_HEIGHT - height) / 2;
		graph.drawImage(src, x, y, width, height, null);
		Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
		graph.setStroke(new BasicStroke(3f));
		graph.draw(shape);
		graph.dispose();
	}
}
