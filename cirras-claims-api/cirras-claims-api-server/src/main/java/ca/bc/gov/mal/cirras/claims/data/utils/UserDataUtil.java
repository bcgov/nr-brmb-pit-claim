package ca.bc.gov.mal.cirras.claims.data.utils;

import org.springframework.security.core.Authentication;

import com.microsoft.graph.models.ServicePrincipal;
import com.microsoft.graph.models.User;
import com.microsoft.graph.serviceclient.GraphServiceClient;

public class UserDataUtil {
	
    private GraphServiceClient graphServiceClient;

	public void setGraphServiceClient(GraphServiceClient graphServiceClient) {
		this.graphServiceClient = graphServiceClient;
	}
    
    //Returns IDIR for Users and Display Name for Applications
    public String getAuditUser(Authentication authentication) {
    	String auditUser = "Not Set";
        String upn = AuthenticationUtil.toJwt(authentication).getClaimAsString("upn");
        if (upn == null) {
        	//upn is null if an application is calling the api
        	String appid = AuthenticationUtil.toJwt(authentication).getClaimAsString("appid");
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
        return auditUser;
    }

}
