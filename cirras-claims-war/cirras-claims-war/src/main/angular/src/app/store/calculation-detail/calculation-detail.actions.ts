import {Action} from "@ngrx/store";
import {ErrorState} from "../application/application.state";
import {LabeledAction} from "../index";
import {
  vmCalculation
} from "../../conversion/models";

export const LOAD_CALCULATION_DETAIL = "LOAD_CALCULATION_DETAIL";
export const LOAD_CALCULATION_DETAIL_SUCCESS = "LOAD_CALCULATION_SUCCESS";
export const LOAD_CALCULATION_DETAIL_ERROR = "LOAD_CALCULATION_ERROR";

export const UPDATE_CALCULATION_DETAIL_METADATA = "UPDATE_CALCULATION_DETAIL_METADATA";
export const UPDATE_CALCULATION_DETAIL_METADATA_SUCCESS = "UPDATE_CALCULATION_DETAIL_METADATA_SUCCESS";
export const UPDATE_CALCULATION_DETAIL_METADATA_ERROR = "UPDATE_CALCULATION_DETAIL_METADATA_ERROR";

export const CLEAR_CALCULATION_DETAIL_METADATA = "CLEAR_CALCULATION_DETAIL_METADATA";

export const SYNC_CLAIMS_CODE_TABLES = 'SYNC_CLAIMS_CODE_TABLES';
export const SYNC_CLAIMS_CODE_TABLES_SUCCESS = 'SYNC_CLAIMS_CODE_TABLES_SUCCESS';
export const SYNC_CLAIMS_CODE_TABLES_ERROR = 'SYNC_CLAIMS_CODE_TABLES_SUCCESS_ERROR';

export interface ClearCalculationDetailAction extends Action {
  payload: {
    value: vmCalculation;
  };

}

export function clearCalculationDetail(): ClearCalculationDetailAction {
  return {
    type: CLEAR_CALCULATION_DETAIL_METADATA,
    payload: {
      value: <vmCalculation>{}
    }
  };
}

export interface LoadCalculationDetailAction extends LabeledAction {
  payload: {
    claimCalculationGuid: string;
    claimNumber: string;
    doRefreshManualClaimData: string;
  }
}

export interface LoadCalculationDetailSuccessAction extends Action {
  payload: {
    value: any;
  }
}

export interface LoadCalculationDetailErrorAction extends Action {
  payload: {
    error:ErrorState;
  }
}

export function loadCalculationDetail(claimCalculationGuid: string, displayLabel: string, claimNumber: string, doRefreshManualClaimData: string): LoadCalculationDetailAction {
  return {
    type: LOAD_CALCULATION_DETAIL,
    displayLabel: displayLabel,
    payload: {
      claimCalculationGuid,
      claimNumber,
      doRefreshManualClaimData
    }
  };
}

export function loadCalculationDetailSuccess(value: any): LoadCalculationDetailSuccessAction {
  return {
    type: LOAD_CALCULATION_DETAIL_SUCCESS,
    payload: {
      value
    }
  };
}

export function loadCalculationDetailError(error:ErrorState): LoadCalculationDetailErrorAction {
  return {
    type: LOAD_CALCULATION_DETAIL_ERROR,
    payload: {
      error
    }
  };
}

export interface UpdateCalculationDetailMetadataAction extends LabeledAction {
  payload: {
    value: vmCalculation;
    updateType: string;
  };
}

export interface UpdateCalculationDetailMetadataSuccessAction extends Action {
  payload: {
    value: vmCalculation;
  };
}

export interface UpdateCalculationDetailMetadataErrorAction extends Action {
  payload: {
    error: ErrorState;
  };
}


export function updateCalculationDetailMetadata(calculation: vmCalculation, updateType: string): UpdateCalculationDetailMetadataAction {
  return {
    type: UPDATE_CALCULATION_DETAIL_METADATA,
    displayLabel: null,
    payload: {
      value: calculation,
      updateType: updateType
    }
  };
}

export function updateCalculationDetailMetadataSuccess(value: vmCalculation): UpdateCalculationDetailMetadataSuccessAction {
  return {
    type: UPDATE_CALCULATION_DETAIL_METADATA_SUCCESS,
    payload: {
      value
    }
  };
}

export function updateCalculationDetailMetadataError(error: ErrorState): UpdateCalculationDetailMetadataErrorAction {
  return {
    type: UPDATE_CALCULATION_DETAIL_METADATA_ERROR,
    payload: {
      error
    }
  };
}

export interface SyncClaimsCodeTablesAction extends Action {
  payload: {
  }
}

export interface SyncClaimsCodeTablesSuccessAction extends Action {

}

export interface SyncClaimsCodeTablesErrorAction extends Action {
  payload: {
      errors?: ErrorState
  }
}

export function syncClaimsCodeTables(): SyncClaimsCodeTablesAction {
  return {
      type: SYNC_CLAIMS_CODE_TABLES,
      payload: {
      }
  };
}

export function syncClaimsCodeTablesSuccess(): SyncClaimsCodeTablesSuccessAction {
  return {
      type: SYNC_CLAIMS_CODE_TABLES_SUCCESS,

  };
}

export function syncClaimsCodeTablesError(errors: ErrorState): SyncClaimsCodeTablesErrorAction {
  return {
      type: SYNC_CLAIMS_CODE_TABLES_ERROR,
      payload: {
          errors
      }
  };
}
