package top.yueshushu.mapstruct.map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import top.yueshushu.mapstruct.map.assember.MapListUserAssember;
import top.yueshushu.mapstruct.map.assember.MapUserAssember;
import top.yueshushu.mapstruct.map.model.MapMoreUserSchoolVo;
import top.yueshushu.mapstruct.map.model.MapUser;
import top.yueshushu.mapstruct.map.model.MapUserSchool;
import top.yueshushu.mapstruct.map.model.MapUserVo;

/**
 * MapStruct 处理 实体转换
 *
 * @author yuejianli
 * @date 2022-07-14
 */
@Slf4j
public class MapUserModelTest {
	
	private MapUser mapUser;
	
	private MapUserSchool mapUserSchool;
	
	@Before
	public void initUser() {
		mapUser = new MapUser();
		mapUser.setId(1);
		mapUser.setName("岳泽霖");
		mapUser.setAge(28);
		mapUser.setSex(1);
		mapUser.setDescription("一个充满希望的老程序员");
		mapUser.setPassword("123456");
		mapUser.setBirthday(DateUtil.parse("1995-02-07", DatePattern.NORM_DATE_FORMAT));
		
		mapUserSchool = new MapUserSchool();
		mapUserSchool.setId(1);
		mapUserSchool.setName("睢县回高");
		mapUserSchool.setAddress("河南省睢县回高");
		mapUser.setMapUserSchool(mapUserSchool);
		log.info(">>>> user is:{}", mapUser);
	}
	
	/**
	 * 11:46:23.283 [main] INFO top.yueshushu.mapstruct.map.MapUserModelTest - >>>> user is:MapUser(id=1, name=岳泽霖, birthday=1995-02-07 00:00:00, age=28, sex=1, description=一个充满希望的老程序员, password=123456)
	 * 11:46:23.286 [main] INFO top.yueshushu.mapstruct.map.MapUserModelTest - >>> map simple user is MapUserVo(id=1, name=岳泽霖, birthday=1995-02-07 00:00:00, age=28, sex=1, description=一个充满希望的老程序员, userPassword=null)
	 * <p>
	 * 只有3秒， 转换速度挺快。
	 * sex 同属性有值， 但发现 userPassword 没有值
	 */
	@Test
	public void simpleToUserVoTest() {
		
		MapUserVo mapUserVo = MapUserAssember.instance.simpleToUserVo(mapUser);
		log.info(">>> map simple user is {}", mapUserVo);
	}
	
	/**
	 * 11:50:37.949 [main] INFO top.yueshushu.mapstruct.map.MapUserModelTest - >>>> user is:MapUser(id=1, name=岳泽霖, birthday=1995-02-07 00:00:00, age=28, sex=1, description=一个充满希望的老程序员, password=123456)
	 * 11:50:37.952 [main] INFO top.yueshushu.mapstruct.map.MapUserModelTest - >>> map target user is MapUserVo(id=1, name=岳泽霖, birthday=1995-02-07 00:00:00, age=28, sex=1, description=一个充满希望的老程序员, userPassword=123456)
	 * userPassword 存在值了。
	 * <p>
	 * <p>
	 * source 对象中的属性不存在时，会报错。
	 * Error:(37, 41) java: No property named "pass" exists in source parameter(s). Did you mean "pass"?
	 */
	@Test
	public void userTargetToUserVoTst() {
		
		MapUserVo mapUserVo = MapUserAssember.instance.userTargetToUserVo(mapUser);
		log.info(">>> map target user is {}", mapUserVo);
	}
	
	
	@Test
	public void userMoreTargetToUserVoTest() {
		
		MapUserVo mapUserVo = MapUserAssember.instance.userMoreTargetToUserVo(mapUser);
		log.info(">>> map target user is {}", mapUserVo);
	}
	
	/**
	 * 有内置对象的
	 */
	@Test
	public void userSchoolTargetToUserVoTest() {
		MapUserVo mapUserVo = MapUserAssember.instance.userSchoolTargetToUserVo(mapUser);
		log.info(">>> map target user is {}", mapUserVo);
	}
	
	/**
	 * 多对象转换
	 */
	@Test
	public void mapMoreVoTest() {
		MapMoreUserSchoolVo mapMoreUserSchoolVo = MapUserAssember.instance.mapMoreVo(mapUser, mapUserSchool);
		
		log.info(">>> more user is {}", mapMoreUserSchoolVo);
	}
	
	/**
	 * 更新某个属性
	 */
	@Test
	public void userMapperTargetToUserVoTest() {
		MapUserVo mapUserVo = MapUserAssember.instance.userTargetToUserVo(mapUser);
		log.info(">>>>> simple {}", mapUserVo);
		
		// 更新对象
		mapUser.setPassword("i change password");
		
		MapUserAssember.instance.userMapperTargetToUserVo(mapUser, mapUser.getPassword(), mapUserVo);
		
		log.info(">>>>> new {}", mapUserVo);
	}
	
	/**
	 * 集合形式转换
	 */
	@Test
	public void userListConvertTest() {
		
		List<MapUser> mapUserList = new ArrayList<>();
		mapUserList.add(mapUser);
		// 构建一个新的 mapUser
		MapUser mapUser1 = new MapUser();
		
		BeanUtils.copyProperties(mapUser, mapUser1);
		mapUser1.setPassword(null);
		mapUserList.add(mapUser1);
		
		List<MapUserVo> mapUserVoList = MapListUserAssember.instance.userListConvert(mapUserList);
		mapUserVoList.forEach(
				n -> {
					log.info(">>>>> lis {}", n);
				}
		);
	}
	
	@Test
	public void sexConvertTest() {
		MapUserVo mapUserVo = MapUserAssember.instance.sexConvert(mapUser);
		log.info(">>>>> sex transform {}", mapUserVo);
	}
}
