import {RootState} from "../index";
import {
  vmCalculation
} from "../../conversion/models";

export const selectCalculationDetail = () => (state: RootState): vmCalculation => ((state.calculationDetail) ? state.calculationDetail.calculationDetail : null);
