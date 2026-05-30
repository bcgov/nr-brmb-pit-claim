import {SearchState} from "@wf1/wfcc-core-lib";
import {vmCalculationList} from "../../conversion/models";
import {getDefaultPagingInfoRequest} from "../application/application.state";

export const SEARCH_CALCULATIONS_COMPONENT_ID = "searchCalculations";
export const CALCULATIONS_COMPONENT_ID = "calculations";

const EMPTY_CALCULATIONS: vmCalculationList = {
  pageNumber: null,
  pageRowCount: null,
  totalPageCount: null,
  totalRowCount: null,
  collection: []
};

export const initialCalculationsSearchState: SearchState = {
  query: null,
  sortParam: "claimNumber",
  sortDirection: "ASC",
  sortModalVisible: false,
  filters: {},
  hiddenFilters: {},
  componentId: SEARCH_CALCULATIONS_COMPONENT_ID
};

export const initCalculationsPaging = getDefaultPagingInfoRequest(1, 20, "claimNumber", "ASC", undefined);

export interface CalculationsState {
  calculations?: vmCalculationList;  
}

export function getDefaultCalculationsState(): CalculationsState {
  return {
    calculations: EMPTY_CALCULATIONS,      
  };
}
