package ca.bc.gov.mal.cirras.claims.data.repositories.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.claims.data.entities.ClaimCalculationBerriesOutboxDto;


public interface ClaimCalculationBerriesOutboxMapper {

	ClaimCalculationBerriesOutboxDto fetch(Map<String, Object> parameters);
    
	int insert(Map<String, Object> parameters);
    
	int update(Map<String, Object> parameters);
    
	int delete(Map<String, Object> parameters);

    List<ClaimCalculationBerriesOutboxDto> select(Map<String, Object> parameters);
}