package ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.CalculationStatusCodeDto;

public interface CalculationStatusCodeMapper {

	List<CalculationStatusCodeDto> fetchAll(Map<String, Object> parameters);
}
