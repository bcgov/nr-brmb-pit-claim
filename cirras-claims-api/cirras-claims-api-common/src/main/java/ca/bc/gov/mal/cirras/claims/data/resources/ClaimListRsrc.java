package ca.bc.gov.mal.cirras.claims.data.resources;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.nrs.common.wfone.rest.resource.PagedResource;
import ca.bc.gov.mal.cirras.claims.data.resources.types.ResourceTypes;
import ca.bc.gov.mal.cirras.claims.data.models.ClaimList;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.CLAIM_LIST_NAME)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({ @Type(value = ClaimListRsrc.class, name = ResourceTypes.CLAIM_LIST) })
public class ClaimListRsrc extends PagedResource implements ClaimList<ClaimRsrc> {
	private static final long serialVersionUID = 1L;

	private List<ClaimRsrc> collection = new ArrayList<ClaimRsrc>(0);

	public ClaimListRsrc() {
		collection = new ArrayList<ClaimRsrc>();
	}

	@Override
	public List<ClaimRsrc> getCollection() {
		return collection;
	}

	@Override
	public void setCollection(List<ClaimRsrc> collection) {
		this.collection = collection;
	}
}