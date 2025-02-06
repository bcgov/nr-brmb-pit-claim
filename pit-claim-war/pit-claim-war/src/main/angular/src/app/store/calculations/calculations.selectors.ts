import {RootState} from "../index";
import {vmCalculationList} from "../../conversion/models";

export const selectCalculations = () => (state: RootState): vmCalculationList => ((state.calculations) ? state.calculations.calculations : null);
