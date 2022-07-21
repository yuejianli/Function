package top.yueshushu.itextpdf.model;

import java.io.Serializable;

import lombok.Data;

/**
 * 学校经历
 *
 * @author yuejianli
 * @date 2022-07-21
 */
@Data
public class School implements Serializable {
	private String name;
	private String startDate;
	private String endDate;
}
