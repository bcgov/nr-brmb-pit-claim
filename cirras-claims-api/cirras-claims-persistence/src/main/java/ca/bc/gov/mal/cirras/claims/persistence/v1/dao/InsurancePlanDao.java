package ca.bc.gov.mal.cirras.claims.persistence.v1.dao;


import java.io.Serializable;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.InsurancePlanDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface InsurancePlanDao extends Serializable {
	
	InsurancePlanDto fetch(Integer insurancePlanId) throws DaoException;
	
    void insert(InsurancePlanDto dto, String userId) throws DaoException;
    
    void update(InsurancePlanDto dto, String userId) throws DaoException, NotFoundDaoException;

    void delete(Integer insurancePlanId) throws DaoException, NotFoundDaoException;

}
