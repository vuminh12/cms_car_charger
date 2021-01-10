package com.example.demospringbean.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import java.util.HashMap;
import java.util.Map;

// anotation đánh đấu nó là 1 file cấu hình
@Configuration
// anotation để lấy thuôc tính của file
@PropertySource("classpath:application.properties")
// anotation tự động cấu hình jpa
@EnableJpaRepositories(
        // là 1 interface để quản lý entity customer kết nối đến db
        entityManagerFactoryRef = "customersEntityManager",
        // là 1 interface để quản lý transaction customer
        transactionManagerRef = "customersTransactionManager",
        basePackages = {"com.example.demospringbean.Repository"})
public class AppConfig {
    @Autowired
    // tiêm môi trường của application.properties vào class
    private Environment environment;

    // định nghĩa các thuộc tính bằng các hằng
    private static final String PROPERTY_NAME_DATABASE_DRIVER = "spring.datasource.driverClassName";
    private static final String PROPERTY_NAME_DATABASE_URL = "spring.datasource.url";
    private static final String PROPERTY_NAME_DATABASE_USERNAME = "spring.datasource.username";
    private static final String PROPERTY_NAME_DATABASE_PASSWORD = "spring.datasource.password";
    private static final String PROPERTY_NAME_HIBERNATE_DIALECT = "spring.jpa.hibernate.dialect";
    private static final String PROPERTY_NAME_HIBERNATE_HBN2DDL_SQL = "spring.jpa.hibernate.ddl-auto";


    // đánh dấu 1 bean
    @Bean(name = "customersDataSource")
    // @Primary để bỏ việc bean tự động cấu hình
    @Primary
    public DataSource getDataSourceMysql() {
        // dùng datasourceBuilder để quản lý kết nối đến db
        DataSourceBuilder dataSource = DataSourceBuilder.create();
        // lấy các thuộc tính của application.properties
        dataSource.url(environment.getProperty(PROPERTY_NAME_DATABASE_URL));
        dataSource.username(environment.getProperty(PROPERTY_NAME_DATABASE_USERNAME));
        dataSource.password(environment.getProperty(PROPERTY_NAME_DATABASE_PASSWORD));
        dataSource.driverClassName(environment.getProperty(PROPERTY_NAME_DATABASE_DRIVER));
        return dataSource.build();
    }

    @Bean(name = "customersEntityManager")

    public LocalContainerEntityManagerFactoryBean getCustomersEntityManager(
            EntityManagerFactoryBuilder builder,
            // Qualifier ?
            @Qualifier("customersDataSource") DataSource customersDataSource){
        // LocalContainerEntityManagerFactoryBean hỗ trợ khai báo đối tượng EntityManagerFactory
        LocalContainerEntityManagerFactoryBean result = builder
                .dataSource(customersDataSource)
                .packages("com.example.demospringbean.Model")
                // để làm việc với jpa cần có 1 file persistence.xml ,
                // persistenceUnit tên đã được khai náo trong persistence.xml
                .persistenceUnit("customers")
                .properties(additionalJpaProperties())
                .build();
        return result;
    }   // vì thuộc tính trong applicatio.properties có key và values nên cần khai báo 1 map để lưu trữ
    //
        Map<String,?> additionalJpaProperties(){
        Map<String,String> map = new HashMap<>();
        map.put(PROPERTY_NAME_HIBERNATE_HBN2DDL_SQL, environment.getProperty(PROPERTY_NAME_HIBERNATE_HBN2DDL_SQL));
        map.put(PROPERTY_NAME_HIBERNATE_DIALECT, environment.getProperty(PROPERTY_NAME_HIBERNATE_DIALECT));
        return map;
    }
    // khai báo và quản lý 1 transaction
    @Bean(name = "customersTransactionManager")
    public JpaTransactionManager transactionManager(@Qualifier("customersEntityManager") EntityManagerFactory customersEntityManager){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(customersEntityManager);
        return transactionManager;
    }
}
