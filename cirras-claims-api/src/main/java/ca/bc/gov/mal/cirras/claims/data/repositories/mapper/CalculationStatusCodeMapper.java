package ca.bc.gov.mal.cirras.claims.data.repositories.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.claims.data.entities.CalculationStatusCodeDto;

public interface CalculationStatusCodeMapper {

	List<CalculationStatusCodeDto> fetchAll(Map<String, Object> parameters);
}
