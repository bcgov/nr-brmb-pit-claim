package ca.bc.gov.mal.cirras.claims.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.mal.cirras.claims.data.models.ClaimCalculationBerriesOutbox;
import ca.bc.gov.mal.cirras.claims.data.models.OutboxTransactionTypes;
import ca.bc.gov.mal.cirras.claims.data.entities.ClaimCalculationBerriesDto;
import ca.bc.gov.mal.cirras.claims.data.entities.ClaimCalculationBerriesOutboxDto;
import ca.bc.gov.mal.cirras.claims.controllers.publisher.EventPublisher;
import ca.bc.gov.mal.cirras.claims.controllers.publisher.EventPublisherException;
import ca.bc.gov.mal.cirras.claims.data.assemblers.ClaimCalculationSimpleRsrcFactory;
import ca.bc.gov.mal.cirras.claims.data.assemblers.OutboxFactory;
import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimCalculationBerriesDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimCalculationBerriesOutboxDao;
import ca.bc.gov.mal.cirras.claims.data.resources.ClaimCalculationSimpleRsrc;
import ca.bc.gov.mal.cirras.claims.data.resources.ClaimEventTypes;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;
import ca.bc.gov.nrs.wfone.common.service.api.ServiceException;
import ca.bc.gov.nrs.wfone.common.webade.authentication.WebAdeAuthentication;

public class CirrasClaimsOutboxService {

	private static final Logger logger = LoggerFactory.getLogger(CirrasClaimsOutboxService.class);

	private Properties applicationProperties;

	// factories
	private OutboxFactory outboxFactory;
	private ClaimCalculationSimpleRsrcFactory claimCalculationSimpleRsrcFactory;

	// daos
	private ClaimCalculationBerriesOutboxDao claimCalculationBerriesOutboxDao;
	private ClaimCalculationBerriesDao claimCalculationBerriesDao;

	private EventPublisher eventPublisher;

