package top.yueshushu.controller;

import cn.snowheart.dingtalk.robot.starter.client.DingTalkRobotClient;
import cn.snowheart.dingtalk.robot.starter.entity.DingTalkResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 简单的测试使用
 *
 * @author yuejianli
 * @date 2022-11-03
 */
@RestController
@Slf4j
public class DingTalkController {
    @Resource
    @Qualifier("dingTalkRobotClient")
    private DingTalkRobotClient client;


    @GetMapping("/sendMessage/{userId}/{message}")
    public String sendMessage(@PathVariable("userId") String userId, @PathVariable("message") String message) {

        DingTalkResponse response = client.sendTextMessage("业务报警：" + message, new String[]{userId});
        if (response.getErrcode().longValue() == 0) {
            return "发送成功";
        } else {
            return "发送失败";
        }
    }
}
