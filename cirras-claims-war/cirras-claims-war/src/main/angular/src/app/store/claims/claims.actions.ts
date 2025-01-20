import {Action} from "@ngrx/store";
import {vmClaimList} from "../../conversion/models";
import {ErrorState, PagingInfoRequest} from "../application/application.state";
import {LabeledAction} from "../index";

export const SEARCH_CLAIMS = "SEARCH_CLAIMS";
export const SEARCH_CLAIMS_SUCCESS = "SEARCH_CLAIMS_SUCCESS";
export const SEARCH_CLAIMS_ERROR = "SEARCH_CLAIMS_ERROR";

export const CLEAR_CLAIM_SEARCH = "CLEAR_CLAIM_SEARCH";

export interface ClearClaimAction extends Action {
  payload: {
    value: vmClaimList;
  };

}

export function clearClaimSearch(): ClearClaimAction {
  return {
    type: CLEAR_CLAIM_SEARCH,
    payload: {
      value: {collection: [] }
    }
  };
}

export interface SearchClaimsAction extends LabeledAction {
  componentId: string;
  payload: {
    pageInfoRequest: PagingInfoRequest,
    filters: {
      [param: string]: any[];
    }
  };
}

export interface SearchClaimsSuccessAction extends Action {
  componentId: string;
  payload: {
    value: vmClaimList;
  };
}

export interface SearchClaimsErrorAction extends Action {
  componentId: string;
  payload: {
    error: ErrorState;
  };
}

export function searchClaims(componentId: string, 
                             pageInfoRequest: PagingInfoRequest,
                             displayLabel: string,
                             filters: any): SearchClaimsAction {

  return {
    type: SEARCH_CLAIMS,
    componentId: componentId,
    displayLabel: displayLabel,
    payload: {
      pageInfoRequest,
      filters: filters
    }
  };
}

export function searchClaimsSuccess(componentId: string, value: vmClaimList): SearchClaimsSuccessAction {
  return {
    type: SEARCH_CLAIMS_SUCCESS,
    componentId: componentId,
    payload: {
      value
    }
  };
}

export function searchClaimsError(componentId: string, error: ErrorState): SearchClaimsErrorAction {
  return {
    type: SEARCH_CLAIMS_ERROR,
    componentId: componentId,
    payload: {
      error
    }
  };
}
