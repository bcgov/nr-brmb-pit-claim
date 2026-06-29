<%@ page import="org.springframework.context.ApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="java.util.Properties" %>
<%@ page import="ca.bc.gov.mal.pit.claim.util.EnvironmentVariable" %>

<%
  ApplicationContext context =  WebApplicationContextUtils.getWebApplicationContext(application);
  Properties properties = (Properties)context.getBean("applicationProperties");
  if (properties != null) {
    StringBuffer url = request.getRequestURL();
    String uri = request.getRequestURI();
    String ctx = request.getContextPath();
    String baseUrl = EnvironmentVariable.getVariable("BASE_URL");

    StringBuilder json = new StringBuilder("{");

    // General Application Section
    json = json.append("\"application\":{");
    json = json.append("\"acronym\":\"").append(properties.getProperty("project.acronym", "")).append("\"").append(",");
    json = json.append("\"version\":\"").append(properties.getProperty("application.version", "")).append("\"").append(",");
    json = json.append("\"environment\":\"").append("LOCAL".toUpperCase()).append("\"").append(",");
	  json = json.append("\"baseUrl\":\"").append(baseUrl).append("\"");
    json = json.append("},");

    // REST API Section
    String pitClaimRestUri = "http://localhost:8080/cirras-claims-api-server-2.5.3-SNAPSHOT";	
    if (pitClaimRestUri.endsWith("/")) {
      pitClaimRestUri = pitClaimRestUri.substring(0, pitClaimRestUri.length() - 1); //Strip off trailing slash, if it exists.
    }

    String pitUnderwritingUiUrl = "https://cirras-underwriting-ui-route-dev-a12541-dev.apps.silver.devops.gov.bc.ca/pub/cirras-underwriting";
    if (pitUnderwritingUiUrl.endsWith("/")) {
      pitUnderwritingUiUrl = pitUnderwritingUiUrl.substring(0, pitUnderwritingUiUrl.length() - 1); //Strip off trailing slash, if it exists.
    }

    json = json.append("\"rest\":{");
    json = json.append("\"cirras_claims\":\"").append(pitClaimRestUri).append("\"").append(",");
    json = json.append("\"pit_underwriting_ui\":\"").append(pitUnderwritingUiUrl).append("\"");
    json = json.append("},");

    String TENANT_ID = EnvironmentVariable.getVariable("TENANT_ID");
    String CLIENT_ID = EnvironmentVariable.getVariable("CLIENT_ID");
    String WEBADE_OAUTH2_AUTHORIZE_URL = "https://login.microsoftonline.com/" + TENANT_ID + "/oauth2/v2.0/authorize";
    String WEBADE_OAUTH2_ENABLE_CHECKTOKEN = "true";
    String UI_CHECKTOKEN_ENDPOINT = "http://localhost:8080/cirras-claims-api-server-2.5.3-SNAPSHOT/checkToken";
    String WEBADE_CHECK_TOKEN_URL = EnvironmentVariable.getVariable("WEBADE_CHECK_TOKEN_URL");
    String WEBADE_OAUTH2_SITEMINDER_URL = EnvironmentVariable.getVariable("WEBADE_OAUTH2_SITEMINDER_URL");
    String WEBADE_OAUTH2_SCOPES = CLIENT_ID + "/.default";
    String WEBADE_GET_TOKEN_URL = EnvironmentVariable.getVariable("WEBADE_GET_TOKEN_URL");

    // WebADE OAuth Section
    json = json.append("\"webade\":{");
    json = json.append("\"oauth2Url\":\"").append(WEBADE_OAUTH2_AUTHORIZE_URL).append("\"").append(",");	
    json = json.append("\"clientId\":\"").append(CLIENT_ID).append("\"").append(",");
    json = json.append("\"enableCheckToken\":").append(WEBADE_OAUTH2_ENABLE_CHECKTOKEN).append(",");	
    json = json.append("\"checkTokenUrl\":\"").append(UI_CHECKTOKEN_ENDPOINT).append("\"").append(",");	
	  json = json.append("\"siteminderUrlPrefix\":\"").append(WEBADE_OAUTH2_SITEMINDER_URL).append("\"").append(",");		
    json = json.append("\"authScopes\":\"").append(WEBADE_OAUTH2_SCOPES).append("\"");	
    json = json.append("}");


    json = json.append("}");
    out.write(json.toString());
  } else {
    out.write("{}");
  } 
%>
