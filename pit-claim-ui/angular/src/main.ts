import { enableProdMode, provideAppInitializer, inject, Injector, Type, ModuleWithProviders, importProvidersFrom } from "@angular/core";

import { cirrasClaimsRestInitializerFn } from "./app/app.module";
import {environment} from "./environments/environment";
import { provideBootstrapEffects, DATE_FORMATS } from "./app/utils";
import { rootEffects, rootReducers, initialRootState } from "./app/store";
import { UpdateService } from "./app/services/update.service";
import { AppConfigService } from "./app/services/app-config.service";
import { Configuration as CirrasClaimsAPIServiceConfiguration, ApiModule as CirrasClaimsAPIServiceModule } from "@cirras/cirras-claims-api";
import { TokenService } from "./app/services/token.service";
import { Title, BrowserModule, bootstrapApplication } from "@angular/platform-browser";
import { appInitFn } from "./app/utils/app-initializer";
import { HttpHandler, HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from "@angular/common/http";
import { OWL_DATE_TIME_FORMATS, OwlDateTimeModule, OwlMomentDateTimeModule } from "@busacca/ng-pick-datetime";
import { RouteReuseStrategy } from "@angular/router";
import { CustomReuseStrategy } from "./app/utils/custom-route-reuse-strategy";
import { ResourcesInterceptor } from "./app/interceptors/resources-interceptor";
import { ConnectionServiceOptionsToken, ConnectionServiceOptions } from "ngx-connection-service";
import { MAT_FORM_FIELD_DEFAULT_OPTIONS } from "@angular/material/form-field";
import { provideNgxMask, NgxMaskDirective, NgxMaskPipe } from "ngx-mask";
import { DragDropModule } from "@angular/cdk/drag-drop";
import { CdkTableModule } from "@angular/cdk/table";
import { provideAnimations } from "@angular/platform-browser/animations";
import { FormsModule } from "@angular/forms";
import { MatExpansionModule } from "@angular/material/expansion";
import { MatBadgeModule } from "@angular/material/badge";
import { MatGridListModule } from "@angular/material/grid-list";
import { MatAutocompleteModule } from "@angular/material/autocomplete";
import { MatButtonModule } from "@angular/material/button";
import { MatCardModule } from "@angular/material/card";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatDialogModule } from "@angular/material/dialog";
import { MatListModule } from "@angular/material/list";
import { MatMenuModule } from "@angular/material/menu";
import { MatRadioModule } from "@angular/material/radio";
import { MatSnackBarModule } from "@angular/material/snack-bar";
import { MatSortModule } from "@angular/material/sort";
import { MatTabsModule } from "@angular/material/tabs";
import { MatTableModule } from "@angular/material/table";
import { MatPaginatorModule } from "@angular/material/paginator";
import { MatTooltipModule } from "@angular/material/tooltip";
import { MatSidenavModule } from "@angular/material/sidenav";
import { MatToolbarModule } from "@angular/material/toolbar";
import { MomentModule } from "ngx-moment";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { ScrollingModule } from "@angular/cdk/scrolling";
import { CoreUIModule } from "./app/lib/core-ui.module";
import { StoreModule } from "@ngrx/store";
import { AppRoutingModule } from "./app/app-routing.module";
import { NgxPaginationModule } from "ngx-pagination";
import { EffectsModule } from "@ngrx/effects";
import { ServiceWorkerModule } from "@angular/service-worker";
import { A11yModule } from "@angular/cdk/a11y";
import { WildfireApplicationModule, WildfireResourceManagerModule } from "@wf1/wfcc-application-ui";
import { AppComponent } from "./app/containers/application-root/app.component";

const apiConfiguration = new CirrasClaimsAPIServiceConfiguration();
let devOnlyImports: Array<Type<any> | ModuleWithProviders<any>> = [];


if (environment.production) {
    enableProdMode();
}

bootstrapApplication(AppComponent, {
providers: [
    importProvidersFrom(
        CirrasClaimsAPIServiceModule, 
        DragDropModule, 
        CdkTableModule, 
        BrowserModule, 
        FormsModule, 
        MatExpansionModule, 
        MatBadgeModule, 
        MatGridListModule, 
        MatAutocompleteModule, 
        MatButtonModule, 
        MatCardModule, 
        MatCheckboxModule, 
        MatDialogModule, 
        MatListModule, 
        MatMenuModule, 
        MatRadioModule, 
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
        WildfireResourceManagerModule.forRoot()
    ),
    // Added provideBootstrapEffects function to handle the ngrx issue that loads effects before APP_INITIALIZER
    // providers have finished initializing.
    // See https://github.com/ngrx/platform/issues/931 for more information.
    provideBootstrapEffects(rootEffects),
    UpdateService,
    AppConfigService,
    TokenService,
    Title,
    provideAppInitializer(() => {
        const initializerFn = (appInitFn)(inject(HttpHandler), inject(Injector));
        return initializerFn();
    }),
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
    {
        provide: MAT_FORM_FIELD_DEFAULT_OPTIONS,
        useValue: {
            subscriptSizing: 'dynamic'
        }
    },
    provideNgxMask(),
    provideHttpClient(withInterceptorsFromDi()),
    provideAnimations()
]
}).then(() => {
    if ("serviceWorker" in navigator && environment.production) {
        navigator.serviceWorker.register("ngsw-worker.js");
    }

}).catch(err => console.log(err));



