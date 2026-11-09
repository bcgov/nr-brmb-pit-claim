package ca.bc.gov.mal.cirras.claims.listener.api.rest.client.v1;

public class PayrollEmployeeSyncServiceException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public PayrollEmployeeSyncServiceException(String message) {
		super(message);
	}

	public PayrollEmployeeSyncServiceException(Throwable cause) {
		super(cause);
	}

	public PayrollEmployeeSyncServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
