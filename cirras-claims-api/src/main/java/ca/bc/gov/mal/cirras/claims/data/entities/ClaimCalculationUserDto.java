package ca.bc.gov.mal.cirras.claims.data.entities;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.wfone.common.persistence.dto.BaseDto;
import ca.bc.gov.nrs.wfone.common.persistence.utils.DtoUtils;




public class ClaimCalculationUserDto extends BaseDto<ClaimCalculationUserDto> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ClaimCalculationUserDto.class);

	private String claimCalculationUserGuid;
	private String loginUserGuid;
	private String loginUserId;
	private String loginUserType;
	private String givenName;
	private String familyName;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;
		
	public ClaimCalculationUserDto() {
	}
	
	
	public ClaimCalculationUserDto(ClaimCalculationUserDto dto) {

		this.claimCalculationUserGuid = dto.claimCalculationUserGuid;
		this.loginUserGuid = dto.loginUserGuid;
		this.loginUserId = dto.loginUserId;
		this.loginUserType = dto.loginUserType;
		this.givenName = dto.givenName;
		this.familyName = dto.familyName;
		this.createUser = dto.createUser;
		this.createDate = dto.createDate;
		this.updateUser = dto.updateUser;
		this.updateDate = dto.updateDate;
	}
	

	@Override
	public boolean equalsBK(ClaimCalculationUserDto other) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	@Override
	public boolean equalsAll(ClaimCalculationUserDto other) {
		boolean result = false;
		
		if(other!=null) {
			result = true;
			DtoUtils dtoUtils = new DtoUtils(getLogger());
			
			// equalsAll is called by dto.isDirty. The other DTO is created using the "copy" constructor
			result = result&&dtoUtils.equals("claimCalculationUserGuid", claimCalculationUserGuid, other.claimCalculationUserGuid);
			result = result&&dtoUtils.equals("loginUserGuid", loginUserGuid, other.loginUserGuid);
			result = result&&dtoUtils.equals("loginUserId", loginUserId, other.loginUserId);
			result = result&&dtoUtils.equals("loginUserType", loginUserType, other.loginUserType);
			result = result&&dtoUtils.equals("givenName", givenName, other.givenName);
			result = result&&dtoUtils.equals("familyName", familyName, other.familyName);
		}
		
		return result;
	}
	
	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public ClaimCalculationUserDto copy() {
		return new ClaimCalculationUserDto(this);
	}

	public String getClaimCalculationUserGuid() {
		return claimCalculationUserGuid;
	}

	public void setClaimCalculationUserGuid(String claimCalculationUserGuid) {
		this.claimCalculationUserGuid = claimCalculationUserGuid;
	}

	public String getLoginUserGuid() {
		return loginUserGuid;
	}

	public void setLoginUserGuid(String loginUserGuid) {
		this.loginUserGuid = loginUserGuid;
	}

	public String getLoginUserId() {
		return loginUserId;
	}

	public void setLoginUserId(String loginUserId) {
		this.loginUserId = loginUserId;
	}

	public String getLoginUserType() {
		return loginUserType;
	}

	public void setLoginUserType(String loginUserType) {
		this.loginUserType = loginUserType;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
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
