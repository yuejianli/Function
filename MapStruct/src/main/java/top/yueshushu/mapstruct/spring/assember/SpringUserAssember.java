package top.yueshushu.mapstruct.spring.assember;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import top.yueshushu.mapstruct.map.assember.UserSexTransform;
import top.yueshushu.mapstruct.spring.model.SpringUser;
import top.yueshushu.mapstruct.spring.model.SpringUserVo;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-07-14
 */
@Component
@Mapper(componentModel = "spring", uses = {UserSexTransform.class})
public interface SpringUserAssember {
	
	/**
	 * 转换，有属性映射的转换
	 */
	@Mapping(target = "userPassword", source = "password")
	SpringUserVo userTargetToUserVo(SpringUser springUser);
}
