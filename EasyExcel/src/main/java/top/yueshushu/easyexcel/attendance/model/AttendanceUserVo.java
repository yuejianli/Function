package top.yueshushu.easyexcel.attendance.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @ClassName:AttendanceInfo
 * @Description 要展示的对象信息，按照公司业务实际的来，这儿只做模拟。
 * @Author zk_yjl
 * @Date 2021/9/17 14:19
 * @Version 1.0
 * @Since 1.0
 **/
@Data
public class AttendanceUserVo {
    @ExcelIgnore
    private Integer id;
    @ExcelProperty(value = "员工工号")
    private String jobNum;
    @ExcelProperty(value = "姓名")
    private String name;
    @ExcelProperty(value = "工伤假天数")
    private Integer inductrialDay;
    @ExcelProperty(value = "病假天数")
    private Integer diseaseDay;
    @ExcelProperty(value = "产假天数")
    private Integer maternityDay;
    @ExcelProperty(value = "产检假天数")
    private Integer prenatalDay;
    // @ExcelProperty(value = "产检假天数2")
    // private Integer prenatalDay2;
    @ExcelProperty(value = "班次")
    private String shift;
    @ExcelProperty(value = "D1")
    private String day1;
    @ExcelProperty(value = "D2")
    private String day2;
    @ExcelProperty(value = "D3")
    private String day3;
    @ExcelProperty(value = "D4")
    private String day4;
    @ExcelProperty(value = "D5")
    private String day5;
    @ExcelProperty(value = "D6")
    private String day6;
    @ExcelProperty(value = "D7")
    private String day7;
    @ExcelProperty(value = "D8")
    private String day8;
    @ExcelProperty(value = "D9")
    private String day9;
    @ExcelProperty(value = "D10")
    private String day10;
    @ExcelProperty(value = "D11")
    private String day11;
    @ExcelProperty(value = "D12")
    private String day12;
    @ExcelProperty(value = "D13")
    private String day13;
    @ExcelProperty(value = "D14")
    private String day14;
    @ExcelProperty(value = "D15")
    private String day15;
    @ExcelProperty(value = "D16")
    private String day16;
    @ExcelProperty(value = "D17")
    private String day17;
    @ExcelProperty(value = "D18")
    private String day18;
    @ExcelProperty(value = "D19")
    private String day19;
    @ExcelProperty(value = "D20")
    private String day20;
    @ExcelProperty(value = "D21")
    private String day21;
    @ExcelProperty(value = "D22")
    private String day22;
    @ExcelProperty(value = "D23")
    private String day23;
    @ExcelProperty(value = "D24")
    private String day24;
    // ... 一直 day 下去，直到 31天
    @ExcelProperty(value = "D25")
    private String day25;
    @ExcelProperty(value = "D26")
    private String day26;
    @ExcelProperty(value = "D27")
    private String day27;
    @ExcelProperty(value = "D28")
    private String day28;
    @ExcelProperty(value = "D29")
    private String day29;
    @ExcelProperty(value = "D30")
    private String day30;
    @ExcelProperty(value = "D31")
    private String day31;
}
