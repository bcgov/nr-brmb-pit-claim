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
  @Input() linkedClaimNumber? : number;
  @Input() currentDate = new Date();

  perilCodeOptions: (CodeData|Option)[];
  claimNumber: string;

  ngOnInit() {
    //super.ngOnInit()
    this.perilCodeOptions = getCodeOptions("PERIL_CODE");

    if(this.linkedClaimNumber){
      this.claimNumber = "Claims: " + this.calculationDetail.claimNumber + ", " + this.linkedClaimNumber;
    } else {
      this.claimNumber = "Claim: " + this.calculationDetail.claimNumber;
    }
  }

  getPerilCode( code ) {    
    return this.perilCodeOptions.find( c => c.code == code )?.description || code;
  }

  deleteDuplicateWords (str) {
    return removeDuplicateWords(str)
  }
}
