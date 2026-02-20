package ca.bc.gov.mal.cirras.claims.data.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.claims.data.repositories.CalculationStatusCodeDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.mapper.CalculationStatusCodeMapper;
import ca.bc.gov.mal.cirras.claims.data.entities.CalculationStatusCodeDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class CalculationStatusCodeDao extends BaseDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(CalculationStatusCodeDao.class);

	@Autowired
	private CalculationStatusCodeMapper mapper;
	
	public List<CalculationStatusCodeDto> fetchAll() throws DaoException {
		List<CalculationStatusCodeDto> dtos = null;

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
}
