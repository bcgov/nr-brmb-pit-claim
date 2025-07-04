package ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis.mapper.ClaimCalculationMapper;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.TooManyRecordsException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;
import ca.bc.gov.nrs.wfone.common.persistence.dto.PagedDtos;


@Repository
public class ClaimCalculationDaoImpl extends BaseDao implements ClaimCalculationDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ClaimCalculationDaoImpl.class);

	@Autowired
	private ClaimCalculationMapper mapper;

	@Override
	public ClaimCalculationDto fetch(String claimCalculationGuid) throws DaoException {
		logger.debug("<fetch");

		ClaimCalculationDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("claimCalculationGuid", claimCalculationGuid);
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
	public void insert(ClaimCalculationDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		String claimCalculationGuid = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}
			
			claimCalculationGuid = (String) parameters.get("claimCalculationGuid");
			
			dto.setClaimCalculationGuid(claimCalculationGuid);
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + claimCalculationGuid);
	}
	

	@Override
	public void update(String claimCalculationGuid, ClaimCalculationDto dto, String userId) 
			throws DaoException, NotFoundDaoException {
		logger.debug("<update");
		
		if(dto.isDirty()) {
			try {
	
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("claimCalculationGuid", claimCalculationGuid);
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
	public void delete(String claimCalculationGuid) throws DaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("claimCalculationGuid", claimCalculationGuid);
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
	public List<ClaimCalculationDto> getCalculationsByClaimNumber(
			Integer claimNumber,
	    	String calculationStatusCode
			   ) throws DaoException {
		
		logger.debug("<getCalculationsByClaimNumber");
		
		List<ClaimCalculationDto> dtos = null;

		try {
			
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("claimNumber", claimNumber);
			parameters.put("calculationStatusCode", calculationStatusCode);
			
			dtos = this.mapper.getByClaimNumber(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">getCalculationsByClaimNumber " + dtos);
		return dtos;		
	}

	@Override
	public ClaimCalculationDto getLatestVersionOfCalculation(Integer claimNumber) throws DaoException {
		logger.debug("<fetch");

		List<ClaimCalculationDto> dtos = null;
		ClaimCalculationDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("claimNumber", claimNumber);
			dtos = this.mapper.getCalculationsForDataSync(parameters);
			
			if(dtos != null && dtos.size() > 0) {
				result = dtos.get(0);
			}
			
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
	public List<ClaimCalculationDto> getCalculationsByGrainQuantityGuid(String claimCalculationGrainQuantityGuid) throws DaoException {
		
		logger.debug("<getCalculationsByGrainQuantityGuid");
		
		List<ClaimCalculationDto> dtos = null;

		try {
			
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("claimCalculationGrainQuantityGuid", claimCalculationGrainQuantityGuid);
			
			dtos = this.mapper.getCalculationsByGrainQuantityGuid(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">getCalculationsByGrainQuantityGuid " + dtos);
		return dtos;		
	}	

	@Override
	public ClaimCalculationDto getByClaimNumberAndVersion(Integer claimNumber, Integer calculationVersion) throws DaoException {
		logger.debug("<getByClaimNumberAndVersion");

		ClaimCalculationDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("claimNumber", claimNumber);
			parameters.put("calculationVersion", calculationVersion);

			result = this.mapper.getByClaimNumberAndVersion(parameters);
			
			if(result!=null) {
				result.resetDirty();
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">getByClaimNumberAndVersion " + result);
		return result;
	}
	
	
	@Override
	public PagedDtos<ClaimCalculationDto> select(
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
	) throws DaoException, TooManyRecordsException {
		PagedDtos<ClaimCalculationDto> results = new PagedDtos<>();

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
			parameters.put("cropYear", cropYear);
			parameters.put("calculationStatusCode", calculationStatusCode);
			parameters.put("createClaimCalcUserGuid", createClaimCalcUserGuid);
			parameters.put("updateClaimCalcUserGuid", updateClaimCalcUserGuid);
			parameters.put("insurancePlanId", insurancePlanId);
			parameters.put("orderBy", orderBy);
			parameters.put("offset", offset);
			parameters.put("pageRowCount", pageRowCount);
			
			int totalRowCount = this.mapper.selectCount(parameters);
			
			boolean pageRowCountExceeds = (pageRowCount==null||pageRowCount.intValue()>maximumRows);
			if(pageRowCountExceeds&&totalRowCount>maximumRows) {
				throw new TooManyRecordsException("Exceeded maximum ("+maximumRows+") results per page.");
			}
			
			List<ClaimCalculationDto> dtos = this.mapper.select(parameters);

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
	
	private String getOrderBy(String modelColumnName, String sortDirection) {
	
		//Making sure the direction is a valid value
		sortDirection = getSortDirection(sortDirection);

		String databaseColumn = "CLAIM_NUMBER";
		boolean isStringColumn = false;

		if (modelColumnName != null) {
			switch (modelColumnName) {
			case "calculationVersion":
				databaseColumn = "CALCULATION_VERSION";
				isStringColumn = false;
				break;
			case "calculationStatusCode":
				databaseColumn = "CALCULATION_STATUS_CODE";
				isStringColumn = true;
				break;
			case "claimNumber":
				databaseColumn = "CLAIM_NUMBER";
				isStringColumn = false;
				break;
			case "claimStatusCode":
				databaseColumn = "CLAIM_STATUS_CODE";
				isStringColumn = true;
				break;
			case "policyNumber":
				databaseColumn = "POLICY_NUMBER";
				isStringColumn = true;
				break;
			case "growerName":
				databaseColumn = "GROWER_NAME";
				isStringColumn = true;
				break;
			case "insurancePlanName":
				databaseColumn = "INSURANCE_PLAN_NAME";
				isStringColumn = true;
				break;
			case "coverageName":
				databaseColumn = "COVERAGE_NAME";
				isStringColumn = true;
				break;
			case "commodityName":
				databaseColumn = "COMMODITY_NAME";
				isStringColumn = true;
				break;
			case "totalClaimAmount":
				databaseColumn = "TOTAL_CLAIM_AMOUNT";
				isStringColumn = false;
				break;
			case "createClaimCalcUserName":
				databaseColumn = "CREATE_CLAIM_CALC_USER_NAME";
				isStringColumn = true;
				break;
			case "createUser":
				databaseColumn = "T.CREATE_USER";
				isStringColumn = true;
				break;
			case "updateClaimCalcUserName":
				databaseColumn = "UPDATE_CLAIM_CALC_USER_NAME";
				isStringColumn = true;
				break;			
			case "updateUser":
				databaseColumn = "T.UPDATE_USER";
				isStringColumn = true;
				break;
			case "createDate":
				databaseColumn = "T.CREATE_DATE";
				isStringColumn = false;
				break;
			case "updateDate":
				databaseColumn = "T.UPDATE_DATE";
				isStringColumn = false;
				break;
			default:
				databaseColumn = "CLAIM_NUMBER";
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
