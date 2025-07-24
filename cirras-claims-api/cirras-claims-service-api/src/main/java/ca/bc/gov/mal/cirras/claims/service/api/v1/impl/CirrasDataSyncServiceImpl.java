package ca.bc.gov.mal.cirras.claims.service.api.v1.impl;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.SyncClaimRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.SyncCodeRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.SyncCommodityVarietyRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.SyncCoveragePerilRsrc;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculation;
import ca.bc.gov.mal.cirras.claims.model.v1.SyncClaim;
import ca.bc.gov.mal.cirras.claims.model.v1.SyncCode;
import ca.bc.gov.mal.cirras.claims.model.v1.SyncCommodityVariety;
import ca.bc.gov.mal.cirras.claims.model.v1.SyncCoveragePeril;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimStatusCodeDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.CommodityCoverageCodeDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.CoveragePerilDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.CropCommodityDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.CropVarietyDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.InsurancePlanDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.PerilCodeDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationUserDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimStatusCodeDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.CommodityCoverageCodeDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.CoveragePerilDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.CropCommodityDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.CropVarietyDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.InsurancePlanDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.PerilCodeDto;
import ca.bc.gov.mal.cirras.claims.service.api.v1.CirrasDataSyncService;
import ca.bc.gov.mal.cirras.claims.service.api.v1.model.factory.CirrasDataSyncFactory;
import ca.bc.gov.mal.cirras.claims.service.api.v1.model.factory.ClaimCalculationFactory;
import ca.bc.gov.mal.cirras.claims.service.api.v1.util.CirrasServiceHelper;
import ca.bc.gov.mal.cirras.claims.service.api.v1.util.ClaimsServiceEnums;
import ca.bc.gov.mal.cirras.claims.service.api.v1.validation.ModelValidator;

//import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.InsuranceClaimRsrc;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.IntegrityConstraintViolatedDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.OptimisticLockingFailureDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;
import ca.bc.gov.nrs.wfone.common.service.api.ConflictException;
import ca.bc.gov.nrs.wfone.common.service.api.ForbiddenException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.ValidationFailureException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.InsuranceClaimRsrc;
import ca.bc.gov.mal.cirras.policies.model.v1.CodeTableTypes;
import ca.bc.gov.mal.cirras.policies.model.v1.PoliciesEventTypes;

public class CirrasDataSyncServiceImpl implements CirrasDataSyncService {

	private static final Logger logger = LoggerFactory.getLogger(CirrasDataSyncServiceImpl.class);

	private Properties applicationProperties;

	private ModelValidator modelValidator;

	// factories
	private CirrasDataSyncFactory cirrasDataSyncFactory;
	private ClaimCalculationFactory claimCalculationFactory;

	// daos
	private ClaimDao claimDao;
	private ClaimCalculationDao claimCalculationDao;
	private CropCommodityDao cropCommodityDao;
	private CropVarietyDao cropVarietyDao;
	private PerilCodeDao perilCodeDao;
	private CoveragePerilDao coveragePerilDao;
	private InsurancePlanDao insurancePlanDao;
	private ClaimStatusCodeDao claimStatusCodeDao;
	private CommodityCoverageCodeDao commodityCoverageCodeDao;

	// utils
	private CirrasServiceHelper cirrasServiceHelper;

	public static final String MaximumResultsProperty = "maximum.results";

	public static final int DefaultMaximumResults = 800;

