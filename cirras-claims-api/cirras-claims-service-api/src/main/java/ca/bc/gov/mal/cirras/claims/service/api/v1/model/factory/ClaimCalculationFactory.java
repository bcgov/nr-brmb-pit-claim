package ca.bc.gov.mal.cirras.claims.service.api.v1.model.factory;

import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dto.PagedDtos;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculation;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationBerries;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationGrainUnseeded;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationGrapes;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationList;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationPlantAcres;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationPlantUnits;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationVariety;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationBerriesDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationGrainUnseededDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationGrapesDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationPlantAcresDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationPlantUnitsDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationVarietyDto;

public interface ClaimCalculationFactory {

	ClaimCalculation getClaimCalculation(
		ClaimCalculationDto dto, 
		FactoryContext context, 
		WebAdeAuthentication authentication
	) throws FactoryException;
	
	
	ClaimCalculationList<? extends ClaimCalculation> getClaimCalculationList(
		PagedDtos<ClaimCalculationDto> dtos,
		Integer claimNumber,
		String policyNumber,
		Integer cropYear,
		String calculationStatusCode,
		String createClaimCalcUserGuid,
		String updateClaimCalcUserGuid,
		Integer insurancePlanId,
		String sortColumn,
		String sortDirection,
		Integer pageRowCount,
		FactoryContext context, 
		WebAdeAuthentication authentication
	) 
	throws FactoryException, DaoException;
	
	void updateDto(ClaimCalculationDto dto, ClaimCalculation model);
	void updateDto(ClaimCalculationVarietyDto dto, ClaimCalculationVariety model);
	void updateDto(ClaimCalculationBerriesDto dto, ClaimCalculationBerries model);
	void updateDto(ClaimCalculationPlantUnitsDto dto, ClaimCalculationPlantUnits model);
	void updateDto(ClaimCalculationPlantAcresDto dto, ClaimCalculationPlantAcres model);
	void updateDto(ClaimCalculationGrapesDto dto, ClaimCalculationGrapes model);
	void updateDto(ClaimCalculationGrainUnseededDto dto, ClaimCalculationGrainUnseeded model);
	
	ClaimCalculationDto createDto(ClaimCalculation model);
	ClaimCalculationVarietyDto createDto(ClaimCalculationVariety model);
	ClaimCalculationBerriesDto createDto(ClaimCalculationBerries model);
	ClaimCalculationPlantUnitsDto createDto(ClaimCalculationPlantUnits model);
	ClaimCalculationPlantAcresDto createDto(ClaimCalculationPlantAcres model);
	ClaimCalculationGrapesDto createDto(ClaimCalculationGrapes model);
	ClaimCalculationGrainUnseededDto createDto(ClaimCalculationGrainUnseeded model);
	
	public ClaimCalculation getCalculationFromClaim(
			ca.bc.gov.mal.cirras.policies.model.v1.InsuranceClaim claim,
			FactoryContext context, 
			WebAdeAuthentication authentication
		) throws FactoryException;

	public void updateCalculationFromClaim(ClaimCalculation claimCalculation, ca.bc.gov.mal.cirras.policies.model.v1.InsuranceClaim claim);

	public ClaimCalculation getCalculationFromCalculation(
			ClaimCalculation claimCalculation,
			FactoryContext context, 
			WebAdeAuthentication authentication
		) throws FactoryException;


}
