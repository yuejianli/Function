package top.yueshushu.easyexcel.handler;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.util.ConverterUtils;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.log4j.Log4j2;
import top.yueshushu.easyexcel.readpojo.Person;
import top.yueshushu.easyexcel.service.PersonService;

import java.util.List;
import java.util.Map;

/**
 * @ClassName:SimpleReadHandler
 * @Description 简单的读处理器
 * @Author yjl
 * @Date 2021/6/7 17:39
 * @Version 1.0
 **/
@Log4j2
public class SimpleReadWithHeadHandler extends AnalysisEventListener<Person> {
    private static final Integer BATCH_READ = 5;
    //批量保存的数据集合。
    private List<Person> batchSaveDataList = ListUtils.newArrayListWithExpectedSize(BATCH_READ);

    //需要引入 PersonService 对象.
    private PersonService personService;


    //如果与Spring 进行整合的话，构建方法传参的形式引入。
    public SimpleReadWithHeadHandler(PersonService personService) {
        this.personService = personService;
    }

    /**
     * 对解析的每一条数据，进行处理。 data 时，已经处理完成了。
     *
     * @param data
     * @param context
     */
    @Override
    public void invoke(Person data, AnalysisContext context) {
        log.info("解析读取数据>>>>" + data);
        batchSaveDataList.add(data);
        if (batchSaveDataList.size() >= BATCH_READ) {
            personService.saveData(batchSaveDataList);
            // 存储完成清理 list
            batchSaveDataList = ListUtils.newArrayListWithExpectedSize(BATCH_READ);
        }
    }

    /**
     * 解析完成后的数据。
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info(">>>>>>>>>解析数据完成");
        //再次保存一下，避免后面不大于5时，依旧能保存信息。
        personService.saveData(batchSaveDataList);
        batchSaveDataList = ListUtils.newArrayListWithExpectedSize(BATCH_READ);
    }

    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        //    log.info("invokeHead,解析到一条头数据:{}", JSON.toJSONString(headMap));
        // 如果想转成成 Map<Integer,String>
        // 方案1： 不要implements ReadListener 而是 extends AnalysisEventListener
        // 方案2： 调用 ConverterUtils.convertToStringMap(headMap, context) 自动会转换

        Map<Integer, String> integerStringMap = ConverterUtils.convertToStringMap(headMap, context);
        log.info("invokeHead,解析到一条头数据:{}", JSON.toJSONString(integerStringMap));
    }
}
