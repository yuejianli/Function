package top.yueshushu.mapstruct.beanconvert;

import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.util.ArrayUtil;

/**
 * 反射转换对象
 *
 * @author yuejianli
 * @date 2022-07-14
 */

public class ReflectBeanUtil {
	
	/**
	 * 单个对象之间转换
	 *
	 * @param source   源对象
	 * @param target   目标对象
	 * @param paramMap 转换映射关系, 对应的是 源key--->目标key
	 */
	public static Object beanToBean(Object source, Object target, Map<String, String> paramMap) throws Exception {
		Field[] declaredFields = source.getClass().getDeclaredFields();
		if (ArrayUtil.isEmpty(declaredFields)) {
			return target;
		}
		List<String> fieldNameList = Arrays.stream(declaredFields).map(Field::getName).collect(Collectors.toList());
		
		List<Field> fillFieldList = new ArrayList<>();
		// 对属性进行设置.
		for (Map.Entry<String, String> paramEntry : paramMap.entrySet()) {
			// 获取属性
			if (!fieldNameList.contains(paramEntry.getKey())) {
				continue;
			}
			fillFieldList.add(source.getClass().getDeclaredField(paramEntry.getKey()));
		}
		
		if (CollectionUtils.isEmpty(fillFieldList)) {
			return target;
		}
		
		// 对每一个属性进行处理.
		
		for (Field field : fillFieldList) {
			// 获取属性
			String fieldName = field.getName();
			
			String fieldValue = paramMap.get(fieldName);
			
			field.setAccessible(true);
			
			
			Field targetField = target.getClass().getDeclaredField(fieldValue);
			targetField.setAccessible(true);
			
			
			targetField.set(target, field.get(source));
		}
		
		return target;
	}
	
	/**
	 * 多对象转换
	 *
	 * @param listSource 多对象
	 * @param target     目标对象
	 * @param paramMap   对应转换关系
	 */
	public static Object listToList(Object listSource, Object target, Map<String, String> paramMap) throws Exception {
		List<Object> sourceList = (List<Object>) listSource;
		
		
		Field[] declaredFields = sourceList.get(0).getClass().getDeclaredFields();
		if (ArrayUtil.isEmpty(declaredFields)) {
			return Collections.emptyList();
		}
		List<String> fieldNameList = Arrays.stream(declaredFields).map(Field::getName).collect(Collectors.toList());
		
		List<Field> fillFieldList = new ArrayList<>();
		// 对属性进行设置.
		for (Map.Entry<String, String> paramEntry : paramMap.entrySet()) {
			// 获取属性
			if (!fieldNameList.contains(paramEntry.getKey())) {
				continue;
			}
			fillFieldList.add(sourceList.get(0).getClass().getDeclaredField(paramEntry.getKey()));
		}
		
		if (CollectionUtils.isEmpty(fillFieldList)) {
			return Collections.emptyList();
		}
		
		// 对每一个属性进行处理.
		
		
		List<Object> targetList = new ArrayList<>();
		
		
		for (Object source : sourceList) {
			
			Object targetObj = target.getClass().newInstance();
			
			for (Field field : fillFieldList) {
				// 获取属性
				String fieldName = field.getName();
				
				String fieldValue = paramMap.get(fieldName);
				
				field.setAccessible(true);
				
				Field targetField = targetObj.getClass().getDeclaredField(fieldValue);
				targetField.setAccessible(true);
				
				
				targetField.set(targetObj, field.get(source));
				
			}
			
			targetList.add(targetObj);
		}
		return targetList;
	}
}
