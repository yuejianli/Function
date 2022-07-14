package top.yueshushu.mapstruct.map.model;

import java.io.Serializable;

import lombok.Data;

/**
 * 用户关联的 School 列表
 *
 * @author yuejianli
 * @date 2022-07-14
 */
@Data
public class MapUserSchool implements Serializable {
	private Integer id;
	private String name;
	private String address;
}
