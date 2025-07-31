package ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.CropCommodityDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis.mapper.CropCommodityMapper;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.CropCommodityDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;

@Repository
public class CropCommodityDaoImpl extends BaseDao implements CropCommodityDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(CropCommodityDaoImpl.class);

	@Autowired
	private CropCommodityMapper mapper;

	@Override
	public CropCommodityDto fetch(Integer cropCommodityId) throws DaoException {
		logger.debug("<fetch");

		CropCommodityDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("cropCommodityId", cropCommodityId);
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
	public CropCommodityDto getLinkedCommodityByPedigree(Integer cropCommodityId) throws DaoException {
		logger.debug("<getLinkedCommodityByPedigree");

		CropCommodityDto result = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("cropCommodityId", cropCommodityId);
			result = this.mapper.getLinkedCommodityByPedigree(parameters);
			
			if(result!=null) {
				result.resetDirty();
			}
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">getLinkedCommodityByPedigree " + result);
		return result;
	}
	
	
	@Override
	public void insert(CropCommodityDto dto, String userId) throws DaoException {
		logger.debug("<insert");

		Integer CropCommodityId = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("dto", dto);
			parameters.put("userId", userId);
			int count = this.mapper.insert(parameters);

			if(count==0) {
				throw new DaoException("Record not inserted: "+count);
			}
			
			CropCommodityId = dto.getCropCommodityId();
			
		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">insert " + CropCommodityId);
	}
	

	@Override
	public void update(CropCommodityDto dto, String userId) 
	throws DaoException, NotFoundDaoException {
		logger.debug("<update");
		
		if(dto.isDirty()) {
			try {
				Map<String, Object> parameters = new HashMap<String, Object>();
				//parameters.put("cropCommodityId", dto.getCropCommodityId());
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
	public void delete(Integer cropCommodityId) throws DaoException {
		logger.debug("<delete");

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("cropCommodityId", cropCommodityId);
			this.mapper.delete(parameters);

		} catch (RuntimeException e) {
			handleException(e);
		}

		logger.debug(">delete");
	}
	
}
