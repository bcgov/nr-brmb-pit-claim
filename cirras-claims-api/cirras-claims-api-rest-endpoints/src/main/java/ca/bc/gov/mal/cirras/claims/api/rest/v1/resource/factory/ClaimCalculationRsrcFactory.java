package ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.factory;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.UriBuilder;

import ca.bc.gov.nrs.common.wfone.rest.resource.RelLink;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dto.PagedDtos;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.ClaimCalculationEndpoint;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.endpoints.ClaimCalculationListEndpoint;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.ClaimCalculationListRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.ClaimCalculationRsrc;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculation;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationBerries;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationGrainQuantity;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationGrainQuantityDetail;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationGrainSpotLoss;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationGrainUnseeded;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationGrapes;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationList;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationPlantAcres;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationPlantUnits;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationVariety;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationBerriesDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationPlantAcresDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationPlantUnitsDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationGrainQuantityDetailDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationGrainQuantityDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationGrainSpotLossDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationGrainUnseededDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationGrapesDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationVarietyDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.CropCommodityDto;
import ca.bc.gov.mal.cirras.claims.service.api.v1.model.factory.ClaimCalculationFactory;
import ca.bc.gov.mal.cirras.claims.service.api.v1.util.ClaimsServiceEnums;
import ca.bc.gov.mal.cirras.policies.api.rest.v1.resource.ProductRsrc;
import ca.bc.gov.mal.cirras.policies.model.v1.Product;
import ca.bc.gov.mal.cirras.policies.model.v1.Variety;
import ca.bc.gov.mal.cirras.underwriting.model.v1.UnderwritingComment;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldAmendment;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldContractSimple;
import ca.bc.gov.mal.cirras.underwriting.model.v1.VerifiedYieldSummary;

public class ClaimCalculationRsrcFactory extends BaseResourceFactory implements ClaimCalculationFactory {

	@Override
	public ClaimCalculation getClaimCalculation(ClaimCalculationDto dto, FactoryContext context,
			WebAdeAuthentication authentication) throws FactoryException {

		ClaimCalculationRsrc resource = new ClaimCalculationRsrc();

		populateResource(resource, dto);

		if (!dto.getVarieties().isEmpty()) {
			List<ClaimCalculationVariety> modelVarieties = new ArrayList<ClaimCalculationVariety>();

			for (ClaimCalculationVarietyDto vdto : dto.getVarieties()) {
				ClaimCalculationVariety modelVariety = createClaimCalculationVariety(vdto);
				modelVarieties.add(modelVariety);
			}

			resource.setVarieties(modelVarieties);
		}

		if (dto.getClaimCalculationGrapes() != null) {
			resource.setClaimCalculationGrapes(createClaimCalculationGrapes(dto.getClaimCalculationGrapes()));
		}

		// Add quantity berries specific values
		if (dto.getClaimCalculationBerries() != null) {
			resource.setClaimCalculationBerries(createClaimCalculationBerries(dto.getClaimCalculationBerries()));
		}

		// Add plant loss berries by units specific values
		if (dto.getClaimCalculationPlantUnits() != null) {
			resource.setClaimCalculationPlantUnits(
					createClaimCalculationPlantUnits(dto.getClaimCalculationPlantUnits()));
		}

		// Add plant loss berries by acres specific values
		if (dto.getClaimCalculationPlantAcres() != null) {
			resource.setClaimCalculationPlantAcres(
					createClaimCalculationPlantAcres(dto.getClaimCalculationPlantAcres()));
		}
		
		//Add grain unseeded
		if(dto.getClaimCalculationGrainUnseeded() != null) {
			resource.setClaimCalculationGrainUnseeded(createClaimCalculationGrainUnseeded(dto.getClaimCalculationGrainUnseeded()));
		}

		//Add grain unseeded
		if(dto.getClaimCalculationGrainSpotLoss() != null) {
			resource.setClaimCalculationGrainSpotLoss(createClaimCalculationGrainSpotLoss(dto.getClaimCalculationGrainSpotLoss()));
		}

		//Add grain quantity
		if(dto.getClaimCalculationGrainQuantity() != null) {
			resource.setClaimCalculationGrainQuantity(createClaimCalculationGrainQuantity(dto.getClaimCalculationGrainQuantity()));
		}

		//Add grain quantity detail
		if(dto.getClaimCalculationGrainQuantityDetail() != null) {
			resource.setClaimCalculationGrainQuantityDetail(createClaimCalculationGrainQuantityDetail(dto.getClaimCalculationGrainQuantityDetail()));
		}
		
		String eTag = getEtag(resource);
		resource.setETag(eTag);

		URI baseUri = getBaseURI(context);
		setSelfLink(dto.getClaimCalculationGuid(), resource, baseUri);
		setLinks(dto.getClaimCalculationGuid(), resource, baseUri, authentication);

		return resource;
	}

