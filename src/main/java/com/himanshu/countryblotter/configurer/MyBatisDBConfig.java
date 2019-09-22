package com.himanshu.countryblotter.configurer;

import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = {"com.himanshu.countryblotter.dao"})
@EnableTransactionManagement
public class MyBatisDBConfig {
  @Bean
  public DataSource dataSource() {
    return new EmbeddedDatabaseBuilder()
          .setType(EmbeddedDatabaseType.H2)
          .addScript("schema.sql")
          .build();
  }

  @Bean
  public SqlSessionFactory sqlSessionFactory() throws Exception {
    SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
    factoryBean.setDatabaseIdProvider(new VendorDatabaseIdProvider());
    factoryBean.setDataSource(dataSource());
    return factoryBean.getObject();
  }

  @Bean
  public PlatformTransactionManager transactionManager() {
    PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource());
    return transactionManager;
  }
}
