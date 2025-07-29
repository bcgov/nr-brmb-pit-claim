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
import { makeTitleCase } from 'src/app/utils';

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

  totalYieldCoverageValue: number         // Sum of grain_basket_coverage_value and quantity_total_coverage_value
  quantityTotalYieldValue: number         // Sum of claim_calculation_grain_basket_product.yield_value
  totalYieldLoss: number                  // total_yield_coverage_value - quantity_total_yield_value
  quantityTotalYieldLossIndemnity: number // sum((production_quarantee - assessed_yield - total_yield_to_count) x insurable_value) from claim_calculation_grain_basket_product
  totalClaimAmount: number                // claim_calculation_grain_basket.yield_loss - quantity_total_yield_loss_indemnity


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

    getCmdtyName(str) {

      if (str.indexOf(" - PEDIGREED") > 1 ) {
        str = str.substring(0, str.indexOf(" - PEDIGREED")) 
      }

      return makeTitleCase(str)
    }

}
