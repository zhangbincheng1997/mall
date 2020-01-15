package com.example.demo;

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

    public static void main(String[] args) {
        final ResourceBundle rb = ResourceBundle.getBundle("application"); // application.properties

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir(System.getProperty("user.dir") + "/src/main/java");
        globalConfig.setAuthor("zzz");
        globalConfig.setOpen(false);
        globalConfig.setFileOverride(true);
        globalConfig.setDateType(DateType.ONLY_DATE); // mybatis-plus druid LocalDate冲突
        mpg.setGlobalConfig(globalConfig);

        // dataSource配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl(rb.getString("spring.datasource.url"));
        dataSourceConfig.setDriverName(rb.getString("spring.datasource.driver-class-name"));
        dataSourceConfig.setUsername(rb.getString("spring.datasource.username"));
        dataSourceConfig.setPassword(rb.getString("spring.datasource.password"));
        mpg.setDataSource(dataSourceConfig);

        // package配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent("com.example.demo"); // 生成目录
        mpg.setPackageInfo(packageConfig);

        // template配置
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null);
        templateConfig.setService(null);
        templateConfig.setController(null);
        templateConfig.setServiceImpl(null);
        mpg.setTemplate(templateConfig);

        // strategy配置
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setEntityLombokModel(true);
        mpg.setStrategy(strategyConfig);

        // 模板引擎
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        mpg.execute();
    }
}
