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
    json = json.append("\"environment\":\"").append(EnvironmentVariable.getVariable("APPLICATION_ENVIRONMENT_NAME").toUpperCase()).append("\"").append(",");
	  json = json.append("\"baseUrl\":\"").append(baseUrl).append("\"");
    json = json.append("},");

    // REST API Section
    String pitClaimRestUri = EnvironmentVariable.getVariable("CIRRAS_CLAIMS_REST_URI");	
    if (pitClaimRestUri.endsWith("/")) {
      pitClaimRestUri = pitClaimRestUri.substring(0, pitClaimRestUri.length() - 1); //Strip off trailing slash, if it exists.
    }

    String pitUnderwritingUiUrl = EnvironmentVariable.getVariable("PIT_UNDERWRITING_UI_URL");
    if (pitUnderwritingUiUrl.endsWith("/")) {
      pitUnderwritingUiUrl = pitUnderwritingUiUrl.substring(0, pitUnderwritingUiUrl.length() - 1); //Strip off trailing slash, if it exists.
    }

    json = json.append("\"rest\":{");
    json = json.append("\"cirras_claims\":\"").append(pitClaimRestUri).append("\"");
    json = json.append("\"pit_underwriting_ui\":\"").append(pitUnderwritingUiUrl).append("\"");
    json = json.append("},");

    String WEBADE_OAUTH2_AUTHORIZE_URL = EnvironmentVariable.getVariable("WEBADE_OAUTH2_AUTHORIZE_URL");
    String WEBADE_OAUTH2_ENABLE_CHECKTOKEN = EnvironmentVariable.getVariable("WEBADE_OAUTH2_ENABLE_CHECKTOKEN");
    String LOCAL_CHECKTOKEN_ENDPOINT = EnvironmentVariable.getVariable("LOCAL_CHECKTOKEN_ENDPOINT");
    String WEBADE_CHECK_TOKEN_URL = EnvironmentVariable.getVariable("WEBADE_CHECK_TOKEN_URL");
    String WEBADE_OAUTH2_SITEMINDER_URL = EnvironmentVariable.getVariable("WEBADE_OAUTH2_SITEMINDER_URL");
    String WEBADE_OAUTH2_SCOPES = EnvironmentVariable.getVariable("WEBADE_OAUTH2_SCOPES");
    String WEBADE_GET_TOKEN_URL = EnvironmentVariable.getVariable("WEBADE_GET_TOKEN_URL");

    // WebADE OAuth Section
    json = json.append("\"webade\":{");
    json = json.append("\"oauth2Url\":\"").append(WEBADE_OAUTH2_AUTHORIZE_URL).append("\"").append(",");	
    json = json.append("\"clientId\":\"").append("CIRRAS_CLAIMS_UI").append("\"").append(",");
    json = json.append("\"enableCheckToken\":").append(WEBADE_OAUTH2_ENABLE_CHECKTOKEN).append(",");	
    json = json.append("\"checkTokenUrl\":\"").append(LOCAL_CHECKTOKEN_ENDPOINT).append("\"").append(",");	
	  json = json.append("\"siteminderUrlPrefix\":\"").append(WEBADE_OAUTH2_SITEMINDER_URL).append("\"").append(",");		
    json = json.append("\"authScopes\":\"").append(WEBADE_OAUTH2_SCOPES).append("\"");	
    json = json.append("}");


    json = json.append("}");
    out.write(json.toString());
  } else {
    out.write("{}");
  } 
%>
