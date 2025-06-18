import { AfterViewInit, ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { BaseComponent } from '../../common/base/base.component';
import { vmCalculation } from 'src/app/conversion/models';
import { CodeData, Option } from 'src/app/store/application/application.state';
import { getCodeOptions } from 'src/app/utils/code-table-utils';
import { CALCULATION_DETAIL_COMPONENT_ID } from 'src/app/store/calculation-detail/calculation-detail.state';
import { CalculationDetailGrainQuantityComponentModel } from './grain-quantity.component.model';
import { loadCalculationDetail } from 'src/app/store/calculation-detail/calculation-detail.actions';
import { makeNumberOnly, roundUpDecimals, setHttpHeaders } from 'src/app/utils';
import { lastValueFrom } from 'rxjs';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'calculation-detail-grain-quantity',
  templateUrl: './grain-quantity.component.html',
  styleUrl: './grain-quantity.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CalculationDetailGrainQuantityComponent extends BaseComponent implements OnChanges, AfterViewInit{

  displayLabel = "Calculation Detail";

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
  maxClaimPayable: number
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
        
        if (this.calculationDetail.linkedProductId) {
          this.showNonPedigreeColumn = true
          this.showPedigreeColumn = true
        }

        if (this.calculationDetail.linkedClaimCalculationGuid) {
          this.loadLinkedCalculation()
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
    
    this.viewModel.formGroup.controls.damagedAcresNonPedigree.valueChanges.subscribe(value => this.updateCalculated() )
    this.viewModel.formGroup.controls.damagedAcresPedigree.valueChanges.subscribe(value => this.updateCalculated() )
    this.viewModel.formGroup.controls.seededAcresNonPedigree.valueChanges.subscribe(value => this.updateCalculated() )
    this.viewModel.formGroup.controls.seededAcresPedigree.valueChanges.subscribe(value => this.updateCalculated() )
    this.viewModel.formGroup.controls.inspEarlyEstYieldNonPedigree.valueChanges.subscribe(value => this.updateCalculated() )
    this.viewModel.formGroup.controls.inspEarlyEstYieldPedigree.valueChanges.subscribe(value => this.updateCalculated() )

    this.viewModel.formGroup.controls.reseedClaim.valueChanges.subscribe(value => this.updateCalculated() )
    this.viewModel.formGroup.controls.advancedClaim.valueChanges.subscribe(value => this.updateCalculated() )

  }

  ngOnChanges2(changes: SimpleChanges) {

    if ( changes.calculationDetail && this.calculationDetail ) {

      this.viewModel.formGroup.controls.primaryPerilCode.setValue( this.calculationDetail.primaryPerilCode )
      this.viewModel.formGroup.controls.secondaryPerilCode.setValue( this.calculationDetail.secondaryPerilCode )  
      
      this.viewModel.formGroup.controls.calculationComment.setValue( this.calculationDetail.calculationComment )  
  
      this.setFormFields(this.calculationDetail)

      if (!this.calculationDetail.claimCalculationGrainQuantityDetail.claimCalculationGrainQuantityDetailGuid) {

        // calculate values for new calculations only
        this.totalCoverageValue = this.calculationDetail.claimCalculationGrainQuantityDetail.coverageValue
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
    // we are loading the linked calculation only if it was already saved

    let url = this.appConfigService.getConfig().rest["cirras_claims"]

    const httpOptions = setHttpHeaders(this.tokenService.getOauthToken())

    // load existing linked calculation
    url = url +"/calculations/" + this.calculationDetail.linkedClaimCalculationGuid 
    url = url + "?doRefreshManualClaimData=false"
    
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

        this.viewModel.formGroup.controls.totalClaimAmountPedigree.setValue( calcDetail.totalClaimAmount ) 

      } else {
        
        this.viewModel.formGroup.controls.assessedYieldNonPedigree.setValue( calcDetail.claimCalculationGrainQuantityDetail.assessedYield ) 
        this.viewModel.formGroup.controls.damagedAcresNonPedigree.setValue( calcDetail.claimCalculationGrainQuantityDetail.damagedAcres ) 
        this.viewModel.formGroup.controls.seededAcresNonPedigree.setValue( calcDetail.claimCalculationGrainQuantityDetail.seededAcres ) 
        this.viewModel.formGroup.controls.inspEarlyEstYieldNonPedigree.setValue( calcDetail.claimCalculationGrainQuantityDetail.inspEarlyEstYield ) 

        this.viewModel.formGroup.controls.totalClaimAmountNonPedigree.setValue( calcDetail.totalClaimAmount ) 
      }

      this.viewModel.formGroup.controls.reseedClaim.setValue( calcDetail.claimCalculationGrainQuantity.reseedClaim ) 
      this.viewModel.formGroup.controls.advancedClaim.setValue( calcDetail.claimCalculationGrainQuantity.advancedClaim ) 

  }

  setValues(calcDetail : vmCalculation) {

    if (calcDetail.isPedigreeInd) {

      this.fiftyPercentProductionGuaranteePedigree = calcDetail.claimCalculationGrainQuantityDetail.fiftyPercentProductionGuarantee
      this.calcEarlyEstYieldPedigree = calcDetail.claimCalculationGrainQuantityDetail.calcEarlyEstYield
      this.earlyEstDeemedYieldValuePedigree = calcDetail.claimCalculationGrainQuantityDetail.earlyEstDeemedYieldValue
      this.yieldValuePedigree = calcDetail.claimCalculationGrainQuantityDetail.yieldValue
      this.yieldValueWithEarlyEstDeemedYieldPedigree = calcDetail.claimCalculationGrainQuantityDetail.yieldValueWithEarlyEstDeemedYield
      this.totalClaimAmountPedigree = calcDetail.totalClaimAmount
    } else {
      this.fiftyPercentProductionGuaranteeNonPedigree = calcDetail.claimCalculationGrainQuantityDetail.fiftyPercentProductionGuarantee
      this.calcEarlyEstYieldNonPedigree = calcDetail.claimCalculationGrainQuantityDetail.calcEarlyEstYield
      this.earlyEstDeemedYieldValueNonPedigree = calcDetail.claimCalculationGrainQuantityDetail.earlyEstDeemedYieldValue
      this.yieldValueNonPedigree = calcDetail.claimCalculationGrainQuantityDetail.yieldValue
      this.yieldValueWithEarlyEstDeemedYieldNonPedigree = calcDetail.claimCalculationGrainQuantityDetail.yieldValueWithEarlyEstDeemedYield
      this.totalClaimAmountNonPedigree = calcDetail.totalClaimAmount
    }

  }
  
  roundUpToPrecision(ctrl, precision){
    let value = this.viewModel.formGroup.controls[ctrl].value
    this.viewModel.formGroup.controls[ctrl].setValue(roundUpDecimals(value, precision))
  }

  updateCalculated() {

      if ( !this.calculationDetail ) return
      if ( this.calculationDetail && !this.calculationDetail.claimCalculationGrainQuantityDetail) return

      this.calculateValues()
  }

  calculateValues() {

    // Line J: ( D - I ) x E
    this.prodGuaranteeMinusAssessmentsNonPedigree = this.calculateProductionGuaranteeWeight(this.calculationDetailNonPedigree, <FormControl>this.viewModel.formGroup.controls.assessedYieldNonPedigree)
    this.prodGuaranteeMinusAssessmentsPedigree = this.calculateProductionGuaranteeWeight(this.calculationDetailPedigree, <FormControl>this.viewModel.formGroup.controls.assessedYieldPedigree)
    
    // Line K: Sum of J
    this.productionGuaranteeAmount = this.prodGuaranteeMinusAssessmentsNonPedigree + this.prodGuaranteeMinusAssessmentsPedigree

    // Line O: D x 50%
    this.fiftyPercentProductionGuaranteeNonPedigree = this.calculateFiftyPercentProductionGuarantee(this.calculationDetailNonPedigree)
    this.fiftyPercentProductionGuaranteePedigree = this.calculateFiftyPercentProductionGuarantee(this.calculationDetailPedigree)
    
    // Line P: O x ( M / N)
    this.calcEarlyEstYieldNonPedigree = this.calculateCalcEarlyEstYield( this.calculationDetailNonPedigree, <FormControl>this.viewModel.formGroup.controls.damagedAcresNonPedigree, <FormControl>this.viewModel.formGroup.controls.seededAcresNonPedigree)
    this.calcEarlyEstYieldPedigree = this.calculateCalcEarlyEstYield( this.calculationDetailPedigree, <FormControl>this.viewModel.formGroup.controls.damagedAcresPedigree, <FormControl>this.viewModel.formGroup.controls.seededAcresPedigree)

    // Line L: ( Q or P ) x E
    this.earlyEstDeemedYieldValueNonPedigree = this.calculateEarlyEstDeemedYieldValue(this.calculationDetailNonPedigree, 
                                                                                      <FormControl>this.viewModel.formGroup.controls.inspEarlyEstYieldNonPedigree, 
                                                                                      this.calcEarlyEstYieldNonPedigree) 

    this.earlyEstDeemedYieldValuePedigree = this.calculateEarlyEstDeemedYieldValue(this.calculationDetailPedigree, 
                                                                                      <FormControl>this.viewModel.formGroup.controls.inspEarlyEstYieldPedigree, 
                                                                                      this.calcEarlyEstYieldPedigree) 
    
    // Line R: H * E
    this.yieldValueNonPedigree = this.calculateYieldValue(this.calculationDetailNonPedigree)
    this.yieldValuePedigree = this.calculateYieldValue(this.calculationDetailPedigree)

    // Line S: R + L
    this.yieldValueWithEarlyEstDeemedYieldNonPedigree = this.calculateYieldValueWithEarlyEstDeemedYield(this.yieldValueNonPedigree, this.earlyEstDeemedYieldValueNonPedigree)
    this.yieldValueWithEarlyEstDeemedYieldPedigree = this.calculateYieldValueWithEarlyEstDeemedYield(this.yieldValuePedigree, this.earlyEstDeemedYieldValuePedigree)

    // Line T: K - Sum of S  
    this.totalYieldLossValue = this.productionGuaranteeAmount - (this.yieldValueWithEarlyEstDeemedYieldNonPedigree + this.yieldValueWithEarlyEstDeemedYieldPedigree)
    // T should have a Min value of 0
    this.totalYieldLossValue = Math.max(0, this.totalYieldLossValue)

    // Line V: G - U
    this.maxClaimPayable = this.calculateMaxClaimPayable()

    // Line Y: Lesser of ( V or W ) - X
    this.quantityLossClaim = this.calculateQuantityLossClaim() 

  }
  
  calculateProductionGuaranteeWeight(calcDetail: vmCalculation, ctlAssessedYield: FormControl){
    // Line J: ( D - I ) x E
    // Production Guarantee - Assessments: Calculated as (  (Production Guarantee - Assessed Yield) * Insurable Value  ) 
    let result = 0
    let assessedYield = 0

    let productionGuaranteeWeight = 
      (calcDetail && calcDetail.claimCalculationGrainQuantityDetail && calcDetail.claimCalculationGrainQuantityDetail.productionGuaranteeWeight) ? 
        calcDetail.claimCalculationGrainQuantityDetail.productionGuaranteeWeight : 0 ;

    if ( ctlAssessedYield && !isNaN(parseFloat(ctlAssessedYield.value ))) {
      assessedYield = parseFloat(ctlAssessedYield.value )
    }

    if (calcDetail && calcDetail.claimCalculationGrainQuantityDetail && calcDetail.claimCalculationGrainQuantityDetail.insurableValue) {
      result = ( productionGuaranteeWeight - assessedYield) * calcDetail.claimCalculationGrainQuantityDetail.insurableValue
    }
    
    return Math.max(0, result)
  }

  calculateFiftyPercentProductionGuarantee(calcDetail: vmCalculation) {
    // 50% of Production Guarantee - calculated as ( Production Guarantee x 50% )
    let result = 0;

    if (calcDetail && calcDetail.claimCalculationGrainQuantityDetail ) {
      result = 0.5 * calcDetail.claimCalculationGrainQuantityDetail.productionGuaranteeWeight
    }
     return result
  }

  calculateCalcEarlyEstYield(calcDetail: vmCalculation, ctlDamagedAcres: FormControl, ctlSeededAcres: FormControl){
    // Calculated Early Establishment Yield - calculated as ( 50% of Production Guarantee x ( Damaged Acres / Acres Seeded) )

    let result = 0

    let damagedAcres = 0
    let seededAcres = 0

    if ( ctlDamagedAcres && !isNaN(parseFloat(ctlDamagedAcres.value ))) {
      damagedAcres = parseFloat(ctlDamagedAcres.value )
    }

    if ( ctlSeededAcres && !isNaN(parseFloat(ctlSeededAcres.value ))) {
      seededAcres = parseFloat(ctlSeededAcres.value )
    }

    if (seededAcres > 0 && calcDetail.claimCalculationGrainQuantityDetail && calcDetail.claimCalculationGrainQuantityDetail.productionGuaranteeWeight) {
      result = 0.5 * calcDetail.claimCalculationGrainQuantityDetail.productionGuaranteeWeight * damagedAcres / seededAcres
    }

    return result
  }

  calculateEarlyEstDeemedYieldValue(calcDetail: vmCalculation, ctlInspEarlyEstYield: FormControl, calcEarlyEstYield: number) {
    // Less Early Establishment Deemed Yield Value → calculated as 
    // ( Inspected Early Establishment Yield (if empty then take Calculated Early Establishment Yield) x Insurable Value per Tonne

    let result = 0
    let inspEarlyEstYield = 0

    if ( ctlInspEarlyEstYield && !isNaN(parseFloat(ctlInspEarlyEstYield.value ))) {
      inspEarlyEstYield = parseFloat(ctlInspEarlyEstYield.value )
    }

    if (calcDetail && calcDetail.claimCalculationGrainQuantityDetail) {
      
      if (inspEarlyEstYield > 0) {
        result = inspEarlyEstYield * calcDetail.claimCalculationGrainQuantityDetail.insurableValue
      } else {
        result = calcEarlyEstYield * calcDetail.claimCalculationGrainQuantityDetail.insurableValue
      }
    }

    return result

  }

  calculateYieldValue(calcDetail: vmCalculation) {
    // Yield Value - Calculated as ( Total Yield Harvested and Appraised * Insurable Value per Tonnes )

    let result = 0

    if (calcDetail && calcDetail.claimCalculationGrainQuantityDetail) {
      result = calcDetail.claimCalculationGrainQuantityDetail.totalYieldToCount * calcDetail.claimCalculationGrainQuantityDetail.insurableValue
    }
    
    return result
  }

  calculateYieldValueWithEarlyEstDeemedYield(yieldValue: number, earlyEstDeemedYieldValue: number) {
    //Calculated as ( Yield Value + Less Early Establishment Deemed Yield Value )
    return yieldValue + earlyEstDeemedYieldValue
  }

  calculateMaxClaimPayable() {
    // calculated as ( Total Pedigreed and Non-Pedigreed Seeds Coverage Value - Reseed Claim )

    let result = 0
    let reseedClaim = 0

    if ( this.viewModel.formGroup.controls.reseedClaim && !isNaN(parseFloat(this.viewModel.formGroup.controls.reseedClaim.value ))) {
      reseedClaim = parseFloat(this.viewModel.formGroup.controls.reseedClaim.value )
    }

    result = Math.max(0, this.totalCoverageValue - reseedClaim)
    return result
  } 

  calculateQuantityLossClaim() {
    // calculated as the lesser of (Maximum Claim Payable or Total Quantity Loss) - Less Advanced Claim(s)

    let result = 0
    let advancedClaim = 0

    if ( this.viewModel.formGroup.controls.advancedClaim && !isNaN(parseFloat(this.viewModel.formGroup.controls.advancedClaim.value ))) {
      advancedClaim = parseFloat(this.viewModel.formGroup.controls.advancedClaim.value )
    }

    result = Math.max(0, ( Math.min(this.maxClaimPayable, this.totalYieldLossValue)  - advancedClaim ) )
    return result
  } 

  onCancel() {
    this.store.dispatch(loadCalculationDetail(this.calculationDetail.claimCalculationGuid, this.displayLabel, this.calculationDetail.claimNumber.toString(), "false"));
  }

  numberOnly(event): boolean {
    return makeNumberOnly(event)
  }

   setComment() {
    this.calculationComment = this.viewModel.formGroup.controls.calculationComment.value
  }

  // enable / disable fields
    enableDisableFormControls() {
      if(this.calculationDetail){
  
        if (this.calculationDetail.linkedProductId) {

          if(this.calculationDetail.isPedigreeInd) {
            // disable non pedigree fields
            this.viewModel.formGroup.controls.assessedYieldNonPedigree.disable();
            this.viewModel.formGroup.controls.damagedAcresNonPedigree.disable();
            this.viewModel.formGroup.controls.seededAcresNonPedigree.disable();
            this.viewModel.formGroup.controls.inspEarlyEstYieldNonPedigree.disable();            
            this.viewModel.formGroup.controls.totalClaimAmountNonPedigree.disable();
          }

          if(!this.calculationDetail.isPedigreeInd) {
            // disable pedigree fields
            this.viewModel.formGroup.controls.assessedYieldPedigree.disable();
            this.viewModel.formGroup.controls.damagedAcresPedigree.disable();
            this.viewModel.formGroup.controls.seededAcresPedigree.disable();
            this.viewModel.formGroup.controls.inspEarlyEstYieldPedigree.disable();
            this.viewModel.formGroup.controls.totalClaimAmountPedigree.disable();
          }
        }

        // this will be useful once we start dealing with replace / reopen calculation
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
      
      let styles =  {
        'grid-template-columns': '3fr 2fr ' + (this.calculationDetail.linkedProductId? '1fr' : '')  +' 1fr' ,
      }

      return styles;
    }

}
