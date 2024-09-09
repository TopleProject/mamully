package com.mamully.toyProject.config;


import com.mamully.toyProject.test.command.services.TestDTO;
import com.mamully.toyProject.test.query.mappers.TestMapper;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.mamully.toyProject", annotationClass = Mapper.class)
public class MybatisConfig {

    @Value("${DRIVER_CLASS_NAME}")
    private String driverClassName;

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${MYSQL_USER}")
    private String username;

    @Value("${MYSQL_PASSWORD}")
    private String password;

    @Bean
    public HikariDataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        // 데이터베이스 연결을 시도할 때 타임 아웃 (30초)
        dataSource.setConnectionTimeout(30000);

        // 데이터베이스 연결 유휴 상태 유지 시간 (10분)
        dataSource.setIdleTimeout(600000);

        // 데이터베이스 연결 최대 유지 시간 (30분)
        dataSource.setMaxLifetime(1800000);

        // 커넥션 풀의 최대 연결 수
        dataSource.setMaximumPoolSize(50);

        return dataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();

        // 필요 시 TypeAlias, Mapper 등록
        configuration.getTypeAliasRegistry().registerAlias("TestDTO", TestDTO.class);
        configuration.addMapper(TestMapper.class);

        // DB 필드명이 언더스코어로 구분될 때, 자바의 카멜케이스 변수로 변환
//        configuration.setMapUnderscoreToCamelCase(true);

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        sqlSessionFactoryBean.setConfiguration(configuration);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory());
    }
}
