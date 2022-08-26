package top.yueshushu.learn.qrcode.hutool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.Color;
import java.nio.charset.Charset;

import cn.hutool.extra.qrcode.QrConfig;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-06-28
 */
@Configuration
public class MyQrCodeConfig {
	//采用JavaConfig的方式显示注入hutool中 生成二维码
	@Bean
	public QrConfig qrConfig() {
		//初始宽度和高度
		QrConfig qrConfig = new QrConfig(300, 300);
		
		//设置边距，即二维码和边框的距离
		qrConfig.setMargin(2);
		//设置前景色
		qrConfig.setForeColor(Color.BLACK);
		//设置背景色
		qrConfig.setBackColor(Color.WHITE);
		//设置编码
		qrConfig.setCharset(Charset.forName("UTF-8"));
		return qrConfig;
	}
}
