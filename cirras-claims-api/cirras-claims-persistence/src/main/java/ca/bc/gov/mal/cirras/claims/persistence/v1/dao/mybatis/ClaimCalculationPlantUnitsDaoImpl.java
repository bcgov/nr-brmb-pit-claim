package ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationPlantUnitsDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis.mapper.ClaimCalculationPlantUnitsMapper;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationPlantUnitsDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class ClaimCalculationPlantUnitsDaoImpl extends BaseDao implements ClaimCalculationPlantUnitsDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ClaimCalculationPlantUnitsDaoImpl.class);

	@Autowired
	private ClaimCalculationPlantUnitsMapper mapper;

	@Override
	public ClaimCalculationPlantUnitsDto fetch(String claimCalcPlantUnitsGuid) throws DaoException {
		logger.debug("<fetch");

		ClaimCalculationPlantUnitsDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("claimCalcPlantUnitsGuid", claimCalcPlantUnitsGuid);
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
	public ClaimCalculationPlantUnitsDto select(String claimCalculationGuid) throws DaoException {
		ClaimCalculationPlantUnitsDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("claimCalculationGuid", claimCalculationGuid);
			result = this.mapper.select(parameters);
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">select " + result);
		return result;
	}
	
	@Override
	public void insert(ClaimCalculationPlantUnitsDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		String claimCalcPlantUnitsGuid = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}
			
			claimCalcPlantUnitsGuid = (String) parameters.get("claimCalcPlantUnitsGuid");
			
			dto.setClaimCalcPlantUnitsGuid(claimCalcPlantUnitsGuid);
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + claimCalcPlantUnitsGuid);
	}
	

	@Override
	public void update(ClaimCalculationPlantUnitsDto dto, String userId) 
	throws DaoException, NotFoundDaoException {
		logger.debug("<update");
		
		if(dto.isDirty()) {
			try {
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("claimCalcPlantUnitsGuid", dto.getClaimCalcPlantUnitsGuid());
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
	public void delete(String claimCalcPlantUnitsGuid) throws DaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("claimCalcPlantUnitsGuid", claimCalcPlantUnitsGuid);
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
	public void deleteForClaim(String claimCalculationGuid) throws DaoException {
		logger.debug("<deleteForClaim");

		try {

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("claimCalculationGuid", claimCalculationGuid);
			int count = this.mapper.deleteForClaim(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">deleteForClaim");
	}
	
	public List<ClaimCalculationPlantUnitsDto> selectAll() throws DaoException {
		List<ClaimCalculationPlantUnitsDto> dtos = null;

		try {
			dtos = this.mapper.selectAll();
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectAll " + dtos);
		return dtos;
	}

}
