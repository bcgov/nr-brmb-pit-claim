import {Action} from "@ngrx/store";
import {getDefaultClaimsState, ClaimsState} from "./claims.state";
import {SEARCH_CLAIMS_SUCCESS, SearchClaimsSuccessAction, CLEAR_CLAIM_SEARCH, ClearClaimAction} from "./claims.actions";

export function claimsReducer(state: ClaimsState = getDefaultClaimsState(), action: Action): ClaimsState {
  switch (action.type) {
    case CLEAR_CLAIM_SEARCH:  {
      const typedAction = <ClearClaimAction>action;
      return {...state, claims: typedAction.payload.value };
    }

    case SEARCH_CLAIMS_SUCCESS: {
      const typedAction = <SearchClaimsSuccessAction>action;
      return {...state, claims: typedAction.payload.value};
    }
    default: {
      return state;
    }
  }
}