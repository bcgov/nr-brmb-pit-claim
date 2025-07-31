package ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationGrainQuantityDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis.mapper.ClaimCalculationGrainQuantityMapper;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationGrainQuantityDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class ClaimCalculationGrainQuantityDaoImpl extends BaseDao implements ClaimCalculationGrainQuantityDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ClaimCalculationGrainQuantityDaoImpl.class);

	@Autowired
	private ClaimCalculationGrainQuantityMapper mapper;

	@Override
	public ClaimCalculationGrainQuantityDto fetch(String claimCalculationGrainQuantityGuid) throws DaoException {
		logger.debug("<fetch");

		ClaimCalculationGrainQuantityDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("claimCalculationGrainQuantityGuid", claimCalculationGrainQuantityGuid);
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
	public ClaimCalculationGrainQuantityDto select(String claimCalculationGuid) throws DaoException {
		ClaimCalculationGrainQuantityDto result = null;

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
	public void insert(ClaimCalculationGrainQuantityDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		String claimCalculationGrainQuantityGuid = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}
			
			claimCalculationGrainQuantityGuid = (String) parameters.get("claimCalculationGrainQuantityGuid");
			
			dto.setClaimCalculationGrainQuantityGuid(claimCalculationGrainQuantityGuid);
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + claimCalculationGrainQuantityGuid);
	}
	

	@Override
	public void update(ClaimCalculationGrainQuantityDto dto, String userId) 
	throws DaoException, NotFoundDaoException {
		logger.debug("<update");
		
		if(dto.isDirty()) {
			try {
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("claimCalculationGrainQuantityGuid", dto.getClaimCalculationGrainQuantityGuid());
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
	public void delete(String claimCalculationGrainQuantityGuid) throws DaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("claimCalculationGrainQuantityGuid", claimCalculationGrainQuantityGuid);
			int count = this.mapper.delete(parameters);

			if(count==0) {
				throw new DaoException("Record not deleted: "+count);
			}

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">delete");
	}

	public List<ClaimCalculationGrainQuantityDto> selectAll() throws DaoException {
		List<ClaimCalculationGrainQuantityDto> dtos = null;

		try {
			dtos = this.mapper.selectAll();
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">selectAll " + dtos);
		return dtos;
	}

}
