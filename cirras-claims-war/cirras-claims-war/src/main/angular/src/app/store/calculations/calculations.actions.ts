import {Action} from "@ngrx/store";
import {vmCalculationList} from "../../conversion/models";
import {ErrorState, PagingInfoRequest} from "../application/application.state";
import {LabeledAction} from "../index";

export const SEARCH_CALCULATIONS = "SEARCH_CALCULATIONS";
export const SEARCH_CALCULATIONS_SUCCESS = "SEARCH_CALCULATIONS_SUCCESS";
export const SEARCH_CALCULATIONS_ERROR = "SEARCH_CALCULATIONS_ERROR";

export const CLEAR_CALCULATION_SEARCH = "CLEAR_CALCULATION_SEARCH";

export interface ClearCalculationSearchAction extends Action {
  payload: {
    value: vmCalculationList;
  };
}

export function clearCalculationSearch(): ClearCalculationSearchAction {
  return {
    type: CLEAR_CALCULATION_SEARCH,
    payload: {
      value: {collection: [] }
    }
  };
}


export interface SearchCalculationsAction extends LabeledAction {
  componentId: string;
  payload: {
    pageInfoRequest: PagingInfoRequest,
    filters: {
      [param: string]: any[];
    }
  };
}

export interface SearchCalculationsSuccessAction extends Action {
  componentId: string;
  payload: {
    value: vmCalculationList;
  };
}

export interface SearchCalculationsErrorAction extends Action {
  componentId: string;
  payload: {
    error: ErrorState;
  };
}

export function searchCalculations(componentId: string, 
                             pageInfoRequest: PagingInfoRequest,
                             searchClaimsNumber?: string,
                             searchPolicyNumber?: string,
                             selectedPlan?: string,
                             selectedCalculationStatusCode?: string,
                             selectedCropYear?: string,
                             selectedCreatedByUser?: string,
                             selectedLastUpdatedBy?: string,
                             displayLabel?: string): SearchCalculationsAction {
  let filters = {};
  filters["searchClaimsNumber"] = searchClaimsNumber ? [searchClaimsNumber] : undefined;
  filters["searchPolicyNumber"] = searchPolicyNumber ? [searchPolicyNumber] : undefined;
  filters["selectedCalculationStatusCode"] = selectedCalculationStatusCode ? [selectedCalculationStatusCode] : undefined;
  filters["selectedCropYear"] = selectedCropYear ? [selectedCropYear] : undefined;
  filters["selectedPlan"] = selectedPlan ? [selectedPlan] : undefined;
  filters["selectedCreatedByUser"] = selectedCreatedByUser ? [selectedCreatedByUser] : undefined;
  filters["selectedLastUpdatedBy"] = selectedLastUpdatedBy ? [selectedLastUpdatedBy] : undefined;

  return {
    type: SEARCH_CALCULATIONS,
    componentId: componentId,
    displayLabel: displayLabel,
    payload: {
      pageInfoRequest,
      filters: filters
    }
  };
}

export function searchCalculationsSuccess(componentId: string, value: vmCalculationList): SearchCalculationsSuccessAction {
  return {
    type: SEARCH_CALCULATIONS_SUCCESS,
    componentId: componentId,
    payload: {
      value
    }
  };
}

export function searchCalculationsError(componentId: string, error: ErrorState): SearchCalculationsErrorAction {
  return {
    type: SEARCH_CALCULATIONS_ERROR,
    componentId: componentId,
    payload: {
      error
    }
  };
}
