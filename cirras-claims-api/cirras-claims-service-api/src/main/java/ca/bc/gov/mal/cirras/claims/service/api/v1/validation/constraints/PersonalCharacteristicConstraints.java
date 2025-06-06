package ca.bc.gov.mal.cirras.claims.service.api.v1.validation.constraints;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import ca.bc.gov.mal.cirras.claims.service.api.v1.validation.Errors;

public interface PersonalCharacteristicConstraints {

	@NotNull(message=Errors.PERSONAL_CHAR_SELF_DECLARATION_IND_NOTNULL, groups=PersonalCharacteristicConstraints.class)
	public Boolean getSelfDeclarationInd();

	@NotBlank(message=Errors.PERSONAL_CHAR_SELF_DECLARATION_CHAR_TYPE_NOTBLANK, groups=PersonalCharacteristicConstraints.class)
	public String getCharacteristicType();

	@NotBlank(message=Errors.PERSONAL_CHAR_SELF_DECLARATION_CHAR_VALUE_NOTBLANK, groups=PersonalCharacteristicConstraints.class)
	@Size(max=4000, message=Errors.PERSONAL_CHAR_SELF_DECLARATION_CHAR_VALUE_SIZE, groups=PersonalCharacteristicConstraints.class)
	public String getCharacteristicValue();

}
