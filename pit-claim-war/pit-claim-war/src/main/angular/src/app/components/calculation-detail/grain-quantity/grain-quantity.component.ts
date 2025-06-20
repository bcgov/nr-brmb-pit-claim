import { AfterViewInit, ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { BaseComponent } from '../../common/base/base.component';
import { vmCalculation } from 'src/app/conversion/models';
import { CodeData, Option } from 'src/app/store/application/application.state';
import { getCodeOptions } from 'src/app/utils/code-table-utils';
import { CALCULATION_DETAIL_COMPONENT_ID } from 'src/app/store/calculation-detail/calculation-detail.state';
import { CalculationDetailGrainQuantityComponentModel } from './grain-quantity.component.model';
import { loadCalculationDetail, syncClaimsCodeTables, updateCalculationDetailMetadata } from 'src/app/store/calculation-detail/calculation-detail.actions';
import { areNotEqual, CALCULATION_STATUS_CODE, CALCULATION_UPDATE_TYPE, CLAIM_STATUS_CODE, makeNumberOnly, roundUpDecimals, setHttpHeaders } from 'src/app/utils';
import { lastValueFrom } from 'rxjs';
import { FormControl, UntypedFormGroup } from '@angular/forms';
import { displayErrorMessage } from 'src/app/utils/user-feedback-utils';
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';

@Component({
  selector: 'calculation-detail-grain-quantity',
  templateUrl: './grain-quantity.component.html',
  styleUrl: './grain-quantity.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CalculationDetailGrainQuantityComponent extends BaseComponent implements OnChanges, AfterViewInit{

  displayLabel = "Calculation Detail";

  @Input() calculationDetail: vmCalculation;
  @Input() isUnsaved: boolean;
  
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

    // Populate Line X 
    if (!this.calculationDetail.claimCalculationGuid) {
      if (this.calculationDetail.isPedigreeInd) {      
        this.viewModel.formGroup.controls.totalClaimAmountPedigree.setValue(this.quantityLossClaim)
      } else {
        this.viewModel.formGroup.controls.totalClaimAmountNonPedigree.setValue(this.quantityLossClaim)
      }
    }
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
    this.store.dispatch(setFormStateUnsaved(CALCULATION_DETAIL_COMPONENT_ID, false ));
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

      this.enableAllFields()

      if (this.calculationDetail.calculationStatusCode !== CALCULATION_STATUS_CODE.DRAFT) {
        this.viewModel.formGroup.controls.primaryPerilCode.disable();
        this.viewModel.formGroup.controls.secondaryPerilCode.disable();
        this.viewModel.formGroup.controls.reseedClaim.disable();
        this.viewModel.formGroup.controls.advancedClaim.disable();
      }

      if(this.calculationDetail.isPedigreeInd == true || this.calculationDetail.calculationStatusCode !== CALCULATION_STATUS_CODE.DRAFT) {
        // disable non pedigree fields
        this.viewModel.formGroup.controls.assessedYieldNonPedigree.disable();
        this.viewModel.formGroup.controls.damagedAcresNonPedigree.disable();
        this.viewModel.formGroup.controls.seededAcresNonPedigree.disable();
        this.viewModel.formGroup.controls.inspEarlyEstYieldNonPedigree.disable();            
        this.viewModel.formGroup.controls.totalClaimAmountNonPedigree.disable();
      }

      if(this.calculationDetail.isPedigreeInd == false || this.calculationDetail.calculationStatusCode !== CALCULATION_STATUS_CODE.DRAFT) {
        // disable pedigree fields
        this.viewModel.formGroup.controls.assessedYieldPedigree.disable();
        this.viewModel.formGroup.controls.damagedAcresPedigree.disable();
        this.viewModel.formGroup.controls.seededAcresPedigree.disable();
        this.viewModel.formGroup.controls.inspEarlyEstYieldPedigree.disable();
        this.viewModel.formGroup.controls.totalClaimAmountPedigree.disable();
      }

    }
  }

  enableAllFields(){
    this.viewModel.formGroup.controls.primaryPerilCode.enable();
    this.viewModel.formGroup.controls.secondaryPerilCode.enable();
    this.viewModel.formGroup.controls.assessedYieldNonPedigree.enable();
    this.viewModel.formGroup.controls.assessedYieldPedigree.enable();
    this.viewModel.formGroup.controls.damagedAcresNonPedigree.enable();
    this.viewModel.formGroup.controls.damagedAcresPedigree.enable();
    this.viewModel.formGroup.controls.seededAcresNonPedigree.enable();
    this.viewModel.formGroup.controls.seededAcresPedigree.enable();
    this.viewModel.formGroup.controls.inspEarlyEstYieldNonPedigree.enable();    
    this.viewModel.formGroup.controls.inspEarlyEstYieldPedigree.enable();
    this.viewModel.formGroup.controls.totalClaimAmountNonPedigree.enable();
    this.viewModel.formGroup.controls.totalClaimAmountPedigree.enable();
    this.viewModel.formGroup.controls.reseedClaim.enable();
    this.viewModel.formGroup.controls.advancedClaim.enable();
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

  onSave(saveCommentsOnly:boolean) {
      const  updatedClaim = this.getUpdatedClaim(saveCommentsOnly);
      if (this.isFormValid(updatedClaim) )  {

          this.store.dispatch(updateCalculationDetailMetadata(updatedClaim, ""));
          this.doSyncClaimsCodeTables();

          this.store.dispatch(setFormStateUnsaved(CALCULATION_DETAIL_COMPONENT_ID, false ));
      }
  }

  getUpdatedClaim(saveCommentsOnly:boolean) {
    // making a deep copy here
    let updatedCalculation :vmCalculation = JSON.parse(JSON.stringify(this.calculationDetail));

    if (saveCommentsOnly) {

        updatedCalculation.calculationComment = this.viewModel.formGroup.controls.calculationComment.value

    } else {
      // user fields
      updatedCalculation.primaryPerilCode = this.viewModel.formGroup.controls.primaryPerilCode.value
      updatedCalculation.secondaryPerilCode = this.viewModel.formGroup.controls.secondaryPerilCode.value

      // claimCalculationGrainQuantityGuid
      updatedCalculation.claimCalculationGrainQuantityGuid = updatedCalculation.claimCalculationGrainQuantity.claimCalculationGrainQuantityGuid

      let assessedYield = 0
      let damagedAcres = 0
      let seededAcres = 0
      let inspEarlyEstYield = 0

      if (updatedCalculation.isPedigreeInd) {

        assessedYield = this.viewModel.formGroup.controls.assessedYieldPedigree.value ? parseFloat(this.viewModel.formGroup.controls.assessedYieldPedigree.value) : null
        damagedAcres = this.viewModel.formGroup.controls.damagedAcresPedigree.value ? parseFloat(this.viewModel.formGroup.controls.damagedAcresPedigree.value) : null
        seededAcres = this.viewModel.formGroup.controls.seededAcresPedigree.value ? parseFloat(this.viewModel.formGroup.controls.seededAcresPedigree.value) : null
        inspEarlyEstYield = this.viewModel.formGroup.controls.inspEarlyEstYieldPedigree.value ? parseFloat(this.viewModel.formGroup.controls.inspEarlyEstYieldPedigree.value) : null
      
        if (updatedCalculation.linkedProductId) {
          // take the user entered value
          updatedCalculation.totalClaimAmount = this.viewModel.formGroup.controls.totalClaimAmountPedigree.value ? parseFloat(this.viewModel.formGroup.controls.totalClaimAmountPedigree.value) : 0
        } else {
          // take the calculated value
          updatedCalculation.totalClaimAmount = this.quantityLossClaim
        }
        
      } else {
      
        assessedYield = this.viewModel.formGroup.controls.assessedYieldNonPedigree.value ? parseFloat(this.viewModel.formGroup.controls.assessedYieldNonPedigree.value) : null
        damagedAcres = this.viewModel.formGroup.controls.damagedAcresNonPedigree.value ? parseFloat(this.viewModel.formGroup.controls.damagedAcresNonPedigree.value) : null
        seededAcres = this.viewModel.formGroup.controls.seededAcresNonPedigree.value ? parseFloat(this.viewModel.formGroup.controls.seededAcresNonPedigree.value) : null
        inspEarlyEstYield = this.viewModel.formGroup.controls.inspEarlyEstYieldNonPedigree.value ? parseFloat(this.viewModel.formGroup.controls.inspEarlyEstYieldNonPedigree.value) : null
      
        if (updatedCalculation.linkedProductId) {
          // take the user entered value
          updatedCalculation.totalClaimAmount = this.viewModel.formGroup.controls.totalClaimAmountNonPedigree.value ? parseFloat(this.viewModel.formGroup.controls.totalClaimAmountNonPedigree.value) : 0
        } else {
          // take the calculated value
          updatedCalculation.totalClaimAmount = this.quantityLossClaim
        }
        
      }

      updatedCalculation.claimCalculationGrainQuantityDetail.assessedYield = assessedYield
      updatedCalculation.claimCalculationGrainQuantityDetail.damagedAcres = damagedAcres
      updatedCalculation.claimCalculationGrainQuantityDetail.seededAcres = seededAcres
      updatedCalculation.claimCalculationGrainQuantityDetail.inspEarlyEstYield = inspEarlyEstYield

      updatedCalculation.claimCalculationGrainQuantity.reseedClaim = this.viewModel.formGroup.controls.reseedClaim.value ? parseFloat(this.viewModel.formGroup.controls.reseedClaim.value) : null
      updatedCalculation.claimCalculationGrainQuantity.advancedClaim = this.viewModel.formGroup.controls.advancedClaim.value ? parseFloat(this.viewModel.formGroup.controls.advancedClaim.value) : null

      updatedCalculation.calculationComment = this.viewModel.formGroup.controls.calculationComment.value
    }

    return updatedCalculation
  }
       
  isFormValid (claimForm: vmCalculation) { 

    if (!claimForm.primaryPerilCode )  {

        displayErrorMessage(this.snackbarService, "Please choose a primary peril")
        return false

    }  

    // M <= N
    let damagedAcresNonPedigree = this.viewModel.formGroup.controls.damagedAcresNonPedigree.value ? parseFloat(this.viewModel.formGroup.controls.damagedAcresNonPedigree.value) : 0
    let seededAcresNonPedigree = this.viewModel.formGroup.controls.seededAcresNonPedigree.value ? parseFloat(this.viewModel.formGroup.controls.seededAcresNonPedigree.value) : 0
    if (damagedAcresNonPedigree > seededAcresNonPedigree) {
      displayErrorMessage(this.snackbarService, "The Non-Pedigree Damaged Acres should not exceed the Non-Pedigree Seeded Acres.")
      return false
    }

    let damagedAcresPedigree = this.viewModel.formGroup.controls.damagedAcresPedigree.value ? parseFloat(this.viewModel.formGroup.controls.damagedAcresPedigree.value) : 0
    let seededAcresPedigree = this.viewModel.formGroup.controls.seededAcresPedigree.value ? parseFloat(this.viewModel.formGroup.controls.seededAcresPedigree.value) : 0
    if (damagedAcresPedigree > seededAcresPedigree) {
      displayErrorMessage(this.snackbarService, "The Pedigree Damaged Acres should not exceed the Pedigree Seeded Acres.")
      return false
    }

    // Submit is only possible if: the payed out amount of the claim doesn’t exceed the coverage value on line F
    let totalClaimAmountNonPedigree = this.viewModel.formGroup.controls.totalClaimAmountNonPedigree.value ? parseFloat(this.viewModel.formGroup.controls.totalClaimAmountNonPedigree.value) : 0
    let totalClaimAmountPedigree = this.viewModel.formGroup.controls.totalClaimAmountPedigree.value ? parseFloat(this.viewModel.formGroup.controls.totalClaimAmountPedigree.value) : 0
    
    if (this.calculationDetailNonPedigree && totalClaimAmountNonPedigree > this.calculationDetailNonPedigree.claimCalculationGrainQuantityDetail.coverageValue) {
      displayErrorMessage(this.snackbarService, "The Non-Pedigree Total Claim Amount should not exceed the Non-Pedigree Coverage Value.")
      return false
    }

    if (this.calculationDetailPedigree && totalClaimAmountPedigree > this.calculationDetailPedigree.claimCalculationGrainQuantityDetail.coverageValue) {
      displayErrorMessage(this.snackbarService, "The Pedigree Total Claim Amount should not exceed the Pedigree Coverage Value.")
      return false
    }

    // If there is a second calculation, the sum of both payed out amounts have to be equal to Quantity Loss Claim on line Y.
    if (this.calculationDetail.linkedClaimCalculationGuid && this.isLinkedCalculationSubmitted() ) {
      
      if (  (Math.round( (totalClaimAmountNonPedigree + totalClaimAmountPedigree) * 100) / 100) !== (Math.round(this.quantityLossClaim  * 100 ) / 100) ) {
        displayErrorMessage(this.snackbarService, "The sum of Pedigree and Non-Pedigree Total Claim Amount should not exceed the Quantity Loss Claim.")
        return false
      }
    }
    
    return true
  }

  doSyncClaimsCodeTables(){
    this.store.dispatch(syncClaimsCodeTables());
  }

  showSubmitButton() {

    if (this.calculationDetail.isOutOfSync == null) {
        return false
    }

    if (this.calculationDetail.linkedProductId && !this.calculationDetail.linkedClaimCalculationGuid) {
      // if linked, the linked calculation should be saved first before we allow submit
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

  isLinkedCalculationSubmitted() {

    if (this.calculationDetail.linkedClaimCalculationGuid) {
      // find which one is the linked calculation - the pedigree or non-pedigree

      if (this.calculationDetailNonPedigree && 
        this.calculationDetailNonPedigree.claimCalculationGuid == this.calculationDetail.linkedClaimCalculationGuid &&
        this.calculationDetailNonPedigree.calculationStatusCode == CALCULATION_STATUS_CODE.SUBMITTED) {

          return true

      }

      if (this.calculationDetailPedigree && 
        this.calculationDetailPedigree.claimCalculationGuid == this.calculationDetail.linkedClaimCalculationGuid &&
        this.calculationDetailPedigree.calculationStatusCode == CALCULATION_STATUS_CODE.SUBMITTED) {

          return true

      }
    }

    return false // default
  }

  onSubmit() {
    let  updatedClaim = this.getUpdatedClaim(false);

    if (this.isFormValid(updatedClaim) )  {

        updatedClaim.calculationStatusCode = CALCULATION_STATUS_CODE.SUBMITTED;
        this.store.dispatch(updateCalculationDetailMetadata(updatedClaim, CALCULATION_UPDATE_TYPE.SUBMIT))

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

    if (this.calculationDetail.claimCalculationGrainQuantity && 
        ( areNotEqual (this.calculationDetail.claimCalculationGrainQuantity.reseedClaim, frmMain.controls.reseedClaim.value) ||
          areNotEqual (this.calculationDetail.claimCalculationGrainQuantity.advancedClaim, frmMain.controls.advancedClaim.value) )
      ) {

      return true
    }

    // non-pedigree fields
    if ( this.calculationDetailNonPedigree && this.calculationDetailNonPedigree.claimCalculationGrainQuantityDetail) {
      if (areNotEqual (this.calculationDetailNonPedigree.claimCalculationGrainQuantityDetail.assessedYield, frmMain.controls.assessedYieldNonPedigree.value) ||
          areNotEqual (this.calculationDetailNonPedigree.claimCalculationGrainQuantityDetail.damagedAcres, frmMain.controls.damagedAcresNonPedigree.value) ||
          areNotEqual (this.calculationDetailNonPedigree.claimCalculationGrainQuantityDetail.seededAcres, frmMain.controls.seededAcresNonPedigree.value) ||
          areNotEqual (this.calculationDetailNonPedigree.claimCalculationGrainQuantityDetail.inspEarlyEstYield, frmMain.controls.inspEarlyEstYieldNonPedigree.value) ||
          areNotEqual (this.calculationDetailNonPedigree.totalClaimAmount, frmMain.controls.totalClaimAmountNonPedigree.value)
    ) {
        return true
      }
    }

    // pedigree fields
    if ( this.calculationDetailPedigree && this.calculationDetailPedigree.claimCalculationGrainQuantityDetail) {
      if (areNotEqual (this.calculationDetailPedigree.claimCalculationGrainQuantityDetail.assessedYield, frmMain.controls.assessedYieldPedigree.value) ||
          areNotEqual (this.calculationDetailPedigree.claimCalculationGrainQuantityDetail.damagedAcres, frmMain.controls.damagedAcresPedigree.value) ||
          areNotEqual (this.calculationDetailPedigree.claimCalculationGrainQuantityDetail.seededAcres, frmMain.controls.seededAcresPedigree.value) ||
          areNotEqual (this.calculationDetailPedigree.claimCalculationGrainQuantityDetail.inspEarlyEstYield, frmMain.controls.inspEarlyEstYieldPedigree.value) ||
          areNotEqual (this.calculationDetailPedigree.totalClaimAmount, frmMain.controls.totalClaimAmountPedigree.value)
    ) {
        return true
      }
    }

    return false
  }

}
