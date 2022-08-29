package top.yueshushu.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import top.yueshushu.anno.Compensate;
import top.yueshushu.entity.CompensationDO;
import top.yueshushu.enumtype.RevokeStatusEnum;
import top.yueshushu.mapper.CompensationMapper;
import top.yueshushu.service.CompensationService;

/**
 * <p>
 * 服务补偿表 自定义的
 * </p>
 *
 * @author yuejianli
 * @since 2022-08-26
 */
@Service
@Slf4j
public class CompensationServiceImpl extends ServiceImpl<CompensationMapper, CompensationDO> implements CompensationService {
	
	@Override
	public boolean existIngKey(String busKey) {
		int count = this.lambdaQuery()
				.eq(CompensationDO::getBusKey, busKey)
				.eq(CompensationDO::getRevokeStatus, RevokeStatusEnum.ING.getValue())
				.count();
		return count > 0 ? true : false;
	}
	
	@Override
	public CompensationDO getByBusKey(String busKey) {
		return this.lambdaQuery()
				.eq(CompensationDO::getBusKey, busKey)
				.one();
	}
	
	@Override
	public List<CompensationDO> listIng() {
		return this.lambdaQuery()
				.eq(CompensationDO::getRevokeStatus, RevokeStatusEnum.ING.getValue())
				.list();
	}
	
	@Override
	public String autoRetry() {
		//1. 查询出目前正在执行中的任务
		List<CompensationDO> ingList = listIng();
		if (CollectionUtils.isEmpty(ingList)) {
			return null;
		}
		StringBuilder resultBuffer = new StringBuilder();
		
		resultBuffer.append("待补偿数据：").append(ingList.size()).append("条!");
		Integer successCount = 0;
		Integer failCount = 0;
		Date now = DateUtil.date();
		for (CompensationDO compensationEntity : ingList) {
			boolean runFlag = execSinge(compensationEntity, now);
			if (runFlag) {
				successCount++;
			} else {
				failCount++;
			}
		}
		//批量更新数据
		saveOrUpdateBatch(ingList);
		
		resultBuffer.append("处理成功：").append(successCount).append("条!");
		resultBuffer.append("处理失败：").append(failCount).append("条!");
		
		return resultBuffer.toString();
	}
	
