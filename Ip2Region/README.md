ip2Region
将 ip 地址，转换成相应的位置归属地。

需要传入  ip 信息，
会返回地址信息.

1. 添加依赖
~~~xml
        <!--ip2region库-->
        <dependency>
            <groupId>net.dreamlu</groupId>
            <artifactId>mica-ip2region</artifactId>
            <version>2.5.6</version>
        </dependency>
~~~

2. 注入对象:

~~~java
	@Resource
	Ip2regionSearcher ip2regionSearcher;
~~~

3. 根据ip 地址进行查询
~~~java
	/**
	 139.155.90.176
	 IpInfo(cityId=null, country=中国, region=null, province=四川, city=成都, isp=电信, dataPtr=153719)
	 稳定，且正确。
	 */
	@RequestMapping("/getAddress2/{ip}")
	public String getAddressByIp2(@PathVariable("ip") String ip){
		log.info("get address by ip:{}",ip);
		
		IpInfo ipInfo = ip2regionSearcher.memorySearch(ip);
		if (null == ipInfo){
			return UNKNOWN;
		}
		return ipInfo.toString();
	}
~~~

