import { AfterViewInit, ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { BaseComponent } from '../../common/base/base.component';
import { vmCalculation } from 'src/app/conversion/models';
import { CodeData, Option } from 'src/app/store/application/application.state';
import { getCodeOptions } from 'src/app/utils/code-table-utils';
import { CALCULATION_DETAIL_COMPONENT_ID } from 'src/app/store/calculation-detail/calculation-detail.state';
import { CalculationDetailGrainQuantityComponentModel } from './grain-quantity.component.model';
import { loadCalculationDetail } from 'src/app/store/calculation-detail/calculation-detail.actions';

@Component({
  selector: 'calculation-detail-grain-quantity',
  templateUrl: './grain-quantity.component.html',
  styleUrl: './grain-quantity.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CalculationDetailGrainQuantityComponent extends BaseComponent implements OnChanges, AfterViewInit{

  displayLabel = "Calculation Detail";
  @Input() claimCalculationGuid?: string;
  @Input() claimNumber?: string;
  @Input() calculationDetail: vmCalculation;
  
  calculationStatusOptions: (CodeData|Option)[];
  perilCodeOptions: (CodeData|Option)[];

  totalClaimAmount: number
  calculationComment: string = ""

  prodGuaranteeMinusAssessments: number
  earlyEstDeemedYieldValue: number
  
  
  initModels() {
    this.viewModel = new CalculationDetailGrainQuantityComponentModel(this.sanitizer, this.fb, this.calculationDetail);
  }

  loadPage() {
      this.calculationStatusOptions = getCodeOptions("CALCULATION_STATUS_CODE");
      this.perilCodeOptions = getCodeOptions("PERIL_CODE");
      this.componentId = CALCULATION_DETAIL_COMPONENT_ID;
      this.updateView();
  }

  getViewModel(): CalculationDetailGrainQuantityComponentModel  { 
      return <CalculationDetailGrainQuantityComponentModel>this.viewModel;
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

    // this.viewModel.formGroup.controls.adjustedAcres.valueChanges.subscribe(value => this.updateCalculated() )
    // this.viewModel.formGroup.controls.percentYieldReduction.valueChanges.subscribe(value => this.updateCalculated() )
    
  }

  ngOnChanges2(changes: SimpleChanges) {

    if ( changes.calculationDetail && this.calculationDetail ) {

      this.viewModel.formGroup.controls.primaryPerilCode.setValue( this.calculationDetail.primaryPerilCode )
      this.viewModel.formGroup.controls.secondaryPerilCode.setValue( this.calculationDetail.secondaryPerilCode )  
      
      this.viewModel.formGroup.controls.adjustedAcres.setValue( this.calculationDetail.claimCalculationGrainSpotLoss.adjustedAcres ) 
      this.viewModel.formGroup.controls.percentYieldReduction.setValue( this.calculationDetail.claimCalculationGrainSpotLoss.percentYieldReduction ) 
      this.viewModel.formGroup.controls.calculationComment.setValue( this.calculationDetail.calculationComment )  
  
      // if (!this.calculationDetail.claimCalculationGrainSpotLoss.claimCalculationGrainSpotLossGuid) {

      //   // calculate values for new calculations only
      //   this.calculateValues()

      // } else {
        
      //   this.eligibleYieldReduction = this.calculationDetail.claimCalculationGrainSpotLoss.eligibleYieldReduction
      //   this.spotLossReductionValue = this.calculationDetail.claimCalculationGrainSpotLoss.spotLossReductionValue
      //   this.totalClaimAmount = this.calculationDetail.totalClaimAmount

      // }

      // this.enableDisableFormControls();

    }
  }


   ngAfterViewInit() {
    super.ngAfterViewInit();
  }

    onCancel() {
        this.store.dispatch(loadCalculationDetail(this.claimCalculationGuid, this.displayLabel, this.claimNumber, "false"));
    }


}
