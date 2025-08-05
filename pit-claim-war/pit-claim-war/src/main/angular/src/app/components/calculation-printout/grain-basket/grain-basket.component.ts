import { Component, Input } from '@angular/core';
import { vmCalculation } from 'src/app/conversion/models';

@Component({
  selector: 'calculation-printout-grain-basket',
  templateUrl: './grain-basket.component.html',
  styleUrl: './grain-basket.component.scss'
})
export class CalculationPrintoutGrainBasketComponent {
  @Input() calculationDetail: vmCalculation;
}
