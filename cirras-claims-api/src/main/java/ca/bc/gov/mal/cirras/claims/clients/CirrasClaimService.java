package ca.bc.gov.mal.cirras.claims.clients;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AuthorizationServiceException;

import ca.bc.gov.mal.cirras.claims.data.resources.EndpointsRsrc;
import ca.bc.gov.mal.cirras.claims.data.resources.SyncClaimRsrc;
import ca.bc.gov.mal.cirras.claims.data.resources.SyncCodeRsrc;
import ca.bc.gov.mal.cirras.claims.data.resources.SyncCommodityVarietyRsrc;
import ca.bc.gov.mal.cirras.claims.data.resources.SyncCoveragePerilRsrc;
import ca.bc.gov.mal.cirras.claims.data.resources.ClaimListRsrc;
import ca.bc.gov.mal.cirras.claims.data.resources.ClaimRsrc;
import ca.bc.gov.mal.cirras.claims.data.resources.ClaimCalculationListRsrc;
import ca.bc.gov.mal.cirras.claims.data.resources.ClaimCalculationRsrc;

import ca.bc.gov.mal.cirras.claims.data.resources.types.ResourceTypes;
import ca.bc.gov.nrs.common.wfone.rest.resource.BaseResource;
import ca.bc.gov.nrs.common.wfone.rest.resource.CodeTableListRsrc;
import ca.bc.gov.nrs.common.wfone.rest.resource.CodeTableRsrc;
import ca.bc.gov.nrs.common.wfone.rest.resource.RelLink;
import ca.bc.gov.nrs.wfone.common.rest.client.BadRequestException;
import ca.bc.gov.nrs.wfone.common.rest.client.BaseRestServiceClient;
import ca.bc.gov.nrs.wfone.common.rest.client.GenericRestDAO;
import ca.bc.gov.nrs.wfone.common.rest.client.Response;
import ca.bc.gov.nrs.wfone.common.rest.client.RestDAOException;

public class CirrasClaimService extends BaseRestServiceClient {

	private static final Logger logger = LoggerFactory.getLogger(CirrasClaimService.class);
	
	public static final String CLIENT_VERSION = "1";

	private static final String Scopes = "WEBADE-REST.*";
	
	/**
	 * Constructor used for making OAuth2 Client Credentials requests
	 * @param webadeOauth2ClientId
	 * @param webadeOauth2ClientSecret
	 * @param webadeOauth2TokenUrl
	 */
	public CirrasClaimService(String webadeOauth2ClientId, String webadeOauth2ClientSecret, String webadeOauth2TokenUrl, String scopes) {
		super(webadeOauth2ClientId, webadeOauth2ClientSecret, webadeOauth2TokenUrl, scopes != null ? scopes : Scopes);
		logger.debug("<CirrasClaimServiceImpl");
		
		logger.debug(">CirrasClaimServiceImpl");
	}

	/**
	 * Constructor used for making requests with basic credentials
	 *
	 * @param webadeOauth2ClientId
	 * @param webadeOauth2ClientSecret
	 */
	public CirrasClaimService(String webadeOauth2ClientId, String webadeOauth2ClientSecret) {
		super(webadeOauth2ClientId, webadeOauth2ClientSecret);
		logger.debug("<CirrasClaimServiceImpl");
		
		logger.debug(">CirrasClaimServiceImpl");
	}

	/**
	 * Constructor used for making requests with basic credentials
	 *
	 * @param headerValue
	 */
	public CirrasClaimService(String headerValue) {
		super(headerValue);
		logger.debug("<CirrasClaimServiceImpl");
		
		logger.debug(">CirrasClaimServiceImpl");
	}
	
	/**
	 * Constructor used for making requests using the authorization header of the current HttpServletRequest
	 * 
	 */
	public CirrasClaimService() {
		super();
		logger.debug("<CirrasClaimServiceImpl");
		
		logger.debug(">CirrasClaimServiceImpl");
	}

	
	public String getClientVersion() {
		return CLIENT_VERSION;
	}
	
	
	
