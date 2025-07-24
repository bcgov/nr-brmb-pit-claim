package ca.bc.gov.mal.cirras.claims.service.api.v1.util;

public class ClaimsServiceEnums {

	public enum UpdateTypes {
		REPLACE_NEW,
		REPLACE_COPY,
		SUBMIT
	}
	
	public enum InsurancePlans{
		GRAPES,
		TREEFRUITS,
		BERRIES,
		GRAIN,
		FORAGE,
		VEGETABLES,
		FLOWERS
	}

	public enum CalculationStatusCodes {
		DRAFT,
		SUBMITTED,
		RECOMMENDED,
		APPROVED,
		ARCHIVED
	}

	public enum ClaimStatusCodes {
		Open("OPEN"),
		InProgress("IN PROGRESS"),
		Pending("PENDING"),
		Approved("APPROVED");
		
		private String claimStatus;
		 
		ClaimStatusCodes(String status) {
	        this.claimStatus = status;
	    }
	 
	    public String getClaimStatusCode() {
	        return claimStatus;
	    }
	}
	
	public enum CommodityCoverageCodes{
		Awp("AWP"),
		AwpExcreta("AWPE"),
		AcreageLoss("CAL"),
		HailRain("CHAIL"),
		LotHailRain("CLHR"),
		Other("COTH"),
		Plant("CPLANT"),
		PlantTreeFruit("CPTF"),
		QuantityForage("CQF"),
		QuantityGrain("CQG"),
		Quantity("CQNT"),
		QuantitySilageCorn("CQSC"),
		QuantityTreeFruit("CQTF"),
		QuantityTreeFruitNetProduction("CQTFN"),
		GrainSpotLoss("CSL"),
		CropUnseeded("CUNS"),
		ForageSupply("FS"),
		GrainBasket("GB"),
		SpecialBu("SBU"),
		SpecialTc("STC"),
		YoungPlant("YP");
		
		private String code;
		 
		CommodityCoverageCodes(String cvrgCode) {
	        this.code = cvrgCode;
	    }
	 
	    public String getCode() {
	        return code;
	    }
		
	}
	
	public enum InsuredByMeasurementType{
		ACRES,
		UNITS,
		UNKNOWN
	}

	public enum ProductStatusCodes {
		OFFER,
		PENDING,
		FINAL
	}
	
	public enum TransactionTypes{
		Inserted("INSERTED"),
		Updated("UPDATED"),
		Deleted("DELETED");
		
		private String transactionType;
		 
		TransactionTypes(String trnType) {
	        this.transactionType = trnType;
	    }
	 
	    public String getCode() {
	        return transactionType;
	    }
		
	}	
}
