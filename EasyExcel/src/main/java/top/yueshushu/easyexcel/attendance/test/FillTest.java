package top.yueshushu.easyexcel.attendance.test;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import org.junit.Test;
import top.yueshushu.easyexcel.attendance.handler.ExcelMergeStrategy;
import top.yueshushu.easyexcel.attendance.util.DataUtil;
import top.yueshushu.easyexcel.util.WriteDataUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * @ClassName:FillTest
 * @Description TODO
 * @Author zk_yjl
 * @Date 2021/9/17 15:00
 * @Version 1.0
 * @Since 1.0
 **/
public class FillTest {

    @Test
    public void dynamicWrite() throws Exception {
        File file = new File(WriteDataUtil.FILE_PATH + "attendance.xlsx");
        OutputStream outputStream = new FileOutputStream(file);
        ExcelWriter excelWriter = null;
        try {
            //定义合并的策略
            //需要合并的列数。
            int[] mergeCell = getMergeIndex();

            List<List<String>> dynamicHeaderList = header();
            //获取合并的列数。
            ExcelMergeStrategy excelMergeStrategy = new ExcelMergeStrategy(2, mergeCell, 4);
            EasyExcel.write(outputStream)
                    // 这里放入动态头
                    .head(dynamicHeaderList)
                    //使用合并策略
                    .registerWriteHandler(excelMergeStrategy)
                    .sheet("员工考勤统计表")
                    //里面填充数据
                    .doWrite(DataUtil.getVirtualDataList());


            System.out.println(">>>>>>>>数据导出成功");
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    /**
     * 处理导出的列，只展示动态表头里面的数据。
     *
     * @param dynamicHeaderList
     * @return java.util.Set<java.lang.String>
     * @date 2021/9/17 18:30
     * @author zk_yjl
     */
    private Set<String> getExportList(List<List<String>> dynamicHeaderList) {
        Set<String> result = new HashSet<>();
        for (List<String> fieldList : dynamicHeaderList) {
            String filed = fieldList.get(0);
            result.add(filed);
        }
        return result;
    }

    /**
     * 获取要合并的列数
     *
     * @param
     * @return int[]
     * @date 2021/9/17 15:34
     * @author zk_yjl
     */
    private int[] getMergeIndex() {
        List<Integer> indexList = new ArrayList<>();
        indexList.add(0);
        indexList.add(1);
        //合并的都是前几列的信息。
        int size = getVirtualTypeDayHeader().size();
        //大小
        for (int i = 0; i < size; i++) {
            indexList.add(i + 2);
        }
        int[] arr = new int[indexList.size()];
        int i = 0;
        for (Integer index : indexList) {
            arr[i++] = index;
        }
        return arr;
    }

    /**
     * 处理动态的表头
     *
     * @param
     * @return java.util.List<java.util.List < java.lang.String>>
     * @date 2021/9/17 15:01
     * @author zk_yjl
     */
    private List<List<String>> header() {
        //注意表头编写时，应该用两行。
        List<List<String>> result = new ArrayList<>();
        //填充固定的列
        result.add(Arrays.asList("员工工号", "员工工号"));
        result.add(Arrays.asList("姓名", "姓名"));

        //添加动态的请假类型天数
        result.addAll(getVirtualTypeDayHeader());
        //添加班次天数
        result.add(Arrays.asList("班次", "班次"));
        //添加动态的天数，是日历。  来个7月的
        result.addAll(getVirtualCalendarHeader("2021-09"));
        return result;
    }

    /**
     * 获取虚拟的天数头
     *
     * @param yearMonth
     * @return java.util.List<java.util.List < java.lang.String>>
     * @date 2021/9/17 17:42
     * @author zk_yjl
     */
    private List<List<String>> getVirtualCalendarHeader(String yearMonth) {
        List<List<String>> result = new ArrayList<>();
        //获取年,月
        String[] yearAndMonthArr = yearMonth.split("-");
        //获取当月的总的天数
        int totalDate = DataUtil.getDaysByYearMonth(Integer.parseInt(yearAndMonthArr[0]), Integer.parseInt(yearAndMonthArr[1]));
        //i 从1开始，表示第1天。
        for (int i = 1; i <= totalDate; i++) {
            List<String> dayList = new ArrayList<>();
            dayList.add("D" + (i));
            //获取星期几  建议更换其他的实现方式。
            dayList.add(DataUtil.getWeekByYearMonth(Integer.parseInt(yearAndMonthArr[0]), Integer.parseInt(yearAndMonthArr[1]), i));
            result.add(dayList);
        }
        return result;
    }

    /**
     * 模拟 请假的类型的表头信息
     *
     * @param
     * @return java.util.Collection<? extends java.util.List < java.lang.String>>
     * @date 2021/9/17 15:06
     * @author zk_yjl
     */
    private List<List<String>> getVirtualTypeDayHeader() {
        List<List<String>> result = new ArrayList<>();
        result.add(Arrays.asList("工伤假天数", "工伤假天数"));
        result.add(Arrays.asList("病假天数", "病假天数"));
        result.add(Arrays.asList("产假天数", "产假天数"));
        result.add(Arrays.asList("产检假天数", "产检假天数"));
        return result;
    }
}
