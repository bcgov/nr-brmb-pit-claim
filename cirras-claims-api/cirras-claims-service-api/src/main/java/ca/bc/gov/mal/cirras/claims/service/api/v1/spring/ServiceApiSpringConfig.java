package ca.bc.gov.mal.cirras.claims.service.api.v1.spring;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;

import ca.bc.gov.mal.cirras.claims.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.mal.cirras.claims.service.api.v1.CirrasClaimService;
import ca.bc.gov.mal.cirras.claims.service.api.v1.CirrasDataSyncService;
import ca.bc.gov.mal.cirras.claims.service.api.v1.impl.CirrasClaimServiceImpl;
import ca.bc.gov.mal.cirras.claims.service.api.v1.impl.CirrasDataSyncServiceImpl;
import ca.bc.gov.mal.cirras.policies.api.rest.client.v1.CirrasPolicyService;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.claims.service.api.v1.model.factory.ClaimFactory;
import ca.bc.gov.mal.cirras.claims.service.api.v1.model.factory.CirrasDataSyncFactory;
import ca.bc.gov.mal.cirras.claims.service.api.v1.util.CirrasServiceHelper;
import ca.bc.gov.mal.cirras.claims.service.api.v1.util.OutOfSync;

import ca.bc.gov.mal.cirras.claims.service.api.v1.model.factory.ClaimCalculationFactory;
import ca.bc.gov.mal.cirras.claims.service.api.v1.validation.ModelValidator;

@Configuration
@Import({
	CodeHierarchySpringConfig.class,  // can't remove this because some wfone stuff depends on it
	CodeTableSpringConfig.class, 
	PersistenceSpringConfig.class
})
public class ServiceApiSpringConfig {

	private static final Logger logger = LoggerFactory.getLogger(ServiceApiSpringConfig.class);

	public ServiceApiSpringConfig() {
		logger.debug("<ServiceApiSpringConfig");
		
		logger.debug(">ServiceApiSpringConfig");
	}

	// Beans provided by EndpointsSpringConfig
	@Autowired ResourceBundleMessageSource messageSource;
	@Autowired Properties applicationProperties;
	@Autowired CirrasPolicyService cirrasPolicyService;
	/* TODO: @Autowired */ CirrasUnderwritingService cirrasUnderwritingService;
	@Autowired CirrasDataSyncFactory cirrasDataSyncFactory; 
	
	
    // Beans provided by ResourceFactorySpringConfig
	@Autowired ClaimFactory claimFactory;
	@Autowired ClaimCalculationFactory claimCalculationFactory;
	
	// Imported Spring Config
	@Autowired CodeTableSpringConfig codeTableSpringConfig;
	@Autowired CodeHierarchySpringConfig codeHierarchySpringConfig;
	@Autowired PersistenceSpringConfig persistenceSpringConfig;

	@Bean
	public ModelValidator modelValidator() {
		ModelValidator result;
		
		result = new ModelValidator();
		result.setCachedCodeTables(codeTableSpringConfig.cachedCodeTables());
		result.setMessageSource(messageSource);
		
		return result;
	}
	
	@Bean
	public CirrasServiceHelper cirrasServiceHelper() {
		CirrasServiceHelper result = new CirrasServiceHelper();
		
		result.setClaimCalculationDao(persistenceSpringConfig.claimCalculationDao());
		result.setClaimCalculationVarietyDao(persistenceSpringConfig.claimCalculationVarietyDao());
		result.setClaimCalculationBerriesDao(persistenceSpringConfig.claimCalculationBerriesDao());
		result.setClaimCalculationPlantUnitsDao(persistenceSpringConfig.claimCalculationPlantUnitsDao());
		result.setClaimCalculationPlantAcresDao(persistenceSpringConfig.claimCalculationPlantAcresDao());
		result.setClaimCalculationGrapesDao(persistenceSpringConfig.claimCalculationGrapesDao());
		result.setClaimCalculationGrainUnseededDao(persistenceSpringConfig.claimCalculationGrainUnseededDao());
		result.setClaimCalculationGrainSpotLossDao(persistenceSpringConfig.claimCalculationGrainSpotLossDao());
		result.setClaimCalculationUserDao(persistenceSpringConfig.claimCalculationUserDao());
		
		return result;
	}
	
