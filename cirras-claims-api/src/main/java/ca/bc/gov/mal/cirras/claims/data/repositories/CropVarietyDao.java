package ca.bc.gov.mal.cirras.claims.data.repositories;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.claims.data.repositories.CropVarietyDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.mapper.CropVarietyMapper;
import ca.bc.gov.mal.cirras.claims.data.entities.CropVarietyDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;

@Repository
public class CropVarietyDao extends BaseDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(CropVarietyDao.class);

	@Autowired
	private CropVarietyMapper mapper;

	
	public CropVarietyDto fetch(Integer cropVarietyId) throws DaoException {
		logger.debug("<fetch");

		CropVarietyDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("cropVarietyId", cropVarietyId);
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
	
	
	public void insert(CropVarietyDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		Integer CropVarietyId = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}
			
			CropVarietyId = dto.getCropVarietyId();
			
			//dto.setClaimGuid(ClaimGuid);
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + CropVarietyId);
	}
	

	
	public void update(CropVarietyDto dto, String userId) 
	throws DaoException, NotFoundDaoException {
		logger.debug("<update");
		
		if(dto.isDirty()) {
			try {
				Map<String, Object> parameters = new HashMap<String, Object>();
				//parameters.put("cropVarietyId", dto.getCropVarietyId());
				parameters.put("dto", dto);
				parameters.put("userId", userId);
				this.mapper.update(parameters);

			} catch (RuntimeException e) {
				handleException(e);
			}
		} else {
			
			logger.info("Skipping update because dto is not dirty");
		}

		logger.debug(">update");
	}

	
	public void delete(Integer cropVarietyId) throws DaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("cropVarietyId", cropVarietyId);
			this.mapper.delete(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">delete");
	}
	
}