	private boolean execSinge(CompensationDO compensationEntity, Date now) {
		int hasRetryCount = compensationEntity.getHasRetryCount() + 1;
		compensationEntity.setHasRetryCount(hasRetryCount);
		compensationEntity.setUpdateDate(now);
		try {
			// 调用需要重试的方法
			boolean callStatus = callTargetMethod(compensationEntity);
			// 补偿成功
			if (callStatus) {
				compensationEntity.setExceptionMessage("");
				compensationEntity.setRevokeStatus(RevokeStatusEnum.SUCCESS.getValue());
				return true;
			} else {
				if (compensationEntity.getRetryCount() == hasRetryCount) {
					compensationEntity.setRevokeStatus(RevokeStatusEnum.FAIL.getValue());
				}
				return false;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			compensationEntity.setExceptionMessage(e.getMessage());
			if (compensationEntity.getRetryCount() == hasRetryCount) {
				compensationEntity.setRevokeStatus(RevokeStatusEnum.FAIL.getValue());
			}
			return false;
		}
	}
	
	@Override
	public String manualRetry(Integer id) {
		CompensationDO compensationDO = getById(id);
		if (compensationDO == null || !compensationDO.getRevokeStatus().equals(RevokeStatusEnum.ING.getValue())) {
			return "未查询到相应的记录";
		}
		StringBuilder resultBuffer = new StringBuilder();
		boolean flag = execSinge(compensationDO, DateUtil.date());
		resultBuffer.append("执行编号" + id + ",执行结果是:" + flag);
		return resultBuffer.toString();
	}
	
	
	@Override
	@Compensate(value = Exception.class, retryCount = 5)
	public void randSingleMethod(Integer randNum) {
		int randomNum = RandomUtil.getRandom().nextInt(10);
		if (randomNum > randNum) {
			log.error(">>>> 运行失败,值是:{}", randomNum);
			throw new RuntimeException();
		} else {
			log.info(">>> 运行正常");
		}
	}
	
	@Override
	@Compensate(value = Exception.class, retryCount = 3)
	public void randNoMethod() {
		int randomNum = RandomUtil.getRandom().nextInt(10);
		if (randomNum >= 1) {
			log.error(">>>> 运行失败,值是:{}", randomNum);
			throw new RuntimeException();
		} else {
			log.info(">>> 运行正常");
		}
	}
	
	@Override
	@Compensate(value = Exception.class, retryCount = 10)
	public void randMoreMethod(Integer maxNum, Integer randNum) {
		int randomNum = RandomUtil.getRandom().nextInt(maxNum);
		if (randomNum > randNum) {
			log.error(">>>> 运行失败,值是:{}", randomNum);
			throw new RuntimeException();
		} else {
			log.info(">>> 运行正常");
		}
	}
	
	
	/**
	 * 3： 调用的核心代码
	 *
	 * @param compensationDo 补偿对象。
	 */
	private boolean callTargetMethod(CompensationDO compensationDo) throws Exception {
		// 1: 通过类找到对应的方法
		Method targetMethod = null;
		try {
			Class<?> targetClass = Class.forName(compensationDo.getClassName());
			Method[] methods = targetClass.getMethods();
			for (Method method : methods) {
				// 匹配方法
				if (matchMethod(method, compensationDo)) {
					targetMethod = method;
					break;
				}
			}
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
			//throw e;
			return false;
		}
		try {
			//补偿的目标方法中的参数类型
			// 2: 回去方法的参数类型
			Class<?>[] paramTypes = targetMethod.getParameterTypes();
			// 3: 从spring 取的对象
			Object targetObject = SpringUtil.getBean(StringUtils.uncapitalize(compensationDo.getBeanName()));
			if (ArrayUtil.isEmpty(paramTypes)) {
				targetMethod.invoke(targetObject);
			} else {
				String paramValue = compensationDo.getParamValue();
				if (paramTypes.length == 1) {
					// 转成实际类型调用目标方法
					Object runValue = JSONObject.parseObject(paramValue, Class.forName(compensationDo.getParamRealType()));
					targetMethod.invoke(targetObject, runValue);
				} else if (paramTypes.length > 1) {
					Object[] realParam = new Object[paramTypes.length];
					
					String[] splitValue = paramValue.split("\\,");
					String[] realType = compensationDo.getParamRealType().split("\\,");
					
					for (int i = 0; i < splitValue.length; i++) {
						realParam[i] = JSONObject.parseObject(splitValue[i], Class.forName(realType[i]));
					}
					targetMethod.invoke(targetObject, realParam);
				} else {
					log.error("不支持的参数类型调用");
				}
			}
			return true;
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			//throw e;
			return false;
		}
	}
	
	/**
	 * 3-1： 寻找类中对应的方法。考虑重载
	 *
	 * @param method         方法method
	 * @param compensationDo 补偿的类
	 * @return 是否匹配上
	 */
	private Boolean matchMethod(Method method, CompensationDO compensationDo) {
		//  1：先判断方法名，如果类名不匹配，直接返回false
		if (!method.getName().equals(compensationDo.getMethodName())) {
			return false;
		}
		// 2: 方法名匹配成功后，匹配参数
		Class<?>[] args = method.getParameterTypes();
		if (ArrayUtil.isEmpty(args) && StringUtils.isEmpty(compensationDo.getParamRealType())) {
			return true;
		} else if (ArrayUtil.isEmpty(args) && !StringUtils.isEmpty(compensationDo.getParamRealType())) {
			return false;
		} else {
			if (args.length == 1) {
				return args[0].getName().equals(compensationDo.getParamType());
			} else {
				Class<?>[] classesReq = method.getParameterTypes();
				StringBuilder stringBuffer = new StringBuilder();
				for (Class<?> aClass : classesReq) {
					stringBuffer.append(aClass.getName()).append(",");
				}
				String multiParamStr = stringBuffer.substring(0, stringBuffer.length() - 1);
				return compensationDo.getParamRealType().equals(multiParamStr);
			}
		}
	}
}
