package top.yueshushu.itextpdf.service;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.bean.BeanUtil;
import top.yueshushu.itextpdf.model.School;
import top.yueshushu.itextpdf.model.UserArchives;
import top.yueshushu.itextpdf.util.PdfUtil;

/**
 * Pdf 实现
 *
 * @author yuejianli
 * @date 2022-07-21
 */

@Service
public class PdfServiceImpl implements PdfService {
	
	private VelocityEngine velocityEngine;
	@Value("${pdf.directory}")
	private String pdfDirectory;
	
	
	@PostConstruct
	public void initVelocityEngine() {
		velocityEngine = new VelocityEngine();
		Properties p = new Properties();
		velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		velocityEngine.init(p);
	}
	

	
	@Override
	public void saveFile() {
		String velocityMailText = getVelocityMailText(getDataMap());
		
		PdfUtil.html2Pdf(velocityMailText,pdfDirectory+"user.pdf");
	}
	
	@Override
	public void show(HttpServletResponse httpServletResponse) throws Exception {
		String velocityMailText = getVelocityMailText(getDataMap());
		
		byte[] bytes = PdfUtil.html2Pdf(velocityMailText);
		
		httpServletResponse.getOutputStream().write(bytes);
		
	}
	
	private String getVelocityMailText(Map<String, Object> dataMap) {
		VelocityContext velocityContext = new VelocityContext(dataMap);
		StringWriter writer = new StringWriter();
		String templateLocation = "user" + ".vm";
		velocityEngine.mergeTemplate(templateLocation, "UTF-8", velocityContext, writer);
		return writer.toString();
	}
	
	private Map<String,Object> getDataMap(){
		Map<String,Object> result;
		
		UserArchives userArchives = new UserArchives();
		userArchives.setId(1);
		userArchives.setName("岳泽霖");
		userArchives.setAge(28);
		userArchives.setSex("男");
		userArchives.setAddress("保密");
		userArchives.setMarry("未婚");
		
		userArchives.setEvaluation("一个好青年");
		
		List<School> schoolList = new ArrayList<>();
		
		School school1 = new School();
		school1.setName("沈阳理工大学");
		school1.setStartDate("2014-09");
		school1.setEndDate("2018-07");
		
		schoolList.add(school1);
		
		School school2 = new School();
		school2.setName("睢县回高");
		school2.setStartDate("2011-09");
		school2.setEndDate("2014-07");
		
		schoolList.add(school2);
		
		userArchives.setSchoolList(schoolList);
		
		result = BeanUtil.beanToMap(userArchives);
		
		return result;
	}
}
