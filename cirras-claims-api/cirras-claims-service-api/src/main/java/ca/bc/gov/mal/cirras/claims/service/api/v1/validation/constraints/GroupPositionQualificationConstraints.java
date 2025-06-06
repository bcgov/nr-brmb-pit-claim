package ca.bc.gov.mal.cirras.claims.service.api.v1.validation.constraints;

import javax.validation.constraints.NotBlank;

import ca.bc.gov.mal.cirras.claims.service.api.v1.validation.Errors;

public interface GroupPositionQualificationConstraints {
	@NotBlank(message=Errors.RESOURCE_POSITION_QUALIFICATION_CLASS_NAME_NOTBLANK, groups= GroupPositionQualificationConstraints.class)
	public String getResourceClassificationName();

}
