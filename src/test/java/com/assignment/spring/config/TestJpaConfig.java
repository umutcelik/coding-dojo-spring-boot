package com.assignment.spring.config;

import javax.sql.DataSource;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootConfiguration
@ComponentScan(basePackages = "com.assignment.spring.repository")
@EnableTransactionManagement
@EnableAutoConfiguration
@EnableJpaRepositories(
    basePackages = {"com.assignment.spring.repository"},
    considerNestedRepositories = true
)
@EntityScan(basePackages = {"com.assignment.spring.entity"})
    public class TestJpaConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "test.datasource")
    public DataSource testDataSource() {
        return DataSourceBuilder.create().build();
    }
}
