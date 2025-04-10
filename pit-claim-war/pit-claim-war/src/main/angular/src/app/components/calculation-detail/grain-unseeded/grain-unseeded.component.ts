import { Component, ChangeDetectionStrategy, Input, OnChanges, AfterViewInit, SimpleChanges } from '@angular/core';
import { vmCalculation } from 'src/app/conversion/models';
import {CodeData, Option} from "../../../store/application/application.state";
import { BaseComponent } from '../../common/base/base.component';
import { GrainUnseededComponentModel } from './grain-unseeded.component.model';
import {getCodeOptions} from "../../../utils/code-table-utils";
import { CALCULATION_DETAIL_COMPONENT_ID } from 'src/app/store/calculation-detail/calculation-detail.state';
import { loadCalculationDetail } from 'src/app/store/calculation-detail/calculation-detail.actions';
import { makeNumberOnly } from 'src/app/utils';

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
  
          // TODO 
          // this.enableDisableFormControls();
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
      const enteredUnseededAcres = parseFloat(this.viewModel.formGroup.controls.unseededAcres.value) // line I
      const enteredLessAssessmentAcres = parseFloat(this.viewModel.formGroup.controls.lessAssessmentAcres.value) // line J
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
}
