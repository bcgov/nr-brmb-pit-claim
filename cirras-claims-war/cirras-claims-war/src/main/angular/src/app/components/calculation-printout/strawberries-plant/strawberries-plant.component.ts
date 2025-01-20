import {ChangeDetectionStrategy, Component, Input} from '@angular/core';
import {vmCalculation} from "../../../conversion/models";

@Component({
  selector: 'cirras-calculation-printout-strawberries-plant',
  templateUrl: './strawberries-plant.component.html',
  styleUrls: ['./strawberries-plant.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CalculationPrintoutStrawberriesPlantComponent {

  @Input() calculationDetail: vmCalculation;

}
