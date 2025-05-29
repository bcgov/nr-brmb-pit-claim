import {inject, Injectable, Injector} from "@angular/core";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {Action, Store} from "@ngrx/store";
import {Observable, of, forkJoin} from "rxjs";
import {
  catchError,
  concatMap,
  map,
  mergeMap,
  switchMap, 
  withLatestFrom,
  debounceTime
} from "rxjs/operators";
import {RootState} from "../index";
import {
  LOAD_CALCULATION_DETAIL,
  LoadCalculationDetailAction,
  loadCalculationDetailSuccess,
  loadCalculationDetailError
} from "./calculation-detail.actions";
import {
  UPDATE_CALCULATION_DETAIL_METADATA,
  UpdateCalculationDetailMetadataAction,
  updateCalculationDetailMetadataSuccess,
  updateCalculationDetailMetadataError,
  SYNC_CLAIMS_CODE_TABLES,
  SyncClaimsCodeTablesAction,
  syncClaimsCodeTablesError,
  syncClaimsCodeTablesSuccess  
} from "./calculation-detail.actions";
import {DefaultService as CirrasClaimsAPIService} from "@cirras/cirras-claims-api";
import {UUID} from "angular2-uuid";
import {TokenService} from "@wf1/wfcc-core-lib";
import {
  convertToCalculation,
  convertToErrorState
} from "../../conversion/conversion-from-rest";
import {
  convertToCalculationRsrc
} from "../../conversion/conversion-to-rest";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Router} from "@angular/router";
import {
  displaySaveSuccessSnackbar,
  displaySuccessSnackbar,
} from "../../utils/user-feedback-utils";
import {vmCalculation} from "../../conversion/models";
import {
  getClaimsCodeTables,
  populateClaimsCodeTableCache
} from "../../utils/app-initializer";
import { CALCULATION_UPDATE_TYPE, navigateToCalculation } from "src/app/utils";

@Injectable()
export class CalculationDetailEffects {
  constructor(
    private actions: Actions,
    private store: Store<RootState>,
    private router: Router,
    private tokenService: TokenService,
    private injector: Injector,        
    private cirrasClaimsAPIService: CirrasClaimsAPIService,
    private snackbarService: MatSnackBar) {
  }

loadCalculationDetail: Observable<Action> = createEffect(() => this.actions
  .pipe(
    ofType(LOAD_CALCULATION_DETAIL),
    mergeMap(action => {
      let typedAction = <LoadCalculationDetailAction>action;
      let payload = typedAction.payload;
      let authToken = this.tokenService.getOauthToken();
      let requestId = `CIRRAS-CLAIMS${UUID.UUID().toUpperCase()}`.replace(/-/g, "");

      if (payload.claimCalculationGuid ) {
        // if the calculation exists get it
        return this.cirrasClaimsAPIService.getTheCalculation(
          payload.claimCalculationGuid,
          requestId,
          1,  
          "no-cache", 
          "no-cache", 
          `Bearer ${authToken}`,
          payload.doRefreshManualClaimData,
          "response")
          .pipe(
            map((response: any) => {
              return loadCalculationDetailSuccess(convertToCalculation(response.body, response.headers ? response.headers.get("ETag") : null));
            }),
            catchError(error => of(loadCalculationDetailError(convertToErrorState(error, "Calculation Detail data"))))
          );

      }else {

        // get data to set up new calculation
        return this.cirrasClaimsAPIService.getTheClaim(
          payload.claimNumber,
          requestId,
          1,  
          "no-cache",  
          "no-cache",  
          `Bearer ${authToken}`,
          "response")
          .pipe(
            map((response: any) => {
              return loadCalculationDetailSuccess(convertToCalculation(response.body, response.headers ? response.headers.get("ETag") : null));
            }),
            catchError(error => of(loadCalculationDetailError(convertToErrorState(error, "Calculation Detail data"))))
          );

      }

      }
    ),
  )
);    

  
  updateCalculationDetailMetadata: Observable<Action> = createEffect(() => this.actions.pipe(
      ofType(UPDATE_CALCULATION_DETAIL_METADATA),
      withLatestFrom(this.store),
      debounceTime(500),
      mergeMap(
          ([action, store]) => {
              let typedAction = <UpdateCalculationDetailMetadataAction>action;
              let requestId = `WFDME${UUID.UUID().toUpperCase()}`.replace(/-/g, "");
              let authToken = this.tokenService.getOauthToken();
              let payload = <vmCalculation>typedAction.payload.value;
              let updateType = typedAction.payload.updateType;

              const body = new Blob([JSON.stringify(convertToCalculationRsrc(payload))],{type: 'application/json'});

              let displayLabel:string = "Calculation ";
              if (updateType == CALCULATION_UPDATE_TYPE.REPLACE_COPY || updateType == CALCULATION_UPDATE_TYPE.REPLACE_NEW) {
                displayLabel = "New version ";
              }

              if (payload.claimCalculationGuid) {
                //update current calculation
                return this.cirrasClaimsAPIService.updateCalculation(
                  payload.etag,
                  payload.claimCalculationGuid,
                  body,
                  requestId,
                  1, 
                  "no-cache",  
                  "no-cache",  
                  `Bearer ${authToken}`,                 
                  updateType,                  
                  "response")
                .pipe(
                    concatMap((response: any) => {
                      if (updateType == CALCULATION_UPDATE_TYPE.SUBMIT) {
                        displayLabel = " Calculation was submitted successfully "
                      } else {
                        displayLabel = " Calculation was saved successfully "
                      }
                      displaySuccessSnackbar(this.snackbarService, displayLabel);
                        return [                            
                            updateCalculationDetailMetadataSuccess(convertToCalculation(response.body, response.headers ? response.headers.get("ETag") : null))
                        ]
                    }),
                    catchError(error =>{
                        return of(updateCalculationDetailMetadataError(convertToErrorState(error, displayLabel))) 
                    }
                ),
                )            
              } else {
                //create new calculation
                return this.cirrasClaimsAPIService.addANewCalculation(
                  body,
                  requestId,
                  1, 
                  "no-cache",  
                  "no-cache",  
                  `Bearer ${authToken}`,
                  "response")
                .pipe(
                    concatMap((response: any) => {
                      displaySaveSuccessSnackbar(this.snackbarService, "Calculation ");
                      let newCalculation: vmCalculation = convertToCalculation(response.body, response.headers ? response.headers.get("ETag") : null) 

                      navigateToCalculation( newCalculation, this.router )
                        return [                                                                         
                            updateCalculationDetailMetadataSuccess(newCalculation)                             
                        ]
                    }),
                    catchError(error =>{
                        return of(updateCalculationDetailMetadataError(convertToErrorState(error, "Calculation "))) 
                    }
                ),
                )            
              }
          }                  
      )
  ));

  dtrSync: Observable<Action> = createEffect(() => 
  this.actions.pipe(
    ofType(SYNC_CLAIMS_CODE_TABLES),
    withLatestFrom(this.store),
    debounceTime(500),    
    switchMap(
      ([action, store]) => {
        let typedAction = <SyncClaimsCodeTablesAction>action;

        return forkJoin([
          getClaimsCodeTables(this.tokenService.getOauthToken(), this.tokenService, this.injector),

        ]).pipe(
            concatMap(results => {
                let retActions = [];

                if (results[0] && results[0]['codeTableList']) { populateClaimsCodeTableCache(results[0]['codeTableList']); }

                retActions.push(syncClaimsCodeTablesSuccess());
                return retActions;
            }),
            catchError(error => of(syncClaimsCodeTablesError(convertToErrorState(error))))
        )
      }
    )
  ));

}
