import {ChangeDetectionStrategy, Component, Input, SimpleChanges} from '@angular/core';
import { roundedDollars } from 'src/app/utils';
import {vmCalculation} from "../../../conversion/models";
import { CalculationPrintoutLogoComponent } from '../calculation-printout-logo/calculation-printout-logo.component';
import { NgIf, DecimalPipe, CurrencyPipe } from '@angular/common';
import { CalculationPrintoutHeaderComponent } from '../calculation-printout-header/calculation-printout-header.component';
import { CalculationPrintoutFooterComponent } from '../calculation-printout-footer/calculation-printout-footer.component';

@Component({
    selector: 'cirras-calculation-printout-berries-plant',
    templateUrl: './blueberries-plant.component.html',
    styleUrls: ['./blueberries-plant.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    imports: [CalculationPrintoutLogoComponent, NgIf, CalculationPrintoutHeaderComponent, 
      CalculationPrintoutFooterComponent, DecimalPipe, CurrencyPipe]
})
export class CalculationPrintoutBlueberriesPlantComponent {

  @Input() calculationDetail: vmCalculation;

  makeDollarsRounded( val ) {
    return roundedDollars(val)
  }

}
