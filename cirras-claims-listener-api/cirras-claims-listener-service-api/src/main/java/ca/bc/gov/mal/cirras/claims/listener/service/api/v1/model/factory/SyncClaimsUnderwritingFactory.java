package ca.bc.gov.mal.cirras.claims.listener.service.api.v1.model.factory;

import java.util.Date;

import ca.bc.gov.mal.cirras.claims.data.resources.SyncDopYieldContractSimpleRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.DopYieldContractSimpleRsrc;

public interface SyncClaimsUnderwritingFactory {

	//Dop Yield Contract Simple
	SyncDopYieldContractSimpleRsrc getSyncDopYieldContractSimpleRsrc(DopYieldContractSimpleRsrc dopYieldContractSimpleRsrc, Date eventDate, String eventType);
	SyncDopYieldContractSimpleRsrc getDeleteSyncDopYieldContractSimpleRsrc(String declaredYieldContractCommodityBerriesGuid, Date eventDate, String eventType);
	
}
