package ca.bc.gov.mal.cirras.claims.data.models;

import java.io.Serializable;
import java.util.Date;


public interface SyncCommodityVariety extends Serializable {

	public Integer getCropId();
	public void setCropId(Integer cropId);

	public String getCropName();
	public void setCropName(String cropName);	
	
	public Integer getParentCropId();
	public void setParentCropId(Integer parentCropId);

	public Boolean getIsProductInsurable();
	public void setIsProductInsurable(Boolean isProductInsurable);

	public Boolean getIsInventoryCrop();
	public void setIsInventoryCrop(Boolean isInventoryCrop);
	
	public Date getDataSyncTransDate();
	public void setDataSyncTransDate(Date dataSyncTransDate);
	
	public String getTransactionType();
	public void setTransactionType(String transactionType);

}
