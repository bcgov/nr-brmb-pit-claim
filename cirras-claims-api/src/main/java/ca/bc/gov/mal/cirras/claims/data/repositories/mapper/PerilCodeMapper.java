package ca.bc.gov.mal.cirras.claims.data.repositories.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.claims.data.entities.PerilCodeDto;

public interface PerilCodeMapper {

	List<PerilCodeDto> fetchAll(Map<String, Object> parameters);

	List<PerilCodeDto> selectForCoverage(Map<String, Object> parameters);

	PerilCodeDto fetch(Map<String, Object> parameters);
	
	int insert(Map<String, Object> parameters);

	int update(Map<String, Object> parameters);
	
	int delete(Map<String, Object> parameters);
    
}
