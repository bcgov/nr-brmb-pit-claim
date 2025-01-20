package ca.bc.gov.mal.cirras.claims.persistence.v1.dao;


import java.io.Serializable;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.CommodityCoverageCodeDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface CommodityCoverageCodeDao extends Serializable {
	
	CommodityCoverageCodeDto fetch(String commodityCoverageCode) throws DaoException;
	
    void insert(CommodityCoverageCodeDto dto, String userId) throws DaoException;
    
    void update(CommodityCoverageCodeDto dto, String userId) throws DaoException, NotFoundDaoException;

    void delete(String commodityCoverageCode) throws DaoException, NotFoundDaoException;

}
