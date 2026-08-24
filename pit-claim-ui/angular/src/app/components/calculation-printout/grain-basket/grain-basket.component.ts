import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { vmCalculation } from 'src/app/conversion/models';
import { makeTitleCase } from 'src/app/utils';
import { CalculationPrintoutLogoComponent } from '../calculation-printout-logo/calculation-printout-logo.component';
import { NgIf, NgFor, DecimalPipe, CurrencyPipe } from '@angular/common';
import { CalculationPrintoutHeaderComponent } from '../calculation-printout-header/calculation-printout-header.component';
import { CalculationPrintoutFooterComponent } from '../calculation-printout-footer/calculation-printout-footer.component';

@Component({
    selector: 'calculation-printout-grain-basket',
    templateUrl: './grain-basket.component.html',
    styleUrl: './grain-basket.component.scss',
    imports: [CalculationPrintoutLogoComponent, NgIf, CalculationPrintoutHeaderComponent, NgFor, 
      CalculationPrintoutFooterComponent, DecimalPipe, CurrencyPipe]
})
export class CalculationPrintoutGrainBasketComponent  implements OnChanges {
  @Input() calculationDetail: vmCalculation;

  hasPdgCmdty: boolean = false;

  ngOnChanges(changes: SimpleChanges) {
    
      if (changes.calculationDetail) {
          this.calculationDetail = changes.calculationDetail.currentValue;
           
        if (this.calculationDetail){
           let el = this.calculationDetail.claimCalculationGrainBasketProducts.find (x => x.isPedigreeInd == true )

            if (el) {
              this.hasPdgCmdty = true
            }
        }

      }
  
      
    }

  getCmdtyName(str) {

    if (str.indexOf(" - PEDIGREED") > 1 ) {
      str = str.substring(0, str.indexOf(" - PEDIGREED")) 
    }

    return makeTitleCase(str)
  }


}
