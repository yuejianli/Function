package top.yueshushu.learn;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;
import top.yueshushu.learn.dynamic.SerializableFunctionUtil;
import top.yueshushu.learn.pojo.Role;
import top.yueshushu.learn.pojo.User;
import top.yueshushu.learn.service.RoleService;
import top.yueshushu.learn.service.UserService;
import top.yueshushu.learn.vo.UserReqVo;
import top.yueshushu.learn.vo.UserResVo;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-09-05
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Log4j2
public class MoreTests {
	@Resource
	private UserService userService;
	@Resource
	private RoleService roleService;


	/**
	 * 查询 DB 次数比较多， 且返回结果不动态。
	 */
	@Test
	public void simpleTest() {
		//1. 传递过来一个条件
		UserReqVo userReqVo = new UserReqVo();
		userReqVo.setName("岳");
		userReqVo.setRoleName("用户");

		//2.  进行查询

		//2.1 先按照条件少的，非主体的 角色进行查询对应的适合信息.

		List<Role> roleList = roleService.lambdaQuery()
				.like(StringUtils.isNotBlank(userReqVo.getRoleName()), Role::getRoleName, userReqVo.getRoleName())
				.list();
		// 构建出 角色id
		List<Integer> roleIdList = roleList.stream().map(Role::getId).collect(Collectors.toList());

		Map<Integer, Role> roleMap = roleList.stream().collect(Collectors.toMap(Role::getId, n -> n));


		// 查询出用户信息
		List<User> userList = userService.lambdaQuery()
				.like(StringUtils.isNotBlank(userReqVo.getName()), User::getName, userReqVo.getName())
				.in(!CollectionUtils.isEmpty(roleIdList), User::getRoleId, roleIdList)
				.list();

		if (CollectionUtils.isEmpty(userList)) {
			log.info(">>>> 没有匹配的用户");
			return;
		}
		//3. 构建返回信息
		List<UserResVo> userResVoList = userList.stream().map(
				n -> {
					UserResVo userResVo = new UserResVo();

					// 填充用户信息
					BeanUtils.copyProperties(n, userResVo);

					Role role = roleMap.get(n.getRoleId());

					// 填充角色信息
					BeanUtils.copyProperties(role, userResVo);

					return userResVo;
				}
		).collect(Collectors.toList());

		//4. 打印返回信息
		userResVoList.forEach(
				n -> {
					log.info(">>>> 查询出员工信息: {}", n);
				}
		);
	}


	/**
	 * 动态查询处理
	 * <p>
	 * select `user`.`id` as `id` , `user`.`name` as `name` , `role`.`name` as `name` , `role`.`description` as `description` from user user left join role role on user.role_id = role.id WHERE user.name like concat('%',?,'%')
	 * AND role.name like concat('%',?,'%') order by user.name desc limit ? offset ?
	 */
	@Test
	public void dynamicTest() {
		//1. 传递过来一个条件
		UserReqVo userReqVo = new UserReqVo();
		userReqVo.setName("岳");
		userReqVo.setRoleName("用户");
		userReqVo.setSortKey("user.name");
		userReqVo.setSortType(1);
		userReqVo.setLimitN(10);
		userReqVo.setLimitOffset(0);
		userReqVo.setReturnFields(
				SerializableFunctionUtil.getSignatureKey(UserResVo::getId, UserResVo::getName, UserResVo::getRoleName, UserResVo::getRoleDescription)
		);

		//2.  进行查询
		List<UserResVo> userResVoList = userService.findListByCondition(userReqVo);

		if (CollectionUtils.isEmpty(userResVoList)) {
			log.info(">>>> 没有匹配的用户");
			return;
		}

		//4. 打印返回信息
		userResVoList.forEach(
				n -> {
					log.info(">>>> 查询出员工信息: {}", n);
				}
		);
	}


}
