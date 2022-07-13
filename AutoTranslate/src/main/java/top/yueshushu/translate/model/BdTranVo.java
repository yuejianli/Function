package top.yueshushu.translate.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * @ClassName:BdTranVo
 * @Description 百度翻译所使用的Vo
 * @Author zk_yjl
 * @Date 2021/10/12 11:14
 * @Version 1.0
 * @Since 1.0
 **/
@Data
public class BdTranVo implements Serializable {
    private String content;
    private List<BdTranDto> tranList;
}
