package ca.bc.gov.mal.cirras.claims.persistence.v1.dao;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.TooManyRecordsException;
import ca.bc.gov.nrs.wfone.common.persistence.dto.PagedDtos;


public interface ClaimDao extends Serializable {
	
	ClaimDto fetch(Integer colId) throws DaoException;
	
	ClaimDto selectByClaimNumber(Integer claimNumber) throws DaoException;
        
    void insert(ClaimDto dto, String userId) throws DaoException;
    
    void update(ClaimDto dto, String userId) throws DaoException, NotFoundDaoException;

    void updateClaimData(ClaimDto dto, String userId) throws DaoException, NotFoundDaoException;

    void updatePolicyData(ClaimDto dto, String userId) throws DaoException, NotFoundDaoException;

    void updateGrowerData(ClaimDto dto, String userId) throws DaoException, NotFoundDaoException;

    void delete(Integer colId) throws DaoException, NotFoundDaoException;
    
    List<ClaimDto> selectAll() throws DaoException;

	PagedDtos<ClaimDto> select(Integer claimNumber, String policyNumber, String calculationStatusCode,
			String sortColumn, String sortDirection, int maximumRows, Integer pageNumber, Integer pageRowCount)
			throws DaoException, TooManyRecordsException;
}
