package ca.bc.gov.mal.cirras.claims.data.repositories.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.mal.cirras.claims.data.entities.ClaimDto;

public interface ClaimMapper {
	
	ClaimDto fetch(Map<String, Object> parameters);
	
	ClaimDto selectByClaimNumber(Map<String, Object> parameters);

	ClaimDto selectByProductId(Map<String, Object> parameters);
	
	List<ClaimDto> selectQuantityClaimsByPolicyId(Map<String, Object> parameters);
	
	int insert(Map<String, Object> parameters);

	int update(Map<String, Object> parameters);
	
	int updateClaimData(Map<String, Object> parameters);
	
	int updatePolicyData(Map<String, Object> parameters);
	
	int updateGrowerData(Map<String, Object> parameters);

	int delete(Map<String, Object> parameters);
	
	List<ClaimDto> selectAll();
	
	int selectCount(Map<String, Object> parameters);
	
	List<ClaimDto> select(Map<String, Object> parameters);

	
}
