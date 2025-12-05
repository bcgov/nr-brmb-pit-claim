import {ClaimsComponentModel} from "./claims.component.model";
import {searchClaims, clearClaimSearch} from "../../store/claims/claims.actions";
import {initClaimsPaging, SEARCH_CLAIMS_COMPONENT_ID} from "../../store/claims/claims.state";
import {CollectionComponent} from "../common/base-collection/collection.component";
import {AfterViewInit, ChangeDetectionStrategy, Component, OnChanges, SimpleChanges} from "@angular/core";
import {getCodeOptions} from "../../utils/code-table-utils";

@Component({
    selector: "cirras-claims-desktop",
    templateUrl: "claims.component.html",
    styleUrls: ["../common/base/base.component.scss",
        "../common/base-collection/collection.component.scss",
        "claims.component.scss"],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
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
}
