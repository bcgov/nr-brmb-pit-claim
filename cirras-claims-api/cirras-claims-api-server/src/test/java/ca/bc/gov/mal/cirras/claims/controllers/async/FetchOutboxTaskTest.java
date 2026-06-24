package ca.bc.gov.mal.cirras.claims.controllers.async;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.claims.clients.CirrasClaimService;
import ca.bc.gov.mal.cirras.claims.clients.CirrasClaimServiceException;
import ca.bc.gov.mal.cirras.claims.controllers.scopes.Scopes;
import ca.bc.gov.mal.cirras.claims.data.resources.EndpointsRsrc;
import ca.bc.gov.mal.cirras.claims.test.EndpointsTest;
import ca.bc.gov.nrs.wfone.common.persistence.dao.DaoException;
import ca.bc.gov.nrs.wfone.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.nrs.wfone.common.webade.oauth2.token.client.Oauth2ClientException;
import jakarta.mail.internet.AddressException;


public class FetchOutboxTaskTest extends EndpointsTest {
	private static final Logger logger = LoggerFactory.getLogger(FetchOutboxTaskTest.class);

	private static final String[] SCOPES = {
			Scopes.GET_TOP_LEVEL
	};
		
	private CirrasClaimService service;
	private EndpointsRsrc topLevelEndpoints;
	
	
	@Before
	public void prepareTests() throws CirrasClaimServiceException, Oauth2ClientException, NotFoundDaoException, DaoException{
		service = getService(SCOPES);
		topLevelEndpoints = service.getTopLevelEndpoints();

		delete();
	}

	@After 
	public void cleanUp() throws NotFoundDaoException, DaoException {
		delete();
	}

		
	private void delete() throws NotFoundDaoException, DaoException {
	}

	// Ensure that enableAsyncProcs=true in super class before running this test.
	@Test
	public void testFetchOutboxTask() throws InterruptedException, AddressException {
		logger.debug("<testFetchOutboxTask");
		
		if(skipTests) {
			logger.warn("Skipping tests");
			return;
		}

		Assert.assertFalse(enableAsyncProcs);
		
		// Not really a unit test, but can use to just run the web server for x minutes. Useful for testing the AsynchronousProcessesService and its associated threads.
		synchronized (this) { 
			this.wait(1*60*1000);  // 10 minutes.
		}


		FetchOutboxTask fetchOutboxTask = (FetchOutboxTask)webApplicationContext.getBean("fetchOutboxTask");

		Assert.assertNotNull(fetchOutboxTask);

		//set to false to keep the records and don't push to the queue
		fetchOutboxTask.setCirrasClaimsOutboxServicePublishAndDelete(false);
		fetchOutboxTask.init();
		fetchOutboxTask.run();
		
		logger.debug(">testFetchOutboxTask");
	}
	

}
