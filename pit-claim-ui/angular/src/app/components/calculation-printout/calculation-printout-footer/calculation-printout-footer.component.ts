import { Component, Input } from '@angular/core'; 
import { vmCalculation } from "../../../conversion/models";
import { NgIf, DatePipe } from '@angular/common';

@Component({
    selector: 'cirras-calculation-printout-footer',
    templateUrl: './calculation-printout-footer.component.html',
    styleUrls: ['./calculation-printout-footer.component.scss'],
    imports: [NgIf, DatePipe]
})
export class CalculationPrintoutFooterComponent {

  @Input() calculationDetail: vmCalculation;

}
