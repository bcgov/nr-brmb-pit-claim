import {Injectable, inject} from "@angular/core";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {Action, Store} from "@ngrx/store";
import {DefaultService as CirrasClaimsAPIService} from "@cirras/cirras-claims-api";
import {SortDirection, TokenService} from "@wf1/wfcc-core-lib";
import {UUID} from "angular2-uuid";
import {Observable, of} from 'rxjs';
import {catchError, debounceTime, map, mergeMap, switchMap, withLatestFrom} from 'rxjs/operators';
import {convertToErrorState, convertToClaimList} from "../../conversion/conversion-from-rest";
import {SEARCH_CLAIMS, SearchClaimsAction, searchClaimsSuccess, searchClaimsError} from "./claims.actions";
import {formatSort, getPageInfoRequestForSearchState} from "../../utils";
import {initClaimsPaging} from "./claims.state";
import {RootState} from "../index";

@Injectable()
export class ClaimsEffects {
  //private actions$ = inject(Actions);

  constructor(
    private actions: Actions,
    private store: Store<RootState>,
    private tokenService: TokenService,
    private cirrasClaimsApiService: CirrasClaimsAPIService) {
  }

  getClaims: Observable<Action> = createEffect(() => this.actions.pipe(
    ofType(SEARCH_CLAIMS),
    withLatestFrom(this.store),
    debounceTime(500),
    switchMap( ([action, store]) => {
      let typedAction = <SearchClaimsAction>action;
      let pagingInfoRequest = typedAction.payload.pageInfoRequest ? typedAction.payload.pageInfoRequest : getPageInfoRequestForSearchState(store.searchClaims);
      let savedFilters = store.searchClaims? store.searchClaims.filters: null;
      let pageNumber = pagingInfoRequest.pageNumber ? pagingInfoRequest.pageNumber : initClaimsPaging.pageNumber;
      let pageSize = pagingInfoRequest.pageRowCount ? pagingInfoRequest.pageRowCount : initClaimsPaging.pageRowCount;

      let authToken = this.tokenService.getOauthToken();
      let requestId = `CIRRAS-CLAIMS${UUID.UUID().toUpperCase()}`.replace(/-/g, "");

      let claimNumber = ( '' + typedAction.payload.filters.claimNumber == 'undefined' ? null : '' + typedAction.payload.filters.claimNumber);
      let policyNumber = ( '' + typedAction.payload.filters.policyNumber == 'undefined' ? null : '' + typedAction.payload.filters.policyNumber);
      let calculationStatusCode = ( '' + typedAction.payload.filters.calculationStatusCode == 'undefined' ? null : '' + typedAction.payload.filters.calculationStatusCode);

      let sortParam = pagingInfoRequest.sortColumn;
      if (sortParam == "version") {
        sortParam = "calculationVersion";
      } else if (sortParam == "calculationStatus") {
        sortParam = "calculationStatusCode";
      } else if (sortParam == "claimNumber") {
          sortParam = "claimNumber";
      } else if (sortParam == "policyNumber") {
          sortParam = "policyNumber";
      } else if (sortParam == "planName") {
        sortParam = "insurancePlanName";
      } else if (sortParam == "commodityName") {
        sortParam = "commodityName";
      } else if (sortParam == "coverageName") {
        sortParam = "coverageName";        
      } else if (sortParam == "growerName") {
          sortParam = "growerName";
      } else if (sortParam == "claimStatusCode") {
          sortParam = "claimStatusCode";
      }

      sortParam = sortParam ? sortParam : 'claimNumber'
      let sortDirection = <SortDirection>pagingInfoRequest.sortDirection ? <SortDirection>pagingInfoRequest.sortDirection : 'ASC';

      return this.cirrasClaimsApiService.getListOfClaims(
        requestId, 
        1,  
        "no-cache",  
        "no-cache",  
        `Bearer ${authToken}`, 
        claimNumber,  
        policyNumber,  
        calculationStatusCode,  
        sortParam,
        sortDirection,
        `${pageNumber}`,
        `${pageSize}`)
          .pipe(
            map((response: any) => {              
                return searchClaimsSuccess(typedAction.componentId, convertToClaimList(response));
            }),
            catchError(error => of(searchClaimsError(typedAction.componentId, convertToErrorState(error, typedAction.displayLabel))))
          );
      }
    )
  ));
}