	public void setApplicationProperties(Properties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	public void setOutboxFactory(OutboxFactory outboxFactory) {
		this.outboxFactory = outboxFactory;
	}

	public void setClaimCalculationSimpleRsrcFactory(ClaimCalculationSimpleRsrcFactory claimCalculationSimpleRsrcFactory) {
		this.claimCalculationSimpleRsrcFactory = claimCalculationSimpleRsrcFactory;
	}

	public void setClaimCalculationBerriesOutboxDao(ClaimCalculationBerriesOutboxDao claimCalculationBerriesOutboxDao) {
		this.claimCalculationBerriesOutboxDao = claimCalculationBerriesOutboxDao;
	}

	public void setClaimCalculationBerriesDao(ClaimCalculationBerriesDao claimCalculationBerriesDao) {
		this.claimCalculationBerriesDao = claimCalculationBerriesDao;
	}
	
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public List<ClaimCalculationBerriesOutbox> getNextClaimCalculationBerriesOutboxes(
		Integer maxRecords, 
		WebAdeAuthentication authentication
	) throws ServiceException
	{
		logger.debug("<getNextClaimCalculationBerriesOutboxes");

		List<ClaimCalculationBerriesOutbox> results = null;

		try {
			List<ClaimCalculationBerriesOutboxDto> dtos = claimCalculationBerriesOutboxDao.select(maxRecords);
			results = outboxFactory.getClaimCalculationBerriesOutboxList(dtos);
		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getNextClaimCalculationBerriesOutboxes");
		
		return results;
	}
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void processClaimCalculationBerriesOutbox(
			ClaimCalculationBerriesOutbox claimCalculationBerriesOutbox,
		Boolean doPublishEvent,
		WebAdeAuthentication authentication
	) 
	throws ServiceException
	{
		logger.debug("<processClaimCalculationBerriesOutbox");

		try {

			if ( doPublishEvent.booleanValue() ) { 
				String eventType = null;
				ClaimCalculationSimpleRsrc beforeClaimCalculationSimpleRsrc = null;
				ClaimCalculationSimpleRsrc afterClaimCalculationSimpleRsrc = null;
				Map<String, String> sourceIdentifiers = new HashMap<>();
					
				if ( claimCalculationBerriesOutbox.getTransactionType().equals(OutboxTransactionTypes.Insert) ) {
					eventType = ClaimEventTypes.ClaimCalculationBerriesCreated;
					afterClaimCalculationSimpleRsrc = getClaimCalculationCommdityBerries(claimCalculationBerriesOutbox.getClaimCalculationBerriesGuid());
					sourceIdentifiers.put("claimCalculationBerriesGuid", afterClaimCalculationSimpleRsrc.getClaimCalculationBerries().getClaimCalculationBerriesGuid());
						
				} else if (claimCalculationBerriesOutbox.getTransactionType().equals(OutboxTransactionTypes.Update) ) {
					eventType = ClaimEventTypes.ClaimCalculationBerriesUpdated;
					afterClaimCalculationSimpleRsrc = getClaimCalculationCommdityBerries(claimCalculationBerriesOutbox.getClaimCalculationBerriesGuid());
					sourceIdentifiers.put("claimCalculationBerriesGuid", afterClaimCalculationSimpleRsrc.getClaimCalculationBerries().getClaimCalculationBerriesGuid());
						
				} else if (claimCalculationBerriesOutbox.getTransactionType().equals(OutboxTransactionTypes.Delete) ) {
					eventType = ClaimEventTypes.ClaimCalculationBerriesDeleted;

					// Since the delete has already happened, no resource is included in the event.
					sourceIdentifiers.put("claimCalculationBerriesGuid", claimCalculationBerriesOutbox.getClaimCalculationBerriesGuid());
						
				} else { 
					throw new ServiceException("Claim Calculation Berries Outbox returned invalid transaction type");
				}

				// Delete Outbox record before publishing event. If the publish fails, the exception 
				// rolls back the delete.
				deleteClaimCalculationBerriesOutbox(claimCalculationBerriesOutbox.getClaimCalculationBerriesOutboxId());
				publishClaimCalculationSimple(eventType, beforeClaimCalculationSimpleRsrc, afterClaimCalculationSimpleRsrc, sourceIdentifiers);
			} else {
				// Not publishing an event because it would be a duplicate, so just delete the outbox record.
				deleteClaimCalculationBerriesOutbox(claimCalculationBerriesOutbox.getClaimCalculationBerriesOutboxId());
			}

		} catch (NotFoundException e) {
			// If cropId does not exist, then there must be a delete event that will be processed later.
			// So we can ignore this insert/update event and just delete the outbox record.
			logger.info("Skipped insert/update event for claimCalculationBerriesGuid " + claimCalculationBerriesOutbox.getClaimCalculationBerriesGuid() + " as it no longer exists.");
			try { 
				deleteClaimCalculationBerriesOutbox(claimCalculationBerriesOutbox.getClaimCalculationBerriesOutboxId());
			} catch (DaoException e2) { 
				throw new ServiceException("DAO threw an exception", e2);
			}

		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		} catch (EventPublisherException e) {
			throw new ServiceException("Event Publisher threw an exception", e);
		}
		
		logger.debug(">processClaimCalculationBerriesOutbox");
	}
	
	public boolean publishAndDelete = true;
	
	//Only set to false in certain unit tests
	public void setPublishAndDelete(boolean publishAndDelete) {
		logger.debug("<setPublishAndDelete");
		
		this.publishAndDelete = publishAndDelete;
		
		logger.debug(">setPublishAndDelete");
	}

	public void publishClaimCalculationSimple(String eventType, ClaimCalculationSimpleRsrc beforeClaimCalculationSimpleRsrc,
			ClaimCalculationSimpleRsrc afterClaimCalculationSimpleRsrc, Map<String, String> sourceIdentifiers)
			throws EventPublisherException {
		if(publishAndDelete) {
			eventPublisher.publish(eventType, beforeClaimCalculationSimpleRsrc, afterClaimCalculationSimpleRsrc, sourceIdentifiers);
		} else {
			logger.info("Message not published because publishAndDelete is set to false. sourceIdentifier: " + sourceIdentifiers.values().stream()
                    .findFirst()
                    .orElse("No sourceIdentifier found"));
		}

	}

	public void deleteClaimCalculationBerriesOutbox(
			Integer claimCalculationBerriesOutboxId) throws DaoException, NotFoundDaoException {
		if(publishAndDelete) {
			claimCalculationBerriesOutboxDao.delete(claimCalculationBerriesOutboxId);
		} else {
			logger.info("Record not deleted because publishAndDelete is set to false. claimCalculationBerriesOutboxId: " + claimCalculationBerriesOutboxId.toString());
		}
	}

	private ClaimCalculationSimpleRsrc getClaimCalculationCommdityBerries(String claimCalculationBerriesGuid) throws ServiceException, NotFoundException {
		logger.debug("<getClaimCalculationCommdityBerries");
			
		ClaimCalculationSimpleRsrc result = null;

		try {
			ClaimCalculationBerriesDto berriesDto = claimCalculationBerriesDao.fetch(claimCalculationBerriesGuid);
				
			if(berriesDto == null) {
				throw new NotFoundException("no claim calculation berries record found for " + claimCalculationBerriesGuid);
			}
				
			result = claimCalculationSimpleRsrcFactory.getClaimCalculationSimple(berriesDto);
		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}
			
		logger.debug(">getClaimCalculationCommdityBerries");
		return result;
	}



}
