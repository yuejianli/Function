package top.yueshushu.learn.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
@EqualsAndHashCode
@TableName("`person`")
public class PersonDO {

    @TableId
    private Integer id;

    private String name;

    private Integer sex;

    private String headImg;

    private Integer addressId;

    @TableLogic
    private Boolean del;
}
