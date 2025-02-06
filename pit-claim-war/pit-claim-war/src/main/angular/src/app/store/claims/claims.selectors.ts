import {RootState} from "../index";
import {vmClaimList} from "../../conversion/models";

export const selectClaims = () => (state: RootState): vmClaimList => ((state.claims) ? state.claims.claims : null);
