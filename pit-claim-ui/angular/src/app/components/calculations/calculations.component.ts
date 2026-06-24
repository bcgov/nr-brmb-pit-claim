import { vmCalculation } from './../../conversion/models';
import {CalculationsComponentModel} from "./calculations.component.model";
import {searchCalculations, clearCalculationSearch} from "../../store/calculations/calculations.actions";
import {initCalculationsPaging, SEARCH_CALCULATIONS_COMPONENT_ID} from "../../store/calculations/calculations.state";
import {CollectionComponent} from "../common/base-collection/collection.component";
import {AfterViewInit, ChangeDetectionStrategy, Component, OnChanges, SimpleChanges} from "@angular/core";
import {getCodeOptions} from "../../utils/code-table-utils";
import {navigateToCalculation} from "../../utils";

@Component({
    selector: "cirras-claims-calculations",
    templateUrl: "calculations.component.html",
    styleUrls: ["../common/base/base.component.scss",
        "../common/base-collection/collection.component.scss",
        "calculations.component.scss"],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})

export class CalculationsComponent extends CollectionComponent implements OnChanges, AfterViewInit {
  columnsToDisplay = ["version", "calculationStatus", "claimNumber", "claimStatusCode", "policyNumber", "growerName", "plan", "coverage", "commodity", "totalClaimAmount", "createdBy", "lastUpdatedBy", "createDate", "lastUpdatedOn"];
  displayLabel = "Calculation Search";
  calculationStatusOptions = getCodeOptions("CALCULATION_STATUS_CODE");
  cropYearOptions = getCodeOptions("CLAIM_CALC_CROP_YEAR");
  createdByUserOptions = getCodeOptions("CLAIM_CALC_CREATE_USER");
  lastUpdatedByOptions = getCodeOptions("CLAIM_CALC_UPDATE_USER");
  insurancePlans = getCodeOptions("INSURANCE_PLAN")

  initModels() {
    this.model = new CalculationsComponentModel(this.sanitizer);
    this.viewModel = new CalculationsComponentModel(this.sanitizer);
  }

  loadPage() {
    this.componentId = SEARCH_CALCULATIONS_COMPONENT_ID;
    this.updateView();
    this.initSortingAndPaging(initCalculationsPaging);
    this.config = this.getPagingConfig();
    this.doSearch();
  }

  ngOnChanges(changes: SimpleChanges) {
    super.ngOnChanges(changes);
  }

  ngAfterViewInit() {
    super.ngAfterViewInit();
  }

  getViewModel(): CalculationsComponentModel {
    return <CalculationsComponentModel>this.viewModel;
  }

  isSearchValid() {
    if (this.searchClaimsNumber && this.searchClaimsNumber.length > 4 && isNaN(this.searchClaimsNumber) ) { 
      return false
    }

    return true
  }


  doSearch() {
    // clear the previous search
    this.store.dispatch(clearCalculationSearch());

    if (this.isSearchValid()) {
      this.store.dispatch(searchCalculations(this.componentId, {
        pageNumber: this.config.currentPage,
        pageRowCount: this.config.itemsPerPage,
        sortColumn: this.currentSort,
        sortDirection: this.currentSortDirection,
        query: this.searchText
      },
      this.searchClaimsNumber,
      this.searchPolicyNumber,
      this.insurancePlan,
      this.selectedCalculationStatusCode,
      this.selectedCropYear,
      this.selectedCreatedByUser,
      this.selectedLastUpdatedBy,
      this.displayLabel));
    }
    
  }

  onChangeFilters() {
    super.onChangeFilters();
    this.doSearch();
  }

  clearSearchAndFilters() {
    this.searchText = null;
    this.searchClaimsNumber = undefined;
    this.searchPolicyNumber = undefined;
    this.selectedCalculationStatusCode = undefined;
    this.selectedCropYear = undefined;
    this.insurancePlan = undefined;
    this.selectedCreatedByUser = undefined;
    this.selectedLastUpdatedBy = undefined;
    super.onChangeFilters();
    this.doSearch();
  } 

  defaultItemActionPermitted(item): boolean {
    return this.securityUtilService.canViewCalculationDetail(item);
  }    

  doDefaultItemAction(item) {
    this.selectCalculation(item);
  }

  selectCalculation(item: vmCalculation) {
    navigateToCalculation(item, this.router);
  }
}
