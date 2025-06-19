package ca.bc.gov.mal.cirras.claims.api.rest.client.v1;

import java.time.LocalDate;

import ca.bc.gov.mal.cirras.claims.api.rest.client.v1.ValidationException;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.EndpointsRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.SyncClaimRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.SyncCodeRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.SyncCommodityVarietyRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.SyncCoveragePerilRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.ClaimListRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.ClaimRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.ClaimCalculationListRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.ClaimCalculationRsrc;
import ca.bc.gov.nrs.common.wfone.rest.resource.CodeTableListRsrc;
import ca.bc.gov.nrs.common.wfone.rest.resource.CodeTableRsrc;
import ca.bc.gov.nrs.common.wfone.rest.resource.HealthCheckResponseRsrc;
import ca.bc.gov.nrs.wfone.common.rest.client.RestClientServiceException;

public interface CirrasClaimService {
	
	EndpointsRsrc getTopLevelEndpoints() throws CirrasClaimServiceException;

	String getSwaggerString() throws RestClientServiceException;

	HealthCheckResponseRsrc getHealthCheck(String callstack) throws RestClientServiceException;

	CodeTableListRsrc getCodeTables(
			EndpointsRsrc parent,
			String codeTableName, 
			LocalDate effectiveAsOfDate) throws CirrasClaimServiceException;

	CodeTableRsrc getCodeTable(CodeTableRsrc codeTable, LocalDate effectiveAsOfDate) throws CirrasClaimServiceException;

	CodeTableRsrc updateCodeTable(CodeTableRsrc codeTable) throws CirrasClaimServiceException, ValidationException;
	
	ClaimListRsrc getClaimList(
			EndpointsRsrc parent,
			String claimNumber, 
			String policyNumber, 
			String calculationStatusCode,
			String sortColumn,
			String sortDirection,
		    Integer pageNumber,
			Integer pageRowCount
		)	
		throws CirrasClaimServiceException;

	
	ClaimCalculationRsrc getClaim(
			ClaimRsrc resource
			)throws CirrasClaimServiceException;

	
	ClaimCalculationListRsrc getClaimCalculations(
			EndpointsRsrc parent,
			String claimNumber, 
			String policyNumber, 
			String cropYear,
			String calculationStatusCode,
			String createClaimCalcUserGuid,
			String updateClaimCalcUserGuid,
			String insurancePlanId,
			String sortColumn,
			String sortDirection,
			Integer pageNumber, 
			Integer pageRowCount
		) 
		throws CirrasClaimServiceException;
		
		ClaimCalculationRsrc createClaimCalculation(ClaimCalculationRsrc resource) throws CirrasClaimServiceException, ValidationException;
		ClaimCalculationRsrc getClaimCalculation(ClaimCalculationRsrc resource, Boolean doRefreshManualClaimData) throws CirrasClaimServiceException;
		ClaimCalculationRsrc updateClaimCalculation(ClaimCalculationRsrc resource, String updateType) throws CirrasClaimServiceException, ValidationException;
		void deleteClaimCalculation(ClaimCalculationRsrc resource, Boolean doDeleteLinkedCalculations) throws CirrasClaimServiceException;
		
		//////////////////////////////////////////////////////
		//DATA SYNC METHODS
		//////////////////////////////////////////////////////
		SyncClaimRsrc createSyncClaim(SyncClaimRsrc resource) throws CirrasClaimServiceException, ValidationException;
		SyncClaimRsrc getSyncClaim(EndpointsRsrc parent, String colId) throws CirrasClaimServiceException;
		SyncClaimRsrc updateSyncClaim(SyncClaimRsrc resource) throws CirrasClaimServiceException, ValidationException;
		void deleteSyncClaim(SyncClaimRsrc resource) throws CirrasClaimServiceException;
		
		void updateSyncClaimRelatedData(SyncClaimRsrc resource) throws CirrasClaimServiceException, ValidationException;
		
		//Commodity and Variety
		void synchronizeCommodityVariety(SyncCommodityVarietyRsrc resource) throws CirrasClaimServiceException, ValidationException;
		void deleteSyncCommodityVariety(EndpointsRsrc parent, String crptId) throws CirrasClaimServiceException;

		//Generic Code sync
		void synchronizeCode(SyncCodeRsrc resource) throws CirrasClaimServiceException, ValidationException;
		void deleteSyncCode(EndpointsRsrc parent, String codeTableType, String uniqueKey) throws CirrasClaimServiceException;

		//Coverage Peril
		void synchronizeCoveragePeril(SyncCoveragePerilRsrc resource) throws CirrasClaimServiceException, ValidationException;
		void deleteSyncCoveragePeril(EndpointsRsrc parent, String coveragePerilId) throws CirrasClaimServiceException;
}
