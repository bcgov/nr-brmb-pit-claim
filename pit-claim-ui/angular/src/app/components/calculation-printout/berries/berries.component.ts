import {ChangeDetectionStrategy, Component, Input, OnInit, SimpleChanges} from '@angular/core';
import { roundedDollars } from 'src/app/utils';
import {vmCalculation} from "../../../conversion/models";

@Component({
    selector: 'cirras-calculation-printout-berries',
    templateUrl: './berries.component.html',
    styleUrls: ['./berries.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})

export class CalculationPrintoutBerriesComponent {
  @Input() calculationDetail: vmCalculation;

  getLineJText () {

    if (this.calculationDetail && this.calculationDetail.claimCalculationBerries && 
      this.calculationDetail.claimCalculationBerries.totalYieldFromAdjuster && 
      this.calculationDetail.claimCalculationBerries.totalYieldFromAdjuster  > 0 ) {
        
      return ( "Yield from Adjuster Estimate (lbs)" )

    } else { 

      return ("Yield from Declaration of Production" )

    }
  }

  getLineJYield () {

    if (this.calculationDetail && this.calculationDetail.claimCalculationBerries && 
      this.calculationDetail.claimCalculationBerries.totalYieldFromAdjuster && 
      this.calculationDetail.claimCalculationBerries.totalYieldFromAdjuster  > 0 ) {
        
      return (  this.calculationDetail.claimCalculationBerries.totalYieldFromAdjuster )

    } else { 

      return ( this.calculationDetail.claimCalculationBerries.totalYieldFromDop )
      
    }
  }

  makeDollarsRounded( val ) {
    return roundedDollars(val)
  }

}