	public void setApplicationProperties(Properties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	public void setModelValidator(ModelValidator modelValidator) {
		this.modelValidator = modelValidator;
	}

	public void setCirrasDataSyncFactory(CirrasDataSyncFactory cirrasDataSyncFactory) {
		this.cirrasDataSyncFactory = cirrasDataSyncFactory;
	}

	public void setClaimCalculationFactory(ClaimCalculationFactory claimCalculationFactory) {
		this.claimCalculationFactory = claimCalculationFactory;
	}

	public void setCirrasServiceHelper(CirrasServiceHelper cirrasServiceHelper) {
		this.cirrasServiceHelper = cirrasServiceHelper;
	}

	public void setClaimDao(ClaimDao claimDao) {
		this.claimDao = claimDao;
	}

	public void setClaimCalculationDao(ClaimCalculationDao claimCalculationDao) {
		this.claimCalculationDao = claimCalculationDao;
	}

	public void setCropCommodityDao(CropCommodityDao cropCommodityDao) {
		this.cropCommodityDao = cropCommodityDao;
	}

	public void setCropVarietyDao(CropVarietyDao cropVarietyDao) {
		this.cropVarietyDao = cropVarietyDao;
	}

	public void setPerilCodeDao(PerilCodeDao perilCodeDao) {
		this.perilCodeDao = perilCodeDao;
	}

	public void setCoveragePerilDao(CoveragePerilDao coveragePerilDao) {
		this.coveragePerilDao = coveragePerilDao;
	}

	public void setInsurancePlanDao(InsurancePlanDao insurancePlanDao) {
		this.insurancePlanDao = insurancePlanDao;
	}
	
	public void setClaimStatusCodeDao(ClaimStatusCodeDao claimStatusCodeDao) {
		this.claimStatusCodeDao = claimStatusCodeDao;
	}
	
	public void setCommodityCoverageCodeDao(CommodityCoverageCodeDao commodityCoverageCodeDao) {
		this.commodityCoverageCodeDao = commodityCoverageCodeDao;
	}

	

	//
	// The "proof of concept" REST service doesn't have any security
	//
	private String getUserId(WebAdeAuthentication authentication) {
		String userId = "DEFAULT_USERID";

		if (authentication != null) {
			userId = authentication.getUserId();
		}

		return userId;
	}

	public SyncClaim getSyncClaim(Integer colId, FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException {
		logger.debug("<getSyncClaim");

		SyncClaim result = null;

		try {
			ClaimDto dto = claimDao.fetch(colId);

			if (dto == null) {
				throw new NotFoundException("Did not find the claim: " + colId);
			}

			result = cirrasDataSyncFactory.getSyncClaim(dto, factoryContext, authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getSyncClaim");
		return result;
	}

	@Override
	public SyncClaim createSyncClaim(SyncClaim syncClaim, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException, ForbiddenException,
			ConflictException, ValidationFailureException {
		logger.debug("<createSyncClaim");

		SyncClaim result = null;

		try {

			String userId = getUserId(authentication);

			ClaimDto dto = cirrasDataSyncFactory.createClaimDto(syncClaim);
			claimDao.insert(dto, userId);

			result = getSyncClaim(syncClaim.getColId(), factoryContext, authentication);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">createSyncClaim");
		return result;
	}

	@Override
	public SyncClaim updateSyncClaim(SyncClaim syncClaim, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException, ForbiddenException,
			ConflictException, ValidationFailureException, DaoException {

		logger.debug("<updateSyncClaim");

		SyncClaim result = null;

		// Only true if there is a claim record and the transaction date of the record
		// is older then the current date
		boolean compareAndUpdateStatus = saveClaimData(syncClaim, true, factoryContext, authentication);

		if (compareAndUpdateStatus) {
			// compareAndUpdateClaimStatus(syncClaim, factoryContext, authentication);
			Integer claimNumber = syncClaim.getClaimNumber();

			// Get latest version of the claim calculation
			ClaimCalculationDto claimCalculationDto = claimCalculationDao.getLatestVersionOfCalculation(claimNumber);

			// No need to compare or update if there is no calculation
			// This is quite possible and ok because not all claims have a calculation yet.
			// It's still a successful transaction
			// TODO
			if (claimCalculationDto != null) {
				ClaimCalculation calculation = claimCalculationFactory.getClaimCalculation(claimCalculationDto, null,
						factoryContext, authentication);

				updateClaimAndCalculationStatus(calculation, syncClaim, claimCalculationDto, authentication,
						calculation.getClaimCalculationGuid());

			}
		}

		logger.debug(">updateSyncClaim");

		result = getSyncClaim(syncClaim.getColId(), factoryContext, authentication);

		return result;
	}

	// This is called at runtime when a user opens the calculation sheet
	// It checks CIRRAS for the latest claim status and updates it automatically
	@Override
	public Boolean syncClaimData(ClaimCalculation claimCalculation, InsuranceClaimRsrc policyClaim,
			ClaimCalculationDto dto, String claimCalculationGuid, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, DaoException, NotFoundException {

		logger.debug("<syncClaimData");
		
		Boolean claimCalculationUpdated = false;

		// Convert claim object from cirras to a syncClaim
		SyncClaim syncClaim = cirrasDataSyncFactory.getSyncClaimFromPolicyClaim(policyClaim);
		// Because it's called at runtime the transaction date and time is the current
		// time
		syncClaim.setClaimDataSyncTransDate(new Date());

		// Update claim record if necessary
		boolean compareAndUpdateStatus = saveClaimData(syncClaim, false, factoryContext, authentication);

		if (compareAndUpdateStatus) {
			// Set new claim and calculation status if necessary
			claimCalculationUpdated = updateClaimAndCalculationStatus(claimCalculation, syncClaim, dto, authentication, claimCalculationGuid);
		}

		logger.debug(">syncClaimData");
		
		return claimCalculationUpdated;
	}

	private boolean cachedDataIsDifferent(SyncClaim syncClaim, ClaimDto claimDto) {
		boolean result = true;

		DtoUtils dtoUtils = new DtoUtils(logger);

		result = result
				&& dtoUtils.equals("claimStatusCode", syncClaim.getClaimStatusCode(), claimDto.getClaimStatusCode());
		result = result && dtoUtils.equals("SubmittedByUserid", syncClaim.getSubmittedByUserid(),
				claimDto.getSubmittedByUserid());
		result = result
				&& dtoUtils.equals("SubmittedByName", syncClaim.getSubmittedByName(), claimDto.getSubmittedByName());
		result = result && dtoUtils.equals("RecommendedByUserid", syncClaim.getRecommendedByUserid(),
				claimDto.getRecommendedByUserid());
		result = result && dtoUtils.equals("RecommendedByName", syncClaim.getRecommendedByName(),
				claimDto.getRecommendedByName());
		result = result
				&& dtoUtils.equals("ApprovedByUserid", syncClaim.getApprovedByUserid(), claimDto.getApprovedByUserid());
		result = result
				&& dtoUtils.equals("ApprovedByName", syncClaim.getApprovedByName(), claimDto.getApprovedByName());

		result = result && cirrasServiceHelper.equals("SubmittedByDate", syncClaim.getSubmittedByDate(),
				claimDto.getSubmittedByDate());
		result = result && cirrasServiceHelper.equals("RecommendedByDate", syncClaim.getRecommendedByDate(),
				claimDto.getRecommendedByDate());
		result = result && cirrasServiceHelper.equals("ApprovedByDate", syncClaim.getApprovedByDate(),
				claimDto.getApprovedByDate());

		result = result
				&& dtoUtils.equals("HasChequeReqInd", syncClaim.getHasChequeReqInd(), claimDto.getHasChequeReqInd());

		// result is false if at least one value is not equal
		return result == false;
	}

	private boolean claimCalculationDataIsDifferent(SyncClaim syncClaim, ClaimCalculation claimCalculation) {
		boolean result = true;

		DtoUtils dtoUtils = new DtoUtils(logger);

		result = result && dtoUtils.equals("claimStatusCode", syncClaim.getClaimStatusCode(),
				claimCalculation.getClaimStatusCode());
		result = result && dtoUtils.equals("SubmittedByUserid", syncClaim.getSubmittedByUserid(),
				claimCalculation.getSubmittedByUserid());
		result = result && dtoUtils.equals("SubmittedByName", syncClaim.getSubmittedByName(),
				claimCalculation.getSubmittedByName());
		result = result && dtoUtils.equals("RecommendedByUserid", syncClaim.getRecommendedByUserid(),
				claimCalculation.getRecommendedByUserid());
		result = result && dtoUtils.equals("RecommendedByName", syncClaim.getRecommendedByName(),
				claimCalculation.getRecommendedByName());
		result = result && dtoUtils.equals("ApprovedByUserid", syncClaim.getApprovedByUserid(),
				claimCalculation.getApprovedByUserid());
		result = result && dtoUtils.equals("ApprovedByName", syncClaim.getApprovedByName(),
				claimCalculation.getApprovedByName());
		result = result && cirrasServiceHelper.equals("SubmittedByDate", syncClaim.getSubmittedByDate(),
				claimCalculation.getSubmittedByDate());
		result = result && cirrasServiceHelper.equals("RecommendedByDate", syncClaim.getRecommendedByDate(),
				claimCalculation.getRecommendedByDate());
		result = result && cirrasServiceHelper.equals("ApprovedByDate", syncClaim.getApprovedByDate(),
				claimCalculation.getApprovedByDate());
		result = result && dtoUtils.equals("HasChequeReqInd", syncClaim.getHasChequeReqInd(),
				claimCalculation.getHasChequeReqInd());

		// result is false if at least one value is not equal
		return result == false;
	}

	// Updates the cached claim data if the claim data sync transaction date in the
	// database is older than the date of the update in CIRRAS
	private boolean saveClaimData(SyncClaim syncClaim, Boolean isMessagingUpdate, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, DaoException {
		logger.debug("<saveClaimData");

		boolean result = false;

		// Get current cached claim record
		Integer colId = syncClaim.getColId();
		ClaimDto dto = claimDao.fetch(colId);

		// Only update if there is a claim record (should always be)
		if (dto != null) {
			boolean updateClaimData = false;

			if (isMessagingUpdate) {
				// If the update is triggered by a message (auto sync from CIRRAS) updates
				// always happen if the transaction date
				// of the record is older than the message's transaction date
				updateClaimData = (dto.getClaimDataSyncTransDate() == null
						|| dto.getClaimDataSyncTransDate().before(syncClaim.getClaimDataSyncTransDate()));
			} else {
				// If the update is triggered at runtime all the data (but the transaction date)
				// is compared
				// to make sure that updates only happen if there was a change.
				updateClaimData = cachedDataIsDifferent(syncClaim, dto);
			}

			if (updateClaimData) {
				String userId = getUserId(authentication);

				cirrasDataSyncFactory.updateClaimDataDto(dto, syncClaim);
				claimDao.updateClaimData(dto, userId);

				result = true;
			}
		}

		logger.debug(">saveClaimData");
		return result;
	}

	// Checks if the claim status in the cirras claim equals the claim status in the
	// calculation and updates it
	// in the calculation
	// claimCalculation = current data in the database
	// syncClaim = possibly newer data
	private Boolean updateClaimAndCalculationStatus(ClaimCalculation claimCalculation, SyncClaim syncClaim,
			ClaimCalculationDto dto, WebAdeAuthentication authentication, String claimCalculationGuid)
			throws DaoException, NotFoundException {

		logger.debug("<updateClaimAndCalculationStatus");

		if (claimCalculation == null || syncClaim == null) {
			logger.debug("<claimCalculation or insuranceClaim was null.");
			return false;
		}
		
		Boolean claimCalculationUpdated = false;

		// Update only necessary if claim data in cirras is different form the one in
		// the calculator
		if (claimCalculationDataIsDifferent(syncClaim, claimCalculation)) {

			if (claimCalculation.getClaimStatusCode()
					.equalsIgnoreCase(ClaimsServiceEnums.ClaimStatusCodes.Open.getClaimStatusCode())
					|| claimCalculation.getClaimStatusCode()
							.equalsIgnoreCase(ClaimsServiceEnums.ClaimStatusCodes.InProgress.getClaimStatusCode())
					|| claimCalculation.getClaimStatusCode()
							.equalsIgnoreCase(ClaimsServiceEnums.ClaimStatusCodes.Pending.getClaimStatusCode())) {

				// Only if the current claim status of the calculation is open, in progress or
				// pending

				// Update calculation status if claim status changed AND calculation status is
				// draft DRAFT
				// Calculation status changes are triggered in the calculator only. If the claim
				// status in CIRRAS
				// has been changed manually with a calculation in status DRAFT it is ignored by
				// the calculator.
				if (claimCalculation.getClaimStatusCode().equalsIgnoreCase(syncClaim.getClaimStatusCode()) == false
						&& claimCalculation.getCalculationStatusCode().equalsIgnoreCase(
								ClaimsServiceEnums.CalculationStatusCodes.DRAFT.toString()) == false) {

					// Set calculation status according to the new claim status

					if (syncClaim.getClaimStatusCode()
							.equalsIgnoreCase(ClaimsServiceEnums.ClaimStatusCodes.Open.getClaimStatusCode())) {
						// Status changed to OPEN in CIRRAS (rollback) the calculation status changes to
						// draft

						claimCalculation
								.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.DRAFT.toString());

					} else if (syncClaim.getClaimStatusCode()
							.equalsIgnoreCase(ClaimsServiceEnums.ClaimStatusCodes.InProgress.getClaimStatusCode())) {
						// Status changed to IN PROGRESS in CIRRAS (reject) the calculation status
						// changes to submitted

						// Set calculation status
						claimCalculation.setCalculationStatusCode(
								ClaimsServiceEnums.CalculationStatusCodes.SUBMITTED.toString());

					} else if (syncClaim.getClaimStatusCode()
							.equalsIgnoreCase(ClaimsServiceEnums.ClaimStatusCodes.Pending.getClaimStatusCode())) {
						// Status changed to PENDING in CIRRAS the calculation status changes to
						// recommended

						claimCalculation.setCalculationStatusCode(
								ClaimsServiceEnums.CalculationStatusCodes.RECOMMENDED.toString());

					} else if (syncClaim.getClaimStatusCode()
							.equalsIgnoreCase(ClaimsServiceEnums.ClaimStatusCodes.Approved.getClaimStatusCode())) {
						// Status changed to APPROVED in CIRRAS the calculation status changes to
						// approved

						// Set calculation status
						claimCalculation.setCalculationStatusCode(
								ClaimsServiceEnums.CalculationStatusCodes.APPROVED.toString());
					}
				}

				setClaimDataAndUpdateStatus(claimCalculation, syncClaim, dto, authentication, claimCalculationGuid);
				claimCalculationUpdated = true;

			} else if (claimCalculation.getClaimStatusCode()
					.equalsIgnoreCase(ClaimsServiceEnums.ClaimStatusCodes.Approved.getClaimStatusCode())
					&& claimCalculation.getCalculationStatusCode()
							.equalsIgnoreCase(ClaimsServiceEnums.CalculationStatusCodes.DRAFT.toString())) {
				// If the calculation is in status DRAFT and the calculation claim status =
				// Approved and the
				// Claim has been rolled back or rejected the calculation claim status has to be
				// updated as well
				// This situation is most likely caused by user error but needs to be tracked
				// anyways
				setClaimDataAndUpdateStatus(claimCalculation, syncClaim, dto, authentication, claimCalculationGuid);
				claimCalculationUpdated = true;

			}
		}

		logger.debug(">updateClaimAndCalculationStatus");
		
		return claimCalculationUpdated;

	}

	private void setClaimDataAndUpdateStatus(ClaimCalculation claimCalculation, SyncClaim syncClaim,
			ClaimCalculationDto dto, WebAdeAuthentication authentication, String claimCalculationGuid)
			throws NotFoundException, DaoException {
		// The source field for submitted by user in CIRRAS is not set to null if claim
		// is rolled back to Open
		// The date is though
		if (syncClaim.getSubmittedByDate() == null) {
			claimCalculation.setSubmittedByUserid(null);
			claimCalculation.setSubmittedByName(null);
		} else {
			claimCalculation.setSubmittedByUserid(syncClaim.getSubmittedByUserid());
			claimCalculation.setSubmittedByName(syncClaim.getSubmittedByName());
		}
		claimCalculation.setSubmittedByDate(syncClaim.getSubmittedByDate());
		claimCalculation.setRecommendedByUserid(syncClaim.getRecommendedByUserid());
		claimCalculation.setRecommendedByName(syncClaim.getRecommendedByName());
		claimCalculation.setRecommendedByDate(syncClaim.getRecommendedByDate());
		claimCalculation.setApprovedByUserid(syncClaim.getApprovedByUserid());
		claimCalculation.setApprovedByName(syncClaim.getApprovedByName());
		claimCalculation.setApprovedByDate(syncClaim.getApprovedByDate());
		claimCalculation.setHasChequeReqInd(syncClaim.getHasChequeReqInd());

		String userId = getUserId(authentication);
		claimCalculation.setClaimStatusCode(syncClaim.getClaimStatusCode());
		
		saveUpdateClaimCalculation(claimCalculation, dto, authentication, claimCalculationGuid, userId);
	}

	private void saveUpdateClaimCalculation(ClaimCalculation claimCalculation, ClaimCalculationDto dto,
			WebAdeAuthentication authentication, String claimCalculationGuid, String userId)
			throws NotFoundException, DaoException {

		claimCalculationFactory.updateDto(dto, claimCalculation);

		ClaimCalculationUserDto dtoUser = cirrasServiceHelper.getClaimCalculationUserDto(authentication);

		if (dtoUser != null) {
			dto.setUpdateClaimCalcUserGuid(dtoUser.getClaimCalculationUserGuid());
		} else {
			dto.setUpdateClaimCalcUserGuid(null);
		}

		claimCalculationDao.update(claimCalculationGuid, dto, userId);
	}

	@Override
	public void deleteSyncClaim(Integer colId, String optimisticLock, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, ForbiddenException, ConflictException {

		logger.debug("<deleteSyncClaim");

		try {
			ClaimDto dto = claimDao.fetch(colId);

			if (dto == null) {
				throw new NotFoundException("Did not find the claim: " + colId);
			}

			// Get all calculations of the claim
			List<ClaimCalculationDto> dtos = claimCalculationDao.getCalculationsByClaimNumber(dto.getClaimNumber(),
					null);
			for (ClaimCalculationDto claimCalcDto : dtos) {
				// delete one by one to make sure the sub table records are deleted as well
				cirrasServiceHelper.deleteClaimCalculation(claimCalcDto.getClaimCalculationGuid());
			}

			// Delete claim record
			claimDao.delete(colId);

		} catch (IntegrityConstraintViolatedDaoException e) {
			throw new ConflictException(e.getMessage());
		} catch (OptimisticLockingFailureDaoException e) {
			throw new ConflictException(e.getMessage());
		} catch (NotFoundDaoException e) {
			throw new NotFoundException(e.getMessage());
		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">deleteSyncClaim");
	}

	@Override
	public void updateSyncClaimRelatedData(SyncClaimRsrc resource, FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication) {

		switch (resource.getTransactionType()) {
		case PoliciesEventTypes.PolicyUpdated:
			updatePolicyData(resource, factoryContext, webAdeAuthentication);
			break;
		case PoliciesEventTypes.GrowerUpdated:
			updateGrowerData(resource, factoryContext, webAdeAuthentication);
			break;
		default:
			break;
		}
	}

	private void updatePolicyData(SyncClaim syncClaim, FactoryContext factoryContext,
			WebAdeAuthentication authentication) {
		logger.debug("<updatePolicyData");

		// SyncClaim result = null;

		try {

			String userId = getUserId(authentication);

			ClaimDto dto = cirrasDataSyncFactory.updatePolicyDataDto(syncClaim);
			claimDao.updatePolicyData(dto, userId);

			// result = getSyncClaim(syncClaim.getColId(), factoryContext, authentication);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">updatePolicyData");
		// return result;
	}

	private void updateGrowerData(SyncClaim syncClaim, FactoryContext factoryContext,
			WebAdeAuthentication authentication) {
		logger.debug("<updateGrowerData");

		// SyncClaim result = null;

		try {

			String userId = getUserId(authentication);

			ClaimDto dto = cirrasDataSyncFactory.updateGrowerDataDto(syncClaim);
			claimDao.updateGrowerData(dto, userId);

			// result = getSyncClaim(syncClaim.getColId(), factoryContext, authentication);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">updateGrowerData");
		// return result;
	}

	@Override
	public void synchronizeCommodityVariety(SyncCommodityVarietyRsrc resource, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException, DaoException {

		logger.debug("<synchronizeCommodityVariety");

		if (resource.getTransactionType().equalsIgnoreCase(PoliciesEventTypes.CommodityVarietyDeleted)) {
			// DELETE
			inactivateSyncCommodityVariety(resource, authentication);

		} else if (resource.getTransactionType().equalsIgnoreCase(PoliciesEventTypes.CommodityVarietyCreated)
				|| resource.getTransactionType().equalsIgnoreCase(PoliciesEventTypes.CommodityVarietyUpdated)) {
			// INSERT OR UPDATE
			
			// Determine if it's commodity or variety update
			if (resource != null && resource.getParentCropId() == null) {
				// Commodity
				// Only product insurable or inventoriable commodities are synchronized
				if (resource.getIsInventoryCrop() || resource.getIsProductInsurable()) {
					//Check if record already exist and call the correct method
					CropCommodityDto dto = cropCommodityDao.fetch(resource.getCropId());

					if (dto == null) {
						createCropCommodity(resource, factoryContext, authentication);
					} else {
						updateCropCommodity(resource, dto, factoryContext, authentication);
					}
				}
			} else if (resource != null && resource.getParentCropId() != null) {
				// Variety has the parent id set
				
				//Check if record already exist and call the correct method
				CropVarietyDto dto = cropVarietyDao.fetch(resource.getCropId());

				if (dto == null) {
					createCropVariety(resource, factoryContext, authentication);
				} else {
					updateCropVariety(resource, dto, factoryContext, authentication);
				}
			}
		}

		logger.debug(">synchronizeCommodityVariety");

	}

	private void createCropCommodity(SyncCommodityVarietyRsrc resource, FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication) {

		logger.debug("<createCropCommodity");

		try {

			String userId = getUserId(webAdeAuthentication);

			CropCommodityDto dto = cirrasDataSyncFactory.createCropCommodity(resource);
			cropCommodityDao.insert(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">createCropCommodity");

	}

	private void updateCropCommodity(SyncCommodityVarietyRsrc resource, CropCommodityDto dto, 
			FactoryContext factoryContext, WebAdeAuthentication webAdeAuthentication) {

		logger.debug("<updateCropCommodity");

		try {

			String userId = getUserId(webAdeAuthentication);

			cirrasDataSyncFactory.updateCropCommodity(dto, resource);
			cropCommodityDao.update(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">updateCropCommodity");

	}

	private void createCropVariety(SyncCommodityVarietyRsrc resource, FactoryContext factoryContext,
			WebAdeAuthentication webAdeAuthentication) {

		logger.debug("<createCropVariety");

		try {

			String userId = getUserId(webAdeAuthentication);

			CropVarietyDto dto = cirrasDataSyncFactory.createCropVariety(resource);
			cropVarietyDao.insert(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">createCropVariety");

	}

	private void updateCropVariety(SyncCommodityVarietyRsrc resource, CropVarietyDto dto, 
			FactoryContext factoryContext, WebAdeAuthentication webAdeAuthentication) {

		logger.debug("<updateCropVariety");

		try {

			String userId = getUserId(webAdeAuthentication);

			cirrasDataSyncFactory.updateCropVariety(dto, resource);
			cropVarietyDao.update(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">updateCropVariety");

	}

	// The crop record is not deleted but the expiry date is set to the current
	// date.
	// It's possible that the record doesn't exist in the calculator.
	private void inactivateSyncCommodityVariety(SyncCommodityVarietyRsrc resource, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, DaoException {

		logger.debug("<inactivateSyncCommodityVariety");

		String userId = getUserId(authentication);

		CropVarietyDto dtoVariety = cropVarietyDao.fetch(resource.getCropId());

		if (dtoVariety != null) {
			cirrasDataSyncFactory.updateCropVarietyExpiryDate(dtoVariety, resource.getDataSyncTransDate());
			cropVarietyDao.update(dtoVariety, userId);

		} else {
			CropCommodityDto dtoCommodity = cropCommodityDao.fetch(resource.getCropId());
			if (dtoCommodity != null) {
				cirrasDataSyncFactory.updateCropCommodityExpiryDate(dtoCommodity, resource.getDataSyncTransDate());
				cropCommodityDao.update(dtoCommodity, userId);
			}
		}

		logger.debug(">inactivateSyncCommodityVariety");

	}

	// This is not acutally used if a crop gets deleted in CIRRAS.
	@Override
	public void deleteSyncCommodityVariety(Integer crptId, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException, DaoException {

		logger.debug("<deleteSyncCommodityVariety");

		SyncCommodityVariety model = getSyncCommodityVariety(crptId, factoryContext, authentication);

		// Determine if it's commodity or variety update
		if (model != null && model.getParentCropId() == null) {
			// Commodity
			cropCommodityDao.delete(model.getCropId());
		} else if (model != null && model.getParentCropId() != null) {
			// Variety has parent id set
			cropVarietyDao.delete(model.getCropId());
		}

		logger.debug(">deleteSyncCommodityVariety");

	}

	// This method is used to determine if the crop is a commodity or a variety and
	// return the cropId and the parentId (if it's a variety)
	@Override
	public SyncCommodityVariety getSyncCommodityVariety(Integer crptId, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException {

		logger.debug("<getSyncCommodityVariety");

		SyncCommodityVarietyRsrc resource = null;

		if (crptId != null) {
			try {

				CropVarietyDto dtoVariety = cropVarietyDao.fetch(crptId);

				if (dtoVariety != null) {
					resource = cirrasDataSyncFactory.getSyncClaimFromVariety(dtoVariety);

				} else {
					CropCommodityDto dtoCommodity = cropCommodityDao.fetch(crptId);
					if (dtoCommodity != null) {
						resource = cirrasDataSyncFactory.getSyncClaimFromCropCommodity(dtoCommodity);
					} else {
						// No record found
						throw new NotFoundException("Did not find a variety nor a commodity with id: " + crptId);
					}
				}

			} catch (DaoException e) {
				e.printStackTrace();
				throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
			}
		}

		logger.debug(">getSyncCommodityVariety");

		return resource;

	}

	////////////////////////////////////////////////////////////////////
	// Generic Code Tables
	////////////////////////////////////////////////////////////////////
	@Override
	public SyncCode getSyncCode(String codeTableType, String uniqueKey, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException, DaoException {

		logger.debug("<getSyncCode");

		SyncCodeRsrc resource = new SyncCodeRsrc();

		// Determine the type of code that is synchronized
		switch (codeTableType) {
		case CodeTableTypes.ClaimStatuses:
			ClaimStatusCodeDto claimStatusCodeDto = claimStatusCodeDao.fetch(uniqueKey);
			if (claimStatusCodeDto != null) {
				resource.setUniqueKeyString(claimStatusCodeDto.getClaimStatusCode());
				return resource;
			}
			break;
		case CodeTableTypes.CropCoverageType:
			CommodityCoverageCodeDto commodityCoverageCodeDto = commodityCoverageCodeDao.fetch(uniqueKey);
			if (commodityCoverageCodeDto != null) {
				resource.setUniqueKeyString(commodityCoverageCodeDto.getCommodityCoverageCode());
				return resource;
			}
			break;
		case CodeTableTypes.InsurancePlans:
			InsurancePlanDto insurancePlanDto = insurancePlanDao.fetch(cirrasServiceHelper.toInteger(uniqueKey));
			if (insurancePlanDto != null) {
				resource.setUniqueKeyInteger(insurancePlanDto.getInsurancePlanId());
				return resource;
			}
			break;
		case CodeTableTypes.PerilTypes:
			PerilCodeDto perilCodeDto = perilCodeDao.fetch(uniqueKey);
			if (perilCodeDto != null) {
				resource.setUniqueKeyString(perilCodeDto.getPerilCode());
				return resource;
			}
			break;
		default:
			break;
		}

		logger.debug(">getSyncCode");

		return null;

	}

	@Override
	public void synchronizeCode(SyncCodeRsrc resource, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException, DaoException {

		logger.debug("<synchronizeCode");

		// Determine the type of code that is synchronized
		switch (resource.getCodeTableType()) {
		case CodeTableTypes.ClaimStatuses:
			synchronizeClaimStatusCode(resource, factoryContext, authentication);
			break;
		case CodeTableTypes.CropCoverageType:
			synchronizeCommodityCoverageCode(resource, factoryContext, authentication);
			break;
		case CodeTableTypes.InsurancePlans:
			synchronizeInsurancePlan(resource, factoryContext, authentication);
			break;
		case CodeTableTypes.PerilTypes:
			synchronizePerilCode(resource, factoryContext, authentication);
			break;
		default:
			break;
		}

		logger.debug(">synchronizeCode");

	}

	private void synchronizePerilCode(SyncCodeRsrc resource, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws DaoException {

		logger.debug("<synchronizePerilCode");

		if (resource.getTransactionType().equalsIgnoreCase(PoliciesEventTypes.CodeDeleted)) {
			// DELETE
			inactivatePerilCode(resource, authentication);

		} else if (resource.getTransactionType().equalsIgnoreCase(PoliciesEventTypes.CodeCreated)
				|| resource.getTransactionType().equalsIgnoreCase(PoliciesEventTypes.CodeUpdated)) {
			// INSERT OR UPDATE
			
			//Check if record already exist and call the correct method
			PerilCodeDto dto = perilCodeDao.fetch(resource.getUniqueKeyString());

			
			if (dto == null) {
				createPerilCode(resource, factoryContext, authentication);
			} else {
				updatePerilCode(resource, dto, factoryContext, authentication);
			}
		}

		logger.debug(">synchronizePerilCode");

	}

	private void updatePerilCode(SyncCodeRsrc resource, PerilCodeDto dto, FactoryContext factoryContext,
			WebAdeAuthentication authentication) {

		logger.debug("<updatePerilCode");

		try {

			String userId = getUserId(authentication);

			cirrasDataSyncFactory.updatePerilCode(dto, resource);
			perilCodeDao.update(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">updatePerilCode");

	}

	private void createPerilCode(SyncCodeRsrc resource, FactoryContext factoryContext,
			WebAdeAuthentication authentication) {

		logger.debug("<createPerilCode");

		try {

			String userId = getUserId(authentication);

			PerilCodeDto dto = cirrasDataSyncFactory.createPerilCode(resource);
			perilCodeDao.insert(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">createPerilCode");

	}

	private void inactivatePerilCode(SyncCodeRsrc resource, WebAdeAuthentication authentication) throws DaoException {

		logger.debug("<inactivatePerilCode");

		String userId = getUserId(authentication);

		PerilCodeDto dto = perilCodeDao.fetch(resource.getUniqueKeyString());

		if (dto != null) {
			cirrasDataSyncFactory.updatePerilCodeExpiryDate(dto, resource.getDataSyncTransDate());
			perilCodeDao.update(dto, userId);
		}

		logger.debug(">inactivatePerilCode");

	}
	
	private void synchronizeCommodityCoverageCode(SyncCodeRsrc resource, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws DaoException {

		logger.debug("<synchronizeCommodityCoverageCode");

		if (resource.getTransactionType().equalsIgnoreCase(PoliciesEventTypes.CodeDeleted)) {
			// DELETE
			inactivateCommodityCoverageCode(resource, authentication);

		} else if (resource.getTransactionType().equalsIgnoreCase(PoliciesEventTypes.CodeCreated)
				|| resource.getTransactionType().equalsIgnoreCase(PoliciesEventTypes.CodeUpdated)) {
			// INSERT OR UPDATE
			
			//Check if record already exist and call the correct method
			CommodityCoverageCodeDto dto = commodityCoverageCodeDao.fetch(resource.getUniqueKeyString());
			
			if (dto == null) {
				createCommodityCoverageCode(resource, factoryContext, authentication);
			} else {
				updateCommodityCoverageCode(resource, dto, factoryContext, authentication);
			}
		}

		logger.debug(">synchronizeCommodityCoverageCode");

	}

	private void updateCommodityCoverageCode(SyncCodeRsrc resource, CommodityCoverageCodeDto dto, 
			FactoryContext factoryContext, WebAdeAuthentication authentication) {

		logger.debug("<updateClaimStatusCode");

		try {

			String userId = getUserId(authentication);

			cirrasDataSyncFactory.updateCommodityCoverageCode(dto, resource);
			commodityCoverageCodeDao.update(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">updateCommodityCoverageCode");

	}

	private void createCommodityCoverageCode(SyncCodeRsrc resource, FactoryContext factoryContext,
			WebAdeAuthentication authentication) {

		logger.debug("<createCommodityCoverageCode");

		try {

			String userId = getUserId(authentication);

			CommodityCoverageCodeDto dto = cirrasDataSyncFactory.createCommodityCoverageCode(resource);
			commodityCoverageCodeDao.insert(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">createCommodityCoverageCode");

	}

	private void inactivateCommodityCoverageCode(SyncCodeRsrc resource, WebAdeAuthentication authentication) throws DaoException {

		logger.debug("<inactivateCommodityCoverage");

		String userId = getUserId(authentication);

		CommodityCoverageCodeDto dto = commodityCoverageCodeDao.fetch(resource.getUniqueKeyString());

		if (dto != null) {
			cirrasDataSyncFactory.updateCommodityCoverageCodeExpiryDate(dto, resource.getDataSyncTransDate());
			commodityCoverageCodeDao.update(dto, userId);
		}

		logger.debug(">inactivateCommodityCoverage");

	}	
	
	private void synchronizeClaimStatusCode(SyncCodeRsrc resource, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws DaoException {

		logger.debug("<synchronizeClaimStatusCode");

		if (resource.getTransactionType().equalsIgnoreCase(PoliciesEventTypes.CodeDeleted)) {
			// DELETE
			inactivateClaimStatusCode(resource, authentication);

		} else if (resource.getTransactionType().equalsIgnoreCase(PoliciesEventTypes.CodeCreated)
				|| resource.getTransactionType().equalsIgnoreCase(PoliciesEventTypes.CodeUpdated)) {
			// INSERT OR UPDATE
			
			//Check if record already exist and call the correct method
			ClaimStatusCodeDto dto = claimStatusCodeDao.fetch(resource.getUniqueKeyString());

			if (dto == null) {
				createClaimStatusCode(resource, factoryContext, authentication);
			} else {
				updateClaimStatusCode(resource, dto, factoryContext, authentication);
			}
		}

		logger.debug(">synchronizeClaimStatusCode");

	}

	private void updateClaimStatusCode(SyncCodeRsrc resource, ClaimStatusCodeDto dto, 
			FactoryContext factoryContext, WebAdeAuthentication authentication) {

		logger.debug("<updateClaimStatusCode");

		try {

			String userId = getUserId(authentication);

			cirrasDataSyncFactory.updateClaimStatusCode(dto, resource);
			claimStatusCodeDao.update(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">updateClaimStatusCode");

	}

	private void createClaimStatusCode(SyncCodeRsrc resource, FactoryContext factoryContext,
			WebAdeAuthentication authentication) {

		logger.debug("<createClaimStatusCode");

		try {

			String userId = getUserId(authentication);

			ClaimStatusCodeDto dto = cirrasDataSyncFactory.createClaimStatusCode(resource);
			claimStatusCodeDao.insert(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">createClaimStatusCode");

	}

	private void inactivateClaimStatusCode(SyncCodeRsrc resource, WebAdeAuthentication authentication) throws DaoException {

		logger.debug("<inactivateClaimStatusCode");

		String userId = getUserId(authentication);

		ClaimStatusCodeDto dto = claimStatusCodeDao.fetch(resource.getUniqueKeyString());

		if (dto != null) {
			cirrasDataSyncFactory.updateClaimStatusExpiryDate(dto, resource.getDataSyncTransDate());
			claimStatusCodeDao.update(dto, userId);
		}

		logger.debug(">inactivateClaimStatusCode");

	}	

	private void synchronizeInsurancePlan(SyncCodeRsrc resource, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws DaoException {

		logger.debug("<synchronizeInsurancePlan");

		if (resource.getTransactionType().equalsIgnoreCase(PoliciesEventTypes.CodeDeleted)) {
			// DELETE
			inactivateInsurancePlan(resource, authentication);
		
		} else if (resource.getTransactionType().equalsIgnoreCase(PoliciesEventTypes.CodeCreated)
				|| resource.getTransactionType().equalsIgnoreCase(PoliciesEventTypes.CodeUpdated)) {
			// INSERT OR UPDATE
			
			//Check if record already exist and call the correct method
			InsurancePlanDto dto = insurancePlanDao.fetch(resource.getUniqueKeyInteger());
			
			if (dto == null) {
				createInsurancePlan(resource, factoryContext, authentication);
			} else {
				updateInsurancePlan(resource, dto, factoryContext, authentication);
			}
		}

		logger.debug(">synchronizeInsurancePlan");

	}

	private void updateInsurancePlan(SyncCodeRsrc resource, InsurancePlanDto dto, 
			FactoryContext factoryContext, WebAdeAuthentication authentication) {

		logger.debug("<updateInsurancePlan");

		try {

			String userId = getUserId(authentication);

			cirrasDataSyncFactory.updateInsurancePlan(dto, resource);
			insurancePlanDao.update(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">updateInsurancePlan");

	}

	private void createInsurancePlan(SyncCodeRsrc resource, FactoryContext factoryContext,
			WebAdeAuthentication authentication) {

		logger.debug("<createInsurancePlan");

		try {

			String userId = getUserId(authentication);

			InsurancePlanDto dto = cirrasDataSyncFactory.createInsurancePlan(resource);
			insurancePlanDao.insert(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">createInsurancePlan");

	}

	private void inactivateInsurancePlan(SyncCodeRsrc resource, WebAdeAuthentication authentication) throws DaoException {

		logger.debug("<inactivatePerilCode");

		String userId = getUserId(authentication);

		InsurancePlanDto dto = insurancePlanDao.fetch(resource.getUniqueKeyInteger());

		if (dto != null) {
			cirrasDataSyncFactory.updateInsurancePlanExpiryDate(dto, resource.getDataSyncTransDate());
			insurancePlanDao.update(dto, userId);
		}

		logger.debug(">inactivatePerilCode");

	}

	// This is not acutally used if a crop gets deleted in CIRRAS at the moment.
	@Override
	public void deleteSyncCode(String codeTableType, String uniqueKey, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException, DaoException {

		logger.debug("<deleteSyncCode");

		// Determine the type of code that is synchronized
		switch (codeTableType) {
		case CodeTableTypes.ClaimStatuses:
			claimStatusCodeDao.delete(uniqueKey);
			break;
		case CodeTableTypes.CropCoverageType:
			commodityCoverageCodeDao.delete(uniqueKey);
			break;
		case CodeTableTypes.InsurancePlans:
			insurancePlanDao.delete(cirrasServiceHelper.toInteger(uniqueKey));
			break;
		case CodeTableTypes.PerilTypes:
			perilCodeDao.delete(uniqueKey);
			break;
		default:
			break;
		}

		logger.debug(">deleteSyncCode");

	}

	@Override
	public SyncCoveragePeril getSyncCoveragePeril(Integer coveragePerilId, FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, DaoException {

		logger.debug("<getSyncCoveragePeril");

		SyncCoveragePeril result = null;

		try {
			CoveragePerilDto dto = coveragePerilDao.fetch(coveragePerilId);

			if (dto == null) {
				throw new NotFoundException("Did not find coverage peril record: coveragePerilId: " + coveragePerilId);
			}

			result = cirrasDataSyncFactory.getSyncCoveragePeril(dto);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getSyncCoveragePeril");
		return result;
	}

	@Override
	public void synchronizeCoveragePeril(SyncCoveragePerilRsrc resource, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException, DaoException {

		logger.debug("<synchronizeCoveragePeril");

		// implemented
		if (resource.getTransactionType().equalsIgnoreCase(PoliciesEventTypes.CoveragePerilDeleted)) {
			// DELETE
			inactivateCoveragePeril(resource, authentication);
		
		} else if (resource.getTransactionType().equalsIgnoreCase(PoliciesEventTypes.CoveragePerilCreated)
				|| resource.getTransactionType().equalsIgnoreCase(PoliciesEventTypes.CoveragePerilUpdated)) {
			// INSERT OR UPDATE
			
			//Check if record already exist and call the correct method
			CoveragePerilDto dto = coveragePerilDao.fetch(resource.getCoveragePerilId());

			if (dto == null) {
				createCoveragePeril(resource, factoryContext, authentication);
			} else {
				updateCoveragePeril(resource, dto, factoryContext, authentication);
			}
		}

		logger.debug(">synchronizeCoveragePeril");

	}

	private void updateCoveragePeril(SyncCoveragePerilRsrc resource, CoveragePerilDto dto, 
			FactoryContext factoryContext, WebAdeAuthentication authentication) {

		logger.debug("<updateCoveragePeril");

		try {

			String userId = getUserId(authentication);

			cirrasDataSyncFactory.updateCoveragePeril(dto, resource);
			coveragePerilDao.update(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">updateCoveragePeril");

	}

	private void createCoveragePeril(SyncCoveragePerilRsrc resource, FactoryContext factoryContext,
			WebAdeAuthentication authentication) {

		logger.debug("<createCoveragePeril");

		try {

			String userId = getUserId(authentication);

			CoveragePerilDto dto = cirrasDataSyncFactory.createCoveragePeril(resource);
			coveragePerilDao.insert(dto, userId);

		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">createCoveragePeril");

	}

	private void inactivateCoveragePeril(SyncCoveragePerilRsrc resource, WebAdeAuthentication authentication)
			throws DaoException {

		logger.debug("<inactivateCoveragePeril");

		String userId = getUserId(authentication);

		CoveragePerilDto dto = coveragePerilDao.fetch(resource.getCoveragePerilId());

		if (dto != null) {
			cirrasDataSyncFactory.updateCoveragePerilExpiryDate(dto, resource.getDataSyncTransDate());
			coveragePerilDao.update(dto, userId);
		}

		logger.debug(">inactivateCoveragePeril");

	}

	@Override
	public void deleteSyncCoveragePeril(Integer coveragePerilId,
			FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, DaoException {
		logger.debug("<deleteSyncCoveragePeril");

		coveragePerilDao.delete(coveragePerilId);

		logger.debug(">deleteSyncCoveragePeril");

	}

}
