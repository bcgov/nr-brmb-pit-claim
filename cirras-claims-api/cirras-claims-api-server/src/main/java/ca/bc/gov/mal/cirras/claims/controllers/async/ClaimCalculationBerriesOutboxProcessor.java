package ca.bc.gov.mal.cirras.claims.controllers.async;

import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.claims.data.models.BaseOutbox;
import ca.bc.gov.mal.cirras.claims.data.models.ClaimCalculationBerriesOutbox;
import ca.bc.gov.mal.cirras.claims.services.CirrasClaimsOutboxService;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

public class ClaimCalculationBerriesOutboxProcessor extends OutboxProcessor{

	protected ClaimCalculationBerriesOutboxProcessor(Properties applicationProperties) {
		super(applicationProperties);
	}

	private static final Logger logger = LoggerFactory.getLogger(ClaimCalculationBerriesOutboxProcessor.class);

	@Override
	protected String getOutboxClassName() {
		return ClaimCalculationBerriesOutbox.class.getName();
	}

	@Override
	protected List<? extends BaseOutbox> getNextOutboxes(int maxRecords, WebAdeAuthentication authentication,
			CirrasClaimsOutboxService cirrasClaimsOutboxService) throws ServiceException {
		//TODO: PIM-2509
		//return cirrasClaimsOutboxService.getNextClaimCalculationBerriesOutboxes(maxRecords, authentication);
		return null;
	}

	@Override
	protected void processOutbox(BaseOutbox outbox, boolean doPublishEvent, WebAdeAuthentication authentication,
			CirrasClaimsOutboxService cirrasClaimsOutboxService) throws ServiceException {
		ClaimCalculationBerriesOutbox dopYieldContractCommodityBerriesOutbox = (ClaimCalculationBerriesOutbox)outbox;
		//TODO: PIM-2509
		//cirrasClaimsOutboxService.processClaimCalculationBerriesOutbox(dopYieldContractCommodityBerriesOutbox, doPublishEvent, authentication);
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

}
