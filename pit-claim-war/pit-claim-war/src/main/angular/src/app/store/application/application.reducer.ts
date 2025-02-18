import {Action} from "@ngrx/store";
import {
  ApplicationState,
  ERROR_TYPE,
  ErrorState,
  getDefaultApplicationState,
  getDefaultFormState,
  getDefaultLoadStates
} from "./application.state";
import {
  SEARCH_CLAIMS, 
  SEARCH_CLAIMS_ERROR, 
  SEARCH_CLAIMS_SUCCESS
} from "../claims/claims.actions";
import {
  SEARCH_CALCULATIONS, 
  SEARCH_CALCULATIONS_ERROR, 
  SEARCH_CALCULATIONS_SUCCESS
} from "../calculations/calculations.actions";
import {
  LOAD_CALCULATION_DETAIL,
  LOAD_CALCULATION_DETAIL_SUCCESS,
  LOAD_CALCULATION_DETAIL_ERROR,
  UPDATE_CALCULATION_DETAIL_METADATA,
  UPDATE_CALCULATION_DETAIL_METADATA_ERROR,
  UPDATE_CALCULATION_DETAIL_METADATA_SUCCESS
} from "../calculation-detail/calculation-detail.actions";
import {SET_FORM_STATE_UNSAVED, SetFormStateUnsavedAction} from "./application.actions";
import {CLAIMS_COMPONENT_ID} from "../claims/claims.state";
import {CALCULATIONS_COMPONENT_ID} from "../calculations/calculations.state";
import {CALCULATION_DETAIL_COMPONENT_ID} from "../calculation-detail/calculation-detail.state";

export function applicationReducer(state: ApplicationState = getDefaultApplicationState(), action: Action): ApplicationState {
  switch (action.type) {
    case SEARCH_CLAIMS:
    case SEARCH_CALCULATIONS:
    case LOAD_CALCULATION_DETAIL:
    case UPDATE_CALCULATION_DETAIL_METADATA:        
    {
      return updateLoadState(state, action, true);
    }
    case SEARCH_CLAIMS_SUCCESS:
    case SEARCH_CALCULATIONS_SUCCESS:
    case LOAD_CALCULATION_DETAIL_SUCCESS:
    case UPDATE_CALCULATION_DETAIL_METADATA_SUCCESS:
    {
      return updateLoadState(state, action, false);
    }
    case SEARCH_CLAIMS_ERROR:
    case SEARCH_CALCULATIONS_ERROR:    
    case LOAD_CALCULATION_DETAIL_ERROR:
    case UPDATE_CALCULATION_DETAIL_METADATA_ERROR:
    {
      return updateErrorState(state, action, action["payload"]["error"]);
    }
    case SET_FORM_STATE_UNSAVED: {
      let typedAction = <SetFormStateUnsavedAction>action;
      return {...state, formStates: {...state.formStates,
              [typedAction.payload.componentId]: {
                ...state.formStates[typedAction.payload.componentId],
                isUnsaved: typedAction.payload.isUnsaved}}
             };
    }
    default: {
      return state;
    }
  }
}

export function getStatePropertyNameForActionName(action: Action): string {
  let actionType = action.type;
  let typedAction = null;
  switch (actionType) {
    case SEARCH_CLAIMS:
    case SEARCH_CLAIMS_SUCCESS:
    case SEARCH_CLAIMS_ERROR:
      return CLAIMS_COMPONENT_ID;
    case SEARCH_CALCULATIONS:
    case SEARCH_CALCULATIONS_SUCCESS:
    case SEARCH_CALCULATIONS_ERROR:
        return CLAIMS_COMPONENT_ID;      
    case LOAD_CALCULATION_DETAIL:
    case LOAD_CALCULATION_DETAIL_SUCCESS:
    case LOAD_CALCULATION_DETAIL_ERROR:
    case UPDATE_CALCULATION_DETAIL_METADATA:
    case UPDATE_CALCULATION_DETAIL_METADATA_SUCCESS:
    case UPDATE_CALCULATION_DETAIL_METADATA_ERROR:
      return CALCULATION_DETAIL_COMPONENT_ID;
    default:
      return null;
  }
}

export function updateLoadState(state: ApplicationState, action: Action, value: boolean): ApplicationState {
  let component = getStatePropertyNameForActionName(action);
  let st = state;
  if (value) { // if starting load, reset error state
      st = clearErrorState(state, action);
  } else { //if ending load, reset form state
      st = clearFormState(state, action);
  }
  // Only update state if there is a value change
  //console.log(component, action.type, value);
  if (component && (!state.loadStates || !state.loadStates[component] || state.loadStates[component].isLoading !== value)) {
    return {
      ...st,
      loadStates: {...st.loadStates, [component]: {isLoading: value}}
    };
  } else {
    return st;
  }
}

export function updateErrorState(state: ApplicationState, action: Action, value: ErrorState): ApplicationState {
  let component = getStatePropertyNameForActionName(action);
  if (component) {
    if (value.type == ERROR_TYPE.FATAL) {
      let ns = {
        ...state,
        //errorStates: {...state.errorStates, ["severe"]: [...state.errorStates["severe"], value]}, // severe errorstate does not seem to be used
        loadStates: getDefaultLoadStates() // set all load states to false on a fatal error
      };
      return ns;
    }

    if (state.errorStates && state.errorStates[component]) {
      if (state.errorStates[component].find && state.errorStates[component].find((errorState: ErrorState) => errorState.message == value.message)) {
        return state;
      }
    }

    return {
      ...state,
      errorStates: {...state.errorStates, [component]: [...state.errorStates[component], value]},
      loadStates: {...state.loadStates, [component]: {isLoading: false}}
    };
  } else {
      return state;
  }
}

export function clearErrorState(state: ApplicationState, action: Action): ApplicationState {
  //TODO filter out errors in 'errors' param by UUID
  let component = getStatePropertyNameForActionName(action);
  if (component) {
    return {
      ...state,
      errorStates: {...state.errorStates, [component]: []},
    };
  } else {
      return state;
  }
}

export function clearFormState(state: ApplicationState, action: Action): ApplicationState {
  let component = getStatePropertyNameForActionName(action);
  if (component) {
    return {
      ...state,
      formStates: {...state.formStates, [component]: getDefaultFormState()},
    };
  } else {
    return state;
  }
}
