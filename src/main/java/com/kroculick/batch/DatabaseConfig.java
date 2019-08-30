package com.kroculick.batch;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:application.properties")
public class DatabaseConfig {

//
//@Value("${db.driver}")
//private String DB_DRIVER;

    @Autowired
    private Environment env;

    @Bean
    @Primary
    public DataSource dataSource() {

        // org.apache.commons.dbcp2.BasicDataSource dataSource = new org.apache.commons.dbcp2.BasicDataSource();
        //BasicDataSource dataSource = new BasicDataSource();

        /*DataSourceBuilder dataSource = DataSourceBuilder.create();
        //SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.driverClassName("org.h2.Driver");
        dataSource.url("jdbc:h2:tcp://localhost:9092/~/firstdb");
        dataSource.username("sa");
        dataSource.password("");

        return dataSource.build();*/
        BasicDataSource dataSource = new BasicDataSource();
        //DriverManagerDataSource dataSource = new DriverManagerDataSource();

        //dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));

        // dataSource.setUrl("jdbc:h2:tcp://localhost:9092/~/firstdb");
       // dataSource.setUrl("jdbc:h2:file:~/firstdb;DB_CLOSE_ON_EXIT=FALSE;IFEXISTS=TRUE;DB_CLOSE_DELAY=-1;");
        dataSource.setUrl(env.getProperty("spring.datasource.url"));


       // dataSource.setUsername("sa");
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        //dataSource.setPassword("");
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        return dataSource;

    }

    @Bean
    public DataSource dataSource1() {

        // org.apache.commons.dbcp2.BasicDataSource dataSource = new org.apache.commons.dbcp2.BasicDataSource();
        //BasicDataSource dataSource = new BasicDataSource();

        /*DataSourceBuilder dataSource = DataSourceBuilder.create();
        //SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.driverClassName("org.h2.Driver");
        dataSource.url("jdbc:h2:tcp://localhost:9092/~/firstdb");
        dataSource.username("sa");
        dataSource.password("");

        return dataSource.build();*/

        BasicDataSource dataSource = new BasicDataSource();
        //DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        //dataSource.setUrl("jdbc:h2:tcp://localhost:9092/~/firstdb");
        dataSource.setUrl("jdbc:h2:file:~/firstdb;DB_CLOSE_ON_EXIT=FALSE;IFEXISTS=TRUE;DB_CLOSE_DELAY=-1;");
        dataSource.setUsername("sa");
        dataSource.setPassword("");

        return dataSource;

    }


    /*@Bean
    public JdbcTemplate jdbcTemplate(final DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }*/
}
