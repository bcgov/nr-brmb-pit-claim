package ca.bc.gov.mal.cirras.claims.persistence.v1.dao;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationGrainBasketProductDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface ClaimCalculationGrainBasketProductDao extends Serializable {
	
	ClaimCalculationGrainBasketProductDto fetch(String claimCalcGrainBasketProductGuid) throws DaoException;
        
    void insert(ClaimCalculationGrainBasketProductDto dto, String userId) throws DaoException;
    
    void update(ClaimCalculationGrainBasketProductDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String claimCalcGrainBasketProductGuid) throws DaoException, NotFoundDaoException;
    
    void deleteForClaim(String claimCalculationGuid) throws DaoException;
    
    List<ClaimCalculationGrainBasketProductDto> select(String claimCalculationGuid) throws DaoException;

    List<ClaimCalculationGrainBasketProductDto> selectAll() throws DaoException;
}
