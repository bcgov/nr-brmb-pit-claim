import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { vmCalculation } from 'src/app/conversion/models';
import { BaseComponent } from '../../common/base/base.component';

@Component({
  selector: 'calculation-printout-grain-quantity',
  templateUrl: './grain-quantity.component.html',
  styleUrl: './grain-quantity.component.scss'
})
export class CalculationPrintoutGrainQuantityComponent{
  @Input() calculationDetail: vmCalculation;

}
