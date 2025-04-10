package ca.bc.gov.mal.cirras.claims.service.api.v1.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.mal.cirras.claims.model.v1.Claim;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimList;

import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculation;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationGrainUnseeded;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationList;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationPlantAcres;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationPlantUnits;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationVariety;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationBerriesDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationGrainUnseededDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationGrapesDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationPlantAcresDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationPlantUnitsDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationVarietyDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationUserDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationBerriesDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationGrainUnseededDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationGrapesDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationPlantAcresDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationPlantUnitsDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationVarietyDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationUserDto;
import ca.bc.gov.mal.cirras.claims.service.api.v1.CirrasClaimService;
import ca.bc.gov.mal.cirras.claims.service.api.v1.model.factory.ClaimCalculationFactory;
import ca.bc.gov.mal.cirras.claims.service.api.v1.model.factory.ClaimFactory;
import ca.bc.gov.mal.cirras.claims.service.api.v1.util.ClaimsServiceEnums;
import ca.bc.gov.mal.cirras.claims.service.api.v1.util.CirrasServiceHelper;
import ca.bc.gov.mal.cirras.claims.service.api.v1.util.OutOfSync;
import ca.bc.gov.mal.cirras.claims.service.api.v1.validation.ModelValidator;
import ca.bc.gov.mal.cirras.policies.api.rest.client.v1.CirrasPolicyService;
import ca.bc.gov.mal.cirras.policies.api.rest.client.v1.CirrasPolicyServiceException;
import ca.bc.gov.mal.cirras.policies.api.rest.client.v1.ValidationException;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.InsuranceClaimRsrc;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.ProductListRsrc;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.ProductRsrc;
import ca.bc.gov.nrs.wfone.common.model.Message;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.TooManyRecordsException;
import ca.bc.gov.nrs.wfone.common.persistence.dto.PagedDtos;
import ca.bc.gov.nrs.wfone.common.service.api.ConflictException;
import ca.bc.gov.nrs.wfone.common.service.api.ForbiddenException;
import ca.bc.gov.nrs.wfone.common.service.api.MaxResultsExceededException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.ValidationFailureException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.ClaimCalculationSubmitRsrc;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.EndpointsRsrc;
import ca.bc.gov.mal.cirras.policies.model.v1.InsuranceClaim;
import ca.bc.gov.mal.cirras.claims.service.api.v1.CirrasDataSyncService;


public class CirrasClaimServiceImpl implements CirrasClaimService {

	private static final Logger logger = LoggerFactory.getLogger(CirrasClaimServiceImpl.class);

	private Properties applicationProperties;

	private ModelValidator modelValidator;

	// factories
	private ClaimFactory claimFactory;
	private ClaimCalculationFactory claimCalculationFactory;
	
	// utils
	private OutOfSync outOfSync;

	// daos
	private ClaimCalculationDao claimCalculationDao;
	private ClaimCalculationVarietyDao claimCalculationVarietyDao;
	private ClaimCalculationBerriesDao claimCalculationBerriesDao;
	private ClaimCalculationPlantUnitsDao claimCalculationPlantUnitsDao;
	private ClaimCalculationPlantAcresDao claimCalculationPlantAcresDao;
	private ClaimCalculationGrapesDao claimCalculationGrapesDao;
	private ClaimCalculationGrainUnseededDao claimCalculationGrainUnseededDao;
	private ClaimCalculationUserDao claimCalculationUserDao;
	private ClaimDao claimDao;

	// services
	private CirrasPolicyService cirrasPolicyService;

	//@Autowired
	private CirrasDataSyncService cirrasDataSyncService;
	
	//utils
	//@Autowired
	private CirrasServiceHelper cirrasServiceHelper;
	
	public static final String MaximumResultsProperty = "maximum.results";

	public static final int DefaultMaximumResults = 800;

	public void setCirrasPolicyService(CirrasPolicyService cirrasPolicyService) {
		this.cirrasPolicyService = cirrasPolicyService;
	}

	public void setCirrasDataSyncService(CirrasDataSyncService cirrasDataSyncService) {
		this.cirrasDataSyncService = cirrasDataSyncService;
	}

	public void setCirrasServiceHelper(CirrasServiceHelper cirrasServiceHelper) {
		this.cirrasServiceHelper = cirrasServiceHelper;
	}
	
