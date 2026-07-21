package ca.bc.gov.mal.cirras.claims.data.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.claims.data.entities.ClaimCalculationBerriesOutboxDto;
import ca.bc.gov.mal.cirras.claims.data.repositories.mapper.ClaimCalculationBerriesOutboxMapper;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class ClaimCalculationBerriesOutboxDao extends BaseDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ClaimCalculationBerriesOutboxDao.class);

	@Autowired
	private ClaimCalculationBerriesOutboxMapper mapper;

	
	public ClaimCalculationBerriesOutboxDto fetch(Integer claimCalculationBerriesOutboxId) throws DaoException {
		logger.debug("<fetch");

		ClaimCalculationBerriesOutboxDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("claimCalculationBerriesOutboxId", claimCalculationBerriesOutboxId);
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

	
	
	public void insert(ClaimCalculationBerriesOutboxDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		Integer claimCalculationBerriesOutboxId = null;
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}

			claimCalculationBerriesOutboxId = (Integer) parameters.get("claimCalculationBerriesOutboxId");
			dto.setClaimCalculationBerriesOutboxId(claimCalculationBerriesOutboxId);
			
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + claimCalculationBerriesOutboxId);
	}
	

	
	public void update(ClaimCalculationBerriesOutboxDto dto, String userId) 
			throws DaoException, NotFoundDaoException {
		logger.debug("<update");
		
		if(dto.isDirty()) {
			try {
	
				Map<String, Object> parameters = new HashMap<String, Object>();
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

	
	public void delete(Integer claimCalculationBerriesOutboxId) throws DaoException, NotFoundDaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("claimCalculationBerriesOutboxId", claimCalculationBerriesOutboxId);
			int count = this.mapper.delete(parameters);

			if(count==0) {
				throw new DaoException("Record not deleted: "+count);
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">delete");
	}
	
	
	public List<ClaimCalculationBerriesOutboxDto> select(Integer maxRecords) throws DaoException {

		logger.debug("<select");

		List<ClaimCalculationBerriesOutboxDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("maxRecords", maxRecords);
						
			dtos = this.mapper.select(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">select " + dtos);
		return dtos;
	}
			
}
