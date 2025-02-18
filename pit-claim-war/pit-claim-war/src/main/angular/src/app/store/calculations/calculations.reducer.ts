import {Action} from "@ngrx/store";
import {getDefaultCalculationsState, CalculationsState} from "./calculations.state";
import {
  SEARCH_CALCULATIONS_SUCCESS, 
  SearchCalculationsSuccessAction, 
  CLEAR_CALCULATION_SEARCH, 
  ClearCalculationSearchAction
} from "./calculations.actions";

export function calculationsReducer(state: CalculationsState = getDefaultCalculationsState(), action: Action): CalculationsState {
  switch (action.type) {

    case CLEAR_CALCULATION_SEARCH: {
      const typedAction = <ClearCalculationSearchAction>action;
      return {...state, calculations: typedAction.payload.value};
    }

    case SEARCH_CALCULATIONS_SUCCESS: {
      const typedAction = <SearchCalculationsSuccessAction>action;
      return {...state, calculations: typedAction.payload.value};
    }
    default: {
      return state;
    }
  }
}