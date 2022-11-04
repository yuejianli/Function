package top.yueshushu.easyexcel.writepojo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode
@ContentRowHeight(40)
@HeadRowHeight(40)
@ColumnWidth(25)
public class WidthAndHeightData {

    @ExcelProperty(value = "名称")
    private String name;
    @ExcelProperty(value = "生日")
    private Date birthday;
    /**
     * 宽度为50
     */
    @ColumnWidth(50)
    @ExcelProperty(value = "年龄")
    private Integer age;

}