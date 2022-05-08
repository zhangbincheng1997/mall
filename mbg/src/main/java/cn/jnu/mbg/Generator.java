package cn.jnu.mbg;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ResourceBundle;

/**
 * MyBatis-Plus Generator https://mp.baomidou.com/guide/config.html
 */
public class Generator {

    // 工程路径
    private static final String PATH = System.getProperty("user.dir");
    // 模块名
    private static final String MODULE = "/mbg";
    // 包名
    private static final String PACKAGE = "cn.jnu.mbg";

    public static void main(String[] args) {
        final ResourceBundle rb = ResourceBundle.getBundle("mbg"); // mbg.properties

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir(PATH + MODULE + "/src/main/java");
        globalConfig.setOpen(false);
        globalConfig.setFileOverride(true);
        globalConfig.setServiceName("%sService");
        globalConfig.setServiceImplName("%sServiceImpl");
        globalConfig.setDateType(DateType.ONLY_DATE); // mybatis-plus druid LocalDate冲突
        mpg.setGlobalConfig(globalConfig);

        // dataSource配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl(rb.getString("spring.datasource.url"));
        dataSourceConfig.setUsername(rb.getString("spring.datasource.username"));
        dataSourceConfig.setPassword(rb.getString("spring.datasource.password"));
        dataSourceConfig.setDriverName(rb.getString("spring.datasource.driver-class-name"));
        mpg.setDataSource(dataSourceConfig);

        // package配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent(PACKAGE); // 生成目录
        mpg.setPackageInfo(packageConfig);

        // template配置
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null);
        templateConfig.setEntity("templates/entity.java");
        templateConfig.setMapper("templates/mapper.java");
        templateConfig.setService("templates/service.java");
        templateConfig.setServiceImpl("templates/serviceImpl.java");
        templateConfig.setController(null);
        mpg.setTemplate(templateConfig);

        // strategy配置
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setEntityLombokModel(true);
        strategyConfig.setChainModel(true);
        mpg.setStrategy(strategyConfig);

        // 模板引擎
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        // RUN!!!
        mpg.execute();
    }
}
