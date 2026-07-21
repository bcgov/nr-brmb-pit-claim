package ca.bc.gov.mal.cirras.claims.data.assemblers;

import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.mal.cirras.claims.data.entities.ClaimCalculationBerriesOutboxDto;
import ca.bc.gov.mal.cirras.claims.data.models.ClaimCalculationBerriesOutbox;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;

public class OutboxFactory extends BaseResourceFactory { 
	
	
	//======================================================================================================================
	// Claim Calculation Berries Outbox
	//======================================================================================================================

	public List<ClaimCalculationBerriesOutbox> getClaimCalculationBerriesOutboxList(List<ClaimCalculationBerriesOutboxDto> dtos)
			throws FactoryException {

		List<ClaimCalculationBerriesOutbox> claimCalculationBerriesOutbox = null;
		
		if ( dtos != null && !dtos.isEmpty() ) {

			claimCalculationBerriesOutbox = new ArrayList<ClaimCalculationBerriesOutbox>();
			
			for ( ClaimCalculationBerriesOutboxDto dto : dtos ) { 
				ClaimCalculationBerriesOutbox model = new ClaimCalculationBerriesOutbox();
				populateModel(model, dto);
				claimCalculationBerriesOutbox.add(model);
			}
			
		}
		
		return claimCalculationBerriesOutbox;
	}

	
	private void populateModel(ClaimCalculationBerriesOutbox model, ClaimCalculationBerriesOutboxDto dto) {
		
		model.setClaimCalculationBerriesOutboxId(dto.getClaimCalculationBerriesOutboxId());
		model.setClaimCalculationBerriesGuid(dto.getClaimCalculationBerriesGuid());
		model.setCreateDate(dto.getCreateDate());
		model.setTransactionType(dto.getAuditTransactionTypeCode());

	}

}
