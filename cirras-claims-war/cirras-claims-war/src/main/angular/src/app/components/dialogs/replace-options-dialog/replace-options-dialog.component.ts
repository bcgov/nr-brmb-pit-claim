import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import { Store } from "@ngrx/store";
import { RootState } from 'src/app/store';
import { updateCalculationDetailMetadata } from "../../../store/calculation-detail/calculation-detail.actions";
import { vmCalculation } from '../../../conversion/models';


@Component({
  selector: 'cirras-replace-options-dialog',
  templateUrl: './replace-options-dialog.component.html',
  styleUrls: ["../../common/base/base.component.scss", './replace-options-dialog.component.scss']
})

export class ReplaceOptionsDialogComponent implements OnInit {

  replaceOption: string;
  calculationDetail: vmCalculation;


  constructor(
    private store: Store<RootState>,
    public dialogRef: MatDialogRef<ReplaceOptionsDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: vmCalculation ) { 

      if (data) {
        this.calculationDetail = data;
      }

    }

  ngOnInit() {
  }

  cancelDialog(): void {
    this.dialogRef.close();
  }

  onReplace() {
    console.log(" replaceOption : " + this.replaceOption)

  if (this.replaceOption) {
    console.log(" Calling the API ");

      // call the API
      let updatedClaim = Object.assign( {}, this.calculationDetail)
      updatedClaim.calculationStatusCode = 'ARCHIVED';
      this.store.dispatch(updateCalculationDetailMetadata(updatedClaim, this.replaceOption));
      
      this.dialogRef.close();
  }    

}

}
