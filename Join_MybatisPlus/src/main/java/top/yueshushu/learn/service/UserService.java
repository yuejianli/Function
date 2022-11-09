package top.yueshushu.learn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.yueshushu.learn.pojo.User;
import top.yueshushu.learn.vo.UserReqVo;
import top.yueshushu.learn.vo.UserResVo;

import java.util.List;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-09-05
 */

public interface UserService extends IService<User> {

    List<UserResVo> findListByCondition(UserReqVo userReqVo);

}
