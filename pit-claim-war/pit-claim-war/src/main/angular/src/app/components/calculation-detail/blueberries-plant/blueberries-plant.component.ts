import { Component, Input, OnChanges, SimpleChanges, AfterViewInit, ChangeDetectionStrategy} from "@angular/core";
import {CalculationDetailBlueberriesPlantComponentModel} from "./blueberries-plant.component.model";
import {
  loadCalculationDetail,
  updateCalculationDetailMetadata
} from "../../../store/calculation-detail/calculation-detail.actions";
import {CALCULATION_DETAIL_COMPONENT_ID} from "../../../store/calculation-detail/calculation-detail.state";
import {BaseComponent} from "../../common/base/base.component";
import {vmCalculation} from "../../../conversion/models";
import {CodeData, Option} from "../../../store/application/application.state";
import {getCodeOptions} from "../../../utils/code-table-utils";
import {syncClaimsCodeTables} from "../../../store/calculation-detail/calculation-detail.actions";
import { displayErrorMessage  } from "../../../utils/user-feedback-utils";
import {CALCULATION_STATUS_CODE, CALCULATION_UPDATE_TYPE, CLAIM_STATUS_CODE, getPrintTitle, makeNumberOnly, roundedDollars} from "../../../utils"

@Component({
  selector: 'cirras-calculation-detail-berries-plant',
  templateUrl: './blueberries-plant.component.html',
  styleUrls: ['./blueberries-plant.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CalculationDetailBlueberriesPlantComponent extends BaseComponent implements OnChanges, AfterViewInit {
  displayLabel = "Calculation Detail";
  @Input() claimCalculationGuid?: string;
  @Input() claimNumber?: string;
  @Input() calculationDetail: vmCalculation;
  
  calculationStatusOptions: (CodeData|Option)[];
  perilCodeOptions: (CodeData|Option)[];

  adjustedUnits: number;
  deductibleUnits: number;
  totalCoverageUnits: number;
  coverageAmount: number;
  damagedUnits: number;
  totalDamagedUnits: number;
  totalClaimAmount: number;

  isClaimTotalHigh: boolean = false;

  calculationComment: string = ""

  initModels() {
      this.viewModel = new CalculationDetailBlueberriesPlantComponentModel(this.sanitizer, this.fb, this.calculationDetail);
  }

  loadPage() {
      this.calculationStatusOptions = getCodeOptions("CALCULATION_STATUS_CODE");
      this.perilCodeOptions = getCodeOptions("PERIL_CODE");
      this.componentId = CALCULATION_DETAIL_COMPONENT_ID;
      this.updateView();
  }

  getViewModel(): CalculationDetailBlueberriesPlantComponentModel  { //
      return <CalculationDetailBlueberriesPlantComponentModel>this.viewModel;
  }

  ngOnChanges(changes: SimpleChanges) {
    super.ngOnChanges(changes);
    if (changes.claimCalculationGuid) {
        this.claimCalculationGuid = changes.claimCalculationGuid.currentValue;
    }

    if (changes.claimNumber) {
      this.claimNumber = changes.claimNumber.currentValue;
    }

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

    this.viewModel.formGroup.controls.lessAdjustmentUnits.valueChanges.subscribe(value => this.updateCalculated() )
    this.viewModel.formGroup.controls.damagedUnits.valueChanges.subscribe(value => this.updateCalculated() )
    this.viewModel.formGroup.controls.lessAssessmentUnits.valueChanges.subscribe(value => this.updateCalculated() )
  }

  ngOnChanges2(changes: SimpleChanges) {
      var self = this
      if ( changes.calculationDetail && this.calculationDetail ) {


        let adjustedUnits = 0
        if (this.calculationDetail.claimCalculationPlantUnits.adjustedUnits) {
          adjustedUnits = this.calculationDetail.claimCalculationPlantUnits.adjustedUnits
        } else {
          if (this.calculationDetail.claimCalculationPlantUnits.lessAdjustmentUnits) {
            adjustedUnits = this.calculationDetail.claimCalculationPlantUnits.insuredUnits - this.calculationDetail.claimCalculationPlantUnits.lessAdjustmentUnits
          } else {
            adjustedUnits = this.calculationDetail.claimCalculationPlantUnits.insuredUnits
          }
        }
        this.adjustedUnits = adjustedUnits

        if (this.calculationDetail.claimCalculationPlantUnits.deductibleUnits) {
          this.deductibleUnits = this.calculationDetail.claimCalculationPlantUnits.deductibleUnits
        } else {
          this.deductibleUnits = this.adjustedUnits * this.calculationDetail.claimCalculationPlantUnits.deductibleLevel / 100
        }

        if (this.calculationDetail.claimCalculationPlantUnits.totalCoverageUnits) {
          this.totalCoverageUnits = this.calculationDetail.claimCalculationPlantUnits.totalCoverageUnits
        } else {
          this.totalCoverageUnits = Math.max( 0, this.adjustedUnits - this.deductibleUnits)
        }

        if (this.calculationDetail.claimCalculationPlantUnits.coverageAmount) {
          this.coverageAmount = this.calculationDetail.claimCalculationPlantUnits.coverageAmount
        } else {
          this.coverageAmount = this.totalCoverageUnits * this.calculationDetail.claimCalculationPlantUnits.insurableValue // F x G
        }
        
        this.damagedUnits = this.calculationDetail.claimCalculationPlantUnits.damagedUnits
        this.totalDamagedUnits = this.calculationDetail.claimCalculationPlantUnits.totalDamagedUnits
        this.totalClaimAmount = Math.round(this.calculationDetail.totalClaimAmount)

        this.viewModel.formGroup.controls.primaryPerilCode.setValue( this.calculationDetail.primaryPerilCode )
        this.viewModel.formGroup.controls.secondaryPerilCode.setValue( this.calculationDetail.secondaryPerilCode )

        this.viewModel.formGroup.controls.lessAdjustmentReason.setValue( this.calculationDetail.claimCalculationPlantUnits.lessAdjustmentReason ) 
        this.viewModel.formGroup.controls.lessAdjustmentUnits.setValue( this.calculationDetail.claimCalculationPlantUnits.lessAdjustmentUnits ) 
        this.viewModel.formGroup.controls.damagedUnits.setValue( this.calculationDetail.claimCalculationPlantUnits.damagedUnits ) 
        this.viewModel.formGroup.controls.lessAssessmentReason.setValue( this.calculationDetail.claimCalculationPlantUnits.lessAssessmentReason ) 
        this.viewModel.formGroup.controls.lessAssessmentUnits.setValue( this.calculationDetail.claimCalculationPlantUnits.lessAssessmentUnits ) 
        
        this.viewModel.formGroup.controls.calculationComment.setValue( this.calculationDetail.calculationComment )  

        this.isClaimTotalHigh = false
        if (this.totalClaimAmount > this.coverageAmount) {
          this.isClaimTotalHigh = true
        }

        this.enableDisableFormControls();

      }
  }

  enableDisableFormControls() {
    if(this.calculationDetail){

      if(this.calculationDetail.calculationStatusCode == CALCULATION_STATUS_CODE.DRAFT){
        this.viewModel.formGroup.controls.primaryPerilCode.enable();
        this.viewModel.formGroup.controls.secondaryPerilCode.enable();
        this.viewModel.formGroup.controls.lessAdjustmentReason.enable();
        this.viewModel.formGroup.controls.lessAdjustmentUnits.enable();
        this.viewModel.formGroup.controls.damagedUnits.enable();
        this.viewModel.formGroup.controls.lessAssessmentReason.enable();
        this.viewModel.formGroup.controls.lessAssessmentUnits.enable();

      } else {
        this.viewModel.formGroup.controls.primaryPerilCode.disable();
        this.viewModel.formGroup.controls.secondaryPerilCode.disable();
        this.viewModel.formGroup.controls.lessAdjustmentReason.disable();
        this.viewModel.formGroup.controls.lessAdjustmentUnits.disable();
        this.viewModel.formGroup.controls.damagedUnits.disable();
        this.viewModel.formGroup.controls.lessAssessmentReason.disable();
        this.viewModel.formGroup.controls.lessAssessmentUnits.disable();
      }
    }
  }

  updatingCalculated = false
  updateCalculated() {

      if ( !this.calculationDetail ) return
      if ( this.calculationDetail && !this.calculationDetail.claimCalculationPlantUnits) return
      if ( this.updatingCalculated ) return
      this.updatingCalculated = true

      // Line C = A - B
      let enteredAdjustedUnits = this.viewModel.formGroup.controls.lessAdjustmentUnits.value ?
          parseFloat(this.viewModel.formGroup.controls.lessAdjustmentUnits.value) : 0 // line B


      if (this.calculationDetail.claimCalculationPlantUnits.insuredUnits) {
        this.adjustedUnits =  this.calculationDetail.claimCalculationPlantUnits.insuredUnits -  enteredAdjustedUnits
      }

      // Line E = C x D
      if (!isNaN(this.adjustedUnits) && !isNaN(this.calculationDetail.claimCalculationPlantUnits.deductibleLevel) ) {
        this.deductibleUnits = this.adjustedUnits * this.calculationDetail.claimCalculationPlantUnits.deductibleLevel / 100
      }
      
      // Line F = C - E
      if (!isNaN(this.adjustedUnits) && !isNaN(this.deductibleUnits)) {
        this.totalCoverageUnits = Math.max( 0, this.adjustedUnits - this.deductibleUnits)
      }      

      // Line H = F x G
      if (!isNaN(this.totalCoverageUnits) && !isNaN(this.calculationDetail.claimCalculationPlantUnits.insurableValue)) {
        this.coverageAmount = Math.round(this.totalCoverageUnits * this.calculationDetail.claimCalculationPlantUnits.insurableValue)
      }
      
      // Line L = I - J - E
      const enteredDamagedUnits = this.viewModel.formGroup.controls.damagedUnits.value ? parseFloat(this.viewModel.formGroup.controls.damagedUnits.value) : 0
      const enteredLessAssessmentUnits = this.viewModel.formGroup.controls.lessAssessmentUnits.value ? parseFloat(this.viewModel.formGroup.controls.lessAssessmentUnits.value) : 0
      this.totalDamagedUnits = Math.max(0, enteredDamagedUnits - enteredLessAssessmentUnits - this.deductibleUnits)

      // Line M = L x G
      this.totalClaimAmount =  Math.round(this.totalDamagedUnits * this.calculationDetail.claimCalculationPlantUnits.insurableValue)

      this.isClaimTotalHigh = false
      if (this.totalClaimAmount > this.coverageAmount) {
        this.isClaimTotalHigh = true
      }
  
      this.updatingCalculated = false
  }

  ngAfterViewInit() {
      super.ngAfterViewInit();
  }


  onCancel() {
      this.store.dispatch(loadCalculationDetail(this.claimCalculationGuid, this.displayLabel, this.claimNumber, "false"));
  }

  onSave(saveCommentsOnly:boolean) {
      const updatedClaim = this.getUpdatedClaim(saveCommentsOnly);
      if (this.isFormValid(updatedClaim) )  {

          this.store.dispatch(updateCalculationDetailMetadata(updatedClaim, ""));
          this.doSyncClaimsCodeTables();
      }
  }

  getUpdatedClaim(saveCommentsOnly:boolean) {
      // making a deep copy
      let updatedCalculation = JSON.parse(JSON.stringify(this.calculationDetail));

      if (saveCommentsOnly) {

        updatedCalculation.calculationComment = this.viewModel.formGroup.controls.calculationComment.value

      } else {

        // user fields
        updatedCalculation.primaryPerilCode = this.viewModel.formGroup.controls.primaryPerilCode.value
        updatedCalculation.secondaryPerilCode = this.viewModel.formGroup.controls.secondaryPerilCode.value

        updatedCalculation.claimCalculationPlantUnits.lessAdjustmentReason = this.viewModel.formGroup.controls.lessAdjustmentReason.value
        updatedCalculation.claimCalculationPlantUnits.lessAdjustmentUnits = this.viewModel.formGroup.controls.lessAdjustmentUnits.value ? parseFloat(this.viewModel.formGroup.controls.lessAdjustmentUnits.value) : null
        updatedCalculation.claimCalculationPlantUnits.damagedUnits = this.viewModel.formGroup.controls.damagedUnits.value ? parseFloat(this.viewModel.formGroup.controls.damagedUnits.value) : null
        updatedCalculation.claimCalculationPlantUnits.lessAssessmentReason = this.viewModel.formGroup.controls.lessAssessmentReason.value
        updatedCalculation.claimCalculationPlantUnits.lessAssessmentUnits = this.viewModel.formGroup.controls.lessAssessmentUnits.value ? parseFloat(this.viewModel.formGroup.controls.lessAssessmentUnits.value) : null
        
        updatedCalculation.calculationComment = this.viewModel.formGroup.controls.calculationComment.value
        
      }
      
      return updatedCalculation
  }

  isFormValid (claimForm: vmCalculation) {      
      if (claimForm.primaryPerilCode )  {
          return true;
      }
      else {
          displayErrorMessage(this.snackbarService, "Please choose a primary peril")
          return false;
      }      
  }

  doSyncClaimsCodeTables(){
    this.store.dispatch(syncClaimsCodeTables());
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

  onSubmit() {
      let  updatedClaim = this.getUpdatedClaim(false); // save all changes not just the comments

      if (this.isFormValid(updatedClaim) )  {

          updatedClaim.calculationStatusCode = CALCULATION_STATUS_CODE.SUBMITTED
          this.store.dispatch(updateCalculationDetailMetadata(updatedClaim, CALCULATION_UPDATE_TYPE.SUBMIT))

      }
  }

  onReopen() {
    const  updatedClaim = this.getUpdatedClaim(true);
    if (this.isFormValid(updatedClaim) )  {

        updatedClaim.calculationStatusCode = CALCULATION_STATUS_CODE.DRAFT;
        this.store.dispatch(updateCalculationDetailMetadata(updatedClaim, ""));
        this.doSyncClaimsCodeTables();
    }
}

showReopenButton() {

    //Allow to reopen a calculation if the claim has been amended and is in status In Progress
    // and the calculation is in status submitted
    if ( this.calculationDetail.calculationStatusCode === CALCULATION_STATUS_CODE.SUBMITTED && 
        this.calculationDetail.claimStatusCode === CLAIM_STATUS_CODE.IN_PROGRESS && 
        this.calculationDetail.currentHasChequeReqInd == true){
        return true
    } else {
      return false
    }
}    

numberOnly(event): boolean {
    return makeNumberOnly(event)
  }

  showSubmitButton() {

    if (this.calculationDetail.isOutOfSync == null) {
      return false
    }

    if (this.calculationDetail.isOutOfSync || this.isClaimTotalHigh ) {
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

  makeDollarsRounded( val ) {
    return roundedDollars(val)
  }

  setComment() {
    this.calculationComment = this.viewModel.formGroup.controls.calculationComment.value
  }
  
}
