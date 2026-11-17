package ca.bc.gov.mal.cirras.claims.listener.service.api.v1.impl;

import java.util.ArrayList;
import java.util.List;

public class CirrasClaimsListenerValidationException extends Exception {

	private static final long serialVersionUID = 1L;
	
	List<String> errors = new ArrayList<>();
	
	public CirrasClaimsListenerValidationException(List<String> errors) {
	
		this.errors = errors;
	}

	public List<String> getErrors() {
		return errors;
	}
}
