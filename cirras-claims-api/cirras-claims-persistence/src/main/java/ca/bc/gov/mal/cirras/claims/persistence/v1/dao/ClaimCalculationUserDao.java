package ca.bc.gov.mal.cirras.claims.persistence.v1.dao;


import java.io.Serializable;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationUserDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface ClaimCalculationUserDao extends Serializable {
	
	ClaimCalculationUserDto fetch(String claimCalculationUserGuid) throws DaoException;

	ClaimCalculationUserDto getByLoginUserGuid(String loginUserGuid) throws DaoException;
	
    void insert(ClaimCalculationUserDto dto, String userId) throws DaoException;
    
    void update(ClaimCalculationUserDto dto, String userId) throws DaoException, NotFoundDaoException;
    
    void delete(String claimCalculationUserGuid) throws DaoException, NotFoundDaoException;        
}
