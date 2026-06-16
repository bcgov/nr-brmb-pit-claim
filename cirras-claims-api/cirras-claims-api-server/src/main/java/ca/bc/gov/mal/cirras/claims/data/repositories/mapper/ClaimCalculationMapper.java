package ca.bc.gov.mal.cirras.claims.data.repositories.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.claims.data.entities.ClaimCalculationDto;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;

public interface ClaimCalculationMapper {
	ClaimCalculationDto fetch(Map<String, Object> parameters);
	
	int insert(Map<String, Object> parameters);

	int update(Map<String, Object> parameters);

	int delete(Map<String, Object> parameters);
	
	int selectCount(Map<String, Object> parameters);
	
	List<ClaimCalculationDto> select(Map<String, Object> parameters);

	List<ClaimCalculationDto> getByClaimNumber(Map<String, Object> parameters);
	
	List<ClaimCalculationDto> getCalculationsForDataSync(Map<String, Object> parameters);
	
	List<ClaimCalculationDto> getCalculationsByGrainQuantityGuid(Map<String, Object> parameters);

	ClaimCalculationDto getByClaimNumberAndVersion(Map<String, Object> parameters);
}
