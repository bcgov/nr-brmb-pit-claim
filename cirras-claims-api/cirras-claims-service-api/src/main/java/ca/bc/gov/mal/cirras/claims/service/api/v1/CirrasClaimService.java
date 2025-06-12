package ca.bc.gov.mal.cirras.claims.service.api.v1;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.mal.cirras.claims.model.v1.Claim;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimList;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationDto;
import ca.bc.gov.mal.cirras.policies.api.rest.client.v1.CirrasPolicyServiceException;
import ca.bc.gov.mal.cirras.policies.api.rest.client.v1.ValidationException;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculation;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationList;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.TooManyRecordsException;
import ca.bc.gov.nrs.wfone.common.service.api.ConflictException;
import ca.bc.gov.nrs.wfone.common.service.api.ForbiddenException;
import ca.bc.gov.nrs.wfone.common.service.api.MaxResultsExceededException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.ValidationFailureException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

public interface CirrasClaimService {

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	ClaimCalculation createClaimCalculation(
		ClaimCalculation claimCalculation,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	) 
	throws ServiceException, NotFoundException, ValidationFailureException;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	ClaimCalculation getClaimCalculation(
		String claimCalculationGuid, 
		Boolean doRefreshManualClaimData,
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	ClaimCalculation updateClaimCalculation(
		String claimCalculationGuid, 
		String updateType,
		String optimisticLock,
		ClaimCalculation claimCalculation, 
		FactoryContext factoryContext, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, ForbiddenException, ConflictException, ValidationFailureException, ValidationException;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	void deleteClaimCalculation(
		String claimCalculationGuid, 
		Boolean doDeleteLinkedCalculations,
		String optimisticLock, 
		WebAdeAuthentication authentication
	)
	throws ServiceException, NotFoundException, ForbiddenException, ConflictException;
	
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	ClaimCalculationList<? extends ClaimCalculation> getClaimCalculationList(
		Integer claimNumber,
		String policyNumber,
		Integer cropYear,
		String calculationStatusCode,
		String createClaimCalcUserGuid,
		String updateClaimCalcUserGuid,
		Integer insurancePlanId,
		String sortColumn,
		String sortDirection,
	    Integer pageNumber,
		Integer pageRowCount,
		FactoryContext context, 
		WebAdeAuthentication authentication
	) 
	throws ServiceException, MaxResultsExceededException;	
	
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	ClaimList<? extends Claim> getClaimList(
		Integer claimNumber, 
		String policyNumber, 
		String calculationStatusCode,
		String sortColumn,
		String sortDirection,
	    Integer pageNumber,
		Integer pageRowCount,
		FactoryContext context, 
		WebAdeAuthentication authentication
	) 
	throws ServiceException, MaxResultsExceededException, CirrasPolicyServiceException, TooManyRecordsException;
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	ClaimCalculation getClaim(
		String claimNumber,
		FactoryContext context, 
		WebAdeAuthentication authentication
	) 
	throws ServiceException, NotFoundException, ValidationFailureException, CirrasPolicyServiceException;

}
