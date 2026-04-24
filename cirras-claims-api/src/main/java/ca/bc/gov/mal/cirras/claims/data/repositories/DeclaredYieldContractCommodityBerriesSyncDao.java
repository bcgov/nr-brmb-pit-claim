package ca.bc.gov.mal.cirras.claims.data.repositories;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.claims.data.repositories.mapper.DeclaredYieldContractCommodityBerriesSyncMapper;
import ca.bc.gov.mal.cirras.claims.data.entities.DeclaredYieldContractCommodityBerriesSyncDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;

@Repository
public class DeclaredYieldContractCommodityBerriesSyncDao extends BaseDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(DeclaredYieldContractCommodityBerriesSyncDao.class);

	@Autowired
	private DeclaredYieldContractCommodityBerriesSyncMapper mapper;

	
	public DeclaredYieldContractCommodityBerriesSyncDto fetch(String declaredYieldContractCommodityBerriesGuid) throws DaoException {
		logger.debug("<fetch");

		DeclaredYieldContractCommodityBerriesSyncDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("declaredYieldContractCommodityBerriesGuid", declaredYieldContractCommodityBerriesGuid);
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
	
	public void insert(DeclaredYieldContractCommodityBerriesSyncDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		String declaredYieldContractCommodityBerriesGuid = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}
			
			declaredYieldContractCommodityBerriesGuid = dto.getDeclaredYieldContractCommodityBerriesGuid();
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + declaredYieldContractCommodityBerriesGuid);
	}
	
	public void update(DeclaredYieldContractCommodityBerriesSyncDto dto, String userId) 
	throws DaoException, NotFoundDaoException {
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

		logger.debug(">update");
	}

	
	public void delete(String declaredYieldContractCommodityBerriesGuid) throws DaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("declaredYieldContractCommodityBerriesGuid", declaredYieldContractCommodityBerriesGuid);
			this.mapper.delete(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">delete");
	}
	
}
