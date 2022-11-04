package top.yueshushu.easyexcel.handler;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.log4j.Log4j2;
import top.yueshushu.easyexcel.readpojo.ConvertData;

/**
 * @ClassName:ConvertReadHandler
 * @Description 读数据转换
 * @Author yjl
 * @Date 2021/6/7 19:15
 * @Version 1.0
 **/
@Log4j2
public class ConvertReadHandler extends AnalysisEventListener<ConvertData> {

    @Override
    public void invoke(ConvertData data, AnalysisContext context) {
        log.info("解析数据>>>>>>>>>" + data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("解析完成");
    }
}
