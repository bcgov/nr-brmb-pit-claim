package ca.bc.gov.mal.cirras.claims.persistence.v1.dao.mybatis.mapper;

import java.util.Map;

import ca.bc.gov.mal.cirras.claims.persistence.v1.dto.ClaimCalculationUserDto;

public interface ClaimCalculationUserMapper {
	ClaimCalculationUserDto fetch(Map<String, Object> parameters);
	
	ClaimCalculationUserDto getByLoginUserGuid(Map<String, Object> parameters);
	
	int insert(Map<String, Object> parameters);

	int update(Map<String, Object> parameters);

	int delete(Map<String, Object> parameters);	
}
