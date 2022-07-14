package top.yueshushu.mapstruct.map.assember;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

import top.yueshushu.mapstruct.map.model.MapUser;
import top.yueshushu.mapstruct.map.model.MapUserSchool;
import top.yueshushu.mapstruct.map.model.MapUserSchoolVo;
import top.yueshushu.mapstruct.map.model.MapUserVo;

/**
 * list 集合形式进行转换
 *
 * @author yuejianli
 * @date 2022-07-14
 */
@Mapper
public interface MapListUserAssember {
	
	MapListUserAssember instance = Mappers.getMapper(MapListUserAssember.class);
	
	
	@Mapping(target = "userPassword", source = "password", defaultValue = "no password")
	@Mapping(target = "mapUserSchoolVo", source = "mapUserSchool")
	MapUserVo userToVo(MapUser mapUser);
	
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
	 * 集合映射转换
	 *
	 * @param mapUsers
	 */
	List<MapUserVo> userListConvert(List<MapUser> mapUsers);
	
}
