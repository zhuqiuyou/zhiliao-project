package com.cn.thinkx.ecom.cash;

import java.util.Properties;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.WebApplicationInitializer;

import com.alibaba.druid.pool.DruidDataSource;

@EnableTransactionManagement
@SpringBootApplication
@EnableJms
public class App extends SpringBootServletInitializer implements WebApplicationInitializer {

	@Autowired
	private MyBatisProps myBatis;
	@Autowired
	private Environment env;
	
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(App.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	public DruidDataSource dataSource() {
		return new DruidDataSource();
	}
	
//	// 测试及生产环境DataSource配置 使用jndi数据库连接池
//	@Bean
//	public DataSource dataSource() {
//		JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
//		return dataSourceLookup.getDataSource(env.getProperty("spring.datasource.jndi-name"));
//	}

	// 提供SqlSeesion
	@Bean
	@ConditionalOnMissingBean
	public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource());
		Properties props = new Properties();
		props.setProperty("dialect", "oracle");
		props.setProperty("reasonable", "true");
		props.setProperty("supportMethodsArguments", "true");
		props.setProperty("returnPageInfo", "check");
		props.setProperty("params", "count=countSql");

		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		// sqlSessionFactoryBean.setTypeAliasesPackage(myBatis.getTypeAliasesPackage());
		sqlSessionFactoryBean.setMapperLocations(resolver.getResources(myBatis.getMapperLocations()));
		sqlSessionFactoryBean.setConfigLocation(resolver.getResource(myBatis.getConfigLocations()));
		return sqlSessionFactoryBean.getObject();
	}

	// 事务管理
	@Bean
	@ConditionalOnMissingBean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}

}