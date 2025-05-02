import { AfterViewInit, ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { vmCalculation } from 'src/app/conversion/models';
import { BaseComponent } from '../../common/base/base.component';
import { CodeData, Option } from 'src/app/store/application/application.state';
import { CalculationDetailGrainSpotLossComponentModel } from './grain-spot-loss.component.model';
import { getCodeOptions } from 'src/app/utils/code-table-utils';
import { CALCULATION_DETAIL_COMPONENT_ID } from 'src/app/store/calculation-detail/calculation-detail.state';
import { loadCalculationDetail } from 'src/app/store/calculation-detail/calculation-detail.actions';
import { getPrintTitle, makeNumberOnly } from 'src/app/utils';

@Component({
  selector: 'calculation-detail-grain-spot-loss',
  templateUrl: './grain-spot-loss.component.html',
  styleUrl: './grain-spot-loss.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})

export class CalculationDetailGrainSpotLossComponent extends BaseComponent implements OnChanges, AfterViewInit {
  displayLabel = "Calculation Detail";
  @Input() claimCalculationGuid?: string;
  @Input() claimNumber?: string;
  @Input() calculationDetail: vmCalculation;
  

  calculationStatusOptions: (CodeData|Option)[];
  perilCodeOptions: (CodeData|Option)[];

  eligibleYieldReduction: number
  spotLossReductionValue: number

  totalClaimAmount: number
  calculationComment: string = ""
  
    initModels() {
        this.viewModel = new CalculationDetailGrainSpotLossComponentModel(this.sanitizer, this.fb, this.calculationDetail);
    }
  
    loadPage() {
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
      super.ngOnInit()
  
      this.viewModel.formGroup.controls.adjustedAcres.valueChanges.subscribe(value => this.updateCalculated() )
      this.viewModel.formGroup.controls.percentYieldReduction.valueChanges.subscribe(value => this.updateCalculated() )
      
    }
  
    ngOnChanges2(changes: SimpleChanges) {

      if ( changes.calculationDetail && this.calculationDetail ) {

        this.viewModel.formGroup.controls.primaryPerilCode.setValue( this.calculationDetail.primaryPerilCode )
        this.viewModel.formGroup.controls.secondaryPerilCode.setValue( this.calculationDetail.secondaryPerilCode )  
        
        this.viewModel.formGroup.controls.adjustedAcres.setValue( this.calculationDetail.claimCalculationGrainSpotLoss.adjustedAcres ) 
        this.viewModel.formGroup.controls.percentYieldReduction.setValue( this.calculationDetail.claimCalculationGrainSpotLoss.percentYieldReduction ) 
        
        if (!this.calculationDetail.claimCalculationGrainSpotLoss.claimCalculationGrainSpotLossGuid) {

          // calculate values for new calculations only
          this.calculateValues()

        } else {
          
          this.eligibleYieldReduction = this.calculationDetail.claimCalculationGrainSpotLoss.eligibleYieldReduction
          this.spotLossReductionValue = this.calculationDetail.claimCalculationGrainSpotLoss.spotLossReductionValue
          this.totalClaimAmount = this.calculationDetail.totalClaimAmount

        }

        // this.enableDisableFormControls();

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
        this.store.dispatch(loadCalculationDetail(this.claimCalculationGuid, this.displayLabel, this.claimNumber, "false"));
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

}
