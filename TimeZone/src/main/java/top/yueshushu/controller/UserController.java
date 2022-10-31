package top.yueshushu.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.yueshushu.annotation.TimeZoneSwitch;
import top.yueshushu.aop.AuthToken;
import top.yueshushu.common.Global;
import top.yueshushu.entity.TimeSwitchScope;
import top.yueshushu.entity.User;
import top.yueshushu.model.UserRo;
import top.yueshushu.model.UserVo;
import top.yueshushu.service.UserService;
import top.yueshushu.utils.DateUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Yue Jianli
 * @date 2022-05-18
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @TimeZoneSwitch
    @PostMapping("/update")
    @AuthToken
    public User addUser(@RequestBody UserRo userRo) {
        log.info("员工信息:{}", userRo);
        User user = new User();
        BeanUtils.copyProperties(userRo, user);

        user.setId(Global.getUser().getId());

        // 更新
        user.setUpdateTime(DateUtil.date());

        userService.updateById(user);
        return userService.getById(user.getId());
    }

    @RequestMapping("/listAll")
    @TimeZoneSwitch
    @AuthToken
    public List<UserVo> listAll() {
        List<User> list = userService.list();

        List<UserVo> result = new ArrayList<>();
        list.stream().forEach(
                n -> {
                    UserVo userVo = new UserVo();
                    BeanUtils.copyProperties(n, userVo);
                    //处理时间
                    userVo.setCreateTimeNum(n.getCreateTime().getTime());
                    userVo.setCreateTime(DateUtil.format(n.getCreateTime(), DateUtils.FORMAT));
                    userVo.setUpdateTimeNum(n.getUpdateTime().getTime());
                    userVo.setUpdateTime(DateUtil.format(n.getUpdateTime(), DateUtils.FORMAT));
                    result.add(userVo);
                }
        );
        result.forEach(
                n -> log.info("展示信息:{}", n)
        );
        return result;
    }


    @RequestMapping("/query")
    @TimeZoneSwitch
    @AuthToken
    public List<UserVo> query(@RequestBody UserRo userRo) {

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        //处理日期
        if (userRo.getTimeRange() != null) {
            Date startDate = DateUtil.parse(userRo.getTimeRange().get(0));
            Date endDate = DateUtil.parse(userRo.getTimeRange().get(1));
            queryWrapper.and(q1 -> q1.gt(
                    User::getUpdateTime, startDate
            )).and(
                    q1 -> q1.lt(
                            User::getUpdateTime, endDate
                    )
            );
        }

        List<User> list = userService.list(queryWrapper);

        List<UserVo> result = new ArrayList<>();
        list.stream().forEach(
                n -> {
                    UserVo userVo = new UserVo();
                    BeanUtils.copyProperties(n, userVo);
                    //处理时间
                    userVo.setCreateTimeNum(n.getCreateTime().getTime());
                    userVo.setCreateTime(DateUtil.format(n.getCreateTime(), DateUtils.FORMAT));
                    userVo.setUpdateTimeNum(n.getUpdateTime().getTime());
                    userVo.setUpdateTime(DateUtil.format(n.getUpdateTime(), DateUtils.FORMAT));
                    result.add(userVo);
                }
        );
        result.forEach(
                n -> log.info("展示信息:{}", n)
        );
        return result;
    }
}
