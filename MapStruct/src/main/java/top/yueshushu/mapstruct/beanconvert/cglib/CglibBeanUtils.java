package top.yueshushu.mapstruct.beanconvert.cglib;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.bean.BeanUtil;

/**
 * CglibBeanUtils   class
 *
 * @author zhiwei
 * @date 2020/09/28
 */
public class CglibBeanUtils {
	private static final ObjectMapper mapper = new ObjectMapper();
	
	static {
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	/**
	 * 根据源对象content转换成目标对象，源对象支持集合类型，但源对象中的实体类和目标对象中的实体类需要属性名相同
	 *
	 * @param content         源对象
	 * @param collectionClass 需要转换的目标集合类型
	 * @param elementClasses  目标集合类型里封装的实体类
	 * @return
	 */
	public static <T> T getCollectionBean(Object content, Class<?> collectionClass, Class<?>... elementClasses) {
		if (content == null) {
			return null;
		}
		String contentStr = null;
		try {
			contentStr = mapper.writeValueAsString(content);
			JavaType javaType = mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
			return mapper.readValue(contentStr, javaType);
		} catch (IllegalArgumentException | IOException e) {
		}
		return null;
	}
	
	/**
	 * 根据源对象obj转换成目标对象，不支持集合类型，源对象和目标对象属性名需要相同
	 *
	 * @param obj       源对象
	 * @param valueType 需要转换的目标集合类型
	 * @return
	 */
	public static <T> T getDataBeanWithType(Object obj, Class<T> valueType) {
		String dataNode = null;
		try {
			dataNode = mapper.writeValueAsString(obj);
			return mapper.readValue(dataNode, valueType);
		} catch (IOException e) {
		}
		return null;
	}
	
	/**
	 * 拷贝bean的不同名称之间属性值,
	 * 需要手工维护一个bean之间不同属性名的映射关系，不支持源对象或目标对象为集合类型
	 *
	 * @param target 目标对象
	 * @param source 源对象
	 * @param param  源对象和目标对象的映射关系,其中key为源对象的属性名，value为目标对象的属性名
	 * @return 目标对象
	 * @throws ClassNotFoundException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	@SuppressWarnings("rawtypes")
	public static Object getObject(Object target, Object source, Map<String, String> param) throws ClassNotFoundException,
			IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		//获取源对象的属性名和值，属性名作为key,值作为value
		Map<String, Object> sourceBeanMap = BeanUtil.beanToMap(source);
		// 获取实体对象属性名数组
		Field[] fields = source.getClass().getDeclaredFields();
		// 属性名，属性类型
		Map<String, Class> temp = new HashMap<>();
		// 属性名，属性值
		Map<String, Object> valueParam = new HashMap<>();
		for (Field f : fields) {
			// 设置访问权限，否则不能访问私有化属性
			f.setAccessible(true);
			// 若没有映射关系，则跳过继续寻找有映射关系的属性
			if (null == param.get(f.getName())) {
				continue;
			}
			temp.put(param.get(f.getName()), Class.forName(f.getType().getName()));
			for (Map.Entry<String, String> entry : param.entrySet()) {
				if (entry.getKey().equals(f.getName())) {
					Object value = sourceBeanMap.get(entry.getKey());
					valueParam.put(param.get(f.getName()), value);
					break;
				}
			}
		}
		// 根据参数生成CglibBean对象
		CglibBean cglibBean = new CglibBean(temp);
		for (Map.Entry<String, Object> entry : valueParam.entrySet()) {
			cglibBean.setValue(entry.getKey(), entry.getValue());
		}
		Object object = cglibBean.getObject();
        /*// 用object给目标对象赋值
        PropertyUtils.copyProperties(target, object);*/
		BeanUtils.copyProperties(target, object);
		return target;
	}
}