package ca.bc.gov.mal.cirras.claims.listener.service.api.v1;

import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;

public interface CirrasClaimsPoliciesListenerService {
  
	void processCirrasClaimEvent(String eventMessageString, FactoryContext factoryContext) throws ServiceException, Throwable;
}
