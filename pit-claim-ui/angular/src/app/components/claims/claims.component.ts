import {ClaimsComponentModel} from "./claims.component.model";
import {searchClaims, clearClaimSearch} from "../../store/claims/claims.actions";
import {initClaimsPaging, SEARCH_CLAIMS_COMPONENT_ID} from "../../store/claims/claims.state";
import {CollectionComponent} from "../common/base-collection/collection.component";
import {AfterViewInit, ChangeDetectionStrategy, Component, OnChanges, SimpleChanges} from "@angular/core";
import {getCodeOptions} from "../../utils/code-table-utils";
import { BaseWrapperComponent } from "../common/base-wrapper/base-wrapper.component";
import { NgIf, NgFor, NgStyle } from "@angular/common";
import { MatProgressSpinner } from "@angular/material/progress-spinner";
import { MatLabel, MatSuffix } from "@angular/material/form-field";
import { ReactiveFormsModule, FormsModule } from "@angular/forms";
import { MatIconButton, MatButton } from "@angular/material/button";
import { MatTooltip } from "@angular/material/tooltip";
import { MatIcon } from "@angular/material/icon";
import { SingleSelectDirective } from "../../directives/singleselect.directive";
import { MatTable, MatColumnDef, MatHeaderCellDef, MatHeaderCell, MatCellDef, MatCell, MatHeaderRowDef, MatHeaderRow, MatRowDef, MatRow } from "@angular/material/table";
import { MatSort, MatSortHeader } from "@angular/material/sort";
import { RouterLink } from "@angular/router";
import { NgxPaginationModule } from "ngx-pagination";
import { vmClaim } from "src/app/conversion/models";
import { navigateToCalculation, setHttpHeaders } from "src/app/utils";
import { lastValueFrom } from "rxjs";
import { ClaimCalculationRsrc } from "@cirras/cirras-claims-api";
import { convertToCalculation } from "src/app/conversion/conversion-from-rest";

@Component({
    selector: "cirras-claims-desktop",
    templateUrl: "claims.component.html",
    styleUrls: ["../common/base/base.component.scss",
        "../common/base-collection/collection.component.scss",
        "claims.component.scss"],
    changeDetection: ChangeDetectionStrategy.OnPush,
    imports: [BaseWrapperComponent, NgIf, MatProgressSpinner, MatLabel, ReactiveFormsModule, FormsModule, MatIconButton, 
      MatSuffix, MatTooltip, MatIcon, SingleSelectDirective, NgFor, MatButton, MatTable, MatSort, MatColumnDef, 
      MatHeaderCellDef, MatHeaderCell, MatSortHeader, MatCellDef, MatCell, RouterLink, NgStyle, MatHeaderRowDef, 
      MatHeaderRow, MatRowDef, MatRow, NgxPaginationModule]
})
export class ClaimsComponent extends CollectionComponent implements OnChanges, AfterViewInit {
  columnsToDisplay = ["version", "calculationStatus", "claimNumber", "policyNumber", "planName", "commodityName", "coverageName", "growerName", "claimStatusCode"];
  displayLabel = "Claim Search";
  calculationStatusOptions = getCodeOptions("CALCULATION_STATUS_CODE");
  filters: {
    [param: string]: any[];
  }

    initModels() {
        this.model = new ClaimsComponentModel(this.sanitizer);
        this.viewModel = new ClaimsComponentModel(this.sanitizer);
    }

    loadPage() {
        this.componentId = SEARCH_CLAIMS_COMPONENT_ID;
        this.updateView();
        this.initSortingAndPaging(initClaimsPaging);
        this.config = this.getPagingConfig();
        this.doSearch();
    }

    ngOnChanges(changes: SimpleChanges) {
      super.ngOnChanges(changes);
    }

    ngAfterViewInit() {
      super.ngAfterViewInit();
    }

    getViewModel(): ClaimsComponentModel {
      return <ClaimsComponentModel>this.viewModel;
    }

    doSearch() {
      // clear claim search list
      this.store.dispatch(clearClaimSearch());

      if ( (this.searchClaimsNumber && this.searchClaimsNumber.length > 4 && !isNaN(this.searchClaimsNumber) )  
          || (this.searchPolicyNumber && this.searchPolicyNumber.length > 8 ) ) { 

          this.filters = {
            claimNumber: this.searchClaimsNumber ? this.searchClaimsNumber.trim() : "",
            policyNumber: this.searchPolicyNumber ? this.searchPolicyNumber.trim() : "",
            calculationStatusCode: this.selectedCalculationStatusCode
          }

          this.store.dispatch(searchClaims(this.componentId, {
              pageNumber: this.config.currentPage,
              pageRowCount: this.config.itemsPerPage,
              sortColumn: this.currentSort,
              sortDirection: this.currentSortDirection,
              query: this.searchText
          },
          this.displayLabel,
          this.filters));
      } 
    }

    isSearchValid() {
      if (this.searchClaimsNumber && this.searchClaimsNumber.length > 4 && isNaN(this.searchClaimsNumber) ) { 
        return false
      }

      return true
    }

    onChangeFilters() {
        super.onChangeFilters();
        this.doSearch();
    }

    clearSearchAndFilters() {
      this.searchClaimsNumber = undefined;
      this.searchPolicyNumber = undefined;
      this.selectedCalculationStatusCode = undefined;
      this.store.dispatch(clearClaimSearch());
    }    

  getCalulationUrl(isNew: boolean, claimNumber: number, claimCalculationGuid: string, ) {
    let url = this.appConfigService.getConfig().rest["cirras_claims"]

    if (isNew) {
      url = url +"/claims/" + claimNumber 
    } else {
      url = url +"/calculations/" + claimCalculationGuid + "?doRefreshManualClaimData=false" 
    }

    return url
  }

  redirectToVersion(item:vmClaim, isNew: boolean) {

      if (!item.claimNumber) {
       return 
      }

      let url = this.getCalulationUrl(isNew, item.claimNumber, item.claimCalculationGuid)

      const httpOptions = setHttpHeaders(this.tokenService.getOauthToken())

      return lastValueFrom(this.http.get(url,httpOptions)).then((data: ClaimCalculationRsrc) => {
        const calculation = convertToCalculation(data)

        navigateToCalculation(calculation, this.router)

      })
    }



}
