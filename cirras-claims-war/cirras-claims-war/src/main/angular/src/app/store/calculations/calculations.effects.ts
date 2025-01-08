import {inject, Injectable} from "@angular/core";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {Action, Store} from "@ngrx/store";
import {DefaultService as CirrasClaimsAPIService} from "@cirras/cirras-claims-api";
import {SortDirection, TokenService} from "@wf1/wfcc-core-lib";
import {UUID} from "angular2-uuid";
import {Observable, of} from 'rxjs';
import {catchError, debounceTime, map, mergeMap, switchMap, withLatestFrom} from 'rxjs/operators';
import {convertToErrorState, convertToCalculationList} from "../../conversion/conversion-from-rest";
import {SEARCH_CALCULATIONS, SearchCalculationsAction, searchCalculationsSuccess, searchCalculationsError} from "./calculations.actions";
import {formatSort, getPageInfoRequestForSearchState} from "../../utils";
import {initCalculationsPaging} from "./calculations.state";
import {RootState} from "../index";

@Injectable()
export class CalculationsEffects {
  constructor(
    private actions: Actions,
    private store: Store<RootState>,
    private tokenService: TokenService,
    private cirrasClaimsApiService: CirrasClaimsAPIService) {
  }

  getCalculations: Observable<Action> = createEffect(() => this.actions.pipe(
    ofType(SEARCH_CALCULATIONS),
    withLatestFrom(this.store),
    debounceTime(500),
    switchMap(([action, store]) => 
        {
      let typedAction = <SearchCalculationsAction>action;
      let pagingInfoRequest = typedAction.payload.pageInfoRequest ? typedAction.payload.pageInfoRequest : getPageInfoRequestForSearchState(store.searchCalculations);
      let savedFilters = store.searchCalculations? store.searchCalculations.filters: null;
      let pageNumber = pagingInfoRequest.pageNumber ? pagingInfoRequest.pageNumber : initCalculationsPaging.pageNumber;
      let pageSize = pagingInfoRequest.pageRowCount ? pagingInfoRequest.pageRowCount : initCalculationsPaging.pageRowCount;
      let sortParam = pagingInfoRequest.sortColumn;
      if (sortParam == "version") {
        sortParam = "calculationVersion";
      } else if (sortParam == "calculationStatus") {
        sortParam = "calculationStatusCode";
      } else if (sortParam == "claimNumber") {
          sortParam = "claimNumber";
      } else if (sortParam == "claimStatusCode") {
          sortParam = "claimStatusCode";
      } else if (sortParam == "policyNumber") {
          sortParam = "policyNumber";
      } else if (sortParam == "growerName") {
          sortParam = "growerName";
      } else if (sortParam == "plan") {
          sortParam = "insurancePlanId";
      } else if (sortParam == "coverage") {
          sortParam = "commodityCoverageCode";
      } else if (sortParam == "commodity") {
        sortParam = "cropCommodityId";
      } else if (sortParam == "totalClaimAmount") {
        sortParam = "totalClaimAmount";
      } else if (sortParam == "createdBy") {
        sortParam = "createUser";
      } else if (sortParam == "lastUpdatedBy") {
        sortParam = "updateUser";
      } else if (sortParam == "createDate") {
        sortParam = "createDate";
      } else if (sortParam == "lastUpdatedOn") {
        sortParam = "updateDate";
      }

      sortParam = sortParam ? sortParam : 'claimNumber'
      let sortDirection = <SortDirection>pagingInfoRequest.sortDirection ? <SortDirection>pagingInfoRequest.sortDirection : 'ASC';
      
      let savedSearchClaimsNumberFilter = savedFilters && savedFilters.searchClaimsNumber ? savedFilters.searchClaimsNumber : undefined;
      let savedSearchPolicyNumberFilter = savedFilters && savedFilters.searchPolicyNumber ? savedFilters.searchPolicyNumber : undefined;
      let savedSearchselectedPlan = savedFilters && savedFilters.selectedPlan ? savedFilters.selectedPlan : undefined;
      let savedCalculationStatusFilter = savedFilters && savedFilters.selectedCalculationStatusCode ? savedFilters.selectedCalculationStatusCode : undefined;
      let savedCropYearFilter = savedFilters && savedFilters.selectedCropYear ? savedFilters.selectedCropYear : undefined;
      let savedCreatedByUserFilter = savedFilters && savedFilters.selectedCreatedByUser ? savedFilters.selectedCreatedByUser : undefined;
      let savedLastUpdatedByFilter = savedFilters && savedFilters.selectedLastUpdatedBy ? savedFilters.selectedLastUpdatedBy : undefined;

      let claimsNumberFilter = typedAction.payload.filters["searchClaimsNumber"] ? typedAction.payload.filters["searchClaimsNumber"] : savedSearchClaimsNumberFilter;
      let policyNumberFilter = typedAction.payload.filters["searchPolicyNumber"] ? typedAction.payload.filters["searchPolicyNumber"] : savedSearchPolicyNumberFilter;
      let selectedPlan = typedAction.payload.filters["selectedPlan"] ? typedAction.payload.filters["selectedPlan"] : savedSearchselectedPlan;      
      let calculationStatusFilter = typedAction.payload.filters["selectedCalculationStatusCode"] ? typedAction.payload.filters["selectedCalculationStatusCode"] : savedCalculationStatusFilter;
      let cropYearFilter = typedAction.payload.filters["selectedCropYear"] ? typedAction.payload.filters["selectedCropYear"] : savedCropYearFilter;
      let createdByUserFilter = typedAction.payload.filters["selectedCreatedByUser"] ? typedAction.payload.filters["selectedCreatedByUser"] : savedCreatedByUserFilter;
      let lastUpdatedByFilter = typedAction.payload.filters["selectedLastUpdatedBy"] ? typedAction.payload.filters["selectedLastUpdatedBy"] : savedLastUpdatedByFilter;

      let authToken = this.tokenService.getOauthToken();
      let requestId = `CIRRAS-CLAIMS${UUID.UUID().toUpperCase()}`.replace(/-/g, "");
      return this.cirrasClaimsApiService.getListOfCalculations(
        requestId, 
        1, 
        "no-cache",  
        "no-cache",  
        `Bearer ${authToken}`, 
        claimsNumberFilter ? claimsNumberFilter[0] : undefined,
        policyNumberFilter ? policyNumberFilter[0] : undefined,
        cropYearFilter ? cropYearFilter[0] : undefined,
        calculationStatusFilter ? calculationStatusFilter[0] : undefined,
        createdByUserFilter ? createdByUserFilter[0] : undefined,
        lastUpdatedByFilter ? lastUpdatedByFilter[0] : undefined,
        selectedPlan ? selectedPlan[0] : undefined ,  // insurancePlanId
        sortParam,
        sortDirection,
        `${pageNumber}`,
        `${pageSize}`)
          .pipe(
            map((response: any) => {
                return searchCalculationsSuccess(typedAction.componentId, convertToCalculationList(response));
            }),
            catchError(error => of(searchCalculationsError(typedAction.componentId, convertToErrorState(error, typedAction.displayLabel))))
          );
      }
    )
  ));
}
