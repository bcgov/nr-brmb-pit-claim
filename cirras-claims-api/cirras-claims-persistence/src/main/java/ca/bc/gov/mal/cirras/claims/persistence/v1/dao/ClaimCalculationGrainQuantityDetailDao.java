package ca.bc.gov.mal.cirras.claims.persistence.v1.dao;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationGrainQuantityDetailDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface ClaimCalculationGrainQuantityDetailDao extends Serializable {
	
	ClaimCalculationGrainQuantityDetailDto fetch(String claimCalculationGrainQuantityDetailGuid) throws DaoException;
        
    void insert(ClaimCalculationGrainQuantityDetailDto dto, String userId) throws DaoException;
    
    void update(ClaimCalculationGrainQuantityDetailDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String claimCalculationGrainQuantityDetailGuid) throws DaoException, NotFoundDaoException;
    
    void deleteForClaim(String claimCalculationGuid) throws DaoException;
    
    ClaimCalculationGrainQuantityDetailDto select(String claimCalculationGuid) throws DaoException;

    List<ClaimCalculationGrainQuantityDetailDto> selectAll() throws DaoException;
}
