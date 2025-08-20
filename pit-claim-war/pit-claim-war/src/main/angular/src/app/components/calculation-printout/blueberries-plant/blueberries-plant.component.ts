import {ChangeDetectionStrategy, Component, Input, SimpleChanges} from '@angular/core';
import { roundedDollars } from 'src/app/utils';
import {vmCalculation} from "../../../conversion/models";

@Component({
    selector: 'cirras-calculation-printout-berries-plant',
    templateUrl: './blueberries-plant.component.html',
    styleUrls: ['./blueberries-plant.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: false
})
export class CalculationPrintoutBlueberriesPlantComponent {

  @Input() calculationDetail: vmCalculation;

  makeDollarsRounded( val ) {
    return roundedDollars(val)
  }

}
