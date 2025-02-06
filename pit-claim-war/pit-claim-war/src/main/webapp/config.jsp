<%@ page import="org.springframework.context.ApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="java.util.Properties" %>

<%
  ApplicationContext context =  WebApplicationContextUtils.getWebApplicationContext(application);
  Properties properties = (Properties)context.getBean("applicationProperties");
  if (properties != null) {
    StringBuffer url = request.getRequestURL();
    String uri = request.getRequestURI();
    String ctx = request.getContextPath();
    String baseUrl = url.substring(0, url.length() - uri.length() + ctx.length()) + "/";

    StringBuilder json = new StringBuilder("{");

    // General Application Section
    json = json.append("\"application\":{");
    json = json.append("\"acronym\":\"").append(properties.getProperty("project.acronym", "")).append("\"").append(",");
    json = json.append("\"version\":\"").append(properties.getProperty("application.version", "")).append("\"").append(",");
    json = json.append("\"buildNumber\":\"").append(properties.getProperty("build.number", "")).append("\"").append(",");
    json = json.append("\"environment\":\"").append(properties.getProperty("default.application.environment", "")).append("\"").append(",");
    json = json.append("\"baseUrl\":\"").append(baseUrl).append("\"");
    json = json.append("},");

    // REST API Section
    String cirrasClaimsRestUri = properties.getProperty("cirras-claims-rest.url", "");	
    if (cirrasClaimsRestUri.endsWith("/")) {
      cirrasClaimsRestUri = cirrasClaimsRestUri.substring(0, cirrasClaimsRestUri.length() - 1); //Strip off trailing slash, if it exists.
    }

    json = json.append("\"rest\":{");
    json = json.append("\"cirras_claims\":\"").append(cirrasClaimsRestUri).append("\"");
    json = json.append("},");

    // WebADE OAuth Section
    json = json.append("\"webade\":{");
    json = json.append("\"oauth2Url\":\"").append(properties.getProperty("webade-oauth2.authorize.url", "")).append("\"").append(",");	
    json = json.append("\"clientId\":\"").append("CIRRAS_CLAIMS_UI").append("\"").append(",");
    json = json.append("\"enableCheckToken\":").append(properties.getProperty("cirras-claims-ui.oauth.enableCheckToken")).append(",");	
    json = json.append("\"checkTokenUrl\":\"").append(properties.getProperty("cirras-claims-ui.oauth.checkTokenUrl")).append("\"").append(",");	
	json = json.append("\"siteminderUrlPrefix\":\"").append(properties.getProperty("cirras-claims-ui.oauth.siteminder.url", "")).append("\"").append(",");		
    json = json.append("\"authScopes\":\"").append(properties.getProperty("cirras-claims-ui.oauth.scopes")).append("\"");	
    json = json.append("}");


    json = json.append("}");
    out.write(json.toString());
  } else {
    out.write("{}");
  }
%>
