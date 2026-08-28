import {
  Configuration as CirrasClaimsAPIServiceConfiguration
} from "@cirras/cirras-claims-api";
import {StoreDevtoolsModule} from "@ngrx/store-devtools";
import { AppConfigService } from "./services/app-config.service";
import {environment} from "../environments/environment";
import { Type } from '@angular/core';
import { ModuleWithProviders } from '@angular/core';


/**
 * Function that initializes the Configuration injector with the application base url from the app config service.
 * Used by the Swagger CodeGen Rest API angular services.
 */
 export function cirrasClaimsRestInitializerFn(appConfig: AppConfigService) {
  const apiConfiguration = new CirrasClaimsAPIServiceConfiguration();
  apiConfiguration.basePath = appConfig.getConfig().rest["cirras_claims"];
  return apiConfiguration;
}

// Use a union type to allow both standard and configured modules
let devOnlyImports: Array<Type<any> | ModuleWithProviders<any>> = [];

if (!environment.production || !environment.restrict_imports) {
    devOnlyImports = [
        StoreDevtoolsModule.instrument({
            maxAge: 50,
        connectInZone: true}),
    ];
}


