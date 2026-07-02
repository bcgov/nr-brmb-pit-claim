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
import ca.bc.gov.mal.cirras.claims.data.assemblers.OutboxFactory;
import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimCalculationBerriesDao;
import ca.bc.gov.mal.cirras.claims.data.repositories.ClaimCalculationBerriesOutboxDao;
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
//	private DopYieldContractSimpleRsrcFactory dopYieldContractSimpleRsrcFactory;

	// daos
	private ClaimCalculationBerriesOutboxDao declaredYieldContractCommodityBerriesOutboxDao;
	private ClaimCalculationBerriesDao declaredYieldContractCommodityBerriesDao;

	private EventPublisher eventPublisher;

	public void setApplicationProperties(Properties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	public void setOutboxFactory(OutboxFactory outboxFactory) {
		this.outboxFactory = outboxFactory;
	}

//	public void setDopYieldContractSimpleRsrcFactory(DopYieldContractSimpleRsrcFactory dopYieldContractSimpleRsrcFactory) {
//		this.dopYieldContractSimpleRsrcFactory = dopYieldContractSimpleRsrcFactory;
//	}

	public void setClaimCalculationBerriesOutboxDao(ClaimCalculationBerriesOutboxDao declaredYieldContractCommodityBerriesOutboxDao) {
		this.declaredYieldContractCommodityBerriesOutboxDao = declaredYieldContractCommodityBerriesOutboxDao;
	}

	public void setClaimCalculationBerriesDao(ClaimCalculationBerriesDao declaredYieldContractCommodityBerriesDao) {
		this.declaredYieldContractCommodityBerriesDao = declaredYieldContractCommodityBerriesDao;
	}
	
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public List<ClaimCalculationBerriesOutbox> getNextDopYieldContractCommodityBerriesOutboxes(
		Integer maxRecords, 
		WebAdeAuthentication authentication
	) throws ServiceException
	{
		logger.debug("<getNextDopYieldContractCommodityBerriesOutboxes");

		List<ClaimCalculationBerriesOutbox> results = null;

		try {
			List<ClaimCalculationBerriesOutboxDto> dtos = declaredYieldContractCommodityBerriesOutboxDao.select(maxRecords);
			results = outboxFactory.getDopYieldContractCommodityBerriesOutboxList(dtos);
		} catch (DaoException e) {
			throw new ServiceException("DAO threw an exception", e);
		}

		logger.debug(">getNextDopYieldContractCommodityBerriesOutboxes");
		
		return results;
	}
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void processDopYieldContractCommodityBerriesOutbox(
			ClaimCalculationBerriesOutbox dopYieldContractCommodityBerriesOutbox,
		Boolean doPublishEvent,
		WebAdeAuthentication authentication
	) 
	throws ServiceException
	{
		logger.debug("<processDopYieldContractCommodityBerriesOutbox");

//TODO: PIM-2509
//		try {
//
//			if ( doPublishEvent.booleanValue() ) { 
//				String eventType = null;
//				DopYieldContractSimpleRsrc beforeDopYieldContractSimpleRsrc = null;
//				DopYieldContractSimpleRsrc afterDopYieldContractSimpleRsrc = null;
//				Map<String, String> sourceIdentifiers = new HashMap<>();
//					
//				if ( dopYieldContractCommodityBerriesOutbox.getTransactionType().equals(OutboxTransactionTypes.Insert) ) {
//					eventType = ClaimEventTypes.DopYieldContractCommodityBerriesCreated;
//					afterDopYieldContractSimpleRsrc = getDopYieldContractCommdityBerries(dopYieldContractCommodityBerriesOutbox.getClaimCalculationBerriesGuid());
//					sourceIdentifiers.put("declaredYieldContractCommodityBerriesGuid", afterDopYieldContractSimpleRsrc.getDopYieldContractCommodityBerries().getClaimCalculationBerriesGuid());
//						
//				} else if (dopYieldContractCommodityBerriesOutbox.getTransactionType().equals(OutboxTransactionTypes.Update) ) {
//					eventType = UnderwritingEventTypes.DopYieldContractCommodityBerriesUpdated;
//					afterDopYieldContractSimpleRsrc = getDopYieldContractCommdityBerries(dopYieldContractCommodityBerriesOutbox.getClaimCalculationBerriesGuid());
//					sourceIdentifiers.put("declaredYieldContractCommodityBerriesGuid", afterDopYieldContractSimpleRsrc.getDopYieldContractCommodityBerries().getClaimCalculationBerriesGuid());
//						
//				} else if (dopYieldContractCommodityBerriesOutbox.getTransactionType().equals(OutboxTransactionTypes.Delete) ) {
//					eventType = UnderwritingEventTypes.DopYieldContractCommodityBerriesDeleted;
//
//					// Since the delete has already happened, no resource is included in the event.
//					sourceIdentifiers.put("declaredYieldContractCommodityBerriesGuid", dopYieldContractCommodityBerriesOutbox.getClaimCalculationBerriesGuid());
//						
//				} else { 
//					throw new ServiceException("Declared Yield Contract Commodity Berries Outbox returned invalid transaction type");
//				}
//
//				// Delete Outbox record before publishing event. If the publish fails, the exception 
//				// rolls back the delete.
//				deleteClaimCalculationBerriesOutbox(dopYieldContractCommodityBerriesOutbox.getClaimCalculationBerriesOutboxId());
//				publishDopYieldContractSimple(eventType, beforeDopYieldContractSimpleRsrc, afterDopYieldContractSimpleRsrc, sourceIdentifiers);
//			} else {
//				// Not publishing an event because it would be a duplicate, so just delete the outbox record.
//				deleteClaimCalculationBerriesOutbox(dopYieldContractCommodityBerriesOutbox.getClaimCalculationBerriesOutboxId());
//			}
//
//		} catch (NotFoundException e) {
//			// If cropId does not exist, then there must be a delete event that will be processed later.
//			// So we can ignore this insert/update event and just delete the outbox record.
//			logger.info("Skipped insert/update event for declaredYieldContractCommodityBerriesGuid " + dopYieldContractCommodityBerriesOutbox.getClaimCalculationBerriesGuid() + " as it no longer exists.");
//			try { 
//				deleteClaimCalculationBerriesOutbox(dopYieldContractCommodityBerriesOutbox.getClaimCalculationBerriesOutboxId());
//			} catch (DaoException e2) { 
//				throw new ServiceException("DAO threw an exception", e2);
//			}
//
//		} catch (DaoException e) {
//			throw new ServiceException("DAO threw an exception", e);
//		} catch (EventPublisherException e) {
//			throw new ServiceException("Event Publisher threw an exception", e);
//		}
		
		logger.debug(">processDopYieldContractCommodityBerriesOutbox");
	}
	
	public boolean publishAndDelete = true;
	
	//Only set to false in certain unit tests
	public void setPublishAndDelete(boolean publishAndDelete) {
		logger.debug("<setPublishAndDelete");
		
		this.publishAndDelete = publishAndDelete;
		
		logger.debug(">setPublishAndDelete");
	}

//TODO: PIM-2509
//	public void publishDopYieldContractSimple(String eventType, DopYieldContractSimpleRsrc beforeDopYieldContractSimpleRsrc,
//			DopYieldContractSimpleRsrc afterDopYieldContractSimpleRsrc, Map<String, String> sourceIdentifiers)
//			throws EventPublisherException {
//		if(publishAndDelete) {
//			eventPublisher.publish(eventType, beforeDopYieldContractSimpleRsrc, afterDopYieldContractSimpleRsrc, sourceIdentifiers);
//		} else {
//			logger.info("Message not published because publishAndDelete is set to false. sourceIdentifier: " + sourceIdentifiers.values().stream()
//                    .findFirst()
//                    .orElse("No sourceIdentifier found"));
//		}
//
//	}

	public void deleteClaimCalculationBerriesOutbox(
			Integer declaredYieldContractCommodityBerriesOutboxId) throws DaoException, NotFoundDaoException {
		if(publishAndDelete) {
			declaredYieldContractCommodityBerriesOutboxDao.delete(declaredYieldContractCommodityBerriesOutboxId);
		} else {
			logger.info("Record not deleted because publishAndDelete is set to false. declaredYieldContractCommodityBerriesOutboxId: " + declaredYieldContractCommodityBerriesOutboxId.toString());
		}
	}

//TODO: PIM-2509
//	private DopYieldContractSimpleRsrc getDopYieldContractCommdityBerries(String declaredYieldContractCommodityBerriesGuid) throws ServiceException, NotFoundException {
//		logger.debug("<getDopYieldContractCommdityBerries");
//			
//		DopYieldContractSimpleRsrc result = null;
//
//		try {
//			ClaimCalculationBerriesDto berriesDto = declaredYieldContractCommodityBerriesDao.fetch(declaredYieldContractCommodityBerriesGuid);
//				
//			if(berriesDto == null) {
//				throw new NotFoundException("no declared yield contract commodity berries record found for " + declaredYieldContractCommodityBerriesGuid);
//			}
//				
//			result = dopYieldContractSimpleRsrcFactory.getDopYieldContractSimple(berriesDto);
//		} catch (DaoException e) {
//			throw new ServiceException("DAO threw an exception", e);
//		}
//			
//		logger.debug(">getDopYieldContractCommdityBerries");
//		return result;
//	}



}
