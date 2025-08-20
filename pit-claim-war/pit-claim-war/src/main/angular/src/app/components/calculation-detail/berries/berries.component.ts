import {ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges, AfterViewInit} from "@angular/core";
import {CalculationDetailBerriesComponentModel} from "./berries.component.model";
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
import {areNotEqual, CALCULATION_STATUS_CODE, CALCULATION_UPDATE_TYPE, CLAIM_STATUS_CODE, dollars, getPrintTitle, makeNumberOnly, roundedDollars} from "../../../utils"
import { setFormStateUnsaved } from "src/app/store/application/application.actions";
import { UntypedFormGroup } from "@angular/forms";

@Component({
    selector: "cirras-claims-calculation-detail-berries",
    templateUrl: "./berries.component.html",
    styleUrls: ["../../common/base/base.component.scss",
        "./berries.component.scss"],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class CalculationDetailBerriesComponent extends BaseComponent implements OnChanges, AfterViewInit {
    displayLabel = "Calculation Detail";
    @Input() claimCalculationGuid?: string;
    @Input() claimNumber?: string;
    @Input() calculationDetail: vmCalculation;
    @Input() isUnsaved: boolean;

    calculationStatusOptions: (CodeData|Option)[];
    perilCodeOptions: (CodeData|Option)[];

    adjustmentFactor: number 
    adjustedProductionGuarantee: number
    coverageAmountAdjusted: number
    totalYieldFromDop: number
    estimatedYieldFromAdjuster: number
    totalYieldForCalculation: number
    yieldLossEligible: number
    totalClaimAmount: number

    lineJtext: string = "Yield from Adjuster Estimate (lbs)"

    isClaimTotalHigh: boolean = false

    calculationComment: string = ""

    initModels() {
        this.viewModel = new CalculationDetailBerriesComponentModel(this.sanitizer, this.fb, this.calculationDetail);
    }

    loadPage() {
        this.calculationStatusOptions = getCodeOptions("CALCULATION_STATUS_CODE");
        this.perilCodeOptions = getCodeOptions("PERIL_CODE");
        this.componentId = CALCULATION_DETAIL_COMPONENT_ID;
        this.updateView();
    }

    getViewModel(): CalculationDetailBerriesComponentModel  { //
        return <CalculationDetailBerriesComponentModel>this.viewModel;
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

      this.viewModel.formGroup.controls.confirmedAcres.valueChanges.subscribe(value => this.updateCalculated() )
      this.viewModel.formGroup.controls.harvestedYield.valueChanges.subscribe(value => this.updateCalculated() )
      this.viewModel.formGroup.controls.appraisedYield.valueChanges.subscribe(value => this.updateCalculated() )
      this.viewModel.formGroup.controls.abandonedYield.valueChanges.subscribe(value => this.updateCalculated() )
      this.viewModel.formGroup.controls.totalYieldFromAdjuster.valueChanges.subscribe(value => this.updateCalculated() )
      this.viewModel.formGroup.controls.yieldAssessment.valueChanges.subscribe(value => this.updateCalculated() )
    }

    ngOnChanges2(changes: SimpleChanges) {
        var self = this
        if ( changes.calculationDetail && this.calculationDetail ) {

          this.adjustmentFactor = this.calculationDetail.claimCalculationBerries.adjustmentFactor ? this.calculationDetail.claimCalculationBerries.adjustmentFactor : 1;
          this.adjustedProductionGuarantee = this.calculationDetail.claimCalculationBerries.adjustedProductionGuarantee 
          this.coverageAmountAdjusted = this.calculationDetail.claimCalculationBerries.coverageAmountAdjusted

          this.viewModel.formGroup.controls.primaryPerilCode.setValue( this.calculationDetail.primaryPerilCode )
          this.viewModel.formGroup.controls.secondaryPerilCode.setValue( this.calculationDetail.secondaryPerilCode )
          this.viewModel.formGroup.controls.coverageAmountAdjusted.setValue( dollars(this.calculationDetail.claimCalculationBerries.coverageAmountAdjusted) )

          this.viewModel.formGroup.controls.confirmedAcres.setValue( this.calculationDetail.claimCalculationBerries.confirmedAcres ) 
          this.viewModel.formGroup.controls.harvestedYield.setValue( this.calculationDetail.claimCalculationBerries.harvestedYield ) 
          this.viewModel.formGroup.controls.appraisedYield.setValue( this.calculationDetail.claimCalculationBerries.appraisedYield ) 
          this.viewModel.formGroup.controls.abandonedYield.setValue( this.calculationDetail.claimCalculationBerries.abandonedYield ) 
          this.viewModel.formGroup.controls.totalYieldFromAdjuster.setValue( this.calculationDetail.claimCalculationBerries.totalYieldFromAdjuster ) 
          this.viewModel.formGroup.controls.yieldAssessment.setValue( this.calculationDetail.claimCalculationBerries.yieldAssessment ) 

          this.viewModel.formGroup.controls.calculationComment.setValue( this.calculationDetail.calculationComment )  
        
          if (this.calculationDetail.claimCalculationBerries && 
            this.calculationDetail.claimCalculationBerries.totalYieldFromAdjuster && 
            this.calculationDetail.claimCalculationBerries.totalYieldFromAdjuster  > 0 ) {
              
            this.lineJtext = "Yield from Adjuster Estimate (lbs)"
            this.estimatedYieldFromAdjuster = this.calculationDetail.claimCalculationBerries.totalYieldFromAdjuster

          } else {

            this.lineJtext = "Yield from Declaration of Production"

            if (this.calculationDetail.claimCalculationBerries.totalYieldFromDop) {
              this.estimatedYieldFromAdjuster = this.calculationDetail.claimCalculationBerries.totalYieldFromDop
            }

          }
          
          if (this.calculationDetail.claimCalculationBerries && this.calculationDetail.claimCalculationBerries.totalYieldForCalculation) {
            this.totalYieldForCalculation = this.calculationDetail.claimCalculationBerries.totalYieldForCalculation
          }
          
          if (this.calculationDetail.claimCalculationBerries && this.calculationDetail.claimCalculationBerries.yieldLossEligible) {
            this.yieldLossEligible = this.calculationDetail.claimCalculationBerries.yieldLossEligible
          }

          if (this.calculationDetail.totalClaimAmount) {
            this.totalClaimAmount = this.calculationDetail.totalClaimAmount 
          }
          
          // Line N cannot exceed Line I
          this.isClaimTotalHigh = false
          if (this.calculationDetail.totalClaimAmount > this.calculationDetail.claimCalculationBerries.coverageAmountAdjusted) {
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
          this.viewModel.formGroup.controls.coverageAmountAdjusted.enable();
          this.viewModel.formGroup.controls.confirmedAcres.enable();
          this.viewModel.formGroup.controls.harvestedYield.enable();
          this.viewModel.formGroup.controls.appraisedYield.enable();
          this.viewModel.formGroup.controls.abandonedYield.enable();
          this.viewModel.formGroup.controls.totalYieldFromAdjuster.enable();
          this.viewModel.formGroup.controls.yieldAssessment.enable();
        } else {
          this.viewModel.formGroup.controls.primaryPerilCode.disable();
          this.viewModel.formGroup.controls.secondaryPerilCode.disable();
          this.viewModel.formGroup.controls.coverageAmountAdjusted.disable();
          this.viewModel.formGroup.controls.confirmedAcres.disable();
          this.viewModel.formGroup.controls.harvestedYield.disable();
          this.viewModel.formGroup.controls.appraisedYield.disable();
          this.viewModel.formGroup.controls.abandonedYield.disable();
          this.viewModel.formGroup.controls.totalYieldFromAdjuster.disable();
          this.viewModel.formGroup.controls.yieldAssessment.disable();
        }
      }
    }

    updatingCalculated = false
    updateCalculated() {

        if ( !this.calculationDetail ) return
        if ( this.calculationDetail && !this.calculationDetail.claimCalculationBerries) return
        if ( this.updatingCalculated ) return
        this.updatingCalculated = true

        //calculate the adjustment factor
        let confirmedAcres = (this.viewModel.formGroup.controls.confirmedAcres.value) ? this.viewModel.formGroup.controls.confirmedAcres.value : null
        
        this.adjustmentFactor = 1       // line F
        if (this.calculationDetail.claimCalculationBerries.declaredAcres && 
          this.calculationDetail.claimCalculationBerries.declaredAcres > 0 && 
          !isNaN(confirmedAcres) && 
          parseFloat(confirmedAcres) > 0) {

            this.adjustmentFactor = confirmedAcres / this.calculationDetail.claimCalculationBerries.declaredAcres
            // the adjustment factor should not be greater than 1
            if (this.adjustmentFactor > 1 ) {
              this.adjustmentFactor = 1
            }
        } 

        this.adjustedProductionGuarantee = Math.round( this.calculationDetail.claimCalculationBerries.productionGuarantee * this.adjustmentFactor ) //line  G
        this.coverageAmountAdjusted = this.adjustedProductionGuarantee * this.calculationDetail.claimCalculationBerries.insurableValueSelected  // line I
        
        // if Line E is empty or Line E is equal to Line D then show the coverage amount from CIRRAS
        if (!confirmedAcres || 
            (this.calculationDetail.claimCalculationBerries.declaredAcres && parseFloat(confirmedAcres) == this.calculationDetail.claimCalculationBerries.declaredAcres ) ) {

                if (this.calculationDetail.claimCalculationBerries.maxCoverageAmount) {
                  this.coverageAmountAdjusted = this.calculationDetail.claimCalculationBerries.maxCoverageAmount
                }          
        }
        
        // if this.coverageAmountAdjusted is larger than maxCoverageAmount from CIRRAS then take the CIRRAS value
        if ( this.calculationDetail.claimCalculationBerries.maxCoverageAmount && 
              this.coverageAmountAdjusted > this.calculationDetail.claimCalculationBerries.maxCoverageAmount) {
                
          this.coverageAmountAdjusted = this.calculationDetail.claimCalculationBerries.maxCoverageAmount 
        }

        this.coverageAmountAdjusted = Math.round(this.coverageAmountAdjusted)

        // calculate yields
        const harvestedYield = this.viewModel.formGroup.controls.harvestedYield.value ? parseFloat(this.viewModel.formGroup.controls.harvestedYield.value) : 0  // line 1
        const appraisedYield = this.viewModel.formGroup.controls.appraisedYield.value ? parseFloat(this.viewModel.formGroup.controls.appraisedYield.value) : 0  // line 2
        const abandonedYield = this.viewModel.formGroup.controls.abandonedYield.value ? parseFloat(this.viewModel.formGroup.controls.abandonedYield.value) : 0 // line 3
        this.totalYieldFromDop = Math.round( harvestedYield + appraisedYield + abandonedYield )         // line 4

        let totalYieldFromAdjuster = this.viewModel.formGroup.controls.totalYieldFromAdjuster.value ? parseFloat(this.viewModel.formGroup.controls.totalYieldFromAdjuster.value) : 0 // line 5

        this.estimatedYieldFromAdjuster = 0 // line J
        
        if (totalYieldFromAdjuster > 0 ) {
          this.lineJtext = "Yield from Adjuster Estimate (lbs)"
          this.estimatedYieldFromAdjuster = Math.round(totalYieldFromAdjuster)
        } else {
          this.lineJtext = "Yield from Declaration of Production"
          this.estimatedYieldFromAdjuster = this.totalYieldFromDop
        }

        const yieldAssessment = this.viewModel.formGroup.controls.yieldAssessment.value ? parseFloat(this.viewModel.formGroup.controls.yieldAssessment.value) : 0

        this.totalYieldForCalculation = Math.round(this.estimatedYieldFromAdjuster + yieldAssessment) // L = J + K

        this.yieldLossEligible = Math.max(0, Math.round(this.adjustedProductionGuarantee - this.totalYieldForCalculation) )  // M = G - L

        this.totalClaimAmount =  this.calculationDetail.claimCalculationBerries.insurableValueSelected * this.yieldLossEligible // N = H x M
        
        // If Line N is bigger than Line I or Line M = Line G then display line I in place of line N
        if (this.totalClaimAmount > this.coverageAmountAdjusted || this.adjustedProductionGuarantee == this.yieldLossEligible ) {
          this.totalClaimAmount = this.coverageAmountAdjusted
        }

        // Line N cannot exceed Line I
        this.isClaimTotalHigh = false
        if (this.totalClaimAmount > this.coverageAmountAdjusted) {
          this.isClaimTotalHigh = true
        }

        this.updatingCalculated = false
    }

    ngAfterViewInit() {
        super.ngAfterViewInit();
    }


    onCancel() {
        this.store.dispatch(loadCalculationDetail(this.claimCalculationGuid, this.displayLabel, this.claimNumber, this.calculationDetail.policyNumber, "false"));
    }

    onSave(saveCommentsOnly:boolean) {
        const  updatedClaim = this.getUpdatedClaim(saveCommentsOnly);
        if (this.isFormValid(updatedClaim) )  {

            this.store.dispatch(updateCalculationDetailMetadata(updatedClaim, ""));
            this.doSyncClaimsCodeTables();
        }
    }

    getUpdatedClaim(saveCommentsOnly:boolean) {
        //let updatedClaim = Object.assign( {}, this.calculationDetail, this.viewModel.formGroup.value )
        // unfortunately, I can't use Object.assign here, I need to make a deep copy
        let updatedCalculation = JSON.parse(JSON.stringify(this.calculationDetail));

        if (saveCommentsOnly) {

          updatedCalculation.calculationComment = this.viewModel.formGroup.controls.calculationComment.value
  
        } else {

          updatedCalculation.primaryPerilCode = this.viewModel.formGroup.controls.primaryPerilCode.value
          updatedCalculation.secondaryPerilCode = this.viewModel.formGroup.controls.secondaryPerilCode.value

          updatedCalculation.claimCalculationBerries.confirmedAcres = (this.viewModel.formGroup.controls.confirmedAcres.value) ? parseFloat(this.viewModel.formGroup.controls.confirmedAcres.value) : null
          updatedCalculation.claimCalculationBerries.harvestedYield = this.viewModel.formGroup.controls.harvestedYield.value ? parseFloat(this.viewModel.formGroup.controls.harvestedYield.value) : null
          updatedCalculation.claimCalculationBerries.appraisedYield = this.viewModel.formGroup.controls.appraisedYield.value ? parseFloat(this.viewModel.formGroup.controls.appraisedYield.value) : null
          updatedCalculation.claimCalculationBerries.abandonedYield = this.viewModel.formGroup.controls.abandonedYield.value ? parseFloat(this.viewModel.formGroup.controls.abandonedYield.value) : null
          updatedCalculation.claimCalculationBerries.totalYieldFromAdjuster = this.viewModel.formGroup.controls.totalYieldFromAdjuster.value ? parseFloat(this.viewModel.formGroup.controls.totalYieldFromAdjuster.value) : null
          updatedCalculation.claimCalculationBerries.yieldAssessment = this.viewModel.formGroup.controls.yieldAssessment.value ? parseFloat(this.viewModel.formGroup.controls.yieldAssessment.value) : null

          updatedCalculation.calculationComment = this.viewModel.formGroup.controls.calculationComment.value

          // calculated fields
          updatedCalculation.claimCalculationBerries.adjustmentFactor = this.adjustmentFactor
          updatedCalculation.claimCalculationBerries.adjustedProductionGuarantee = this.adjustedProductionGuarantee
          updatedCalculation.claimCalculationBerries.totalYieldForCalculation = this.totalYieldForCalculation
          updatedCalculation.claimCalculationBerries.totalYieldFromDop = this.totalYieldFromDop
          updatedCalculation.claimCalculationBerries.yieldLossEligible = this.yieldLossEligible

          updatedCalculation.claimCalculationBerries.coverageAmountAdjusted = this.coverageAmountAdjusted
          updatedCalculation.totalClaimAmount = this.totalClaimAmount
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
        let  updatedClaim = this.getUpdatedClaim(false);

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

      if ((this.calculationDetail && this.calculationDetail.isOutOfSync) || this.isClaimTotalHigh ) {
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

    if (this.calculationDetail.claimCalculationBerries && 
        ( areNotEqual (this.calculationDetail.claimCalculationBerries.confirmedAcres, frmMain.controls.confirmedAcres.value) ||
          areNotEqual (this.calculationDetail.claimCalculationBerries.harvestedYield, frmMain.controls.harvestedYield.value) || 
          areNotEqual (this.calculationDetail.claimCalculationBerries.appraisedYield, frmMain.controls.appraisedYield.value) || 
          areNotEqual (this.calculationDetail.claimCalculationBerries.abandonedYield, frmMain.controls.abandonedYield.value) || 
          areNotEqual (this.calculationDetail.claimCalculationBerries.totalYieldFromAdjuster, frmMain.controls.totalYieldFromAdjuster.value) || 
          areNotEqual (this.calculationDetail.claimCalculationBerries.yieldAssessment, frmMain.controls.yieldAssessment.value) 
        )
      ) {

      return true
    }

    return false
  }

}
