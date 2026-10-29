package ca.bc.gov.mal.cirras.claims.listener.api.rest.client.v1.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.mal.cirras.claims.listener.api.rest.client.v1.CirrasClaimsListenerService;
import ca.bc.gov.nrs.wfone.common.rest.client.BaseRestServiceClient;

public class CirrasClaimsListenerServiceImpl extends BaseRestServiceClient implements CirrasClaimsListenerService {

	private static final Logger logger = LoggerFactory.getLogger(CirrasClaimsListenerServiceImpl.class);
	
	public static final String CLIENT_VERSION = "1";

	private static final String Scopes = "WFHR.*";
	
	/**
	 * Constructor used for making OAuth2 Client Credentials requests
	 * @param webadeOauth2ClientId
	 * @param webadeOauth2ClientSecret
	 * @param webadeOauth2TokenUrl
	 */
	public CirrasClaimsListenerServiceImpl(String webadeOauth2ClientId, String webadeOauth2ClientSecret, String webadeOauth2TokenUrl) {
		super(webadeOauth2ClientId, webadeOauth2ClientSecret, webadeOauth2TokenUrl, Scopes);
		logger.debug("<PayrollEmployeeSyncServiceImpl");
		
		logger.debug(">PayrollEmployeeSyncServiceImpl");
	}
	
	/**
	 * Constructor used for making requests with basic credentials
	 * 
	 * @param headerValue
	 */
	public CirrasClaimsListenerServiceImpl(String webadeOauth2ClientId, String webadeOauth2ClientSecret) {
		super(webadeOauth2ClientId, webadeOauth2ClientSecret);
		logger.debug("<PayrollEmployeeSyncServiceImpl");
		
		logger.debug(">PayrollEmployeeSyncServiceImpl");
	}
	
	/**
	 * Constructor used for making requests with basic credentials
	 * 
	 * @param webadeOauth2ClientId
	 * @param webadeOauth2ClientSecret
	 */
	public CirrasClaimsListenerServiceImpl(String headerValue) {
		super(headerValue);
		logger.debug("<PayrollEmployeeSyncServiceImpl");
		
		logger.debug(">PayrollEmployeeSyncServiceImpl");
	}
	
	/**
	 * Constructor used for making requests using the authorization header of the current HttpServletRequest
	 * 
	 */
	public CirrasClaimsListenerServiceImpl() {
		super();
		logger.debug("<PayrollEmployeeSyncServiceImpl");
		
		logger.debug(">PayrollEmployeeSyncServiceImpl");
	}
	
	@Override
	public String getClientVersion() {
		return CLIENT_VERSION;
	}

	protected static String toQueryParam(String value) throws UnsupportedEncodingException {
		String result = null;
		if(value != null) {
			result = URLEncoder.encode(value, "UTF-8");
		}
		return result;
	}
	
	protected static String toQueryParam(LocalDate value) {
		String result = null;
		if(value != null) {
			result = value.format(DateTimeFormatter.ISO_LOCAL_DATE);
		}
		return result;
	}

	protected static String toQueryParam(Number value) {
		String result = null;
		
		if(value!=null) {
			result = value.toString();
		}
		
		return result;
	}

	protected static String toQueryParam(Boolean value) {
		String result = null;
		
		if(value!=null) {
			result = value.toString();
		}
		
		return result;
	}
}
