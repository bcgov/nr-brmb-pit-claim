package ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.ClaimCalculationUserDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis.mapper.ClaimCalculationUserMapper;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationUserDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class ClaimCalculationUserDaoImpl extends BaseDao implements ClaimCalculationUserDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ClaimCalculationUserDaoImpl.class);

	@Autowired
	private ClaimCalculationUserMapper mapper;

	@Override
	public ClaimCalculationUserDto fetch(String claimCalculationUserGuid) throws DaoException {
		logger.debug("<fetch");

		ClaimCalculationUserDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("claimCalculationUserGuid", claimCalculationUserGuid);
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
	public ClaimCalculationUserDto getByLoginUserGuid(String loginUserGuid) throws DaoException {
		logger.debug("<getByLoginUserGuid");

		ClaimCalculationUserDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("loginUserGuid", loginUserGuid);
			result = this.mapper.getByLoginUserGuid(parameters);
			
			if(result!=null) {
				result.resetDirty();
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">getByLoginUserGuid " + result);
		return result;
	}
	
	@Override
	public void insert(ClaimCalculationUserDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		String claimCalculationUserGuid = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}
			
			claimCalculationUserGuid = (String) parameters.get("claimCalculationUserGuid");
			
			dto.setClaimCalculationUserGuid(claimCalculationUserGuid);
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + claimCalculationUserGuid);
	}
	

	@Override
	public void update(ClaimCalculationUserDto dto, String userId) 
	throws DaoException, NotFoundDaoException {
		logger.debug("<update");
		
		if(dto.isDirty()) {
			try {
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("claimCalculationUserGuid", dto.getClaimCalculationUserGuid());
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
	public void delete(String claimCalculationUserGuid) throws DaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("claimCalculationUserGuid", claimCalculationUserGuid);
			int count = this.mapper.delete(parameters);

			if(count==0) {
				throw new DaoException("Record not deleted: "+count);
			}

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">delete");
	}
}
