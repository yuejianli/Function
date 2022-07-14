package top.yueshushu.mapstruct.spring;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import top.yueshushu.mapstruct.spring.assember.SpringUserAssember;
import top.yueshushu.mapstruct.spring.model.SpringUser;
import top.yueshushu.mapstruct.spring.model.SpringUserVo;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-07-14
 */
@SpringBootTest
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringMapStructTest {
	
	@Resource
	private SpringUserAssember springUserAssember;
	
	
	private SpringUser springUser;
	
	@Before
	public void initUser() {
		springUser = new SpringUser();
		springUser.setId(1);
		springUser.setName("岳泽霖");
		springUser.setAge(28);
		springUser.setSex(1);
		springUser.setDescription("一个充满希望的老程序员");
		springUser.setPassword("123456");
		springUser.setBirthday(DateUtil.parse("1995-02-07", DatePattern.NORM_DATE_FORMAT));
		log.info(">>>> user is:{}", springUser);
	}
	
	@Test
	public void springUserTest() {
		SpringUserVo springUserVo = springUserAssember.userTargetToUserVo(springUser);
		log.info("spring user {}:", springUserVo);
	}
}
