import { Component, Input } from '@angular/core';
import { vmCalculation } from 'src/app/conversion/models';
import { CalculationPrintoutLogoComponent } from '../calculation-printout-logo/calculation-printout-logo.component';
import { NgIf, DecimalPipe, CurrencyPipe } from '@angular/common';
import { CalculationPrintoutHeaderComponent } from '../calculation-printout-header/calculation-printout-header.component';
import { CalculationPrintoutFooterComponent } from '../calculation-printout-footer/calculation-printout-footer.component';

@Component({
    selector: 'calculation-printout-grain-spot-loss',
    templateUrl: './grain-spot-loss.component.html',
    styleUrl: './grain-spot-loss.component.scss',
    imports: [CalculationPrintoutLogoComponent, NgIf, CalculationPrintoutHeaderComponent, 
      CalculationPrintoutFooterComponent, DecimalPipe, CurrencyPipe]
})
export class CalculationPrintoutGrainSpotLossComponent {
  @Input() calculationDetail: vmCalculation;
}
