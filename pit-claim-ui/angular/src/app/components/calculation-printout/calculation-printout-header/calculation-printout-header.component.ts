import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { CodeData, Option } from 'src/app/store/application/application.state';
import { vmCalculation } from "../../../conversion/models";
import { getCodeOptions } from "../../../utils/code-table-utils";
import { removeDuplicateWords } from "../../../utils"
import { NgIf, UpperCasePipe } from '@angular/common';

@Component({
    selector: 'cirras-calculation-printout-header',
    templateUrl: './calculation-printout-header.component.html',
    styleUrls: ['./calculation-printout-header.component.scss'],
    imports: [NgIf, UpperCasePipe]
})
export class CalculationPrintoutHeaderComponent implements OnInit, OnChanges {

  @Input() calculationDetail: vmCalculation;
  @Input() linkedClaimNumber? : number;
  @Input() currentDate = new Date();

  perilCodeOptions: (CodeData|Option)[];
  claimNumber: string;

  ngOnInit() {
    this.perilCodeOptions = getCodeOptions("PERIL_CODE");
  }

  ngOnChanges(changes: SimpleChanges) {
    if ( (changes.calculationDetail && this.calculationDetail ) || changes.linkedClaimNumber ) {

      if(this.linkedClaimNumber){
        this.claimNumber = "Claims: " + this.calculationDetail.claimNumber + ", " + this.linkedClaimNumber;
      } else {
        this.claimNumber = "Claim: " + this.calculationDetail.claimNumber;
      }

    }
  }

  getPerilCode( code ) {    
    return this.perilCodeOptions.find( c => c.code == code )?.description || code;
  }

  deleteDuplicateWords (str) {
    return removeDuplicateWords(str)
  }
}
