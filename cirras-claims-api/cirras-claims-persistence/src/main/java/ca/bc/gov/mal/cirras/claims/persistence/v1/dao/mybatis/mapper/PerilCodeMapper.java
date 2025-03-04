package ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.PerilCodeDto;

public interface PerilCodeMapper {

	List<PerilCodeDto> fetchAll(Map<String, Object> parameters);

	List<PerilCodeDto> selectForCoverage(Map<String, Object> parameters);

	PerilCodeDto fetch(Map<String, Object> parameters);
	
	int insert(Map<String, Object> parameters);

	int update(Map<String, Object> parameters);
	
	int delete(Map<String, Object> parameters);
    
}
