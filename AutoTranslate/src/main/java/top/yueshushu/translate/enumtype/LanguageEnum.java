package top.yueshushu.translate.enumtype;

import lombok.Getter;

/**
 * 数据状态
 *
 * @author xin.liu
 * @date 2020-05-28
 */
@SuppressWarnings("unused")
@Getter
public enum LanguageEnum {
    
    
    CHINA("zh", "中文"),
    EN("en", "英语"),
    JP("jp", "日语"),
    ;
    
    private String value;
    private String msg;
    
    LanguageEnum(String value, String msg) {
        this.value = value;
        this.msg = msg;
    }
}
