import { Component, Input, OnChanges, SimpleChanges, AfterViewInit, ChangeDetectionStrategy} from "@angular/core";
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
import {dollars, dollarsToNumber, makeNumberOnly, CALCULATION_STATUS_CODE, CALCULATION_UPDATE_TYPE, getPrintTitle, CLAIM_STATUS_CODE, areNotEqual} from "../../../utils"
import { StrawberriesPlantComponentModel } from "./strawberries-plant.component.model";
import { setFormStateUnsaved } from "src/app/store/application/application.actions";
import { UntypedFormGroup } from "@angular/forms";

@Component({
    selector: 'cirras-calculation-detail-strawberries-plant',
    templateUrl: './strawberries-plant.component.html',
    styleUrls: ['./strawberries-plant.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class CalculationDetailStrawberriesPlantComponent extends BaseComponent implements OnChanges, AfterViewInit {

  displayLabel = "Calculation Detail";
  @Input() claimCalculationGuid?: string;
  @Input() claimNumber?: string;
  @Input() calculationDetail: vmCalculation;
  @Input() isUnsaved: boolean;

  calculationStatusOptions: (CodeData|Option)[];
  perilCodeOptions: (CodeData|Option)[];

  insuredAcres: number;
  deductibleAcres: number;
  totalCoverageAcres: number;
  damagedAcres: number;
  acresLossCovered: number;
  coverageAmount: number;
  totalClaimAmount: number;

  calculationComment: string = ""

  initModels() {
      this.viewModel = new StrawberriesPlantComponentModel(this.sanitizer, this.fb, this.calculationDetail);
  }

  loadPage() {
      this.calculationStatusOptions = getCodeOptions("CALCULATION_STATUS_CODE");
      this.perilCodeOptions = getCodeOptions("PERIL_CODE");
      this.componentId = CALCULATION_DETAIL_COMPONENT_ID;
      this.updateView();
  }

  getViewModel(): StrawberriesPlantComponentModel  { //
      return <StrawberriesPlantComponentModel>this.viewModel;
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
    this.viewModel.formGroup.controls.damagedAcres.valueChanges.subscribe(value => this.updateCalculated() )
    this.viewModel.formGroup.controls.lessAssessmentAmount.valueChanges.subscribe(value => this.updateCalculated() )
  }

  ngOnChanges2(changes: SimpleChanges) {
      var self = this
      if ( changes.calculationDetail && this.calculationDetail ) {

        // Line C = Lessor of A or B
        let tempInsuredAcres = (this.calculationDetail.claimCalculationPlantAcres.declaredAcres ? this.calculationDetail.claimCalculationPlantAcres.declaredAcres : 0 )
        if (this.calculationDetail.claimCalculationPlantAcres.insuredAcres) {
          tempInsuredAcres = this.calculationDetail.claimCalculationPlantAcres.insuredAcres
        } else {
          if (this.calculationDetail.claimCalculationPlantAcres.confirmedAcres) {
            tempInsuredAcres = tempInsuredAcres - this.calculationDetail.claimCalculationPlantAcres.confirmedAcres
          }
        }
        this.insuredAcres = tempInsuredAcres

        // Line E - C x D
        if (this.calculationDetail.claimCalculationPlantAcres.deductibleAcres) {
          this.deductibleAcres = this.calculationDetail.claimCalculationPlantAcres.deductibleAcres
        } else {
          this.deductibleAcres = this.insuredAcres * this.calculationDetail.claimCalculationPlantAcres.deductibleLevel / 100
        }
        
        // Line F = C - E
        if (this.calculationDetail.claimCalculationPlantAcres.totalCoverageAcres) {
          this.totalCoverageAcres = this.calculationDetail.claimCalculationPlantAcres.totalCoverageAcres
        } else {
          this.totalCoverageAcres = Math.max( 0, this.insuredAcres - this.deductibleAcres)
        }
        
        //
        this.damagedAcres = this.calculationDetail.claimCalculationPlantAcres.damagedAcres

        // Line I = G - H
        if (this.calculationDetail.claimCalculationPlantAcres.acresLossCovered) {
          this.acresLossCovered = this.calculationDetail.claimCalculationPlantAcres.acresLossCovered
        } else {
          this.acresLossCovered = Math.max(0, (this.damagedAcres ? this.damagedAcres : 0) - this.deductibleAcres ) 
        }
        
        // Line K = I x J
        if (this.calculationDetail.claimCalculationPlantAcres.coverageAmount){
          this.coverageAmount = this.calculationDetail.claimCalculationPlantAcres.coverageAmount
        } else {
          if (!isNaN(this.acresLossCovered && this.calculationDetail.claimCalculationPlantAcres.insurableValue) ){
            this.coverageAmount = this.acresLossCovered * this.calculationDetail.claimCalculationPlantAcres.insurableValue 
          } else {
            this.coverageAmount = 0 
          }
        }

        // Line M = K - L
        if (this.calculationDetail.totalClaimAmount) {
          this.totalClaimAmount = this.calculationDetail.totalClaimAmount
        } else {
          const tempAmount = (this.calculationDetail.claimCalculationPlantAcres.lessAssessmentAmount ? this.calculationDetail.claimCalculationPlantAcres.lessAssessmentAmount : 0)
          this.totalClaimAmount = Math.max(0, this.coverageAmount - tempAmount)
        }
        
        this.viewModel.formGroup.controls.primaryPerilCode.setValue( this.calculationDetail.primaryPerilCode )
        this.viewModel.formGroup.controls.secondaryPerilCode.setValue( this.calculationDetail.secondaryPerilCode )
        
        this.viewModel.formGroup.controls.confirmedAcres.setValue( this.calculationDetail.claimCalculationPlantAcres.confirmedAcres ) 
        this.viewModel.formGroup.controls.damagedAcres.setValue( this.calculationDetail.claimCalculationPlantAcres.damagedAcres ) 
        this.viewModel.formGroup.controls.lessAssessmentReason.setValue( this.calculationDetail.claimCalculationPlantAcres.lessAssessmentReason ) 
        this.viewModel.formGroup.controls.lessAssessmentAmount.setValue( dollars( this.calculationDetail.claimCalculationPlantAcres.lessAssessmentAmount ) )
        this.viewModel.formGroup.controls.calculationComment.setValue( this.calculationDetail.calculationComment )  

        this.enableDisableFormControls();
      }
  }

  enableDisableFormControls() {
    if(this.calculationDetail){

      if(this.calculationDetail.calculationStatusCode == CALCULATION_STATUS_CODE.DRAFT){
        this.viewModel.formGroup.controls.primaryPerilCode.enable();
        this.viewModel.formGroup.controls.secondaryPerilCode.enable();
        this.viewModel.formGroup.controls.confirmedAcres.enable();
        this.viewModel.formGroup.controls.damagedAcres.enable();
        this.viewModel.formGroup.controls.lessAssessmentReason.enable();
        this.viewModel.formGroup.controls.lessAssessmentAmount.enable();

      } else {
        this.viewModel.formGroup.controls.primaryPerilCode.disable();
        this.viewModel.formGroup.controls.secondaryPerilCode.disable();
        this.viewModel.formGroup.controls.confirmedAcres.disable();
        this.viewModel.formGroup.controls.damagedAcres.disable();
        this.viewModel.formGroup.controls.lessAssessmentReason.disable();
        this.viewModel.formGroup.controls.lessAssessmentAmount.disable();
      }
    }
  }

  updatingCalculated = false
  updateCalculated() {

      if ( !this.calculationDetail ) return
      if ( this.calculationDetail && !this.calculationDetail.claimCalculationPlantAcres) return
      if ( this.updatingCalculated ) return
      this.updatingCalculated = true

      // Line C = lessor of A or B    
      if (!this.viewModel.formGroup.controls.confirmedAcres.value) {
        this.insuredAcres = this.calculationDetail.claimCalculationPlantAcres.declaredAcres
      } else {
        const enteredConfirmedAcres = parseFloat(this.viewModel.formGroup.controls.confirmedAcres.value) // line B
        this.insuredAcres = Math.min(enteredConfirmedAcres, this.calculationDetail.claimCalculationPlantAcres.declaredAcres)
      }

      // Line E = C x D
      if (!isNaN(this.deductibleAcres) && !isNaN(this.calculationDetail.claimCalculationPlantAcres.deductibleLevel) ) {
        this.deductibleAcres = this.insuredAcres * this.calculationDetail.claimCalculationPlantAcres.deductibleLevel / 100
      }
      
      // Line F = C - E
      if (!isNaN(this.insuredAcres) && !isNaN(this.deductibleAcres)) {
        this.totalCoverageAcres = Math.max( 0, this.insuredAcres - this.deductibleAcres)
      }      

      // Line I = G - H
      const enteredDamagedAcres = this.viewModel.formGroup.controls.damagedAcres.value ? parseFloat(this.viewModel.formGroup.controls.damagedAcres.value) : 0
      this.acresLossCovered = Math.max(0, enteredDamagedAcres - this.deductibleAcres)

      // Line K = I x J
      if (!isNaN(this.acresLossCovered ) ){
        this.coverageAmount = this.acresLossCovered * this.calculationDetail.claimCalculationPlantAcres.insurableValue 
      }

      // Line M = K - L
      let enteredLessAssessmentAmount = 0
      if (this.viewModel.formGroup.controls.lessAssessmentAmount.value) {
        enteredLessAssessmentAmount = dollarsToNumber(this.viewModel.formGroup.controls.lessAssessmentAmount.value)
      }    

      this.totalClaimAmount = Math.max( 0, this.coverageAmount  - enteredLessAssessmentAmount)

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
      // making a deep copy here
      let updatedCalculation = JSON.parse(JSON.stringify(this.calculationDetail));

      if (saveCommentsOnly) {

          updatedCalculation.calculationComment = this.viewModel.formGroup.controls.calculationComment.value

      } else {
        // user fields
        updatedCalculation.primaryPerilCode = this.viewModel.formGroup.controls.primaryPerilCode.value
        updatedCalculation.secondaryPerilCode = this.viewModel.formGroup.controls.secondaryPerilCode.value

        updatedCalculation.claimCalculationPlantAcres.confirmedAcres = this.viewModel.formGroup.controls.confirmedAcres.value ? parseFloat(this.viewModel.formGroup.controls.confirmedAcres.value) : null

        updatedCalculation.claimCalculationPlantAcres.insuredAcres = this.insuredAcres

        updatedCalculation.claimCalculationPlantAcres.damagedAcres = this.viewModel.formGroup.controls.damagedAcres.value ? parseFloat(this.viewModel.formGroup.controls.damagedAcres.value) : null
        updatedCalculation.claimCalculationPlantAcres.lessAssessmentReason = this.viewModel.formGroup.controls.lessAssessmentReason.value

        let enteredLessAssessmentAmount = 0
        if (this.viewModel.formGroup.controls.lessAssessmentAmount.value ) {
          enteredLessAssessmentAmount = dollarsToNumber(this.viewModel.formGroup.controls.lessAssessmentAmount.value)
        }  
        updatedCalculation.claimCalculationPlantAcres.lessAssessmentAmount = enteredLessAssessmentAmount
        
        updatedCalculation.calculationComment = this.viewModel.formGroup.controls.calculationComment.value
      }

      return updatedCalculation
  }

  isFormValid (claimForm: vmCalculation) { 

      if (!claimForm.primaryPerilCode )  {

          displayErrorMessage(this.snackbarService, "Please choose a primary peril")
          return false

      }     
      
      if (claimForm.claimCalculationPlantAcres.damagedAcres &&
        claimForm.claimCalculationPlantAcres.damagedAcres > claimForm.claimCalculationPlantAcres.insuredAcres) {

        displayErrorMessage(this.snackbarService, "Damaged acres should not exceed insured acres")
        return false

      }
      
      return true

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

          updatedClaim.calculationStatusCode = CALCULATION_STATUS_CODE.SUBMITTED;
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
    
        if (this.calculationDetail.claimCalculationPlantAcres && 
            ( areNotEqual (this.calculationDetail.claimCalculationPlantAcres.confirmedAcres, frmMain.controls.confirmedAcres.value) ||
              areNotEqual (this.calculationDetail.claimCalculationPlantAcres.damagedAcres, frmMain.controls.damagedAcres.value) || 
              areNotEqual (this.calculationDetail.claimCalculationPlantAcres.lessAssessmentReason, frmMain.controls.lessAssessmentReason.value) || 
              areNotEqual (this.calculationDetail.claimCalculationPlantAcres.lessAssessmentAmount, frmMain.controls.lessAssessmentAmount.value) 
            )
          ) {
    
          return true
        }
    
        return false
      }

  
}