	@Bean
	public OutOfSync outOfSync() {
		OutOfSync result = new OutOfSync();
		return result;
	}

	@Bean()
	public CirrasClaimService cirrasClaimService() {
		CirrasClaimServiceImpl result;
		
		result = new CirrasClaimServiceImpl();
		result.setModelValidator(modelValidator());
		result.setApplicationProperties(applicationProperties);

		result.setClaimFactory(claimFactory);

		result.setClaimCalculationFactory(claimCalculationFactory);
		
		result.setClaimCalculationDao(persistenceSpringConfig.claimCalculationDao());
		result.setClaimCalculationVarietyDao(persistenceSpringConfig.claimCalculationVarietyDao());
		result.setClaimCalculationBerriesDao(persistenceSpringConfig.claimCalculationBerriesDao());
		result.setClaimCalculationPlantUnitsDao(persistenceSpringConfig.claimCalculationPlantUnitsDao());
		result.setClaimCalculationPlantAcresDao(persistenceSpringConfig.claimCalculationPlantAcresDao());
		result.setClaimCalculationGrapesDao(persistenceSpringConfig.claimCalculationGrapesDao());
		result.setClaimCalculationGrainUnseededDao(persistenceSpringConfig.claimCalculationGrainUnseededDao());
		result.setClaimCalculationGrainSpotLossDao(persistenceSpringConfig.claimCalculationGrainSpotLossDao());
		result.setClaimCalculationGrainQuantityDao(persistenceSpringConfig.claimCalculationGrainQuantityDao());
		result.setClaimCalculationGrainQuantityDetailDao(persistenceSpringConfig.claimCalculationGrainQuantityDetailDao());
		result.setClaimCalculationUserDao(persistenceSpringConfig.claimCalculationUserDao());
		result.setClaimDao(persistenceSpringConfig.claimDao());
		
		result.setCirrasPolicyService(cirrasPolicyService);
		result.setCirrasUnderwritingService(cirrasUnderwritingService);

		result.setCirrasDataSyncService(cirrasDataSyncService());
		result.setCirrasServiceHelper(cirrasServiceHelper());
		
		result.setOutOfSync(outOfSync());
		
		return result;
	}
	
	@Bean()
	public CirrasDataSyncService cirrasDataSyncService() {
		CirrasDataSyncServiceImpl result;
		
		result = new CirrasDataSyncServiceImpl();
		
		result.setModelValidator(modelValidator());
		result.setApplicationProperties(applicationProperties);

		result.setCirrasDataSyncFactory(cirrasDataSyncFactory);
		result.setClaimCalculationFactory(claimCalculationFactory);
		
		result.setClaimDao(persistenceSpringConfig.claimDao());
		result.setClaimCalculationDao(persistenceSpringConfig.claimCalculationDao());
		result.setCropCommodityDao(persistenceSpringConfig.cropCommodityDao());
		result.setCropVarietyDao(persistenceSpringConfig.cropVarietyDao());
		result.setPerilCodeDao(persistenceSpringConfig.perilCodeDao());
		result.setCoveragePerilDao(persistenceSpringConfig.coveragePerilDao());
		result.setInsurancePlanDao(persistenceSpringConfig.insurancePlanDao());
		result.setClaimStatusCodeDao(persistenceSpringConfig.claimStatusCodeDao());
		result.setCommodityCoverageCodeDao(persistenceSpringConfig.commodityCoverageCodeDao());
		
		result.setCirrasServiceHelper(cirrasServiceHelper());
		
		return result;
	}
	
}
