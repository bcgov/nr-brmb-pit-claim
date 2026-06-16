package ca.bc.gov.mal.cirras.claims.data.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimCalculationGrainQuantityDetailDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.mapper.ClaimCalculationGrainQuantityDetailMapper;
import ca.bc.gov.mal.cirras.claims.data.entities.ClaimCalculationGrainQuantityDetailDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class ClaimCalculationGrainQuantityDetailDao extends BaseDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ClaimCalculationGrainQuantityDetailDao.class);

	@Autowired
	private ClaimCalculationGrainQuantityDetailMapper mapper;

	
	public ClaimCalculationGrainQuantityDetailDto fetch(String claimCalculationGrainQuantityDetailGuid) throws DaoException {
		logger.debug("<fetch");

		ClaimCalculationGrainQuantityDetailDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("claimCalculationGrainQuantityDetailGuid", claimCalculationGrainQuantityDetailGuid);
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

	
	public ClaimCalculationGrainQuantityDetailDto select(String claimCalculationGuid) throws DaoException {
		ClaimCalculationGrainQuantityDetailDto result = null;

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
	
	
	public void insert(ClaimCalculationGrainQuantityDetailDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		String claimCalculationGrainQuantityDetailGuid = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}
			
			claimCalculationGrainQuantityDetailGuid = (String) parameters.get("claimCalculationGrainQuantityDetailGuid");
			
			dto.setClaimCalculationGrainQuantityDetailGuid(claimCalculationGrainQuantityDetailGuid);
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + claimCalculationGrainQuantityDetailGuid);
	}
	

	
	public void update(ClaimCalculationGrainQuantityDetailDto dto, String userId) 
	throws DaoException, NotFoundDaoException {
		logger.debug("<update");
		
		if(dto.isDirty()) {
			try {
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("claimCalculationGrainQuantityDetailGuid", dto.getClaimCalculationGrainQuantityDetailGuid());
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

	
	public void delete(String claimCalculationGrainQuantityDetailGuid) throws DaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("claimCalculationGrainQuantityDetailGuid", claimCalculationGrainQuantityDetailGuid);
			int count = this.mapper.delete(parameters);

			if(count==0) {
				throw new DaoException("Record not deleted: "+count);
			}

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">delete");
	}
	
	
	
	public void deleteForClaim(String claimCalculationGuid) throws DaoException {
		logger.debug("<deleteForClaim");

		try {

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("claimCalculationGuid", claimCalculationGuid);
			this.mapper.deleteForClaim(parameters);
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">deleteForClaim");
	}
	
	
	public List<ClaimCalculationGrainQuantityDetailDto> selectAll() throws DaoException {
		List<ClaimCalculationGrainQuantityDetailDto> dtos = null;

		try {
			dtos = this.mapper.selectAll();
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectAll " + dtos);
		return dtos;
	}

}
