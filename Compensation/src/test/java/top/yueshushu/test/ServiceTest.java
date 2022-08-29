package top.yueshushu.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import top.yueshushu.service.CompensationService;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-08-26
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class ServiceTest {
	@Resource
	private CompensationService compensationService;
	
	@Test
	public void runSingleMethodTest() throws Exception {
		try {
			compensationService.randSingleMethod(3);
		} catch (Exception e) {
			log.info(">>>> 运行失败");
		}
		
		TimeUnit.SECONDS.sleep(200);
	}
	
	@Test
	public void runNoMethodTest() throws Exception {
		try {
			compensationService.randNoMethod();
		} catch (Exception e) {
			log.info(">>>> 运行失败");
		}
		
		TimeUnit.SECONDS.sleep(200);
	}
	
	@Test
	public void runMoreMethodTest() throws Exception {
		try {
			compensationService.randMoreMethod(10, 1);
		} catch (Exception e) {
			log.info(">>>> 运行失败");
		}
		
		TimeUnit.SECONDS.sleep(200);
	}
}
