package ca.bc.gov.mal.cirras.claims.service.api.v1.validation.constraints;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import ca.bc.gov.mal.cirras.claims.service.api.v1.validation.Errors;

public interface GroupMemberConstraints {

	public String getResourceGroupMemberGuid();

	@NotNull(message=Errors.RESOURCE_GROUP_MEMBER_LEADER_IND_NOTNULL, groups= GroupMemberConstraints.class)
	public Boolean getLeaderInd();

	public String getResourceRemovedDate();

	@NotBlank(message=Errors.RESOURCE_GROUP_MEMBER_RESOURCE_UNIT_GUID_NOTBLANK, groups= GroupMemberConstraints.class)
	@Size(min=32, max=32, message=Errors.RESOURCE_GROUP_MEMBER_RESOURCE_UNIT_GUID_SIZE, groups=GroupMemberConstraints.class)
	public String getResourceUnitGuid();

	public String getFirstName();

	public String getMiddleName();

	public String getLastName();

	public String getOtherPositionName();

	public String getOtherPositionAcronym();

	public Long getOtherDisplayOrder();

	public String getPositionName();

	public String getPositionAcronym();

	public Long getDisplayOrder();

}
