package top.yueshushu.entity;

/**
 * 时区转化区域
 *
 * @author wanyanhw
 * @date 2022/3/21 11:01
 */
public enum TimeSwitchScope {
	/**
	 * 默认，（输入输出参数）
	 */
	DEFAULT,

	/**
	 * 仅输入参数
	 */
	INPUT_PARAM_ONLY,

	/**
	 * 仅输出参数
	 */
	OUTPUT_PARAM_ONLY
}
