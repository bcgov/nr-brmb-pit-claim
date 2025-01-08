package ca.bc.gov.mal.cirras.claims.persistence.v1.dao;


import java.io.Serializable;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.CropVarietyDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;


public interface CropVarietyDao extends Serializable {
	
	CropVarietyDto fetch(Integer cropVarietyId) throws DaoException;
	
    void insert(CropVarietyDto dto, String userId) throws DaoException;
    
    void update(CropVarietyDto dto, String userId) throws DaoException, NotFoundDaoException;

    void delete(Integer cropVarietyId) throws DaoException, NotFoundDaoException;
    
}
