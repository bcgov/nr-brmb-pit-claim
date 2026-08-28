import {ChangeDetectionStrategy, Component, Input, OnInit, SimpleChanges} from '@angular/core';
import { roundedDollars } from 'src/app/utils';
import {vmCalculation} from "../../../conversion/models";
import { CalculationPrintoutLogoComponent } from '../calculation-printout-logo/calculation-printout-logo.component';
import { NgIf, DecimalPipe, CurrencyPipe } from '@angular/common';
import { CalculationPrintoutHeaderComponent } from '../calculation-printout-header/calculation-printout-header.component';
import { CalculationPrintoutFooterComponent } from '../calculation-printout-footer/calculation-printout-footer.component';

@Component({
    selector: 'cirras-calculation-printout-berries',
    templateUrl: './berries.component.html',
    styleUrls: ['./berries.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    imports: [CalculationPrintoutLogoComponent, NgIf, CalculationPrintoutHeaderComponent, 
      CalculationPrintoutFooterComponent, DecimalPipe, CurrencyPipe]
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
