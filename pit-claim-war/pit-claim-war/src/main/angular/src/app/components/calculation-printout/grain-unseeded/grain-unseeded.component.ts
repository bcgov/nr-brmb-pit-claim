import { Component, Input } from '@angular/core';
import {vmCalculation} from "../../../conversion/models";

@Component({
  selector: 'calculation-printout-grain-unseeded',
  templateUrl: './grain-unseeded.component.html',
  styleUrl: './grain-unseeded.component.scss'
})
export class CalculationPrintoutGrainUnseededComponent {

  @Input() calculationDetail: vmCalculation;

}
