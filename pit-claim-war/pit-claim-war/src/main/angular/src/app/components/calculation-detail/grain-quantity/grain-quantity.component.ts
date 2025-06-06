import { AfterViewInit, ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { BaseComponent } from '../../common/base/base.component';
import { vmCalculation } from 'src/app/conversion/models';
import { CodeData, Option } from 'src/app/store/application/application.state';
import { getCodeOptions } from 'src/app/utils/code-table-utils';
import { CALCULATION_DETAIL_COMPONENT_ID } from 'src/app/store/calculation-detail/calculation-detail.state';
import { CalculationDetailGrainQuantityComponentModel } from './grain-quantity.component.model';
import { loadCalculationDetail } from 'src/app/store/calculation-detail/calculation-detail.actions';
import { makeNumberOnly, setHttpHeaders } from 'src/app/utils';
import { lastValueFrom } from 'rxjs';

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

  calculationComment: string = ""

  calculationDetailNonPedigree: vmCalculation;
  calculationDetailPedigree: vmCalculation;

  // shared calculated values
  prodGuaranteeMinusAssessmentsNonPedigree: number
  prodGuaranteeMinusAssessmentsPedigree: number
  fiftyPercentProductionGuaranteeNonPedigree: number
  fiftyPercentProductionGuaranteePedigree: number 
  calcEarlyEstYieldNonPedigree: number
  calcEarlyEstYieldPedigree: number
  earlyEstDeemedYieldValueNonPedigree: number
  earlyEstDeemedYieldValuePedigree: number
  yieldValueNonPedigree: number
  yieldValuePedigree: number
  yieldValueWithEarlyEstDeemedYieldNonPedigree: number
  yieldValueWithEarlyEstDeemedYieldPedigree: number

  // total calculated values
  totalCoverageValue: number
  productionGuaranteeAmount: number
  totalYieldLossValue: number
  quantityLossClaim: number
  totalClaimAmountNonPedigree: number
  totalClaimAmountPedigree: number

  showEarlyEstDeemedYieldValueRows = false
  showNonPedigreeColumn = false  
  showPedigreeColumn = false 

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

        if (this.calculationDetail.isPedigreeInd) {
          this.calculationDetailPedigree = this.calculationDetail
          this.showPedigreeColumn = true
        } else {
          this.calculationDetailNonPedigree = this.calculationDetail
          this.showNonPedigreeColumn = true
        }
        
        if (this.calculationDetail.linkedClaimNumber) {
          this.loadLinkedCalculation()

          this.showNonPedigreeColumn = true
          this.showPedigreeColumn = true
        } 
        
        setTimeout(() => {
            this.cdr.detectChanges();
        });
    }

    this.ngOnChanges2(changes);
  }

  ngOnInit() {
    super.ngOnInit()

    this.viewModel.formGroup.controls.assessedYieldNonPedigree.valueChanges.subscribe(value => this.updateCalculated() )
    this.viewModel.formGroup.controls.assessedYieldPedigree.valueChanges.subscribe(value => this.updateCalculated() )
    
  }

  ngOnChanges2(changes: SimpleChanges) {

    if ( changes.calculationDetail && this.calculationDetail ) {

      this.viewModel.formGroup.controls.primaryPerilCode.setValue( this.calculationDetail.primaryPerilCode )
      this.viewModel.formGroup.controls.secondaryPerilCode.setValue( this.calculationDetail.secondaryPerilCode )  
      
      this.viewModel.formGroup.controls.calculationComment.setValue( this.calculationDetail.calculationComment )  
  
      this.setFormFields(this.calculationDetail)

      if (!this.calculationDetail.claimCalculationGrainQuantityDetail.claimCalculationGrainQuantityDetailGuid) {

        // calculate values for new calculations only
        this.calculateValues()

      } else {
        // set the total calculated values
        this.totalCoverageValue = this.calculationDetail.claimCalculationGrainQuantity.totalCoverageValue
        this.productionGuaranteeAmount = this.calculationDetail.claimCalculationGrainQuantity.productionGuaranteeAmount
        this.totalYieldLossValue = this.calculationDetail.claimCalculationGrainQuantity.totalYieldLossValue
        this.quantityLossClaim = this.calculationDetail.claimCalculationGrainQuantity.quantityLossClaim

        this.setValues(this.calculationDetail)
      }

      this.enableDisableFormControls();

    }
  }

  ngAfterViewInit() {
    super.ngAfterViewInit();
  }

  loadLinkedCalculation() {
    // quick api call to load the linked calculation data

    let url = this.appConfigService.getConfig().rest["cirras_claims"]

    const httpOptions = setHttpHeaders(this.tokenService.getOauthToken())

    if (this.calculationDetail.linkedClaimCalculationGuid) {

      // load existing linked calculation
      url = url +"/calculations/" + this.calculationDetail.linkedClaimCalculationGuid 
      url = url + "?doRefreshManualClaimData=false"

    } else {
      if (this.calculationDetail.linkedClaimNumber) {

        // load new linked calculation
        url = url +"/claims/" + this.calculationDetail.linkedClaimNumber

      } 
    }

    
    var self = this
    return lastValueFrom(this.http.get(url,httpOptions)).then((data: vmCalculation) => {
      if (data.isPedigreeInd) {
        self.calculationDetailPedigree = data
      } else {
        self.calculationDetailNonPedigree = data
      }

      self.setFormFields(data)
      self.setValues(data)
      self.totalCoverageValue = self.calculationDetailNonPedigree.claimCalculationGrainQuantityDetail.coverageValue + self.calculationDetailPedigree.claimCalculationGrainQuantityDetail.coverageValue

      setTimeout(() => {
            this.cdr.detectChanges();
        });
    })

  }

  setFormFields (calcDetail) {
      
    if (calcDetail.isPedigreeInd) {

        this.viewModel.formGroup.controls.assessedYieldPedigree.setValue( calcDetail.claimCalculationGrainQuantityDetail.assessedYield ) 
        this.viewModel.formGroup.controls.damagedAcresPedigree.setValue( calcDetail.claimCalculationGrainQuantityDetail.damagedAcres ) 
        this.viewModel.formGroup.controls.seededAcresPedigree.setValue( calcDetail.claimCalculationGrainQuantityDetail.seededAcres ) 
        this.viewModel.formGroup.controls.inspEarlyEstYieldPedigree.setValue( calcDetail.claimCalculationGrainQuantityDetail.inspEarlyEstYield ) 

      } else {
        
        this.viewModel.formGroup.controls.assessedYieldNonPedigree.setValue( calcDetail.claimCalculationGrainQuantityDetail.assessedYield ) 
        this.viewModel.formGroup.controls.damagedAcresNonPedigree.setValue( calcDetail.claimCalculationGrainQuantityDetail.damagedAcres ) 
        this.viewModel.formGroup.controls.seededAcresNonPedigree.setValue( calcDetail.claimCalculationGrainQuantityDetail.seededAcres ) 
        this.viewModel.formGroup.controls.inspEarlyEstYieldNonPedigree.setValue( calcDetail.claimCalculationGrainQuantityDetail.inspEarlyEstYield ) 

      }
  }

  setValues(calcDetail) {

    if (calcDetail.isPedigreeInd) {
      this.prodGuaranteeMinusAssessmentsPedigree = calcDetail.claimCalculationGrainQuantityDetail.prodGuaranteeMinusAssessments
      this.fiftyPercentProductionGuaranteePedigree = calcDetail.claimCalculationGrainQuantityDetail.fiftyPercentProductionGuarantee
      this.calcEarlyEstYieldPedigree = calcDetail.claimCalculationGrainQuantityDetail.calcEarlyEstYield
      this.earlyEstDeemedYieldValuePedigree = calcDetail.claimCalculationGrainQuantityDetail.earlyEstDeemedYieldValue
      this.yieldValuePedigree = calcDetail.claimCalculationGrainQuantityDetail.yieldValue
      this.yieldValueWithEarlyEstDeemedYieldPedigree = calcDetail.claimCalculationGrainQuantityDetail.yieldValueWithEarlyEstDeemedYield
      this.totalClaimAmountPedigree = calcDetail.claimCalculationGrainQuantityDetail.totalClaimAmount
    } else {
      this.prodGuaranteeMinusAssessmentsNonPedigree = calcDetail.claimCalculationGrainQuantityDetail.prodGuaranteeMinusAssessments
      this.fiftyPercentProductionGuaranteeNonPedigree = calcDetail.claimCalculationGrainQuantityDetail.fiftyPercentProductionGuarantee
      this.calcEarlyEstYieldNonPedigree = calcDetail.claimCalculationGrainQuantityDetail.calcEarlyEstYield
      this.earlyEstDeemedYieldValueNonPedigree = calcDetail.claimCalculationGrainQuantityDetail.earlyEstDeemedYieldValue
      this.yieldValueNonPedigree = calcDetail.claimCalculationGrainQuantityDetail.yieldValue
      this.yieldValueWithEarlyEstDeemedYieldNonPedigree = calcDetail.claimCalculationGrainQuantityDetail.yieldValueWithEarlyEstDeemedYield
      this.totalClaimAmountNonPedigree = calcDetail.claimCalculationGrainQuantityDetail.totalClaimAmount
    }

  }
  
  updatingCalculated = false
  updateCalculated() {

      if ( !this.calculationDetail ) return
      if ( this.calculationDetail && !this.calculationDetail.claimCalculationGrainQuantityDetail) return
      if ( this.updatingCalculated ) return
      this.updatingCalculated = true

      this.calculateValues()

      this.updatingCalculated = false
  }

  calculateValues() {

    let productionGuaranteeWeightNonPedigree = 
        (this.calculationDetailNonPedigree && this.calculationDetailNonPedigree.claimCalculationGrainQuantityDetail && this.calculationDetailNonPedigree.claimCalculationGrainQuantityDetail.productionGuaranteeWeight) ? 
            this.calculationDetailNonPedigree.claimCalculationGrainQuantityDetail.productionGuaranteeWeight : 0 ;

    let productionGuaranteeWeightPedigree = 
        (this.calculationDetailPedigree && this.calculationDetailPedigree.claimCalculationGrainQuantityDetail && this.calculationDetailPedigree.claimCalculationGrainQuantityDetail.productionGuaranteeWeight) ? 
            this.calculationDetailPedigree.claimCalculationGrainQuantityDetail.productionGuaranteeWeight : 0 ;

    // Line J: ( D - I ) x E

    if ( this.viewModel.formGroup.controls.assessedYieldNonPedigree && !isNaN(parseFloat(this.viewModel.formGroup.controls.assessedYieldNonPedigree.value ))) {

      let assessedYield = parseFloat(this.viewModel.formGroup.controls.assessedYieldNonPedigree.value )

      this.prodGuaranteeMinusAssessmentsNonPedigree = 
        ( productionGuaranteeWeightNonPedigree - assessedYield) * this.calculationDetailNonPedigree.claimCalculationGrainQuantityDetail.insurableValue
    }
  
    if ( this.viewModel.formGroup.controls.assessedYieldPedigree && !isNaN(parseFloat(this.viewModel.formGroup.controls.assessedYieldPedigree.value ))) {

      let assessedYield = parseFloat(this.viewModel.formGroup.controls.assessedYieldPedigree.value )
      this.prodGuaranteeMinusAssessmentsPedigree = 
        ( productionGuaranteeWeightPedigree - assessedYield) * this.calculationDetailPedigree.claimCalculationGrainQuantityDetail.insurableValue
    }

    // let adjustedAcres = 0
    // let percentYieldReduction = 0

    // if (this.viewModel.formGroup.controls.adjustedAcres.value && this.viewModel.formGroup.controls.percentYieldReduction.value ) {
      
    //   if (!isNaN(parseFloat(this.viewModel.formGroup.controls.adjustedAcres.value)) 
    //     && !isNaN(parseFloat(this.viewModel.formGroup.controls.percentYieldReduction.value)) ) {
      
    //       adjustedAcres = parseFloat(this.viewModel.formGroup.controls.adjustedAcres.value)
    //       percentYieldReduction = parseFloat(this.viewModel.formGroup.controls.percentYieldReduction.value)
    //     }        
    // }

    // // Line F = D * E 
    // this.eligibleYieldReduction = adjustedAcres * percentYieldReduction / 100
    

    // // Line G = B * F
    // this.spotLossReductionValue = 0
    // if (!isNaN(this.calculationDetail.claimCalculationGrainSpotLoss.coverageAmtPerAcre)) {
    //   this.spotLossReductionValue = this.calculationDetail.claimCalculationGrainSpotLoss.coverageAmtPerAcre * this.eligibleYieldReduction
    // }
    
    // // Line I = D * ( E - H ) * B 
    // if ( !isNaN(this.calculationDetail.claimCalculationGrainSpotLoss.deductible) && !isNaN(this.calculationDetail.claimCalculationGrainSpotLoss.coverageAmtPerAcre)) {

    //   this.totalClaimAmount = adjustedAcres * 
    //                         ( percentYieldReduction - this.calculationDetail.claimCalculationGrainSpotLoss.deductible) / 100 * 
    //                         this.calculationDetail.claimCalculationGrainSpotLoss.coverageAmtPerAcre 

    // }
    
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

  // show / hide columns
  showNonPdgrColumn() {
    if (!this.calculationDetail.isPedigreeInd || this.calculationDetail.linkedClaimNumber ) {
      return true
    }
  }

  showPgdrColumn() {
    if (this.calculationDetail.isPedigreeInd || this.calculationDetail.linkedClaimNumber ) {
      return true
    }
  }

  // enable / disable fields
    enableDisableFormControls() {
      if(this.calculationDetail){
  
        if (this.calculationDetail.linkedClaimNumber) {

          if(this.calculationDetail.isPedigreeInd) {
            // disable non pedigree fields
            this.viewModel.formGroup.controls.assessedYieldNonPedigree.disable();
            this.viewModel.formGroup.controls.damagedAcresNonPedigree.disable();
            this.viewModel.formGroup.controls.seededAcresNonPedigree.disable();
            this.viewModel.formGroup.controls.inspEarlyEstYieldNonPedigree.disable();            
            this.viewModel.formGroup.controls.totalClaimAmountNonPedigree.disable();
          }

          if(!this.calculationDetail.isPedigreeInd) {
            // disable non pedigree fields
            this.viewModel.formGroup.controls.assessedYieldPedigree.disable();
            this.viewModel.formGroup.controls.damagedAcresPedigree.disable();
            this.viewModel.formGroup.controls.seededAcresPedigree.disable();
            this.viewModel.formGroup.controls.inspEarlyEstYieldPedigree.disable();
            this.viewModel.formGroup.controls.totalClaimAmountPedigree.disable();
          }
        }

        // if(this.calculationDetail.calculationStatusCode == CALCULATION_STATUS_CODE.DRAFT){
        //   this.viewModel.formGroup.controls.primaryPerilCode.enable();
        //   this.viewModel.formGroup.controls.secondaryPerilCode.enable();
        //   this.viewModel.formGroup.controls.adjustedAcres.enable();
        //   this.viewModel.formGroup.controls.percentYieldReduction.enable();
  
        // } else {
        //   this.viewModel.formGroup.controls.primaryPerilCode.disable();
        //   this.viewModel.formGroup.controls.secondaryPerilCode.disable();
        //   this.viewModel.formGroup.controls.adjustedAcres.disable();
        //   this.viewModel.formGroup.controls.percentYieldReduction.disable();
        // }
      }
    }

    toggleEarlyEstDeemedYieldValueRows(){
      this.showEarlyEstDeemedYieldValueRows = !this.showEarlyEstDeemedYieldValueRows;
    }

    setStyles(){
      // grid-template-columns: 3fr 2fr 1fr 1fr;
      
      let styles =  {

        'grid-template-columns': '3fr 2fr ' + (this.calculationDetail.linkedClaimNumber? '1fr' : '')  +' 1fr' ,

      }

      // styles = {

      //       'grid-template-columns': '1fr 1fr 1fr ' + (this.isWineGrape()? '3fr 1fr' : ' 4fr')  +' 1fr 1fr ' + (this.isWineGrape()? ' 1fr' : '') ,

      // }

      return styles;
    }

}
