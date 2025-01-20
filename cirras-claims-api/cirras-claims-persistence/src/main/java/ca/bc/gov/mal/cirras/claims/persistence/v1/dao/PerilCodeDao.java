package ca.bc.gov.mal.cirras.claims.persistence.v1.dao;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.PerilCodeDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface PerilCodeDao extends Serializable {
	
	List<PerilCodeDto> fetchAll() throws DaoException;
	
	List<PerilCodeDto> selectForCoverage(String commodityCoverageCode, int cropCommodityId) throws DaoException;	

	PerilCodeDto fetch(String perilCode) throws DaoException;
	
    void insert(PerilCodeDto dto, String userId) throws DaoException;
    
    void update(PerilCodeDto dto, String userId) throws DaoException, NotFoundDaoException;

    void delete(String perilCode) throws DaoException, NotFoundDaoException;

}