	@Override
	public ClaimCalculation getCalculationFromClaim(ca.bc.gov.mal.cirras.policies.model.v1.InsuranceClaim claim,
			ProductRsrc productRsrc, 
			CropCommodityDto crpDto,
			CropCommodityDto linkedCrpDto,
			VerifiedYieldContractSimple verifiedYield,
			FactoryContext context, 
			WebAdeAuthentication authentication) throws FactoryException {

		ClaimCalculationRsrc resource = new ClaimCalculationRsrc();

		// Add policy data to the insurance claim resource
		populateResource(resource, claim, crpDto);

		if (!claim.getVarieties().isEmpty()) {
			List<ClaimCalculationVariety> modelVarieties = new ArrayList<ClaimCalculationVariety>();

			for (ca.bc.gov.mal.cirras.policies.model.v1.Variety policiesVariety : claim.getVarieties()) {
				ClaimCalculationVariety modelVariety = createClaimCalculationVarietyFromClaim(policiesVariety);
				modelVarieties.add(modelVariety);
			}

			resource.setVarieties(modelVarieties);
		}

		// Add a berries object if the insurance plan is grapes
		if (claim.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAPES.toString())
				&& claim.getCommodityCoverageCode()
						.equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.Quantity.getCode())) {
			resource.setClaimCalculationGrapes(createClaimCalculationGrapesFromClaim(claim));
		}

		// Add a berries object if the insurance plan is berries
		if (claim.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.BERRIES.toString())
				&& claim.getCommodityCoverageCode()
						.equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.Quantity.getCode())) {
			resource.setClaimCalculationBerries(createClaimCalculationBerriesFromClaim(claim));
		}

		if (claim.getCommodityCoverageCode()
				.equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.Plant.getCode())) {
			// Depending on measurement type
			if (claim.getInsuredByMeasurementType()
					.equalsIgnoreCase(ClaimsServiceEnums.InsuredByMeasurementType.UNITS.toString())) {
				resource.setClaimCalculationPlantUnits(createClaimCalculationPlantUnitsFromClaim(claim));
			} else if (claim.getInsuredByMeasurementType()
					.equalsIgnoreCase(ClaimsServiceEnums.InsuredByMeasurementType.ACRES.toString())) {
				resource.setClaimCalculationPlantAcres(createClaimCalculationPlantAcresFromClaim(claim));
			}
		}
		
		if (claim.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAIN.toString())) {
			// Add a grain unseeded object if the insurance plan is grain and coverage is unseeded
			if (claim.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.CropUnseeded.getCode())) {
				resource.setClaimCalculationGrainUnseeded(createClaimCalculationGrainUnseededFromClaim(productRsrc));
			}
			
			// Add a grain spot loss object if the insurance plan is grain and coverage is grain spot loss
			else if (claim.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.GrainSpotLoss.getCode())) {
				resource.setClaimCalculationGrainSpotLoss(createClaimCalculationGrainSpotLossFromClaim(productRsrc));
			}

			// Add a grain quantity object if the insurance plan is grain and coverage is grain quantity
			else if (claim.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.QuantityGrain.getCode())) {

				// If there is already a calculation that is linked to this one, then this default ClaimCalculationGrainQuantity object will 
				// be overwritten by the one from the linked calculation.
				resource.setClaimCalculationGrainQuantity(createClaimCalculationGrainQuantityFromClaim());

				resource.setClaimCalculationGrainQuantityDetail(createClaimCalculationGrainQuantityDetailFromClaim(productRsrc, crpDto, linkedCrpDto, verifiedYield));
				populateCommentForGrainQuantity(resource, crpDto, linkedCrpDto, verifiedYield);
			}
		
		}

		String eTag = getEtag(resource);
		resource.setETag(eTag);

		URI baseUri = getBaseURI(context);

		// if (authentication.hasAuthority(Scopes.CREATE_CALCULATION)) {
		String result = UriBuilder.fromUri(baseUri).path(ClaimCalculationListEndpoint.class).build().toString();
		resource.getLinks().add(new RelLink(ResourceTypes.CREATE_CLAIM_CALCULATION, result, "POST"));
		// }

		return resource;
	}

	// Updates only fields for a linked claim calculation.
	@Override
	public void updateCalculationFromLinkedCalculation(ClaimCalculation claimCalculation, Product linkedProduct, ClaimDto linkedClaimDto, ClaimCalculationDto linkedCalcDto, boolean doUpdateGrainQuantity) {

		if ( linkedProduct != null ) {
			claimCalculation.setLinkedProductId(linkedProduct.getProductId());
		}
		
		if ( linkedClaimDto != null ) { 
			claimCalculation.setLinkedClaimNumber(linkedClaimDto.getClaimNumber());
		}
		
		if ( linkedCalcDto != null ) {
			claimCalculation.setLinkedClaimCalculationGuid(linkedCalcDto.getClaimCalculationGuid());
			
			if ( doUpdateGrainQuantity && linkedCalcDto.getClaimCalculationGrainQuantityGuid() != null ) {
				claimCalculation.setClaimCalculationGrainQuantityGuid(linkedCalcDto.getClaimCalculationGrainQuantityGuid());
				
				if ( linkedCalcDto.getClaimCalculationGrainQuantity() != null ) {
					claimCalculation.setClaimCalculationGrainQuantity(createClaimCalculationGrainQuantity(linkedCalcDto.getClaimCalculationGrainQuantity()));
				}
			}
		}		
	}
	
	// Updates only the manually refreshed fields from the Claim.
	@Override
	public void updateCalculationFromClaim(ClaimCalculation claimCalculation,
			ca.bc.gov.mal.cirras.policies.model.v1.InsuranceClaim claim,
			Product product
	) {

		// Update ClaimCalculation fields
		claimCalculation.setGrowerNumber(claim.getGrowerNumber());
		claimCalculation.setGrowerName(claim.getGrowerName());
		claimCalculation.setGrowerAddressLine1(claim.getGrowerAddressLine1());
		claimCalculation.setGrowerAddressLine2(claim.getGrowerAddressLine2());
		claimCalculation.setGrowerPostalCode(claim.getGrowerPostalCode());
		claimCalculation.setGrowerCity(claim.getGrowerCity());
		claimCalculation.setGrowerProvince(claim.getGrowerProvince());

		// Update Claim Calculation Varieties
		updateVarietesFromClaim(claimCalculation, claim);

		// Add a grapes object if the insurance plan is berries
		if (claim.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAPES.toString())
				&& claim.getCommodityCoverageCode()
						.equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.Quantity.getCode())) {
			updateClaimCalculationGrapesFromClaim(claimCalculation, claim);
		}

		// Add a berries object if the insurance plan is berries
		if (claim.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.BERRIES.toString())
				&& claim.getCommodityCoverageCode()
						.equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.Quantity.getCode())) {
			updateClaimCalculationBerriesFromClaim(claimCalculation, claim);
		}

		if (claim.getCommodityCoverageCode()
				.equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.Plant.getCode())) {
			// Depending on measurement type
			if (claim.getInsuredByMeasurementType()
					.equalsIgnoreCase(ClaimsServiceEnums.InsuredByMeasurementType.UNITS.toString())) {
				updateClaimCalculationPlantUnitsFromClaim(claimCalculation, claim);
			} else if (claim.getInsuredByMeasurementType()
					.equalsIgnoreCase(ClaimsServiceEnums.InsuredByMeasurementType.ACRES.toString())) {
				updateClaimCalculationPlantAcresFromClaim(claimCalculation, claim);
			}
		}
		
		// Add a grain unseeded object if the insurance plan is grain and coverage is unseeded
		if (claim.getInsurancePlanName().equalsIgnoreCase(ClaimsServiceEnums.InsurancePlans.GRAIN.toString())){
			if (claim.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.CropUnseeded.getCode())) {
				updateClaimCalculationGrainUnseededFromClaim(claimCalculation, product);
			} else if (claim.getCommodityCoverageCode().equalsIgnoreCase(ClaimsServiceEnums.CommodityCoverageCodes.GrainSpotLoss.getCode())) {
				updateClaimCalculationGrainSpotLossFromClaim(claimCalculation, product);
			}
		}

	}

	private void updateVarietesFromClaim(ClaimCalculation claimCalculation,
			ca.bc.gov.mal.cirras.policies.model.v1.InsuranceClaim claim) {
		Set<Integer> claimCalcVarietyIds = new HashSet<Integer>();
		Map<Integer, Variety> insClaimVarietyMap = new HashMap<Integer, Variety>();

		if (claimCalculation.getVarieties() != null && claimCalculation.getVarieties().size() > 0) {
			for (ClaimCalculationVariety variety : claimCalculation.getVarieties()) {
				claimCalcVarietyIds.add(variety.getCropVarietyId());
			}
		}

		if (claim.getVarieties() != null && claim.getVarieties().size() > 0) {
			for (Variety variety : claim.getVarieties()) {
				insClaimVarietyMap.put(variety.getCropVarietyId(), variety);
			}
		}

		// Update or remove variety.
		if (claimCalculation.getVarieties() != null && claimCalculation.getVarieties().size() > 0) {

			Iterator<ClaimCalculationVariety> it = claimCalculation.getVarieties().iterator();
			while (it.hasNext()) {
				ClaimCalculationVariety claimCalcVariety = it.next();

				Variety insClaimVariety = insClaimVarietyMap.get(claimCalcVariety.getCropVarietyId());

				if (insClaimVariety != null) {
					//Set average price (might be null)
					claimCalcVariety.setAveragePrice(insClaimVariety.getAveragePrice());
				} else {
					it.remove();
				}
			}
		}

		// Add new varieties.
		if (claim.getVarieties() != null && claim.getVarieties().size() > 0) {
			for (Variety insClaimVariety : claim.getVarieties()) {
				if (!claimCalcVarietyIds.contains(insClaimVariety.getCropVarietyId())) {
					// Add new variety.
					if (claimCalculation.getVarieties() == null) {
						List<ClaimCalculationVariety> claimCalcVarieties = new ArrayList<ClaimCalculationVariety>();
						claimCalculation.setVarieties(claimCalcVarieties);
					}

					ClaimCalculationVariety claimCalcVariety = createClaimCalculationVarietyFromClaim(insClaimVariety);
					claimCalculation.getVarieties().add(claimCalcVariety);
				}
			}
		}
	}

	private void updateClaimCalculationBerriesFromClaim(ClaimCalculation claimCalculation,
			ca.bc.gov.mal.cirras.policies.model.v1.InsuranceClaim claim) {

		claimCalculation.getClaimCalculationBerries().setTotalProbableYield(claim.getTotalProbableYield());
		claimCalculation.getClaimCalculationBerries().setDeductibleLevel(claim.getDeductibleLevel());
		claimCalculation.getClaimCalculationBerries().setDeclaredAcres(claim.getDeclaredAcres());
		claimCalculation.getClaimCalculationBerries().setProductionGuarantee(claim.getProductionGuarantee());
		claimCalculation.getClaimCalculationBerries().setInsurableValueSelected(claim.getInsurableValueSelected());
		claimCalculation.getClaimCalculationBerries().setInsurableValueHundredPercent(claim.getInsurableValueHundredPercent());
		claimCalculation.getClaimCalculationBerries().setMaxCoverageAmount(claim.getCoverageAmount());

	}

	private void updateClaimCalculationPlantUnitsFromClaim(ClaimCalculation claimCalculation,
			ca.bc.gov.mal.cirras.policies.model.v1.InsuranceClaim claim) {

		claimCalculation.getClaimCalculationPlantUnits().setInsuredUnits(claim.getPlantDeclaredUnits());
		claimCalculation.getClaimCalculationPlantUnits().setDeductibleLevel(claim.getDeductibleLevel());
		claimCalculation.getClaimCalculationPlantUnits().setInsurableValue(claim.getPlantInsurableValue());

	}

	private void updateClaimCalculationPlantAcresFromClaim(ClaimCalculation claimCalculation,
			ca.bc.gov.mal.cirras.policies.model.v1.InsuranceClaim claim) {

		claimCalculation.getClaimCalculationPlantAcres().setDeclaredAcres(claim.getPlantDeclaredAcres());
		claimCalculation.getClaimCalculationPlantAcres().setDeductibleLevel(claim.getDeductibleLevel());
		claimCalculation.getClaimCalculationPlantAcres().setInsurableValue(claim.getPlantInsurableValue());
	}

	private void updateClaimCalculationGrapesFromClaim(ClaimCalculation claimCalculation,
			ca.bc.gov.mal.cirras.policies.model.v1.InsuranceClaim claim) {

		claimCalculation.getClaimCalculationGrapes().setInsurableValueSelected(claim.getInsurableValueSelected());
		claimCalculation.getClaimCalculationGrapes()
				.setInsurableValueHundredPercent(claim.getInsurableValueHundredPercent());
		claimCalculation.getClaimCalculationGrapes().setCoverageAmount(claim.getCoverageAmount());

	}

	private void updateClaimCalculationGrainUnseededFromClaim(ClaimCalculation claimCalculation, Product product) {

		claimCalculation.getClaimCalculationGrainUnseeded().setInsuredAcres(product.getAcres());
		claimCalculation.getClaimCalculationGrainUnseeded().setDeductibleLevel(product.getDeductibleLevel());
		claimCalculation.getClaimCalculationGrainUnseeded().setInsurableValue(product.getUnseededSelectedInsurableValue());

	}
	
	private void updateClaimCalculationGrainSpotLossFromClaim(ClaimCalculation claimCalculation, Product product) {

		claimCalculation.getClaimCalculationGrainSpotLoss().setInsuredAcres(product.getAcres());
		claimCalculation.getClaimCalculationGrainSpotLoss().setCoverageAmtPerAcre(product.getSpotLossCoverageAmountPerAcre());
		claimCalculation.getClaimCalculationGrainSpotLoss().setCoverageValue(product.getCoverageDollars());
		claimCalculation.getClaimCalculationGrainSpotLoss().setDeductible(grainSpotLossDeductible);

	}
	
	private void populateResource(ClaimCalculationRsrc resource,
			ca.bc.gov.mal.cirras.policies.model.v1.InsuranceClaim claim,
			CropCommodityDto crpDto) {
		resource.setClaimNumber(claim.getClaimNumber());
		resource.setContractId(claim.getContractId());
		resource.setPolicyNumber(claim.getPolicyNumber());
		resource.setGrowerNumber(claim.getGrowerNumber());
		resource.setGrowerName(claim.getGrowerName());
		resource.setInsurancePlanId(claim.getInsurancePlanId());
		resource.setInsurancePlanName(claim.getInsurancePlanName());
		resource.setCoverageName(claim.getCoverageName());
		resource.setCommodityCoverageCode(claim.getCommodityCoverageCode());
		resource.setCommodityName(claim.getCommodityName());
		resource.setIsPedigreeInd(crpDto != null ? crpDto.getIsPedigreeInd() : false);
		resource.setCropCommodityId(claim.getCropCommodityId());
		resource.setClaimStatusCode(claim.getClaimStatusCode());
		resource.setClaimType(claim.getClaimType());
		resource.setGrowerAddressLine1(claim.getGrowerAddressLine1());
		resource.setGrowerAddressLine2(claim.getGrowerAddressLine2());
		resource.setGrowerPostalCode(claim.getGrowerPostalCode());
		resource.setGrowerCity(claim.getGrowerCity());
		resource.setGrowerProvince(claim.getGrowerProvince());
		resource.setCropYear(claim.getCropYear());
		resource.setInsuredByMeasurementType(claim.getInsuredByMeasurementType());
		resource.setPrimaryPerilCode(claim.getPrimaryPerilCode());
		resource.setSecondaryPerilCode(claim.getSecondaryPerilCode());
		resource.setSubmittedByUserid(claim.getSubmittedByUserid());
		resource.setSubmittedByName(claim.getSubmittedByName());
		resource.setSubmittedByDate(claim.getSubmittedByDate());
		resource.setRecommendedByUserid(claim.getRecommendedByUserid());
		resource.setRecommendedByName(claim.getRecommendedByName());
		resource.setRecommendedByDate(claim.getRecommendedByDate());
		resource.setApprovedByUserid(claim.getApprovedByUserid());
		resource.setApprovedByName(claim.getApprovedByName());
		resource.setApprovedByDate(claim.getApprovedByDate());
		resource.setCalculateIivInd(claim.getCalculateIivInd());
		resource.setHasChequeReqInd(claim.getHasChequeReqInd());
		
		// Default to null, but may be updated later from a linked calculation.
		resource.setClaimCalculationGrainQuantityGuid(null);
		resource.setLinkedProductId(null);
		resource.setLinkedClaimNumber(null);
		resource.setLinkedClaimCalculationGuid(null);

	}
	
	private void populateCommentForGrainQuantity(ClaimCalculationRsrc resource, CropCommodityDto crpDto, CropCommodityDto linkedCrpDto, VerifiedYieldContractSimple verifiedYield) {

		// CUWS stores yield data always using the non-pedigree crop id, whereas CCS stores Calculations for pedigree commodities using that crop id. So we have to account for this mis-match 
		// here when filtering for Verified Yield.
		Integer vysCropCommodityId = null;
		if ( crpDto.getIsPedigreeInd() ) {
			vysCropCommodityId = linkedCrpDto.getCropCommodityId();
		} else {
			vysCropCommodityId = crpDto.getCropCommodityId();
		}
		
		StringBuilder commentStr = new StringBuilder();
	
		if ( verifiedYield.getVerifiedYieldSummaries() != null ) {
			for ( VerifiedYieldSummary vys : verifiedYield.getVerifiedYieldSummaries() ) {
				if ( vys.getCropCommodityId().equals(vysCropCommodityId) ) {      // Extract comments for non-pedigree and pedigree.
					List<UnderwritingComment> uwComments = vys.getUwComments();
					
					if ( uwComments != null && !uwComments.isEmpty() ) {
						StringBuilder vysCommentStr = new StringBuilder();
	
						for ( UnderwritingComment uwCmt : uwComments ) {
							if ( vysCommentStr.length() > 0 ) {
								vysCommentStr.append("\n\n");
							}
							vysCommentStr.append(uwCmt.getUnderwritingComment());
						}
						
						if ( vysCommentStr.length() > 0 ) {
							
							if ( commentStr.length() > 0 ) {
								commentStr.append("\n\n");
							}
							
							commentStr.append("Verified Yield Summary - ")
					          .append(vys.getCropCommodityName())
					          .append(vys.getIsPedigreeInd() ? " - Pedigreed" : "")
					          .append(":\n")
					          .append(vysCommentStr.toString());						
						}
					}
				}
			}
		}

		if ( verifiedYield.getVerifiedYieldAmendments() != null ) {
			for ( VerifiedYieldAmendment vya : verifiedYield.getVerifiedYieldAmendments() ) {
				if ( vya.getCropCommodityId().equals(vysCropCommodityId) && vya.getRationale() != null ) {

					if ( commentStr.length() > 0 ) {
						commentStr.append("\n\n");
					}
					
					commentStr.append("Verified Yield Amendment Rationale - ")
			          .append(vya.getCropCommodityName())
			          .append(vya.getIsPedigreeInd() ? " - Pedigreed" : "")
			          .append(":\n")
			          .append(vya.getRationale());
				}
			}
		}

		if ( commentStr.length() > 0 ) {
			resource.setCalculationComment(commentStr.toString());
		}
		
	}

	private ClaimCalculationVariety createClaimCalculationVarietyFromClaim(
			ca.bc.gov.mal.cirras.policies.model.v1.Variety policiesVariety) {
		ClaimCalculationVariety model = new ClaimCalculationVariety();

		model.setCropVarietyId(policiesVariety.getCropVarietyId());
		model.setVarietyName(policiesVariety.getVarietyName());
		model.setAveragePrice(policiesVariety.getAveragePrice());
		model.setAveragePriceOverride(null);
		
		return model;
	}

	private ClaimCalculationBerries createClaimCalculationBerriesFromClaim(
			ca.bc.gov.mal.cirras.policies.model.v1.InsuranceClaim claim) {
		ClaimCalculationBerries model = new ClaimCalculationBerries();

		model.setTotalProbableYield(claim.getTotalProbableYield());
		model.setDeductibleLevel(claim.getDeductibleLevel());
		model.setDeclaredAcres(claim.getDeclaredAcres());
		model.setProductionGuarantee(claim.getProductionGuarantee());
		model.setInsurableValueSelected(claim.getInsurableValueSelected());
		model.setInsurableValueHundredPercent(claim.getInsurableValueHundredPercent());
		model.setMaxCoverageAmount(claim.getCoverageAmount());

		return model;
	}

	private ClaimCalculationPlantUnits createClaimCalculationPlantUnitsFromClaim(
			ca.bc.gov.mal.cirras.policies.model.v1.InsuranceClaim claim) {
		ClaimCalculationPlantUnits model = new ClaimCalculationPlantUnits();

		model.setInsuredUnits(claim.getPlantDeclaredUnits());
		model.setDeductibleLevel(claim.getDeductibleLevel());
		model.setInsurableValue(claim.getPlantInsurableValue());

		return model;
	}

	private ClaimCalculationPlantAcres createClaimCalculationPlantAcresFromClaim(
			ca.bc.gov.mal.cirras.policies.model.v1.InsuranceClaim claim) {
		ClaimCalculationPlantAcres model = new ClaimCalculationPlantAcres();

		model.setDeclaredAcres(claim.getPlantDeclaredAcres());
		model.setDeductibleLevel(claim.getDeductibleLevel());
		model.setInsurableValue(claim.getPlantInsurableValue());

		return model;
	}

	private ClaimCalculationGrapes createClaimCalculationGrapesFromClaim(
			ca.bc.gov.mal.cirras.policies.model.v1.InsuranceClaim claim) {
		ClaimCalculationGrapes model = new ClaimCalculationGrapes();

		model.setInsurableValueSelected(claim.getInsurableValueSelected());
		model.setInsurableValueHundredPercent(claim.getInsurableValueHundredPercent());
		model.setCoverageAmount(claim.getCoverageAmount());

		return model;
	}

	private ClaimCalculationGrainUnseeded createClaimCalculationGrainUnseededFromClaim(
			ProductRsrc productRsrc) {
		ClaimCalculationGrainUnseeded model = new ClaimCalculationGrainUnseeded();

		model.setInsuredAcres(productRsrc.getAcres());
		model.setDeductibleLevel(productRsrc.getDeductibleLevel());
		model.setInsurableValue(productRsrc.getUnseededSelectedInsurableValue());

		return model;
	}
	
	private final Integer grainSpotLossDeductible = 5;

	private ClaimCalculationGrainSpotLoss createClaimCalculationGrainSpotLossFromClaim(
			ProductRsrc productRsrc) {
		ClaimCalculationGrainSpotLoss model = new ClaimCalculationGrainSpotLoss();

		model.setInsuredAcres(productRsrc.getAcres());
		model.setCoverageAmtPerAcre(productRsrc.getSpotLossCoverageAmountPerAcre());
		model.setCoverageValue(productRsrc.getCoverageDollars());
		model.setDeductible(grainSpotLossDeductible);

		return model;
	}

	private ClaimCalculationGrainQuantity createClaimCalculationGrainQuantityFromClaim() {
		ClaimCalculationGrainQuantity model = new ClaimCalculationGrainQuantity();

		model.setAdvancedClaim(null);
		model.setMaxClaimPayable(null);
		model.setProductionGuaranteeAmount(null);
		model.setQuantityLossClaim(null);
		model.setReseedClaim(null);
		model.setTotalCoverageValue(null);
		model.setTotalYieldLossValue(null);
		
		return model;
	}

	private ClaimCalculationGrainQuantityDetail createClaimCalculationGrainQuantityDetailFromClaim(ProductRsrc productRsrc, CropCommodityDto crpDto, CropCommodityDto linkedCrpDto, VerifiedYieldContractSimple verifiedYield) {

		ClaimCalculationGrainQuantityDetail model = new ClaimCalculationGrainQuantityDetail();

		// From CIRRAS
		model.setCoverageValue(productRsrc.getCoverageDollars());
		model.setDeductible(productRsrc.getDeductibleLevel());
		model.setInsurableValue(productRsrc.getSelectedInsurableValue());
		model.setInsuredAcres(productRsrc.getAcres());
		model.setProbableYield(productRsrc.getProbableYield());
		model.setProductionGuaranteeWeight(productRsrc.getProductionGuarantee());

		// From CUWS
		// CUWS stores yield data always using the non-pedigree crop id, whereas CCS stores Calculations for pedigree commodities using that crop id. So we have to account for this mis-match 
		// here when filtering for Verified Yield.
		Integer vysCropCommodityId = null;
		if ( crpDto.getIsPedigreeInd() ) {
			vysCropCommodityId = linkedCrpDto.getCropCommodityId();
		} else {
			vysCropCommodityId = crpDto.getCropCommodityId();
		}
		
		
		VerifiedYieldSummary vys = null;
		if ( verifiedYield.getVerifiedYieldSummaries() != null ) {
			for ( VerifiedYieldSummary currVys : verifiedYield.getVerifiedYieldSummaries() ) {
				if ( currVys.getCropCommodityId().equals(vysCropCommodityId) && currVys.getIsPedigreeInd().equals(crpDto.getIsPedigreeInd()) ) {
					vys = currVys;
					break;
				}
			}
		}

		if ( vys != null ) { 
			model.setTotalYieldToCount(vys.getYieldToCount());
		} else {
			throw new FactoryException("Did not find Verified Yield Summary for " + crpDto.getCommodityName());
		}

		// User Entered
		model.setAssessedYield(null);
		model.setDamagedAcres(null);
		model.setEarlyEstDeemedYieldValue(null);
		model.setInspEarlyEstYield(null);
		model.setSeededAcres(null);
		
		// Calculated
		model.setCalcEarlyEstYield(null);
		model.setFiftyPercentProductionGuarantee(null);
		model.setYieldValue(null);
		model.setYieldValueWithEarlyEstDeemedYield(null);

		return model;
	}
	
	
	@Override
	public ClaimCalculation getCalculationFromCalculation(ClaimCalculation claimCalculation, FactoryContext context,
			WebAdeAuthentication authentication) throws FactoryException {
		ClaimCalculationRsrc resource = new ClaimCalculationRsrc();

		// Copy from claimCalculation.
		populateResource(resource, claimCalculation);

		// Copy varieties
		if (claimCalculation.getVarieties() != null && claimCalculation.getVarieties().size() > 0) {
			List<ClaimCalculationVariety> modelVarieties = new ArrayList<ClaimCalculationVariety>();

			for (ClaimCalculationVariety variety : claimCalculation.getVarieties()) {
				ClaimCalculationVariety modelVariety = createClaimCalculationVarietyFromCalculation(variety);
				modelVarieties.add(modelVariety);
			}

			resource.setVarieties(modelVarieties);
		}

		// Copy grapes quantity data
		if (claimCalculation.getClaimCalculationGrapes() != null) {
			resource.setClaimCalculationGrapes(
					createClaimCalculationGrapesFromCalculation(claimCalculation.getClaimCalculationGrapes()));
		}

		// Copy berries quantity data
		if (claimCalculation.getClaimCalculationBerries() != null) {
			resource.setClaimCalculationBerries(
					createClaimCalculationBerriesFromCalculation(claimCalculation.getClaimCalculationBerries()));
		}

		// Copy plant units data
		if (claimCalculation.getClaimCalculationPlantUnits() != null) {
			resource.setClaimCalculationPlantUnits(
					createClaimCalculationPlantUnitsFromCalculation(claimCalculation.getClaimCalculationPlantUnits()));
		}

		// Copy plant acres data
		if (claimCalculation.getClaimCalculationPlantAcres() != null) {
			resource.setClaimCalculationPlantAcres(
					createClaimCalculationPlantAcresFromCalculation(claimCalculation.getClaimCalculationPlantAcres()));
		}
		
		// Copy grain unseeded data
		if (claimCalculation.getClaimCalculationGrainUnseeded() != null) {
			resource.setClaimCalculationGrainUnseeded(
					createClaimCalculationGrainUnseededFromCalculation(claimCalculation.getClaimCalculationGrainUnseeded()));
		}
		
		// Copy grain spot loss data
		if (claimCalculation.getClaimCalculationGrainSpotLoss() != null) {
			resource.setClaimCalculationGrainSpotLoss(
					createClaimCalculationGrainSpotLossFromCalculation(claimCalculation.getClaimCalculationGrainSpotLoss()));
		}
		
		String eTag = getEtag(resource);
		resource.setETag(eTag);

		URI baseUri = getBaseURI(context);

		String result = UriBuilder.fromUri(baseUri).path(ClaimCalculationListEndpoint.class).build().toString();
		resource.getLinks().add(new RelLink(ResourceTypes.CREATE_CLAIM_CALCULATION, result, "POST"));

		return resource;
	}

	private void populateResource(ClaimCalculationRsrc resource, ClaimCalculation claimCalculation) {

		// Copy claim-related fields.
		resource.setClaimNumber(claimCalculation.getClaimNumber());
		resource.setContractId(claimCalculation.getContractId());
		resource.setPolicyNumber(claimCalculation.getPolicyNumber());
		resource.setGrowerNumber(claimCalculation.getGrowerNumber());
		resource.setGrowerName(claimCalculation.getGrowerName());
		resource.setInsurancePlanId(claimCalculation.getInsurancePlanId());
		resource.setInsurancePlanName(claimCalculation.getInsurancePlanName());
		resource.setCoverageName(claimCalculation.getCoverageName());
		resource.setCommodityCoverageCode(claimCalculation.getCommodityCoverageCode());
		resource.setCommodityName(claimCalculation.getCommodityName());
		resource.setCropCommodityId(claimCalculation.getCropCommodityId());
		resource.setClaimStatusCode(claimCalculation.getClaimStatusCode());
		resource.setClaimType(((ClaimCalculationRsrc) claimCalculation).getClaimType());
		resource.setGrowerAddressLine1(claimCalculation.getGrowerAddressLine1());
		resource.setGrowerAddressLine2(claimCalculation.getGrowerAddressLine2());
		resource.setGrowerPostalCode(claimCalculation.getGrowerPostalCode());
		resource.setGrowerCity(claimCalculation.getGrowerCity());
		resource.setGrowerProvince(claimCalculation.getGrowerProvince());
		resource.setCropYear(claimCalculation.getCropYear());
		resource.setInsuredByMeasurementType(claimCalculation.getInsuredByMeasurementType());
		resource.setPrimaryPerilCode(claimCalculation.getPrimaryPerilCode());
		resource.setSecondaryPerilCode(claimCalculation.getSecondaryPerilCode());
		resource.setSubmittedByUserid(claimCalculation.getSubmittedByUserid());
		resource.setSubmittedByName(claimCalculation.getSubmittedByName());
		resource.setSubmittedByDate(claimCalculation.getSubmittedByDate());
		resource.setRecommendedByUserid(claimCalculation.getRecommendedByUserid());
		resource.setRecommendedByName(claimCalculation.getRecommendedByName());
		resource.setRecommendedByDate(claimCalculation.getRecommendedByDate());
		resource.setApprovedByUserid(claimCalculation.getApprovedByUserid());
		resource.setApprovedByName(claimCalculation.getApprovedByName());
		resource.setApprovedByDate(claimCalculation.getApprovedByDate());
		resource.setCalculateIivInd(claimCalculation.getCalculateIivInd());
		resource.setHasChequeReqInd(claimCalculation.getHasChequeReqInd());

		// Copy calculation-related fields.
		resource.setCalculationComment(claimCalculation.getCalculationComment());
		resource.setCurrentClaimStatusCode(claimCalculation.getCurrentClaimStatusCode());
		resource.setCurrentHasChequeReqInd(claimCalculation.getCurrentHasChequeReqInd());

		// These are calculated in CirrasClaimServiceImpl.calculateTotals
		// resource.setTotalClaimAmount(claimCalculation.getTotalClaimAmount());
		// resource.setTotalProductionValue(claimCalculation.getTotalProductionValue());

	}

	private ClaimCalculationVariety createClaimCalculationVarietyFromCalculation(
			ClaimCalculationVariety claimCalcVariety) {
		ClaimCalculationVariety model = new ClaimCalculationVariety();

		model.setCropVarietyId(claimCalcVariety.getCropVarietyId());
		model.setAveragePrice(claimCalcVariety.getAveragePrice());
		model.setAveragePriceOverride(claimCalcVariety.getAveragePriceOverride());
		model.setAveragePriceFinal(claimCalcVariety.getAveragePriceFinal());
		model.setVarietyName(claimCalcVariety.getVarietyName());

		model.setYieldActual(claimCalcVariety.getYieldActual());
		model.setYieldAssessed(claimCalcVariety.getYieldAssessed());
		model.setYieldAssessedReason(claimCalcVariety.getYieldAssessedReason());

		// These are calculated in
		// CirrasClaimServiceImpl.calculateVarietyInsurableValues and
		// CirrasClaimServiceImpl.calculateTotals
		// model.setInsurableValue(claimCalcVariety.getInsurableValue());
		// model.setVarietyProductionValue(claimCalcVariety.getVarietyProductionValue());
		// model.setYieldTotal(claimCalcVariety.getYieldTotal());

		return model;
	}

	private ClaimCalculationBerries createClaimCalculationBerriesFromCalculation(
			ClaimCalculationBerries claimCalcBerries) {
		ClaimCalculationBerries model = new ClaimCalculationBerries();

		model.setClaimCalculationBerriesGuid(claimCalcBerries.getClaimCalculationBerriesGuid());
		model.setClaimCalculationGuid(claimCalcBerries.getClaimCalculationGuid());
		model.setTotalProbableYield(claimCalcBerries.getTotalProbableYield());
		model.setDeductibleLevel(claimCalcBerries.getDeductibleLevel());
		model.setProductionGuarantee(claimCalcBerries.getProductionGuarantee());
		model.setInsurableValueSelected(claimCalcBerries.getInsurableValueSelected());
		model.setInsurableValueHundredPercent(claimCalcBerries.getInsurableValueHundredPercent());
		model.setMaxCoverageAmount(claimCalcBerries.getMaxCoverageAmount());
		model.setDeclaredAcres(claimCalcBerries.getDeclaredAcres());
		model.setConfirmedAcres(claimCalcBerries.getConfirmedAcres());
		model.setHarvestedYield(claimCalcBerries.getHarvestedYield());
		model.setAppraisedYield(claimCalcBerries.getAppraisedYield());
		model.setAbandonedYield(claimCalcBerries.getAbandonedYield());
		model.setTotalYieldFromAdjuster(claimCalcBerries.getTotalYieldFromAdjuster());
		model.setYieldAssessment(claimCalcBerries.getYieldAssessment());

		// These are calculated in
		// CirrasClaimServiceImpl.calculateVarietyInsurableValues and
		// CirrasClaimServiceImpl.calculateTotals
		// model.setAdjustmentFactor(claimCalcBerries.getAdjustmentFactor());
		// model.setAdjustedProductionGuarantee(claimCalcBerries.getAdjustedProductionGuarantee());
		// model.setCoverageAmountAdjusted(claimCalcBerries.getCoverageAmountAdjusted());
		// model.setTotalYieldFromDop(claimCalcBerries.getTotalYieldFromDop());
		// model.setTotalYieldForCalculation(claimCalcBerries.getTotalYieldForCalculation());
		// model.setYieldLossEligible(claimCalcBerries.getYieldLossEligible());

		return model;
	}

	private ClaimCalculationPlantUnits createClaimCalculationPlantUnitsFromCalculation(
			ClaimCalculationPlantUnits claimCalcPlantUnits) {
		ClaimCalculationPlantUnits model = new ClaimCalculationPlantUnits();

		model.setClaimCalcPlantUnitsGuid(claimCalcPlantUnits.getClaimCalcPlantUnitsGuid());
		model.setClaimCalculationGuid(claimCalcPlantUnits.getClaimCalculationGuid());
		model.setInsuredUnits(claimCalcPlantUnits.getInsuredUnits());
		model.setLessAdjustmentReason(claimCalcPlantUnits.getLessAdjustmentReason());
		model.setLessAdjustmentUnits(claimCalcPlantUnits.getLessAdjustmentUnits());
		model.setDeductibleLevel(claimCalcPlantUnits.getDeductibleLevel());
		model.setInsurableValue(claimCalcPlantUnits.getInsurableValue());
		model.setDamagedUnits(claimCalcPlantUnits.getDamagedUnits());
		model.setLessAssessmentReason(claimCalcPlantUnits.getLessAssessmentReason());
		model.setLessAssessmentUnits(claimCalcPlantUnits.getLessAssessmentUnits());

		// These are calculated in CirrasClaimServiceImpl.calculateTotalsPlantUnits
		// model.setAdjustedUnits(claimCalcPlantUnits.getAdjustedUnits());
		// model.setDeductibleUnits(claimCalcPlantUnits.getDeductibleUnits());
		// model.setTotalCoverageUnits(claimCalcPlantUnits.getTotalCoverageUnits());
		// model.setCoverageAmount(claimCalcPlantUnits.getCoverageAmount());
		// model.setTotalDamagedUnits(claimCalcPlantUnits.getTotalDamagedUnits());

		return model;
	}

	private ClaimCalculationPlantAcres createClaimCalculationPlantAcresFromCalculation(
			ClaimCalculationPlantAcres claimCalcPlantAcres) {
		ClaimCalculationPlantAcres model = new ClaimCalculationPlantAcres();

		model.setClaimCalcPlantAcresGuid(claimCalcPlantAcres.getClaimCalcPlantAcresGuid());
		model.setClaimCalculationGuid(claimCalcPlantAcres.getClaimCalculationGuid());
		model.setDeclaredAcres(claimCalcPlantAcres.getDeclaredAcres());
		model.setConfirmedAcres(claimCalcPlantAcres.getConfirmedAcres());
		model.setDeductibleLevel(claimCalcPlantAcres.getDeductibleLevel());
		model.setDamagedAcres(claimCalcPlantAcres.getDamagedAcres());
		model.setInsurableValue(claimCalcPlantAcres.getInsurableValue());
		model.setLessAssessmentReason(claimCalcPlantAcres.getLessAssessmentReason());
		model.setLessAssessmentAmount(claimCalcPlantAcres.getLessAssessmentAmount());

		// These are calculated in CirrasClaimServiceImpl.calculateTotalsPlantAcres
		// model.setInsuredAcres(claimCalcPlantAcres.getInsuredAcres());
		// model.setDeductibleAcres(claimCalcPlantAcres.getDeductibleAcres());
		// model.setTotalCoverageAcres(claimCalcPlantAcres.getTotalCoverageAcres());
		// model.setAcresLossCovered(claimCalcPlantAcres.getAcresLossCovered());
		// model.setCoverageAmount(claimCalcPlantAcres.getCoverageAmount());

		return model;
	}

	private ClaimCalculationGrapes createClaimCalculationGrapesFromCalculation(ClaimCalculationGrapes claimCalcGrapes) {
		ClaimCalculationGrapes model = new ClaimCalculationGrapes();

		model.setClaimCalculationGrapesGuid(claimCalcGrapes.getClaimCalculationGrapesGuid());
		model.setClaimCalculationGuid(claimCalcGrapes.getClaimCalculationGuid());
		model.setInsurableValueSelected(claimCalcGrapes.getInsurableValueSelected());
		model.setInsurableValueHundredPercent(claimCalcGrapes.getInsurableValueHundredPercent());
		model.setCoverageAmount(claimCalcGrapes.getCoverageAmount());
		model.setCoverageAmountAssessed(claimCalcGrapes.getCoverageAmountAssessed());
		model.setCoverageAssessedReason(claimCalcGrapes.getCoverageAssessedReason());

		// These are calculated in CirrasClaimServiceImpl.calculateTotalsGrapes
		// model.setCoverageAmountAdjusted(claimCalcGrapes.getCoverageAmountAdjusted());
		// model.setTotalProductionValue(claimCalcGrapes.getTotalProductionValue());

		return model;
	}

	private ClaimCalculationGrainUnseeded createClaimCalculationGrainUnseededFromCalculation(ClaimCalculationGrainUnseeded claimCalcGrainUnseeded) {
		ClaimCalculationGrainUnseeded model = new ClaimCalculationGrainUnseeded();

		model.setClaimCalculationGrainUnseededGuid(claimCalcGrainUnseeded.getClaimCalculationGrainUnseededGuid());
		model.setClaimCalculationGuid(claimCalcGrainUnseeded.getClaimCalculationGuid());
		model.setInsuredAcres(claimCalcGrainUnseeded.getInsuredAcres());
		model.setLessAdjustmentAcres(claimCalcGrainUnseeded.getLessAdjustmentAcres());
		model.setDeductibleLevel(claimCalcGrainUnseeded.getDeductibleLevel());
		model.setInsurableValue(claimCalcGrainUnseeded.getInsurableValue());
		model.setUnseededAcres(claimCalcGrainUnseeded.getUnseededAcres());
		model.setLessAssessmentAcres(claimCalcGrainUnseeded.getLessAssessmentAcres());

		// These are calculated in CirrasClaimServiceImpl.calculateTotalsGrainUnseeded
		// model.setAdjustedAcres(claimCalcGrainUnseeded.getAdjustedAcres());
		// model.setDeductibleAcres(claimCalcGrainUnseeded.getDeductibleAcres());
		// model.setMaxEligibleAcres(claimCalcGrainUnseeded.getMaxEligibleAcres());
		// model.setCoverageValue(claimCalcGrainUnseeded.getCoverageValue());
		// model.setEligibleUnseededAcres(claimCalcGrainUnseeded.getEligibleUnseededAcres());

		return model;
	}

	private ClaimCalculationGrainSpotLoss createClaimCalculationGrainSpotLossFromCalculation(ClaimCalculationGrainSpotLoss claimCalcGrainSpotLoss) {
		ClaimCalculationGrainSpotLoss model = new ClaimCalculationGrainSpotLoss();

		model.setClaimCalculationGrainSpotLossGuid(claimCalcGrainSpotLoss.getClaimCalculationGrainSpotLossGuid());
		model.setClaimCalculationGuid(claimCalcGrainSpotLoss.getClaimCalculationGuid());
		model.setInsuredAcres(claimCalcGrainSpotLoss.getInsuredAcres());
		model.setCoverageAmtPerAcre(claimCalcGrainSpotLoss.getCoverageAmtPerAcre());
		model.setCoverageValue(claimCalcGrainSpotLoss.getCoverageValue());
		model.setAdjustedAcres(claimCalcGrainSpotLoss.getAdjustedAcres());
		model.setPercentYieldReduction(claimCalcGrainSpotLoss.getPercentYieldReduction());
		model.setEligibleYieldReduction(claimCalcGrainSpotLoss.getEligibleYieldReduction());
		model.setSpotLossReductionValue(claimCalcGrainSpotLoss.getSpotLossReductionValue());
		model.setDeductible(claimCalcGrainSpotLoss.getDeductible());
		
		return model;
	}
	
	@Override
	public ClaimCalculationList<? extends ClaimCalculation> getClaimCalculationList(PagedDtos<ClaimCalculationDto> dtos,
			Integer claimNumber, String policyNumber, Integer cropYear, String calculationStatusCode,
			String createClaimCalcUserGuid, String updateClaimCalcUserGuid, Integer insurancePlanId, String sortColumn,
			String sortDirection, Integer pageRowCount, FactoryContext context, WebAdeAuthentication authentication)
			throws FactoryException, DaoException {
		URI baseUri = getBaseURI(context);

		ClaimCalculationListRsrc result = null;

		List<ClaimCalculationRsrc> resources = new ArrayList<ClaimCalculationRsrc>();

		for (ClaimCalculationDto dto : dtos.getResults()) {
			ClaimCalculationRsrc resource = new ClaimCalculationRsrc();

			populateResource(resource, dto);
			// don't populate varieties for search results

			setSelfLink(dto.getClaimCalculationGuid(), resource, baseUri);
			resources.add(resource);
		}

		int pageNumber = dtos.getPageNumber();
		int totalRowCount = dtos.getTotalRowCount();
		pageRowCount = Integer.valueOf(pageRowCount == null ? totalRowCount : pageRowCount.intValue());
		int totalPageCount = (int) Math.ceil(((double) totalRowCount) / ((double) pageRowCount.intValue()));

		result = new ClaimCalculationListRsrc();
		result.setCollection(resources);
		result.setPageNumber(pageNumber);
		result.setPageRowCount(pageRowCount.intValue());
		result.setTotalRowCount(totalRowCount);
		result.setTotalPageCount(totalPageCount);

		String eTag = getEtag(result);
		result.setETag(eTag);

		setSelfLink(result, claimNumber, policyNumber, cropYear, calculationStatusCode, createClaimCalcUserGuid,
				updateClaimCalcUserGuid, sortColumn, sortDirection, pageNumber, pageRowCount.intValue(), baseUri);

		setLinks(result, claimNumber, policyNumber, cropYear, calculationStatusCode, createClaimCalcUserGuid,
				updateClaimCalcUserGuid, sortColumn, sortDirection, pageNumber, pageRowCount.intValue(), totalRowCount,
				baseUri, authentication);

		return result;
	}

	@Override
	public void updateDto(ClaimCalculationDto dto, ClaimCalculation model) {

		dto.setGrowerNumber(model.getGrowerNumber());
		dto.setGrowerName(model.getGrowerName());
		dto.setGrowerAddressLine1(model.getGrowerAddressLine1());
		dto.setGrowerAddressLine2(model.getGrowerAddressLine2());
		dto.setGrowerPostalCode(model.getGrowerPostalCode());
		dto.setGrowerCity(model.getGrowerCity());
		dto.setGrowerProvince(model.getGrowerProvince());
		dto.setPrimaryPerilCode(model.getPrimaryPerilCode());
		dto.setSecondaryPerilCode(model.getSecondaryPerilCode());
		dto.setClaimStatusCode(model.getClaimStatusCode());
		dto.setCalculationStatusCode(model.getCalculationStatusCode());
		dto.setCalculationComment(model.getCalculationComment());
		dto.setTotalClaimAmount(model.getTotalClaimAmount());
		dto.setSubmittedByUserid(model.getSubmittedByUserid());
		dto.setSubmittedByName(model.getSubmittedByName());
		dto.setSubmittedByDate(model.getSubmittedByDate());
		dto.setRecommendedByUserid(model.getRecommendedByUserid());
		dto.setRecommendedByName(model.getRecommendedByName());
		dto.setRecommendedByDate(model.getRecommendedByDate());
		dto.setApprovedByUserid(model.getApprovedByUserid());
		dto.setApprovedByName(model.getApprovedByName());
		dto.setApprovedByDate(model.getApprovedByDate());
		dto.setHasChequeReqInd(model.getHasChequeReqInd());

	}

	@Override
	public void updateDto(ClaimCalculationVarietyDto dto, ClaimCalculationVariety model) {
		dto.setAveragePrice(model.getAveragePrice());
		dto.setAveragePriceOverride(model.getAveragePriceOverride());
		dto.setAveragePriceFinal(model.getAveragePriceFinal());
		dto.setInsurableValue(model.getInsurableValue());
		dto.setYieldAssessed(model.getYieldAssessed());
		dto.setYieldTotal(model.getYieldTotal());
		dto.setYieldActual(model.getYieldActual());
		dto.setVarietyProductionValue(model.getVarietyProductionValue());
		dto.setYieldAssessedReason(model.getYieldAssessedReason());
	}

	@Override
	public void updateDto(ClaimCalculationBerriesDto dto, ClaimCalculationBerries model) {
		dto.setTotalProbableYield(model.getTotalProbableYield());
		dto.setDeductibleLevel(model.getDeductibleLevel());
		dto.setProductionGuarantee(model.getProductionGuarantee());
		dto.setDeclaredAcres(model.getDeclaredAcres());
		dto.setConfirmedAcres(model.getConfirmedAcres());
		dto.setAdjustmentFactor(model.getAdjustmentFactor());
		dto.setAdjustedProductionGuarantee(model.getAdjustedProductionGuarantee());
		dto.setInsurableValueSelected(model.getInsurableValueSelected());
		dto.setInsurableValueHundredPercent(model.getInsurableValueHundredPercent());
		dto.setCoverageAmountAdjusted(model.getCoverageAmountAdjusted());
		dto.setMaxCoverageAmount(model.getMaxCoverageAmount());
		dto.setHarvestedYield(model.getHarvestedYield());
		dto.setAppraisedYield(model.getAppraisedYield());
		dto.setAbandonedYield(model.getAbandonedYield());
		dto.setTotalYieldFromDop(model.getTotalYieldFromDop());
		dto.setTotalYieldFromAdjuster(model.getTotalYieldFromAdjuster());
		dto.setYieldAssessment(model.getYieldAssessment());
		dto.setTotalYieldForCalculation(model.getTotalYieldForCalculation());
		dto.setYieldLossEligible(model.getYieldLossEligible());
	}

	@Override
	public void updateDto(ClaimCalculationPlantUnitsDto dto, ClaimCalculationPlantUnits model) {
		dto.setInsuredUnits(model.getInsuredUnits());
		dto.setLessAdjustmentReason(model.getLessAdjustmentReason());
		dto.setLessAdjustmentUnits(model.getLessAdjustmentUnits());
		dto.setAdjustedUnits(model.getAdjustedUnits());
		dto.setDeductibleLevel(model.getDeductibleLevel());
		dto.setDeductibleUnits(model.getDeductibleUnits());
		dto.setTotalCoverageUnits(model.getTotalCoverageUnits());
		dto.setInsurableValue(model.getInsurableValue());
		dto.setCoverageAmount(model.getCoverageAmount());
		dto.setDamagedUnits(model.getDamagedUnits());
		dto.setLessAssessmentReason(model.getLessAssessmentReason());
		dto.setLessAssessmentUnits(model.getLessAssessmentUnits());
		dto.setTotalDamagedUnits(model.getTotalDamagedUnits());
	}

	@Override
	public void updateDto(ClaimCalculationPlantAcresDto dto, ClaimCalculationPlantAcres model) {
		dto.setDeclaredAcres(model.getDeclaredAcres());
		dto.setConfirmedAcres(model.getConfirmedAcres());
		dto.setInsuredAcres(model.getInsuredAcres());
		dto.setDeductibleLevel(model.getDeductibleLevel());
		dto.setDeductibleAcres(model.getDeductibleAcres());
		dto.setTotalCoverageAcres(model.getTotalCoverageAcres());
		dto.setDamagedAcres(model.getDamagedAcres());
		dto.setAcresLossCovered(model.getAcresLossCovered());
		dto.setInsurableValue(model.getInsurableValue());
		dto.setCoverageAmount(model.getCoverageAmount());
		dto.setLessAssessmentReason(model.getLessAssessmentReason());
		dto.setLessAssessmentAmount(model.getLessAssessmentAmount());
	}

	@Override
	public void updateDto(ClaimCalculationGrainUnseededDto dto, ClaimCalculationGrainUnseeded model) {

		dto.setInsuredAcres(model.getInsuredAcres());
		dto.setLessAdjustmentAcres(model.getLessAdjustmentAcres());
		dto.setAdjustedAcres(model.getAdjustedAcres());
		dto.setDeductibleLevel(model.getDeductibleLevel());
		dto.setDeductibleAcres(model.getDeductibleAcres());
		dto.setMaxEligibleAcres(model.getMaxEligibleAcres());
		dto.setInsurableValue(model.getInsurableValue());
		dto.setCoverageValue(model.getCoverageValue());
		dto.setUnseededAcres(model.getUnseededAcres());
		dto.setLessAssessmentAcres(model.getLessAssessmentAcres());
		dto.setEligibleUnseededAcres(model.getEligibleUnseededAcres());
		
	}

	@Override
	public void updateDto(ClaimCalculationGrainSpotLossDto dto, ClaimCalculationGrainSpotLoss model) {
		
		dto.setAdjustedAcres(model.getAdjustedAcres());
		dto.setCoverageAmtPerAcre(model.getCoverageAmtPerAcre());
		dto.setCoverageValue(model.getCoverageValue());
		dto.setDeductible(model.getDeductible());
		dto.setEligibleYieldReduction(model.getEligibleYieldReduction());
		dto.setInsuredAcres(model.getInsuredAcres());
		dto.setPercentYieldReduction(model.getPercentYieldReduction());
		dto.setSpotLossReductionValue(model.getSpotLossReductionValue());
		
	}
	
	@Override
	public void updateDto(ClaimCalculationGrapesDto dto, ClaimCalculationGrapes model) {

		dto.setInsurableValueSelected(model.getInsurableValueSelected());
		dto.setInsurableValueHundredPercent(model.getInsurableValueHundredPercent());
		dto.setCoverageAmount(model.getCoverageAmount());
		dto.setCoverageAmountAssessed(model.getCoverageAmountAssessed());
		dto.setCoverageAssessedReason(model.getCoverageAssessedReason());
		dto.setCoverageAmountAdjusted(model.getCoverageAmountAdjusted());
		dto.setTotalProductionValue(model.getTotalProductionValue());

	}

	@Override
	public ClaimCalculationDto createDto(ClaimCalculation resource) {
		ClaimCalculationDto dto = new ClaimCalculationDto();

		// don't set ClaimCalculationGuid
		dto.setPrimaryPerilCode(resource.getPrimaryPerilCode());
		dto.setSecondaryPerilCode(resource.getSecondaryPerilCode());
		dto.setClaimStatusCode(resource.getClaimStatusCode());
		dto.setCommodityCoverageCode(resource.getCommodityCoverageCode());
		dto.setCalculationStatusCode(resource.getCalculationStatusCode());
		dto.setInsurancePlanId(resource.getInsurancePlanId());
		dto.setCropCommodityId(resource.getCropCommodityId());
		dto.setCropYear(resource.getCropYear());
		dto.setInsuredByMeasurementType(resource.getInsuredByMeasurementType());
		dto.setPolicyNumber(resource.getPolicyNumber());
		dto.setClaimNumber(resource.getClaimNumber());
		dto.setContractId(resource.getContractId());
		dto.setCalculationVersion(resource.getCalculationVersion());
		dto.setGrowerNumber(resource.getGrowerNumber());
		dto.setGrowerName(resource.getGrowerName());
		dto.setGrowerAddressLine1(resource.getGrowerAddressLine1());
		dto.setGrowerAddressLine2(resource.getGrowerAddressLine2());
		dto.setGrowerPostalCode(resource.getGrowerPostalCode());
		dto.setGrowerCity(resource.getGrowerCity());
		dto.setGrowerProvince(resource.getGrowerProvince());
		dto.setTotalClaimAmount(resource.getTotalClaimAmount());
		dto.setCalculationComment(resource.getCalculationComment());
		dto.setSubmittedByUserid(resource.getSubmittedByUserid());
		dto.setSubmittedByName(resource.getSubmittedByName());
		dto.setSubmittedByDate(resource.getSubmittedByDate());
		dto.setRecommendedByUserid(resource.getRecommendedByUserid());
		dto.setRecommendedByName(resource.getRecommendedByName());
		dto.setRecommendedByDate(resource.getRecommendedByDate());
		dto.setApprovedByUserid(resource.getApprovedByUserid());
		dto.setApprovedByName(resource.getApprovedByName());
		dto.setApprovedByDate(resource.getApprovedByDate());
		dto.setCalculateIivInd(resource.getCalculateIivInd());
		dto.setHasChequeReqInd(resource.getHasChequeReqInd());
		return dto;
	}

	@Override
	public ClaimCalculationVarietyDto createDto(ClaimCalculationVariety model) {
		ClaimCalculationVarietyDto dto = new ClaimCalculationVarietyDto();

		dto.setClaimCalculationGuid(model.getClaimCalculationGuid());
		dto.setCropVarietyId(model.getCropVarietyId());
		dto.setAveragePrice(model.getAveragePrice());
		dto.setAveragePriceOverride(model.getAveragePriceOverride());
		dto.setAveragePriceFinal(model.getAveragePriceFinal());
		dto.setInsurableValue(model.getInsurableValue());
		dto.setYieldAssessedReason(model.getYieldAssessedReason());
		dto.setYieldAssessed(model.getYieldAssessed());
		dto.setYieldTotal(model.getYieldTotal());
		dto.setYieldActual(model.getYieldActual());
		dto.setVarietyProductionValue(model.getVarietyProductionValue());

		return dto;
	}

	@Override
	public ClaimCalculationBerriesDto createDto(ClaimCalculationBerries model) {
		ClaimCalculationBerriesDto dto = new ClaimCalculationBerriesDto();

		dto.setClaimCalculationGuid(model.getClaimCalculationGuid());
		dto.setTotalProbableYield(model.getTotalProbableYield());
		dto.setDeductibleLevel(model.getDeductibleLevel());
		dto.setProductionGuarantee(model.getProductionGuarantee());
		dto.setDeclaredAcres(model.getDeclaredAcres());
		dto.setConfirmedAcres(model.getConfirmedAcres());
		dto.setAdjustmentFactor(model.getAdjustmentFactor());
		dto.setAdjustedProductionGuarantee(model.getAdjustedProductionGuarantee());
		dto.setInsurableValueSelected(model.getInsurableValueSelected());
		dto.setInsurableValueHundredPercent(model.getInsurableValueHundredPercent());
		dto.setCoverageAmountAdjusted(model.getCoverageAmountAdjusted());
		dto.setMaxCoverageAmount(model.getMaxCoverageAmount());
		dto.setHarvestedYield(model.getHarvestedYield());
		dto.setAppraisedYield(model.getAppraisedYield());
		dto.setAbandonedYield(model.getAbandonedYield());
		dto.setTotalYieldFromDop(model.getTotalYieldFromDop());
		dto.setTotalYieldFromAdjuster(model.getTotalYieldFromAdjuster());
		dto.setYieldAssessment(model.getYieldAssessment());
		dto.setTotalYieldForCalculation(model.getTotalYieldForCalculation());
		dto.setYieldLossEligible(model.getYieldLossEligible());

		return dto;
	}

	@Override
	public ClaimCalculationPlantUnitsDto createDto(ClaimCalculationPlantUnits model) {
		ClaimCalculationPlantUnitsDto dto = new ClaimCalculationPlantUnitsDto();

		dto.setClaimCalculationGuid(model.getClaimCalculationGuid());
		dto.setInsuredUnits(model.getInsuredUnits());
		dto.setLessAdjustmentReason(model.getLessAdjustmentReason());
		dto.setLessAdjustmentUnits(model.getLessAdjustmentUnits());
		dto.setAdjustedUnits(model.getAdjustedUnits());
		dto.setDeductibleLevel(model.getDeductibleLevel());
		dto.setDeductibleUnits(model.getDeductibleUnits());
		dto.setTotalCoverageUnits(model.getTotalCoverageUnits());
		dto.setInsurableValue(model.getInsurableValue());
		dto.setCoverageAmount(model.getCoverageAmount());
		dto.setDamagedUnits(model.getDamagedUnits());
		dto.setLessAssessmentReason(model.getLessAssessmentReason());
		dto.setLessAssessmentUnits(model.getLessAssessmentUnits());
		dto.setTotalDamagedUnits(model.getTotalDamagedUnits());

		return dto;
	}

	@Override
	public ClaimCalculationPlantAcresDto createDto(ClaimCalculationPlantAcres model) {
		ClaimCalculationPlantAcresDto dto = new ClaimCalculationPlantAcresDto();

		dto.setClaimCalculationGuid(model.getClaimCalculationGuid());
		dto.setDeclaredAcres(model.getDeclaredAcres());
		dto.setConfirmedAcres(model.getConfirmedAcres());
		dto.setInsuredAcres(model.getInsuredAcres());
		dto.setDeductibleLevel(model.getDeductibleLevel());
		dto.setDeductibleAcres(model.getDeductibleAcres());
		dto.setTotalCoverageAcres(model.getTotalCoverageAcres());
		dto.setDamagedAcres(model.getDamagedAcres());
		dto.setAcresLossCovered(model.getAcresLossCovered());
		dto.setInsurableValue(model.getInsurableValue());
		dto.setCoverageAmount(model.getCoverageAmount());
		dto.setLessAssessmentReason(model.getLessAssessmentReason());
		dto.setLessAssessmentAmount(model.getLessAssessmentAmount());

		return dto;
	}

	@Override
	public ClaimCalculationGrainUnseededDto createDto(ClaimCalculationGrainUnseeded model) {
		ClaimCalculationGrainUnseededDto dto = new ClaimCalculationGrainUnseededDto();

		dto.setClaimCalculationGuid(model.getClaimCalculationGuid());
		dto.setInsuredAcres(model.getInsuredAcres());
		dto.setLessAdjustmentAcres(model.getLessAdjustmentAcres());
		dto.setAdjustedAcres(model.getAdjustedAcres());
		dto.setDeductibleLevel(model.getDeductibleLevel());
		dto.setDeductibleAcres(model.getDeductibleAcres());
		dto.setMaxEligibleAcres(model.getMaxEligibleAcres());
		dto.setInsurableValue(model.getInsurableValue());
		dto.setCoverageValue(model.getCoverageValue());
		dto.setUnseededAcres(model.getUnseededAcres());
		dto.setLessAssessmentAcres(model.getLessAssessmentAcres());
		dto.setEligibleUnseededAcres(model.getEligibleUnseededAcres());

		return dto;
	}

	@Override
	public ClaimCalculationGrainSpotLossDto createDto(ClaimCalculationGrainSpotLoss model) {
		ClaimCalculationGrainSpotLossDto dto = new ClaimCalculationGrainSpotLossDto();

		dto.setClaimCalculationGuid(model.getClaimCalculationGuid());
		dto.setAdjustedAcres(model.getAdjustedAcres());
		dto.setCoverageAmtPerAcre(model.getCoverageAmtPerAcre());
		dto.setCoverageValue(model.getCoverageValue());
		dto.setDeductible(model.getDeductible());
		dto.setEligibleYieldReduction(model.getEligibleYieldReduction());
		dto.setInsuredAcres(model.getInsuredAcres());
		dto.setPercentYieldReduction(model.getPercentYieldReduction());
		dto.setSpotLossReductionValue(model.getSpotLossReductionValue());

		return dto;
	}
	
	
	@Override
	public ClaimCalculationGrapesDto createDto(ClaimCalculationGrapes model) {
		ClaimCalculationGrapesDto dto = new ClaimCalculationGrapesDto();

		dto.setClaimCalculationGuid(model.getClaimCalculationGuid());
		dto.setInsurableValueSelected(model.getInsurableValueSelected());
		dto.setInsurableValueHundredPercent(model.getInsurableValueHundredPercent());
		dto.setCoverageAmount(model.getCoverageAmount());
		dto.setCoverageAmountAssessed(model.getCoverageAmountAssessed());
		dto.setCoverageAssessedReason(model.getCoverageAssessedReason());
		dto.setCoverageAmountAdjusted(model.getCoverageAmountAdjusted());
		dto.setTotalProductionValue(model.getTotalProductionValue());

		return dto;
	}

	private ClaimCalculationVariety createClaimCalculationVariety(ClaimCalculationVarietyDto dto) {
		ClaimCalculationVariety model = new ClaimCalculationVariety();

		model.setClaimCalculationVarietyGuid(dto.getClaimCalculationVarietyGuid());
		model.setClaimCalculationGuid(dto.getClaimCalculationGuid());
		model.setVarietyName(dto.getVarietyName());
		model.setCropVarietyId(dto.getCropVarietyId());
		model.setAveragePrice(dto.getAveragePrice());
		model.setAveragePriceOverride(dto.getAveragePriceOverride());
		model.setAveragePriceFinal(dto.getAveragePriceFinal());
		model.setInsurableValue(dto.getInsurableValue());
		model.setYieldAssessed(dto.getYieldAssessed());
		model.setYieldTotal(dto.getYieldTotal());
		model.setYieldActual(dto.getYieldActual());
		model.setVarietyProductionValue(dto.getVarietyProductionValue());
		model.setYieldAssessedReason(dto.getYieldAssessedReason());

		return model;
	}

	private ClaimCalculationBerries createClaimCalculationBerries(ClaimCalculationBerriesDto dto) {
		ClaimCalculationBerries model = new ClaimCalculationBerries();

		model.setClaimCalculationBerriesGuid(dto.getClaimCalculationBerriesGuid());
		model.setClaimCalculationGuid(dto.getClaimCalculationGuid());
		model.setTotalProbableYield(dto.getTotalProbableYield());
		model.setDeductibleLevel(dto.getDeductibleLevel());
		model.setProductionGuarantee(dto.getProductionGuarantee());
		model.setInsurableValueSelected(dto.getInsurableValueSelected());
		model.setInsurableValueHundredPercent(dto.getInsurableValueHundredPercent());
		model.setCoverageAmountAdjusted(dto.getCoverageAmountAdjusted());
		model.setMaxCoverageAmount(dto.getMaxCoverageAmount());
		model.setDeclaredAcres(dto.getDeclaredAcres());
		model.setConfirmedAcres(dto.getConfirmedAcres());
		model.setAdjustmentFactor(dto.getAdjustmentFactor());
		model.setAdjustedProductionGuarantee(dto.getAdjustedProductionGuarantee());
		model.setHarvestedYield(dto.getHarvestedYield());
		model.setAppraisedYield(dto.getAppraisedYield());
		model.setAbandonedYield(dto.getAbandonedYield());
		model.setTotalYieldFromDop(dto.getTotalYieldFromDop());
		model.setTotalYieldFromAdjuster(dto.getTotalYieldFromAdjuster());
		model.setYieldAssessment(dto.getYieldAssessment());
		model.setTotalYieldForCalculation(dto.getTotalYieldForCalculation());
		model.setYieldLossEligible(dto.getYieldLossEligible());

		return model;
	}

	private ClaimCalculationPlantUnits createClaimCalculationPlantUnits(ClaimCalculationPlantUnitsDto dto) {
		ClaimCalculationPlantUnits model = new ClaimCalculationPlantUnits();

		model.setClaimCalcPlantUnitsGuid(dto.getClaimCalcPlantUnitsGuid());
		model.setClaimCalculationGuid(dto.getClaimCalculationGuid());
		model.setInsuredUnits(dto.getInsuredUnits());
		model.setLessAdjustmentReason(dto.getLessAdjustmentReason());
		model.setLessAdjustmentUnits(dto.getLessAdjustmentUnits());
		model.setAdjustedUnits(dto.getAdjustedUnits());
		model.setDeductibleLevel(dto.getDeductibleLevel());
		model.setDeductibleUnits(dto.getDeductibleUnits());
		model.setTotalCoverageUnits(dto.getTotalCoverageUnits());
		model.setInsurableValue(dto.getInsurableValue());
		model.setCoverageAmount(dto.getCoverageAmount());
		model.setDamagedUnits(dto.getDamagedUnits());
		model.setLessAssessmentReason(dto.getLessAssessmentReason());
		model.setLessAssessmentUnits(dto.getLessAssessmentUnits());
		model.setTotalDamagedUnits(dto.getTotalDamagedUnits());

		return model;
	}

	private ClaimCalculationPlantAcres createClaimCalculationPlantAcres(ClaimCalculationPlantAcresDto dto) {
		ClaimCalculationPlantAcres model = new ClaimCalculationPlantAcres();

		model.setClaimCalcPlantAcresGuid(dto.getClaimCalcPlantAcresGuid());
		model.setClaimCalculationGuid(dto.getClaimCalculationGuid());
		model.setDeclaredAcres(dto.getDeclaredAcres());
		model.setConfirmedAcres(dto.getConfirmedAcres());
		model.setInsuredAcres(dto.getInsuredAcres());
		model.setDeductibleLevel(dto.getDeductibleLevel());
		model.setDeductibleAcres(dto.getDeductibleAcres());
		model.setTotalCoverageAcres(dto.getTotalCoverageAcres());
		model.setDamagedAcres(dto.getDamagedAcres());
		model.setAcresLossCovered(dto.getAcresLossCovered());
		model.setInsurableValue(dto.getInsurableValue());
		model.setCoverageAmount(dto.getCoverageAmount());
		model.setLessAssessmentReason(dto.getLessAssessmentReason());
		model.setLessAssessmentAmount(dto.getLessAssessmentAmount());

		return model;
	}

	private ClaimCalculationGrapes createClaimCalculationGrapes(ClaimCalculationGrapesDto dto) {
		ClaimCalculationGrapes model = new ClaimCalculationGrapes();

		model.setClaimCalculationGrapesGuid(dto.getClaimCalculationGrapesGuid());
		model.setClaimCalculationGuid(dto.getClaimCalculationGuid());
		model.setInsurableValueSelected(dto.getInsurableValueSelected());
		model.setInsurableValueHundredPercent(dto.getInsurableValueHundredPercent());
		model.setCoverageAmount(dto.getCoverageAmount());
		model.setCoverageAmountAssessed(dto.getCoverageAmountAssessed());
		model.setCoverageAssessedReason(dto.getCoverageAssessedReason());
		model.setCoverageAmountAdjusted(dto.getCoverageAmountAdjusted());
		model.setTotalProductionValue(dto.getTotalProductionValue());

		return model;
	}

	private ClaimCalculationGrainUnseeded createClaimCalculationGrainUnseeded(ClaimCalculationGrainUnseededDto dto) {
		ClaimCalculationGrainUnseeded model = new ClaimCalculationGrainUnseeded();

		model.setClaimCalculationGrainUnseededGuid(dto.getClaimCalculationGrainUnseededGuid());
		model.setClaimCalculationGuid(dto.getClaimCalculationGuid());
		model.setInsuredAcres(dto.getInsuredAcres());
		model.setLessAdjustmentAcres(dto.getLessAdjustmentAcres());
		model.setAdjustedAcres(dto.getAdjustedAcres());
		model.setDeductibleLevel(dto.getDeductibleLevel());
		model.setDeductibleAcres(dto.getDeductibleAcres());
		model.setMaxEligibleAcres(dto.getMaxEligibleAcres());
		model.setInsurableValue(dto.getInsurableValue());
		model.setCoverageValue(dto.getCoverageValue());
		model.setUnseededAcres(dto.getUnseededAcres());
		model.setLessAssessmentAcres(dto.getLessAssessmentAcres());
		model.setEligibleUnseededAcres(dto.getEligibleUnseededAcres());

		return model;
	}
	
	private ClaimCalculationGrainSpotLoss createClaimCalculationGrainSpotLoss(ClaimCalculationGrainSpotLossDto dto) {

		ClaimCalculationGrainSpotLoss model = new ClaimCalculationGrainSpotLoss();
		
		model.setClaimCalculationGrainSpotLossGuid(dto.getClaimCalculationGrainSpotLossGuid());
		model.setClaimCalculationGuid(dto.getClaimCalculationGuid());
		model.setInsuredAcres(dto.getInsuredAcres());
		model.setCoverageAmtPerAcre(dto.getCoverageAmtPerAcre());
		model.setCoverageValue(dto.getCoverageValue());
		model.setAdjustedAcres(dto.getAdjustedAcres());
		model.setPercentYieldReduction(dto.getPercentYieldReduction());
		model.setEligibleYieldReduction(dto.getEligibleYieldReduction());
		model.setSpotLossReductionValue(dto.getSpotLossReductionValue());
		model.setDeductible(dto.getDeductible());

		return model;
	}

	private ClaimCalculationGrainQuantity createClaimCalculationGrainQuantity(ClaimCalculationGrainQuantityDto dto) {
		ClaimCalculationGrainQuantity model = new ClaimCalculationGrainQuantity();

		model.setClaimCalculationGrainQuantityGuid(dto.getClaimCalculationGrainQuantityGuid());
		model.setAdvancedClaim(dto.getAdvancedClaim());
		model.setMaxClaimPayable(dto.getMaxClaimPayable());
		model.setProductionGuaranteeAmount(dto.getProductionGuaranteeAmount());
		model.setQuantityLossClaim(dto.getQuantityLossClaim());
		model.setReseedClaim(dto.getReseedClaim());
		model.setTotalCoverageValue(dto.getTotalCoverageValue());
		model.setTotalYieldLossValue(dto.getTotalYieldLossValue());
		
		return model;
	}

	private ClaimCalculationGrainQuantityDetail createClaimCalculationGrainQuantityDetail(ClaimCalculationGrainQuantityDetailDto dto) {
		ClaimCalculationGrainQuantityDetail model = new ClaimCalculationGrainQuantityDetail();

		model.setClaimCalculationGrainQuantityDetailGuid(dto.getClaimCalculationGrainQuantityDetailGuid());
		model.setClaimCalculationGuid(dto.getClaimCalculationGuid());
		model.setAssessedYield(dto.getAssessedYield());
		model.setCalcEarlyEstYield(dto.getCalcEarlyEstYield());
		model.setCoverageValue(dto.getCoverageValue());
		model.setDamagedAcres(dto.getDamagedAcres());
		model.setDeductible(dto.getDeductible());
		model.setEarlyEstDeemedYieldValue(dto.getEarlyEstDeemedYieldValue());
		model.setFiftyPercentProductionGuarantee(dto.getFiftyPercentProductionGuarantee());
		model.setInspEarlyEstYield(dto.getInspEarlyEstYield());
		model.setInsurableValue(dto.getInsurableValue());
		model.setInsuredAcres(dto.getInsuredAcres());
		model.setProbableYield(dto.getProbableYield());
		model.setProductionGuaranteeWeight(dto.getProductionGuaranteeWeight());
		model.setSeededAcres(dto.getSeededAcres());
		model.setTotalYieldToCount(dto.getTotalYieldToCount());
		model.setYieldValue(dto.getYieldValue());
		model.setYieldValueWithEarlyEstDeemedYield(dto.getYieldValueWithEarlyEstDeemedYield());

		return model;
	}
	
	private void populateResource(ClaimCalculationRsrc resource, ClaimCalculationDto dto) {

		resource.setClaimCalculationGuid(dto.getClaimCalculationGuid());
		resource.setCalculationVersion(dto.getCalculationVersion());
		resource.setTotalClaimAmount(dto.getTotalClaimAmount());
		resource.setCalculationStatusCode(dto.getCalculationStatusCode());
		resource.setCalculationComment(dto.getCalculationComment());
		resource.setRevisionCount(dto.getRevisionCount());
		resource.setCreateClaimCalcUserGuid(dto.getCreateClaimCalcUserGuid());
		resource.setCreateClaimCalcUserName(dto.getCreateClaimCalcUserName());
		resource.setCreateUser(dto.getCreateUser());
		resource.setCreateDate(dto.getCreateDate());
		resource.setUpdateClaimCalcUserGuid(dto.getUpdateClaimCalcUserGuid());
		resource.setUpdateClaimCalcUserName(dto.getUpdateClaimCalcUserName());
		resource.setUpdateUser(dto.getUpdateUser());
		resource.setUpdateDate(dto.getUpdateDate());
		resource.setClaimNumber(dto.getClaimNumber());
		resource.setContractId(dto.getContractId());
		resource.setCommodityCoverageCode(dto.getCommodityCoverageCode());
		resource.setCoverageName(dto.getCoverageName());
		resource.setCropCommodityId(dto.getCropCommodityId());
		resource.setCommodityName(dto.getCommodityName());
		resource.setIsPedigreeInd(dto.getIsPedigreeInd());
		resource.setPrimaryPerilCode(dto.getPrimaryPerilCode());
		resource.setSecondaryPerilCode(dto.getSecondaryPerilCode());
		resource.setClaimStatusCode(dto.getClaimStatusCode());
		resource.setSubmittedByUserid(dto.getSubmittedByUserid());
		resource.setSubmittedByName(dto.getSubmittedByName());
		resource.setSubmittedByDate(dto.getSubmittedByDate());
		resource.setRecommendedByUserid(dto.getRecommendedByUserid());
		resource.setRecommendedByName(dto.getRecommendedByName());
		resource.setRecommendedByDate(dto.getRecommendedByDate());
		resource.setApprovedByUserid(dto.getApprovedByUserid());
		resource.setApprovedByName(dto.getApprovedByName());
		resource.setApprovedByDate(dto.getApprovedByDate());
		resource.setCalculateIivInd(dto.getCalculateIivInd());
		resource.setGrowerNumber(dto.getGrowerNumber());
		resource.setGrowerName(dto.getGrowerName());
		resource.setGrowerAddressLine1(dto.getGrowerAddressLine1());
		resource.setGrowerAddressLine2(dto.getGrowerAddressLine2());
		resource.setGrowerPostalCode(dto.getGrowerPostalCode());
		resource.setGrowerCity(dto.getGrowerCity());
		resource.setGrowerProvince(dto.getGrowerProvince());
		resource.setInsurancePlanId(dto.getInsurancePlanId());
		resource.setInsurancePlanName(dto.getInsurancePlanName());
		resource.setCropYear(dto.getCropYear());
		resource.setInsuredByMeasurementType(dto.getInsuredByMeasurementType());
		resource.setPolicyNumber(dto.getPolicyNumber());
		resource.setHasChequeReqInd(dto.getHasChequeReqInd());
		resource.setClaimCalculationGrainQuantityGuid(dto.getClaimCalculationGrainQuantityGuid());

		// Set to null for now. Will be updated later from a linked calculation if one exists.
		resource.setLinkedProductId(null);
		resource.setLinkedClaimNumber(null);
		resource.setLinkedClaimCalculationGuid(null);
	}

	static void setSelfLink(String claimCalculationGuid, ClaimCalculationRsrc resource, URI baseUri) {
		if (claimCalculationGuid != null) {
			String selfUri = getClaimCalculationSelfUri(claimCalculationGuid, baseUri);
			resource.getLinks().add(new RelLink(ResourceTypes.SELF, selfUri, "GET"));
		}
	}

	public static String getClaimCalculationSelfUri(String claimCalculationGuid, URI baseUri) {
		String result = UriBuilder.fromUri(baseUri).path(ClaimCalculationEndpoint.class).build(claimCalculationGuid)
				.toString();
		return result;
	}

	private static void setSelfLink(ClaimCalculationListRsrc resource, Integer claimNumber, String policyNumber,
			Integer cropYear, String calculationStatusCode, String createClaimCalcUserGuid,
			String updateClaimCalcUserGuid, String sortColumn, String sortDirection, int pageNumber, int pageRowCount,
			URI baseUri) {

		String selfUri = getClaimCalculationListSelfUri(claimNumber, policyNumber, cropYear, calculationStatusCode,
				createClaimCalcUserGuid, updateClaimCalcUserGuid, sortColumn, sortDirection,
				Integer.valueOf(pageNumber), Integer.valueOf(pageRowCount), baseUri);

		resource.getLinks().add(new RelLink(ResourceTypes.SELF, selfUri, "GET"));
	}

	public static String getClaimCalculationListSelfUri(Integer claimNumber, String policyNumber, Integer cropYear,
			String calculationStatusCode, String createClaimCalcUserGuid, String updateClaimCalcUserGuid,
			String sortColumn, String sortDirection, Integer pageNumber, Integer pageRowCount, URI baseUri) {

		String result = UriBuilder.fromUri(baseUri).path(ClaimCalculationListEndpoint.class)
				.queryParam("claimNumber", nvl(claimNumber, "")).queryParam("policyNumber", nvl(policyNumber, ""))
				.queryParam("cropYear", nvl(cropYear, ""))
				.queryParam("calculationStatusCode", nvl(calculationStatusCode, ""))
				.queryParam("createClaimCalcUserGuid", nvl(createClaimCalcUserGuid, ""))
				.queryParam("updateClaimCalcUserGuid", nvl(updateClaimCalcUserGuid, ""))
				.queryParam("sortColumn", nvl(sortColumn, "")).queryParam("sortDirection", nvl(sortDirection, ""))
				.queryParam("pageNumber", nvl(toString(pageNumber), ""))
				.queryParam("pageRowCount", nvl(toString(pageRowCount), "")).build().toString();

		return result;
	}

	protected static String nvl(Integer value, String defaultValue) {
		return (value == null) ? defaultValue : value.toString();
	}

	private static void setLinks(ClaimCalculationListRsrc resource, Integer claimNumber, String policyNumber,
			Integer cropYear, String calculationStatusCode, String createClaimCalcUserGuid,
			String updateClaimCalcUserGuid, String sortColumn, String sortDirection, int pageNumber, int pageRowCount,
			int totalRowCount, URI baseUri, WebAdeAuthentication authentication) {

		// if (authentication.hasAuthority(Scopes.CREATE_CALCULATION)) {
		String result = UriBuilder.fromUri(baseUri).path(ClaimCalculationListEndpoint.class).build().toString();
		resource.getLinks().add(new RelLink(ResourceTypes.CREATE_CLAIM_CALCULATION, result, "POST"));
		// }

		if (pageNumber > 1) {
			String previousUri = getClaimCalculationListSelfUri(claimNumber, policyNumber, cropYear,
					calculationStatusCode, createClaimCalcUserGuid, updateClaimCalcUserGuid, sortColumn, sortDirection,
					Integer.valueOf(pageNumber - 1), Integer.valueOf(pageRowCount), baseUri);
			resource.getLinks().add(new RelLink(ResourceTypes.PREV, previousUri, "GET"));
		}

		if ((pageNumber * pageRowCount) < totalRowCount) {
			String nextUri = getClaimCalculationListSelfUri(claimNumber, policyNumber, cropYear, calculationStatusCode,
					createClaimCalcUserGuid, updateClaimCalcUserGuid, sortColumn, sortDirection,
					Integer.valueOf(pageNumber + 1), Integer.valueOf(pageRowCount), baseUri);
			resource.getLinks().add(new RelLink(ResourceTypes.NEXT, nextUri, "GET"));
		}
	}

	private static void setLinks(String claimCalculationGuid, ClaimCalculationRsrc resource, URI baseUri,
			WebAdeAuthentication authentication) {

		// if (authentication.hasAuthority(Scopes.UPDATE_CALCULATION)) {
		String result = UriBuilder.fromUri(baseUri).path(ClaimCalculationEndpoint.class).build(claimCalculationGuid)
				.toString();
		resource.getLinks().add(new RelLink(ResourceTypes.UPDATE_CLAIM_CALCULATION, result, "PUT"));
		// }

		// if (authentication.hasAuthority(Scopes.DELETE_CLAIM)) {
		result = UriBuilder.fromUri(baseUri).path(ClaimCalculationEndpoint.class).build(claimCalculationGuid)
				.toString();
		resource.getLinks().add(new RelLink(ResourceTypes.DELETE_CLAIM_CALCULATION, result, "DELETE"));
		// }
	}

}
