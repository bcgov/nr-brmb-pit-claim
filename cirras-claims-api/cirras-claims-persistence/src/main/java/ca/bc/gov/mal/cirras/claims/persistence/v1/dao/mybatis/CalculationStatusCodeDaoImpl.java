package ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.CalculationStatusCodeDao;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis.mapper.CalculationStatusCodeMapper;
import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.CalculationStatusCodeDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
//import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.mybatis.BaseDao;


@Repository
public class CalculationStatusCodeDaoImpl extends BaseDao implements CalculationStatusCodeDao {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(CalculationStatusCodeDaoImpl.class);

	@Autowired
	private CalculationStatusCodeMapper mapper;
	
	@Override
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
