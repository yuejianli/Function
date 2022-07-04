package top.yueshushu.learn.emoji.test;

import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;

import org.junit.Test;

import java.util.Collection;
import java.util.List;

import cn.hutool.extra.emoji.EmojiUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * hutool 工具类的处理， 使用的是: EmojiUtil
 *
 * @author yuejianli
 * @date 2022-07-04
 */
@Slf4j
public class HutoolTest {
	
	@Test
	public void getAllTest() {
		Collection<Emoji> emojiList = EmojiManager.getAll();
		emojiList.forEach(
				n -> {
					log.info("获取表情:{}", n);
				}
		);
	}
	
	/**
	 * Emoji{
	 * description='smiling face with open mouth and smiling eyes',
	 * supportsFitzpatrick=false,
	 * aliases=[smile],
	 * tags=[happy, joy, pleased],
	 * unicode='😄',
	 * htmlDec='&#128516;',
	 * htmlHex='&#x1f604;'
	 * }
	 * <p>
	 * 表情提取
	 */
	@Test
	public void extractEmojisTest() {
		// 😄
		List<String> emojiList = EmojiUtil.extractEmojis("abcdd\uD83D\uDE04 2323");
		log.info("提供表情表情:{}", emojiList);
	}
	
	/**
	 * 移除所有的表情
	 */
	@Test
	public void removeAllEmojisTest() {
		String s = EmojiUtil.removeAllEmojis("abcdd\uD83D\uDE04 2323");
		log.info("移除表情后的字符串:{}", s);
	}
	
	/**
	 * 转换成 html 样式
	 */
	@Test
	public void toHtmlTest() {
		String s = EmojiUtil.toHtml("\uD83D\uDE04");
		log.info(">>>转换成的 html {}", s);
	}
	
	@Test
	public void toUnicodeTest() {
		String s = EmojiUtil.toUnicode("\uD83D\uDE04");
		// 😄
		log.info(">>>转换成的 unicode {}", s);
	}
}
