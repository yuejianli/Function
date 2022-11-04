package top.yueshushu.easyexcel.handler;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.log4j.Log4j2;
import top.yueshushu.easyexcel.readpojo.PersonFormula;

/**
 * @ClassName:PersonFormula
 * @Description TODO
 * @Author yjl
 * @Date 2021/6/8 9:08
 * @Version 1.0
 **/
@Log4j2
public class PersonFormulaHandler extends AnalysisEventListener<PersonFormula> {
    @Override
    public void invoke(PersonFormula data, AnalysisContext context) {
        log.info(">>>>>>>>>>>解析数据,{}", data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("解析数据完成");
    }
}
