package top.yueshushu.translate.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import top.yueshushu.translate.enumtype.LanguageEnum;
import top.yueshushu.translate.util.TranslateUtil;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-07-13
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class TranslateTest {
	@Resource
	private TranslateUtil translateUtil;
	
	@Test
	public void getTranslateTest() {
		
		String content = translateUtil.getTranslate("Hello", LanguageEnum.EN.getValue(), LanguageEnum.CHINA.getValue());
		log.info("content is {}", content);
	}
}
