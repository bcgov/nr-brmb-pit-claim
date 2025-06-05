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

    // this.viewModel.formGroup.controls.adjustedAcres.valueChanges.subscribe(value => this.updateCalculated() )
    // this.viewModel.formGroup.controls.percentYieldReduction.valueChanges.subscribe(value => this.updateCalculated() )
    
  }

  ngOnChanges2(changes: SimpleChanges) {

    if ( changes.calculationDetail && this.calculationDetail ) {

      this.viewModel.formGroup.controls.primaryPerilCode.setValue( this.calculationDetail.primaryPerilCode )
      this.viewModel.formGroup.controls.secondaryPerilCode.setValue( this.calculationDetail.secondaryPerilCode )  
      
      // this.viewModel.formGroup.controls.adjustedAcres.setValue( this.calculationDetail.claimCalculationGrainSpotLoss.adjustedAcres ) 
      // this.viewModel.formGroup.controls.percentYieldReduction.setValue( this.calculationDetail.claimCalculationGrainSpotLoss.percentYieldReduction ) 
      // this.viewModel.formGroup.controls.calculationComment.setValue( this.calculationDetail.calculationComment )  
  
      // if (!this.calculationDetail.claimCalculationGrainSpotLoss.claimCalculationGrainSpotLossGuid) {

      //   // calculate values for new calculations only
      //   this.calculateValues()

      // } else {
        
      //   this.eligibleYieldReduction = this.calculationDetail.claimCalculationGrainSpotLoss.eligibleYieldReduction
      //   this.spotLossReductionValue = this.calculationDetail.claimCalculationGrainSpotLoss.spotLossReductionValue
      //   this.totalClaimAmount = this.calculationDetail.totalClaimAmount

      // }

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

      self.totalCoverageValue = self.calculationDetailNonPedigree.claimCalculationGrainQuantityDetail.coverageValue + self.calculationDetailPedigree.claimCalculationGrainQuantityDetail.coverageValue

      setTimeout(() => {
            this.cdr.detectChanges();
        });
    })

  }

  //  validateRenameLegalLand(searchLegal) {

  //   // /uwcontracts/{policyId}/validateRenameLegal
  //   let url = this.appConfig.getConfig().rest["cirras_underwriting"]
  //   url = url +"/uwcontracts/" + this.dataReceived.policyId + "/validateRenameLegal" 
  //   url = url + "?policyId=" + this.dataReceived.policyId
  //   url = url + "&annualFieldDetailId=" + this.dataReceived.annualFieldDetailId
  //   url = url + "&newLegalLocation=" + encodeURI(searchLegal)  

  //   const httpOptions = setHttpHeaders(this.tokenService.getOauthToken())

  //   var self = this
  //   return this.http.get(url,httpOptions).toPromise().then((data: RenameLegalValidationRsrc) => {
  //     self.renameLegalLandList = data
  //    })
  // }




  onCancel() {
      this.store.dispatch(loadCalculationDetail(this.claimCalculationGuid, this.displayLabel, this.claimNumber, "false"));
  }


  numberOnly(event): boolean {
    return makeNumberOnly(event)
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
            this.viewModel.formGroup.controls.totalYieldToCountNonPedigree.disable();
            this.viewModel.formGroup.controls.damagedAcresNonPedigree.disable();
            this.viewModel.formGroup.controls.seededAcresNonPedigree.disable();
            this.viewModel.formGroup.controls.inspEarlyEstYieldNonPedigree.disable();            
            this.viewModel.formGroup.controls.totalClaimAmountNonPedigree.disable();
          }

          if(!this.calculationDetail.isPedigreeInd) {
            // disable non pedigree fields
            this.viewModel.formGroup.controls.totalYieldToCountPedigree.disable();
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
}
