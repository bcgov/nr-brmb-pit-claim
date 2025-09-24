package ca.bc.gov.mal.cirras.claims.persistence.v1.dao;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.TooManyRecordsException;
import ca.bc.gov.nrs.wfone.common.persistence.dto.PagedDtos;


public interface ClaimCalculationDao extends Serializable {
	
	ClaimCalculationDto fetch(
		String claimCalculationGuid
	) throws DaoException;
        
    void insert(
    		ClaimCalculationDto dto, 
        String userId
    ) throws DaoException;
    
    void update(
        String claimCalculationGuid, 
        ClaimCalculationDto dto, 
        String userId
    ) throws DaoException, NotFoundDaoException;
    
    void delete(
        String insuranceClaimGuid
   ) throws DaoException, NotFoundDaoException;
    
   PagedDtos<ClaimCalculationDto> select(
		Integer claimNumber,
    	String policyNumber,
    	Integer cropYear,
    	String calculationStatusCode,
    	String createClaimCalcUserGuid,
    	String updateClaimCalcUserGuid,
    	Integer insurancePlanId,
		String sortColumn,
		String sortDirection,
    	int maximumRows,
    	Integer pageNumber, 
    	Integer pageRowCount
    ) throws DaoException, TooManyRecordsException;
   
   
   List<ClaimCalculationDto> getCalculationsByClaimNumber(
		Integer claimNumber,
    	String calculationStatusCode
   ) throws DaoException;
   
   ClaimCalculationDto getLatestVersionOfCalculation(Integer claimNumber) throws DaoException;
   
   List<ClaimCalculationDto> getCalculationsByGrainQuantityGuid(
		String claimCalculationGrainQuantityGuid
   ) throws DaoException;

   ClaimCalculationDto getByClaimNumberAndVersion(Integer claimNumber, Integer calculationVersion) throws DaoException;   

}
