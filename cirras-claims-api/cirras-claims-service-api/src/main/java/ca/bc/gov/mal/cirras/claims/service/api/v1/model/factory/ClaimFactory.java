package ca.bc.gov.mal.cirras.claims.service.api.v1.model.factory;

import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dto.PagedDtos;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

import ca.bc.gov.mal.cirras.claims.model.v1.Claim;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimList;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimDto;

public interface ClaimFactory {

	ClaimList<? extends Claim> getClaimList(PagedDtos<ClaimDto> dtos, Integer claimNumber,
			String policyNumber, String calculationStatusCode, String sortColumn, String sortDirection,
			Integer pageRowCount, FactoryContext context, WebAdeAuthentication authentication)
			throws FactoryException, DaoException;
	
}
