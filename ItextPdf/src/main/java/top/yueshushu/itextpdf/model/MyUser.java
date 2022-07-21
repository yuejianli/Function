package top.yueshushu.itextpdf.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户配置
 *
 * @author yuejianli
 * @date 2022-07-21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyUser implements Serializable {
	private Integer id;
	private String name;
	private Integer age;
	private String description;
}
