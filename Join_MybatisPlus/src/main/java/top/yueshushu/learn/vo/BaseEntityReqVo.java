package top.yueshushu.learn.vo;

import lombok.Data;
import top.yueshushu.learn.dynamic.SerializableFunction;
import top.yueshushu.learn.dynamic.SerializableFunctionUtil;

import java.io.Serializable;
import java.util.List;

/**
 * 用户信息
 *
 * @author yuejianli
 * @date 2022-11-08
 */
@Data
public class BaseEntityReqVo implements Serializable {
    private Integer sortType;  //1是逆序，否则为正序
    private String sortKey;
    private Integer limitN; //实例：limit #{limitN}
    private Integer limitOffset; //实例：offset #{limitOffset}
    private List<String> returnFields;  //默认为全部返回


    public <T> void setReturnFieldsByLambda(SerializableFunction<T, ?>... returnFields) {
        this.returnFields = SerializableFunctionUtil.getSignatureKey(returnFields);
    }

    public void setReturnFields(List<String> returnFields) {
        this.returnFields = returnFields;
    }

    public List<String> getReturnFields() {
        return this.returnFields;
    }
}
