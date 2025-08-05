import { Component, Input } from '@angular/core';
import { vmCalculation } from 'src/app/conversion/models';
import { makeTitleCase } from 'src/app/utils';

@Component({
  selector: 'calculation-printout-grain-basket',
  templateUrl: './grain-basket.component.html',
  styleUrl: './grain-basket.component.scss'
})
export class CalculationPrintoutGrainBasketComponent {
  @Input() calculationDetail: vmCalculation;


  getCmdtyName(str) {

    if (str.indexOf(" - PEDIGREED") > 1 ) {
      str = str.substring(0, str.indexOf(" - PEDIGREED")) 
    }

    return makeTitleCase(str)
  }

  //TODO add a check if there is a pedigreed commodity. If no, then hid the Seeds column

}
