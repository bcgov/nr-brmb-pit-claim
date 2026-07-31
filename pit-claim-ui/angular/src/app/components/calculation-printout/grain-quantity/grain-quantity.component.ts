import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { vmCalculation } from 'src/app/conversion/models';
import { BaseComponent } from '../../common/base/base.component';

@Component({
    selector: 'calculation-printout-grain-quantity',
    templateUrl: './grain-quantity.component.html',
    styleUrl: './grain-quantity.component.scss',
    standalone: false
})

export class CalculationPrintoutGrainQuantityComponent implements OnChanges {

  @Input() calculationDetail: vmCalculation;
  @Input() calculationDetailNonPedigree: vmCalculation;
  @Input() calculationDetailPedigree: vmCalculation;

  linkedClaimNumber?: number;

  prodGuaranteeMinusAssessmentsNonPedigree: number
  prodGuaranteeMinusAssessmentsPedigree: number

  showYieldRows = false
  showNonPedigreeColumn = false  
  showPedigreeColumn = false 

  ngOnChanges(changes: SimpleChanges) {

    if (changes.calculationDetail) {

      this.calculationDetail = changes.calculationDetail.currentValue;

      if (this.calculationDetail.isPedigreeInd) {
        this.showPedigreeColumn = true
      } else {
        this.showNonPedigreeColumn = true
      }

      if (this.calculationDetail.linkedProductId) {
        this.showNonPedigreeColumn = true
        this.showPedigreeColumn = true

        this.linkedClaimNumber = this.calculationDetail.linkedClaimNumber
      }

    }

    if ( changes.calculationDetailNonPedigree && this.calculationDetailNonPedigree ) {
      this.prodGuaranteeMinusAssessmentsNonPedigree = this.calculateProductionGuaranteeWeight(this.calculationDetailNonPedigree)

      if (this.calculationDetailNonPedigree.claimCalculationGrainQuantityDetail && 
        this.calculationDetailNonPedigree.claimCalculationGrainQuantityDetail.earlyEstDeemedYieldValue &&
        this.calculationDetailNonPedigree.claimCalculationGrainQuantityDetail.earlyEstDeemedYieldValue > 0
      ) {
        this.showYieldRows = true
      }
    }

    if ( changes.calculationDetailPedigree && this.calculationDetailPedigree ) {
      
      this.prodGuaranteeMinusAssessmentsPedigree = this.calculateProductionGuaranteeWeight(this.calculationDetailPedigree)
    
      if (this.calculationDetailPedigree.claimCalculationGrainQuantityDetail && 
        this.calculationDetailPedigree.claimCalculationGrainQuantityDetail.earlyEstDeemedYieldValue &&
        this.calculationDetailPedigree.claimCalculationGrainQuantityDetail.earlyEstDeemedYieldValue > 0
      ) {
        this.showYieldRows = true
      }
    }
  }

  calculateProductionGuaranteeWeight(calcDetail: vmCalculation){
    // Line J: ( D - I ) x E
    // Production Guarantee - Assessments: Calculated as (  (Production Guarantee - Assessed Yield) * Insurable Value  ) 
    let result = 0
    
    let productionGuaranteeWeight = 
      (calcDetail && calcDetail.claimCalculationGrainQuantityDetail && calcDetail.claimCalculationGrainQuantityDetail.productionGuaranteeWeight) ? 
        calcDetail.claimCalculationGrainQuantityDetail.productionGuaranteeWeight : 0 ;

    let assessedYield = (calcDetail && calcDetail.claimCalculationGrainQuantityDetail && calcDetail.claimCalculationGrainQuantityDetail.assessedYield) ? 
        calcDetail.claimCalculationGrainQuantityDetail.assessedYield : 0 ;


    if (calcDetail && calcDetail.claimCalculationGrainQuantityDetail && calcDetail.claimCalculationGrainQuantityDetail.insurableValue) {
      result = ( productionGuaranteeWeight - assessedYield) * calcDetail.claimCalculationGrainQuantityDetail.insurableValue
    }
    
    return Math.max(0, result)
  }

  isLinked(){
    if (this.showNonPedigreeColumn && this.showPedigreeColumn) {
      return true
    } else {
      return false
    }
  }

  getYieldValueLetter() {
    if (this.isLinked() && this.showYieldRows ) {
      return "R"
    }

    if (this.isLinked() && !this.showYieldRows ) {
      return "M" 
    }
    
    if (!this.isLinked() && this.showYieldRows ) {
      return "P"
    }
          
    if (!this.isLinked() && !this.showYieldRows ) {
      return "K"
    } 

    return ""
  }

  getYieldValuePlusEarlyEstLetter() {
    if (this.isLinked() && this.showYieldRows ) {
      return "S"
    }

    if (this.isLinked() && !this.showYieldRows ) {
      return "N" 
    }
    
    if (!this.isLinked() && this.showYieldRows ) {
      return "Q"
    }
          
    if (!this.isLinked() && !this.showYieldRows ) {
      return "L"
    } 

    return ""
  }

  getYieldValuePlusEarlyEstLetterCalc() {
    if (this.isLinked() && this.showYieldRows ) {
      return "R+L"
    }

    if (this.isLinked() && !this.showYieldRows ) {
      return "M+L" 
    }
    
    if (!this.isLinked() && this.showYieldRows ) {
      return "K+J"
    }
          
    if (!this.isLinked() && !this.showYieldRows ) {
      return "K+J"
    } 

    return ""
  }


  setLetter(baseLetter) {
    if (this.isLinked() && this.showYieldRows ) {
      return baseLetter
    }

    if (this.isLinked() && !this.showYieldRows ) {
      return String.fromCharCode(baseLetter.charCodeAt(0) - 5)
    }
    
    if (!this.isLinked() && this.showYieldRows ) {
      return String.fromCharCode(baseLetter.charCodeAt(0) - 3)
    }
          
    if (!this.isLinked() && !this.showYieldRows ) {
      return String.fromCharCode(baseLetter.charCodeAt(0) - 8)
    } 

    return ""
  }

}
