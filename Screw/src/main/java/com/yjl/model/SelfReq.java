package com.yjl.model;

import java.io.Serializable;

import lombok.Data;

/**
 * 自定义设置生成请求配置信息
 *
 * @author yuejianli
 * @date 2022-07-06
 */
@Data
public class SelfReq implements Serializable {
	private String url;
	private String driverClassName;
	private String userName;
	private String password;
	private String filePath;
	private Integer fileType;
	private String fileName;
	// 指定生成的信息
	private String tableNameStr;
	private String prefixTableNameStr;
	private String suffixTableNameStr;
	//指定忽略的信息
	private String ignoreTableNameStr;
	private String ignorePrefixTableNameStr;
	private String ignoreSuffixTableNameStr;
	private String version;
	private String description;
}
