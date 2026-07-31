import { Component, Input } from '@angular/core';
import { vmCalculation } from 'src/app/conversion/models';

@Component({
    selector: 'calculation-printout-grain-spot-loss',
    templateUrl: './grain-spot-loss.component.html',
    styleUrl: './grain-spot-loss.component.scss',
    standalone: false
})
export class CalculationPrintoutGrainSpotLossComponent {
  @Input() calculationDetail: vmCalculation;
}
