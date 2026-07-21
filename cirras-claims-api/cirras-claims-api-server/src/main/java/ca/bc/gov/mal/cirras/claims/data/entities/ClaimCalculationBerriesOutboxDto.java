package ca.bc.gov.mal.cirras.claims.data.entities;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;

public class ClaimCalculationBerriesOutboxDto extends BaseDto<ClaimCalculationBerriesOutboxDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ClaimCalculationBerriesOutboxDto.class);

	private Integer claimCalculationBerriesOutboxId;
	private String claimCalculationBerriesGuid;
	private String auditTransactionTypeCode;
	
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;
	
	public ClaimCalculationBerriesOutboxDto() {
	}
	
	
	public ClaimCalculationBerriesOutboxDto(ClaimCalculationBerriesOutboxDto dto) {

		this.claimCalculationBerriesOutboxId = dto.claimCalculationBerriesOutboxId;
		this.claimCalculationBerriesGuid = dto.claimCalculationBerriesGuid;
		this.auditTransactionTypeCode = dto.auditTransactionTypeCode;

		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;
	}
	

	@Override
	public boolean equalsBK(ClaimCalculationBerriesOutboxDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(ClaimCalculationBerriesOutboxDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			result = result&&dtoUtils.equals("claimCalculationBerriesOutboxId", claimCalculationBerriesOutboxId, other.claimCalculationBerriesOutboxId);
			result = result&&dtoUtils.equals("claimCalculationBerriesGuid", claimCalculationBerriesGuid, other.claimCalculationBerriesGuid);
			result = result&&dtoUtils.equals("auditTransactionTypeCode", auditTransactionTypeCode, other.auditTransactionTypeCode);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public ClaimCalculationBerriesOutboxDto copy() {
		return new ClaimCalculationBerriesOutboxDto(this);
	}

	public Integer getClaimCalculationBerriesOutboxId() {
		return claimCalculationBerriesOutboxId;
	}

	public void setClaimCalculationBerriesOutboxId(Integer claimCalculationBerriesOutboxId) {
		this.claimCalculationBerriesOutboxId = claimCalculationBerriesOutboxId;
	}
	
 	public String getClaimCalculationBerriesGuid() {
		return claimCalculationBerriesGuid;
	}

	public void setClaimCalculationBerriesGuid(String claimCalculationBerriesGuid) {
		this.claimCalculationBerriesGuid = claimCalculationBerriesGuid;
	}

	public String getAuditTransactionTypeCode() {
		return auditTransactionTypeCode;
	}

	public void setAuditTransactionTypeCode(String auditTransactionTypeCode) {
		this.auditTransactionTypeCode = auditTransactionTypeCode;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
 
 	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
 
 	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
 
 	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
 
}
