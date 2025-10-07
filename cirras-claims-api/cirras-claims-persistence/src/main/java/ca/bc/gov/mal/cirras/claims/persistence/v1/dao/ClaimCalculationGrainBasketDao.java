package ca.bc.gov.mal.cirras.claims.persistence.v1.dao;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationGrainBasketDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface ClaimCalculationGrainBasketDao extends Serializable {
	
	ClaimCalculationGrainBasketDto fetch(String claimCalculationGrainBasketGuid) throws DaoException;
        
    void insert(ClaimCalculationGrainBasketDto dto, String userId) throws DaoException;
    
    void update(ClaimCalculationGrainBasketDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String claimCalculationGrainBasketGuid) throws DaoException, NotFoundDaoException;
    
    void deleteForClaim(String claimCalculationGuid) throws DaoException;
    
    ClaimCalculationGrainBasketDto select(String claimCalculationGuid) throws DaoException;

    List<ClaimCalculationGrainBasketDto> selectAll() throws DaoException;
}
