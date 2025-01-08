package ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.CropVarietyDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis.mapper.CropVarietyMapper;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.CropVarietyDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;

@Repository
public class CropVarietyDaoImpl extends BaseDao implements CropVarietyDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(CropVarietyDaoImpl.class);

	@Autowired
	private CropVarietyMapper mapper;

	@Override
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
	
	@Override
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
	

	@Override
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

	@Override
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
