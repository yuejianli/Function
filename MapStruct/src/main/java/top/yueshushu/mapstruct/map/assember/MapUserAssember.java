package top.yueshushu.mapstruct.map.assember;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import top.yueshushu.mapstruct.map.model.*;

/**
 * 定义转换器
 *
 * @author yuejianli
 * @date 2022-07-14
 */
//1. 添加注解  @org.mapstruct.Mapper 注解
// 使用转换器  UserSexTransform
@Mapper(uses = {UserSexTransform.class})
public interface MapUserAssember {
	//2. 创建转换器
	MapUserAssember instance = Mappers.getMapper(MapUserAssember.class);
	
	/**
	 * 简单默认转换
	 */
	MapUserVo simpleToUserVo(MapUser mapUser);
	
	/**
	 * 转换，有属性映射的转换
	 */
	@Mapping(target = "userPassword", source = "password")
	MapUserVo userTargetToUserVo(MapUser mapUser);
	
	/**
	 * 通过 @Mappings, 制定多个规则。
	 */
//	@Mappings(
//			{
//					@Mapping(target = "userPassword",source = "password"),
//					@Mapping(target = "name",source = "name")
//			}
//	)
	@Mapping(target = "userPassword", source = "password")
	@Mapping(target = "name", source = "name")
	MapUserVo userMoreTargetToUserVo(MapUser mapUser);
	
	
	//如果进行了配置，会自动按照 MapUserSchool 默认的进行映射。  可是，如果里面有属性不一致呢?
	@Mapping(target = "mapUserSchoolVo", source = "mapUserSchool")
	MapUserVo userSchoolTargetToUserVo(MapUser mapUser);
	
	
	default MapUserSchoolVo mapUserSchoolToVo(MapUserSchool mapUserSchool) {
		MapUserSchoolVo mapUserSchoolVo = new MapUserSchoolVo();
		if (null == mapUserSchool) {
			return mapUserSchoolVo;
		}
		mapUserSchoolVo.setId(mapUserSchool.getId());
		mapUserSchoolVo.setName(mapUserSchool.getName());
		mapUserSchoolVo.setAddressVo(mapUserSchool.getAddress());
		return mapUserSchoolVo;
	}
	
	/**
	 * 多个对象，合并成一个对象。
	 * 注意，需要将所有的属性都进行合并。
	 */
	@Mapping(target = "id", source = "mapUser.id")
	@Mapping(target = "name", source = "mapUser.name")
	@Mapping(target = "sex", source = "mapUser.sex")
	@Mapping(target = "age", source = "mapUser.age")
	@Mapping(target = "description", source = "mapUser.description")
	@Mapping(target = "userPassword", source = "mapUser.password")
	@Mapping(target = "schoolId", source = "mapUserSchool.id")
	@Mapping(target = "schoolName", source = "mapUserSchool.name")
	@Mapping(target = "schoolAddress", source = "mapUserSchool.address")
	MapMoreUserSchoolVo mapMoreVo(MapUser mapUser, MapUserSchool mapUserSchool);
	
	
	/**
	 * 转换，有属性映射的转换
	 */
	@Mapping(target = "userPassword", source = "password")
	void userMapperTargetToUserVo(MapUser mapUser, String password, @MappingTarget MapUserVo mapUserVo);
	
	
	/**
	 * 对性别进行转换
	 */
	@Mapping(target = "userPassword", source = "password")
	MapUserVo sexConvert(MapUser mapUser);
	
	
}