	public void setApplicationProperties(Properties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	public void setModelValidator(ModelValidator modelValidator) {
		this.modelValidator = modelValidator;
	}

	public void setClaimFactory(ClaimFactory claimFactory) {
		this.claimFactory = claimFactory;
	}

	public void setClaimCalculationFactory(ClaimCalculationFactory claimCalculationFactory) {
		this.claimCalculationFactory = claimCalculationFactory;
	}

	public void setClaimCalculationDao(ClaimCalculationDao claimCalculationDao) {
		this.claimCalculationDao = claimCalculationDao;
	}

	public void setClaimCalculationVarietyDao(ClaimCalculationVarietyDao claimCalculationVarietyDao) {
		this.claimCalculationVarietyDao = claimCalculationVarietyDao;
	}

	public void setClaimCalculationBerriesDao(ClaimCalculationBerriesDao claimCalculationBerriesDao) {
		this.claimCalculationBerriesDao = claimCalculationBerriesDao;
	}

	public void setClaimCalculationGrapesDao(ClaimCalculationGrapesDao claimCalculationGrapesDao) {
		this.claimCalculationGrapesDao = claimCalculationGrapesDao;
	}

	public void setClaimCalculationGrainUnseededDao(ClaimCalculationGrainUnseededDao claimCalculationGrainUnseededDao) {
		this.claimCalculationGrainUnseededDao = claimCalculationGrainUnseededDao;
	}

	public void setClaimCalculationPlantUnitsDao(ClaimCalculationPlantUnitsDao claimCalculationPlantUnitsDao) {
		this.claimCalculationPlantUnitsDao = claimCalculationPlantUnitsDao;
	}

	public void setClaimCalculationPlantAcresDao(ClaimCalculationPlantAcresDao claimCalculationPlantAcresDao) {
		this.claimCalculationPlantAcresDao = claimCalculationPlantAcresDao;
	}

	public void setClaimCalculationUserDao(ClaimCalculationUserDao claimCalculationUserDao) {
		this.claimCalculationUserDao = claimCalculationUserDao;
	}

	public void setClaimDao(ClaimDao claimDao) {
		this.claimDao = claimDao;
	}

	
	public void setOutOfSync(OutOfSync outOfSync) {
		this.outOfSync = outOfSync;
	}
	
	@Autowired
	private CirrasPolicyService cirrasPolicyServiceImpl;

	//
	// The "proof of concept" REST service doesn't have any security
	//
	private String getUserId(WebAdeAuthentication authentication) {
		String userId = "DEFAULT_USERID";

		if (authentication != null) {
			userId = authentication.getUserId();
			authentication.getClientId();
		}

		return userId;
	}
	@Override
	public ClaimList<? extends Claim> getClaimList(Integer claimNumber, String policyNumber,
			String calculationStatusCode, String sortColumn, String sortDirection, Integer pageNumber,
			Integer pageRowCount, FactoryContext context, WebAdeAuthentication authentication)
			throws ServiceException, MaxResultsExceededException, CirrasPolicyServiceException, TooManyRecordsException {

		ClaimList<? extends Claim> results = null;

		try {

			int maximumRows = DefaultMaximumResults;

			PagedDtos<ClaimDto> dtos = claimDao.select(claimNumber, policyNumber, calculationStatusCode,
					sortColumn, sortDirection, maximumRows, pageNumber, pageRowCount);
			
			results = claimFactory.getClaimList(dtos, claimNumber, policyNumber, calculationStatusCode,
					sortColumn, sortDirection, pageRowCount, context, authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		return results;
	}

	@Override
	public ClaimCalculation getClaim(String claimNumber, FactoryContext context, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, ValidationFailureException, CirrasPolicyServiceException {

		logger.debug("<getClaim");

		ClaimCalculation result = null;

		try {

			// Check if there is no calculation for that claim number yet
			int nextVersionNumber = GetNextVersionNumberForClaim(Integer.parseInt(claimNumber));

			// If nextVersionNumber is greater than 1 then we can't add a new
			if (nextVersionNumber > 1) {
				throw new ServiceException("A calculation already exists for this claim number: " + claimNumber
						+ " and a new one can't be added manually");
			}

			logger.debug("cirrasPolicyService call");
			// Get claims data from cirras
			InsuranceClaimRsrc policyClaimRsrc = getCirrasClaim(claimNumber);

			if (policyClaimRsrc == null) {
				throw new NotFoundException("no claim found for " + claimNumber);
			} else {
				if (!policyClaimRsrc.getClaimStatusCode().equals(ClaimsServiceEnums.ClaimStatusCodes.Open.getClaimStatusCode())) {
					throw new ServiceException(
							"Claim: " + claimNumber + " needs to be in status Open to add a calculation");
				}
			}
			
			
			ProductRsrc productRsrc = null;
			
			if (policyClaimRsrc.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAIN.toString())
					&& policyClaimRsrc.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.CropUnseeded.getCode())) {
				
				ProductListRsrc productListRsrc = getCirrasClaimProducts(
														policyClaimRsrc.getInsurancePolicyId().toString(), 
														true,
														policyClaimRsrc.getPurchaseId().toString(),
														null);
				if (productListRsrc == null) {
					throw new NotFoundException("no product found for " + claimNumber);
				}
				
				productRsrc = productListRsrc.getCollection().get(0);
			}

			// Convert InsuranceClaimRsrc to ClaimCalculation
			result = claimCalculationFactory.getCalculationFromClaim(policyClaimRsrc, productRsrc, context, authentication);

			result.setCalculationVersion(new Integer(nextVersionNumber));
			result.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.DRAFT.toString());
			result.setClaimCalculationGuid(null);

			// Calculate variety iv
			calculateVarietyInsurableValues(result);

		} catch (CirrasPolicyServiceException e) {
			throw new ServiceException("Policy service threw an exception (CirrasPolicyServiceException)", e);
		} catch (TooManyRecordsException e) {
			throw new ServiceException("DAO threw an exception (TooManyRecordsException)", e);
		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception (DaoException)", e);
		}

		logger.debug(">getClaim");
		return result;
	}

	@Override
	public ClaimCalculation createClaimCalculation(ClaimCalculation claimCalculation, FactoryContext factoryContext,
			WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, ValidationFailureException {
		logger.debug("<createInsuranceClaim");

		ClaimCalculation result = null;

		try {
			List<Message> errors = new ArrayList<Message>();
			// errors.addAll(modelValidator.validateInsuranceClaim(insuranceClaim)); // TODO

			if (!errors.isEmpty()) {
				throw new ValidationFailureException(errors);
			}

			calculateVarietyInsurableValues(claimCalculation);
			calculateTotals(claimCalculation);

			ClaimCalculationDto dto = claimCalculationFactory.createDto(claimCalculation);

			//
			// set the version number based on how many calculations there are for the claim
			//
			Integer claimNumber = dto.getClaimNumber();
			int nextVersionNumber = GetNextVersionNumberForClaim(claimNumber);

			dto.setCalculationVersion(nextVersionNumber);
			dto.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.DRAFT.toString());
			dto.setClaimCalculationGuid(null);

			ClaimCalculationUserDto dtoUser = cirrasServiceHelper.getClaimCalculationUserDto(authentication);

			if (dtoUser != null) {
				dto.setCreateClaimCalcUserGuid(dtoUser.getClaimCalculationUserGuid());
				dto.setUpdateClaimCalcUserGuid(dtoUser.getClaimCalculationUserGuid());
			} else {
				dto.setCreateClaimCalcUserGuid(null);
				dto.setUpdateClaimCalcUserGuid(null);
			}

			String userId = getUserId(authentication);
			claimCalculationDao.insert(dto, userId);

			String claimCalculationGuid = dto.getClaimCalculationGuid();
			
			//Create sub table records
			createSubTableRecords(claimCalculation, userId, claimCalculationGuid);
			
			// have to call getInsuranceClaim so that the REVISION_COUNT is correct
			result = getClaimCalculation(claimCalculationGuid, false, factoryContext, authentication);
		} catch (TooManyRecordsException e) {
			throw new ServiceException("DAO threw an exception", e);
		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">createInsuranceClaim");
		return result;
	}

	private void createSubTableRecords(ClaimCalculation claimCalculation, String userId, String claimCalculationGuid)
			throws DaoException {
		// Insert Varieties
		createVarieties(claimCalculation, userId, claimCalculationGuid, false);
		
		// Insert Grapes data
		createGrapesQuantity(claimCalculation, userId, claimCalculationGuid);

		// Insert Berries data
		createBerriesQuantity(claimCalculation, userId, claimCalculationGuid);

		// Insert Plant by Units data
		createPlantUnits(claimCalculation, userId, claimCalculationGuid);  

		// Insert Plant by Acres data
		createPlantAcres(claimCalculation, userId, claimCalculationGuid);
		
		// Insert Grain Unseeded data
		createGrainUnseeded(claimCalculation, userId, claimCalculationGuid);
	}

	private void createPlantAcres(ClaimCalculation claimCalculation, String userId, String claimCalculationGuid)
			throws DaoException {
		// Insert Plant by Acres data
		if (claimCalculation.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.Plant.getCode())
				&& claimCalculation.getInsuredByMeasurementType().equalsIgnoreCase(ClaimsServiceEnums.InsuredByMeasurementType.ACRES.toString())) {
			ClaimCalculationPlantAcresDto dtoPlantAcres = claimCalculationFactory
					.createDto(claimCalculation.getClaimCalculationPlantAcres());
			dtoPlantAcres.setClaimCalcPlantAcresGuid(null);
			dtoPlantAcres.setClaimCalculationGuid(claimCalculationGuid);
			claimCalculationPlantAcresDao.insert(dtoPlantAcres, userId);
		}
	}

