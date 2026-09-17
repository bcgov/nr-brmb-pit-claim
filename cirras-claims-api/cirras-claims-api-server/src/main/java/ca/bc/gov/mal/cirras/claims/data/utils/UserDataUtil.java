package ca.bc.gov.mal.cirras.claims.data.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

import com.microsoft.graph.models.ServicePrincipal;
import com.microsoft.graph.models.User;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import com.microsoft.kiota.http.OkHttpRequestAdapter;
import java.lang.reflect.Field;
import com.microsoft.kiota.RequestAdapter;
import com.microsoft.kiota.authentication.AuthenticationProvider;

import ca.bc.gov.mal.cirras.claims.services.utils.CirrasServiceHelper;

public class UserDataUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(UserDataUtil.class);

    private GraphServiceClient graphServiceClient;

	public void setGraphServiceClient(GraphServiceClient graphServiceClient) {
		this.graphServiceClient = graphServiceClient;
	}

    
    //Returns IDIR for Users and Display Name for Applications
    public String getAuditUser(Authentication authentication) {
    	
    	logger.debug("<getAuditUser");
    	
    	String auditUser = "Not Set";
        String upn = AuthenticationUtil.getUserId(authentication);
        if (upn == null) {
        	//upn is null if an application is calling the api
        	String appid = AuthenticationUtil.toJwt(authentication).getClaimAsString("appid");
        	logger.debug("appid: " + appid);
        	logger.debug("aud: " + AuthenticationUtil.toJwt(authentication).getClaimAsString("aud"));
        	
        	if(appid == null) {
        		auditUser = "No appid found"; //Todo
        	} else {
        		
         		//auditUser = "Service Account";
        		ServicePrincipal app = graphServiceClient.servicePrincipals()
        		        .byServicePrincipalId(appid)
        		        .get(requestConfiguration -> {
        		            requestConfiguration.queryParameters.select = new String[]{
        		                "displayName"
        		            };
        		        });
        		
        		logger.debug("app: " + app);
        		
//        		AppRoleAssignmentCollectionResponse app = 
//        				graphServiceClient.servicePrincipals()
//        					.byServicePrincipalId(appid)
//        					.appRoleAssignedTo()
//        					.get(requestConfiguration -> {
//		                          requestConfiguration.queryParameters.select = new String[]{
//		                              "appId", 
//		                              "displayName"
//		                          };
//		                      });
            
        	
        	if(app != null && app.getDisplayName() != null ) {
        		auditUser = app.getDisplayName();
        	} else {
        		auditUser = appid;
        	}
//TODO: Wait for permission approval in Entra
//            	Application app = graphServiceClient.applications()
//                        .byApplicationId(appid)
//                        .get(requestConfiguration -> {
//                            requestConfiguration.queryParameters.select = new String[]{
//                                "id", 
//                                "appId", 
//                                "displayName", 
//                                "requiredResourceAccess"
//                            };
//                        });
//            	if(app != null && app.getDisplayName() != null) {
//            		auditUser = app.getDisplayName();
//            	} else {
//            		auditUser = appid;
//            	}
        	}
        } else {
    		auditUser = "User Account";

//        	User user = graphServiceClient.users()
//                    .byUserId(upn)
//                    .get(requestConfiguration -> {
//                        requestConfiguration.queryParameters.select = new String[]{"onPremisesSamAccountName"};
//                    });
//        	if (user != null && user.getOnPremisesSamAccountName() != null) {
//        		auditUser = user.getOnPremisesSamAccountName();
//        	}
        }
        
    	logger.debug(">getAuditUser");

        return auditUser;
    }

}
