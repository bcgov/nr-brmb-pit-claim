package ca.bc.gov.mal.cirras.claims.persistence.v1.dao;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationGrapesDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface ClaimCalculationGrapesDao extends Serializable {
	
	ClaimCalculationGrapesDto fetch(String claimCalculationGrapesGuid) throws DaoException;
        
    void insert(ClaimCalculationGrapesDto dto, String userId) throws DaoException;
    
    void update(ClaimCalculationGrapesDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String claimCalculationGrapesGuid) throws DaoException, NotFoundDaoException;
    
    void deleteForClaim(String claimCalculationGuid) throws DaoException;
    
    ClaimCalculationGrapesDto select(String claimCalculationGuid) throws DaoException;

    List<ClaimCalculationGrapesDto> selectAll() throws DaoException;
}
