package ca.bc.gov.mal.cirras.claims.listener.api.rest.v1.resource.factory;

import java.util.Date;

import ca.bc.gov.mal.cirras.claims.listener.service.api.v1.model.factory.SyncClaimsUnderwritingFactory;
import ca.bc.gov.mal.cirras.claims.data.models.SyncDopYieldContractCommodityBerries;
import ca.bc.gov.mal.cirras.claims.data.resources.SyncDopYieldContractSimpleRsrc;
import ca.bc.gov.mal.cirras.claims.data.resources.UnderwritingSyncEventTypes;
import ca.bc.gov.mal.cirras.underwriting.data.models.DopYieldContractCommodityBerries;
import ca.bc.gov.mal.cirras.underwriting.data.resources.DopYieldContractSimpleRsrc;
import ca.bc.gov.mal.cirras.underwriting.data.resources.UnderwritingEventTypes;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.resource.factory.BaseResourceFactory;

public class SyncClaimsUnderwritingRsrcFactory extends BaseResourceFactory implements SyncClaimsUnderwritingFactory {

	@Override
	public SyncDopYieldContractSimpleRsrc getSyncDopYieldContractSimpleRsrc(DopYieldContractSimpleRsrc dopYieldContractSimpleRsrc, Date eventDate, String eventType) {
		
		DopYieldContractCommodityBerries dopYieldContractCommodityBerries = dopYieldContractSimpleRsrc.getDopYieldContractCommodityBerries();
		
		SyncDopYieldContractSimpleRsrc resource = new SyncDopYieldContractSimpleRsrc();
		
		resource.setDeclaredYieldContractGuid(dopYieldContractSimpleRsrc.getDeclaredYieldContractGuid());
		resource.setContractId(dopYieldContractSimpleRsrc.getContractId());
		resource.setCropYear(dopYieldContractSimpleRsrc.getCropYear());
		resource.setDataSyncTransDate(eventDate);
		resource.setTransactionType(eventType.replace(UnderwritingEventTypes.EventTypeNamespace, UnderwritingSyncEventTypes.EventTypeNamespace));

		
		SyncDopYieldContractCommodityBerries sdyccbModel = new SyncDopYieldContractCommodityBerries();
		sdyccbModel.setCropCommodityId(dopYieldContractCommodityBerries.getCropCommodityId());
		sdyccbModel.setCropCommodityName(dopYieldContractCommodityBerries.getCropCommodityName());
		sdyccbModel.setDeclaredYieldContractCommodityBerriesGuid(dopYieldContractCommodityBerries.getDeclaredYieldContractCommodityBerriesGuid());
		sdyccbModel.setTotalAbandonmentYield(dopYieldContractCommodityBerries.getTotalAbandonmentYield());
		sdyccbModel.setTotalProduction(dopYieldContractCommodityBerries.getTotalProduction());
		sdyccbModel.setTotalProductionOverride(dopYieldContractCommodityBerries.getTotalProductionOverride());
		sdyccbModel.setTotalSalesYield(dopYieldContractCommodityBerries.getTotalSalesYield());
		sdyccbModel.setTotalSoldShippedYield(dopYieldContractCommodityBerries.getTotalSoldShippedYield());
		
		resource.setSyncDopYieldContractCommodityBerries(sdyccbModel);
		
		return resource;
	}

	@Override
	public SyncDopYieldContractSimpleRsrc getDeleteSyncDopYieldContractSimpleRsrc(String declaredYieldContractCommodityBerriesGuid, Date eventDate,
			String eventType) {

		SyncDopYieldContractSimpleRsrc resource = new SyncDopYieldContractSimpleRsrc();

		resource.setDataSyncTransDate(eventDate);
		resource.setTransactionType(eventType.replace(UnderwritingEventTypes.EventTypeNamespace, UnderwritingSyncEventTypes.EventTypeNamespace));


		SyncDopYieldContractCommodityBerries sdyccbModel = new SyncDopYieldContractCommodityBerries();
		sdyccbModel.setDeclaredYieldContractCommodityBerriesGuid(declaredYieldContractCommodityBerriesGuid);

		resource.setSyncDopYieldContractCommodityBerries(sdyccbModel);

		return resource;
	}

}
