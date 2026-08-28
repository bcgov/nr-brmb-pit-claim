import {ChangeDetectionStrategy, Component, Input} from '@angular/core';
import {vmCalculation} from "../../../conversion/models";
import { makeTitleCase } from 'src/app/utils';
import { CalculationPrintoutLogoComponent } from '../calculation-printout-logo/calculation-printout-logo.component';
import { NgIf, NgFor, DecimalPipe, CurrencyPipe } from '@angular/common';
import { CalculationPrintoutHeaderComponent } from '../calculation-printout-header/calculation-printout-header.component';
import { CalculationPrintoutFooterComponent } from '../calculation-printout-footer/calculation-printout-footer.component';

@Component({
    selector: 'cirras-calculation-printout',
    templateUrl: './grapes.component.html',
    styleUrls: ['./grapes.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    imports: [CalculationPrintoutLogoComponent, NgIf, CalculationPrintoutHeaderComponent, NgFor, 
      CalculationPrintoutFooterComponent, DecimalPipe, CurrencyPipe]
})
export class CalculationPrintoutComponent {
  @Input() calculationDetail: vmCalculation;

  getCropVarietyName( code ) {
    let varietyName = this.calculationDetail.varieties.find( c => c.cropVarietyId == code )?.varietyName || code;
    return makeTitleCase(varietyName);
  }
}
