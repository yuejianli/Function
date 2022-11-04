package top.yueshushu.easyexcel.readpojo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @ClassName:Since
 * @Description 读取批注，超链接和合并单元格时使用
 * @Author yjl
 * @Date 2021/6/7 19:57
 * @Version 1.0
 **/
@Data
public class ExtraSince {
    /**
     * @param notation 批注
     * @param href 超链接信息。
     */
    @ExcelProperty(index = 0)
    private String notation;
    @ExcelProperty(index = 1)
    private String href;
    @ExcelProperty(index = 2)
    private String merge;
}
