package ca.bc.gov.mal.cirras.claims.persistence.v1.dao;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationGrainQuantityDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface ClaimCalculationGrainQuantityDao extends Serializable {
	
	ClaimCalculationGrainQuantityDto fetch(String claimCalculationGrainQuantityGuid) throws DaoException;
        
    void insert(ClaimCalculationGrainQuantityDto dto, String userId) throws DaoException;
    
    void update(ClaimCalculationGrainQuantityDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String claimCalculationGrainQuantityGuid) throws DaoException, NotFoundDaoException;
        
    ClaimCalculationGrainQuantityDto select(String claimCalculationGuid) throws DaoException;

    List<ClaimCalculationGrainQuantityDto> selectAll() throws DaoException;
}
