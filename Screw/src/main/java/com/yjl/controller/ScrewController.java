package com.yjl.controller;

import com.yjl.model.SelfReq;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;

/**
 * @ClassName:DBController
 * @Description TODO
 * @Author 岳建立
 * @Date 2020/9/16 17:59
 * @Version 1.0
 **/
@RestController
@RequestMapping("/screw")
public class ScrewController {
    
    @Autowired
    protected DataSource dataSource;
    @Value("${spring.datasource.hikari.driver-class-name}")
    private String driverClassName;
    @Value("${spring.datasource.hikari.jdbc-url}")
    private String url;
    @Value("${spring.datasource.hikari.username}")
    private String userName;
    @Value("${spring.datasource.hikari.password}")
    private String password;
    @Value("${screw.filePath}")
    private String filePath;
    
    @GetMapping("/show")
    public String show() throws Exception {
        // 获取元数据
        DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
        
        String driverClassName = metaData.getDriverName();
        String url = metaData.getURL();
        String userName = metaData.getUserName().split("@")[0];
        String dbName = getDBNameByDataSource(dataSource);
        return "地址:" + url + ",\n用户名:" + userName + ",关联的数据库名:" + dbName;
    }
    
    /**
     * 文档生成
     */
    @RequestMapping("/default")
    public void defaultGenerate(HttpServletResponse httpServletResponse) {
        try {
            //数据源
            String dbName = getDBNameByDataSource(dataSource);
            //生成配置
            EngineConfig engineConfig = EngineConfig.builder()
                    //生成文件路径
                    .fileOutputDir(filePath)
                    //打开目录
                    .openOutputDir(true)
                    //文件类型
                    .fileType(EngineFileType.HTML)
                    //生成模板实现
                    .produceType(EngineTemplateType.freemarker)
                    //自定义文件名称
                    .fileName(dbName).build();
            ProcessConfig processConfig = ProcessConfig.builder()
                    //指定生成逻辑、当存在指定表、指定表前缀、指定表后缀时，将生成指定表，其余表不生成、并跳过忽略表配置
                    //根据名称指定表生成
                    .designatedTableName(new ArrayList<>())
                    //根据表前缀生成
                    .designatedTablePrefix(new ArrayList<>())
                    //根据表后缀生成
                    .designatedTableSuffix(new ArrayList<>())
                    //忽略表名
                    .build();
            //配置
            Configuration config = Configuration.builder()
                    //版本
                    .version("1.0.0")
                    //描述
                    .description(dbName + " 数据库设计文档生成")
                    //数据源
                    .dataSource(dataSource)
                    //生成配置
                    .engineConfig(engineConfig)
                    //生成配置
                    .produceConfig(processConfig)
                    .build();
            //执行生成
            new DocumentationExecute(config).execute();
            
            String fileAllPath = filePath + File.separator + dbName + ".html";
            
            File resultFile = new File(fileAllPath);
            FileInputStream inputStream = new FileInputStream(resultFile);
            // 设置响应头、以附件形式打开文件
            httpServletResponse.setHeader("content-disposition", "attachment; fileName=" + resultFile.getName());
            ServletOutputStream outputStream = httpServletResponse.getOutputStream();
            int len = 0;
            byte[] data = new byte[1024];
            while ((len = inputStream.read(data)) != -1) {
                outputStream.write(data, 0, len);
            }
            outputStream.close();
            inputStream.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private String getDBNameByDataSource(DataSource dataSource) {
        try {
            // 获取元数据
            return getDBName(dataSource.getConnection().getMetaData().getURL());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 根据 url 获取数据库的名称
     *
     * @param jdbcUrl 数据库 url
     *                如: jdbc:mysql://192.168.100.252:3306/lcd?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC&useInformationSchema=true
     */
    private String getDBName(String jdbcUrl) {
        String database = null;
        int pos, pos1;
        String connUri;
        
        if (StringUtils.isBlank(jdbcUrl)) {
            throw new IllegalArgumentException("Invalid JDBC url.");
        }
        
        jdbcUrl = jdbcUrl.toLowerCase();
        
        if (jdbcUrl.startsWith("jdbc:impala")) {
            jdbcUrl = jdbcUrl.replace(":impala", "");
        }
        
        if (!jdbcUrl.startsWith("jdbc:")
                || (pos1 = jdbcUrl.indexOf(':', 5)) == -1) {
            throw new IllegalArgumentException("Invalid JDBC url.");
        }
        
        connUri = jdbcUrl.substring(pos1 + 1);
        
        if (connUri.startsWith("//")) {
            if ((pos = connUri.indexOf('/', 2)) != -1) {
                database = connUri.substring(pos + 1);
            }
        } else {
            database = connUri;
        }
        if (database.contains("?")) {
            database = database.substring(0, database.indexOf("?"));
        }
        
        if (database.contains(";")) {
            database = database.substring(0, database.indexOf(";"));
        }
        
        if (StringUtils.isBlank(database)) {
            throw new IllegalArgumentException("Invalid JDBC url.");
        }
        return database;
    }
    
    /**
     * 文档生成
     */
    @PostMapping("/self")
    public void self(@RequestBody SelfReq selfReq, HttpServletResponse response) {
        try {
            EngineFileType engineFileType;
            String fileSuffix;
            if (selfReq.getFileType() == null || selfReq.getFileType() == 1) {
                engineFileType = EngineFileType.MD;
                fileSuffix = ".md";
            } else if (selfReq.getFileType() == 2) {
                engineFileType = EngineFileType.HTML;
                fileSuffix = ".html";
            } else {
                engineFileType = EngineFileType.WORD;
                fileSuffix = ".doc";
            }
            DataSource selfDataSource;
            // 对数据源进行处理
            if (StringUtils.isBlank(selfReq.getUrl())) {
                selfDataSource = dataSource;
            } else {
                HikariConfig hikariConfig = new HikariConfig();
                hikariConfig.setDriverClassName(Optional.ofNullable(selfReq.getDriverClassName()).orElse(driverClassName));
                hikariConfig.setJdbcUrl(Optional.ofNullable(selfReq.getUrl()).orElse(url));
                hikariConfig.setUsername(Optional.ofNullable(selfReq.getUserName()).orElse(userName));
                hikariConfig.setPassword(Optional.ofNullable(selfReq.getPassword()).orElse(password));
                //设置可以获取tables remarks信息
                hikariConfig.addDataSourceProperty("useInformationSchema", "true");
                hikariConfig.setMinimumIdle(2);
                hikariConfig.setMaximumPoolSize(5);
                selfDataSource = new HikariDataSource(hikariConfig);
            }
            String fileName = getDBNameByDataSource(selfDataSource);
            //数据源
            EngineConfig engineConfig = EngineConfig.builder()
                    //生成文件路径
                    .fileOutputDir(Optional.ofNullable(selfReq.getFilePath()).orElse(filePath))
                    //打开目录
                    .openOutputDir(false)
                    //文件类型
                    .fileType(engineFileType)
                    //生成模板实现
                    .produceType(EngineTemplateType.freemarker)
                    //自定义文件名称
                    .fileName(Optional.ofNullable(selfReq.getFileName()).orElse(fileName))
                    .build();
            
            //忽略表
            ProcessConfig.ProcessConfigBuilder processConfigBuilder = ProcessConfig.builder();
            if (StringUtils.isNotBlank(selfReq.getTableNameStr())) {
                processConfigBuilder.designatedTableName(splitList(selfReq.getTableNameStr()));
            }
            if (StringUtils.isNotBlank(selfReq.getPrefixTableNameStr())) {
                processConfigBuilder.designatedTablePrefix(splitList(selfReq.getPrefixTableNameStr()));
            }
            if (StringUtils.isNotBlank(selfReq.getSuffixTableNameStr())) {
                processConfigBuilder.designatedTableSuffix(splitList(selfReq.getSuffixTableNameStr()));
            }
            if (StringUtils.isNotBlank(selfReq.getIgnoreTableNameStr())) {
                processConfigBuilder.ignoreTableName(splitList(selfReq.getIgnoreTableNameStr()));
            }
            if (StringUtils.isNotBlank(selfReq.getIgnorePrefixTableNameStr())) {
                processConfigBuilder.ignoreTablePrefix(splitList(selfReq.getIgnorePrefixTableNameStr()));
            }
            if (StringUtils.isNotBlank(selfReq.getIgnoreSuffixTableNameStr())) {
                processConfigBuilder.ignoreTableSuffix(splitList(selfReq.getIgnoreSuffixTableNameStr()));
            }
            ProcessConfig processConfig = processConfigBuilder.build();
            //配置
            Configuration config = Configuration.builder()
                    //版本
                    .version(Optional.ofNullable(selfReq.getVersion()).orElse("1.0.0"))
                    //描述
                    .description(Optional.ofNullable(selfReq.getDescription()).orElse(fileName + "数据库设计文档生成"))
                    //数据源
                    .dataSource(selfDataSource)
                    //生成配置
                    .engineConfig(engineConfig)
                    //生成配置
                    .produceConfig(processConfig)
                    .build();
            //执行生成
            new DocumentationExecute(config).execute();
            
            
            String fileAllPath = Optional.ofNullable(selfReq.getFilePath()).orElse(filePath) + File.separator + Optional.ofNullable(selfReq.getFileName()).orElse(fileName) + fileSuffix;
            
            File resultFile = new File(fileAllPath);
            FileInputStream inputStream = new FileInputStream(resultFile);
            // 设置响应头、以附件形式打开文件
            response.setHeader("content-disposition", "attachment; fileName=" + resultFile.getName());
            ServletOutputStream outputStream = response.getOutputStream();
            int len = 0;
            byte[] data = new byte[1024];
            while ((len = inputStream.read(data)) != -1) {
                outputStream.write(data, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 测试数据库是否连接成功
     */
    @PostMapping("/sqlConnection")
    public String sqlConnection(@RequestBody SelfReq selfReq) throws Exception {
        try {
            Class.forName(selfReq.getDriverClassName());
            Connection connection = DriverManager.getConnection(selfReq.getUrl(),
                    selfReq.getUserName(), selfReq.getPassword());
            if (null == connection) {
                throw new SQLException();
            }
            return "连接成功";
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    
    private List<String> splitList(String prefixStr) {
        if (StringUtils.isBlank(prefixStr)) {
            return Collections.emptyList();
        }
        //按照 ,号进行拆分
        String[] split = prefixStr.split("\\,");
        List<String> result = new ArrayList<>();
        for (String str : split) {
            if (StringUtils.isNotBlank(str)) {
                result.add(str);
            }
        }
        return result;
    }
}
