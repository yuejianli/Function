package top.yueshushu.learn.qrcode.util;

import org.krysalis.barcode4j.BarcodeDimension;
import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.AbstractBarcodeBean;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.impl.int2of5.Interleaved2Of5Bean;
import org.krysalis.barcode4j.impl.upcean.EAN13Bean;
import org.krysalis.barcode4j.impl.upcean.EAN8Bean;
import org.krysalis.barcode4j.output.java2d.Java2DCanvasProvider;
import org.springframework.util.StringUtils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 生成一维码
 * @author yuejianli
 * @date 2022-06-28
 */
@Slf4j
public class BarcodeUtil {
	
	// 格式化 JPG 格式
	public static final String FORMAT = "JPG";
	
	/**
	 * 生成文件
	 *
	 * @param content        条形码内容
	 * @param width           条形码宽度
	 * @param height          条形码高度
	 * @param barcodeColor    条形码颜色
	 * @param backgroundColor 条形码背景颜色
	 * @param barcodeType     条形码类型（0：code128  1：EAN_8  2：EAN_13 10:自适应）
	 * @param path 文件路径
	 * @return 保存到文件路径
	 */
	public static File createCodeToFile(String content, int width, int height, String barcodeColor, String backgroundColor, Byte barcodeType, String path) {
		File file = new File(path);
		try {
			mkdirs(path);
			BufferedImage bufferedImage = encodeBarcodeWithColor(content, width, height, barcodeColor, backgroundColor, barcodeType);
			ImageIO.write(bufferedImage, FORMAT, new File(path));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return file;
	}
	/**
	 递归创建文件
	 */
	public static void mkdirs(String destPath) {
		File file = new File(destPath);
		// 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
		if (!file.exists() && !file.isDirectory()) {
			file.mkdirs();
		}
	}
	
	/**
	 * 生成字节
	 * @param content        条形码内容
	 * @param width           条形码宽度
	 * @param height          条形码高度
	 * @param barcodeColor    条形码颜色
	 * @param backgroundColor 条形码背景颜色
	 * @param barcodeType     条形码类型（0：code128  1：EAN_8  2：EAN_13 10:自适应）
	 * @return 生成字节流
	 */
	public static byte[] createCodeToArray(String content, int width, int height, String barcodeColor, String backgroundColor, Byte barcodeType) {
		ByteArrayOutputStream ous = new ByteArrayOutputStream();
		createCodeToOutput(content,width,height,barcodeColor,backgroundColor,barcodeType,ous);
		return ous.toByteArray();
	}
	
	/**
	 * 生成到流
	 *
	 * @param content        条形码内容
	 * @param width           条形码宽度
	 * @param height          条形码高度
	 * @param barcodeColor    条形码颜色
	 * @param backgroundColor 条形码背景颜色
	 * @param barcodeType     条形码类型（0：code128  1：EAN_8  2：EAN_13 10:自适应）
	 * @param ous 输出流
	 */
	public static void createCodeToOutput(String content, int width, int height, String barcodeColor, String backgroundColor, Byte barcodeType, OutputStream ous) {
		try{
			BufferedImage bufferedImage = encodeBarcodeWithColor(content, width, height, barcodeColor, backgroundColor, barcodeType);
			ImageIO.write(bufferedImage, FORMAT, ous);
		}catch (Exception e){
			log.error("error ",e);
		}
	}
	
	
	/**
	 * 生成条形码(主动设置条码颜色和底色)
	 *
	 * @param content        条形码内容
	 * @param width           条形码宽度
	 * @param height          条形码高度
	 * @param barcodeColor    条形码颜色
	 * @param backgroundColor 条形码背景颜色
	 * @param barcodeType     条形码类型（0：code128  1：EAN_8  2：EAN_13 10:自适应）
	 * @return 绘制好的图片，异常则为空
	 */
	public static BufferedImage encodeBarcodeWithColor(String content, int width, int height, String barcodeColor, String backgroundColor, Byte barcodeType) {
		if (StringUtils.isEmpty(content) || content.getBytes().length != content.length()) {
			log.warn("条形码的编码数据为空，或者字符串中含有全角字符则不生成图片。");
			return null;
		}
		try {
			switch (barcodeType == null ? 0 : barcodeType) {
				// 自适应条码逻辑
				case 10:
					// 由长度判断条码类型
					if (content.length() == 8) {
						return generateEan8(content, width, height, barcodeColor, backgroundColor);
					}
					if (content.length() == 13) {
						return generateEan13(content, width, height, barcodeColor, backgroundColor);
					}
					break;
				case 1:
					return generateEan8(content, width, height, barcodeColor, backgroundColor);
				case 2:
					return generateEan13(content, width, height, barcodeColor, backgroundColor);
				case 3:
					if (StrUtil.isNumeric(content) && content.length() > 4) {
						//特殊处理25交错码
						return generateInterleaved2Of5(content, width, height, barcodeColor, backgroundColor);
					}
					break;
				default://0：code128
			}
		} catch (Exception e) {
			log.warn("较严格编码格式生成条码异常。类型：{}，条码内容：{}。jvm信息：{}。", barcodeType, content, e.getMessage());
			try {
				return generateCode128(content, width, height, barcodeColor, backgroundColor);
			} catch (Exception ex) {
				log.warn("非法数据生成条码异常。类型：{}，条码内容：{}。jvm信息：{}。", barcodeType, content, ex.getMessage());
				return null;
			}
		}
		try {
			return generateCode128(content, width, height, barcodeColor, backgroundColor);
		} catch (Exception e) {
			log.warn("非法数据生成条码异常。类型：{}，条码内容：{}。jvm信息：{}。", barcodeType, content, e.getMessage());
			return null;
		}
	}
	
	private static BufferedImage getBarcodeBufferedImage(String content, int width, int height, String barcodeColor, String backgroundColor, AbstractBarcodeBean bean) {
		// 设置两侧是否留白(默认有)
		bean.doQuietZone(false);
		// 设置条形码高度和宽度
		bean.setBarHeight(height);
		// 短线的粗细（初始1像素）
		bean.setModuleWidth(1);
		//设置宽条比窄条宽的系数(默认就是3倍，而且好计算),bean.setWideFactor(2);
		//设置有没有说明的数字(默认有，这里设置没有)
		bean.setMsgPosition(HumanReadablePlacement.HRP_NONE);
		//通过之前设置的信息，获取这个条码的真实信息
		BarcodeDimension barcodeDimension = bean.calcDimensions(content);
		//获取条码长度
		double barcodeDimensionWidth = barcodeDimension.getWidth();
		BufferedImage bi;
		if (barcodeDimensionWidth > width) {
			//如果最小的基础值生成的条码都比绘画区域大，那就直接按照条码的长度绘图
			bi = new BufferedImage((int) Math.ceil(barcodeDimensionWidth), height, BufferedImage.TYPE_INT_RGB);
		} else {
			//如果条码小那就计算最大的放大倍数
			double floor = Math.floor(width / barcodeDimensionWidth);
			//重新设置条码的基础值(扩大倍数)
			bean.setModuleWidth(floor);
			//设置新的绘图区域的宽度
			bi = new BufferedImage((int) Math.ceil(floor * barcodeDimensionWidth), height, BufferedImage.TYPE_INT_RGB);
		}
		Graphics2D g = bi.createGraphics();
		//全区域底色
		if (StringUtils.isEmpty(backgroundColor)) {
			g.setColor(Color.white);
		} else {
			g.setColor(new Color(Integer.parseInt(backgroundColor.substring(1), 16)));
		}
		g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
		//用笔绘画条码
		if (StringUtils.isEmpty(barcodeColor)) {
			g.setColor(Color.black);
		} else {
			g.setColor(new Color(Integer.parseInt(barcodeColor.substring(1), 16)));
		}
		Java2DCanvasProvider canvas = new Java2DCanvasProvider(g, 0);
		// 生产条形码
		bean.generateBarcode(canvas, content);
		g.dispose();
		return bi;
	}
	
	/**
	 * 通过第三方barcode4j生成25交替码Interleaved2Of5
	 * <br />实际计算公式参考：<a href="http://www.adams1.com/i25code.html">25交替码Interleaved2Of5实际计算公式</a>
	 *
	 * @param content         编码的内容（只能是数字，并且是偶数个，不是的话前面自动补0）
	 * @param width           条码的生成区域长度
	 * @param height          条码的生成区域高度
	 * @param barcodeColor    条形码颜色(不传默认为黑色)
	 * @param backgroundColor 条形码背景颜色（不传默认为白色）
	 * @return 图片对象
	 */
	public static BufferedImage generateInterleaved2Of5(String content, Integer width, Integer height, String barcodeColor, String backgroundColor) {
		Interleaved2Of5Bean bean = new Interleaved2Of5Bean();
		return getBarcodeBufferedImage(content, width, height, barcodeColor, backgroundColor, bean);
	}
	
	public static BufferedImage generateCode128(String content, int width, int height, String barcodeColor, String backgroundColor) {
		Code128Bean bean = new Code128Bean();
		return getBarcodeBufferedImage(content, width, height, barcodeColor, backgroundColor, bean);
	}
	
	public static BufferedImage generateEan13(String content, int width, int height, String barcodeColor, String backgroundColor) {
		EAN13Bean bean = new EAN13Bean();
		return getBarcodeBufferedImage(content, width, height, barcodeColor, backgroundColor, bean);
	}
	
	public static BufferedImage generateEan8(String content, int width, int height, String barcodeColor, String backgroundColor) {
		EAN8Bean bean = new EAN8Bean();
		return getBarcodeBufferedImage(content, width, height, barcodeColor, backgroundColor, bean);
	}
}
