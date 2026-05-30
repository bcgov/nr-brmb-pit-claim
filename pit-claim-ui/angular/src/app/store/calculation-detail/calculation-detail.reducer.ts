import {Action} from "@ngrx/store";
import {
  ClearCalculationDetailAction,
  CLEAR_CALCULATION_DETAIL_METADATA,
  LoadCalculationDetailSuccessAction, 
  LOAD_CALCULATION_DETAIL_SUCCESS,
  UPDATE_CALCULATION_DETAIL_METADATA_SUCCESS
} from "./calculation-detail.actions";
import { CalculationDetailState, getDefaultCalculationDetailState } from "./calculation-detail.state";

export function calculationDetailReducer(state: CalculationDetailState = getDefaultCalculationDetailState(), action: Action): CalculationDetailState {
  switch (action.type) {
    case LOAD_CALCULATION_DETAIL_SUCCESS: 
    case UPDATE_CALCULATION_DETAIL_METADATA_SUCCESS:
      {
      const typedAction = <LoadCalculationDetailSuccessAction>action;
      return {...state, calculationDetail: typedAction.payload.value};
    }

    case CLEAR_CALCULATION_DETAIL_METADATA: 
    {
      const typedAction = <ClearCalculationDetailAction>action;  
      return {...state, calculationDetail: null};  //typedAction.payload.value
    }

    default: {
        return state;
    }
  }
}