package top.yueshushu.easyexcel.writepojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import com.alibaba.excel.enums.poi.FillPatternTypeEnum;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName:WritePerson
 * @Description TODO
 * @Author yjl
 * @Date 2021/6/8 9:15
 * @Version 1.0   这是注解的形式，进行设置相应的风格。
 **/
@Data
//内容的高度
@ContentRowHeight(10)
//表题行的高度
@HeadRowHeight(20)
//列的宽度
@ColumnWidth(25)
//设置头背景成红色
@HeadStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 10)
//设置字体大小
@HeadFontStyle(fontName = "宋休", fontHeightInPoints = 20)
//设置内容
@ContentStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 17)
@ContentFontStyle(fontName = "楷书", fontHeightInPoints = 16)
public class WritePersonFormat {
    /**
     * @param id id编号
     * @param name 名称
     * @param birthday 生日
     * @param age 年龄
     * @param score 成绩
     */
    @ExcelProperty(value = "Id编号")
    private Integer id;
    //单独设计宽度
    @ColumnWidth(50)
    @ExcelProperty(value = "名称")
    //设置头背景成红色
    @HeadStyle(fillPatternType = FillPatternTypeEnum.ALT_BARS, fillForegroundColor = 15)
    //设置字体大小
    @HeadFontStyle(fontName = "宋休", fontHeightInPoints = 25)
    @ContentStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 19)
    @ContentFontStyle(fontName = "楷书", fontHeightInPoints = 23)
    private String name;
    @ColumnWidth(60)
    @ExcelProperty(value = "生日")
    private Date birthday;
    @ColumnWidth(15)
    @ExcelProperty(value = "年龄")
    private Integer age;
    @ExcelProperty(value = "成绩")
    private Double score;
    //忽略的字段
    @ExcelIgnore
    private String other;
}
