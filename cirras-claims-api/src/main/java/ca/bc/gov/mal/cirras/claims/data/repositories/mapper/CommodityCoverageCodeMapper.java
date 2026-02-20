package ca.bc.gov.mal.cirras.claims.data.repositories.mapper;

import java.util.Map;

import ca.bc.gov.mal.cirras.claims.data.entities.CommodityCoverageCodeDto;

public interface CommodityCoverageCodeMapper {

	CommodityCoverageCodeDto fetch(Map<String, Object> parameters);
	
	int insert(Map<String, Object> parameters);

	int update(Map<String, Object> parameters);
	
	int delete(Map<String, Object> parameters);
    
}
