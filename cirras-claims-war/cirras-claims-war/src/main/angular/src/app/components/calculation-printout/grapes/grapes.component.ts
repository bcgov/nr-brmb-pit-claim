import {ChangeDetectionStrategy, Component, Input} from '@angular/core';
import {vmCalculation} from "../../../conversion/models";
import { makeTitleCase } from 'src/app/utils';

@Component({
  selector: 'cirras-calculation-printout',
  templateUrl: './grapes.component.html',
  styleUrls: ['./grapes.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CalculationPrintoutComponent {
  @Input() calculationDetail: vmCalculation;

  getCropVarietyName( code ) {
    let varietyName = this.calculationDetail.varieties.find( c => c.cropVarietyId == code )?.varietyName || code;
    return makeTitleCase(varietyName);
  }
}
