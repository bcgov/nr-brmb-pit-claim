import { Component, Input, OnChanges,  SimpleChanges, AfterViewInit } from '@angular/core';
import { vmCalculation } from 'src/app/conversion/models';
import {BaseComponent} from "../../common/base/base.component";
import { makeTitleCase, removeDuplicateWords, ResourcesRoutes } from "../../../utils"
import { loadCalculationDetail } from "../../../store/calculation-detail/calculation-detail.actions";
import { ReplaceOptionsDialogComponent } from "src/app/components/dialogs/replace-options-dialog/replace-options-dialog.component";

@Component({
  selector: 'cirras-calculation-detail-header',
  templateUrl: './calculation-detail-header.component.html',
  styleUrls: ['./calculation-detail-header.component.scss']
})
export class CalculationDetailHeaderComponent extends BaseComponent implements OnChanges, AfterViewInit {

  displayLabel = "Calculation Detail";
  @Input() claimCalculationGuid?: string;
  @Input() claimNumber?: string;
  @Input() calculationDetail: vmCalculation;

  @Input() calculationComment?:string;

  title: string;
  
  ngOnInit() {
    super.ngOnInit()
  }
  
  ngOnChanges(changes: SimpleChanges): void {

    super.ngOnChanges(changes);
    if (changes.claimCalculationGuid) {
        this.claimCalculationGuid = changes.claimCalculationGuid.currentValue;
    }

    if (changes.calculationComment) {
      this.calculationComment = changes.calculationComment.currentValue;
    }

    if (changes.claimNumber) {
      this.claimNumber = changes.claimNumber.currentValue;
    }

    if (changes.calculationDetail) {
        this.calculationDetail = changes.calculationDetail.currentValue;
        
        setTimeout(() => {
            this.cdr.detectChanges();
        });
    }

  }

  titleCase(str) {
    return removeDuplicateWords(makeTitleCase(str))
  }

  onRefresh() {
    this.store.dispatch(loadCalculationDetail(this.claimCalculationGuid, this.displayLabel, this.claimNumber, "true"));
  }

  onReplace() {        
    // copy the original calculationDetail that comes from the database and update the comments only 

    let updatedCalculation = JSON.parse(JSON.stringify(this.calculationDetail)); 
    updatedCalculation.calculationComment = this.calculationComment

      const dialogRef = this.dialog.open(ReplaceOptionsDialogComponent, {
                width: '400px',
                data: updatedCalculation,
                autoFocus: false // if you remove this line of code then the first radio button would be selected
              });
  }


  goToLinkedCalulation() {
    let resourceRoute = ResourcesRoutes.CALCULATION_DETAIL

    if (this.calculationDetail.linkedClaimCalculationGuid) {
      this.router.navigate([resourceRoute, 
          this.calculationDetail.linkedClaimNumber.toString(),
          this.calculationDetail.linkedClaimCalculationGuid.toString()
        ]);
    } else {
      this.router.navigate([resourceRoute, 
          this.calculationDetail.linkedClaimNumber.toString()
        ]);
    }

  }

}
