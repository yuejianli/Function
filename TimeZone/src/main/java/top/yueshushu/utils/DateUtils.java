package top.yueshushu.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.lang.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-10-31
 */

public class DateUtils {

    public final static String FORMAT = "yyyy-MM-dd HH:mm:ss";
    public final static String FORMAT_Y_M_D_H_M = "yyyy-MM-dd HH:mm";
    public final static String FORMAT_H_M = "HH:mm";
    public final static String SIMPLE_FORMAT = "yyyy-MM-dd";
    private static final Pattern DATE_PATTERN = Pattern.compile(
            "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)");


    /**
     * 时区转换
     *
     * @param time           时间字符串
     * @param pattern        格式 "yyyy-MM-dd HH:mm"
     * @param nowTimeZone    eg:+8，0，+9，-1 等等
     * @param targetTimeZone 同nowTimeZone
     * @return
     */
    public static String timeZoneTransfer(String time, String pattern, String nowTimeZone, String targetTimeZone) {
        if (StringUtils.isBlank(time)) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT" + nowTimeZone));
        Date date;
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {

            return "";
        }
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT" + targetTimeZone));
        return simpleDateFormat.format(date);
    }

    /**
     * 转换 String 类型时间
     * 目前仅支持 yyyy-MM-dd HH:mm:ss 和 yyyy-MM-dd HH:mm 格式时间
     *
     * @param time        时间
     * @param srcTimeZone 源时区
     * @param desTimeZone 目标时区
     * @return String
     * @throws Exception e
     */
    public static String transformString(String time, String srcTimeZone, String desTimeZone) throws Exception {
        String zoneTransfer = timeZoneTransfer(time, DateUtils.FORMAT, srcTimeZone, desTimeZone);
        if (zoneTransfer.isEmpty()) {
            zoneTransfer = timeZoneTransfer(time, DateUtils.FORMAT_Y_M_D_H_M, srcTimeZone, desTimeZone);
        }
        if (zoneTransfer.isEmpty()) {
            zoneTransfer = timeZoneTransfer(time, DateUtils.FORMAT_H_M, srcTimeZone, desTimeZone);
        }
        if (zoneTransfer.isEmpty()) {
            throw new Exception();
        }
        return zoneTransfer;
    }

