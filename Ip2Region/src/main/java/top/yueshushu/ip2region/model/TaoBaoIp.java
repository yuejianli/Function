package top.yueshushu.ip2region.model;

import java.io.Serializable;

import lombok.Data;

/**
 * IP地址展示
 *
 * @author yuejianli
 * @date 2022-07-25
 */
@Data
public class TaoBaoIp implements Serializable {
	private String ip;
	private String country;
	private String area;
	private String region;
	private String city;
	private String isp;
	private String country_id;
	private String area_id;
	private String region_id;
	private String city_id;
	private String isp_id;
}
