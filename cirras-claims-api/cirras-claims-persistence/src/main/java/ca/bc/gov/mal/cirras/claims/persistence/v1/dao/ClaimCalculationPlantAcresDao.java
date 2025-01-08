package ca.bc.gov.mal.cirras.claims.persistence.v1.dao;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationPlantAcresDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface ClaimCalculationPlantAcresDao extends Serializable {
	
	ClaimCalculationPlantAcresDto fetch(String claimCalcPlantAcresGuid) throws DaoException;
        
    void insert(ClaimCalculationPlantAcresDto dto, String userId) throws DaoException;
    
    void update(ClaimCalculationPlantAcresDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String claimCalcPlantAcresGuid) throws DaoException, NotFoundDaoException;
    
    void deleteForClaim(String claimCalculationGuid) throws DaoException;
    
    ClaimCalculationPlantAcresDto select(String claimCalculationGuid) throws DaoException;

    List<ClaimCalculationPlantAcresDto> selectAll() throws DaoException;
}
