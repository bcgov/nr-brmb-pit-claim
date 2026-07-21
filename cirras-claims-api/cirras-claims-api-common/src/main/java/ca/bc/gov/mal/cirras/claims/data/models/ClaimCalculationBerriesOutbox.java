package ca.bc.gov.mal.cirras.claims.data.models;

import java.io.Serializable;

//
// This is not going to be a resource.
//
public class ClaimCalculationBerriesOutbox extends BaseOutbox implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer claimCalculationBerriesOutboxId;
	private String claimCalculationBerriesGuid;
	
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

	@Override
	public String getSourceKey() {
		return claimCalculationBerriesGuid;
	}
	
	@Override
	public Integer getOutboxKey() {
		return claimCalculationBerriesOutboxId;
	}
}
