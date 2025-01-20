package ca.bc.gov.mal.cirras.claims.persistence.v1.dao;


import java.io.Serializable;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimStatusCodeDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface ClaimStatusCodeDao extends Serializable {
	
	ClaimStatusCodeDto fetch(String claimStatusCode) throws DaoException;
	
    void insert(ClaimStatusCodeDto dto, String userId) throws DaoException;
    
    void update(ClaimStatusCodeDto dto, String userId) throws DaoException, NotFoundDaoException;

    void delete(String claimStatusCode) throws DaoException, NotFoundDaoException;

}
