package ca.bc.gov.mal.cirras.claims.model.v1;

import java.io.Serializable;
import java.util.Date;


public interface SyncCoveragePeril extends Serializable {

	public Integer getCoveragePerilId();
	public void setCoveragePerilId(Integer coveragePerilId);

 	public Integer getCropCommodityId();
	public void setCropCommodityId(Integer cropCommodityId);
 
 	public String getPerilCode();
	public void setPerilCode(String perilCode);
 
 	public String getCommodityCoverageCode();
	public void setCommodityCoverageCode(String commodityCoverageCode); 
 
 	public Boolean getIsActive();
	public void setIsActive(Boolean isActive);

	public Date getDataSyncTransDate();
	public void setDataSyncTransDate(Date dataSyncTransDate);

	public String getTransactionType();
	public void setTransactionType(String transactionType);

}
