PIT CLAIM WAR project is now deprecated.

pit-claim-war project contains the front end of the claim calculation app, which used to be deployed using Tomcat server. 

We have made the following changes to the front end:
	- moved the check if the user is authenticated to the api
	- the front end is now served with server express. 
	
The new setup has now moved to pit-claim-ui. 
pit-claim-ui has all code changes from pit-claim-war in feature/2.5.1
