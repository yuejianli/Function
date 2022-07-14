
MapStruct 使用:


1. pom.xml 添加依赖

~~~xml
        <!--引入 MapStruct-->
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct</artifactId>
            <version>1.4.2.Final</version>
        </dependency>
       <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct-processor</artifactId>
            <version>1.4.2.Final</version>
        </dependency>
~~~
                
                
2. 创建两个实体

转换源:

~~~java
@Data
public class MapUser implements Serializable {
	private Integer id;
	private String name;
	private Date birthday;
	private Integer age;
	private Integer sex;
	private String description;
	private String password;
}
~~~

转换目标: 
~~~java
@Data
public class MapUserVo implements Serializable {
	private Integer id;
	private String name;
	private Date birthday;
	private Integer age;
	private String sex;
	private String description;
	private String userPassword;
}
~~~

3. 定义转换的 Mapper

~~~java

//1. 添加注解  @org.mapstruct.Mapper 注解
@Mapper
public interface MapUserAssember {
	//2. 创建转换器
	MapUserAssember instance = Mappers.getMapper(MapUserAssember.class);
	
	/**
	 *简单默认转换
	 */
	MapUserVo simpleToUserVo(MapUser mapUser);
}
~~~


4. 进行转换处理


~~~java
	private MapUser mapUser;
	
	private MapUserSchool mapUserSchool;
	@Before
	public void initUser(){
		mapUser = new MapUser();
		mapUser.setId(1);
		mapUser.setName("岳泽霖");
		mapUser.setAge(28);
		mapUser.setSex(1);
		mapUser.setDescription("一个充满希望的老程序员");
		mapUser.setPassword("123456");
		mapUser.setBirthday(DateUtil.parse("1995-02-07", DatePattern.NORM_DATE_FORMAT));
		
		mapUserSchool = new MapUserSchool();
		mapUserSchool.setId(1);
		mapUserSchool.setName("睢县回高");
		mapUserSchool.setAddress("河南省睢县回高");
		mapUser.setMapUserSchool(mapUserSchool);
		log.info(">>>> user is:{}", mapUser);
	}
	
	/**
	 11:46:23.283 [main] INFO top.yueshushu.mapstruct.map.MapUserModelTest - >>>> user is:MapUser(id=1, name=岳泽霖, birthday=1995-02-07 00:00:00, age=28, sex=1, description=一个充满希望的老程序员, password=123456)
	 11:46:23.286 [main] INFO top.yueshushu.mapstruct.map.MapUserModelTest - >>> map simple user is MapUserVo(id=1, name=岳泽霖, birthday=1995-02-07 00:00:00, age=28, sex=1, description=一个充满希望的老程序员, userPassword=null)
	 
	 只有3秒， 转换速度挺快。
	 sex 同属性有值， 但发现 userPassword 没有值
	 */
	@Test
	public void simpleToUserVoTest(){
		
		MapUserVo mapUserVo = MapUserAssember.instance.simpleToUserVo(mapUser);
		log.info(">>> map simple user is {}",mapUserVo);
	}
~~~






        