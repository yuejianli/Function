package top.yueshushu.easyexcel.handler;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.util.ListUtils;
import lombok.extern.log4j.Log4j2;
import top.yueshushu.easyexcel.readpojo.PersonException;
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
public class PersonExceptionHandler extends AnalysisEventListener<PersonException> {
    private static final Integer BATCH_READ = 5;
    //批量保存的数据集合。
    private List<PersonException> batchSaveDataList = ListUtils.newArrayListWithExpectedSize(BATCH_READ);

    //需要引入 PersonExceptionService 对象.
    private PersonService personService;


    //如果与Spring 进行整合的话，构建方法传参的形式引入。
    public PersonExceptionHandler(PersonService personService) {
        this.personService = personService;
    }

    /**
     * 对解析的每一条数据，进行处理。 data 时，已经处理完成了。
     *
     * @param data
     * @param context
     */
    @Override
    public void invoke(PersonException data, AnalysisContext context) {
        log.info("解析读取数据>>>>" + data);
        batchSaveDataList.add(data);
        if (batchSaveDataList.size() >= BATCH_READ) {
            personService.saveDataException(batchSaveDataList);
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
        personService.saveDataException(batchSaveDataList);
        batchSaveDataList = ListUtils.newArrayListWithExpectedSize(BATCH_READ);
    }


    /**
     * 发生异常的时候，进行处理。
     *
     * @param exception
     * @param context
     * @throws Exception
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        log.error("解析失败{}", exception.getMessage());
        //可以具体到某一行列的数据.
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;
            log.error("解析数据时失败，第{}行，第{}列解析异常,错误值是:{}",
                    excelDataConvertException.getRowIndex(),
                    excelDataConvertException.getColumnIndex(),
                    excelDataConvertException.getCellData().getStringValue());
        }
        //  super.onException(exception, context);  //去掉，放置异常的信息。
    }
}
