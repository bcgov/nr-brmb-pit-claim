package ca.bc.gov.mal.cirras.claims.spring;

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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimCalculationDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimCalculationGrainBasketDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimCalculationGrainBasketProductDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimCalculationGrainQuantityDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimCalculationGrainQuantityDetailDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimCalculationGrainSpotLossDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimCalculationGrainUnseededDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimCalculationPlantAcresDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimCalculationPlantUnitsDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimCalculationGrapesDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimCalculationUserDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimCalculationVarietyDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimStatusCodeDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.CommodityCoverageCodeDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.CoveragePerilDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.CropCommodityDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.CropVarietyDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.InsurancePlanDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.PerilCodeDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.CalculationStatusCodeDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimCalculationBerriesDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimCalculationDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimCalculationGrainBasketDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimCalculationGrainBasketProductDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimCalculationGrainQuantityDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimCalculationGrainQuantityDetailDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimCalculationGrainSpotLossDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimCalculationGrainUnseededDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimCalculationPlantAcresDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimCalculationPlantUnitsDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimCalculationGrapesDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimCalculationUserDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimCalculationVarietyDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimStatusCodeDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.CommodityCoverageCodeDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.CoveragePerilDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.CropCommodityDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.CropVarietyDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.InsurancePlanDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimCalculationBerriesDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.PerilCodeDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.CalculationStatusCodeDao;
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
	public PlatformTransactionManager transactionManager(@Qualifier("cirrasClaimsDataSource") DataSource cirrasClaimsDataSource) {
		return new DataSourceTransactionManager(cirrasClaimsDataSource);
	}

	@Bean
	public SqlSessionFactoryBean sqlSessionFactory(@Qualifier("cirrasClaimsDataSource") DataSource cirrasClaimsDataSource) {
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
		configurer.setBasePackage("ca.bc.gov.mal.cirras.claims.data.repositories.mapper");
		configurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
		return configurer;
	}


	@Bean
	public PerilCodeDao perilCodeDao() { 
		return new PerilCodeDao(); 
	}

	@Bean
	public CalculationStatusCodeDao calculationStatusCodeDao() { 
		return new CalculationStatusCodeDao(); 
	}

	@Bean
	public ClaimCalculationDao claimCalculationDao() { 
		return new ClaimCalculationDao(); 
	}

	@Bean
	public ClaimCalculationVarietyDao claimCalculationVarietyDao() { 
		return new ClaimCalculationVarietyDao(); 
	}

	@Bean
	public ClaimCalculationBerriesDao claimCalculationBerriesDao() { 
		return new ClaimCalculationBerriesDao(); 
	}

	@Bean
	public ClaimCalculationPlantUnitsDao claimCalculationPlantUnitsDao() { 
		return new ClaimCalculationPlantUnitsDao(); 
	}

	@Bean
	public ClaimCalculationPlantAcresDao claimCalculationPlantAcresDao() { 
		return new ClaimCalculationPlantAcresDao(); 
	}

	@Bean
	public ClaimCalculationGrapesDao claimCalculationGrapesDao() { 
		return new ClaimCalculationGrapesDao(); 
	}

	@Bean
	public ClaimCalculationGrainUnseededDao claimCalculationGrainUnseededDao() { 
		return new ClaimCalculationGrainUnseededDao(); 
	}

	@Bean
	public ClaimCalculationGrainSpotLossDao claimCalculationGrainSpotLossDao() { 
		return new ClaimCalculationGrainSpotLossDao(); 
	}

	@Bean
	public ClaimCalculationGrainQuantityDao claimCalculationGrainQuantityDao() { 
		return new ClaimCalculationGrainQuantityDao();
	}

	@Bean
	public ClaimCalculationGrainQuantityDetailDao claimCalculationGrainQuantityDetailDao() { 
		return new ClaimCalculationGrainQuantityDetailDao();
	}

	@Bean
	public ClaimCalculationGrainBasketDao claimCalculationGrainBasketDao() { 
		return new ClaimCalculationGrainBasketDao(); 
	}

	@Bean
	public ClaimCalculationGrainBasketProductDao claimCalculationGrainBasketProductDao() { 
		return new ClaimCalculationGrainBasketProductDao(); 
	}
	
	@Bean
	public ClaimCalculationUserDao claimCalculationUserDao() { 
		return new ClaimCalculationUserDao(); 
	}

	@Bean
	public ClaimDao claimDao() { 
		return new ClaimDao(); 
	}

	@Bean
	public CropCommodityDao cropCommodityDao() { 
		return new CropCommodityDao(); 
	}

	@Bean
	public CropVarietyDao cropVarietyDao() { 
		return new CropVarietyDao(); 
	}
	
	@Bean
	public CoveragePerilDao coveragePerilDao() { 
		return new CoveragePerilDao(); 
	}
	
	@Bean
	public InsurancePlanDao insurancePlanDao() { 
		return new InsurancePlanDao(); 
	}

	@Bean
	public ClaimStatusCodeDao claimStatusCodeDao() { 
		return new ClaimStatusCodeDao(); 
	}

	@Bean
	public CommodityCoverageCodeDao commodityCoverageCodeDao() { 
		return new CommodityCoverageCodeDao(); 
	}

}
