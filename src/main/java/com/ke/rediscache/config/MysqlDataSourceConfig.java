package com.ke.rediscache.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.github.pagehelper.PageInterceptor;
import javax.sql.DataSource;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * @author stephen 2022/11/19 23:39
 */
@org.springframework.context.annotation.Configuration
@MapperScan(basePackages = {
    "com.ke.rediscache.mapper"}, sqlSessionFactoryRef = "mysqlSqlSessionFactory")
public class MysqlDataSourceConfig {

    @Bean(name = "mysqlDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.druid.main")
    public DataSource mysqlDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "mysqlTransactionManager")
    public DataSourceTransactionManager mysqlTransactionManager(@Qualifier("mysqlDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "mysqlSqlSessionFactory")
    public SqlSessionFactory mysqlSqlSessionFactory(
        @Qualifier("mysqlDataSource") DataSource dataSource,
        @Qualifier("mybatisConfiguration") Configuration configuration,
        @Qualifier("pageInterceptor") PageInterceptor pageInterceptor) throws Exception {

        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setConfiguration(configuration);
        factoryBean.setMapperLocations(
            new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/*.xml"));
        factoryBean.setPlugins(pageInterceptor);
        return factoryBean.getObject();
    }

    @Bean(name = "mysqlSqlSessionTemplate")
    public SqlSessionTemplate mysqlSqlSessionTemplate(
        @Qualifier("mysqlSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
