import { Component, ChangeDetectionStrategy, Input, OnChanges, AfterViewInit, SimpleChanges } from '@angular/core';
import { vmCalculation } from 'src/app/conversion/models';
import {CodeData, Option} from "../../../store/application/application.state";
import { BaseComponent } from '../../common/base/base.component';
import { GrainUnseededComponentModel } from './grain-unseeded.component.model';
import {getCodeOptions} from "../../../utils/code-table-utils";
import { CALCULATION_DETAIL_COMPONENT_ID } from 'src/app/store/calculation-detail/calculation-detail.state';
import { loadCalculationDetail, syncClaimsCodeTables, updateCalculationDetailMetadata } from 'src/app/store/calculation-detail/calculation-detail.actions';
import { CALCULATION_STATUS_CODE, CALCULATION_UPDATE_TYPE, CLAIM_STATUS_CODE, getPrintTitle, makeNumberOnly } from 'src/app/utils';
import { displayErrorMessage } from 'src/app/utils/user-feedback-utils';

@Component({
  selector: 'calculation-detail-grain-unseeded',
  templateUrl: './grain-unseeded.component.html',
  styleUrl: './grain-unseeded.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,

})
export class CalculationDetailGrainUnseededComponent extends BaseComponent implements OnChanges, AfterViewInit {

  displayLabel = "Calculation Detail";
  @Input() claimCalculationGuid?: string;
  @Input() calculationDetail: vmCalculation;

  calculationStatusOptions: (CodeData|Option)[];
  perilCodeOptions: (CodeData|Option)[];

  adjustedAcres: number;
  deductibleAcres: number;
  maxEligibleAcres: number;
  coverageValue: number;
  eligibleUnseededAcres: number;
  totalClaimAmount: number;

  calculationComment: string = ""

  initModels() {
      this.viewModel = new GrainUnseededComponentModel(this.sanitizer, this.fb, this.calculationDetail);
  }

  loadPage() {
      this.calculationStatusOptions = getCodeOptions("CALCULATION_STATUS_CODE");
      this.perilCodeOptions = getCodeOptions("PERIL_CODE");
      this.componentId = CALCULATION_DETAIL_COMPONENT_ID;
      this.updateView();
  }

  getViewModel(): GrainUnseededComponentModel  { //
      return <GrainUnseededComponentModel>this.viewModel;
  }

  ngOnInit() {
    super.ngOnInit()

    this.viewModel.formGroup.controls.lessAdjustmentAcres.valueChanges.subscribe(value => this.updateCalculated() )
    this.viewModel.formGroup.controls.unseededAcres.valueChanges.subscribe(value => this.updateCalculated() )
    this.viewModel.formGroup.controls.lessAssessmentAcres.valueChanges.subscribe(value => this.updateCalculated() )
  }

  ngOnChanges(changes: SimpleChanges) {
    super.ngOnChanges(changes);
    if (changes.claimCalculationGuid) {
        this.claimCalculationGuid = changes.claimCalculationGuid.currentValue;
    }

    if (changes.calculationDetail) {
        this.calculationDetail = changes.calculationDetail.currentValue;
        this.calculationComment = this.calculationDetail.calculationComment;
        
        setTimeout(() => {
            this.cdr.detectChanges();
        });
    }
    
    this.ngOnChanges2(changes);
  }

  ngOnChanges2(changes: SimpleChanges) {
        if ( changes.calculationDetail && this.calculationDetail ) {
  
          this.viewModel.formGroup.controls.primaryPerilCode.setValue( this.calculationDetail.primaryPerilCode )
          
          this.viewModel.formGroup.controls.lessAdjustmentAcres.setValue( this.calculationDetail.claimCalculationGrainUnseeded.lessAdjustmentAcres ) 
          this.viewModel.formGroup.controls.unseededAcres.setValue( this.calculationDetail.claimCalculationGrainUnseeded.unseededAcres ) 
          this.viewModel.formGroup.controls.lessAssessmentAcres.setValue( this.calculationDetail.claimCalculationGrainUnseeded.lessAssessmentAcres ) 
          this.viewModel.formGroup.controls.calculationComment.setValue( this.calculationDetail.calculationComment )  
  
          if (!this.calculationDetail.claimCalculationGrainUnseeded.claimCalculationGrainUnseededGuid) {

            // calculate values for new calculations only
            this.calculateValues()

          } else {
            
            this.adjustedAcres = this.calculationDetail.claimCalculationGrainUnseeded.adjustedAcres
            this.deductibleAcres = this.calculationDetail.claimCalculationGrainUnseeded.deductibleAcres
            this.maxEligibleAcres = this.calculationDetail.claimCalculationGrainUnseeded.maxEligibleAcres
            this.coverageValue = this.calculationDetail.claimCalculationGrainUnseeded.coverageValue
            this.eligibleUnseededAcres = this.calculationDetail.claimCalculationGrainUnseeded.eligibleUnseededAcres
            this.totalClaimAmount = this.calculationDetail.totalClaimAmount

          }
          

          this.enableDisableFormControls();
        }
    }
    
    updatingCalculated = false
    updateCalculated() {
  
        if ( !this.calculationDetail ) return
        if ( this.calculationDetail && !this.calculationDetail.claimCalculationGrainUnseeded) return
        if ( this.updatingCalculated ) return
        this.updatingCalculated = true
  
        this.calculateValues()
  
        this.updatingCalculated = false
    }