	public EndpointsRsrc getTopLevelEndpoints() throws CirrasClaimServiceException {
		EndpointsRsrc result = null;

		try {
			String topLevelRestURL = getTopLevelRestURL();
			
			GenericRestDAO<EndpointsRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(EndpointsRsrc.class);
			Response<EndpointsRsrc> response = dao.Process(
				ResourceTypes.ENDPOINTS, this.getTransformer(), new BaseResource() {

					private static final long serialVersionUID = 1L;

					@Override
					public List<RelLink> getLinks() {
						List<RelLink> links = new ArrayList<RelLink>();
						links.add(new RelLink(ResourceTypes.ENDPOINTS, topLevelRestURL, "GET"));
						return links;
					}
				}, getWebClient());

			if (404 == response.getResponseCode()) {
				throw new AuthorizationServiceException("Failed to find toplevel at '" + getTopLevelRestURL() + "'");
			}

			result = response.getResource();

		} catch (RestDAOException e) {
			e.printStackTrace();
			throw new CirrasClaimServiceException(e);
			
		} catch (Throwable t) {
			t.printStackTrace();
			throw new CirrasClaimServiceException(t);
		}

		return result;
	}
	

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	// code tables
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	public CodeTableListRsrc getCodeTables(
			EndpointsRsrc parent,
			String codeTableName, 
			LocalDate effectiveAsOfDate) throws CirrasClaimServiceException {
		logger.debug("<getCodeTables");

		CodeTableListRsrc result = null;

		try {

			Map<String, String> queryParams = new HashMap<String, String>();
			putQueryParam(queryParams, "codeTableName", codeTableName);
			putQueryParam(queryParams, "effectiveAsOfDate", toQueryParam(effectiveAsOfDate));

			GenericRestDAO<CodeTableListRsrc> dao = this.getRestDAOFactory()
					.getGenericRestDAO(CodeTableListRsrc.class);
			Response<CodeTableListRsrc> response = dao.Process(
					ResourceTypes.CODE_TABLE_LIST, this.getTransformer(), parent, queryParams,
					getWebClient());

			result = response.getResource();

		} catch (RestDAOException e) {
			logger.error(e.getMessage(), e);
			throw new CirrasClaimServiceException(e);
		}

		logger.debug(">getCodeTables");
		return result;
	}

	
	public CodeTableRsrc getCodeTable(CodeTableRsrc codeTable, LocalDate effectiveAsOfDate) throws CirrasClaimServiceException {
		logger.debug("<getCodeTable");

		CodeTableRsrc result = null;

		try {

			Map<String, String> queryParams = new HashMap<String, String>();
			putQueryParam(queryParams, "effectiveAsOfDate", toQueryParam(effectiveAsOfDate));

			GenericRestDAO<CodeTableRsrc> dao = this.getRestDAOFactory()
					.getGenericRestDAO(CodeTableRsrc.class);
			Response<CodeTableRsrc> response = dao.Process(
					ResourceTypes.SELF, this.getTransformer(), codeTable, queryParams, getWebClient());

			result = response.getResource();

		} catch (RestDAOException e) {
			logger.error(e.getMessage(), e);
			throw new CirrasClaimServiceException(e);
		}

		logger.debug(">getCodeTable");
		return result;
	}

	
	public CodeTableRsrc updateCodeTable(CodeTableRsrc codeTable)
			throws CirrasClaimServiceException, ValidationException {

		GenericRestDAO<CodeTableRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(CodeTableRsrc.class);
		
		try {
			Response<CodeTableRsrc> response = dao.Process(ResourceTypes.UPDATE_CODE_TABLE, this.getTransformer(), codeTable, getWebClient());
			return response.getResource();
			
		} catch(BadRequestException e) {
			throw new ValidationException(e.getMessages());
		} catch (RestDAOException rde) {
			logger.error(rde.getMessage(), rde);
			throw new CirrasClaimServiceException(rde);
		}
	}

	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	//claims
	/////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	public ClaimListRsrc getClaimList(
			EndpointsRsrc parent,
			String claimNumber, 
			String policyNumber, 
			String calculationStatusCode,
			String sortColumn,
			String sortDirection,
		    Integer pageNumber,
			Integer pageRowCount)
	throws CirrasClaimServiceException {

		
		
		GenericRestDAO<ClaimListRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(ClaimListRsrc.class);
		
		try {
		
			Map<String, String> queryParams = new HashMap<String, String>();

			putQueryParam(queryParams, "claimNumber",  claimNumber);
			putQueryParam(queryParams, "policyNumber",  policyNumber);
			putQueryParam(queryParams, "calculationStatusCode",  calculationStatusCode);
			putQueryParam(queryParams, "sortColumn",  sortColumn);
			putQueryParam(queryParams, "sortDirection",  sortDirection);
			putQueryParam(queryParams, "pageNumber", toQueryParam(pageNumber));
			putQueryParam(queryParams, "pageRowCount", toQueryParam(pageRowCount));
			
			//EndpointsRsrc parentEndpoint = getTopLevelEndpoints();
			
			Response<ClaimListRsrc> response = dao.Process(ResourceTypes.CLAIM_LIST, this.getTransformer(), parent, queryParams, getWebClient());
			
			return response.getResource();

		} catch (RestDAOException e) {
			throw new CirrasClaimServiceException(e);
		}
	}
	
	
	public ClaimCalculationRsrc getClaim(ClaimRsrc resource)
	throws CirrasClaimServiceException {
	
		GenericRestDAO<ClaimCalculationRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(ClaimCalculationRsrc.class);
		
		try {
			Response<ClaimCalculationRsrc> response = dao.Process(ResourceTypes.CLAIM, this.getTransformer(), resource, getWebClient());
			return response.getResource();
		} catch (RestDAOException rde) {
			throw new CirrasClaimServiceException(rde);
		}
	}	
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	//calculations
	/////////////////////////////////////////////////////////////////////////////////////////////////////

	
	public ClaimCalculationListRsrc getClaimCalculations(
		EndpointsRsrc parent,
		String claimNumber,
		String policyNumber,
		String cropYear,
		String calculationStatusCode,
		String createClaimCalcUserGuid,
		String updateClaimCalcUserGuid,
		String insurancePlanId,
		String sortColumn,
		String sortDirection,
		Integer pageNumber, 
		Integer pageRowCount)
	
	throws CirrasClaimServiceException {
	
		GenericRestDAO<ClaimCalculationListRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(ClaimCalculationListRsrc.class);
		
		try {
		
			Map<String, String> queryParams = new HashMap<String, String>();
			
			putQueryParam(queryParams, "claimNumber",  claimNumber);
			putQueryParam(queryParams, "policyNumber",  policyNumber);
			putQueryParam(queryParams, "cropYear",  cropYear);
			putQueryParam(queryParams, "calculationStatusCode",  calculationStatusCode);
			putQueryParam(queryParams, "createClaimCalcUserGuid",  createClaimCalcUserGuid);
			putQueryParam(queryParams, "updateClaimCalcUserGuid",  updateClaimCalcUserGuid);
			putQueryParam(queryParams, "insurancePlanId",  insurancePlanId);
			putQueryParam(queryParams, "sortColumn",  sortColumn);
			putQueryParam(queryParams, "sortDirection",  sortDirection);
			putQueryParam(queryParams, "pageNumber", toQueryParam(pageNumber));
			putQueryParam(queryParams, "pageRowCount", toQueryParam(pageRowCount));
			
			Response<ClaimCalculationListRsrc> response = dao.Process(ResourceTypes.CLAIM_CALCULATION_LIST, this.getTransformer(), parent, queryParams, getWebClient());
			
			return response.getResource();
	
		} catch (RestDAOException e) {
			throw new CirrasClaimServiceException(e);
		}
	}
	
	
	
	public ClaimCalculationRsrc createClaimCalculation(ClaimCalculationRsrc resource)
	throws CirrasClaimServiceException, ValidationException {
	
		EndpointsRsrc parentEndpoint = getTopLevelEndpoints();
		
		GenericRestDAO<ClaimCalculationRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(ClaimCalculationRsrc.class);
		
		try {
			Response<ClaimCalculationRsrc> response = dao.Process(ResourceTypes.CREATE_CLAIM_CALCULATION, this.getTransformer(), parentEndpoint, resource, getWebClient());			
			return response.getResource();			
		} catch(BadRequestException e) {
			throw new ValidationException(e.getMessages());			
		} catch (RestDAOException rde) {
			throw new CirrasClaimServiceException(rde);
		}
	}
	
	
	public ClaimCalculationRsrc getClaimCalculation(ClaimCalculationRsrc resource, Boolean doRefreshManualClaimData)
	throws CirrasClaimServiceException {
	
		GenericRestDAO<ClaimCalculationRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(ClaimCalculationRsrc.class);

		Map<String, String> queryParams = new HashMap<String, String>();
		putQueryParam(queryParams, "doRefreshManualClaimData", toQueryParam(doRefreshManualClaimData));
		
		try {
			Response<ClaimCalculationRsrc> response = dao.Process(ResourceTypes.SELF, this.getTransformer(), resource, queryParams, getWebClient());
			return response.getResource();
		} catch (RestDAOException rde) {
			throw new CirrasClaimServiceException(rde);
		}
	}
	
	
	public ClaimCalculationRsrc updateClaimCalculation(ClaimCalculationRsrc resource, String updateType)
	throws CirrasClaimServiceException, ValidationException {
	
		GenericRestDAO<ClaimCalculationRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(ClaimCalculationRsrc.class);

		Map<String, String> queryParams = new HashMap<String, String>();
		putQueryParam(queryParams, "updateType", updateType);
		
		try {
			Response<ClaimCalculationRsrc> response = dao.Process(ResourceTypes.UPDATE_CLAIM_CALCULATION, this.getTransformer(), resource, queryParams, getWebClient());
			return response.getResource();
		} catch(BadRequestException e) {
			throw new ValidationException(e.getMessages());
		} catch (RestDAOException rde) {
			throw new CirrasClaimServiceException(rde);
		}
	}
	
	
	public void deleteClaimCalculation(ClaimCalculationRsrc resource, Boolean doDeleteLinkedCalculations)
	throws CirrasClaimServiceException {
	
		GenericRestDAO<ClaimCalculationRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(ClaimCalculationRsrc.class);
		
		Map<String, String> queryParams = new HashMap<String, String>();
		putQueryParam(queryParams, "doDeleteLinkedCalculations", toQueryParam(doDeleteLinkedCalculations));
		
		try {
			dao.Process(ResourceTypes.DELETE_CLAIM_CALCULATION, this.getTransformer(), resource, queryParams, getWebClient());
		} catch (RestDAOException rde) {
			throw new CirrasClaimServiceException(rde);
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	// CIRRAS DATA SYNC 
	/////////////////////////////////////////////////////////////////////////////////////////////////////

	
	public SyncClaimRsrc createSyncClaim(SyncClaimRsrc resource)
			throws CirrasClaimServiceException, ValidationException {
		
		EndpointsRsrc parentEndpoint = getTopLevelEndpoints();
		
		GenericRestDAO<SyncClaimRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(SyncClaimRsrc.class);
		
		try {
			Response<SyncClaimRsrc> response = dao.Process(ResourceTypes.CREATE_SYNC_CLAIM, this.getTransformer(), parentEndpoint, resource, getWebClient());			
			return response.getResource();			
		} catch(BadRequestException e) {
			throw new ValidationException(e.getMessages());			
		} catch (RestDAOException rde) {
			throw new CirrasClaimServiceException(rde);
		}
	}


	
	public SyncClaimRsrc getSyncClaim(EndpointsRsrc parent, String colId) 
			throws CirrasClaimServiceException {
		
		GenericRestDAO<SyncClaimRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(SyncClaimRsrc.class);
		
		try {
		
			Map<String, String> queryParams = new HashMap<String, String>();
			
			putQueryParam(queryParams, "colId",  colId);
			
			Response<SyncClaimRsrc> response = dao.Process(ResourceTypes.SYNC_CLAIM, this.getTransformer(), parent, queryParams, getWebClient());
			
			return response.getResource();
	
		} catch (RestDAOException e) {
			throw new CirrasClaimServiceException(e);
		}		
	}

	
	public SyncClaimRsrc updateSyncClaim(SyncClaimRsrc resource)
			throws CirrasClaimServiceException, ValidationException {

		GenericRestDAO<SyncClaimRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(SyncClaimRsrc.class);

		try {
			Response<SyncClaimRsrc> response = dao.Process(ResourceTypes.UPDATE_SYNC_CLAIM, this.getTransformer(), resource, getWebClient());
			return response.getResource();
		} catch(BadRequestException e) {
			throw new ValidationException(e.getMessages());
		} catch (RestDAOException rde) {
			throw new CirrasClaimServiceException(rde);
		}
		
	}


	
	public void deleteSyncClaim(SyncClaimRsrc resource)
	throws CirrasClaimServiceException {
	
		GenericRestDAO<SyncClaimRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(SyncClaimRsrc.class);
		
		try {
			dao.Process(ResourceTypes.DELETE_SYNC_CLAIM, this.getTransformer(), resource, getWebClient());
		} catch (RestDAOException rde) {
			throw new CirrasClaimServiceException(rde);
		}
	}
	
	
	public void updateSyncClaimRelatedData(SyncClaimRsrc resource)
	throws CirrasClaimServiceException, ValidationException {
	
		EndpointsRsrc parentEndpoint = getTopLevelEndpoints();
		
		GenericRestDAO<SyncClaimRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(SyncClaimRsrc.class);
		
		try {
			dao.Process(ResourceTypes.UPDATE_SYNC_CLAIM_RELATED_DATA, this.getTransformer(), parentEndpoint, resource, getWebClient());			
						
		} catch(BadRequestException e) {
			throw new ValidationException(e.getMessages());			
		} catch (RestDAOException rde) {
			throw new CirrasClaimServiceException(rde);
		}
		
	}

	
	public void synchronizeCommodityVariety(SyncCommodityVarietyRsrc resource) throws CirrasClaimServiceException, ValidationException {
		
		EndpointsRsrc parentEndpoint = getTopLevelEndpoints();
		
		GenericRestDAO<SyncCommodityVarietyRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(SyncCommodityVarietyRsrc.class);
		
		try {
			dao.Process(ResourceTypes.SYNCHRONIZE_COMMODITY_VARIETY, this.getTransformer(), parentEndpoint, resource, getWebClient());			
						
		} catch(BadRequestException e) {
			throw new ValidationException(e.getMessages());			
		} catch (RestDAOException rde) {
			throw new CirrasClaimServiceException(rde);
		}
		
	}
		
	
	public void deleteSyncCommodityVariety(EndpointsRsrc parent, String crptId) throws CirrasClaimServiceException {
		
		GenericRestDAO<SyncClaimRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(SyncClaimRsrc.class);
		
		try {
		
			Map<String, String> queryParams = new HashMap<String, String>();
			
			putQueryParam(queryParams, "crptId",  crptId);
			
			dao.Process(ResourceTypes.DELETE_SYNC_COMMODITY_VARIETY, this.getTransformer(), parent, queryParams, getWebClient());
	
		} catch (RestDAOException e) {
			throw new CirrasClaimServiceException(e);
		}		
	}

	
	public void synchronizeCode(SyncCodeRsrc resource)
			throws CirrasClaimServiceException, ValidationException {

		EndpointsRsrc parentEndpoint = getTopLevelEndpoints();
		
		GenericRestDAO<SyncCodeRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(SyncCodeRsrc.class);
		
		try {
			dao.Process(ResourceTypes.SYNCHRONIZE_CODE, this.getTransformer(), parentEndpoint, resource, getWebClient());
						
		} catch(BadRequestException e) {
			throw new ValidationException(e.getMessages());			
		} catch (RestDAOException rde) {
			throw new CirrasClaimServiceException(rde);
		}
		
	}

	
	public void deleteSyncCode(EndpointsRsrc parent, String codeTableType, String uniqueKey)
			throws CirrasClaimServiceException {

		GenericRestDAO<SyncCodeRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(SyncCodeRsrc.class);
		
		try {
		
			Map<String, String> queryParams = new HashMap<String, String>();
			
			putQueryParam(queryParams, "codeTableType",  codeTableType);
			putQueryParam(queryParams, "uniqueKey",  uniqueKey);
			
			dao.Process(ResourceTypes.DELETE_SYNC_CODE, this.getTransformer(), parent, queryParams, getWebClient());
	
		} catch (RestDAOException e) {
			throw new CirrasClaimServiceException(e);
		}
		
	}
	
	
	public void synchronizeCoveragePeril(SyncCoveragePerilRsrc resource)
			throws CirrasClaimServiceException, ValidationException {

		EndpointsRsrc parentEndpoint = getTopLevelEndpoints();
		
		GenericRestDAO<SyncCoveragePerilRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(SyncCoveragePerilRsrc.class);
		
		try {
			dao.Process(ResourceTypes.SYNCHRONIZE_COVERAGE_PERIL, this.getTransformer(), parentEndpoint, resource, getWebClient());
						
		} catch(BadRequestException e) {
			throw new ValidationException(e.getMessages());			
		} catch (RestDAOException rde) {
			throw new CirrasClaimServiceException(rde);
		}
		
	}

	
	public void deleteSyncCoveragePeril(EndpointsRsrc parent, String coveragePerilId)
			throws CirrasClaimServiceException {

		GenericRestDAO<SyncCodeRsrc> dao = this.getRestDAOFactory().getGenericRestDAO(SyncCodeRsrc.class);
		
		try {
		
			Map<String, String> queryParams = new HashMap<String, String>();
			
			putQueryParam(queryParams, "coveragePerilId",  coveragePerilId);
			
			dao.Process(ResourceTypes.DELETE_SYNC_COVERAGE_PERIL, this.getTransformer(), parent, queryParams, getWebClient());
	
		} catch (RestDAOException e) {
			throw new CirrasClaimServiceException(e);
		}
		
	}	
}
