package ca.bc.gov.mal.cirras.claims.data.assemblers;

import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.mal.cirras.claims.data.resources.ClaimCalculationSimpleRsrc;
import ca.bc.gov.mal.cirras.claims.data.models.ClaimCalculationBerries;
import ca.bc.gov.mal.cirras.claims.data.entities.ClaimCalculationBerriesDto;

public class ClaimCalculationSimpleRsrcFactory extends BaseResourceFactory {

	public ClaimCalculationSimpleRsrc getClaimCalculationSimple(ClaimCalculationBerriesDto dto) throws FactoryException {

		ClaimCalculationSimpleRsrc resource = new ClaimCalculationSimpleRsrc();

		resource.setClaimCalculationGuid(dto.getClaimCalculationGuid());
		resource.setCalculationVersion(dto.getCalculationVersion());
		resource.setContractId(dto.getContractId());
		resource.setCropCommodityId(dto.getCropCommodityId());
		resource.setCropYear(dto.getCropYear());
		resource.setCalculationStatusCode(dto.getCalculationStatusCode());

		resource.setClaimCalculationBerries(createClaimCalculationBerries(dto));

		String eTag = getEtag(resource);
		resource.setETag(eTag);

		return resource;
	}

	private ClaimCalculationBerries createClaimCalculationBerries(ClaimCalculationBerriesDto dto) {
		ClaimCalculationBerries model = new ClaimCalculationBerries();

		model.setClaimCalculationBerriesGuid(dto.getClaimCalculationBerriesGuid());
		model.setTotalYieldForCalculation(dto.getTotalYieldForCalculation());

		return model;
	}
	
}