    /**
     * 转换 Date 类型时间
     *
     * @param time        时间
     * @param srcTimeZone 源时区
     * @param desTimeZone 目标时区
     * @return Date
     * @throws Exception e
     */
    public static Date transformDate(Date time, String srcTimeZone, String desTimeZone) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtils.FORMAT);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT" + srcTimeZone));
        String formatTime = simpleDateFormat.format(time);
        String transferredTime = timeZoneTransfer(formatTime, DateUtils.FORMAT, srcTimeZone, desTimeZone);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT" + desTimeZone));
        return simpleDateFormat.parse(transferredTime);
    }

    /**
     * 转换 LocalDateTime 类型时间
     *
     * @param time        时间
     * @param srcTimeZone 源时区
     * @param desTimeZone 目标时区
     * @return
     */
    public static LocalDateTime transformLocalDateTime(LocalDateTime time, String srcTimeZone, String desTimeZone) {
        String formatTime = time.format(DateTimeFormatter.ofPattern(DateUtils.FORMAT));
        String transferredTime = timeZoneTransfer(formatTime, DateUtils.FORMAT, srcTimeZone, desTimeZone);
        LocalDateTime parse = LocalDateTime.parse(transferredTime, DateTimeFormatter.ofPattern(DateUtils.FORMAT));
        Instant instant = parse.toInstant(ZoneOffset.of(desTimeZone));
        return LocalDateTime.ofInstant(instant, ZoneId.of("GMT" + desTimeZone));
    }

    /**
     * 转换成时区对应的日期字符串
     *
     * @param date     日期
     * @param timeZone 目标时区
     * @return String
     */
    public static String formatTimeZone(Date date, TimeZone timeZone) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT);
        simpleDateFormat.setTimeZone(timeZone);
        return simpleDateFormat.format(date);
    }

    /**
     * 给指定日期设置 年/月/日/时/分/秒
     *
     * @param date 指定日期
     * @param y    年
     * @param M    月
     * @param d    日
     * @param h    时
     * @param m    分
     * @param s    秒
     * @return Date
     */
    public static Date setDateTime(Date date, Integer y, Integer M, Integer d, Integer h, Integer m, Integer s) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (y != null) {
            calendar.set(Calendar.YEAR, y);
        }
        if (M != null) {
            calendar.set(Calendar.MONTH, M);
        }
        if (d != null) {
            calendar.set(Calendar.DAY_OF_MONTH, d);
        }
        if (h != null) {
            calendar.set(Calendar.HOUR_OF_DAY, h);
        }
        if (m != null) {
            calendar.set(Calendar.MINUTE, m);
        }
        if (s != null) {
            calendar.set(Calendar.SECOND, s);
        }
        return calendar.getTime();
    }

    /**
     * 给指定日期设置增加 年/月/日/时/分/秒
     *
     * @param date  指定日期
     * @param y     年
     * @param month 月
     * @param d     日
     * @param h     时
     * @param m     分
     * @param s     秒
     * @return Date
     */
    public static Date addDateTime(Date date, Integer y, Integer month, Integer d, Integer h, Integer m, Integer s) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (y != null) {
            calendar.add(Calendar.YEAR, y);
        }
        if (month != null) {
            calendar.add(Calendar.MONTH, month);
        }
        if (d != null) {
            calendar.add(Calendar.DAY_OF_MONTH, d);
        }
        if (h != null) {
            calendar.add(Calendar.HOUR_OF_DAY, h);
        }
        if (m != null) {
            calendar.add(Calendar.MINUTE, m);
        }
        if (s != null) {
            calendar.add(Calendar.SECOND, s);
        }
        return calendar.getTime();
    }

    /**
     * 获取日期指定的属性值
     *
     * @param date     指定日期
     * @param field    属性，e.g. {Calendar.YEAR}
     * @param timeZone 时区
     * @return int
     */
    public static int getDateFieldValue(Date date, int field, @Nullable String timeZone) {
        Calendar calendar = Calendar.getInstance();
        if (StringUtils.isNotEmpty(timeZone)) {
            calendar.setTimeZone(TimeZone.getTimeZone("GMT" + timeZone));
        }
        calendar.setTime(date);
        return calendar.get(field);
    }

    /**
     * 获取时间在时区转换后的跨天情况，0-无，-1-左跨一天，1-右跨一天
     *
     * @param h           时
     * @param m           分
     * @param s           秒
     * @param srcTimeZone 源时区
     * @param desTimeZone 目标时区
     * @return int 0:同一天, -1:日期提前一天, 1:日期延后一天
     * @throws Exception e
     */
    public static int diffDate(Integer h, Integer m, Integer s, String srcTimeZone, String desTimeZone) throws Exception {
        Date originalDate = setDateTime(new Date(), null, null, null, h, m, s);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtils.FORMAT);
        String formatTime = simpleDateFormat.format(originalDate);
        String transformDate = timeZoneTransfer(formatTime, DateUtils.FORMAT, srcTimeZone, desTimeZone);
        int days = daysBetween(originalDate, simpleDateFormat.parse(transformDate));
        return Integer.compare(days, 0);
    }

    /**
     * 计算两个日期的天数差
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int daysBetween(Date startDate, Date endDate) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(startDate);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(endDate);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2) {
            // 不同年
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
                    // 闰年
                    timeDistance += 366;
                } else {
                    // 不是闰年
                    timeDistance += 365;
                }
            }
            return timeDistance + (day2 - day1);
        } else {
            // 同一年
            return day2 - day1;
        }
    }
}
