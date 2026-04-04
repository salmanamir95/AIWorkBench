package com.aiworkbench.auth.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setUrl(DotenvConfig.get("db_conn_string"));
        dataSource.setUsername(DotenvConfig.get("username"));
        dataSource.setPassword(DotenvConfig.get("password"));
        dataSource.setDriverClassName(DotenvConfig.get("driver_class"));

        return dataSource;
    }
}
