package top.yueshushu.translate.model.translatevo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * @ClassName:YdTranslateVo
 * @Description 有道翻译的翻译信息
 * @Author zk_yjl
 * @Date 2021/9/15 14:11
 * @Version 1.0
 * @Since 1.0
 **/
@Data
public class YdTranslateVo implements Serializable {
    private String errorCode;
    private List<String> returnPhrase;
    private List<String> translation;
    private String l;
    private String dict;
    private String webdict;
}
