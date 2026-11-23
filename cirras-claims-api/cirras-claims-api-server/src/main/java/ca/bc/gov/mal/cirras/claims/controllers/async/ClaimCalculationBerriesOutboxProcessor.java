package ca.bc.gov.mal.cirras.claims.controllers.async;

import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.claims.data.models.BaseOutbox;
import ca.bc.gov.mal.cirras.claims.data.models.ClaimCalculationBerriesOutbox;
import ca.bc.gov.mal.cirras.claims.services.CirrasClaimsOutboxService;
import ca.bc.gov.mal.pit.common.service.api.ServiceException;
import ca.bc.gov.mal.cirras.claims.security.ClaimsPrincipal;

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
	protected List<? extends BaseOutbox> getNextOutboxes(int maxRecords, ClaimsPrincipal authentication,
			CirrasClaimsOutboxService cirrasClaimsOutboxService) throws ServiceException {
		return cirrasClaimsOutboxService.getNextClaimCalculationBerriesOutboxes(maxRecords, authentication);
	}

	@Override
	protected void processOutbox(BaseOutbox outbox, boolean doPublishEvent, ClaimsPrincipal authentication,
			CirrasClaimsOutboxService cirrasClaimsOutboxService) throws ServiceException {
		ClaimCalculationBerriesOutbox claimCalculationBerriesOutbox = (ClaimCalculationBerriesOutbox)outbox;
		cirrasClaimsOutboxService.processClaimCalculationBerriesOutbox(claimCalculationBerriesOutbox, doPublishEvent, authentication);
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

}
