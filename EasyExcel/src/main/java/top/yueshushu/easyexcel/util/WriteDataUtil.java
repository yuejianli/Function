package top.yueshushu.easyexcel.util;

import top.yueshushu.easyexcel.writepojo.ConvertWrite;
import top.yueshushu.easyexcel.writepojo.MergePerson;
import top.yueshushu.easyexcel.writepojo.WritePerson;
import top.yueshushu.easyexcel.writepojo.WritePersonFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName:WriteUtil
 * @Description TODO
 * @Author yjl
 * @Date 2021/6/8 9:17
 * @Version 1.0
 **/
public class WriteDataUtil {

    public static final String FILE_PATH = "D:\\githubBoot\\Function\\EasyExcel\\src\\main\\resources\\";

    public static List<WritePerson> getData() {
        List<WritePerson> writePersonList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            WritePerson writePerson = new WritePerson();
            writePerson.setId(i);
            writePerson.setName("两个蝴蝶飞第" + i);
            writePerson.setAge(10 * i);
            writePerson.setBirthday(new Date());
            writePerson.setScore(10 * i + 0.5);
            writePerson.setOther(writePerson.getId() + "【" + writePerson.getName() + "】");
            writePersonList.add(writePerson);
        }
        return writePersonList;
    }

    public static List<ConvertWrite> getConvertData() {
        List<ConvertWrite> writePersonList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            ConvertWrite writePerson = new ConvertWrite();
            writePerson.setId(i);
            writePerson.setName("两个蝴蝶飞第" + i);
            writePerson.setAge(10 * i);
            writePerson.setBirthday(new Date());
            writePerson.setScore(0.1 * i + 0.5);
            writePerson.setOther(writePerson.getId() + "【" + writePerson.getName() + "】");
            writePersonList.add(writePerson);
        }
        return writePersonList;
    }

    public static List<WritePersonFormat> getFormatData() {
        List<WritePersonFormat> writePersonList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            WritePersonFormat writePerson = new WritePersonFormat();
            writePerson.setId(i);
            writePerson.setName("两个蝴蝶飞第" + i);
            writePerson.setAge(10 * i);
            writePerson.setBirthday(new Date());
            writePerson.setScore(0.1 * i + 0.5);
            writePerson.setOther(writePerson.getId() + "【" + writePerson.getName() + "】");
            writePersonList.add(writePerson);
        }
        return writePersonList;
    }

    public static List<MergePerson> getMergeData() {
        List<MergePerson> writePersonList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            MergePerson writePerson = new MergePerson();
            writePerson.setId(i);
            writePerson.setName("两个蝴蝶飞第" + i);
            writePerson.setAge(10 * i);
            writePerson.setBirthday(new Date());
            writePerson.setScore(0.1 * i + 0.5);
            writePerson.setOther(writePerson.getId() + "【" + writePerson.getName() + "】");
            writePersonList.add(writePerson);
        }
        return writePersonList;
    }
}
