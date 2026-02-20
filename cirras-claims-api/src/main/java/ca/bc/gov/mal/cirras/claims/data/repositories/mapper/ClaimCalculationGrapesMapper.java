package ca.bc.gov.mal.cirras.claims.data.repositories.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.claims.data.entities.ClaimCalculationGrapesDto;

public interface ClaimCalculationGrapesMapper {

	ClaimCalculationGrapesDto fetch(Map<String, Object> parameters);
    
    int insert(Map<String, Object> parameters);
    
    int update(Map<String, Object> parameters);
    
    int delete(Map<String, Object> parameters);
    
    int deleteForClaim(Map<String, Object> parameters);
    
    ClaimCalculationGrapesDto select(Map<String, Object> parameters);

    List<ClaimCalculationGrapesDto> selectAll();	
    
}
