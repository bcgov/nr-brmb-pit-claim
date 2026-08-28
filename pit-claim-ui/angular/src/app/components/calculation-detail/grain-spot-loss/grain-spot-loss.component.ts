import { AfterViewInit, ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { vmCalculation } from 'src/app/conversion/models';
import { BaseComponent } from '../../common/base/base.component';
import { CodeData, ErrorState, LoadState, Option } from 'src/app/store/application/application.state';
import { CalculationDetailGrainSpotLossComponentModel } from './grain-spot-loss.component.model';
import { getCodeOptions } from 'src/app/utils/code-table-utils';
import { CALCULATION_DETAIL_COMPONENT_ID } from 'src/app/store/calculation-detail/calculation-detail.state';
import { loadCalculationDetail, syncClaimsCodeTables, updateCalculationDetailMetadata } from 'src/app/store/calculation-detail/calculation-detail.actions';
import { areNotEqual, CALCULATION_STATUS_CODE, CALCULATION_UPDATE_TYPE, CLAIM_STATUS_CODE, getPrintTitle, makeNumberOnly } from 'src/app/utils';
import { displayErrorMessage } from 'src/app/utils/user-feedback-utils';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import { UntypedFormGroup, ReactiveFormsModule } from '@angular/forms';
import { NgIf, NgFor, NgStyle, DecimalPipe, CurrencyPipe, DatePipe } from '@angular/common';
import { CalculationDetailHeaderComponent } from '../calculation-detail-header/calculation-detail-header.component';
import { MatFormField, MatError } from '@angular/material/form-field';
import { MatSelect, MatOption } from '@angular/material/select';
import { MatTooltip } from '@angular/material/tooltip';
import { MatIcon } from '@angular/material/icon';
import { MatInput } from '@angular/material/input';
import { MatButton } from '@angular/material/button';
import { BaseWrapperComponent } from '../../common/base-wrapper/base-wrapper.component';
import { ParamMap } from '@angular/router';
import { CalculationPrintoutGrainSpotLossComponent } from '../../calculation-printout/grain-spot-loss/grain-spot-loss.component';

@Component({
    selector: 'calculation-detail-grain-spot-loss',
    templateUrl: './grain-spot-loss.component.html',
    styleUrl: './grain-spot-loss.component.scss',
    changeDetection: ChangeDetectionStrategy.OnPush,
    imports: [BaseWrapperComponent, CalculationDetailHeaderComponent, CalculationPrintoutGrainSpotLossComponent, 
      ReactiveFormsModule, MatFormField, NgIf, MatSelect, MatOption, 
      NgFor, MatError, MatTooltip, MatIcon, NgStyle, MatInput, MatButton, DecimalPipe, CurrencyPipe, DatePipe]
})

export class CalculationDetailGrainSpotLossComponent extends BaseComponent implements OnChanges, AfterViewInit {
  displayLabel = "Calculation Detail";
  @Input() calculationDetail: vmCalculation;
  @Input() isUnsaved: boolean;
  @Input() loadState: LoadState;
  @Input() errorState: ErrorState[];

  claimCalculationGuid: string;
  claimNumber: string;
  policyNumber: string;

  calculationStatusOptions: (CodeData|Option)[];
  perilCodeOptions: (CodeData|Option)[];

  eligibleYieldReduction: number
  spotLossReductionValue: number

  totalClaimAmount: number
  calculationComment: string = ""
  
  initModels() {
      this.viewModel = new CalculationDetailGrainSpotLossComponentModel(this.sanitizer, this.fb, this.calculationDetail);
  }

  loadCalculation() {
    this.route.paramMap.subscribe(
        (params: ParamMap) => {
            this.claimCalculationGuid = params.get("claimCalculationGuid") ? params.get("claimCalculationGuid") : null;
            this.claimNumber = params.get("claimNumber") ? params.get("claimNumber") : null;
            this.policyNumber = params.get("policyNumber") ? params.get("policyNumber") : null;
 
            this.store.dispatch(loadCalculationDetail(this.claimCalculationGuid, this.displayLabel, this.claimNumber, this.policyNumber, "false"));                   
        }
    );
  }

  loadPage() {
      this.loadCalculation()
      this.calculationStatusOptions = getCodeOptions("CALCULATION_STATUS_CODE");
      this.perilCodeOptions = getCodeOptions("PERIL_CODE");
      this.componentId = CALCULATION_DETAIL_COMPONENT_ID;
      this.updateView();
  }

  getViewModel(): CalculationDetailGrainSpotLossComponentModel  { //
      return <CalculationDetailGrainSpotLossComponentModel>this.viewModel;
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
    this.loadPage()

    this.viewModel.formGroup.controls.adjustedAcres.valueChanges.subscribe(value => this.updateCalculated() )
    this.viewModel.formGroup.controls.percentYieldReduction.valueChanges.subscribe(value => this.updateCalculated() )
    
  }

  ngOnChanges2(changes: SimpleChanges) {

    if ( changes.calculationDetail && this.calculationDetail ) {

      this.viewModel.formGroup.controls.primaryPerilCode.setValue( this.calculationDetail.primaryPerilCode )
      this.viewModel.formGroup.controls.secondaryPerilCode.setValue( this.calculationDetail.secondaryPerilCode )  
      
      this.viewModel.formGroup.controls.adjustedAcres.setValue( this.calculationDetail.claimCalculationGrainSpotLoss.adjustedAcres ) 
      this.viewModel.formGroup.controls.percentYieldReduction.setValue( this.calculationDetail.claimCalculationGrainSpotLoss.percentYieldReduction ) 
      this.viewModel.formGroup.controls.calculationComment.setValue( this.calculationDetail.calculationComment )  
  
      if (!this.calculationDetail.claimCalculationGrainSpotLoss.claimCalculationGrainSpotLossGuid) {

        // calculate values for new calculations only
        this.calculateValues()

      } else {
        
        this.eligibleYieldReduction = this.calculationDetail.claimCalculationGrainSpotLoss.eligibleYieldReduction
        this.spotLossReductionValue = this.calculationDetail.claimCalculationGrainSpotLoss.spotLossReductionValue
        this.totalClaimAmount = this.calculationDetail.totalClaimAmount

      }

      this.enableDisableFormControls();

    }
  }

  updatingCalculated = false
  updateCalculated() {

      if ( !this.calculationDetail ) return
      if ( this.calculationDetail && !this.calculationDetail.claimCalculationGrainSpotLoss) return
      if ( this.updatingCalculated ) return
      this.updatingCalculated = true

      this.calculateValues()

      this.updatingCalculated = false
  }

  calculateValues() {

    let adjustedAcres = 0
    let percentYieldReduction = 0

    if (this.viewModel.formGroup.controls.adjustedAcres.value && this.viewModel.formGroup.controls.percentYieldReduction.value ) {
      
      if (!isNaN(parseFloat(this.viewModel.formGroup.controls.adjustedAcres.value)) 
        && !isNaN(parseFloat(this.viewModel.formGroup.controls.percentYieldReduction.value)) ) {
      
          adjustedAcres = parseFloat(this.viewModel.formGroup.controls.adjustedAcres.value)
          percentYieldReduction = parseFloat(this.viewModel.formGroup.controls.percentYieldReduction.value)
        }        
    }

    // Line F = D * E 
    this.eligibleYieldReduction = adjustedAcres * percentYieldReduction / 100
    

    // Line G = B * F
    this.spotLossReductionValue = 0
    if (!isNaN(this.calculationDetail.claimCalculationGrainSpotLoss.coverageAmtPerAcre)) {
      this.spotLossReductionValue = this.calculationDetail.claimCalculationGrainSpotLoss.coverageAmtPerAcre * this.eligibleYieldReduction
    }
    
    // Line I = D * ( E - H ) * B 
    if ( !isNaN(this.calculationDetail.claimCalculationGrainSpotLoss.deductible) && !isNaN(this.calculationDetail.claimCalculationGrainSpotLoss.coverageAmtPerAcre)) {

      this.totalClaimAmount = adjustedAcres * 
                            ( percentYieldReduction - this.calculationDetail.claimCalculationGrainSpotLoss.deductible) / 100 * 
                            this.calculationDetail.claimCalculationGrainSpotLoss.coverageAmtPerAcre 

    }
    
  }


  onCancel() {
      this.store.dispatch(loadCalculationDetail(this.claimCalculationGuid, this.displayLabel, this.claimNumber, this.calculationDetail.policyNumber, "false"));
  }

  numberOnly(event): boolean {
    return makeNumberOnly(event)
  }
  
  setComment() {
    this.calculationComment = this.viewModel.formGroup.controls.calculationComment.value
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

  ngAfterViewInit() {
    super.ngAfterViewInit();
  }

  enableDisableFormControls() {
    if(this.calculationDetail){

      if(this.calculationDetail.calculationStatusCode == CALCULATION_STATUS_CODE.DRAFT){
        this.viewModel.formGroup.controls.primaryPerilCode.enable();
        this.viewModel.formGroup.controls.secondaryPerilCode.enable();
        this.viewModel.formGroup.controls.adjustedAcres.enable();
        this.viewModel.formGroup.controls.percentYieldReduction.enable();

      } else {
        this.viewModel.formGroup.controls.primaryPerilCode.disable();
        this.viewModel.formGroup.controls.secondaryPerilCode.disable();
        this.viewModel.formGroup.controls.adjustedAcres.disable();
        this.viewModel.formGroup.controls.percentYieldReduction.disable();
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
        updatedCalculation.secondaryPerilCode = this.viewModel.formGroup.controls.secondaryPerilCode.value

        updatedCalculation.claimCalculationGrainSpotLoss.adjustedAcres = this.viewModel.formGroup.controls.adjustedAcres.value ? parseFloat(this.viewModel.formGroup.controls.adjustedAcres.value) : null

        updatedCalculation.claimCalculationGrainSpotLoss.percentYieldReduction = this.viewModel.formGroup.controls.percentYieldReduction.value ? parseFloat(this.viewModel.formGroup.controls.percentYieldReduction.value) : null
        
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
      
          if (this.calculationDetail.claimCalculationGrainSpotLoss && 
              ( areNotEqual (this.calculationDetail.claimCalculationGrainSpotLoss.adjustedAcres, frmMain.controls.adjustedAcres.value) ||
                areNotEqual (this.calculationDetail.claimCalculationGrainSpotLoss.percentYieldReduction, frmMain.controls.percentYieldReduction.value) 
              )
            ) {
      
            return true
          }
      
          return false
        }


}
