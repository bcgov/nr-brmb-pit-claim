package ca.bc.gov.mal.cirras.claims.spring;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;

import ca.bc.gov.mal.cirras.claims.services.utils.CachedCodeTables;
import ca.bc.gov.nrs.wfone.common.persistence.code.dao.CodeTableConfig;
import ca.bc.gov.nrs.wfone.common.persistence.code.spring.CodePersistenceSpringConfig;


@Configuration
@Import({
	CodePersistenceSpringConfig.class
})
public class CodeTableSpringConfig {

	private static final Logger logger = LoggerFactory.getLogger(CodeTableSpringConfig.class);
	
	public CodeTableSpringConfig() {
		logger.debug("<CodeTableSpringConfig");
		
		logger.debug(">CodeTableSpringConfig");
	}

	// Beans provided by EndpointsSpringConfig
	@Autowired ResourceBundleMessageSource messageSource;
	
	// Imported Spring Config
	@Autowired CodePersistenceSpringConfig codePersistenceSpringConfig;
	
	@Bean
	public CachedCodeTables cachedCodeTables() {
		CachedCodeTables result;
		
		result = new CachedCodeTables();
		result.setCodeTableDao(codePersistenceSpringConfig.codeTableDao());
		result.setCodeTableConfigs(codeTableConfigs());
		
		return result;
	}

	@Bean
	public List<CodeTableConfig> codeTableConfigs() {
		List<CodeTableConfig> result = new ArrayList<>();

		result.add(claimStatusCodeTableConfig());
		result.add(perilCodeTableConfig());
		result.add(commodityCoverageCodeTableConfig());
		result.add(calculationStatusCodeTableConfig());
		result.add(claimCalcCreateUserCodeTableConfig());
		result.add(claimCalcUpdateUserCodeTableConfig());
		result.add(claimCalcCropYearCodeTableConfig());
		result.add(insurancePlanTableConfig());

		return result;
	}	
			
	@Bean
	public CodeTableConfig claimStatusCodeTableConfig() {
		return createCodeTableConfig("CLAIM_STATUS_CODE");
	}
		
	@Bean
	public CodeTableConfig perilCodeTableConfig() {
		return createCodeTableConfig("PERIL_CODE");
	}

	@Bean
	public CodeTableConfig commodityCoverageCodeTableConfig() {
		return createCodeTableConfig("COMMODITY_COVERAGE_CODE");
	}

	@Bean
	public CodeTableConfig calculationStatusCodeTableConfig() {
		return createCodeTableConfig("CALCULATION_STATUS_CODE");
	}

	@Bean
	public CodeTableConfig claimCalcCreateUserCodeTableConfig() {
		String fetchSql = "SELECT DISTINCT ccu.claim_calculation_user_guid AS CODE, " + 
						  "                CASE WHEN ccu.given_name IS NOT NULL AND ccu.family_name IS NOT NULL THEN ccu.given_name || ' ' || ccu.family_name " + 
						  "                     ELSE coalesce(coalesce(ccu.given_name, ccu.family_name), 'Unknown') " + 
						  "                END AS DESCRIPTION " + 
						  "FROM CLAIM_CALCULATION cc " + 
						  "JOIN CLAIM_CALCULATION_USER ccu ON ccu.claim_calculation_user_guid = cc.create_claim_calc_user_guid";

		return createVirtualCodeTableConfig("CLAIM_CALC_CREATE_USER", fetchSql);
	}

	@Bean
	public CodeTableConfig claimCalcUpdateUserCodeTableConfig() {
		String fetchSql = "SELECT DISTINCT ccu.claim_calculation_user_guid AS CODE, " + 
						  "                CASE WHEN ccu.given_name IS NOT NULL AND ccu.family_name IS NOT NULL THEN ccu.given_name || ' ' || ccu.family_name " + 
						  "                     ELSE coalesce(coalesce(ccu.given_name, ccu.family_name), 'Unknown') " + 
						  "                END AS DESCRIPTION " + 
						  "FROM CLAIM_CALCULATION cc " + 
						  "JOIN CLAIM_CALCULATION_USER ccu ON ccu.claim_calculation_user_guid = cc.update_claim_calc_user_guid";

		return createVirtualCodeTableConfig("CLAIM_CALC_UPDATE_USER", fetchSql);
	}

	@Bean
	public CodeTableConfig claimCalcCropYearCodeTableConfig() {
		String fetchSql = "SELECT DISTINCT cast(T.CROP_YEAR as text) CODE, cast(T.CROP_YEAR as text) DESCRIPTION FROM CLAIM_CALCULATION T";

		return createVirtualCodeTableConfig("CLAIM_CALC_CROP_YEAR", fetchSql);
	}
	

	@Bean
	public CodeTableConfig insurancePlanTableConfig() {
		String fetchSql = "SELECT cast(T.INSURANCE_PLAN_ID as text) AS CODE, T.INSURANCE_PLAN_NAME AS DESCRIPTION FROM INSURANCE_PLAN T";

		return createVirtualCodeTableConfig("INSURANCE_PLAN", fetchSql);
	}
	
	
	private CodeTableConfig createCodeTableConfig(String tableName) {
		CodeTableConfig result = new CodeTableConfig();
		
		String fetchSql = String.format("SELECT T.%s CODE, T.DESCRIPTION, NULL DISPLAY_ORDER, T.EFFECTIVE_DATE, T.EXPIRY_DATE, T.CREATE_USER, T.CREATE_DATE, T.UPDATE_USER, T.UPDATE_DATE FROM %s T ORDER BY T.DESCRIPTION", tableName, tableName);
				
		//result.setReadScope("CIRRAS_CLAIMS.GET_CODE_TABLES");
		result.setUseRevisionCount(Boolean.FALSE);
		result.setCodeTableDao(codePersistenceSpringConfig.codeTableDao());
		result.setCodeTableName(tableName);
		result.setFetchSql(fetchSql);
		
		return result;
	}

	//Creates a CodeTableConfig, but using a source query that returns CODE and DESCRIPTION that is wrapped into a 
	//format expected by the CodeTablesService, so that it can be accessed as though it were a code table.
	private CodeTableConfig createVirtualCodeTableConfig(String tableName, String innerSql) {
		CodeTableConfig result = new CodeTableConfig();

		String fetchSql = String.format("SELECT V.CODE, " + 
				                        "       V.DESCRIPTION, " + 
				                        "       NULL DISPLAY_ORDER, " + 
				                        "       to_date('2020-01-01','yyyy-mm-dd') EFFECTIVE_DATE, " + 
				                        "       to_date('9999-12-31','yyyy-mm-dd') EXPIRY_DATE, " + 
				                        "       'DEFAULT_USERID' CREATE_USER, " + 
				                        "       current_timestamp CREATE_DATE, " + 
				                        "       'DEFAULT_USERID' UPDATE_USER, " + 
				                        "       current_timestamp UPDATE_DATE " + 
				                        "FROM (%s) V " + 
				                        "ORDER BY V.DESCRIPTION", 
				                        innerSql);
		
		//result.setReadScope("CIRRAS_CLAIMS.GET_CODE_TABLES");
		result.setUseRevisionCount(Boolean.FALSE);
		result.setCodeTableDao(codePersistenceSpringConfig.codeTableDao());
		result.setCodeTableName(tableName);
		result.setFetchSql(fetchSql);
		
		return result;
	}

}
