package ca.bc.gov.mal.cirras.claims.persistence.v1.dao;


import java.io.Serializable;
import java.util.List;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.CalculationStatusCodeDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;

public interface CalculationStatusCodeDao extends Serializable {
	
	List<CalculationStatusCodeDto> fetchAll() throws DaoException;
        
}
