import { Component, ChangeDetectionStrategy, Input, OnChanges, AfterViewInit, SimpleChanges } from '@angular/core';
import { vmCalculation } from 'src/app/conversion/models';
import {CodeData, Option} from "../../../store/application/application.state";
import { BaseComponent } from '../../common/base/base.component';
import {getCodeOptions} from "../../../utils/code-table-utils";
import { CALCULATION_DETAIL_COMPONENT_ID } from 'src/app/store/calculation-detail/calculation-detail.state';
import { loadCalculationDetail, syncClaimsCodeTables, updateCalculationDetailMetadata } from 'src/app/store/calculation-detail/calculation-detail.actions';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import { UntypedFormGroup } from '@angular/forms';
import { CalculationDetailGrainBasketComponentModel } from './grain-basket.component.model';
import { areNotEqual, CALCULATION_STATUS_CODE, CALCULATION_UPDATE_TYPE, CLAIM_STATUS_CODE, getPrintTitle, makeTitleCase } from 'src/app/utils';
import { displayErrorMessage } from 'src/app/utils/user-feedback-utils';

@Component({
  selector: 'calculation-detail-grain-basket',
  templateUrl: './grain-basket.component.html',
  styleUrl: './grain-basket.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})

export class CalculationDetailGrainBasketComponent extends BaseComponent implements OnChanges, AfterViewInit{
  displayLabel = "Calculation Detail";

  @Input() calculationDetail: vmCalculation;
  @Input() isUnsaved: boolean;

  calculationStatusOptions: (CodeData|Option)[];
  perilCodeOptions: (CodeData|Option)[];

  calculationComment: string = ""

  quantityTotalCoverageValue: number
  totalYieldCoverageValue: number         // Sum of grain_basket_coverage_value and quantity_total_coverage_value
  quantityTotalYieldValue: number         // Sum of claim_calculation_grain_basket_product.yield_value
  totalYieldLoss: number                  // total_yield_coverage_value - quantity_total_yield_value
  quantityTotalYieldLossIndemnity: number // sum((production_quarantee - assessed_yield - total_yield_to_count) x insurable_value) from claim_calculation_grain_basket_product
  totalApprovedQuantityClaims: number // sum of all Quantity Claims linked to that policy in the claims calculator, which have status Approved.
  totalClaimAmount: number                // claim_calculation_grain_basket.yield_loss - quantity_total_yield_loss_indemnity

  hasUnapprovedQuantityCalculation: boolean = false

  initModels() {
    this.viewModel = new CalculationDetailGrainBasketComponentModel(this.sanitizer, this.fb, this.calculationDetail);
  }

  loadPage() {
    this.calculationStatusOptions = getCodeOptions("CALCULATION_STATUS_CODE");
    this.perilCodeOptions = getCodeOptions("PERIL_CODE");
    this.componentId = CALCULATION_DETAIL_COMPONENT_ID;
    this.updateView();
  }

  getViewModel(): CalculationDetailGrainBasketComponentModel  { 
      return <CalculationDetailGrainBasketComponentModel>this.viewModel;
  }

  ngOnChanges(changes: SimpleChanges) {
    super.ngOnChanges(changes);

    if (changes.calculationDetail) {
        this.calculationDetail = changes.calculationDetail.currentValue;
        this.calculationComment = this.calculationDetail.calculationComment

        
        setTimeout(() => {
            this.cdr.detectChanges();
        });
    }

    this.ngOnChanges2(changes);
  }

  ngOnInit() {
    super.ngOnInit()
  }

  ngOnChanges2(changes: SimpleChanges) {

    if ( changes.calculationDetail && this.calculationDetail ) {

      this.viewModel.formGroup.controls.primaryPerilCode.setValue( this.calculationDetail.primaryPerilCode )
      this.viewModel.formGroup.controls.secondaryPerilCode.setValue( this.calculationDetail.secondaryPerilCode )  
      this.viewModel.formGroup.controls.calculationComment.setValue( this.calculationDetail.calculationComment )  

      if (!this.calculationDetail.claimCalculationGrainBasket.claimCalculationGrainBasketGuid) {

        // calculate values for new calculations only
        this.calculateValues()

      } else {
        // set the total calculated values
        this.totalYieldCoverageValue = this.calculationDetail.claimCalculationGrainBasket.totalYieldCoverageValue
        this.quantityTotalYieldValue = this.calculationDetail.claimCalculationGrainBasket.quantityTotalYieldValue
        this.totalYieldLoss = this.calculationDetail.claimCalculationGrainBasket.totalYieldLoss
        this.quantityTotalYieldLossIndemnity = this.calculationDetail.claimCalculationGrainBasket.quantityTotalYieldLossIndemnity
        this.totalApprovedQuantityClaims = this.calculationDetail.claimCalculationGrainBasket.quantityTotalClaimAmount
        this.totalClaimAmount = this.calculationDetail.totalClaimAmount  
      }

      this.checkForUnapprovedQuantityCalculation()

      this.enableDisableFormControls();

    }
  }
  
  ngAfterViewInit() {
    super.ngAfterViewInit();
  }

  onCancel() {
    this.store.dispatch(loadCalculationDetail(this.calculationDetail.claimCalculationGuid, this.displayLabel, this.calculationDetail.claimNumber.toString(), this.calculationDetail.policyNumber, "false"));
    this.store.dispatch(setFormStateUnsaved(CALCULATION_DETAIL_COMPONENT_ID, false ));
  }

  setComment() {
    this.calculationComment = this.viewModel.formGroup.controls.calculationComment.value
    this.store.dispatch(setFormStateUnsaved(CALCULATION_DETAIL_COMPONENT_ID, true ));
  }

  getCmdtyName(str) {

    if (str.indexOf(" - PEDIGREED") > 1 ) {
      str = str.substring(0, str.indexOf(" - PEDIGREED")) 
    }

    return makeTitleCase(str)
  }

  calculateValues() {

    this.quantityTotalCoverageValue = 0
    this.quantityTotalYieldValue = 0
    this.quantityTotalYieldLossIndemnity = 0
    this.totalApprovedQuantityClaims = 0

    let yield_value = 0

    for (let i = 0; i < this.calculationDetail.claimCalculationGrainBasketProducts.length; i++ ) {
      
      this.quantityTotalCoverageValue = this.quantityTotalCoverageValue + 
                                    this.calculationDetail.claimCalculationGrainBasketProducts[i].coverageValue
    
      // total_yield_to_count x hundred_percent_insurable_value
      yield_value = this.calculationDetail.claimCalculationGrainBasketProducts[i].totalYieldToCount * this.calculationDetail.claimCalculationGrainBasketProducts[i].hundredPercentInsurableValue

      // Sum of claim_calculation_grain_basket_product.yield_value
      this.quantityTotalYieldValue = this.quantityTotalYieldValue + yield_value

      // sum((production_quarantee - assessed_yield - total_yield_to_count) x insurable_value) from claim_calculation_grain_basket_product
      let qtyYieldLossIndemnity = this.calculationDetail.claimCalculationGrainBasketProducts[i].insurableValue * ( this.calculationDetail.claimCalculationGrainBasketProducts[i].productionGuarantee - this.calculationDetail.claimCalculationGrainBasketProducts[i].assessedYield - this.calculationDetail.claimCalculationGrainBasketProducts[i].totalYieldToCount) 
      this.quantityTotalYieldLossIndemnity = this.quantityTotalYieldLossIndemnity + Math.max(0, qtyYieldLossIndemnity)

      this.totalApprovedQuantityClaims = this.totalApprovedQuantityClaims + this.calculationDetail.claimCalculationGrainBasketProducts[i].quantityClaimAmount
      
    }

    //  (Grain Basket + Quantity Coverage For All Commodities) 
    // Sum of grain_basket_coverage_value and quantity_total_coverage_value
    this.totalYieldCoverageValue = this.calculationDetail.claimCalculationGrainBasket.grainBasketCoverageValue + 
                                    this.quantityTotalCoverageValue

    // total_yield_coverage_value - quantity_total_yield_value
    this.totalYieldLoss = Math.max(0 , this.totalYieldCoverageValue - this.quantityTotalYieldValue)

    // claim_calculation_grain_basket.yield_loss - quantity_total_yield_loss_indemnity
    this.totalClaimAmount = Math.max(0, this.totalYieldLoss - this.quantityTotalYieldLossIndemnity )           

  }

  checkForUnapprovedQuantityCalculation() {
    this.hasUnapprovedQuantityCalculation = false 

    if (this.calculationDetail && this.calculationDetail.claimCalculationGrainBasketProducts) {

      for (let i = 0; i < this.calculationDetail.claimCalculationGrainBasketProducts.length; i++) {
      
        // if this policy has quantity products
        if (this.calculationDetail.claimCalculationGrainBasketProducts[i].quantityCommodityCoverageCode == "CQG") {

          // if this policy has qty calculation but it's not approved -> hide all calculations that depend on qty claim being approved
          if (this.calculationDetail.claimCalculationGrainBasketProducts[i].quantityLatestClaimCalculationGuid && 
             this.calculationDetail.claimCalculationGrainBasketProducts[i].quantityLatestCalculationStatusCode !== "APPROVED") {
            
            this.hasUnapprovedQuantityCalculation = true
          }
        }
      }
    }
  }

  showQtyTotYieldValueWarning() {
    // if Total Yield Value is different than the Harvested Value pulled from the Inventory & Yield app then show warning
    
    if ( (Math.round ( this.quantityTotalYieldValue * 100) / 100) !== (Math.round(this.calculationDetail.claimCalculationGrainBasket.grainBasketHarvestedValue  * 100 ) / 100) ) {
        return true
    } else {
      return false
    }
  }

  showIndemnitiesWarning() {
    
    // display a warning sign if quantityTotalYieldLossIndemnity and totalApprovedQuantityClaims are different
    // they are just calculated in two different ways
    if ( (Math.round ( this.quantityTotalYieldLossIndemnity * 100) / 100) !== (Math.round(this.totalApprovedQuantityClaims  * 100 ) / 100) ) {
        return true
    } else {
      return false
    }

  }

enableDisableFormControls() {
    if(this.calculationDetail){

      if(this.calculationDetail.calculationStatusCode == CALCULATION_STATUS_CODE.DRAFT){
        this.viewModel.formGroup.controls.primaryPerilCode.enable()
        this.viewModel.formGroup.controls.secondaryPerilCode.enable()
      } else {
        this.viewModel.formGroup.controls.primaryPerilCode.disable()
        this.viewModel.formGroup.controls.secondaryPerilCode.disable()
      }
    }
  }

  onSave(saveCommentsOnly:boolean) {
        const  updatedClaim = this.getUpdatedClaim(saveCommentsOnly);
        if (this.isFormValid(updatedClaim) )  {
  
            this.store.dispatch(updateCalculationDetailMetadata(updatedClaim, ""));
            this.doSyncClaimsCodeTables();
  
            this.store.dispatch(setFormStateUnsaved(CALCULATION_DETAIL_COMPONENT_ID, false ));
        }
    }
  
    getUpdatedClaim(saveCommentsOnly:boolean) {
      // making a deep copy here
      let updatedCalculation :vmCalculation = JSON.parse(JSON.stringify(this.calculationDetail));
  
      if (saveCommentsOnly) {
  
          updatedCalculation.calculationComment = this.viewModel.formGroup.controls.calculationComment.value
  
      } else {
        // user fields
        updatedCalculation.primaryPerilCode = this.viewModel.formGroup.controls.primaryPerilCode.value
        updatedCalculation.secondaryPerilCode = this.viewModel.formGroup.controls.secondaryPerilCode.value
  
        updatedCalculation.calculationComment = this.viewModel.formGroup.controls.calculationComment.value
      }
  
      return updatedCalculation
    }
         
    isFormValid (claimForm: vmCalculation) { 
  
      if (!claimForm.primaryPerilCode )  {
  
          displayErrorMessage(this.snackbarService, "Please choose a primary peril")
          return false
  
      } 
      
      return true
    }
  
    doSyncClaimsCodeTables(){
      this.store.dispatch(syncClaimsCodeTables());
    }
  
    showSubmitButton() {
  
      if (this.calculationDetail.isOutOfSync == null) {
          return false
      }
  
      if (this.calculationDetail.linkedProductId && !this.calculationDetail.linkedClaimCalculationGuid) {
        // if linked, the linked calculation should be saved first before we allow submit
        return false
      }
  
      if ( this.calculationDetail.calculationStatusCode === CALCULATION_STATUS_CODE.DRAFT && 
          (
            (this.calculationDetail.claimStatusCode === CLAIM_STATUS_CODE.OPEN && this.calculationDetail.currentHasChequeReqInd == false ) 
            || 
            (this.calculationDetail.claimStatusCode === CLAIM_STATUS_CODE.IN_PROGRESS && this.calculationDetail.currentHasChequeReqInd == true ) 
          )
      ) {
            return true
  
      } else {
  
        return false
      }
  
    }
  
    onSubmit() {
      let  updatedClaim = this.getUpdatedClaim(false);
  
      if (this.isFormValid(updatedClaim) )  {
  
          updatedClaim.calculationStatusCode = CALCULATION_STATUS_CODE.SUBMITTED;
          this.store.dispatch(updateCalculationDetailMetadata(updatedClaim, CALCULATION_UPDATE_TYPE.SUBMIT))
  
      }
    }
  
    isMyFormDirty(){
      const hasChanged = this.isMyFormReallyDirty()
  
      if (hasChanged) {
        this.store.dispatch(setFormStateUnsaved(CALCULATION_DETAIL_COMPONENT_ID, true ));
      }
    }
  
    isMyFormReallyDirty(): boolean {
  
      if (!this.calculationDetail) return false
  
      const frmMain = this.viewModel.formGroup as UntypedFormGroup
  
      if ( areNotEqual (this.calculationDetail.primaryPerilCode, frmMain.controls.primaryPerilCode.value) || 
           areNotEqual (this.calculationDetail.secondaryPerilCode, frmMain.controls.secondaryPerilCode.value) || 
           areNotEqual (this.calculationDetail.calculationComment, frmMain.controls.calculationComment.value)  ) {
          
          return true
      }  
      return false
    }
  
  
    onPrint() {
      const originalTitle = this.titleService.getTitle()
  
      const title = getPrintTitle(this.calculationDetail.commodityName, 
                                  this.calculationDetail.coverageName, 
                                  this.calculationDetail.claimNumber,
                                  this.calculationDetail.policyNumber,
                                  this.calculationDetail.growerNumber)
  
  
      this.titleService.setTitle(title);
  
      window.print()
  
      this.titleService.setTitle(originalTitle);
    }

    
}
