package ca.bc.gov.mal.cirras.claims.spring;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;

import ca.bc.gov.mal.cirras.claims.services.CirrasClaimService;
import ca.bc.gov.mal.cirras.claims.services.CirrasDataSyncService;
import ca.bc.gov.mal.cirras.policies.api.rest.client.v1.CirrasPolicyService;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.claims.data.assemblers.ClaimRsrcFactory;
import ca.bc.gov.mal.cirras.claims.data.assemblers.CirrasDataSyncRsrcFactory;
import ca.bc.gov.mal.cirras.claims.services.utils.CirrasServiceHelper;
import ca.bc.gov.mal.cirras.claims.services.utils.OutOfSync;

import ca.bc.gov.mal.cirras.claims.data.assemblers.ClaimCalculationRsrcFactory;

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
	@Autowired CirrasUnderwritingService cirrasUnderwritingService;
	@Autowired CirrasDataSyncRsrcFactory cirrasDataSyncRsrcFactory; 
	
	
    // Beans provided by ResourceFactorySpringConfig
	@Autowired ClaimRsrcFactory claimRsrcFactory;
	@Autowired ClaimCalculationRsrcFactory claimCalculationRsrcFactory;
	
	// Imported Spring Config
	@Autowired CodeTableSpringConfig codeTableSpringConfig;
	@Autowired CodeHierarchySpringConfig codeHierarchySpringConfig;
	@Autowired PersistenceSpringConfig persistenceSpringConfig;
	
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
		result.setClaimCalculationGrainQuantityDao(persistenceSpringConfig.claimCalculationGrainQuantityDao());
		result.setClaimCalculationGrainQuantityDetailDao(persistenceSpringConfig.claimCalculationGrainQuantityDetailDao());
		result.setClaimCalculationGrainBasketDao(persistenceSpringConfig.claimCalculationGrainBasketDao());
		result.setClaimCalculationGrainBasketProductDao(persistenceSpringConfig.claimCalculationGrainBasketProductDao());
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
		CirrasClaimService result;
		
		result = new CirrasClaimService();
		result.setApplicationProperties(applicationProperties);

		result.setClaimRsrcFactory(claimRsrcFactory);
		result.setClaimCalculationRsrcFactory(claimCalculationRsrcFactory);
		
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
		result.setClaimCalculationGrainBasketDao(persistenceSpringConfig.claimCalculationGrainBasketDao());
		result.setClaimCalculationGrainBasketProductDao(persistenceSpringConfig.claimCalculationGrainBasketProductDao());
		result.setClaimDao(persistenceSpringConfig.claimDao());
		result.setCropCommodityDao(persistenceSpringConfig.cropCommodityDao());
		
		result.setCirrasPolicyService(cirrasPolicyService);
		result.setCirrasUnderwritingService(cirrasUnderwritingService);

		result.setCirrasDataSyncService(cirrasDataSyncService());
		result.setCirrasServiceHelper(cirrasServiceHelper());
		
		result.setOutOfSync(outOfSync());
		
		return result;
	}
	
	@Bean()
	public CirrasDataSyncService cirrasDataSyncService() {
		CirrasDataSyncService result;
		
		result = new CirrasDataSyncService();
		
		result.setApplicationProperties(applicationProperties);

		result.setCirrasDataSyncRsrcFactory(cirrasDataSyncRsrcFactory);
		result.setClaimCalculationRsrcFactory(claimCalculationRsrcFactory);
		
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
