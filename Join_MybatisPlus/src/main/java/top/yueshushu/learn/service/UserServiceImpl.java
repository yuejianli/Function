package top.yueshushu.learn.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.yueshushu.learn.dynamic.ClassFieldHelper;
import top.yueshushu.learn.dynamic.DbField;
import top.yueshushu.learn.dynamic.SerializableFunctionUtil;
import top.yueshushu.learn.mapper.UserMapper;
import top.yueshushu.learn.pojo.Role;
import top.yueshushu.learn.pojo.User;
import top.yueshushu.learn.pojo.UserDo;
import top.yueshushu.learn.vo.UserReqVo;
import top.yueshushu.learn.vo.UserResVo;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/***
 * @author yuejianli
 * @date 2022-09-05
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private ClassFieldHelper classFieldHelper;
    @Resource
    private UserMapper userMapper;

    private List<DbField> generateDbFieldList(List<String> returnFields) {
        //默认返回全部字段
        if (CollUtil.isEmpty(returnFields)) {
            return getAllDbFieldList();
        }
        return classFieldHelper.getDbFieldByVoFunctionKey(
                ListUtil.toList(User.class, Role.class),
                returnFields);
    }

    private List<DbField> getAllDbFieldList() {
        String ignoreId = SerializableFunctionUtil.getSignatureKey(Role::getId);
        List<DbField> allDbField = classFieldHelper.getAllDbField(CollUtil.newHashSet(ignoreId), User.class, Role.class);
        return allDbField;
    }


    @Override
    public List<UserResVo> findListByCondition(UserReqVo userReqVo) {
        List<DbField> ReturnDbFields = generateDbFieldList(userReqVo.getReturnFields());
        List<UserDo> userDoList = userMapper.findList(ReturnDbFields, userReqVo);

        if (CollectionUtils.isEmpty(userDoList)) {
            return Collections.emptyList();
        }
        return userDoList.stream().map(
                n -> {
                    UserResVo userResVo = new UserResVo();
                    //复制时，忽略空值
                    BeanUtil.copyProperties(n, userResVo, CopyOptions.create().ignoreNullValue());
                    return userResVo;
                }
        ).collect(Collectors.toList());
    }
}
