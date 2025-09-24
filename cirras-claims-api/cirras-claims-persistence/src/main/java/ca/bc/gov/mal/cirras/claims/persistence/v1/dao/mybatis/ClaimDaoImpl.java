package ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis.mapper.ClaimMapper;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationDto;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.TooManyRecordsException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;
import ca.bc.gov.nrs.wfone.common.persistence.dto.PagedDtos;


@Repository
public class ClaimDaoImpl extends BaseDao implements ClaimDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ClaimDaoImpl.class);

	@Autowired
	private ClaimMapper mapper;

	@Override
	public ClaimDto fetch(Integer colId) throws DaoException {
		logger.debug("<fetch");

		ClaimDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("colId", colId);
			result = this.mapper.fetch(parameters);
			
			if(result!=null) {
				result.resetDirty();
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">fetch " + result);
		return result;
	}
	
	@Override
	public ClaimDto selectByClaimNumber(Integer claimNumber) throws DaoException {
		logger.debug("<fetch");

		ClaimDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("claimNumber", claimNumber);
			result = this.mapper.selectByClaimNumber(parameters);
			
			if(result!=null) {
				result.resetDirty();
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">fetch " + result);
		return result;
	}

	@Override
	public ClaimDto selectByProductId(Integer ippId) throws DaoException {
		logger.debug("<selectByProductId");

		ClaimDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("ippId", ippId);
			result = this.mapper.selectByProductId(parameters);
			
			if(result!=null) {
				result.resetDirty();
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectByProductId " + result);
		return result;
	}

	@Override
	public List<ClaimDto> selectQuantityClaimsByPolicyId(Integer iplId) throws DaoException {
		logger.debug("<selectQuantityClaimsByPolicyId");

		List<ClaimDto> result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("iplId", iplId);
			result = this.mapper.selectQuantityClaimsByPolicyId(parameters);
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectQuantityClaimsByPolicyId " + result);
		return result;
	}
	
	@Override
	public void insert(ClaimDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		Integer ColId = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}
			
			ColId = dto.getColId();
			
			//dto.setClaimGuid(ClaimGuid);
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + ColId);
	}
	

	@Override
	public void update(ClaimDto dto, String userId) 
	throws DaoException, NotFoundDaoException {
		logger.debug("<update");
		
		if(dto.isDirty()) {
			try {
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("colId", dto.getColId());
				parameters.put("dto", dto);
				parameters.put("userId", userId);
				int count = this.mapper.update(parameters);
	
				if(count==0) {
					throw new DaoException("Record not updated: "+count);
				}
			} catch (RuntimeException e) {
				handleException(e);
			}
		} else {
			
			logger.info("Skipping update because dto is not dirty");
		}

		logger.debug(">update");
	}

	@Override
	public void updateClaimData(ClaimDto dto, String userId) throws DaoException, NotFoundDaoException {
		logger.debug("<updateClaimData");
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("colId", dto.getColId());
			parameters.put("dto", dto);
			parameters.put("userId", userId);
			this.mapper.updateClaimData(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">updateClaimData");
	}

	@Override
	public void updatePolicyData(ClaimDto dto, String userId) throws DaoException, NotFoundDaoException {
		logger.debug("<updatePolicyData");
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("iplId", dto.getIplId());
			parameters.put("dto", dto);
			parameters.put("userId", userId);
			this.mapper.updatePolicyData(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">updatePolicyData");
	}

	@Override
	public void updateGrowerData(ClaimDto dto, String userId) throws DaoException, NotFoundDaoException {
		logger.debug("<updateGrowerData");
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("igId", dto.getIgId());
			parameters.put("dto", dto);
			parameters.put("userId", userId);
			this.mapper.updateGrowerData(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">updateGrowerData");
	}

	@Override
	public void delete(Integer colId) throws DaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("colId", colId);
			int count = this.mapper.delete(parameters);

			if(count==0) {
				throw new DaoException("Record not deleted: "+count);
			}

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">delete");
	}
	
	@Override
	public PagedDtos<ClaimDto> select(
    	Integer claimNumber,
    	String policyNumber,
    	String calculationStatusCode,
		String sortColumn,
		String sortDirection,
    	int maximumRows,
    	Integer pageNumber, 
    	Integer pageRowCount
	) throws DaoException, TooManyRecordsException {
		PagedDtos<ClaimDto> results = new PagedDtos<>();

		try {
			Integer offset = null;
			pageNumber = pageNumber==null? Integer.valueOf(0) : pageNumber;
			
			if(pageRowCount != null) { 
				offset = Integer.valueOf((pageNumber.intValue()-1)*pageRowCount.intValue()); 
				offset = Math.max(offset, 0);	// make sure offset is not negative
			}
			
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			if(policyNumber != null) {
				policyNumber += "%";
			}
			
			//Get database column name and default values if none or invalid value is set
			//sets this.databaseColumn and this.isStringColumn
			String orderBy = getOrderBy(sortColumn, sortDirection);

			parameters.put("claimNumber", claimNumber);
			parameters.put("policyNumber", policyNumber);
			parameters.put("calculationStatusCode", calculationStatusCode);
			parameters.put("orderBy", orderBy);
			parameters.put("offset", offset);
			parameters.put("pageRowCount", pageRowCount);
			
			int totalRowCount = this.mapper.selectCount(parameters);
			
			boolean pageRowCountExceeds = (pageRowCount==null||pageRowCount.intValue()>maximumRows);
			if(pageRowCountExceeds&&totalRowCount>maximumRows) {
				throw new TooManyRecordsException("Exceeded maximum ("+maximumRows+") results per page.");
			}
			
			List<ClaimDto> dtos = this.mapper.select(parameters);

			results.setResults(dtos);
			results.setPageRowCount(dtos.size());
			results.setTotalRowCount(totalRowCount);
			results.setPageNumber(pageNumber == null ? 0 : pageNumber.intValue());

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">select " + results);
		return results;
	}	
	
	
	public List<ClaimDto> selectAll() throws DaoException {
		List<ClaimDto> dtos = null;

		try {
			dtos = this.mapper.selectAll();
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectAll " + dtos);
		return dtos;
	}
	
	private String getOrderBy(String modelColumnName, String sortDirection) {
		
		//Making sure the direction is a valid value
		sortDirection = getSortDirection(sortDirection);

		String databaseColumn = "C.CLAIM_NUMBER";
		boolean isStringColumn = false;

		if (modelColumnName != null) {
			switch (modelColumnName) {
			case "calculationVersion":
				databaseColumn = "CALCULATION_VERSION";
				isStringColumn = false;
				break;
			case "calculationStatusCode":
				databaseColumn = "CC.CALCULATION_STATUS_CODE";
				isStringColumn = true;
				break;
			case "claimNumber":
				databaseColumn = "C.CLAIM_NUMBER";
				isStringColumn = false;
				break;
			case "policyNumber":
				databaseColumn = "C.POLICY_NUMBER";
				isStringColumn = true;
				break;
			case "insurancePlanName":
				databaseColumn = "INSURANCE_PLAN_NAME";
				isStringColumn = true;
				break;
			case "commodityName":
				databaseColumn = "COMMODITY_NAME";
				isStringColumn = true;
				break;
			case "coverageName":
				databaseColumn = "COV.DESCRIPTION"; //COVERAGE_NAME
				isStringColumn = true;
				break;
			case "growerName":
				databaseColumn = "C.GROWER_NAME";
				isStringColumn = true;
				break;
			case "claimStatusCode":
				databaseColumn = "C.CLAIM_STATUS_CODE";
				isStringColumn = true;
				break;
			default:
				databaseColumn = "C.CLAIM_NUMBER";
				isStringColumn = false;
				break;
			}
		}
		
		String orderBy;
		
		if(isStringColumn) {
			//Need to make sure the sorting is done case insensitive
			orderBy = java.text.MessageFormat.format("ORDER BY UPPER({0}) {1}", databaseColumn, sortDirection);
		} else {
			orderBy = java.text.MessageFormat.format("ORDER BY {0} {1}", databaseColumn, sortDirection);
		}
		
		return orderBy;
	}
	
	private String getSortDirection(String sortDirection) {
		
		if (sortDirection != null) {
			String inputSortDirection = sortDirection.toUpperCase();
			switch (inputSortDirection) {
			case "ASC":
				return inputSortDirection;
			case "DESC":
				return inputSortDirection;
			default:
				return "ASC";
			}
		}
		return "ASC";
			
	}	

}