	private void createPlantUnits(ClaimCalculation claimCalculation, String userId, String claimCalculationGuid)
			throws DaoException {
		// Insert Plant by Units data
		if (claimCalculation.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.Plant.getCode())
				&& claimCalculation.getInsuredByMeasurementType().equalsIgnoreCase(ClaimsServiceEnums.InsuredByMeasurementType.UNITS.toString())) {
			ClaimCalculationPlantUnitsDto dtoPlantUnits = claimCalculationFactory
					.createDto(claimCalculation.getClaimCalculationPlantUnits());
			dtoPlantUnits.setClaimCalcPlantUnitsGuid(null);
			dtoPlantUnits.setClaimCalculationGuid(claimCalculationGuid);
			claimCalculationPlantUnitsDao.insert(dtoPlantUnits, userId);
		}
	}

	private void createGrapesQuantity(ClaimCalculation claimCalculation, String userId, String claimCalculationGuid)
			throws DaoException {
		//
		// Insert Berries Data
		//
		if (claimCalculation.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAPES.toString())
				&& claimCalculation.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.Quantity.getCode())) {
				
			ClaimCalculationGrapesDto dtoGrapes = claimCalculationFactory.createDto(claimCalculation.getClaimCalculationGrapes());

			dtoGrapes.setClaimCalculationGrapesGuid(null);
			dtoGrapes.setClaimCalculationGuid(claimCalculationGuid);
			claimCalculationGrapesDao.insert(dtoGrapes, userId);
		}
	}

	private void createGrainUnseeded(ClaimCalculation claimCalculation, String userId, String claimCalculationGuid)
			throws DaoException {
		//
		// Insert Grain Unseeded Data
		//
		if (claimCalculation.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAIN.toString())
				&& claimCalculation.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.CropUnseeded.getCode())) {
				
			ClaimCalculationGrainUnseededDto dtoGrapes = claimCalculationFactory.createDto(claimCalculation.getClaimCalculationGrainUnseeded());

			dtoGrapes.setClaimCalculationGrainUnseededGuid(null);
			dtoGrapes.setClaimCalculationGuid(claimCalculationGuid);
			claimCalculationGrainUnseededDao.insert(dtoGrapes, userId);
		}
	}
	
	private void createBerriesQuantity(ClaimCalculation claimCalculation, String userId, String claimCalculationGuid)
			throws DaoException {
		//
		// Insert Berries Data
		//
		if (claimCalculation.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.BERRIES.toString())
				&& claimCalculation.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.Quantity.getCode())) {
				
			ClaimCalculationBerriesDto dtoBerries = claimCalculationFactory.createDto(claimCalculation.getClaimCalculationBerries());

			dtoBerries.setClaimCalculationBerriesGuid(null);
			dtoBerries.setClaimCalculationGuid(claimCalculationGuid);
			claimCalculationBerriesDao.insert(dtoBerries, userId);
		}
	}

	private void createVarieties(ClaimCalculation claimCalculation, String userId, String claimCalculationGuid, Boolean deleteRecords)
			throws DaoException {

		if (claimCalculation.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAPES.toString())){
			//Delete records first when updating a claim calculation
			if(deleteRecords) {
				claimCalculationVarietyDao.deleteForClaim(claimCalculationGuid);
			}

			//
			// insert varieties
			//
			for (ClaimCalculationVariety modelVariety : claimCalculation.getVarieties()) {
				ClaimCalculationVarietyDto dtoVariety = claimCalculationFactory.createDto(modelVariety);

				dtoVariety.setClaimCalculationVarietyGuid(null);
				dtoVariety.setClaimCalculationGuid(claimCalculationGuid);
				claimCalculationVarietyDao.insert(dtoVariety, userId);
			}
		}
	}

	private int GetNextVersionNumberForClaim(Integer claimNumber) throws DaoException, TooManyRecordsException {

		PagedDtos<ClaimCalculationDto> dtos = claimCalculationDao.select(claimNumber, null, null, null, null, null,
				null, null, null, 1000, null, null);

		return dtos.getTotalRowCount() + 1;
	}

	@Override
	public ClaimCalculation getClaimCalculation(String claimCalculationGuid, Boolean doRefreshManualClaimData,
			FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException {
		logger.debug("<getClaimCalculation");

		ClaimCalculation result = null;
		
		try {
			
			ClaimCalculationDto dto = claimCalculationDao.fetch(claimCalculationGuid);

			if (dto == null) {
				throw new NotFoundException("Did not find the claim: " + claimCalculationGuid);
			}

			// Get Subtable Records
			getSubTableRecords(claimCalculationGuid, dto);

			result = claimCalculationFactory.getClaimCalculation(dto, factoryContext, authentication);

			String claimNumber = result.getClaimNumber().toString();
			InsuranceClaimRsrc policyClaimRsrc = null;
			try {
				policyClaimRsrc = getCirrasClaim(claimNumber);
			} catch (Exception e) {
				//Don't catch exception as this should not result in the calculation sheet not loading.
				//It's handled in the calling methods if InsuranceClaimRsrc is null
				//Logging error
				logger.info("getCirrasClaim Error when getting a claim from CIRRAS: " + e);
			}
			
			if(policyClaimRsrc != null) {
				//Save claim status before it's reset in cirrasDataSyncService.syncClaimData
				String prevClaimStatus = result.getCalculationStatusCode();
	
				// Check if claim status is in sync and update if necessary
				// Needs to be done before setting the out of sync flags
				Boolean claimCalculationUpdated = cirrasDataSyncService.syncClaimData(result, policyClaimRsrc, dto, claimCalculationGuid, factoryContext, authentication);
				
				if(claimCalculationUpdated) {
					//Need to reload claim calculation if it has been updated to prevent precondition errors
					dto = claimCalculationDao.fetch(claimCalculationGuid);
					
					// Get Subtable Records
					getSubTableRecords(claimCalculationGuid, dto);

					result = claimCalculationFactory.getClaimCalculation(dto, factoryContext, authentication);

				}
				
				//If a calculation is in status Approved, then the normally automatically synched claim-related fields, particularly 
				//claimStatus and hasChequeReqInd, are no longer updated, and are deliberately kept out of sync unless the calculation 
				//is replaced. So the currentClaimStatus and currentHasChequeReqInd are used for the actual, up to date values.
				result.setCurrentClaimStatusCode(policyClaimRsrc.getClaimStatusCode());
				result.setCurrentHasChequeReqInd(policyClaimRsrc.getHasChequeReqInd());
	
				if (!ClaimsServiceEnums.CalculationStatusCodes.APPROVED.toString().equals(result.getCalculationStatusCode())
						&& !ClaimsServiceEnums.CalculationStatusCodes.ARCHIVED.toString().equals(result.getCalculationStatusCode())) {
	
					if (policyClaimRsrc.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAIN.toString())
							&& policyClaimRsrc.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.CropUnseeded.getCode())) {
						
//						ProductListRsrc productListRsrc = getCirrasClaimProducts(
//								policyClaimRsrc.getInsurancePolicyId().toString(), 
//								true,
//								policyClaimRsrc.getPurchaseId().toString(),
//								null);
//						if (productListRsrc == null) {
//							throw new NotFoundException("no product found for " + claimNumber);
//						}
					}					
					
					if (doRefreshManualClaimData != null && doRefreshManualClaimData.booleanValue()) {
						refreshManualClaimData(result, policyClaimRsrc);
					}
	
					// Sets the out of sync flags for any fields in the calculation that are out of
					// sync with the Claim in CIRRAS.
					// If the check cannot be performed because policyClaimRsrc is null, then they
					// are left null to indicate that the sync status is unknown.
					outOfSync.calculateOutOfSyncFlags(result, policyClaimRsrc);
				} else {
					if (doRefreshManualClaimData != null && doRefreshManualClaimData.booleanValue()) {
						// Only show an error message if the calculation status was approved or archived
						// before it was updated in updateClaimAndCalculationStatus
						if (prevClaimStatus.equals(ClaimsServiceEnums.CalculationStatusCodes.APPROVED.toString())
								|| prevClaimStatus.equals(ClaimsServiceEnums.CalculationStatusCodes.ARCHIVED.toString())) {
							throw new ServiceException("Cannot refresh manual Claim data for this calculation status");
						}
					}
				}
			}
			
		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getClaimCalculation");
		return result;
	}

	private void getSubTableRecords(String claimCalculationGuid, ClaimCalculationDto dto) throws DaoException {
		// Get Grapes data and Varieties
		if(dto.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAPES.toString()) 
				&& dto.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.Quantity.getCode())) {

			//Load Grapes data
			ClaimCalculationGrapesDto grapesDto = claimCalculationGrapesDao.select(claimCalculationGuid);
			dto.setClaimCalculationGrapes(grapesDto);

			//Load Varieties
			List<ClaimCalculationVarietyDto> varieties = claimCalculationVarietyDao.select(claimCalculationGuid);
			dto.setVarieties(varieties);
		}

		// Get Berries Quantity
		if(dto.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.BERRIES.toString()) 
				&& dto.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.Quantity.getCode())) {
			ClaimCalculationBerriesDto berriesDto = claimCalculationBerriesDao.select(claimCalculationGuid);
			dto.setClaimCalculationBerries(berriesDto);
		}
		
		// Get Plant By Units
		if (dto.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.Plant.getCode())
			&& dto.getInsuredByMeasurementType().equalsIgnoreCase(ClaimsServiceEnums.InsuredByMeasurementType.UNITS.toString())) {
			ClaimCalculationPlantUnitsDto plantUnitsDto = claimCalculationPlantUnitsDao.select(claimCalculationGuid);
			dto.setClaimCalculationPlantUnits(plantUnitsDto);
		} 

		// Get Plant By Acres
		if (dto.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.Plant.getCode())
				&& dto.getInsuredByMeasurementType().equalsIgnoreCase(ClaimsServiceEnums.InsuredByMeasurementType.ACRES.toString())) {
			ClaimCalculationPlantAcresDto plantAcresDto = claimCalculationPlantAcresDao.select(claimCalculationGuid);
			dto.setClaimCalculationPlantAcres(plantAcresDto);
		}
		
		// Get Grain Unseeded
		if(dto.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAIN.toString()) 
				&& dto.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.CropUnseeded.getCode())) {
			ClaimCalculationGrainUnseededDto grainUnseededDto = claimCalculationGrainUnseededDao.select(claimCalculationGuid);
			dto.setClaimCalculationGrainUnseeded(grainUnseededDto);
		}
	}

	// Returns a claim from cirras for a claim number
	private InsuranceClaimRsrc getCirrasClaim(String claimNumber) throws CirrasPolicyServiceException {
		EndpointsRsrc policyTopLevelEndpoints = cirrasPolicyService.getTopLevelEndpoints();
		InsuranceClaimRsrc policyClaimRsrc = cirrasPolicyService.getInsuranceClaim(policyTopLevelEndpoints,
				claimNumber);
			
		return policyClaimRsrc;
	}

	// Returns a product from cirras for a claim number/product purchase
	private ProductListRsrc getCirrasClaimProducts(
			String insurancePolicyId, 
			Boolean includeProductDetails, 
			String purchaseId, 
			String commodityCoverageCode) throws CirrasPolicyServiceException {

		EndpointsRsrc policyTopLevelEndpoints = cirrasPolicyService.getTopLevelEndpoints();
		ProductListRsrc productListRsrc = cirrasPolicyService.getProducts(
										policyTopLevelEndpoints, 
										insurancePolicyId, 
										Boolean.toString(includeProductDetails), 
										purchaseId, 
										commodityCoverageCode);
			
		return productListRsrc;
	}
	
	// Updates fields in claimCalculation from insuranceClaim that are only updated
	// when the user requests a Refresh.
	private void refreshManualClaimData(ClaimCalculation claimCalculation, InsuranceClaim insuranceClaim)
			throws ServiceException {
		logger.debug("<refreshManualClaimData");

		if (claimCalculation == null || insuranceClaim == null) {
			throw new ServiceException("Unable to refresh Claim data. Claim or Claim Calculation was not loaded.");
		}

		claimCalculationFactory.updateCalculationFromClaim(claimCalculation, insuranceClaim);

		// Recalculate.
		calculateVarietyInsurableValues(claimCalculation);
		calculateTotals(claimCalculation);

		logger.debug(">refreshManualClaimData");
	}
	
	@Override
	public ClaimCalculation updateClaimCalculation(String claimCalculationGuid, String updateType,
			String optimisticLock, ClaimCalculation claimCalculation, FactoryContext factoryContext,
			WebAdeAuthentication authentication) throws ServiceException, NotFoundException, ForbiddenException,
			ConflictException, ValidationFailureException {
		logger.debug("<updateClaimCalculation");

		ClaimCalculation result = null;

		try {
			List<Message> errors = new ArrayList<Message>();
			// errors.addAll(modelValidator.validateInsuranceClaim(insuranceClaim)); // TODO

			if (!errors.isEmpty()) {
				throw new ValidationFailureException(errors);
			}

			calculateVarietyInsurableValues(claimCalculation);
			calculateTotals(claimCalculation);

			ClaimCalculationDto dto = claimCalculationDao.fetch(claimCalculationGuid);

			Boolean replaceCalculation = false;
			if (updateType != null && (updateType.equals(ClaimsServiceEnums.UpdateTypes.REPLACE_COPY.toString())
					|| updateType.equals(ClaimsServiceEnums.UpdateTypes.REPLACE_NEW.toString()))) {
				// The calculation gets replaced
				// Check if the old calculation status is approved and the new status is
				// archived
				if (dto.getCalculationStatusCode().equals(ClaimsServiceEnums.CalculationStatusCodes.APPROVED.toString())
						&& claimCalculation.getCalculationStatusCode().equals(ClaimsServiceEnums.CalculationStatusCodes.ARCHIVED.toString())) {
					replaceCalculation = true;
				} else {
					throw new ServiceException(
							"Replacing a calculation can only be done if the status changes from Approved to Archived");
				}
			}

			String userId = getUserId(authentication);
			saveUpdateClaimCalculation(claimCalculation, dto, authentication, claimCalculationGuid, userId);
			
			//Update sub table records
			updateSubTableRecords(claimCalculationGuid, claimCalculation, userId);

			// Replace calculation and return new calculation
			if (replaceCalculation) {
				result = replaceClaimCalculation(claimCalculation, updateType, factoryContext, authentication);
			} else {
				if(updateType.equals(ClaimsServiceEnums.UpdateTypes.SUBMIT.toString())) {
					//If calculation is submitted the claim in CIRRAS needs to get updated.
					submitCalculation(claimCalculation, userId);
				}
				result = getClaimCalculation(claimCalculationGuid, false, factoryContext, authentication);
			}
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("DAO threw an exception: " + e.getMessage(), e);
		} catch (CirrasPolicyServiceException e) {
			e.printStackTrace();
			throw new ServiceException("Policy service threw an exception: " + e.getMessage(), e);
		}

		logger.debug(">updateClaimCalculation");
		return result;
	}

	private void submitCalculation(ClaimCalculation claimCalculation, String userId) throws CirrasPolicyServiceException, ValidationFailureException {

		logger.debug("<cirrasPolicyService call");
		
		try {
			//Making sure the login is clean (without IDIR\
			logger.debug("userId: " + userId);
			int i = userId.lastIndexOf("\\");
			if(i >= 0) {
				userId = userId.substring(i + 1);
			}
			logger.debug("userId: " + userId);

			ClaimCalculationSubmitRsrc resource = new ClaimCalculationSubmitRsrc();
			resource.setClaimNumber(claimCalculation.getClaimNumber());
			resource.setClaimAmount(claimCalculation.getTotalClaimAmount());
			resource.setUserId(userId);

			cirrasPolicyService.claimCalculationSubmit(resource);
		} catch (ValidationException e) {
			throw new ValidationFailureException(e.getMessages());
		}

		logger.debug(">cirrasPolicyService call");
		
	}

	private void updateSubTableRecords(String claimCalculationGuid, ClaimCalculation claimCalculation, String userId)
			throws DaoException, NotFoundDaoException {
		//
		// Since the UI can add or delete varieties by using the "refresh" button, the
		// easiest
		// thing to do is just always delete and insert everything
		//
		createVarieties(claimCalculation, userId, claimCalculationGuid, true);

		// Update Grapes Data
		updateGrapesQuantity(claimCalculation, userId);
		
		// Update Berries Data
		updateBerriesQuantity(claimCalculation, userId);

		// Update Plant By Units
		updatePlantUnits(claimCalculation, userId);

		// Update Plant By Acres
		updatePlantAcres(claimCalculation, userId);
		
		// Update Grain Unseeded
		updateGrainUnseededQuantity(claimCalculation, userId);
	}

	private void updatePlantAcres(ClaimCalculation claimCalculation, String userId)
			throws DaoException, NotFoundDaoException {
		//
		// Update Plant By Acres
		//
		if (claimCalculation.getClaimCalculationPlantAcres() != null) {
			ClaimCalculationPlantAcresDto dtoPlantAcres = claimCalculationPlantAcresDao.fetch(claimCalculation.getClaimCalculationPlantAcres().getClaimCalcPlantAcresGuid());

			claimCalculationFactory.updateDto(dtoPlantAcres, claimCalculation.getClaimCalculationPlantAcres());

			claimCalculationPlantAcresDao.update(dtoPlantAcres, userId);
		}
	}

	private void updatePlantUnits(ClaimCalculation claimCalculation, String userId)
			throws DaoException, NotFoundDaoException {
		//
		// Update Plant By Units
		//
		if (claimCalculation.getClaimCalculationPlantUnits() != null) {
			ClaimCalculationPlantUnitsDto dtoPlantUnits = claimCalculationPlantUnitsDao.fetch(claimCalculation.getClaimCalculationPlantUnits().getClaimCalcPlantUnitsGuid());

			claimCalculationFactory.updateDto(dtoPlantUnits, claimCalculation.getClaimCalculationPlantUnits());

			claimCalculationPlantUnitsDao.update(dtoPlantUnits, userId);
		}
	}

	private void updateGrapesQuantity(ClaimCalculation claimCalculation, String userId)
			throws DaoException, NotFoundDaoException {
		//
		// Update Berries Data
		//
		if (claimCalculation.getClaimCalculationGrapes() != null) {
			ClaimCalculationGrapesDto dtoGrapes = claimCalculationGrapesDao.fetch(claimCalculation.getClaimCalculationGrapes().getClaimCalculationGrapesGuid());

			claimCalculationFactory.updateDto(dtoGrapes, claimCalculation.getClaimCalculationGrapes());

			claimCalculationGrapesDao.update(dtoGrapes, userId);
		}
	}
	
	private void updateGrainUnseededQuantity(ClaimCalculation claimCalculation, String userId)
			throws DaoException, NotFoundDaoException {
		//
		// Update Grain Unseeded Data
		//
		if (claimCalculation.getClaimCalculationGrainUnseeded() != null) {
			ClaimCalculationGrainUnseededDto dtoGrainUnseeded = claimCalculationGrainUnseededDao.fetch(claimCalculation.getClaimCalculationGrainUnseeded().getClaimCalculationGrainUnseededGuid());

			claimCalculationFactory.updateDto(dtoGrainUnseeded, claimCalculation.getClaimCalculationGrainUnseeded());

			claimCalculationGrainUnseededDao.update(dtoGrainUnseeded, userId);
		}
	}
	
	private void updateBerriesQuantity(ClaimCalculation claimCalculation, String userId)
			throws DaoException, NotFoundDaoException {
		//
		// Update Berries Data
		//
		if (claimCalculation.getClaimCalculationBerries() != null) {
			ClaimCalculationBerriesDto dtoBerries = claimCalculationBerriesDao.fetch(claimCalculation.getClaimCalculationBerries().getClaimCalculationBerriesGuid());

			claimCalculationFactory.updateDto(dtoBerries, claimCalculation.getClaimCalculationBerries());

			claimCalculationBerriesDao.update(dtoBerries, userId);
		}
	}

	private void saveUpdateClaimCalculation(ClaimCalculation claimCalculation, ClaimCalculationDto dto,
			WebAdeAuthentication authentication, String claimCalculationGuid, String userId)
			throws NotFoundException, DaoException, NotFoundDaoException {

		claimCalculationFactory.updateDto(dto, claimCalculation);

		ClaimCalculationUserDto dtoUser = cirrasServiceHelper.getClaimCalculationUserDto(authentication);

		if (dtoUser != null) {
			dto.setUpdateClaimCalcUserGuid(dtoUser.getClaimCalculationUserGuid());
		} else {
			dto.setUpdateClaimCalcUserGuid(null);
		}

		claimCalculationDao.update(claimCalculationGuid, dto, userId);
	}

	private ClaimCalculation replaceClaimCalculation(ClaimCalculation claimCalculation, String updateType,
			FactoryContext factoryContext, WebAdeAuthentication authentication)
			throws ServiceException, CirrasPolicyServiceException, NotFoundException, ValidationFailureException {

		logger.debug("<replaceClaimCalculation");

		ClaimCalculation result = null;

		if (claimCalculation == null) {
			throw new ServiceException(
					"Unable to replace Claim Calculation. Existing Claim Calculation was not loaded.");
		}

		if (updateType == null) {
			throw new ServiceException("A replace type needs to be set to replace a calculation.");
		}

		// Get claims data from cirras
		// It's only possible to replace a calculation if the claim is in status open
		InsuranceClaimRsrc policyClaimRsrc = getCirrasClaim(claimCalculation.getClaimNumber().toString());

		if (policyClaimRsrc == null) {
			throw new NotFoundException("no claim found for " + claimCalculation.getClaimNumber().toString());
		} else {
			if (Boolean.TRUE.equals(policyClaimRsrc.getHasChequeReqInd())) {
				if (!policyClaimRsrc.getClaimStatusCode().equals(ClaimsServiceEnums.ClaimStatusCodes.InProgress.getClaimStatusCode())) {

					throw new ServiceException("Claim: " + claimCalculation.getClaimNumber().toString()
							+ " needs to be in status In Progress to add a calculation");
				}
			} else {
				if (!policyClaimRsrc.getClaimStatusCode().equals(ClaimsServiceEnums.ClaimStatusCodes.Open.getClaimStatusCode())) {

					throw new ServiceException("Claim: " + claimCalculation.getClaimNumber().toString()
							+ " needs to be in status Open to add a calculation");
				}				
			}			
		}

		if (updateType.equals(ClaimsServiceEnums.UpdateTypes.REPLACE_COPY.toString())) {
			// Replacement is based on the archived calculation
			result = claimCalculationFactory.getCalculationFromCalculation(claimCalculation, factoryContext,
					authentication);

			// Need to set current claim data: status, type, monitored, recommended and
			// approved
			result.setClaimStatusCode(policyClaimRsrc.getClaimStatusCode());
			result.setClaimType(policyClaimRsrc.getClaimType());
			result.setSubmittedByUserid(policyClaimRsrc.getSubmittedByUserid());
			result.setSubmittedByName(policyClaimRsrc.getSubmittedByName());
			result.setSubmittedByDate(policyClaimRsrc.getSubmittedByDate());
			result.setRecommendedByUserid(policyClaimRsrc.getRecommendedByUserid());
			result.setRecommendedByName(policyClaimRsrc.getRecommendedByName());
			result.setRecommendedByDate(policyClaimRsrc.getRecommendedByDate());
			result.setApprovedByUserid(policyClaimRsrc.getApprovedByUserid());
			result.setApprovedByName(policyClaimRsrc.getApprovedByName());
			result.setApprovedByDate(policyClaimRsrc.getApprovedByDate());
			result.setHasChequeReqInd(policyClaimRsrc.getHasChequeReqInd());

		} else if (updateType.equals(ClaimsServiceEnums.UpdateTypes.REPLACE_NEW.toString())) {

			ProductRsrc productRsrc = null;

			if (policyClaimRsrc.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAIN.toString())
					&& policyClaimRsrc.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.CropUnseeded.getCode())) {
				
//				ProductListRsrc productListRsrc = getCirrasClaimProducts(
//						policyClaimRsrc.getInsurancePolicyId().toString(), 
//						true,
//						policyClaimRsrc.getPurchaseId().toString(),
//						null);
//				if (productListRsrc == null) {
//					throw new NotFoundException("no product found for " + claimNumber);
//				}
			}			
			
			// Replacement is based on the current claim and policy data in CIRRAS
			result = claimCalculationFactory.getCalculationFromClaim(policyClaimRsrc, productRsrc, factoryContext, authentication);

		}

		// Recalculate. -> MH 2022/01/04: not necessary because calculation are done in createClaimCalculation as well
		//calculateVarietyInsurableValues(result);
		//calculateTotals(result);

		// Save
		result = createClaimCalculation(result, factoryContext, authentication);

		logger.debug(">replaceClaimCalculation");

		return result;
	}
	
	private void setAveragePriceFinal(ClaimCalculationVariety claimCalculationVariety) {
		//Set final average price: Set it with the override price if there is a value. If not use the average price from CIRRAS (could be null) 
		if(claimCalculationVariety != null && claimCalculationVariety.getAveragePriceOverride() != null) {
			claimCalculationVariety.setAveragePriceFinal(claimCalculationVariety.getAveragePriceOverride());
		} else {
			claimCalculationVariety.setAveragePriceFinal(claimCalculationVariety.getAveragePrice());
		}
			
	}

	private void calculateVarietyInsurableValues(ClaimCalculation claimCalculation) {
		// If commodity uses IIV calculate the varieties IV
		// Else set the purchase's selected IV
		if(claimCalculation.getClaimCalculationGrapes() != null) {
			if (claimCalculation.getVarieties() != null && claimCalculation.getVarieties().size() > 0) {
				if (claimCalculation.getCalculateIivInd().equalsIgnoreCase("Y")) {
					
					double ivRatio = claimCalculation.getClaimCalculationGrapes().getInsurableValueSelected().doubleValue() / claimCalculation.getClaimCalculationGrapes().getInsurableValueHundredPercent().doubleValue();
					for (ClaimCalculationVariety variety : claimCalculation.getVarieties()) {
						
						//Set/Update final average price used in the IIV calculation
						setAveragePriceFinal(variety);

						// if the commodity uses IIV if there is no average price the IV is null other
						// wise it's calculated
						if (variety.getAveragePriceFinal() == null) {
							variety.setInsurableValue(null);
						} else {
							double iv = variety.getAveragePriceFinal().doubleValue() * ivRatio;
							double ivRoundedUp = (double) Math.round(iv * 10000d) / 10000d;
							variety.setInsurableValue(new Double(ivRoundedUp));
						}
					}
				} else {
					for (ClaimCalculationVariety variety : claimCalculation.getVarieties()) {
						variety.setInsurableValue(claimCalculation.getClaimCalculationGrapes().getInsurableValueSelected());
					}
				}
			}
		}
	}

	@Override
	public void deleteClaimCalculation(String claimCalculationGuid, String optimisticLock,
			WebAdeAuthentication authentication)
			throws ServiceException, NotFoundException, ForbiddenException, ConflictException {
		logger.debug("<deleteClaimCalculation");
		
		cirrasServiceHelper.deleteClaimCalculation(claimCalculationGuid);

		logger.debug(">deleteClaimCalculation");
	}

	@Override
	public ClaimCalculationList<? extends ClaimCalculation> getClaimCalculationList(Integer claimNumber,
			String policyNumber, Integer cropYear, String calculationStatusCode, String createClaimCalcUserGuid,
			String updateClaimCalcUserGuid, Integer insurancePlanId, String sortColumn, String sortDirection,
			Integer pageNumber, Integer pageRowCount, FactoryContext context, WebAdeAuthentication authentication)
			throws ServiceException, MaxResultsExceededException {
		ClaimCalculationList<? extends ClaimCalculation> results = null;

		try {
			int maximumRows = DefaultMaximumResults;

			PagedDtos<ClaimCalculationDto> dtos = claimCalculationDao.select(claimNumber, policyNumber, cropYear,
					calculationStatusCode, createClaimCalcUserGuid, updateClaimCalcUserGuid, insurancePlanId,
					sortColumn, sortDirection, maximumRows, pageNumber, pageRowCount);

			results = claimCalculationFactory.getClaimCalculationList(dtos, claimNumber, policyNumber, cropYear,
					calculationStatusCode, createClaimCalcUserGuid, updateClaimCalcUserGuid, insurancePlanId,
					sortColumn, sortDirection, pageRowCount, context, authentication);

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		} catch (TooManyRecordsException e) {
			throw new MaxResultsExceededException(e.getMessage(), e);
		}

		return results;
	}

	private void calculateTotals(ClaimCalculation claimCalculation) {
		if (claimCalculation.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAPES.toString())) {
			// Calculate totals for Grapes
			calculateTotalsGrapes(claimCalculation);
		} else if (claimCalculation.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.BERRIES.toString())
				&& claimCalculation.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.Quantity.getCode())) {
			// Calculate totals for Berries
			calculateTotalsBerries(claimCalculation);
		} else if (claimCalculation.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.Plant.getCode())) {
			// Calculate totals for Plants
			//Depending on measurement type
			if(claimCalculation.getInsuredByMeasurementType().equalsIgnoreCase(ClaimsServiceEnums.InsuredByMeasurementType.UNITS.toString())) {
				calculateTotalsPlantUnits(claimCalculation);
			} else if(claimCalculation.getInsuredByMeasurementType().equalsIgnoreCase(ClaimsServiceEnums.InsuredByMeasurementType.ACRES.toString())) {
				calculateTotalsPlantAcres(claimCalculation);
			}
		} else if (claimCalculation.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAIN.toString())
				&& claimCalculation.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.CropUnseeded.getCode())) {
			// Calculate totals for Grain Unseeded
			calculateTotalsGrainUnseeded(claimCalculation);
		}
	}

	private void calculateTotalsGrainUnseeded(ClaimCalculation claimCalculation) {
		ClaimCalculationGrainUnseeded grainUnseeded = claimCalculation.getClaimCalculationGrainUnseeded();
		
		//Adjusted Acres: Total Acres Insured - Less Adjustment Acres
		//0 if negative
		Double adjustedAcres = Math.max(0, (notNull(grainUnseeded.getInsuredAcres(), (double)0) - notNull(grainUnseeded.getLessAdjustmentAcres(), (double)0)));
		grainUnseeded.setAdjustedAcres(adjustedAcres);
		
		//Deductible Acres: Adjusted Acres * Deductible %
		Double deductibleAcres = adjustedAcres * (notNull((double)grainUnseeded.getDeductibleLevel(), (double)0)/(double)100);
		grainUnseeded.setDeductibleAcres(deductibleAcres);
		
		//Max Number of Eligible Acres: Adjusted Acres - Deductible Acres
		Double maxEligibleAcres = adjustedAcres - deductibleAcres;
		grainUnseeded.setMaxEligibleAcres(maxEligibleAcres);
		
		//Coverage Value: Max Number of Eligible Acres * Insured Value per Acre
		Double coverageValue = maxEligibleAcres * notNull(grainUnseeded.getInsurableValue(), (double)0);
		grainUnseeded.setCoverageValue(coverageValue);
		
		//Eligible Unseeded Acres: Unseeded Acres - Less Assessment - Less Deductible Acres
		//0 if negative
		Double eligibleUnseededAcres =  Math.max(0, (notNull(grainUnseeded.getUnseededAcres(), (double)0) - notNull(grainUnseeded.getLessAssessmentAcres(), (double)0) - deductibleAcres));
		grainUnseeded.setEligibleUnseededAcres(eligibleUnseededAcres);
		
		//Plant Loss Claim: Eligible Unseeded Acres * Insured Value per Acre 
		Double totalClaimAmount = eligibleUnseededAcres * notNull(grainUnseeded.getInsurableValue(), (double)0);
		claimCalculation.setTotalClaimAmount(totalClaimAmount);
		
	}
	
	private void calculateTotalsPlantUnits(ClaimCalculation claimCalculation) {
		ClaimCalculationPlantUnits plantUnits = claimCalculation.getClaimCalculationPlantUnits();
		
		//Adjusted Plant Inventory: Total Plant Inventory - Less Adjustment For
		Double adjustedUnits = notNull(plantUnits.getInsuredUnits(), (double) 0) - notNull(plantUnits.getLessAdjustmentUnits(), 0);
		plantUnits.setAdjustedUnits(adjustedUnits);

		//Deductible Plants: Adjusted Plant Inventory * Deductible Level/100
		Double deductibleUnits = adjustedUnits * notNull(plantUnits.getDeductibleLevel(), 0) / 100;
		plantUnits.setDeductibleUnits(deductibleUnits);
		
		//Max Number of Plants Eligible for Claim: Adjusted Plant Inventory - Deductible Plants
		plantUnits.setTotalCoverageUnits(Math.max(0, adjustedUnits - deductibleUnits));
		
		//Coverage Value: Max Number of Plants Eligible for Claim * insurable value
		Double coverageAmount = plantUnits.getTotalCoverageUnits() * notNull(plantUnits.getInsurableValue(), (double)0);
		plantUnits.setCoverageAmount((double)Math.round(coverageAmount));
		
		//Total Damaged Plants = Damaged Plants - Less Assessment For - Deductible Plants
		Double totalDamagedUnits = notNull(plantUnits.getDamagedUnits(), 0) - notNull(plantUnits.getLessAssessmentUnits(), 0) - deductibleUnits;
		if(totalDamagedUnits < 0) {
			totalDamagedUnits = (double)0;
		}
		plantUnits.setTotalDamagedUnits(totalDamagedUnits);
		
		//Plant Loss Claim: Total Damaged Plants * insurable Value
		Double totalClaimAmount = totalDamagedUnits * notNull(plantUnits.getInsurableValue(), (double)0);
		claimCalculation.setTotalClaimAmount((double)Math.round(totalClaimAmount));
		
		claimCalculation.setClaimCalculationPlantUnits(plantUnits);
	}

	private void calculateTotalsPlantAcres(ClaimCalculation claimCalculation) {
		ClaimCalculationPlantAcres plantAcres = claimCalculation.getClaimCalculationPlantAcres();
		
		//Insured Acres: Lessor of Declared Acres and Confirmed Acres
		Double insuredAcres = notNull(plantAcres.getDeclaredAcres(), (double)0);
		if(insuredAcres > 0 && plantAcres.getConfirmedAcres() != null) {
			if(insuredAcres > plantAcres.getConfirmedAcres()) {
				insuredAcres = plantAcres.getConfirmedAcres();
			}
		}
		plantAcres.setInsuredAcres(insuredAcres);
		
		//Deductible Acres: Insured Acres * Deductible Level/100
		Double deductibleAcres = insuredAcres * notNull(plantAcres.getDeductibleLevel(), 0) / 100;
		plantAcres.setDeductibleAcres(deductibleAcres);
		
		//Acre Coverage: Insured Acres - Deductible Acres
		plantAcres.setTotalCoverageAcres(Math.max(0, insuredAcres - deductibleAcres)); 

		//Acre Loss Covered: Damaged Acres - Deductible Acres
		Double acresLossCovered = notNull(plantAcres.getDamagedAcres(), (double)0) - deductibleAcres;
		if(acresLossCovered < 0) {
			acresLossCovered = (double)0;
		}
		plantAcres.setAcresLossCovered(acresLossCovered);
		
		//Acreage Loss Claim Value: Acre Loss Covered * insurable value
		Double coverageAmount = acresLossCovered * notNull(plantAcres.getInsurableValue(), (double)0);
		plantAcres.setCoverageAmount(coverageAmount);
		
		//Claim Amount: Acreage Loss Claim Value - Less Assessment For
		Double totalClaimAmount = coverageAmount - notNull(plantAcres.getLessAssessmentAmount(), (double)0);
		if(totalClaimAmount < 0) {
			totalClaimAmount = (double)0;
		}
		claimCalculation.setTotalClaimAmount(totalClaimAmount);
		
		claimCalculation.setClaimCalculationPlantAcres(plantAcres);
	}

	private void calculateTotalsBerries(ClaimCalculation claimCalculation) {

		// Adjustment Factor = Confirmed Acres / Declared Acres
		// Default = 1
		Double adjustmentFactor = new Double(1);
		if (claimCalculation.getClaimCalculationBerries().getConfirmedAcres() != null
				&& (claimCalculation.getClaimCalculationBerries().getDeclaredAcres() != null
						&& claimCalculation.getClaimCalculationBerries().getDeclaredAcres() > 0)) {
			
			adjustmentFactor = claimCalculation.getClaimCalculationBerries().getConfirmedAcres()
					/ claimCalculation.getClaimCalculationBerries().getDeclaredAcres();
			
			if ( adjustmentFactor > 1 ) {
				adjustmentFactor = (double)1 ;
			}
		}
		claimCalculation.getClaimCalculationBerries().setAdjustmentFactor(adjustmentFactor);

		// Adjusted Production Guarantee = Production Guarantee * Adjustment Factor
		//Round to zero decimals
		Double adjustedProductionGuarantee = (double)Math.round(claimCalculation.getClaimCalculationBerries().getProductionGuarantee()
				* claimCalculation.getClaimCalculationBerries().getAdjustmentFactor());
		claimCalculation.getClaimCalculationBerries().setAdjustedProductionGuarantee(adjustedProductionGuarantee);

		// Coverage Value = Adjusted Production Guarantee * Selected IV
		//Round to zero decimals
		Double coverageValue = (double)Math.round(claimCalculation.getClaimCalculationBerries().getAdjustedProductionGuarantee()
				* claimCalculation.getClaimCalculationBerries().getInsurableValueSelected());
		
		//The calculated coverage value needs to match the coverage value in CIRRAS if the declared acres are confirmed to be the same
		//or if there is no confirmed acres value
		Boolean declaredAcresAreCorrect = claimCalculation.getClaimCalculationBerries().getConfirmedAcres() == null 
									|| Double.compare(claimCalculation.getClaimCalculationBerries().getDeclaredAcres(), claimCalculation.getClaimCalculationBerries().getConfirmedAcres()) == 0; 
		
		//The calculated coverage value can't exceed the coverage value in CIRRAS which is stored in MaxCoverageAmount
		if(coverageValue > notNull(claimCalculation.getClaimCalculationBerries().getMaxCoverageAmount(), (double) 0)
				|| declaredAcresAreCorrect) {
			coverageValue = claimCalculation.getClaimCalculationBerries().getMaxCoverageAmount();
		} 
		
		claimCalculation.getClaimCalculationBerries().setCoverageAmountAdjusted(coverageValue);

		// Total Yield From DOP = Harvested Yield + Appraised Yield + Abandoned Yield
		//Round to zero decimals
		Double totalYieldFromDop = (double)Math.round(notNull(claimCalculation.getClaimCalculationBerries().getHarvestedYield(),
				(double) 0) + notNull(claimCalculation.getClaimCalculationBerries().getAppraisedYield(), (double) 0)
				+ notNull(claimCalculation.getClaimCalculationBerries().getAbandonedYield(), (double) 0));
		claimCalculation.getClaimCalculationBerries().setTotalYieldFromDop(totalYieldFromDop);

		// Total Yield for Claim Calculation = (Total Yield from Adjuster if it's not
		// null else Total Yield from DOP) + Yield Assessment
		//Round to zero decimals
		Double totalYieldForCalculation = claimCalculation.getClaimCalculationBerries().getTotalYieldFromAdjuster();
		if (claimCalculation.getClaimCalculationBerries().getTotalYieldFromAdjuster() == null) {
			totalYieldForCalculation = totalYieldFromDop;
		}
		totalYieldForCalculation = (double)Math.round(notNull(totalYieldForCalculation, (double) 0)
				+ notNull(claimCalculation.getClaimCalculationBerries().getYieldAssessment(), (double) 0));
		claimCalculation.getClaimCalculationBerries().setTotalYieldForCalculation(totalYieldForCalculation);

		// Yield Loss Eligible for Claim = Adjusted Production Guarantee - Total Yield
		// for Claim Calculation
		//Round to zero decimals
		Double YieldLossEligibleForClaim = (double)Math.max(0, Math.round(claimCalculation.getClaimCalculationBerries()
				.getAdjustedProductionGuarantee() - totalYieldForCalculation));
		claimCalculation.getClaimCalculationBerries().setYieldLossEligible(YieldLossEligibleForClaim);

		// Quantity Loss Claim = Selected IV * Yield Loss Eligible for Claim
		Double quantityLossClaim = claimCalculation.getClaimCalculationBerries().getInsurableValueSelected() * YieldLossEligibleForClaim;
		//If the calculated claim amount is higher than the coverage value (Line I)
		//OR if the adjusted production guarantee (Line G) equals Yield Loss Eligible for Claim (Line M) 
		//then the Quantity Loss Claim (Line N) has to match the Coverage Value (Line I)
		if(quantityLossClaim > claimCalculation.getClaimCalculationBerries().getCoverageAmountAdjusted()
				|| Double.compare(claimCalculation.getClaimCalculationBerries().getAdjustedProductionGuarantee(), claimCalculation.getClaimCalculationBerries().getYieldLossEligible()) == 0) {
			quantityLossClaim = claimCalculation.getClaimCalculationBerries().getCoverageAmountAdjusted();
		}
		claimCalculation.setTotalClaimAmount(notNull(quantityLossClaim, (double) 0));
	}

	private void calculateTotalsGrapes(ClaimCalculation claimCalculation) {
		double totalVarietyProductionValue = 0;
		boolean atLeastOneVareity = false;
		
		for (ClaimCalculationVariety v : claimCalculation.getVarieties()) {
			double varietyYieldTotal = -1;
			boolean atLeastOneYield = false;

			if (v.getYieldActual() != null) {
				varietyYieldTotal = v.getYieldActual().doubleValue();
				atLeastOneYield = true;
			}

			if (v.getYieldAssessed() != null) {
				if (atLeastOneYield == false) {
					varietyYieldTotal = v.getYieldAssessed().doubleValue();
					atLeastOneYield = true;
				} else {
					varietyYieldTotal += v.getYieldAssessed().doubleValue();
				}
			}

			if (atLeastOneYield) {
				double varietyProductionValue = varietyYieldTotal * v.getInsurableValue().doubleValue();
				v.setYieldTotal(new Double(varietyYieldTotal));
				v.setVarietyProductionValue(new Double(varietyProductionValue));

				totalVarietyProductionValue += varietyProductionValue;
				atLeastOneVareity = true;
			}
		}

		claimCalculation.getClaimCalculationGrapes().setTotalProductionValue(new Double(totalVarietyProductionValue));

		double coverageAmount = claimCalculation.getClaimCalculationGrapes().getCoverageAmount().doubleValue();
		double coverageAmountAdjusted = coverageAmount;

		if (claimCalculation.getClaimCalculationGrapes().getCoverageAmountAssessed() != null) {
			// in this case the assesment is subtracted
			coverageAmountAdjusted = coverageAmount - claimCalculation.getClaimCalculationGrapes().getCoverageAmountAssessed().doubleValue();
		}
		claimCalculation.getClaimCalculationGrapes().setCoverageAmountAdjusted(new Double(coverageAmountAdjusted));

		if (atLeastOneVareity) {
			double totalClaimAmount = coverageAmountAdjusted - totalVarietyProductionValue;

			if (totalClaimAmount < 0) {
				totalClaimAmount = 0;
			}
			claimCalculation.setTotalClaimAmount(new Double(totalClaimAmount));
		} else {
			claimCalculation.setTotalClaimAmount(claimCalculation.getClaimCalculationGrapes().getCoverageAmountAdjusted());
		}
	}

	private Double notNull(Double value, Double defaultValue) {
		return (value == null) ? defaultValue : value;
	}

	private Integer notNull(Integer value, Integer defaultValue) {
		return (value == null) ? defaultValue : value;
	}
}
