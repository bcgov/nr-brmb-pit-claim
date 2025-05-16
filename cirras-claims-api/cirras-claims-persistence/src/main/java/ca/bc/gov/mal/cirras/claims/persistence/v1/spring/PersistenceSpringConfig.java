package ca.bc.gov.mal.cirras.claims.persistence.v1.spring;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.sql.DataSource;

import org.apache.ibatis.session.LocalCacheScope;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationGrainQuantityDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationGrainSpotLossDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationGrainUnseededDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationPlantAcresDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationPlantUnitsDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationGrapesDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationUserDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationVarietyDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimStatusCodeDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.CommodityCoverageCodeDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.CoveragePerilDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.CropCommodityDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.CropVarietyDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.InsurancePlanDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.PerilCodeDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.CalculationStatusCodeDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationBerriesDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis.ClaimCalculationDaoImpl;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis.ClaimCalculationGrainQuantityDaoImpl;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis.ClaimCalculationGrainSpotLossDaoImpl;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis.ClaimCalculationGrainUnseededDaoImpl;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis.ClaimCalculationPlantAcresDaoImpl;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis.ClaimCalculationPlantUnitsDaoImpl;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis.ClaimCalculationGrapesDaoImpl;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis.ClaimCalculationUserDaoImpl;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis.ClaimCalculationVarietyDaoImpl;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis.ClaimDaoImpl;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis.ClaimStatusCodeDaoImpl;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis.CommodityCoverageCodeDaoImpl;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis.CoveragePerilDaoImpl;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis.CropCommodityDaoImpl;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis.CropVarietyDaoImpl;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis.InsurancePlanDaoImpl;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis.ClaimCalculationBerriesDaoImpl;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis.PerilCodeDaoImpl;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis.CalculationStatusCodeDaoImpl;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BooleanTypeHandler;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.InstantTypeHandler;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.LocalDateTimeTypeHandler;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.LocalDateTypeHandler;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.LocalTimeTypeHandler;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.ResetDirtyInterceptor;

@Configuration
@EnableTransactionManagement
public class PersistenceSpringConfig {

	private static final Logger logger = LoggerFactory.getLogger(PersistenceSpringConfig.class);
	
	public PersistenceSpringConfig() {
		logger.debug("<PersistenceSpringConfig");
		
		logger.debug(">PersistenceSpringConfig");
	}

	@Bean
	public PlatformTransactionManager transactionManager(DataSource cirrasClaimsDataSource) {
		return new DataSourceTransactionManager(cirrasClaimsDataSource);
	}

	@Bean
	public SqlSessionFactoryBean sqlSessionFactory(DataSource cirrasClaimsDataSource) {
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(cirrasClaimsDataSource);
		
		org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
		
		configuration.setCacheEnabled(true);
		configuration.setLazyLoadingEnabled(true);
		configuration.setAggressiveLazyLoading(false);
		configuration.setLocalCacheScope(LocalCacheScope.SESSION);
		
		TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
		typeHandlerRegistry.register(Boolean.class, JdbcType.VARCHAR, BooleanTypeHandler.class);
		typeHandlerRegistry.register(LocalTime.class, JdbcType.VARCHAR, LocalTimeTypeHandler.class);
		typeHandlerRegistry.register(LocalDate.class, JdbcType.DATE, LocalDateTypeHandler.class);
		typeHandlerRegistry.register(LocalDateTime.class, JdbcType.TIMESTAMP, LocalDateTimeTypeHandler.class);
		typeHandlerRegistry.register(Instant.class, JdbcType.TIMESTAMP, InstantTypeHandler.class);
		
		sessionFactory.setConfiguration(configuration);
		
		sessionFactory.setPlugins(new ResetDirtyInterceptor());
		
		return sessionFactory;
	}

	@Bean
	public static MapperScannerConfigurer mapperScannerConfigurer() {
		MapperScannerConfigurer configurer = new MapperScannerConfigurer();
		configurer.setBasePackage("ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis.mapper");
		configurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
		return configurer;
	}


	@Bean
	public PerilCodeDao perilCodeDao() { 
		return new PerilCodeDaoImpl(); 
	}

	@Bean
	public CalculationStatusCodeDao calculationStatusCodeDao() { 
		return new CalculationStatusCodeDaoImpl(); 
	}

	@Bean
	public ClaimCalculationDao claimCalculationDao() { 
		return new ClaimCalculationDaoImpl(); 
	}

	@Bean
	public ClaimCalculationVarietyDao claimCalculationVarietyDao() { 
		return new ClaimCalculationVarietyDaoImpl(); 
	}

	@Bean
	public ClaimCalculationBerriesDao claimCalculationBerriesDao() { 
		return new ClaimCalculationBerriesDaoImpl(); 
	}

	@Bean
	public ClaimCalculationPlantUnitsDao claimCalculationPlantUnitsDao() { 
		return new ClaimCalculationPlantUnitsDaoImpl(); 
	}

	@Bean
	public ClaimCalculationPlantAcresDao claimCalculationPlantAcresDao() { 
		return new ClaimCalculationPlantAcresDaoImpl(); 
	}

	@Bean
	public ClaimCalculationGrapesDao claimCalculationGrapesDao() { 
		return new ClaimCalculationGrapesDaoImpl(); 
	}

	@Bean
	public ClaimCalculationGrainUnseededDao claimCalculationGrainUnseededDao() { 
		return new ClaimCalculationGrainUnseededDaoImpl(); 
	}

	@Bean
	public ClaimCalculationGrainSpotLossDao claimCalculationGrainSpotLossDao() { 
		return new ClaimCalculationGrainSpotLossDaoImpl(); 
	}

	@Bean
	public ClaimCalculationGrainQuantityDao claimCalculationGrainQuantityDao() { 
		return new ClaimCalculationGrainQuantityDaoImpl();
	}
	
	@Bean
	public ClaimCalculationUserDao claimCalculationUserDao() { 
		return new ClaimCalculationUserDaoImpl(); 
	}

	@Bean
	public ClaimDao claimDao() { 
		return new ClaimDaoImpl(); 
	}

	@Bean
	public CropCommodityDao cropCommodityDao() { 
		return new CropCommodityDaoImpl(); 
	}

	@Bean
	public CropVarietyDao cropVarietyDao() { 
		return new CropVarietyDaoImpl(); 
	}
	
	@Bean
	public CoveragePerilDao coveragePerilDao() { 
		return new CoveragePerilDaoImpl(); 
	}
	
	@Bean
	public InsurancePlanDao insurancePlanDao() { 
		return new InsurancePlanDaoImpl(); 
	}

	@Bean
	public ClaimStatusCodeDao claimStatusCodeDao() { 
		return new ClaimStatusCodeDaoImpl(); 
	}

	@Bean
	public CommodityCoverageCodeDao commodityCoverageCodeDao() { 
		return new CommodityCoverageCodeDaoImpl(); 
	}

}
