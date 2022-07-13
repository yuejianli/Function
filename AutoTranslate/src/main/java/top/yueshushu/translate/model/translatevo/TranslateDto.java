package top.yueshushu.translate.model.translatevo;

import lombok.Data;

/**
 * @ClassName:TranslateRo
 * @Description 翻译的Dto 跳转对象
 * @Author zk_yjl
 * @Date 2021/9/15 9:29
 * @Version 1.0
 * @Since 1.0
 **/
@Data
public class TranslateDto {
    private String from;
    private String to;
    private String q;
}
