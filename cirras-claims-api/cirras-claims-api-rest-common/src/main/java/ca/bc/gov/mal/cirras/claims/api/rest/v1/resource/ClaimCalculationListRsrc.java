package ca.bc.gov.mal.cirras.claims.api.rest.v1.resource;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.nrs.common.wfone.rest.resource.PagedResource;
import ca.bc.gov.mal.cirras.claims.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.mal.cirras.claims.model.v1.ClaimCalculationList;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.CLAIM_CALCULATION_LIST_NAME)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({ @Type(value = ClaimCalculationListRsrc.class, name = ResourceTypes.CLAIM_CALCULATION_LIST) })
public class ClaimCalculationListRsrc extends PagedResource implements ClaimCalculationList<ClaimCalculationRsrc> {
	private static final long serialVersionUID = 1L;

	private List<ClaimCalculationRsrc> collection = new ArrayList<ClaimCalculationRsrc>(0);

	public ClaimCalculationListRsrc() {
		collection = new ArrayList<ClaimCalculationRsrc>();
	}

	@Override
	public List<ClaimCalculationRsrc> getCollection() {
		return collection;
	}

	@Override
	public void setCollection(List<ClaimCalculationRsrc> collection) {
		this.collection = collection;
	}
}