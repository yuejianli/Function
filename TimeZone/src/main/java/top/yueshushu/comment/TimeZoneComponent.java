package top.yueshushu.comment;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import top.yueshushu.aop.TimeZoneAspect;
import top.yueshushu.entity.User;
import top.yueshushu.mapper.UserMapper;

import javax.annotation.Resource;
import java.util.List;
import java.util.TimeZone;

/**
 * 时区组件
 *
 * @author wanyanhw
 * @date 2022/4/2 17:49
 */
@Component
@Slf4j
public class TimeZoneComponent {

    // 可以注入一些服务
    @Resource
    private UserMapper userMapper;

    /**
     * 获取商家时区，如果获取不到，使用默认时区
     *
     * @param merchantId      商家ID
     * @param defaultTimeZone 默认时区
     * @return 商家时区
     */
    public String getCacheMerchantTimeZone(Long merchantId, String defaultTimeZone) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getMerchantId, merchantId);
        List<User> userList = userMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(userList)) {
            return userList.get(0).getCountry();
        }
        return defaultTimeZone;
    }

    /**
     * 转换成商家时区
     *
     * @param toHandleObj 待处理对象
     * @param merchantId  商家ID
     */
    public void trans2MerchantTimeZone(Object toHandleObj, Long merchantId) {
        // 系统时区
        String sysTimeZone = TimeZone.getDefault().toZoneId().getRules().toString().split("=")[1].split(":")[0];
        // 商家时区
        String merchantTimeZone = getCacheMerchantTimeZone(merchantId, sysTimeZone);
        TimeZoneAspect.resetResponseTimeField(toHandleObj, sysTimeZone, merchantTimeZone);
    }

}
