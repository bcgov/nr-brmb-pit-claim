package ca.bc.gov.mal.cirras.claims.data.resources;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.mal.cirras.claims.data.resources.types.ResourceTypes;
import ca.bc.gov.mal.cirras.claims.data.models.ClaimCalculationBerries;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.CLAIM_CALCULATION_SIMPLE_NAME)
@XmlSeeAlso({ ClaimCalculationSimpleRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class ClaimCalculationSimpleRsrc extends BaseResource {

	private static final long serialVersionUID = 1L;

	// calculation
	private String claimCalculationGuid;
	private Integer cropYear;
	private Integer contractId;
	private Integer cropCommodityId;
	private Integer calculationVersion;
	private String calculationStatusCode;

	//Sub table models specific values
	private ClaimCalculationBerries claimCalculationBerries;

	
	public String getClaimCalculationGuid() {
		return claimCalculationGuid;
	}

	public void setClaimCalculationGuid(String claimCalculationGuid) {
		this.claimCalculationGuid = claimCalculationGuid;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getCropYear() {
		return cropYear;
	}

	public void setCropYear(Integer cropYear) {
		this.cropYear = cropYear;
	}

	public Integer getCropCommodityId() {
		return cropCommodityId;
	}

	public void setCropCommodityId(Integer cropCommodityId) {
		this.cropCommodityId = cropCommodityId;
	}

	public Integer getCalculationVersion() {
		return calculationVersion;
	}

	public void setCalculationVersion(Integer calculationVersion) {
		this.calculationVersion = calculationVersion;
	}
	
	public String getCalculationStatusCode() {
		return calculationStatusCode;
	}

	public void setCalculationStatusCode(String calculationStatusCode) {
		this.calculationStatusCode = calculationStatusCode;
	}

	public ClaimCalculationBerries getClaimCalculationBerries() {
		return claimCalculationBerries;
	}

	public void setClaimCalculationBerries(ClaimCalculationBerries claimCalculationBerries) {
		this.claimCalculationBerries = claimCalculationBerries;
	}

}
