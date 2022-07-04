package top.yueshushu.learn.emoji.test;

import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;

import org.junit.Test;

import java.util.Collection;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

/**
 * 原始的 emoji-java 的处理
 *
 * @author yuejianli
 * @date 2022-07-04
 */
// @SpringBootTest
@Slf4j
public class EmojiManagerTest {
	/**
	 * 获取所有的表情符合
	 * <p>
	 * 16:49:41.765 [main] INFO top.yueshushu.learn.emoji.test.EmojiManagerTest - 获取表情:Emoji{description='smiling face with open mouth and smiling eyes',
	 * supportsFitzpatrick=false, aliases=[smile], tags=[happy, joy, pleased], unicode='😄', htmlDec='&#128516;', htmlHex='&#x1f604;'}
	 * 16:49:41.768 [main] INFO top.yueshushu.learn.emoji.test.EmojiManagerTest - 获取表情:Emoji{description='smiling face with open mouth',
	 * supportsFitzpatrick=false, aliases=[smiley], tags=[happy, joy, haha], unicode='😃', htmlDec='&#128515;', htmlHex='&#x1f603;'}
	 * 16:49:41.769 [main] INFO top.yueshushu.learn.emoji.test.EmojiManagerTest - 获取表情:Emoji{description='grinning face',
	 * supportsFitzpatrick=false, aliases=[grinning], tags=[smile, happy], unicode='😀', htmlDec='&#128512;', htmlHex='&#x1f600;'}
	 * 16:49:41.769 [main] INFO top.yueshushu.learn.emoji.test.EmojiManagerTest - 获取表情:Emoji{description='smiling face with smiling eyes',
	 * supportsFitzpatrick=false, aliases=[blush], tags=[proud], unicode='😊', htmlDec='&#128522;', htmlHex='&#x1f60a;'}
	 */
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
	 * 获取标签, getForTag (传入 tag 名)
	 */
	@Test
	public void getForTagTest() {
		Set<Emoji> happyList = EmojiManager.getForTag("happy");
		happyList.forEach(
				n -> {
					log.info("获取表情:{}", n);
				}
		);
	}
	
	/**
	 * 获取所有的标签
	 */
	@Test
	public void getAllTagsTest() {
		
		Collection<String> allTags = EmojiManager.getAllTags();
		allTags.forEach(
				n -> {
					log.info("获取标签{}", n);
				}
		);
	}
	
	/**
	 * 根据别名，获取表情
	 */
	@Test
	public void getForAliasTest() {
		
		Emoji smile = EmojiManager.getForAlias("smile");
		log.info("获取表情:{}", smile);
	}
	
	/**
	 * 通过 unicode 获取表情
	 * <p>
	 * Emoji{description='smiling face with open mouth and smiling eyes', supportsFitzpatrick=false,
	 * aliases=[smile], tags=[happy, joy, pleased], unicode='😄', htmlDec='&#128516;', htmlHex='&#x1f604;'}
	 */
	@Test
	public void getByUnicodeTest() {
		
		Emoji unicodeEmoji = EmojiManager.getByUnicode("\uD83D\uDE04");
		log.info("获取表情:{}", unicodeEmoji);
	}
	
	/**
	 * 是否是表情
	 */
	@Test
	public void isEmojiTest() {
		// 😄
		boolean emoji = EmojiManager.isEmoji("\uD83D\uDE04");
		log.info("是否是表情{}", emoji);
		emoji = EmojiManager.isEmoji("\uD83D\uDE04\223");
		log.info("是否是表情{}", emoji);
	}
	
	
}
