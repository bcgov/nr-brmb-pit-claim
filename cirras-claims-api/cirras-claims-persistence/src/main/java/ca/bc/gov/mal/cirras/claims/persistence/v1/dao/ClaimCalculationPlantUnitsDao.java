package ca.bc.gov.mal.cirras.claims.persistence.v1.dao;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationPlantUnitsDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface ClaimCalculationPlantUnitsDao extends Serializable {
	
	ClaimCalculationPlantUnitsDto fetch(String claimCalcPlantUnitsGuid) throws DaoException;
        
    void insert(ClaimCalculationPlantUnitsDto dto, String userId) throws DaoException;
    
    void update(ClaimCalculationPlantUnitsDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String claimCalcPlantUnitsGuid) throws DaoException, NotFoundDaoException;
    
    void deleteForClaim(String claimCalculationGuid) throws DaoException;
    
    ClaimCalculationPlantUnitsDto select(String claimCalculationGuid) throws DaoException;

    List<ClaimCalculationPlantUnitsDto> selectAll() throws DaoException;
}
