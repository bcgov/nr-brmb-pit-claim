import {ChangeDetectionStrategy, Component, Input} from '@angular/core';
import {vmCalculation} from "../../../conversion/models";
import { CalculationPrintoutLogoComponent } from '../calculation-printout-logo/calculation-printout-logo.component';
import { DecimalPipe, CurrencyPipe } from '@angular/common';
import { CalculationPrintoutHeaderComponent } from '../calculation-printout-header/calculation-printout-header.component';
import { CalculationPrintoutFooterComponent } from '../calculation-printout-footer/calculation-printout-footer.component';

@Component({
    selector: 'cirras-calculation-printout-strawberries-plant',
    templateUrl: './strawberries-plant.component.html',
    styleUrls: ['./strawberries-plant.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    imports: [CalculationPrintoutLogoComponent, CalculationPrintoutHeaderComponent, CalculationPrintoutFooterComponent, DecimalPipe, CurrencyPipe]
})
export class CalculationPrintoutStrawberriesPlantComponent {

  @Input() calculationDetail: vmCalculation;

}
