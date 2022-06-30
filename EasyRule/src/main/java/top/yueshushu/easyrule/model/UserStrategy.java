package top.yueshushu.easyrule.model;

import lombok.Data;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-06-30
 */
@Data
public class UserStrategy {
	private Integer id;
	private Integer userId;
	private Integer strategyAge;
	private String strategySex;
	private String strategyDescription;
}
