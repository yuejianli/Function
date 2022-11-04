package top.yueshushu.easyexcel.handler;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.log4j.Log4j2;

import java.util.Map;

/**
 * @ClassName:SimpleReadHandler
 * @Description TODO
 * @Author yjl
 * @Date 2021/6/7 17:39
 * @Version 1.0
 **/
@Log4j2
public class NoObjectReadHandler extends AnalysisEventListener<Map<Integer, String>> {

    // 可以进行批量设置信息的读取

    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
        log.info("解析数据,{}", data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("解析完成");
    }
}
