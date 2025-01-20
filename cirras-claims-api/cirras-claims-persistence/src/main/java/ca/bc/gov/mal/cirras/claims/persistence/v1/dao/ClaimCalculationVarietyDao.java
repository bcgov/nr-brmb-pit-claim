package ca.bc.gov.mal.cirras.claims.persistence.v1.dao;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationVarietyDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface ClaimCalculationVarietyDao extends Serializable {
	
	ClaimCalculationVarietyDto fetch(String claimCalculationVarietyGuid) throws DaoException;
        
    void insert(ClaimCalculationVarietyDto dto, String userId) throws DaoException;
    
    void update(ClaimCalculationVarietyDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String claimCalculationVarietyGuid) throws DaoException, NotFoundDaoException;
    
    void deleteForClaim(String claimCalculationGuid) throws DaoException;
    
    List<ClaimCalculationVarietyDto> select(String claimCalculationGuid) throws DaoException;

    List<ClaimCalculationVarietyDto> selectAll() throws DaoException;
}