    calculateValues() {
      // Line C = Total Acres Insured - Less Adjustment  
      if (!this.viewModel.formGroup.controls.lessAdjustmentAcres.value) {
        this.adjustedAcres = this.calculationDetail.claimCalculationGrainUnseeded.insuredAcres
      } else {
        const enteredLessAdjustmentAcres = parseFloat(this.viewModel.formGroup.controls.lessAdjustmentAcres.value) // line B
        this.adjustedAcres = Math.max( 0, this.calculationDetail.claimCalculationGrainUnseeded.insuredAcres - enteredLessAdjustmentAcres)
      }

      // Line E = C x D
      if (!isNaN(this.adjustedAcres) && !isNaN(this.calculationDetail.claimCalculationGrainUnseeded.deductibleLevel) ) {
        this.deductibleAcres = this.adjustedAcres * this.calculationDetail.claimCalculationGrainUnseeded.deductibleLevel / 100
      }
      
      // Line F = C - E
      if (!isNaN(this.adjustedAcres) && !isNaN(this.deductibleAcres)) {
        this.maxEligibleAcres = Math.max( 0, this.adjustedAcres - this.deductibleAcres)
      }      

      // Line H = F x G 
      if (!isNaN(this.maxEligibleAcres) && !isNaN(this.calculationDetail.claimCalculationGrainUnseeded.insurableValue) ){
        this.coverageValue = this.maxEligibleAcres * this.calculationDetail.claimCalculationGrainUnseeded.insurableValue 
      }

      // Line L = I - J - K
      let enteredUnseededAcres = 0
      if (!isNaN(parseFloat(this.viewModel.formGroup.controls.unseededAcres.value))) {
        enteredUnseededAcres = parseFloat(this.viewModel.formGroup.controls.unseededAcres.value) // line I
      }

      let enteredLessAssessmentAcres = 0 
      if (!isNaN(parseFloat(this.viewModel.formGroup.controls.lessAssessmentAcres.value))) {
        enteredLessAssessmentAcres = parseFloat(this.viewModel.formGroup.controls.lessAssessmentAcres.value) // line J
      }

      if (!isNaN(this.deductibleAcres)) {
        this.eligibleUnseededAcres = Math.max( 0, enteredUnseededAcres - enteredLessAssessmentAcres - this.deductibleAcres)
      }
      
      // Line M = L x G
      if (!isNaN(this.eligibleUnseededAcres) && !isNaN(this.calculationDetail.claimCalculationGrainUnseeded.insurableValue)) {
        this.totalClaimAmount = this.eligibleUnseededAcres  * this.calculationDetail.claimCalculationGrainUnseeded.insurableValue 
      }
    }

    ngAfterViewInit() {
        super.ngAfterViewInit();
    }
    
    onCancel() {
      this.store.dispatch(loadCalculationDetail(this.claimCalculationGuid, this.displayLabel, this.calculationDetail.claimNumber.toString(), "false"));
    }
  
    numberOnly(event): boolean {
      return makeNumberOnly(event)
    }

    setComment() {
      this.calculationComment = this.viewModel.formGroup.controls.calculationComment.value
    }

    enableDisableFormControls() {
      if(this.calculationDetail){
  
        if(this.calculationDetail.calculationStatusCode == CALCULATION_STATUS_CODE.DRAFT){
          this.viewModel.formGroup.controls.primaryPerilCode.enable();
          this.viewModel.formGroup.controls.lessAdjustmentAcres.enable();
          this.viewModel.formGroup.controls.unseededAcres.enable();
          this.viewModel.formGroup.controls.lessAssessmentAcres.enable();
  
        } else {
          this.viewModel.formGroup.controls.primaryPerilCode.disable();
          this.viewModel.formGroup.controls.lessAdjustmentAcres.disable();
          this.viewModel.formGroup.controls.unseededAcres.disable();
          this.viewModel.formGroup.controls.lessAssessmentAcres.disable();
        }
      }
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
    
            updatedCalculation.claimCalculationGrainUnseeded.lessAdjustmentAcres = this.viewModel.formGroup.controls.lessAdjustmentAcres.value ? parseFloat(this.viewModel.formGroup.controls.lessAdjustmentAcres.value) : null
    
            updatedCalculation.claimCalculationGrainUnseeded.unseededAcres = this.viewModel.formGroup.controls.unseededAcres.value ? parseFloat(this.viewModel.formGroup.controls.unseededAcres.value) : null
    
            updatedCalculation.claimCalculationGrainUnseeded.lessAssessmentAcres = this.viewModel.formGroup.controls.lessAssessmentAcres.value ? parseFloat(this.viewModel.formGroup.controls.lessAssessmentAcres.value) : null
            
            updatedCalculation.calculationComment = this.viewModel.formGroup.controls.calculationComment.value
          }
    
          return updatedCalculation
      }
    
      isFormValid (claimForm: vmCalculation) { 
    
          if (!claimForm.primaryPerilCode )  {
    
              displayErrorMessage(this.snackbarService, "Please choose a primary peril")
              return false
    
          }     

          if (claimForm.claimCalculationGrainUnseeded.unseededAcres > claimForm.claimCalculationGrainUnseeded.insuredAcres){

            displayErrorMessage(this.snackbarService, "Unseeded acres should be no more than Total Acres Insured")
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
    
}
