package ca.bc.gov.mal.cirras.claims.persistence.v1.dao;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationGrainSpotLossDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface ClaimCalculationGrainSpotLossDao extends Serializable {
	
	ClaimCalculationGrainSpotLossDto fetch(String claimCalculationGrainSpotLossGuid) throws DaoException;
        
    void insert(ClaimCalculationGrainSpotLossDto dto, String userId) throws DaoException;
    
    void update(ClaimCalculationGrainSpotLossDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String claimCalculationGrainSpotLossGuid) throws DaoException, NotFoundDaoException;
    
    void deleteForClaim(String claimCalculationGuid) throws DaoException;
    
    ClaimCalculationGrainSpotLossDto select(String claimCalculationGuid) throws DaoException;

    List<ClaimCalculationGrainSpotLossDto> selectAll() throws DaoException;
}
