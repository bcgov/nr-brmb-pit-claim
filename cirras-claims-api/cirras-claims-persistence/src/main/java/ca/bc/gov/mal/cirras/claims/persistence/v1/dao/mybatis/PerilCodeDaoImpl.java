package ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.PerilCodeDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis.mapper.PerilCodeMapper;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.PerilCodeDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class PerilCodeDaoImpl extends BaseDao implements PerilCodeDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(PerilCodeDaoImpl.class);

	@Autowired
	private PerilCodeMapper mapper;
	
	@Override
	public List<PerilCodeDto> fetchAll() throws DaoException {
		List<PerilCodeDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			//parameters.put("insuranceClaimGuid", insuranceClaimGuid);
			dtos = this.mapper.fetchAll(parameters);
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">select " + dtos);
		return dtos;
	}


	@Override
	public List<PerilCodeDto> selectForCoverage(
			String commodityCoverageCode, 
			int cropCommodityId
	) throws DaoException {
	
		List<PerilCodeDto> dtos = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("commodityCoverageCode", commodityCoverageCode);
			parameters.put("cropCommodityId", cropCommodityId);
			dtos = this.mapper.selectForCoverage(parameters);
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">select " + dtos);
		return dtos;
	}


	@Override
	public PerilCodeDto fetch(String perilCode) throws DaoException {
		logger.debug("<fetch");

		PerilCodeDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("perilCode", perilCode);
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
	public void insert(PerilCodeDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		String perilCode = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}
			
			perilCode = dto.getPerilCode();
			
			//dto.setClaimGuid(ClaimGuid);
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + perilCode);
	}


	@Override
	public void update(PerilCodeDto dto, String userId) throws DaoException, NotFoundDaoException {
		logger.debug("<update");
		
		if(dto.isDirty()) {
			try {
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("dto", dto);
				parameters.put("userId", userId);
				this.mapper.update(parameters);

			} catch (RuntimeException e) {
				handleException(e);
			}
		} else {
			
			logger.info("Skipping update because dto is not dirty");
		}

		logger.debug(">update");	}


	@Override
	public void delete(String perilCode) throws DaoException, NotFoundDaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("perilCode", perilCode);
			this.mapper.delete(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">delete");
	}

}
