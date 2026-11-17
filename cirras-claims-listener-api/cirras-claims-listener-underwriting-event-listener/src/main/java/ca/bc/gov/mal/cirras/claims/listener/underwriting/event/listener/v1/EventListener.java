package ca.bc.gov.mal.cirras.claims.listener.underwriting.event.listener.v1;

public interface EventListener {
	
	public String getProcessName();

	public void startListening();
	
	public void stopListening();

}
