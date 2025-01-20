import {
  vmCalculation,
} from "../../conversion/models";

export const CALCULATION_DETAIL_COMPONENT_ID = "calculationDetail";

export interface CalculationDetailState {
  calculationDetail?: vmCalculation;
}

export function getDefaultCalculationDetailState(): CalculationDetailState {
  return {
    calculationDetail: null,
  };
}
