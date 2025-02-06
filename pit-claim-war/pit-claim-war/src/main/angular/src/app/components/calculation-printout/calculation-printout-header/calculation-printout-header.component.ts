import { Component, Input } from '@angular/core';
import { CodeData, Option } from 'src/app/store/application/application.state';
import { vmCalculation } from "../../../conversion/models";
import { getCodeOptions } from "../../../utils/code-table-utils";
import { removeDuplicateWords } from "../../../utils"

@Component({
  selector: 'cirras-calculation-printout-header',
  templateUrl: './calculation-printout-header.component.html',
  styleUrls: ['./calculation-printout-header.component.scss']
})
export class CalculationPrintoutHeaderComponent {

  @Input() calculationDetail: vmCalculation;
  @Input() currentDate = new Date();

  perilCodeOptions: (CodeData|Option)[];

  ngOnInit() {
    //super.ngOnInit()
    this.perilCodeOptions = getCodeOptions("PERIL_CODE");
  }

  getPerilCode( code ) {    
    return this.perilCodeOptions.find( c => c.code == code )?.description || code;
  }

  deleteDuplicateWords (str) {
    return removeDuplicateWords(str)
  }
}
