package com.restaurant.td5springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        String url = System.getenv("JDBC_URL");
        String user = System.getenv("USER");
        String password = System.getenv("PASSWORD");

        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);

        return dataSource;
    }

}