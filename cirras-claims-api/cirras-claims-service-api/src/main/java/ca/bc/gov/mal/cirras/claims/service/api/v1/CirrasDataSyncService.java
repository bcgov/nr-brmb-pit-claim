package ca.bc.gov.mal.cirras.claims.service.api.v1;

import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.SyncClaimRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.SyncCodeRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.SyncCommodityVarietyRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.SyncCoveragePerilRsrc;
import ca.bc.gov.mal.cirras.claims.model.v1.SyncClaim;
import ca.bc.gov.mal.cirras.claims.model.v1.SyncCode;
import ca.bc.gov.mal.cirras.claims.model.v1.SyncCommodityVariety;
import ca.bc.gov.mal.cirras.claims.model.v1.SyncCoveragePeril;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationDto;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.InsuranceClaimRsrc;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculation;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.service.api.ConflictException;
import ca.bc.gov.nrs.wfone.common.service.api.ForbiddenException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.ValidationFailureException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

public interface CirrasDataSyncService {

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	SyncClaim getSyncClaim(
		Integer colId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	SyncClaim createSyncClaim(
		SyncClaim syncClaim, 
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, ForbiddenException, ConflictException, ValidationFailureException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	SyncClaim updateSyncClaim(
		SyncClaim syncClaim, 
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, ForbiddenException, ConflictException, ValidationFailureException, DaoException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void deleteSyncClaim(
		Integer colId,
		String optimisticLock, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, ForbiddenException, ConflictException;
	
	public Boolean syncClaimData(
			ClaimCalculation claimCalculation, 
			InsuranceClaimRsrc policyClaim, 
			ClaimCalculationDto dto,
			String claimCalculationGuid,
			FactoryContext factoryContext, 
			WebAdeAuthentication authentication 
		) throws ServiceException, DaoException, NotFoundException;

	void updateSyncClaimRelatedData(SyncClaimRsrc resource, FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication);

	////////////////////////////////////////////////////////////////////
	//Commodity Variety Sync
	////////////////////////////////////////////////////////////////////
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	SyncCommodityVariety getSyncCommodityVariety(
		Integer crptId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void synchronizeCommodityVariety (SyncCommodityVarietyRsrc resource, FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication) throws ServiceException, NotFoundException, DaoException;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void deleteSyncCommodityVariety(
		Integer crptId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, DaoException;

	////////////////////////////////////////////////////////////////////
	//Generic Code Tables
	////////////////////////////////////////////////////////////////////
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	SyncCode getSyncCode(
		String codeTableType, 
		String uniqueKey,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, DaoException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void synchronizeCode (SyncCodeRsrc resource, FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication) throws ServiceException, NotFoundException, DaoException;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void deleteSyncCode(
		String codeTableType, 
		String uniqueKey,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, DaoException;

	////////////////////////////////////////////////////////////////////
	//Coverage Peril
	////////////////////////////////////////////////////////////////////
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	SyncCoveragePeril getSyncCoveragePeril(
		Integer coveragePerilId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, DaoException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void synchronizeCoveragePeril (SyncCoveragePerilRsrc resource, FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication) throws ServiceException, NotFoundException, DaoException;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void deleteSyncCoveragePeril(
		Integer coveragePerilId,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, DaoException;

}
