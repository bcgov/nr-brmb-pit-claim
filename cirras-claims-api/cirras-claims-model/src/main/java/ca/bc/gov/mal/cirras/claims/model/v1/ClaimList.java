package ca.bc.gov.mal.cirras.claims.model.v1;

import java.io.Serializable;
import java.util.List;

public interface ClaimList<E extends Claim> extends Serializable {
	public List<E> getCollection();
	public void setCollection(List<E> collection);
}