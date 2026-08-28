import { Component, Input } from '@angular/core';
import {vmCalculation} from "../../../conversion/models";
import { CalculationPrintoutLogoComponent } from '../calculation-printout-logo/calculation-printout-logo.component';
import { NgIf, DecimalPipe, CurrencyPipe } from '@angular/common';
import { CalculationPrintoutHeaderComponent } from '../calculation-printout-header/calculation-printout-header.component';
import { CalculationPrintoutFooterComponent } from '../calculation-printout-footer/calculation-printout-footer.component';

@Component({
    selector: 'calculation-printout-grain-unseeded',
    templateUrl: './grain-unseeded.component.html',
    styleUrl: './grain-unseeded.component.scss',
    imports: [CalculationPrintoutLogoComponent, NgIf, CalculationPrintoutHeaderComponent, 
      CalculationPrintoutFooterComponent, DecimalPipe, CurrencyPipe]
})
export class CalculationPrintoutGrainUnseededComponent {

  @Input() calculationDetail: vmCalculation;

}
