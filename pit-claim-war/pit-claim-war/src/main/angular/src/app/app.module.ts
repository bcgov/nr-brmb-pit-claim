import {APP_INITIALIZER, Injector, NgModule} from "@angular/core";
import {ScrollingModule} from "@angular/cdk/scrolling";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {
  ApiModule as CirrasClaimsAPIServiceModule,
  Configuration as CirrasClaimsAPIServiceConfiguration
} from "@cirras/cirras-claims-api";
import {BrowserModule, Title} from "@angular/platform-browser";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'; // or NoopAnimationsModule
import {ServiceWorkerModule} from "@angular/service-worker";
import {EffectsModule} from "@ngrx/effects";
import {StoreModule} from "@ngrx/store";
import {StoreDevtoolsModule} from "@ngrx/store-devtools";
import {AppConfigService, CoreUIModule, TokenService} from "@wf1/wfcc-core-lib"; //MapService, PublicApplicationHeaderModule, 
import {AppRoutingModule} from "./app-routing.module";
import {AppComponent} from "./containers/application-root/app.component";
import {environment} from "../environments/environment";
import {initialRootState,  rootReducers, rootEffects} from "./store"; //,
import {DATE_FORMATS , provideBootstrapEffects} from "./utils"; // 
import { NgxMaskDirective, NgxMaskPipe, provideNgxMask } from "ngx-mask";
import {MomentModule} from "ngx-moment";
import {UnauthorizedPageComponent} from "./components/unauthorized-page/unauthorized-page.component";
import {ErrorPanelComponent} from "./components/common/error-panel/error-panel.component";
import {ConnectionServiceOptions, ConnectionServiceOptionsToken} from "ngx-connection-service";
import {DragDropModule} from "@angular/cdk/drag-drop";
import {RouteReuseStrategy} from "@angular/router";
import {CustomReuseStrategy} from "./utils/custom-route-reuse-strategy";
import {BaseWrapperComponent} from "./components/common/base-wrapper/base-wrapper.component";
import { HTTP_INTERCEPTORS, HttpHandler, provideHttpClient, withInterceptorsFromDi } from "@angular/common/http";
import {ResourcesInterceptor} from "./interceptors/resources-interceptor";
import {NgxPaginationModule} from "ngx-pagination";
import {CalculationDetailComponent} from "./components/calculation-detail/calculation-detail.component";
import {CalculationDetailGrapesComponent} from "./components/calculation-detail/grapes/grapes.component";
import {CalculationDetailContainer} from "./containers/calculation-detail/calculation-detail-container.component";
import {CalculationPrintoutComponent} from './components/calculation-printout/grapes/grapes.component';
import {CdkTableModule} from "@angular/cdk/table";
import {appInitFn} from "./utils/app-initializer";
import {AutoFocusDirective} from "./directives/auto-focus.directive";
import {BaseDialogComponent} from "./components/dialogs/base-dialog/base-dialog.component";
import {UpdateService} from "./services/update.service";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatSelectModule} from "@angular/material/select";
import {MatIconModule} from "@angular/material/icon";
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatListModule} from "@angular/material/list";
import {MatInputModule} from "@angular/material/input";
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatExpansionModule} from "@angular/material/expansion";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {MatRadioModule} from "@angular/material/radio";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {MatMenuModule} from "@angular/material/menu";
import {MatBadgeModule} from "@angular/material/badge";
import {MatGridListModule} from "@angular/material/grid-list";
import {MatSortModule} from "@angular/material/sort";
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatTableModule} from "@angular/material/table";
import {MatDialogModule} from "@angular/material/dialog";
import {MatCardModule} from "@angular/material/card";
import {MatTabsModule} from "@angular/material/tabs";
import {MatTooltipModule} from "@angular/material/tooltip";
import {MatButtonModule} from "@angular/material/button";
import {OWL_DATE_TIME_FORMATS, OwlDateTimeModule, OwlMomentDateTimeModule} from "@busacca/ng-pick-datetime";
import {BaseExpansionPanelComponent} from "./components/common/base-expansion-panel/base-expansion-panel.component";
import {WFSnackbarComponent} from "./components/common/snackbars/wf-snackbar.component";
import {TimeMaskDirective} from "./directives/time-mask.directive";
import {DateTimeMaskDirective} from "./directives/date-time-mask.directive";
import {DateMaskDirective} from "./directives/date-mask.directive";
import {ErrorDialogComponent} from "./components/dialogs/error-dialog/error-dialog.component";
import {DateRangeMaskDirective} from "./directives/date-range-mask.directive";
import {ReadonlyFieldDirective} from "./directives/readonly-field.directive";
import {ReadonlyFormDirective} from "./directives/readonly-form.directive";
import {A11yModule} from "@angular/cdk/a11y";
import {MultiSelectDirective} from "./directives/multi-select.directive";
import {SingleSelectDirective} from "./directives/singleselect.directive";
import { ReplaceOptionsDialogComponent } from './components/dialogs/replace-options-dialog/replace-options-dialog.component';
import { CalculationDetailBerriesComponent } from './components/calculation-detail/berries/berries.component';
import { CalculationPrintoutBerriesComponent } from './components/calculation-printout/berries/berries.component';
import { CalculationDetailBlueberriesPlantComponent } from './components/calculation-detail/blueberries-plant/blueberries-plant.component';
import { CalculationDetailHeaderComponent } from './components/calculation-detail/calculation-detail-header/calculation-detail-header.component';
import { CalculationPrintoutBlueberriesPlantComponent } from './components/calculation-printout/blueberries-plant/blueberries-plant.component';
import { CalculationDetailStrawberriesPlantComponent } from './components/calculation-detail/strawberries-plant/strawberries-plant.component';
import { CalculationPrintoutStrawberriesPlantComponent } from "./components/calculation-printout/strawberries-plant/strawberries-plant.component";
import { CalculationPrintoutLogoComponent } from './components/calculation-printout/calculation-printout-logo/calculation-printout-logo.component';
import { CalculationPrintoutHeaderComponent } from './components/calculation-printout/calculation-printout-header/calculation-printout-header.component';
import { CalculationPrintoutFooterComponent } from './components/calculation-printout/calculation-printout-footer/calculation-printout-footer.component';
import { WildfireApplicationModule, WildfireResourceManagerModule } from "@wf1/wfcc-application-ui";
import { CalculationsContainer } from "./containers/calculations/calculations-container.component";
import { CalculationsComponent } from "./components/calculations/calculations.component";
import { ClaimsContainer } from "./containers/claims/claims-container.component";
import { ClaimsComponent } from "./components/claims/claims.component";
import { MAT_FORM_FIELD_DEFAULT_OPTIONS } from '@angular/material/form-field';
import { CalculationDetailGrainUnseededComponent } from "./components/calculation-detail/grain-unseeded/grain-unseeded.component";
import { CalculationPrintoutGrainUnseededComponent } from "./components/calculation-printout/grain-unseeded/grain-unseeded.component";
import { CalculationDetailGrainSpotLossComponent } from "./components/calculation-detail/grain-spot-loss/grain-spot-loss.component";
import { CalculationPrintoutGrainSpotLossComponent } from "./components/calculation-printout/grain-spot-loss/grain-spot-loss.component";
import { CalculationDetailGrainQuantityComponent } from "./components/calculation-detail/grain-quantity/grain-quantity.component";
import { UnsavedDialogComponent } from "./components/dialogs/unsaved-dialog/unsaved-dialog.component";
import { CalculationPrintoutGrainQuantityComponent } from "./components/calculation-printout/grain-quantity/grain-quantity.component";
import { CalculationDetailGrainBasketComponent } from "./components/calculation-detail/grain-basket/grain-basket.component";
import { CalculationPrintoutGrainBasketComponent } from "./components/calculation-printout/grain-basket/grain-basket.component";
/**
 * Function that initializes the Configuration injector with the application base url from the app config service.
 * Used by the Swagger CodeGen Rest API angular services.
 */
 export function cirrasClaimsRestInitializerFn(appConfig: AppConfigService) {
  const apiConfiguration = new CirrasClaimsAPIServiceConfiguration();
  apiConfiguration.basePath = appConfig.getConfig().rest["cirras_claims"];
  return apiConfiguration;
}

