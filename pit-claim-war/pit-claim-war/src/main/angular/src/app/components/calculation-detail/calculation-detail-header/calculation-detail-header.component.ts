import { Component, Input, OnChanges,  SimpleChanges, AfterViewInit } from '@angular/core';
import { vmCalculation } from 'src/app/conversion/models';
import {BaseComponent} from "../../common/base/base.component";
import { makeTitleCase, removeDuplicateWords, ResourcesRoutes } from "../../../utils"
import { loadCalculationDetail } from "../../../store/calculation-detail/calculation-detail.actions";
import { ReplaceOptionsDialogComponent } from "src/app/components/dialogs/replace-options-dialog/replace-options-dialog.component";
import { setFormStateUnsaved } from 'src/app/store/application/application.actions';
import { CALCULATION_DETAIL_COMPONENT_ID } from 'src/app/store/calculation-detail/calculation-detail.state';

@Component({
  selector: 'cirras-calculation-detail-header',
  templateUrl: './calculation-detail-header.component.html',
  styleUrls: ['./calculation-detail-header.component.scss']
})
export class CalculationDetailHeaderComponent extends BaseComponent implements OnChanges, AfterViewInit {

  displayLabel = "Calculation Detail";
  @Input() calculationDetail: vmCalculation;
  @Input() calculationComment?:string;
  @Input() linkedCalculationDetail? : vmCalculation;

  title: string;
  
  displayGBErrorMessage: string 

  ngOnInit() {
    super.ngOnInit()
  }
  
  ngOnChanges(changes: SimpleChanges): void {

    super.ngOnChanges(changes);

    if (changes.calculationComment) {
      this.calculationComment = changes.calculationComment.currentValue;
    }

    if (changes.calculationDetail) {
        this.calculationDetail = changes.calculationDetail.currentValue;
        
        setTimeout(() => {
            this.cdr.detectChanges();
        });
    }

    if (changes.linkedCalculationDetail) {
        this.linkedCalculationDetail = changes.linkedCalculationDetail.currentValue;

        setTimeout(() => {
            this.cdr.detectChanges();
        });

    }
  }

  titleCase(str) {
    return removeDuplicateWords(makeTitleCase(str))
  }

  onRefresh() {
    // refresh the calculation
    this.store.dispatch(loadCalculationDetail(this.calculationDetail.claimCalculationGuid, this.displayLabel, "", this.calculationDetail.policyNumber, "true"));
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

    let claimCalculationGuid = "";

    if (this.calculationDetail.linkedClaimCalculationGuid) {
      claimCalculationGuid = this.calculationDetail.linkedClaimCalculationGuid.toString()
    } else if(this.calculationDetail.latestLinkedClaimCalculationGuid){
      claimCalculationGuid = this.calculationDetail.latestLinkedClaimCalculationGuid.toString()
    }

    this.router.navigate([resourceRoute,
      this.calculationDetail.policyNumber.toString(), 
      this.calculationDetail.linkedClaimNumber.toString(),
      claimCalculationGuid
    ]);

  }

  isRefreshAllowedForLinkedCalculations() {

    if (this.linkedCalculationDetail && 
      this.linkedCalculationDetail.calculationStatusCode !== 'DRAFT' ) {

      return false
    }

    return true
  }

  calculationAllowsReplace(){
      //State of calculation and claim allows replace
      return this.securityUtilService.doesUserHaveScope(this.SCOPES_UI.REPLACE_CALCULATION) && 
                            this.calculationDetail.calculationStatusCode === 'APPROVED' && 
                            (
                              ( this.calculationDetail.currentClaimStatusCode === 'OPEN' && this.calculationDetail.currentHasChequeReqInd == false)
                              ||
                              ( this.calculationDetail.currentClaimStatusCode === 'IN PROGRESS' && this.calculationDetail.currentHasChequeReqInd == true)
                            );

  }

  linkedCalculationAllowsReplace(){
      //State of linked calculation and claim allows replace
      //Additional rules if there is a linked calculation: Only show button if the other calculation is in status approved or archived
      if(this.linkedCalculationDetail){
        return (this.linkedCalculationDetail.calculationStatusCode === 'APPROVED' || this.linkedCalculationDetail.calculationStatusCode === 'ARCHIVED');
      }
    return true;
  }

  hasUnapprovedQuantityCalculation() {
    let result = false 

    this.displayGBErrorMessage = ""

    if (this.calculationDetail && this.calculationDetail.claimCalculationGrainBasketProducts) {

      for (let i = 0; i < this.calculationDetail.claimCalculationGrainBasketProducts.length; i++) {
      
        // if this policy has quantity products
        if (this.calculationDetail.claimCalculationGrainBasketProducts[i].quantityCommodityCoverageCode == "CQG") {

          // if the policy has qty product but no qty claim -> no message

          // if this policy has qty claim but no calculation -> display message
          if (this.calculationDetail.claimCalculationGrainBasketProducts[i].quantityClaimNumber && !this.calculationDetail.claimCalculationGrainBasketProducts[i].quantityLatestClaimCalculationGuid) {
            
            this.displayGBErrorMessage = this.displayGBErrorMessage + 
              "<p> Quantity claim # " + this.calculationDetail.claimCalculationGrainBasketProducts[i].quantityClaimNumber + " has no calculation. Please create a calculation for that claim and approve it. </p>"
            result = true
          }

          // if this policy has qty calculation but it's not approved -> display message
          if (this.calculationDetail.claimCalculationGrainBasketProducts[i].quantityLatestClaimCalculationGuid && 
             this.calculationDetail.claimCalculationGrainBasketProducts[i].quantityLatestCalculationStatusCode !== "APPROVED") {
            
            this.displayGBErrorMessage = this.displayGBErrorMessage + 
              "<p> The calculation for the quantity claim # " + this.calculationDetail.claimCalculationGrainBasketProducts[i].quantityClaimNumber + " is not approved. Please approve the claim in CIRRAS. </p>"
            result = true
          }
        }
      }
    }

    return result
  }

  
}
