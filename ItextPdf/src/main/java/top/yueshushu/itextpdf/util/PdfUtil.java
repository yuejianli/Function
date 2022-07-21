package top.yueshushu.itextpdf.util;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.layout.font.FontProvider;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

import lombok.extern.slf4j.Slf4j;

/**
 * ItextPdf 工具类
 *
 * @author yuejianli
 * @date 2022-07-21
 */
@Slf4j
public class PdfUtil {
	
	/**
	 * HTML 转 PDF
	 * @param content html内容
	 * @param outPath           输出pdf路径
	 * @return 是否创建成功
	 */
	public static boolean html2Pdf(String content, String outPath) {
		try {
			ConverterProperties converterProperties = new ConverterProperties();
			converterProperties.setCharset("UTF-8");
			FontProvider fontProvider = new FontProvider();
			fontProvider.addSystemFonts();
			converterProperties.setFontProvider(fontProvider);
			// 转换成目录
			HtmlConverter.convertToPdf(content,new FileOutputStream(outPath),converterProperties);
		} catch (Exception e) {
			log.error("生成模板内容失败,{}",e);
			return false;
		}
		return true;
	}
	/**
	 * HTML 转 PDF
	 * @param content html内容
	 * @return PDF字节数组
	 */
	public static byte[] html2Pdf(String content) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();;
		try {
			ConverterProperties converterProperties = new ConverterProperties();
			converterProperties.setCharset("UTF-8");
			FontProvider fontProvider = new FontProvider();
			fontProvider.addSystemFonts();
			converterProperties.setFontProvider(fontProvider);
			// 转换 成流
			HtmlConverter.convertToPdf(content,outputStream,converterProperties);
		} catch (Exception e) {
			log.error("生成 PDF 失败,{}",e);
		}
		return outputStream.toByteArray();
	}
}
