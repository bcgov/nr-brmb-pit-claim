package ca.bc.gov.mal.cirras.claims.clients;

public class CirrasClaimServiceException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public CirrasClaimServiceException(String message) {
		super(message);
	}

	public CirrasClaimServiceException(Throwable cause) {
		super(cause);
	}

	public CirrasClaimServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
