package com.ke.rediscache.config;

import com.github.pagehelper.PageInterceptor;
import java.util.Properties;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author stephen 2022/11/20 11:03
 */
@Configuration
public class MybatisConfig {

    @Bean(name = "mybatisConfiguration")
    public org.apache.ibatis.session.Configuration mybatisConfiguration() {
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        // 驼峰映射
        configuration.setMapUnderscoreToCamelCase(true);
        // 日志输出格式
        configuration.setLogImpl(StdOutImpl.class);
        return configuration;
    }

    @Bean(name = "pageInterceptor")
    public PageInterceptor interceptor() {
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "mysql");
        properties.setProperty("reasonable", "true");
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("params", "count=countSql");
        pageInterceptor.setProperties(properties);
        return pageInterceptor;
    }
}
