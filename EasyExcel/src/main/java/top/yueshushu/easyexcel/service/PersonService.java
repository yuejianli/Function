package top.yueshushu.easyexcel.service;

import top.yueshushu.easyexcel.readpojo.Person;
import top.yueshushu.easyexcel.readpojo.PersonException;
import top.yueshushu.easyexcel.readpojo.PersonIndex;

import java.util.List;

/**
 * TODO 用途描述
 *
 * @author Yue Jianli
 * @date 2022-11-03
 */

public interface PersonService {
    void saveData(List<Person> personList);

    void saveDataIndex(List<PersonIndex> batchSaveDataList);

    void saveDataException(List<PersonException> batchSaveDataList);
}
