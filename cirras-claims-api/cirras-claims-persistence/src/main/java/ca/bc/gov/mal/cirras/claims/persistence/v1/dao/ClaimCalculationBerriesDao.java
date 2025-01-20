package ca.bc.gov.mal.cirras.claims.persistence.v1.dao;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationBerriesDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface ClaimCalculationBerriesDao extends Serializable {
	
	ClaimCalculationBerriesDto fetch(String claimCalculationBerriesGuid) throws DaoException;
        
    void insert(ClaimCalculationBerriesDto dto, String userId) throws DaoException;
    
    void update(ClaimCalculationBerriesDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String claimCalculationBerriesGuid) throws DaoException, NotFoundDaoException;
    
    void deleteForClaim(String claimCalculationGuid) throws DaoException;
    
    ClaimCalculationBerriesDto select(String claimCalculationGuid) throws DaoException;

    List<ClaimCalculationBerriesDto> selectAll() throws DaoException;
}
