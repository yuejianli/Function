package top.yueshushu.translate.model;

import java.io.Serializable;

import lombok.Data;

/**
 * @ClassName:BdTranDto
 * @Description 百度翻译所使用的Dto
 * @Author zk_yjl
 * @Date 2021/10/12 11:12
 * @Version 1.0
 * @Since 1.0
 **/
@Data
public class BdTranDto implements Serializable {
    private String name;
    private String code;
    private String content;
}
