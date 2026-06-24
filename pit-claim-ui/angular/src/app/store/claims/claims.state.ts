import {SearchState} from "@wf1/wfcc-core-lib";
import {vmClaimList} from "../../conversion/models";
import {getDefaultPagingInfoRequest} from "../application/application.state";

export const SEARCH_CLAIMS_COMPONENT_ID = "searchClaims";
export const CLAIMS_COMPONENT_ID = "claims";

const EMPTY_CLAIMS: vmClaimList = {
  pageNumber: null,
  pageRowCount: null,
  totalPageCount: null,
  totalRowCount: null,
  collection: []
};

export const initialClaimsSearchState: SearchState = {
  query: null,
  sortParam: "claimNumber",
  sortDirection: "ASC",
  sortModalVisible: false,
  filters: {},
  hiddenFilters: {},
  componentId: SEARCH_CLAIMS_COMPONENT_ID
};

export const initClaimsPaging = getDefaultPagingInfoRequest(1, 20, "claimNumber", "ASC", undefined);

export interface ClaimsState {
  claims?: vmClaimList;  
}

export function getDefaultClaimsState(): ClaimsState {
  return {
    claims: EMPTY_CLAIMS,      
  };
}
