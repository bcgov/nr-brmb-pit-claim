import {ErrorState, LoadState} from "./application.state";
import {RootState} from "../index";
import {SearchState} from "@wf1/wfcc-core-lib";

export const selectSearchState = (componentId) => (state: RootState): SearchState => ((state[componentId]) ? state[componentId] : undefined);
export const selectClaimsLoadState = () => (state: RootState): LoadState => ((state.application.loadStates.claims) ? state.application.loadStates.claims : undefined);
export const selectClaimsErrorState = () => (state: RootState): ErrorState[] => ((state.application.errorStates.claims) ? state.application.errorStates.claims : undefined);
export const selectCalculationsLoadState = () => (state: RootState): LoadState => ((state.application.loadStates.calculations) ? state.application.loadStates.calculations : undefined);
export const selectCalculationsErrorState = () => (state: RootState): ErrorState[] => ((state.application.errorStates.calculations) ? state.application.errorStates.calculations : undefined);
export const selectCalculationDetailMetadataLoadState = () => (state: RootState): LoadState => ((state.application.loadStates.calculationDetailMetadata) ? state.application.loadStates.calculationDetailMetadata : undefined);
export const selectCalculationDetailMetadataErrorState = () => (state: RootState): ErrorState[] => ((state.application.errorStates.calculationDetailMetadata) ? state.application.errorStates.calculationDetailMetadata : undefined);
export const selectFormStateUnsaved = (componentId: string) => (state: RootState): boolean => ((state.application.formStates[componentId]) ? state.application.formStates[componentId].isUnsaved : false);

export const selectFormStatesUnsaved = (componentIds: string[]) => (state: RootState): boolean => {
  let ret = false;
  if (componentIds && componentIds.length) {
    componentIds.forEach(componentId => {
      let formUnsaved = state.application.formStates[componentId] ? state.application.formStates[componentId].isUnsaved : false;
      ret = ret || formUnsaved;
    });
  }
  return ret;
};
