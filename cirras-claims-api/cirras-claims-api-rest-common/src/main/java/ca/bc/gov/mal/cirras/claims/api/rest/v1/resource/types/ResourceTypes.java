package ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.types;

import ca.bc.gov.nrs.common.wfone.rest.resource.types.BaseResourceTypes;

public class ResourceTypes extends BaseResourceTypes {
	
	public static final String NAMESPACE = "http://claims.cirras.mal.gov.bc.ca/v1/";

	public static final String ENDPOINTS_NAME = "endpoints";
	public static final String ENDPOINTS = NAMESPACE + ENDPOINTS_NAME;
	

	public static final String CLAIM_CALCULATION_LIST_NAME = "claimCalculationList";
	public static final String CLAIM_CALCULATION_LIST = NAMESPACE + CLAIM_CALCULATION_LIST_NAME;

	public static final String CLAIM_CALCULATION_NAME = "ClaimCalculation";
	public static final String CLAIM_CALCULATION = NAMESPACE + CLAIM_CALCULATION_NAME;
	public static final String CREATE_CLAIM_CALCULATION = NAMESPACE + "createClaimCalculation";
	public static final String UPDATE_CLAIM_CALCULATION = NAMESPACE + "updateClaimCalculation";
	public static final String DELETE_CLAIM_CALCULATION = NAMESPACE + "deleteClaimCalculation";

	public static final String CLAIM_LIST_NAME = "claimList";
	public static final String CLAIM_LIST = NAMESPACE + CLAIM_LIST_NAME;
	public static final String CLAIM_NAME = "claim";
	public static final String CLAIM = NAMESPACE + CLAIM_NAME;
	
	public static final String SYNC_CLAIM_NAME = "syncClaim";
	public static final String SYNC_CLAIM = NAMESPACE + SYNC_CLAIM_NAME;
	public static final String CREATE_SYNC_CLAIM = NAMESPACE + "createSyncClaim";
	public static final String UPDATE_SYNC_CLAIM = NAMESPACE + "updateSyncClaim";
	public static final String DELETE_SYNC_CLAIM = NAMESPACE + "deleteSyncClaim";

	public static final String UPDATE_SYNC_CLAIM_RELATED_DATA = NAMESPACE + "updateSyncClaimRelatedData";

	//Commodity Variety
	public static final String SYNC_COMMODITY_VARIETY_NAME = "syncCommodityVariety";
	public static final String SYNC_COMMODITY_VARIETY = NAMESPACE + SYNC_COMMODITY_VARIETY_NAME;
	public static final String SYNCHRONIZE_COMMODITY_VARIETY = NAMESPACE + "SynchronizeCommodityVariety";
	public static final String DELETE_SYNC_COMMODITY_VARIETY = NAMESPACE + "deleteSyncCommodityVariety";

	//Generic Code Tables
	public static final String SYNC_CODE_NAME = "syncCode";
	public static final String SYNC_CODE = NAMESPACE + SYNC_CODE_NAME;
	public static final String SYNCHRONIZE_CODE = NAMESPACE + "SynchronizeCode";
	public static final String DELETE_SYNC_CODE = NAMESPACE + "deleteSyncCode";

	//Coverage Peril
	public static final String SYNC_COVERAGE_PERIL_NAME = "syncCoveragePeril";
	public static final String SYNC_COVERAGE_PERIL = NAMESPACE + SYNC_COVERAGE_PERIL_NAME;
	public static final String SYNCHRONIZE_COVERAGE_PERIL = NAMESPACE + "synchronizeCoveragePeril";
	public static final String DELETE_SYNC_COVERAGE_PERIL = NAMESPACE + "deleteCoveragePeril";

}

