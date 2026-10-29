package ca.bc.gov.mal.cirras.claims.listener.model.v1;

public enum SubmissionListenerType {
	InvalidMessageFormat,
	InvalidMessageContent,
	ApplicationError,
	Success,
	ValidationError, 
	RetryMessageLater
}
