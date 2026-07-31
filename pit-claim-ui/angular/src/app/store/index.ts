import {Action, ActionReducerMap} from "@ngrx/store";
//import {routerReducer} from "@ngrx/router-store";

import {applicationReducer} from "./application/application.reducer";
import {ApplicationEffects} from "./application/application.effects";
import {ApplicationState, PagingSearchState} from "./application/application.state";

import {pageSearchReducer} from "./common/page-search.reducer";
import {ClaimsEffects} from "./claims/claims.effects";
import {CalculationsEffects} from "./calculations/calculations.effects";
import {CalculationDetailEffects} from "./calculation-detail/calculation-detail.effects";

import {ClaimsState, initialClaimsSearchState} from "./claims/claims.state";
import {CalculationsState, initialCalculationsSearchState} from "./calculations/calculations.state";
import {CalculationDetailState} from "./calculation-detail/calculation-detail.state";

import {claimsReducer} from "./claims/claims.reducer";
import {calculationsReducer} from "./calculations/calculations.reducer";
import {calculationDetailReducer} from "./calculation-detail/calculation-detail.reducer";

export const rootReducers: ActionReducerMap<any> = {
    //router: routerReducer,
    claims: claimsReducer,
    calculations: calculationsReducer,
    calculationDetail: calculationDetailReducer,
    searchClaims: pageSearchReducer,
    searchCalculations: pageSearchReducer,
    application: applicationReducer,
};

export interface RootState {
    application?: ApplicationState;      
    claims?: ClaimsState;
    calculations?: CalculationsState;
    calculationDetail?: CalculationDetailState;
    searchClaims?: PagingSearchState;
    searchCalculations? : PagingSearchState;
}

export const initialRootState: RootState = {
  searchClaims: initialClaimsSearchState,
  searchCalculations: initialCalculationsSearchState
};

export const rootEffects: any[] = [
    ApplicationEffects,
    ClaimsEffects,
    CalculationsEffects,
    CalculationDetailEffects
];

export interface LabeledAction extends Action {
    displayLabel: string;
}

