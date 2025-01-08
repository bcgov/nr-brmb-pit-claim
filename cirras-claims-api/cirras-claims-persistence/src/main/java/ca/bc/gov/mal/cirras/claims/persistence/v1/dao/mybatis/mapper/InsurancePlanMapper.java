package ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis.mapper;

import java.util.Map;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.InsurancePlanDto;

public interface InsurancePlanMapper {

	InsurancePlanDto fetch(Map<String, Object> parameters);
	
	int insert(Map<String, Object> parameters);

	int update(Map<String, Object> parameters);
	
	int delete(Map<String, Object> parameters);
    
}
