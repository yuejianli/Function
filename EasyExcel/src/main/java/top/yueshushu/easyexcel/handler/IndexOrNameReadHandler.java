package top.yueshushu.easyexcel.handler;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.ListUtils;
import lombok.extern.log4j.Log4j2;
import top.yueshushu.easyexcel.readpojo.PersonIndex;
import top.yueshushu.easyexcel.service.PersonService;

import java.util.List;

/**
 * @ClassName:SimpleReadHandler
 * @Description 简单的读处理器
 * @Author yjl
 * @Date 2021/6/7 17:39
 * @Version 1.0
 **/
@Log4j2
public class IndexOrNameReadHandler extends AnalysisEventListener<PersonIndex> {
    private static final Integer BATCH_READ = 5;
    //批量保存的数据集合。
    private List<PersonIndex> batchSaveDataList = ListUtils.newArrayListWithExpectedSize(BATCH_READ);

    //需要引入 PersonService 对象.
    private PersonService personService;


    //如果与Spring 进行整合的话，构建方法传参的形式引入。
    public IndexOrNameReadHandler(PersonService personService) {
        this.personService = personService;
    }

    /**
     * 对解析的每一条数据，进行处理。 data 时，已经处理完成了。
     *
     * @param data
     * @param context
     */
    @Override
    public void invoke(PersonIndex data, AnalysisContext context) {
        log.info("解析读取数据>>>>" + data);
        batchSaveDataList.add(data);
        if (batchSaveDataList.size() >= BATCH_READ) {
            personService.saveDataIndex(batchSaveDataList);
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
        personService.saveDataIndex(batchSaveDataList);
        batchSaveDataList = ListUtils.newArrayListWithExpectedSize(BATCH_READ);
    }
}
