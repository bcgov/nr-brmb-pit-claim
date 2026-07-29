package ca.bc.gov.mal.cirras.claims.data.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;


public class CalculationStatusCodeDto extends BaseDto<CalculationStatusCodeDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(CalculationStatusCodeDto.class);
	
	private String calculationStatusCode;
	private String description;
	
	public CalculationStatusCodeDto() {
	}
	
	
	public CalculationStatusCodeDto(CalculationStatusCodeDto dto) {
		this.calculationStatusCode = dto.calculationStatusCode;
		this.description = dto.description;
	}
	

	@Override
	public boolean equalsBK(CalculationStatusCodeDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(CalculationStatusCodeDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			
			// equalsAll is called by dto.isDirty. The other DTO is created using the "copy" constructor
			result = result&&dtoUtils.equals("calculationStatusCode", calculationStatusCode, other.calculationStatusCode);
			result = result&&dtoUtils.equals("description", description, other.description);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public CalculationStatusCodeDto copy() {
		return new CalculationStatusCodeDto(this);
	}

	public String getCalculationStatusCode() {
		return calculationStatusCode;
	}

	public void setCalculationStatusCode(String calculationStatusCode) {
		this.calculationStatusCode = calculationStatusCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
