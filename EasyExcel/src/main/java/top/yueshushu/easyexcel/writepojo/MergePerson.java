package top.yueshushu.easyexcel.writepojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ContentLoopMerge;
import com.alibaba.excel.annotation.write.style.OnceAbsoluteMerge;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName:WritePerson
 * @Description 单元格合并
 * @Author yjl
 * @Date 2021/6/8 9:15
 * @Version 1.0
 **/
@Data
@OnceAbsoluteMerge(firstRowIndex = 5, lastRowIndex = 6, firstColumnIndex = 3, lastColumnIndex = 4)
public class MergePerson {
    /**
     * @param id id编号
     * @param name 名称
     * @param birthday 生日
     * @param age 年龄
     * @param score 成绩
     */
    @ExcelProperty(value = "Id编号")
    @ContentLoopMerge(eachRow = 2)
    private Integer id;
    @ExcelProperty(value = "名称")
    private String name;
    @ExcelProperty(value = "生日")
    private Date birthday;
    @ExcelProperty(value = "年龄")
    private Integer age;
    @ExcelProperty(value = "成绩")
    private Double score;
    //忽略的字段
    @ExcelIgnore
    private String other;
}
