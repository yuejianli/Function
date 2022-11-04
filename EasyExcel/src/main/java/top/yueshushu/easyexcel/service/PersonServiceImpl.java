package top.yueshushu.easyexcel.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.yueshushu.easyexcel.readpojo.Person;
import top.yueshushu.easyexcel.readpojo.PersonException;
import top.yueshushu.easyexcel.readpojo.PersonIndex;

import java.util.List;

/**
 * 服务处理
 *
 * @author yuejianli
 * @date 2022-11-03
 */
@Service
@Slf4j
public class PersonServiceImpl implements PersonService {

    @Override
    public void saveData(List<Person> personList) {
        //进行批量保存数据库信息.
        if (CollectionUtils.isEmpty(personList)) {
            return;
        }
        // 省略真实的 持久化到数据库的操作
        log.info("DB 层面,批量保存数据:" + personList.size());
    }

    @Override
    public void saveDataIndex(List<PersonIndex> batchSaveDataList) {
        //进行批量保存数据库信息.
        if (CollectionUtils.isEmpty(batchSaveDataList)) {
            return;
        }
        // 省略真实的 持久化到数据库的操作
        log.info("DB 层面,批量保存数据:" + batchSaveDataList.size());
    }

    @Override
    public void saveDataException(List<PersonException> batchSaveDataList) {
        //进行批量保存数据库信息.
        if (CollectionUtils.isEmpty(batchSaveDataList)) {
            return;
        }
        // 省略真实的 持久化到数据库的操作
        log.info("DB 层面,批量保存数据:" + batchSaveDataList.size());
    }
}
