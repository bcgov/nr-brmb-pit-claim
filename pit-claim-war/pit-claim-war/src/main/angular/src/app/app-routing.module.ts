import {NgModule} from "@angular/core";
import {Router, RouterModule, Routes} from "@angular/router";
import {UnauthorizedPageComponent} from "./components/unauthorized-page/unauthorized-page.component";
import {ApplicationStateService} from "./services/application-state.service";
import {ResourcesRoutes as R} from "./utils";
import {CalculationDetailContainer} from "./containers/calculation-detail/calculation-detail-container.component";
import {ResourcesAuthGuard} from "./services/util/ResourcesAuthGuard";
import {SignOutPageComponent} from "./components/sign-out-page/sign-out-page.component";
import {SCOPES_UI} from "./utils/scopes";
import { CalculationsContainer } from "./containers/calculations/calculations-container.component";
import { ClaimsContainer } from "./containers/claims/claims-container.component";

const LANDING_SCOPES = [[SCOPES_UI.GET_TOP_LEVEL, SCOPES_UI.GET_CODE_TABLES, SCOPES_UI.SEARCH_CALCULATIONS]];
const CLAIMS_SCOPES = [[SCOPES_UI.SEARCH_CLAIMS]];
const CALCULATION_SCOPES = [[SCOPES_UI.SEARCH_CALCULATIONS]];
const CALCULATION_DETAIL_SCOPES = [[SCOPES_UI.GET_CALCULATION, SCOPES_UI.GET_CODE_TABLES]];
const CALCULATION_PRINTOUT_SCOPES = [[SCOPES_UI.GET_CALCULATION, SCOPES_UI.GET_CODE_TABLES]];

const routesDesktop: Routes = [
    { path: R.LANDING, redirectTo: R.CALCULATION_LIST, data: {scopes: LANDING_SCOPES}, pathMatch: "full" },
    { path: R.CLAIM_LIST, component: ClaimsContainer, data: {scopes: CLAIMS_SCOPES} }, //**hide claims
    { 
      path: R.CALCULATION_LIST, component: CalculationsContainer, data: {scopes: CALCULATION_SCOPES},
      canActivate: [ResourcesAuthGuard]      
    },  
    { path: R.CALCULATION_DETAIL, children: [
      { 
        path: ':claimNumber', component: CalculationDetailContainer, data: {scopes: CALCULATION_DETAIL_SCOPES},
        canActivate: [ResourcesAuthGuard]
      },
      { 
        path: ':claimNumber/:claimCalculationGuid', component: CalculationDetailContainer, data: {scopes: CALCULATION_DETAIL_SCOPES},
        canActivate: [ResourcesAuthGuard]
      }
    ] },

    { path: R.UNAUTHORIZED, component: UnauthorizedPageComponent },
    { path: R.SIGN_OUT, component: SignOutPageComponent, pathMatch: "full" },
    { path: "**", redirectTo: R.LANDING }
];


@NgModule({
    imports: [RouterModule.forRoot(routesDesktop, {})],
    exports: [RouterModule]
})
export class AppRoutingModule {
    public constructor(router: Router,
                       applicationStateService: ApplicationStateService) {
        // if (applicationStateService.getIsMobileResolution()) {
        //     console.log("mobile mode");
        //     router.resetConfig(routesMobile);
        // }
    }
}
