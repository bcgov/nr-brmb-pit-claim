package ca.bc.gov.mal.cirras.claims.service.api.v1.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.mal.cirras.claims.model.v1.Claim;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimList;

import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculation;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationGrainQuantity;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationGrainQuantityDetail;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationGrainSpotLoss;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationGrainUnseeded;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationList;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationPlantAcres;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationPlantUnits;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationVariety;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationBerriesDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationGrainBasketDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationGrainBasketProductDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationGrainQuantityDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationGrainQuantityDetailDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationGrainSpotLossDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationGrainUnseededDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationGrapesDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationPlantAcresDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationPlantUnitsDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationVarietyDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.CropCommodityDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationUserDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationBerriesDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationGrainQuantityDetailDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationGrainQuantityDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationGrainSpotLossDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationGrainUnseededDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationGrapesDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationPlantAcresDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationPlantUnitsDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationVarietyDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.CropCommodityDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationUserDto;
import ca.bc.gov.mal.cirras.claims.service.api.v1.CirrasClaimService;
import ca.bc.gov.mal.cirras.claims.service.api.v1.model.factory.ClaimCalculationFactory;
import ca.bc.gov.mal.cirras.claims.service.api.v1.model.factory.ClaimFactory;
import ca.bc.gov.mal.cirras.claims.service.api.v1.util.ClaimsServiceEnums;
import ca.bc.gov.mal.cirras.claims.service.api.v1.util.CirrasServiceHelper;
import ca.bc.gov.mal.cirras.claims.service.api.v1.util.OutOfSync;
import ca.bc.gov.mal.cirras.claims.service.api.v1.util.ClaimsServiceEnums.CalculationStatusCodes;
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
import ca.bc.gov.mal.cirras.policies.model.v1.Product;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingService;
import ca.bc.gov.mal.cirras.underwriting.api.rest.client.v1.CirrasUnderwritingServiceException;
import ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.VerifiedYieldContractSimpleRsrc;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldContractSimple;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldSummary;
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
	private ClaimCalculationGrainSpotLossDao claimCalculationGrainSpotLossDao;
	private ClaimCalculationGrainQuantityDao claimCalculationGrainQuantityDao;
	private ClaimCalculationGrainQuantityDetailDao claimCalculationGrainQuantityDetailDao;
	private ClaimCalculationGrainBasketDao claimCalculationGrainBasketDao;
	private ClaimCalculationGrainBasketProductDao claimCalculationGrainBasketProductDao;
	private ClaimCalculationUserDao claimCalculationUserDao;
	private ClaimDao claimDao;
	private CropCommodityDao cropCommodityDao;

	// services
	private CirrasPolicyService cirrasPolicyService;
	private CirrasUnderwritingService cirrasUnderwritingService;

	
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

	public void setCirrasUnderwritingService(CirrasUnderwritingService cirrasUnderwritingService) {
		this.cirrasUnderwritingService = cirrasUnderwritingService;
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

	public void setClaimCalculationGrainSpotLossDao(ClaimCalculationGrainSpotLossDao claimCalculationGrainSpotLossDao) {
		this.claimCalculationGrainSpotLossDao = claimCalculationGrainSpotLossDao;
	}

	public void setClaimCalculationGrainQuantityDao(ClaimCalculationGrainQuantityDao claimCalculationGrainQuantityDao) {
		this.claimCalculationGrainQuantityDao = claimCalculationGrainQuantityDao;
	}

	public void setClaimCalculationGrainQuantityDetailDao(ClaimCalculationGrainQuantityDetailDao claimCalculationGrainQuantityDetailDao) {
		this.claimCalculationGrainQuantityDetailDao = claimCalculationGrainQuantityDetailDao;
	}
	
	public void setClaimCalculationGrainBasketDao(ClaimCalculationGrainBasketDao claimCalculationGrainBasketDao) {
		this.claimCalculationGrainBasketDao = claimCalculationGrainBasketDao;
	}

	public void setClaimCalculationGrainBasketProductDao(ClaimCalculationGrainBasketProductDao claimCalculationGrainBasketProductDao) {
		this.claimCalculationGrainBasketProductDao = claimCalculationGrainBasketProductDao;
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

	public void setCropCommodityDao(CropCommodityDao cropCommodityDao) {
		this.cropCommodityDao = cropCommodityDao;
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
			
			CropCommodityDto crpDto = null;
			CropCommodityDto linkedCrpDto = null;   // For example, if crpDto is BARLEY, then linkedCrpDto is BARLEY - PEDIGREED. Or vice-versa.
			ProductRsrc productRsrc = null;
			ProductListRsrc productListRsrc = null;
			VerifiedYieldContractSimpleRsrc verifiedYieldRsrc = null;

			// Populated for Grain Basket only.
			List<ProductRsrc> quantityProducts = null;
			Map<Integer, ClaimDto> quantityClaimMap = null;       // Maps crop id to Claim
			Map<Integer, CropCommodityDto> quantityCropMap = null;   // Maps crop id to CropCommodity
			Map<Integer, CropCommodityDto> quantityLinkedCropMap = null;  // Maps crop id to linked CropCommodity.
			
			
			if (policyClaimRsrc.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAIN.toString())) {
			
				if (policyClaimRsrc.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.CropUnseeded.getCode())
					  || policyClaimRsrc.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.GrainSpotLoss.getCode())) {
	
					productListRsrc = getCirrasClaimProducts(
															policyClaimRsrc.getInsurancePolicyId().toString(), 
															true,
															policyClaimRsrc.getPurchaseId().toString(),
															null);
					if (productListRsrc == null) {
						throw new NotFoundException("no product found for " + claimNumber);
					}
					
					productRsrc = productListRsrc.getCollection().get(0);
				}
	
				else if (policyClaimRsrc.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.QuantityGrain.getCode())) {
	
					productListRsrc = getCirrasClaimProducts(
															policyClaimRsrc.getInsurancePolicyId().toString(), 
															true,
															null,
															ClaimsServiceEnums.CommodityCoverageCodes.QuantityGrain.getCode());
					if (productListRsrc != null) {
						productRsrc = getProductById(productListRsrc, policyClaimRsrc.getPurchaseId());
					}
					
					if ( productRsrc == null ) { 
						throw new ServiceException("No product found for " + claimNumber);
					}
					
					crpDto = cropCommodityDao.fetch(policyClaimRsrc.getCropCommodityId());
					
					if ( crpDto == null ) {
						throw new ServiceException("No commodity found for " + claimNumber);
					}

					linkedCrpDto = cropCommodityDao.getLinkedCommodityByPedigree(policyClaimRsrc.getCropCommodityId());
										
					verifiedYieldRsrc = getUnderwritingVerifiedYield(policyClaimRsrc, null, null, false, true, true, false);
					if (verifiedYieldRsrc == null ) {
						throw new ServiceException("No verified yield found for " + claimNumber);
					}
				}

				else if (policyClaimRsrc.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.GrainBasket.getCode())) {
					
					productListRsrc = getCirrasClaimProducts(policyClaimRsrc.getInsurancePolicyId().toString(), true, null, null);
					if (productListRsrc != null) {
						productRsrc = getProductById(productListRsrc, policyClaimRsrc.getPurchaseId());
					}
					
					if ( productRsrc == null ) { 
						throw new ServiceException("No product found for " + claimNumber);
					}
					
					quantityProducts = new ArrayList<ProductRsrc>();
					quantityClaimMap = new HashMap<Integer, ClaimDto>();
					quantityCropMap = new HashMap<Integer, CropCommodityDto>();
					quantityLinkedCropMap = new HashMap<Integer, CropCommodityDto>();

					for ( ProductRsrc prd : productListRsrc.getCollection() ) {
						if ( prd.getCommodityCoverageCode().equals(ClaimsServiceEnums.CommodityCoverageCodes.QuantityGrain.getCode()) ) {
							quantityProducts.add(prd);
						
							CropCommodityDto qtyCrpDto = cropCommodityDao.fetch(prd.getCropCommodityId());						
							if ( qtyCrpDto == null ) {
								throw new ServiceException("No commodity found for " + prd.getCommodityName());
							} else {
								quantityCropMap.put(prd.getCropCommodityId(), qtyCrpDto);
							}

							CropCommodityDto qtyLinkedCrpDto = cropCommodityDao.getLinkedCommodityByPedigree(prd.getCropCommodityId());
							if ( qtyLinkedCrpDto != null ) {
								quantityLinkedCropMap.put(prd.getCropCommodityId(), qtyLinkedCrpDto);
							}
						}						
					}
					
					if ( !quantityProducts.isEmpty() ) {
						List<ClaimDto> quantityClaims = claimDao.selectQuantityClaimsByPolicyId(policyClaimRsrc.getInsurancePolicyId());
						
						if ( quantityClaims != null ) {
							for ( ClaimDto qtyClaimDto : quantityClaims ) {
								if ( quantityClaimMap.put(qtyClaimDto.getCropCommodityId(), qtyClaimDto) != null ) {
									throw new ServiceException("Found multiple quantity claims for the same commodity");
								}
							}
						}				
					}

					verifiedYieldRsrc = getUnderwritingVerifiedYield(policyClaimRsrc, null, null, false, false, true, true);
					if (verifiedYieldRsrc == null ) {
						throw new ServiceException("No verified yield found for " + claimNumber);
					}
				}
			
			}
			
			// Convert InsuranceClaimRsrc to ClaimCalculation
			result = claimCalculationFactory.getCalculationFromClaim(policyClaimRsrc, 
					                                                 productRsrc, 
					                                                 crpDto, 
					                                                 linkedCrpDto, 
					                                                 verifiedYieldRsrc, 
					                                                 quantityProducts, 
					                                                 quantityClaimMap, 
					                                                 quantityCropMap, 
					                                                 quantityLinkedCropMap, 
					                                                 context, 
					                                                 authentication);

			result.setCalculationVersion(nextVersionNumber);
			result.setCalculationStatusCode(ClaimsServiceEnums.CalculationStatusCodes.DRAFT.toString());
			result.setClaimCalculationGuid(null);

			// Calculate variety iv
			calculateVarietyInsurableValues(result);

			// Set fields from linked calculation, if any.
			if (policyClaimRsrc.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAIN.toString()) && 
					policyClaimRsrc.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.QuantityGrain.getCode())) {
				updateFromLinkedCalculation(result, policyClaimRsrc, productListRsrc, linkedCrpDto, true);
			}

		} catch (CirrasPolicyServiceException e) {
			throw new ServiceException("Policy service threw an exception (CirrasPolicyServiceException)", e);
		} catch (CirrasUnderwritingServiceException e) {
			throw new ServiceException("Underwriting service threw an exception (CirrasUnderwritingServiceException)", e);
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
			
			String userId = getUserId(authentication);
			
			//Insert or update shared grain quantity record
			if (claimCalculation.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAIN.toString())
					&& claimCalculation.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.QuantityGrain.getCode())) {
				createUpdateGrainQuantity(claimCalculation, userId);
			}

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

		// Insert Grain Spot Loss data
		createGrainSpotLoss(claimCalculation, userId, claimCalculationGuid);

		// Insert Grain Quantity Detail data
		createGrainQuantityDetail(claimCalculation, userId, claimCalculationGuid);
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

	private void createGrainSpotLoss(ClaimCalculation claimCalculation, String userId, String claimCalculationGuid)
			throws DaoException {
		//
		// Insert Grain Spot Loss Data
		//
		if (claimCalculation.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAIN.toString())
				&& claimCalculation.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.GrainSpotLoss.getCode())) {
				
			ClaimCalculationGrainSpotLossDto dtoGrainSpotLoss = claimCalculationFactory.createDto(claimCalculation.getClaimCalculationGrainSpotLoss());

			dtoGrainSpotLoss.setClaimCalculationGrainSpotLossGuid(null);
			dtoGrainSpotLoss.setClaimCalculationGuid(claimCalculationGuid);
			claimCalculationGrainSpotLossDao.insert(dtoGrainSpotLoss, userId);
		}
	}
	
	private void createGrainQuantity(ClaimCalculation claimCalculation, String userId)
			throws DaoException {
		//
		// Insert Grain Quantity Loss Data
		//
		if (claimCalculation.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAIN.toString())
				&& claimCalculation.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.QuantityGrain.getCode())) {
				
			ClaimCalculationGrainQuantityDto dtoGrainQuantity = claimCalculationFactory.createDto(claimCalculation.getClaimCalculationGrainQuantity());

			dtoGrainQuantity.setClaimCalculationGrainQuantityGuid(null);
			claimCalculationGrainQuantityDao.insert(dtoGrainQuantity, userId);
			
			claimCalculation.setClaimCalculationGrainQuantityGuid(dtoGrainQuantity.getClaimCalculationGrainQuantityGuid());
		}
	}
	
	private void createGrainQuantityDetail(ClaimCalculation claimCalculation, String userId, String claimCalculationGuid)
			throws DaoException {
		//
		// Insert Grain Quantity Detail Data
		//
		if (claimCalculation.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAIN.toString())
				&& claimCalculation.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.QuantityGrain.getCode())) {
				
			ClaimCalculationGrainQuantityDetailDto dtoGrainQuantityDetail = claimCalculationFactory.createDto(claimCalculation.getClaimCalculationGrainQuantityDetail());

			dtoGrainQuantityDetail.setClaimCalculationGrainQuantityDetailGuid(null);
			dtoGrainQuantityDetail.setClaimCalculationGuid(claimCalculationGuid);
			claimCalculationGrainQuantityDetailDao.insert(dtoGrainQuantityDetail, userId);
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

				CropCommodityDto crpDto = null;
				CropCommodityDto linkedCrpDto = null;   // For example, if crpDto is BARLEY, then linkedCrpDto is BARLEY - PEDIGREED. Or vice-versa.
				ProductRsrc policyProductRsrc = null;
				ProductListRsrc productListRsrc = null;
				VerifiedYieldContractSimpleRsrc verifiedYieldRsrc = null;

				if (policyClaimRsrc.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAIN.toString())
						&& policyClaimRsrc.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.QuantityGrain.getCode())) 
				{

					try { 
						productListRsrc = getCirrasClaimProducts(
								policyClaimRsrc.getInsurancePolicyId().toString(), 
								true,
								null,
								ClaimsServiceEnums.CommodityCoverageCodes.QuantityGrain.getCode());

						if (productListRsrc != null) {
							policyProductRsrc = getProductById(productListRsrc, policyClaimRsrc.getPurchaseId());
						}
						
						if ( policyProductRsrc == null ) { 
							throw new ServiceException("No product found for " + claimNumber);
						}
						
						crpDto = cropCommodityDao.fetch(policyClaimRsrc.getCropCommodityId());
						
						if ( crpDto == null ) {
							throw new ServiceException("No commodity found for " + claimNumber);
						}
						
						linkedCrpDto = cropCommodityDao.getLinkedCommodityByPedigree(policyClaimRsrc.getCropCommodityId());
									
						verifiedYieldRsrc = getUnderwritingVerifiedYield(policyClaimRsrc, null, null, false, true, true, false);
						if (verifiedYieldRsrc == null ) {
							// If this fails, keep going. refreshManualClaimData() and calculateOutOfSyncFlags() will check if verifiedYieldRsrc is null.
							logger.error("No Verified Yield found for " + claimNumber);
						}
						
					} catch (CirrasPolicyServiceException e) {
						throw new ServiceException("Policy service threw an exception (CirrasPolicyServiceException)", e);
					} catch (CirrasUnderwritingServiceException e) {
						// If this fails, keep going. refreshManualClaimData() and calculateOutOfSyncFlags() will check if verifiedYieldRsrc is null.
						logger.error("getUnderwritingVerifiedYield: Error when getting verified yield from CUWS for Claim Number " + claimNumber + ": " + e);
					}
					
					// Set fields from linked calculation, if any.
					updateFromLinkedCalculation(result, policyClaimRsrc, productListRsrc, linkedCrpDto, false);
				}

				
				if (!ClaimsServiceEnums.CalculationStatusCodes.APPROVED.toString().equals(result.getCalculationStatusCode())
						&& !ClaimsServiceEnums.CalculationStatusCodes.ARCHIVED.toString().equals(result.getCalculationStatusCode())) {

					if (policyClaimRsrc.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAIN.toString())
							&& (policyClaimRsrc.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.CropUnseeded.getCode())
								|| policyClaimRsrc.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.GrainSpotLoss.getCode())
								)
						) {

						try { 
							productListRsrc = getCirrasClaimProducts(
									policyClaimRsrc.getInsurancePolicyId().toString(), 
									true,
									policyClaimRsrc.getPurchaseId().toString(),
									null);

							if ( productListRsrc != null && !productListRsrc.getCollection().isEmpty() ) {
								policyProductRsrc = productListRsrc.getCollection().get(0);
							}
						
						} catch ( CirrasPolicyServiceException e) {
							// If this fails, keep going. refreshManualClaimData() and calculateOutOfSyncFlags() will check if policyProductRsrc is null.
							logger.error("getCirrasClaimProducts: Error when getting product " + policyClaimRsrc.getPurchaseId() + " from CIRRAS for Claim Number " + policyClaimRsrc.getClaimNumber() + ": " + e);
						}
					}
					
					VerifiedYieldSummary verifiedSummary = getVerifiedYieldSummary(verifiedYieldRsrc, crpDto, linkedCrpDto);
					
					if (doRefreshManualClaimData != null && doRefreshManualClaimData.booleanValue()) {
						refreshManualClaimData(result, policyClaimRsrc, policyProductRsrc, verifiedSummary);
					}
	
					// Sets the out of sync flags for any fields in the calculation that are out of
					// sync with the Claim in CIRRAS.
					// If the check cannot be performed because policyClaimRsrc is null, then they
					// are left null to indicate that the sync status is unknown.
					outOfSync.calculateOutOfSyncFlags(result, policyClaimRsrc, policyProductRsrc, verifiedSummary);
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
			} else if (dto.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAIN.toString())
					&& dto.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.QuantityGrain.getCode())) {
				
				// Throw an error for Quantity Grain claims. We need to know if there is a linked claim.
				throw new ServiceException("no claim found for " + claimNumber);
			}
			
		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getClaimCalculation");
		return result;
	}
	
	private VerifiedYieldSummary getVerifiedYieldSummary(
			VerifiedYieldContractSimple verifiedYield, 
			CropCommodityDto crpDto,
			CropCommodityDto linkedCrpDto) {
		// From CUWS
		// CUWS stores yield data always using the non-pedigree crop id, whereas CCS stores Calculations for pedigree commodities using that crop id. So we have to account for this mis-match 
		// here when filtering for Verified Yield.
		VerifiedYieldSummary vys = null;
		
		if(verifiedYield != null && crpDto != null) {
			if ( verifiedYield.getVerifiedYieldSummaries() != null ) {
	
				Integer vysCropCommodityId = null;
				if ( crpDto.getIsPedigreeInd() ) {
					vysCropCommodityId = linkedCrpDto.getCropCommodityId();
				} else {
					vysCropCommodityId = crpDto.getCropCommodityId();
				}
			
				for ( VerifiedYieldSummary currVys : verifiedYield.getVerifiedYieldSummaries() ) {
					if ( currVys.getCropCommodityId().equals(vysCropCommodityId) && currVys.getIsPedigreeInd().equals(crpDto.getIsPedigreeInd()) ) {
						vys = currVys;
						break;
					}
				}
			}
		}
		
		return vys;
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

		// Get Grain Spot Loss
		if(dto.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAIN.toString()) 
				&& dto.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.GrainSpotLoss.getCode())) {
			ClaimCalculationGrainSpotLossDto grainSpotLossDto = claimCalculationGrainSpotLossDao.select(claimCalculationGuid);
			dto.setClaimCalculationGrainSpotLoss(grainSpotLossDto);
		}
		
		// Get Grain Quantity
		if(dto.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAIN.toString()) 
				&& dto.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.QuantityGrain.getCode())) {
			
			ClaimCalculationGrainQuantityDto grainQuantityDto = claimCalculationGrainQuantityDao.fetch(dto.getClaimCalculationGrainQuantityGuid());
			dto.setClaimCalculationGrainQuantity(grainQuantityDto);
			
			ClaimCalculationGrainQuantityDetailDto grainQuantityDetailDto = claimCalculationGrainQuantityDetailDao.select(claimCalculationGuid);
			dto.setClaimCalculationGrainQuantityDetail(grainQuantityDetailDto);
		}
	}

	private VerifiedYieldContractSimpleRsrc getUnderwritingVerifiedYield(
			InsuranceClaimRsrc policyClaimRsrc,
			CropCommodityDto crpDto,    // Warning: This must always be the non-pedigree commodity id. To filter on pedigree yield, set isPedigreeInd param to true.
			Boolean isPedigreeInd,      // Must be set if crpDto is set.
			Boolean loadVerifiedYieldContractCommodities, 
			Boolean loadVerifiedYieldAmendments,
			Boolean loadVerifiedYieldSummaries,
			Boolean loadVerifiedYieldGrainBasket
	) throws CirrasUnderwritingServiceException {

		ca.bc.gov.mal.cirras.underwriting.api.rest.v1.resource.EndpointsRsrc uwTopLevelEndpoints = cirrasUnderwritingService.getTopLevelEndpoints();
		return cirrasUnderwritingService.getVerifiedYieldContractSimple(
				uwTopLevelEndpoints, 
				policyClaimRsrc.getContractId().toString(), 
				policyClaimRsrc.getCropYear().toString(), 
				crpDto != null ? crpDto.getCropCommodityId().toString() : null,
				crpDto != null ? isPedigreeInd.toString() : null, 
				loadVerifiedYieldContractCommodities.toString(), 
				loadVerifiedYieldAmendments.toString(), 
				loadVerifiedYieldSummaries.toString(), 
				loadVerifiedYieldGrainBasket.toString());
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

	private ProductRsrc getProductById(ProductListRsrc productList, Integer productId) {
		
		ProductRsrc product = null;
		
		for ( ProductRsrc prd : productList.getCollection() ) {
			if ( prd.getProductId().equals(productId) ) {
				product = prd;
				break;
			}
		}
		
		return product;
	}

	private ProductRsrc getProductByCommodityAndCoverage(ProductListRsrc productList, Integer commodityId, String commodityCoverageCode) {
		
		ProductRsrc product = null;
		
		for ( ProductRsrc prd : productList.getCollection() ) {
			if ( prd.getCropCommodityId().equals(commodityId) && prd.getCommodityCoverageCode().equals(commodityCoverageCode) ) {
				product = prd;
				break;
			}
		}
		
		return product;
	}

	private void updateFromLinkedCalculation(ClaimCalculation claimCalculation, InsuranceClaimRsrc policyClaimRsrc, ProductListRsrc productListRsrc, CropCommodityDto linkedCrpDto, boolean doUpdateGrainQuantity) throws DaoException {

		ProductRsrc linkedProductRsrc = null;
		ClaimDto linkedClaimDto = null;
		
		// Linked calculation with the same version, if any.
		ClaimCalculationDto currLinkedClaimCalcDto = null;

		// Linked calculation with the highest version, if any.
		ClaimCalculationDto latestLinkedClaimCalcDto = null;
		
		if ( linkedCrpDto != null ) {
			linkedProductRsrc = getProductByCommodityAndCoverage(productListRsrc, linkedCrpDto.getCropCommodityId(), ClaimsServiceEnums.CommodityCoverageCodes.QuantityGrain.getCode());
			
			if ( linkedProductRsrc != null ) {
				linkedClaimDto = claimDao.selectByProductId(linkedProductRsrc.getProductId());
				
				if ( linkedClaimDto != null ) { 
					latestLinkedClaimCalcDto = claimCalculationDao.getLatestVersionOfCalculation(linkedClaimDto.getClaimNumber());
					
					currLinkedClaimCalcDto = claimCalculationDao.getByClaimNumberAndVersion(linkedClaimDto.getClaimNumber(), claimCalculation.getCalculationVersion());
					if ( currLinkedClaimCalcDto != null ) {
						getSubTableRecords(currLinkedClaimCalcDto.getClaimCalculationGuid(), currLinkedClaimCalcDto);
					}
				}
			}
		}
		
		claimCalculationFactory.updateCalculationFromLinkedCalculation(claimCalculation, linkedProductRsrc, linkedClaimDto, currLinkedClaimCalcDto, latestLinkedClaimCalcDto, doUpdateGrainQuantity);
	}
	
	// Updates fields in claimCalculation from insuranceClaim that are only updated
	// when the user requests a Refresh.
	private void refreshManualClaimData(
			ClaimCalculation claimCalculation, 
			InsuranceClaim insuranceClaim, 
			Product product,
			VerifiedYieldSummary verifiedSummary)
			throws ServiceException, DaoException {
		logger.debug("<refreshManualClaimData");

		if (claimCalculation == null || insuranceClaim == null) {
			throw new ServiceException("Unable to refresh Claim data. Claim or Claim Calculation was not loaded.");
		} else if (product == null && claimCalculation.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAIN.toString())
				&& claimCalculation.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.CropUnseeded.getCode())) {
			throw new ServiceException("Unable to refresh Claim data. Product was not loaded.");
		}
		
		claimCalculationFactory.updateCalculationFromClaim(claimCalculation, insuranceClaim, product, verifiedSummary);

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
			
			checkForLinkedCalculations(updateType, claimCalculation);

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

	private void checkForLinkedCalculations(String updateType, ClaimCalculation claimCalculation) throws DaoException {
		
		//If there is a linked product or linked calculation additional business rules apply when updating and submitting a calculation:
		//1. Updating is always possible if it's version 1
		//2. Updating version 2 and up is only possible if there is a linked calculation with the same version number.
		//   This is to make sure both calculations are being replaced.
		//3. Amount on line Z (claim amount pushed to CIRRAS) can't exceed the total coverage (line F) of the claim
		//4. On Submit: If there is a linked product but no linked calculation, submit is not possible
		//5. On Submit: If the linked calculation is already submitted, the sum of both submitted amounts on line Z 
		//   has to be equal to the calculated value on line Y
		
		// Linked calculation has a different version number or does not exist at all
		if(claimCalculation.getLinkedProductId() != null && claimCalculation.getLinkedClaimCalculationGuid() == null) {
			//There is a linked product but no linked calculation
			//Update only possible if the calculations are in version 1
			if(claimCalculation.getCalculationVersion() > 1) {
				throw new ServiceException("The calculation can't be updated because there are two quantity products for this commodity on this policy. However, no calculation with the same version exists.");
			}
			
			//On Submit: If there is a linked product but no linked calculation, submit is not possible
			if(updateType.equals(ClaimsServiceEnums.UpdateTypes.SUBMIT.toString())) {
				throw new ServiceException("The calculation can't be submitted because there are two quantity products for this commodity on this policy. However, no calculation with the same version exists.");
			}

		}
		
		//if(claimCalculation.getLinkedClaimCalculationGuid() != null && updateType.equals(ClaimsServiceEnums.UpdateTypes.SUBMIT.toString())) {
		if(claimCalculation.getLinkedClaimCalculationGuid() != null) {
			//Amount on line Z (claim amount pushed to CIRRAS) can't exceed the total coverage (line F) of the claim
			if(notNull(claimCalculation.getTotalClaimAmount(), 0.0) > notNull(claimCalculation.getClaimCalculationGrainQuantityDetail().getCoverageValue(), 0.0)) {
				throw new ServiceException("The calculation can't be saved because the Total Claim Amount is bigger than the Coverage Value.");
			}

			if(updateType.equals(ClaimsServiceEnums.UpdateTypes.SUBMIT.toString())) {
				//On Submit: If the linked calculation is already submitted, the sum of both submitted amounts on line Z 
				//has to be equal to the calculated value on line Y
				ClaimCalculationDto dtoLinkedCalculation = claimCalculationDao.fetch(claimCalculation.getLinkedClaimCalculationGuid());
				if(dtoLinkedCalculation != null) {
					Double totalClaimAmount = notNull(dtoLinkedCalculation.getTotalClaimAmount(), 0.0) + notNull(claimCalculation.getTotalClaimAmount(), 0.0);
					if(Double.compare(totalClaimAmount, claimCalculation.getClaimCalculationGrainQuantity().getQuantityLossClaim()) != 0) {
						throw new ServiceException("The calculation can't be submitted because the sum of the Total Claim Amount has to be equal to the calculated Quantity Loss Claim.");
					}
				}
			}
		}
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
		
		// Update Grain Spot Loss
		updateGrainSpotLoss(claimCalculation, userId);
		
		//Update Grain Quantity
		updateGrainQuantity(claimCalculation, userId);

		//Update Grain Quantity Detail
		updateGrainQuantityDetail(claimCalculation, userId);
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

	private void updateGrainSpotLoss(ClaimCalculation claimCalculation, String userId)
			throws DaoException, NotFoundDaoException {
		//
		// Update Grain Spot Loss Data
		//
		if (claimCalculation.getClaimCalculationGrainSpotLoss() != null) {
			ClaimCalculationGrainSpotLossDto dtoGrainSpotLoss = claimCalculationGrainSpotLossDao.fetch(claimCalculation.getClaimCalculationGrainSpotLoss().getClaimCalculationGrainSpotLossGuid());

			claimCalculationFactory.updateDto(dtoGrainSpotLoss, claimCalculation.getClaimCalculationGrainSpotLoss());

			claimCalculationGrainSpotLossDao.update(dtoGrainSpotLoss, userId);
		}
	}

	private void createUpdateGrainQuantity(ClaimCalculation claimCalculation, String userId)
			throws DaoException, NotFoundDaoException {
		//
		// Shared grain quantity record could already exist when creating a new calculation
		// Insert or Update Grain Quantity Data
		//
		if (claimCalculation.getClaimCalculationGrainQuantity() != null 
				&& claimCalculation.getClaimCalculationGrainQuantity().getClaimCalculationGrainQuantityGuid() != null) {
			updateGrainQuantity(claimCalculation, userId);
		} else {
			createGrainQuantity(claimCalculation, userId);
		}
	}
	
	private void updateGrainQuantity(ClaimCalculation claimCalculation, String userId)
			throws DaoException, NotFoundDaoException {
		//
		// Update Grain Quantity Data
		//
		if (claimCalculation.getClaimCalculationGrainQuantity() != null) {
			ClaimCalculationGrainQuantityDto dtoGrainQuantity = claimCalculationGrainQuantityDao.fetch(claimCalculation.getClaimCalculationGrainQuantity().getClaimCalculationGrainQuantityGuid());

			claimCalculationFactory.updateDto(dtoGrainQuantity, claimCalculation.getClaimCalculationGrainQuantity());

			claimCalculationGrainQuantityDao.update(dtoGrainQuantity, userId);
		}
	}
	
	private void updateGrainQuantityDetail(ClaimCalculation claimCalculation, String userId)
			throws DaoException, NotFoundDaoException {
		//
		// Update Grain Quantity Detail Data
		//
		if (claimCalculation.getClaimCalculationGrainQuantityDetail() != null) {
			ClaimCalculationGrainQuantityDetailDto dtoGrainQuantityDetail = claimCalculationGrainQuantityDetailDao.fetch(claimCalculation.getClaimCalculationGrainQuantityDetail().getClaimCalculationGrainQuantityDetailGuid());

			claimCalculationFactory.updateDto(dtoGrainQuantityDetail, claimCalculation.getClaimCalculationGrainQuantityDetail());

			claimCalculationGrainQuantityDetailDao.update(dtoGrainQuantityDetail, userId);
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
			throws ServiceException, CirrasPolicyServiceException, NotFoundException, ValidationFailureException, DaoException {

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
			result = claimCalculationFactory.getCalculationFromCalculation(claimCalculation, factoryContext, authentication);

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

			CropCommodityDto crpDto = null;
			CropCommodityDto linkedCrpDto = null;   // For example, if crpDto is BARLEY, then linkedCrpDto is BARLEY - PEDIGREED. Or vice-versa.
			ProductRsrc policyProductRsrc = null;
			VerifiedYieldContractSimpleRsrc verifiedYieldRsrc = null;

			if (policyClaimRsrc.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAIN.toString())
					&& (policyClaimRsrc.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.CropUnseeded.getCode())
							|| policyClaimRsrc.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.GrainSpotLoss.getCode())
							|| policyClaimRsrc.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.QuantityGrain.getCode())
						)
				) {
					
				ProductListRsrc productListRsrc = getCirrasClaimProducts(
						policyClaimRsrc.getInsurancePolicyId().toString(), 
						true,
						policyClaimRsrc.getPurchaseId().toString(),
						null);

				if ( productListRsrc != null && !productListRsrc.getCollection().isEmpty() ) {
					policyProductRsrc = productListRsrc.getCollection().get(0);
				}
			
				if ( policyProductRsrc == null ) {
					throw new NotFoundException("getCirrasClaimProducts: Error when getting product " + policyClaimRsrc.getPurchaseId() + " from CIRRAS for Claim Number " + policyClaimRsrc.getClaimNumber());
				}
				
				if(policyClaimRsrc.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.QuantityGrain.getCode())) {
					crpDto = cropCommodityDao.fetch(policyClaimRsrc.getCropCommodityId());
					
					if ( crpDto == null ) {
						throw new ServiceException("No commodity found for " + claimCalculation.getClaimNumber().toString());
					}

					linkedCrpDto = cropCommodityDao.getLinkedCommodityByPedigree(policyClaimRsrc.getCropCommodityId());
										
					try {
						verifiedYieldRsrc = getUnderwritingVerifiedYield(policyClaimRsrc, null, null, false, true, true, false);
						if (verifiedYieldRsrc == null ) {
							throw new ServiceException("No verified yield found for " + claimCalculation.getClaimNumber().toString());
						}
					} catch (CirrasUnderwritingServiceException e) {
						throw new ServiceException("Underwriting service threw an exception (CirrasUnderwritingServiceException)", e);
					}
				}
			}
			
			// Replacement is based on the current claim and policy data in CIRRAS
			// TODO
			result = claimCalculationFactory.getCalculationFromClaim(policyClaimRsrc, policyProductRsrc, crpDto, linkedCrpDto, verifiedYieldRsrc, null, null, null, null, factoryContext, authentication);

		}

		// Recalculate: Not necessary because calculation are done in createClaimCalculation as well
		//calculateVarietyInsurableValues(result);
		//calculateTotals(result);
		
		// Set fields from linked calculation, if any and load shared grain quantity record  
		if (claimCalculation.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAIN.toString()) && 
				claimCalculation.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.QuantityGrain.getCode())) {
			
			ClaimCalculationGrainQuantityDto quantityDto = null;
			
			//Load shared grain quantity data if the linked calculation has already been replaced.
			if(claimCalculation.getLatestLinkedClaimCalculationGuid() != null) {
				Integer newCalculationVersion = claimCalculation.getCalculationVersion() +1;
				if(newCalculationVersion.equals(claimCalculation.getLatestLinkedCalculationVersion())) {
					//Load new linked calculation
					ClaimCalculationDto linkedCalculation = claimCalculationDao.fetch(claimCalculation.getLatestLinkedClaimCalculationGuid());
					if(linkedCalculation != null) {
						//Load quantity record
						quantityDto = claimCalculationGrainQuantityDao.fetch(linkedCalculation.getClaimCalculationGrainQuantityGuid());
					}
					//Set linked claim calculation guid with latest linked claim calculation guid 
					result.setLinkedClaimCalculationGuid(claimCalculation.getLatestLinkedClaimCalculationGuid());
				}
			}
			
			if(quantityDto == null) {
				//No linked calculation or no linked calculation in the correct version: 
				//copy user entered data if calculation is replaced based on previous calculation.
				//The other fields are calculated in createClaimCalculation
				if(updateType.equals(ClaimsServiceEnums.UpdateTypes.REPLACE_COPY.toString())) {
					result.setClaimCalculationGrainQuantity(new ClaimCalculationGrainQuantity());
					result.setClaimCalculationGrainQuantityGuid(null);
					result.getClaimCalculationGrainQuantity().setReseedClaim(claimCalculation.getClaimCalculationGrainQuantity().getReseedClaim());
					result.getClaimCalculationGrainQuantity().setAdvancedClaim(claimCalculation.getClaimCalculationGrainQuantity().getAdvancedClaim());
				}
			} else {
				result.setClaimCalculationGrainQuantity(claimCalculationFactory.createClaimCalculationGrainQuantity(quantityDto));
				result.setClaimCalculationGrainQuantityGuid(quantityDto.getClaimCalculationGrainQuantityGuid());
			}
		}

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
	public void deleteClaimCalculation(
			String claimCalculationGuid,
			String optimisticLock,
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

	private void calculateTotals(ClaimCalculation claimCalculation) throws DaoException {
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
		} else if (claimCalculation.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAIN.toString())
				&& claimCalculation.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.GrainSpotLoss.getCode())) {
			calculateTotalsGrainSpotLoss(claimCalculation);
		} else if (claimCalculation.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAIN.toString())
				&& claimCalculation.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.QuantityGrain.getCode())) {
			calculateTotalsGrainQuantity(claimCalculation);
		}
	}

	private void calculateTotalsGrainUnseeded(ClaimCalculation claimCalculation) {
		ClaimCalculationGrainUnseeded grainUnseeded = claimCalculation.getClaimCalculationGrainUnseeded();
		
		//Adjusted Acres: Total Acres Insured - Less Adjustment Acres
		//0 if negative
		//Line C
		Double adjustedAcres = Math.max(0, (notNull(grainUnseeded.getInsuredAcres(), (double)0) - notNull(grainUnseeded.getLessAdjustmentAcres(), (double)0)));
		grainUnseeded.setAdjustedAcres(adjustedAcres);
		
		//Deductible Acres: Adjusted Acres * Deductible %
		//Line E
		Double deductibleAcres = adjustedAcres * (notNull((double)grainUnseeded.getDeductibleLevel(), (double)0)/(double)100);
		grainUnseeded.setDeductibleAcres(deductibleAcres);
		
		//Max Number of Eligible Acres: Adjusted Acres - Deductible Acres
		//Line F
		Double maxEligibleAcres = adjustedAcres - deductibleAcres;
		grainUnseeded.setMaxEligibleAcres(maxEligibleAcres);
		
		//Coverage Value: Max Number of Eligible Acres * Insured Value per Acre
		//Line H
		Double coverageValue = maxEligibleAcres * notNull(grainUnseeded.getInsurableValue(), (double)0);
		grainUnseeded.setCoverageValue((double)Math.round(coverageValue));

		
		//Eligible Unseeded Acres: Unseeded Acres - Less Assessment - Less Deductible Acres
		//0 if negative
		//Line L
		Double eligibleUnseededAcres =  Math.max(0, (notNull(grainUnseeded.getUnseededAcres(), (double)0) - notNull(grainUnseeded.getLessAssessmentAcres(), (double)0) - deductibleAcres));
		grainUnseeded.setEligibleUnseededAcres(eligibleUnseededAcres);
		
		//Plant Loss Claim: 
		//Line M
		// If Max Number of Eligible Acres = Eligible Unseeded Acres Then the Plant Loss Claim = Calculated Coverage Value
		// Else Eligible Unseeded Acres * Insured Value per Acre
		Double totalClaimAmount = (double)0;
		
		Double roundedEligibleUnseededAcres = (double)(Math.round(eligibleUnseededAcres * 100.0) / 100.0);
		Double roundedEligibleAcres = (double)(Math.round(maxEligibleAcres * 100.0) / 100.0);
		
		if(Double.compare(roundedEligibleUnseededAcres, roundedEligibleAcres) == 0){
			totalClaimAmount = grainUnseeded.getCoverageValue();
		} else {
			totalClaimAmount = eligibleUnseededAcres * notNull(grainUnseeded.getInsurableValue(), (double)0);
		}
		claimCalculation.setTotalClaimAmount(totalClaimAmount);
		
	}

	private void calculateTotalsGrainSpotLoss(ClaimCalculation claimCalculation) {
		
		ClaimCalculationGrainSpotLoss grainSpotLoss = claimCalculation.getClaimCalculationGrainSpotLoss();
		
		// Eligible Yield Reduction: Adjusted Acres x Percent Yield Reduction.
		Double eligibleYieldReduction = 0.0;
		if ( grainSpotLoss.getAdjustedAcres() != null && grainSpotLoss.getPercentYieldReduction() != null ) {
			eligibleYieldReduction = grainSpotLoss.getAdjustedAcres() * (grainSpotLoss.getPercentYieldReduction() / 100.0);
		}
		
		grainSpotLoss.setEligibleYieldReduction(eligibleYieldReduction);


		// Spot Loss Reduction Value: Coverage Amt Per Acre x Eligible Yield Reduction.
		Double spotLossReductionValue = 0.0;
		if ( grainSpotLoss.getCoverageAmtPerAcre() != null && grainSpotLoss.getEligibleYieldReduction() != null ) {
			spotLossReductionValue = grainSpotLoss.getCoverageAmtPerAcre() * grainSpotLoss.getEligibleYieldReduction();
		}
		
		grainSpotLoss.setSpotLossReductionValue(spotLossReductionValue);

		
		// Spot Loss Claim Value: Adjusted Acres x (Percent Yield Reduction - Deductible) x Coverage Amount Per Acre
		Double spotLossClaimValue = 0.0;
		if ( grainSpotLoss.getAdjustedAcres() != null && grainSpotLoss.getPercentYieldReduction() != null && grainSpotLoss.getDeductible() != null && grainSpotLoss.getCoverageAmtPerAcre() != null ) {
			spotLossClaimValue = grainSpotLoss.getAdjustedAcres() * ((grainSpotLoss.getPercentYieldReduction() - grainSpotLoss.getDeductible()) / 100.0) * grainSpotLoss.getCoverageAmtPerAcre();
		}

		claimCalculation.setTotalClaimAmount(spotLossClaimValue);
	}

	private void calculateTotalsGrainQuantity(ClaimCalculation claimCalculation) throws DaoException {
		
		ClaimCalculationGrainQuantityDetailDto linkedGrainQtyDetailDto = null;
		
		//Load linked calculation if necessary
		if(claimCalculation.getLinkedClaimCalculationGuid() != null) {
			linkedGrainQtyDetailDto = claimCalculationGrainQuantityDetailDao.select(claimCalculation.getLinkedClaimCalculationGuid());
		}
		
		//Calculation specific data
		ClaimCalculationGrainQuantityDetail grainQuantityDetail = claimCalculation.getClaimCalculationGrainQuantityDetail();
		//Shared data
		ClaimCalculationGrainQuantity grainQuantity = claimCalculation.getClaimCalculationGrainQuantity();
		
		//G - Total Pedigreed and Non-Pedigreed Seeds Coverage Value
		//Sum of pedigreed and non pedigreed coverage value
		//G = sum of F
		Double linkedCoverageValue = linkedGrainQtyDetailDto != null ? linkedGrainQtyDetailDto.getCoverageValue() : 0.0;
		Double totalCoverageValue = grainQuantityDetail.getCoverageValue() + linkedCoverageValue;
		grainQuantity.setTotalCoverageValue(totalCoverageValue);

		
		//K - Production Guarantee - Assessment(s) Value
		//Sum ((Production Guarantee (Tonnes) - Assessed Yield (Tonnes)) * Insurable Value (Tonnes))
		//K = Sum of pedigreed and non pedigreed of ( D - I ) x E
		Double linkedProductionGuaranteeAmount = 0.0;
		if( linkedGrainQtyDetailDto != null ) {
			linkedProductionGuaranteeAmount = calculateProductionGuarantee(
												linkedGrainQtyDetailDto.getProductionGuaranteeWeight(),
												linkedGrainQtyDetailDto.getAssessedYield(),
												linkedGrainQtyDetailDto.getInsurableValue());
		}
		Double productionGuaranteeAmount = calculateProductionGuarantee(
				grainQuantityDetail.getProductionGuaranteeWeight(),
				grainQuantityDetail.getAssessedYield(),
				grainQuantityDetail.getInsurableValue())
				+ linkedProductionGuaranteeAmount;
		grainQuantity.setProductionGuaranteeAmount(productionGuaranteeAmount);
		
		//O - 50% of Production Guarantee  (has to be calculated before L and P!!!)
		//Production Guarantee Weight * 50%
		Double fiftyPercentProductionGuarantee = grainQuantityDetail.getProductionGuaranteeWeight() * 0.5;
		grainQuantityDetail.setFiftyPercentProductionGuarantee(fiftyPercentProductionGuarantee);
		
		//P - Calculated Early Establishment Yield (has to be calculated before L!!!)
		//50% Production Guarantee (O) * (Damaged Acres (M) / Acres Seeded (N))
		Double calcEarlyEstYield = 0.0;
		if(grainQuantityDetail.getSeededAcres() != null && grainQuantityDetail.getSeededAcres() > 0) {
			calcEarlyEstYield = fiftyPercentProductionGuarantee * (notNull(grainQuantityDetail.getDamagedAcres(), 0.0) / grainQuantityDetail.getSeededAcres());
		}
		if(calcEarlyEstYield > 0) {
			calcEarlyEstYield = (double) Math.round(calcEarlyEstYield * 1000d) / 1000d;
		}
		grainQuantityDetail.setCalcEarlyEstYield(calcEarlyEstYield);
		
		//L - Less Early Establishment Deemed Yield Value
		// (Inspected Early Establishment Yield (Q) but if empty Calculated Early Establishment Yield (P)) x Insurable Value (Tonnes) (E).  
		//( Q or P ) x E
		Double earlyEstablishment = grainQuantityDetail.getInspEarlyEstYield() == null ? calcEarlyEstYield : grainQuantityDetail.getInspEarlyEstYield();
		Double earlyEstDeemedYieldValue = earlyEstablishment * grainQuantityDetail.getInsurableValue();
		grainQuantityDetail.setEarlyEstDeemedYieldValue(earlyEstDeemedYieldValue);
		
		//R - Yield Value
		//Total Yield Harvested and Appraised (H) * Insurable Value per Tonnes (E)
		// H * E
		Double yieldValue = notNull(grainQuantityDetail.getTotalYieldToCount(), 0.0) * grainQuantityDetail.getInsurableValue();
		grainQuantityDetail.setYieldValue(yieldValue);
		
		//S - Yield Value + Early Establishment Deemed Yields 
		//(R + L)
		Double yieldValueWithEarlyEstDeemedYield = notNull(yieldValue, 0.0) + notNull(earlyEstDeemedYieldValue, 0.0);
		grainQuantityDetail.setYieldValueWithEarlyEstDeemedYield(yieldValueWithEarlyEstDeemedYield);
		
		//T - Total Pedigreed and Non-Pedigreed Seeds Yield Loss Value 
		// Production Guarantee - Assessment(s) Value (K) - SUM(Yield Value + Early Establishment Deemed Yield (S))
		//(K - SUM of S)
		Double linkedYieldValueWithEarlyEstDeemedYield = linkedGrainQtyDetailDto != null ? linkedGrainQtyDetailDto.getYieldValueWithEarlyEstDeemedYield() : 0.0;
		Double totalYieldLossValue = Math.max(0, productionGuaranteeAmount - notNull(yieldValueWithEarlyEstDeemedYield, 0.0) - linkedYieldValueWithEarlyEstDeemedYield);
		grainQuantity.setTotalYieldLossValue(totalYieldLossValue);
		
		// V - Maximum Claim Payable
		// Total Pedigreed and Non-Pedigreed Seeds Coverage Value (G) - Reseed Claim (U)
		// (G - U)
		Double maxClaimPayable = Math.max(0, notNull(totalCoverageValue, 0.0) - notNull(grainQuantity.getReseedClaim(), 0.0));
		grainQuantity.setMaxClaimPayable(maxClaimPayable);
		
		// Y - Quantity Loss Claim
		// Lesser of Maximum Claim Payable (V) or Total Quantity Loss (W) - Less Advanced Claim(s) ( X )
		Double quantityLossClaim = Math.max(0, Math.min(maxClaimPayable, totalYieldLossValue) - notNull(grainQuantity.getAdvancedClaim(), 0.0));
		if(quantityLossClaim > 0) {
			//Round to two decimals
			quantityLossClaim = (double) Math.round(quantityLossClaim * 100d) / 100d;
		}
		grainQuantity.setQuantityLossClaim(quantityLossClaim);

	}
	
	private Double calculateProductionGuarantee(Double productionGuaranteeWeight, Double assessedYield, Double insurableValue) {
		//( D - I ) x E
		Double calcProdGuaranteeWeight = notNull(productionGuaranteeWeight, 0.0) - notNull(assessedYield, 0.0);
		return Math.max(0, calcProdGuaranteeWeight) * notNull(insurableValue, 0.0);
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
