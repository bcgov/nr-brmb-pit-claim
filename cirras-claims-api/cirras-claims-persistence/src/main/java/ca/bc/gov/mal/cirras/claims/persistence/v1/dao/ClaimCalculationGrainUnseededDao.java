package ca.bc.gov.mal.cirras.claims.persistence.v1.dao;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationGrainUnseededDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface ClaimCalculationGrainUnseededDao extends Serializable {
	
	ClaimCalculationGrainUnseededDto fetch(String claimCalculationGrainUnseededGuid) throws DaoException;
        
    void insert(ClaimCalculationGrainUnseededDto dto, String userId) throws DaoException;
    
    void update(ClaimCalculationGrainUnseededDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String claimCalculationGrainUnseededGuid) throws DaoException, NotFoundDaoException;
    
    void deleteForClaim(String claimCalculationGuid) throws DaoException;
    
    ClaimCalculationGrainUnseededDto select(String claimCalculationGuid) throws DaoException;

    List<ClaimCalculationGrainUnseededDto> selectAll() throws DaoException;
}
