package top.yueshushu.mapstruct.beanconvert.cglib;

import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.cglib.beans.BeanMap;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * CglibBean   class
 *
 * @author zhiwei
 * @date 2020/09/28
 */
public class CglibBean {
	/**
	 * 实体Object
	 */
	public Object object = null;
	/**
	 * 属性map
	 */
	public BeanMap beanMap = null;
	
	public CglibBean() {
	}
	
	public CglibBean(Map<String, Class> propertyMap) {
		this.object = generateBean(propertyMap);
		this.beanMap = BeanMap.create(this.object);
	}
	
	/**
	 * 给bean属性赋值
	 *
	 * @param property
	 * @param value
	 */
	public void setValue(String property, Object value) {
		beanMap.put(property, value);
	}
	
	/**
	 * 通过属性名得到属性值
	 *
	 * @param property
	 * @return
	 */
	public Object getValue(String property) {
		return beanMap.get(property);
	}
	
	/**
	 * 得到该实体bean对象
	 *
	 * @return
	 */
	public Object getObject() {
		return this.object;
	}
	
	/**
	 * 创建cglib动态代理的bean
	 *
	 * @param propertyMap 通过map创建
	 * @return 返回bean对象
	 */
	@SuppressWarnings("rawtypes")
	private Object generateBean(Map<String, Class> propertyMap) {
		BeanGenerator generator = new BeanGenerator();
		Set keySet = propertyMap.keySet();
		for (Iterator i = keySet.iterator(); i.hasNext(); ) {
			String key = (String) i.next();
			generator.addProperty(key, (Class) propertyMap.get(key));
		}
		return generator.create();
	}
}