package top.yueshushu.mapstruct.beanconvert;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import top.yueshushu.mapstruct.beanconvert.cglib.CglibBeanUtils;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-07-14
 */
@Slf4j
public class BeanConvertTest {
	
	private List<SourceDto> sourceDtoList;
	private Map<String, String> paramMap;
	
	@Before
	public void init() {
		sourceDtoList = new ArrayList<>(1000);
		
		for (int i = 0; i < 10000; i++) {
			SourceDto sourceDto = new SourceDto();
			// 放置数据
			sourceDto.setId(Long.valueOf(i));
			sourceDto.setAttrName("属性名:" + i);
			sourceDto.setProductArea("test" + i);
			sourceDto.setPrice(BigDecimal.valueOf(Double.parseDouble(i + "")));
			sourceDto.setBarCode("A" + i);
			sourceDtoList.add(sourceDto);
		}
		
		
		paramMap = new HashMap<>();
		
		Field[] declaredFields = SourceDto.class.getDeclaredFields();
		for (Field field : declaredFields) {
			paramMap.put(field.getName(), field.getName());
		}
	}
	
	// 1次是 200 ms 往上
	@Test
	public void beanToTest() throws Exception {
		// 获取1个数据.
		log.info(">>>> begin");
		
		SourceDto sourceDto = sourceDtoList.get(0);
		
		// 获取数据
		TargetDto targetDto = new TargetDto();
		
		Object object = CglibBeanUtils.getObject(targetDto, sourceDto, paramMap);
		
		log.info(">>>end :{}", object);
	}
	
	/**
	 * 100 次 大概 300
	 * <p>
	 * 1000次    450
	 * <p>
	 * 10000 次   1200
	 * <p>
	 * 100000  次  8000
	 */
	@Test
	public void morebeanToTest() throws Exception {
		// 获取1个数据.
		log.info(">>>> begin");
		List<TargetDto> targetDtoList = new ArrayList<>();
		for (SourceDto sourceDto : sourceDtoList) {
			TargetDto targetDto = new TargetDto();
			Object object = CglibBeanUtils.getObject(targetDto, sourceDto, paramMap);
			targetDtoList.add(targetDto);
		}
		log.info(">>>end :");
	}
	
	// 时间是 40ms
	
	@Test
	public void beanRefactToTest() throws Exception {
		// 获取1个数据.
		log.info(">>>> begin");
		SourceDto sourceDto = sourceDtoList.get(0);
		// 获取数据
		TargetDto targetDto = new TargetDto();
		Object object = ReflectBeanUtil.beanToBean(sourceDto, targetDto, paramMap);
		log.info(">>>end : {}", object);
	}
	
	/**
	 * 10   30
	 * <p>
	 * 100    40
	 * <p>
	 * 1000    60
	 * <p>
	 * 10000   140
	 */
	@Test
	public void moreBeanRefactToTest() throws Exception {
		log.info(">>>> begin");
		TargetDto targetDto = new TargetDto();
		Object object = ReflectBeanUtil.listToList(sourceDtoList, targetDto, paramMap);
		// log.info("{}",object);
		
		log.info(">>>end :{}");
		
		
	}
	
}
