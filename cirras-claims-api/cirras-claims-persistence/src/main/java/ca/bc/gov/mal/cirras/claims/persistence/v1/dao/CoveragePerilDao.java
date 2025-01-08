package ca.bc.gov.mal.cirras.claims.persistence.v1.dao;


import java.io.Serializable;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.CoveragePerilDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface CoveragePerilDao extends Serializable {

	CoveragePerilDto fetch(Integer coveragePerilId) throws DaoException;
	
    void insert(CoveragePerilDto dto, String userId) throws DaoException;
    
    void update(CoveragePerilDto dto, String userId) throws DaoException, NotFoundDaoException;

    void delete(Integer coveragePerilId) throws DaoException, NotFoundDaoException;

}
