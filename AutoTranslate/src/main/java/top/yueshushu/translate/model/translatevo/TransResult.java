package top.yueshushu.translate.model.translatevo;

import java.io.Serializable;

import lombok.Data;

/**
 * @ClassName:TransResult
 * @Description 翻译的结果，是 TranslateVo的二级对象。
 * @Author zk_yjl
 * @Date 2021/9/15 9:27
 * @Version 1.0
 * @Since 1.0
 **/
@Data
public class TransResult implements Serializable {
    private String src;
    private String dst;
}
