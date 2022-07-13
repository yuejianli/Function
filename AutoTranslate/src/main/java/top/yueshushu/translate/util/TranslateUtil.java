package top.yueshushu.translate.util;


import com.alibaba.fastjson.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.log4j.Log4j2;
import top.yueshushu.translate.model.translatevo.TransResult;
import top.yueshushu.translate.model.translatevo.TranslateDto;
import top.yueshushu.translate.model.translatevo.TranslateVo;
import top.yueshushu.translate.model.translatevo.YdTranslateVo;
import top.yueshushu.translate.util.bd.BdTransApiUtil;
import top.yueshushu.translate.util.yd.YdTransApiUtil;

/**
 * @ClassName:TranslateUtil
 * @Description 翻译使用的Util
 * @Author zk_yjl
 * @Date 2021/9/18 17:43
 * @Version 1.0
 * @Since 1.0
 **/
@Component
@Log4j2
public class TranslateUtil {
    public static Map<String, String> langMap = null;
    
    static {
        langMap = new HashMap<>();
        langMap.put("cn", "zh");
        langMap.put("tw", "cht");
    }
    
    @Autowired
    private YdTransApiUtil ydTransApiUtil;
    @Autowired
    private BdTransApiUtil bdTransApiUtil;
    
    public String getTranslate(String query, String from, String to) {
        TranslateDto translateDto = new TranslateDto();
        translateDto.setQ(query);
        translateDto.setFrom(langConvert(from));
        translateDto.setTo(langConvert(to));
        TranslateVo content = ydTranslate(translateDto);
        if (content != null && !CollectionUtils.isEmpty(content.getTransResult())) {
            return content.getTransResult().get(0).getDst();
        }
        content = bdTranslate(translateDto);
        if (content != null && !CollectionUtils.isEmpty(content.getTransResult())) {
            return content.getTransResult().get(0).getDst();
        }
        return "未翻译成功";
        
    }
    
    private TranslateVo bdTranslate(TranslateDto translateDto) {
        
        try {
            String requestRestult = bdTransApiUtil.getTranslate(translateDto.getQ(), translateDto.getFrom(), translateDto.getTo());
            if (StringUtils.isEmpty(requestRestult)) {
                return null;
            }
            TranslateVo translateVo = JSONObject.parseObject(requestRestult, TranslateVo.class);
            return translateVo;
        } catch (Exception e) {
            log.error("有异常信息{}", e);
        }
        return null;
    }
    
    private TranslateVo ydTranslate(TranslateDto translateDto) {
        
        try {
            String requestRestult = ydTransApiUtil.getTranslate(translateDto.getQ(), translateDto.getFrom(), translateDto.getTo());
            if (StringUtils.isEmpty(requestRestult)) {
                return null;
            }
            YdTranslateVo ydTranslateVo = JSONObject.parseObject(requestRestult, YdTranslateVo.class);
            if (ydTranslateVo == null) {
                return null;
            }
            return YdConvertTranslate(translateDto, ydTranslateVo);
        } catch (Exception e) {
            log.error("有异常信息{}", e);
        }
        return null;
    }
    
    /**
     * 进行转换
     *
     * @param ydTranslateVo
     * @return com.zk.model.vo.TranslateVo
     * @date 2021/9/15 14:22
     * @author zk_yjl  进行转换
     */
    private TranslateVo YdConvertTranslate(TranslateDto translateDto, YdTranslateVo ydTranslateVo) {
        TranslateVo translateVo = new TranslateVo();
        translateVo.setFrom(translateDto.getFrom());
        translateVo.setTo(translateDto.getTo());
        List<TransResult> transResultList = new ArrayList<>();
        for (String translation : ydTranslateVo.getTranslation()) {
            TransResult transResult = new TransResult();
            transResult.setSrc(translateDto.getQ());
            transResult.setDst(translation);
            transResultList.add(transResult);
        }
        translateVo.setTransResult(transResultList);
        return translateVo;
    }
    
    /**
     * 进行语种的对应。 本地的与网络翻译语言进行对应
     *
     * @param lang
     * @return java.lang.String
     * @date 2021/9/22 13:47
     * @author zk_yjl
     */
    private String langConvert(String lang) {
        return langMap.getOrDefault(lang, lang);
    }
}
