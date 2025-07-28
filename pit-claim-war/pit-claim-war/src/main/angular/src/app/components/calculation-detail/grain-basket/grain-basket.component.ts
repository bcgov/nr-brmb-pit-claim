import { Component, ChangeDetectionStrategy, Input, OnChanges, AfterViewInit, SimpleChanges } from '@angular/core';
import { vmCalculation } from 'src/app/conversion/models';
import {CodeData, Option} from "../../../store/application/application.state";
import { BaseComponent } from '../../common/base/base.component';
import {getCodeOptions} from "../../../utils/code-table-utils";
import { CALCULATION_DETAIL_COMPONENT_ID } from 'src/app/store/calculation-detail/calculation-detail.state';
import { loadCalculationDetail } from 'src/app/store/calculation-detail/calculation-detail.actions';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import { UntypedFormGroup } from '@angular/forms';
import { CalculationDetailGrainBasketComponentModel } from './grain-basket.component.model';

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
  
      // this.viewModel.formGroup.controls.assessedYieldNonPedigree.valueChanges.subscribe(value => this.updateCalculated() )
      // this.viewModel.formGroup.controls.assessedYieldPedigree.valueChanges.subscribe(value => this.updateCalculated() )
      
      // this.viewModel.formGroup.controls.totalClaimAmountNonPedigree.valueChanges.subscribe(value => this.calculateDiffBeteenSumZandY() )
      // this.viewModel.formGroup.controls.totalClaimAmountPedigree.valueChanges.subscribe(value => this.calculateDiffBeteenSumZandY() )
  
    }
  
    ngOnChanges2(changes: SimpleChanges) {
  
      if ( changes.calculationDetail && this.calculationDetail ) {
  
        this.viewModel.formGroup.controls.primaryPerilCode.setValue( this.calculationDetail.primaryPerilCode )
        this.viewModel.formGroup.controls.secondaryPerilCode.setValue( this.calculationDetail.secondaryPerilCode )  
        this.viewModel.formGroup.controls.calculationComment.setValue( this.calculationDetail.calculationComment )  
  
      //   if (!this.calculationDetail.claimCalculationGrainQuantityDetail.claimCalculationGrainQuantityDetailGuid) {
  
      //     // calculate values for new calculations only
      //     this.totalCoverageValue = this.calculationDetail.claimCalculationGrainQuantityDetail.coverageValue
      //     this.calculateValues()
  
      //   } else {
      //     // set the total calculated values
      //     this.totalCoverageValue = this.calculationDetail.claimCalculationGrainQuantity.totalCoverageValue
      //     this.productionGuaranteeAmount = this.calculationDetail.claimCalculationGrainQuantity.productionGuaranteeAmount
      //     this.totalYieldLossValue = this.calculationDetail.claimCalculationGrainQuantity.totalYieldLossValue
      //     this.quantityLossClaim = this.calculationDetail.claimCalculationGrainQuantity.quantityLossClaim
  
      //     this.setValues(this.calculationDetail)
      //   }
  
      //   this.enableDisableFormControls();
  
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
    }
}
