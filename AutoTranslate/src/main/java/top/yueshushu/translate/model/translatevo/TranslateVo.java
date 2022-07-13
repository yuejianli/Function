package top.yueshushu.translate.model.translatevo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * @ClassName:TranslateVo
 * @Description 翻译后展示的对象
 * @Author zk_yjl
 * @Date 2021/9/15 9:27
 * @Version 1.0
 * @Since 1.0
 **/
@Data
public class TranslateVo implements Serializable {
    private String from;
    private String to;
    private List<TransResult> transResult;
}
