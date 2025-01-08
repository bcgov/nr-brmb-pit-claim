package ca.bc.gov.mal.cirras.claims.model.v1;

import java.io.Serializable;

//
// This is not going to be a resource.
//
public class ClaimCalculationUser implements Serializable {
	private static final long serialVersionUID = 1L;  // TODO: Does this need to be generated?

	private String claimCalculationUserGuid;
	private String loginUserGuid;
	private String loginUserId;
	private String loginUserType;
	private String givenName;
	private String familyName;
	
	
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
	
}
