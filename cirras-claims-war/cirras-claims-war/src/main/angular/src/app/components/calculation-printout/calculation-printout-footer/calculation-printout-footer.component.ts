import { Component, Input } from '@angular/core'; 
import { vmCalculation } from "../../../conversion/models";

@Component({
  selector: 'cirras-calculation-printout-footer',
  templateUrl: './calculation-printout-footer.component.html',
  styleUrls: ['./calculation-printout-footer.component.scss']
})
export class CalculationPrintoutFooterComponent {

  @Input() calculationDetail: vmCalculation;

}
