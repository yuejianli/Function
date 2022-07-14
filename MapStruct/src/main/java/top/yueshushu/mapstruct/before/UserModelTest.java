package top.yueshushu.mapstruct.before;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import top.yueshushu.mapstruct.before.model.User;
import top.yueshushu.mapstruct.before.model.UserVo;

/**
 * MapUser 与 UserVo的区别:
 * <p>
 * sex 为 Integer ,   为 String
 * <p>
 * password    一个是 userPassword
 *
 * @author yuejianli
 * @date 2022-07-14
 */
@Slf4j
public class UserModelTest {
	
	private User user;
	
	@Before
	public void initUser() {
		user = new User();
		user.setId(1);
		user.setName("岳泽霖");
		user.setAge(28);
		user.setSex(1);
		user.setDescription("一个充满希望的老程序员");
		user.setPassword("123456");
		user.setBirthday(DateUtil.parse("1995-02-07", DatePattern.NORM_DATE_FORMAT));
		log.info(">>>> user is:{}", user);
	}
	
	/**
	 * 只使用 setter方法的设置
	 * <p>
	 * 11:25:10.360 [main] INFO top.yueshushu.mapstruct.before.UserModelTest - >>>> user is:MapUser(id=1, name=岳泽霖, birthday=1995-02-07 00:00:00, age=28, sex=1, description=一个充满希望的老程序员, password=123456)
	 * 11:25:10.362 [main] INFO top.yueshushu.mapstruct.before.UserModelTest - >>>>setting uservo is :MapUserVo(id=1, name=岳泽霖, birthday=1995-02-07 00:00:00, age=28, sex=男, description=一个充满希望的老程序员, userPassword=123456)
	 * <p>
	 * 发现 ，只有 2 毫秒
	 */
	@Test
	public void settingTest() {
		//1. 定义一个新的对象
		UserVo userVo = new UserVo();
		userVo.setId(user.getId());
		userVo.setName(user.getName());
		userVo.setAge(user.getAge());
		userVo.setSex(user.getSex() == 1 ? "男" : "女");
		userVo.setDescription(user.getDescription());
		userVo.setBirthday(user.getBirthday());
		userVo.setUserPassword(user.getPassword());
		log.info(">>>>setting uservo is :{}", userVo);
	}
	
	/**
	 * 使用 Bean Copy 处理
	 * <p>
	 * 1. 属性名不一致，
	 * 2. 属性名虽然一致，但类型不一致。
	 * <p>
	 * 11:23:29.863 [main] INFO top.yueshushu.mapstruct.before.UserModelTest - >>>> user is:MapUser(id=1, name=岳泽霖, birthday=1995-02-07 00:00:00, age=28, sex=1, description=一个充满希望的老程序员, password=123456)
	 * 11:23:29.915 [main] INFO top.yueshushu.mapstruct.before.UserModelTest - >>>>bean uservo is :MapUserVo(id=1, name=岳泽霖, birthday=1995-02-07 00:00:00, age=28, sex=null, description=一个充满希望的老程序员, userPassword=null)
	 * <p>
	 * 平均 将近 50 毫秒 ， 速度过慢。
	 */
	@Test
	public void beanCopyTest() {
		UserVo userVo = new UserVo();
		
		BeanUtils.copyProperties(user, userVo);
		
		// 需要再手动进行设置填充
		userVo.setSex(user.getSex() == 1 ? "男" : "女");
		userVo.setUserPassword(user.getPassword());
		
		log.info(">>>>bean uservo is :{}", userVo);
	}
	
}
