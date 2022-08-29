package top.yueshushu.service;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

import top.yueshushu.entity.CompensationDO;

/**
 * <p>
 * 服务补偿表 自定义的
 * </p>
 *
 * @author yuejianli
 * @since 2022-08-26
 */
public interface CompensationService extends IService<CompensationDO> {
	/**
	 * 是否存在运行中的 busKey
	 *
	 * @param busKey busKey
	 */
	boolean existIngKey(String busKey);
	
	/**
	 * 根据bus key 进行查询
	 *
	 * @param busKey busKey
	 */
	CompensationDO getByBusKey(String busKey);
	
	/**
	 * 查询出正常运行中的任务
	 */
	List<CompensationDO> listIng();
	
	/**
	 * 全部自动重试
	 */
	String autoRetry();
	
	/**
	 * 手动重试
	 */
	String manualRetry(Integer id);
	
	/**
	 * 运行单参数
	 *
	 * @param randNum 传入参数
	 */
	void randSingleMethod(Integer randNum);
	
	/**
	 * 运行无参方法
	 */
	void randNoMethod();
	
	/**
	 * 运行多参方法
	 *
	 * @param maxNum  最大值
	 * @param randNum 随机值
	 */
	void randMoreMethod(Integer maxNum, Integer randNum);
}
