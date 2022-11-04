package top.yueshushu.easyexcel.attendance.util;

import org.apache.commons.beanutils.BeanUtils;
import top.yueshushu.easyexcel.attendance.model.AttendanceUserVo;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @ClassName:DataUtil
 * @Description TODO
 * @Author zk_yjl
 * @Date 2021/9/17 14:25
 * @Version 1.0
 * @Since 1.0
 **/
public class DataUtil {
    /**
     * 获取虚拟的数据。 正常从数据库中进行获取，然后处理。
     *
     * @param
     * @return java.util.List<com.zk.easyexcel.attendance.model.AttendanceUserVo>
     * @date 2021/9/17 14:26
     * @author 两个蝴蝶飞
     */
    public static List<AttendanceUserVo> getVirtualDataList() {
        List<AttendanceUserVo> dbList = new ArrayList<>();
        //目前模拟 一个员工有四份数据。 上班，下班，时长，流程。  模拟5个员工进行处理。
        List<String> jobNumList = getJobNumerList();
        List<String> nameList = getNameList();
        List<String> shiftList = getShiftList();
        for (int i = 0; i < 5; i++) {
            String jobNum = jobNumList.get(i);
            String name = nameList.get(i);
            for (int j = 0; j < 4; j++) {
                String shift = shiftList.get(j);
                //数据均是假的数据。
                AttendanceUserVo attendanceUserVo = new AttendanceUserVo();
                attendanceUserVo.setId(i);
                attendanceUserVo.setJobNum(jobNum);
                attendanceUserVo.setName(name);

                //模拟获取请假类型的天数。获取的，应该是动态的。
                virtualTypeDay(attendanceUserVo, i + 1);
                attendanceUserVo.setShift(shift);
                //处理日历天数， 获取的，应该是动态的。
                virtualCalendarDay(attendanceUserVo, i, "2021-09");
                dbList.add(attendanceUserVo);
            }
        }
        return dbList;
    }

    /**
     * TODO 需要重新实现的地方。 根据年，月，进行处理
     * 模拟日历时间的信息。 处理成不同的时间即可。 需要根据年，月，进行处理。
     *
     * @param attendanceUserVo
     * @return void
     * @date 2021/9/17 14:50
     * @author zk_yjl
     */
    private static void virtualCalendarDay(AttendanceUserVo attendanceUserVo, int flag, String yearMonth) {
        //获取年,月
        String[] yearAndMonthArr = yearMonth.split("-");
        //获取当月的总的天数
        int totalDate = DataUtil.getDaysByYearMonth(Integer.parseInt(yearAndMonthArr[0]), Integer.parseInt(yearAndMonthArr[1]));
        //i 从1开始，表示第1天。
        for (int i = 1; i <= totalDate; i++) {
            //设置属性
            String property = "day" + i;
            //设置值
            String value = "天数" + i + "的值是:" + flag;
            try {
                BeanUtils.copyProperty(attendanceUserVo, property, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 模拟请假类型的天数
     *
     * @param attendanceUserVo
     * @return void
     * @date 2021/9/17 14:40
     * @author zk_yjl
     */
    private static void virtualTypeDay(AttendanceUserVo attendanceUserVo, int i) {
        attendanceUserVo.setInductrialDay(0 + i);
        attendanceUserVo.setDiseaseDay(1 + i);
        attendanceUserVo.setMaternityDay(2 + i);
        attendanceUserVo.setPrenatalDay(3 + i);
    }

    /**
     * 获取类型
     *
     * @param
     * @return java.util.List<java.lang.String>
     * @date 2021/9/17 14:37
     * @author zk_yjl
     */
    private static List<String> getShiftList() {
        List<String> result = new ArrayList<>();
        result.add("上班");
        result.add("下班");
        result.add("时长");
        result.add("流程");
        return result;
    }

    /**
     * 模拟员工的名称
     *
     * @param
     * @return java.util.List<java.lang.String>
     * @date 2021/9/17 14:35
     * @author zk_yjl
     */
    private static List<String> getNameList() {
        List<String> result = new ArrayList<>();
        result.add("鲁班");
        result.add("后羿");
        result.add("吕布");
        result.add("王昭君");
        result.add("安琪拉");
        return result;
    }

    /**
     * 模拟员工的工号
     *
     * @param
     * @return java.util.List<java.lang.String>
     * @date 2021/9/17 14:33
     * @author zk_yjl
     */
    private static List<String> getJobNumerList() {
        List<String> result = new ArrayList<>();
        result.add("U001");
        result.add("U002");
        result.add("U003");
        result.add("U004");
        result.add("U005");
        return result;
    }

    /**
     * TODO
     *
     * @param null
     * @return
     * @date 2021/9/17 17:39
     * @author zk_yjl
     */
    public static int getDaysByYearMonth(int year, int month) {

        Calendar a = Calendar.getInstance();

        a.set(Calendar.YEAR, year);

        a.set(Calendar.MONTH, month - 1);

        a.set(Calendar.DATE, 1);

        a.roll(Calendar.DATE, -1);

        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 获取星期几
     *
     * @param year
     * @param month
     * @param day
     * @return java.lang.String
     * @date 2021/9/17 17:46
     * @author zk_yjl
     */
    public static String getWeekByYearMonth(int year, int month, int day) {
        String dateString = year + "-" + month + "-" + day;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date = simpleDateFormat.parse(dateString);
            SimpleDateFormat weekSimpleDateFormat = new SimpleDateFormat("EEEE");
            return weekSimpleDateFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