let devOnlyImports = [];

if (!environment.production || !environment.restrict_imports) {
    devOnlyImports = [
        StoreDevtoolsModule.instrument({
            maxAge: 50,
        connectInZone: true}),
    ];
}

@NgModule({ declarations: [
        DateMaskDirective,
        TimeMaskDirective,
        DateTimeMaskDirective,
        DateRangeMaskDirective,
        ReadonlyFieldDirective,
        ReadonlyFormDirective,
        MultiSelectDirective,
        SingleSelectDirective,
        MultiSelectDirective,
        SingleSelectDirective,
        WFSnackbarComponent,
        BaseExpansionPanelComponent,
        BaseDialogComponent,
        AutoFocusDirective,
        ClaimsContainer,
        ClaimsComponent,
        CalculationDetailContainer,
        CalculationDetailComponent,
        CalculationDetailGrapesComponent,
        CalculationPrintoutComponent,
        CalculationsContainer,
        CalculationsComponent,
        AppComponent,
        BaseWrapperComponent,
        UnauthorizedPageComponent,
        ErrorPanelComponent,
        ErrorDialogComponent,
        ReplaceOptionsDialogComponent,
        CalculationDetailBerriesComponent,
        CalculationPrintoutBerriesComponent,
        CalculationDetailBlueberriesPlantComponent,
        CalculationDetailHeaderComponent,
        CalculationPrintoutBlueberriesPlantComponent,
        CalculationDetailStrawberriesPlantComponent,
        CalculationPrintoutStrawberriesPlantComponent,
        CalculationDetailGrainUnseededComponent,
        CalculationPrintoutGrainUnseededComponent,
        CalculationPrintoutLogoComponent,
        CalculationPrintoutHeaderComponent,
        CalculationPrintoutFooterComponent,
        CalculationDetailGrainSpotLossComponent,
        CalculationPrintoutGrainSpotLossComponent,
        CalculationDetailGrainQuantityComponent,
        CalculationPrintoutGrainQuantityComponent,
        CalculationDetailGrainBasketComponent,
        CalculationPrintoutGrainBasketComponent,
        UnsavedDialogComponent
    ],
    bootstrap: [AppComponent], imports: [CirrasClaimsAPIServiceModule,
        DragDropModule,
        CdkTableModule,
        BrowserModule,
        BrowserAnimationsModule,
        FormsModule,
        MatExpansionModule,
        MatBadgeModule,
        MatGridListModule,
        MatAutocompleteModule,
        MatButtonModule,
        MatCardModule,
        MatCheckboxModule,
        MatDialogModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        MatListModule,
        MatMenuModule,
        MatRadioModule,
        MatSelectModule,
        MatSnackBarModule,
        MatSortModule,
        MatTabsModule,
        MatTableModule,
        MatPaginatorModule,
        MatTooltipModule,
        MatSnackBarModule,
        MatSidenavModule,
        MatToolbarModule,
        MomentModule,
        MatProgressSpinnerModule,
        NgxMaskDirective,
        NgxMaskPipe,
        OwlDateTimeModule,
        OwlMomentDateTimeModule,
        ReactiveFormsModule,
        ScrollingModule,
        CoreUIModule.forRoot({ configurationPath: environment.app_config_location }),
        StoreModule.forRoot(rootReducers, { initialState: initialRootState }),
        AppRoutingModule,
        NgxPaginationModule,
        EffectsModule.forRoot([]),
        ServiceWorkerModule.register("ngsw-worker.js", { enabled: environment.production, scope: "./" }),
        ...devOnlyImports,
        A11yModule,
        WildfireApplicationModule.forRoot(),
        WildfireResourceManagerModule.forRoot()], providers: [
        // Added provideBootstrapEffects function to handle the ngrx issue that loads effects before APP_INITIALIZER
        // providers have finished initializing.
        // See https://github.com/ngrx/platform/issues/931 for more information.
        provideBootstrapEffects(rootEffects),
        UpdateService,
        AppConfigService,
        TokenService,
        Title,
        {
            provide: APP_INITIALIZER,
            useFactory: appInitFn,
            multi: true,
            deps: [HttpHandler, Injector]
        },
        {
            provide: CirrasClaimsAPIServiceConfiguration,
            useFactory: cirrasClaimsRestInitializerFn,
            multi: false,
            deps: [AppConfigService]
        },
        { provide: OWL_DATE_TIME_FORMATS, useValue: DATE_FORMATS },
        { provide: RouteReuseStrategy, useClass: CustomReuseStrategy },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: ResourcesInterceptor,
            multi: true
        },
        {
            provide: ConnectionServiceOptionsToken,
            useValue: <ConnectionServiceOptions>{
                enableHeartbeat: false
            }
        },
        { // to remove padding-bottom at the bottom of the text-fields
            provide: MAT_FORM_FIELD_DEFAULT_OPTIONS,
            useValue: {
              subscriptSizing: 'dynamic'
            }
        },
        provideNgxMask(),
        provideHttpClient(withInterceptorsFromDi()),
    ] })
export class AppModule {

}
