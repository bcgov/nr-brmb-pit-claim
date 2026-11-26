import {DefaultService as CirrasClaimsAPIService} from "@cirras/cirras-claims-api";

import {Injector} from "@angular/core";
import { HttpErrorResponse } from "@angular/common/http";
import {catchError, map} from "rxjs/operators";
import {firstValueFrom, forkJoin, of} from "rxjs";
import {UUID} from "angular2-uuid";
import {CODE_TABLE_CACHE, DATE_FORMATS} from "./index";
import {ApplicationStateService} from "../services/application-state.service";
import moment from "moment";
// import { AppConfigService, TokenService } from "@bcgov/pit-common-core-lib";
import { AppConfigService } from "src/lib/services/app-config.service";
import { TokenService } from "src/lib/services/token.service";

export function appInitFn(injector: Injector): () => Promise<void> {
  const appStateService = injector.get(ApplicationStateService);
  //initialize the app gutter space styling based on device resolution
  document.documentElement.style.setProperty("--cirras-claims-gutter-space", appStateService.getIsMobileResolution() ? "8px" : "16px");

  const appConfigService = injector.get(AppConfigService);
  const tokenService = injector.get(TokenService);

  return async (): Promise<void> => {
    await firstValueFrom(appConfigService.configEmitter);
    const authenticated = await tokenService.init();
    debugger
    if (authenticated) {  
      console.log("User is authenticated");
      // getClaimsCodeTables()
    } else {
      console.log("User is not authenticated");
      // potentially show authentication error message to user here
    }
  };
}

export function handleHasToken(token, appConfigService: AppConfigService, tokenService: TokenService, injector: Injector, resolve, reject) {
  // Fetch code tables if there is access
  forkJoin(
      [
        getClaimsCodeTables(token, tokenService, injector),
      ]
  ).pipe(
      map(results => {
          if (results[0] instanceof HttpErrorResponse) {
              console.log("Failed to initialize app", results);
              reject();
              return null;
          } else {
              //console.log(results);
              return results;
          }
      }),
      catchError(error => of(error))
  ).subscribe(results => {
      if (results) {
          if (results[0]) { populateClaimsCodeTableCache(results[0].codeTableList); }
      }

      console.log(CODE_TABLE_CACHE);
      //console.log(JSON.stringify(CODE_TABLE_CACHE));
      setTimeout(() => { //the org code hdq loading takes some time...so wait some time for those to populate before resolving
          console.log("loading code table");
          resolve();
      }, 200);
  });
}

export function getClaimsCodeTables(token, tokenService: TokenService, injector: Injector): any {
  if (tokenService.doesUserHaveApplicationPermissions(["CIRRAS_CLAIMS.GET_TOP_LEVEL", "CIRRAS_CLAIMS.GET_CODE_TABLES"])) {
      let cirrasClaimsApiService = injector.get(CirrasClaimsAPIService);
      let requestId = `CIRRAS-CLAIMS${UUID.UUID().toUpperCase()}`.replace(/-/g, "");
      let ret = cirrasClaimsApiService.getCodeTableListResource(
          requestId,
          1,
          "no-cache",
          "no-cache",
          `Bearer ${token}`,
          moment().format(DATE_FORMATS.API_DATE),
          undefined,
      ).pipe(
          map(result => result),
          catchError(err => of(err))
      );
      return ret;
      
  } else {
      return of({codeTableList: []});
  }
}

export function populateClaimsCodeTableCache(codeTable) {
  try {
      CODE_TABLE_CACHE["codeTables"] = codeTable.reduce((acc, table) => ({...acc, [table.codeTableName]: table.codes}), {});
  } catch (error) {
      console.error(error);
  }
}
