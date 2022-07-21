package top.yueshushu.itextpdf.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 用户档案
 * @author yuejianli
 * @date 2022-07-21
 */
@Data
public class UserArchives implements Serializable {
	private Integer id;
	private String name;
	private Integer age;
	private String sex;
	private String address;
	private String marry;
	/* 教育经历*/
	private List<School> schoolList;
	// 评价
	private String evaluation;
}
