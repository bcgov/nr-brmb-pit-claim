package ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationGrainQuantityDto;

public interface ClaimCalculationGrainQuantityMapper {

	ClaimCalculationGrainQuantityDto fetch(Map<String, Object> parameters);
    
    int insert(Map<String, Object> parameters);
    
    int update(Map<String, Object> parameters);
    
    int delete(Map<String, Object> parameters);
    
    ClaimCalculationGrainQuantityDto select(Map<String, Object> parameters);

    List<ClaimCalculationGrainQuantityDto> selectAll();	
    
}
