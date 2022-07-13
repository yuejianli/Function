package top.yueshushu.translate.model;

import lombok.Data;

/**
 * @ClassName:BdTranQueryDto
 * @Description 百度翻译查询使用dto
 * @Author zk_yjl
 * @Date 2021/10/12 12:54
 * @Version 1.0
 * @Since 1.0
 **/
@Data
public class BdTranQueryDto {
    private String query;
    private String from;
    private String to;
    private String simple_means_flag;
    private String sign;
    private String token;
    private String domain;
    private String transtype;
}
