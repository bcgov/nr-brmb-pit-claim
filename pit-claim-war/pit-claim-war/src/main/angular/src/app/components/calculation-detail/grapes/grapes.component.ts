import {ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges, AfterViewInit} from "@angular/core";
import {CalculationDetailGrapesComponentModel} from "./grapes.component.model";
import {
  loadCalculationDetail,
  updateCalculationDetailMetadata
} from "../../../store/calculation-detail/calculation-detail.actions";
import {CALCULATION_DETAIL_COMPONENT_ID} from "../../../store/calculation-detail/calculation-detail.state";
import {BaseComponent} from "../../common/base/base.component";
import {vmCalculation} from "../../../conversion/models";
import {CodeData, Option} from "../../../store/application/application.state";
import {getCodeOptions} from "../../../utils/code-table-utils";
import {UntypedFormArray, UntypedFormGroup} from "@angular/forms";
import {syncClaimsCodeTables} from "../../../store/calculation-detail/calculation-detail.actions";
import {dollars, dollarsToNumber, makeTitleCase, makeNumberOnly, CALCULATION_UPDATE_TYPE, CALCULATION_STATUS_CODE, getPrintTitle, CLAIM_STATUS_CODE, areNotEqual} from "../../../utils"
import { setFormStateUnsaved } from "src/app/store/application/application.actions";

@Component({
  selector: "cirras-claims-calculation-detail-grapes",
  templateUrl: "./grapes.component.html",
  styleUrls: ["../../common/base/base.component.scss",
      "./grapes.component.scss"],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CalculationDetailGrapesComponent extends BaseComponent implements OnChanges, AfterViewInit {
    displayLabel = "Calculation Detail";
    @Input() claimCalculationGuid?: string;
    @Input() claimNumber?: string;
    @Input() calculationDetail: vmCalculation;
    @Input() updatedCalculation: any;
    @Input() isUnsaved: boolean;

    calculationStatusOptions: (CodeData|Option)[];
    perilCodeOptions: (CodeData|Option)[];
    cropVarietyCodes: (CodeData|Option)[];

    isClaimTotalHigh: boolean = false;

    calculationComment: string = ""

    initModels() {
        this.viewModel = new CalculationDetailGrapesComponentModel(this.sanitizer, this.fb, this.calculationDetail);
    }

    loadPage() {
        this.calculationStatusOptions = getCodeOptions("CALCULATION_STATUS_CODE");
        this.perilCodeOptions = getCodeOptions("PERIL_CODE");
        this.componentId = CALCULATION_DETAIL_COMPONENT_ID;
        this.updateView();
    }

    getViewModel(): CalculationDetailGrapesComponentModel  { //
        return <CalculationDetailGrapesComponentModel>this.viewModel;
    }

    ngOnChanges(changes: SimpleChanges) {
      super.ngOnChanges(changes);
      if (changes.claimCalculationGuid) {
          this.claimCalculationGuid = changes.claimCalculationGuid.currentValue;
      }

      if (changes.claimNumber) {
        this.claimNumber = changes.claimNumber.currentValue;
      }

      if (changes.calculationDetail) {
          this.calculationDetail = changes.calculationDetail.currentValue;
          this.calculationComment = this.calculationDetail.calculationComment

          setTimeout(() => {
              this.cdr.detectChanges();
          });
      }

      this.ngOnChanges2(changes);
    }

    ngOnInit() {
      super.ngOnInit()

      this.viewModel.formGroup.controls.coverageAmountAssessed.valueChanges.subscribe( value => this.updateCalculated() )
      this.viewModel.formGroup.controls.varieties.valueChanges.subscribe( value => this.updateCalculated() )

    }

    ngOnChanges2(changes: SimpleChanges) {
        var self = this
        if ( changes.calculationDetail && this.calculationDetail ) {

            this.viewModel.formGroup.controls.primaryPerilCode.setValue( this.calculationDetail.primaryPerilCode )
            this.viewModel.formGroup.controls.secondaryPerilCode.setValue( this.calculationDetail.secondaryPerilCode )
            this.viewModel.formGroup.controls.coverageAssessedReason.setValue( this.calculationDetail.claimCalculationGrapes.coverageAssessedReason )
            this.viewModel.formGroup.controls.coverageAmount.setValue( this.calculationDetail.claimCalculationGrapes.coverageAmount )            
            this.viewModel.formGroup.controls.coverageAmountAssessed.setValue( dollars(this.calculationDetail.claimCalculationGrapes.coverageAmountAssessed) )
            this.viewModel.formGroup.controls.coverageAmountAdjusted.setValue( dollars(this.calculationDetail.claimCalculationGrapes.coverageAmountAdjusted) )
            this.viewModel.formGroup.controls.calculationComment.setValue( this.calculationDetail.calculationComment )            
            this.viewModel.formGroup.controls.totalProductionValue.setValue( dollars(this.calculationDetail.claimCalculationGrapes.totalProductionValue ))
            this.viewModel.formGroup.controls.totalClaimAmount.setValue( dollars(this.calculationDetail.totalClaimAmount ))


            let vla: UntypedFormArray = this.viewModel.formGroup.controls.varieties as UntypedFormArray
            vla.clear()
            this.calculationDetail.varieties.forEach( v => this.addVariety( v ) )

            this.enableDisableFormControls();

            this.isClaimTotalHigh = false
            if (this.calculationDetail.totalClaimAmount > this.calculationDetail.claimCalculationGrapes.coverageAmountAdjusted ) {
                this.isClaimTotalHigh = true
            }
        }
    }

    enableDisableFormControls() {
        if(this.calculationDetail){
    
          if(this.calculationDetail.calculationStatusCode == CALCULATION_STATUS_CODE.DRAFT){
            this.viewModel.formGroup.controls.primaryPerilCode.enable();
            this.viewModel.formGroup.controls.secondaryPerilCode.enable();
            this.viewModel.formGroup.controls.coverageAssessedReason.enable();
            this.viewModel.formGroup.controls.coverageAmount.enable();
            this.viewModel.formGroup.controls.coverageAmountAssessed.enable();
            this.viewModel.formGroup.controls.coverageAmountAdjusted.enable();
            this.viewModel.formGroup.controls.totalProductionValue.enable();
            this.viewModel.formGroup.controls.totalClaimAmount.enable();

            let varieties: UntypedFormArray = this.viewModel.formGroup.controls.varieties as UntypedFormArray
            varieties.controls.forEach(ctrl => ctrl.enable())

          } else {
            this.viewModel.formGroup.controls.primaryPerilCode.disable();
            this.viewModel.formGroup.controls.secondaryPerilCode.disable();
            this.viewModel.formGroup.controls.coverageAssessedReason.disable();
            this.viewModel.formGroup.controls.coverageAmount.disable();
            this.viewModel.formGroup.controls.coverageAmountAssessed.disable();
            this.viewModel.formGroup.controls.coverageAmountAdjusted.disable();
            this.viewModel.formGroup.controls.totalProductionValue.disable();
            this.viewModel.formGroup.controls.totalClaimAmount.disable();

            let varieties: UntypedFormArray = this.viewModel.formGroup.controls.varieties as UntypedFormArray

            varieties.controls.forEach(ctrl => ctrl.disable())
          }
        }
      }
  
    addVariety( variety ) {
        let vla: UntypedFormArray = this.viewModel.formGroup.controls.varieties as UntypedFormArray

        vla.push( this.fb.group( {
            cropVarietyId:        [ variety.cropVarietyId ],
            yieldActual:            [ variety.yieldActual ],
            yieldAssessed:          [ variety.yieldAssessed ],
            yieldAssessedReason:    [ variety.yieldAssessedReason ],
            averagePrice:           [ variety.averagePrice ],
            insurableValue:         [ variety.insurableValue ],
            varietyProductionValue: [ variety.varietyProductionValue ],
            yieldTotal:             [ variety.yieldTotal ],
            isOutOfSyncAvgPrice:    [ variety.isOutOfSyncAvgPrice],
            isOutOfSyncVarietyRemoved: [ variety.isOutOfSyncVarietyRemoved ],
            averagePriceOverride:   [ variety.averagePriceOverride ],
        } ) )

        const bIsInStatusDraft = this.calculationDetail && this.calculationDetail.calculationStatusCode == 'DRAFT';

        vla.controls.forEach( function ( c: UntypedFormGroup ) {
            if(bIsInStatusDraft){
                c.controls.yieldActual.enable();
                c.controls.yieldAssessed.enable();
                c.controls.yieldAssessedReason.enable();
                c.controls.averagePrice.enable();
            } else {
                c.controls.yieldActual.disable();
                c.controls.yieldAssessed.disable();
                c.controls.yieldAssessedReason.disable();
                if(c.value.isEditable) {
                    c.controls.averagePrice.disable();
                }
            }
        })
    }

    getVarietyCodes() {
        let vla: UntypedFormArray = this.viewModel.formGroup.controls.varieties as UntypedFormArray
        return vla.controls.map( ( c: UntypedFormGroup ) => c.value.cropVarietyId )
    }

    updateVarietyField( claimIndex, updateIndex, fieldName ) {
        let vla: UntypedFormArray = this.viewModel.formGroup.controls.varieties as UntypedFormArray

        if ( this.updatedCalculation.varieties[ updateIndex][ fieldName ] == vla.controls[ claimIndex ].value[ fieldName ] ) return

        console.log( 'updating variety', claimIndex, fieldName, 'to', this.updatedCalculation.varieties[ updateIndex][ fieldName ] )
        let fg = vla.controls[ claimIndex ] as UntypedFormGroup
        fg.controls[ fieldName ].setValue( this.updatedCalculation.varieties[ updateIndex][ fieldName ] )
    }

    updatingCalculated = false
    updateCalculated() {

        if ( !this.calculationDetail ) return
        if ( this.updatingCalculated ) return
        this.updatingCalculated = true

        let coverageValue = this.calculationDetail.claimCalculationGrapes.coverageAmount //A
        let assessmentValue = dollarsToNumber( this.viewModel.formGroup.controls.coverageAmountAssessed.value || 0 ) //B
        let coverageAmountAdjusted = coverageValue - assessmentValue

        this.viewModel.formGroup.controls.coverageAmountAdjusted.setValue( dollars(coverageAmountAdjusted) ) //C

        let total = 0

        var self = this

        let vla = this.viewModel.formGroup.controls.varieties as UntypedFormArray
        vla.controls.forEach( function ( c: UntypedFormGroup ) {
            c.controls.varietyProductionValue.setValue( null )

            let yieldActual = parseFloat( c.value.yieldActual || 0 ) //1
            if ( isNaN( yieldActual ) ) return

            let yieldAssessed = parseFloat( c.value.yieldAssessed || 0 ) //2
            if ( isNaN( yieldAssessed ) ) return

            let insurableValue = parseFloat( c.value.insurableValue ) //3
            if ( isNaN( insurableValue ) ) return

            let varietyProductionValue = ( yieldActual + yieldAssessed ) * insurableValue
            c.controls.varietyProductionValue.setValue( dollars( varietyProductionValue ) ) //(1+2)*3

            total += varietyProductionValue
        } )

        this.viewModel.formGroup.controls.totalProductionValue.setValue( dollars(total) )

        const tempTotalClaimAmount = Math.max( 0, coverageAmountAdjusted - total )
        this.viewModel.formGroup.controls.totalClaimAmount.setValue( dollars( tempTotalClaimAmount ) )

        this.isClaimTotalHigh = false
        if ( tempTotalClaimAmount > coverageAmountAdjusted ) {
            this.isClaimTotalHigh = true
        }

        this.updatingCalculated = false
    }

    ngAfterViewInit() {
        super.ngAfterViewInit();
    }

    getCropVarietyCode( code ) {
        let varietyName = this.calculationDetail.varieties.find( c => c.cropVarietyId == code )?.varietyName || code;
        return makeTitleCase(varietyName);
    }

    onCancel() {
        this.store.dispatch(loadCalculationDetail(this.claimCalculationGuid, this.displayLabel, this.claimNumber, this.calculationDetail.policyNumber, "false"));
    }

    onSave(saveCommentsOnly:boolean) {
        const  updatedClaim = this.getUpdatedClaim(saveCommentsOnly);
        if (this.isFormValid(updatedClaim) )  {

            this.store.dispatch(updateCalculationDetailMetadata(updatedClaim, ""));
            this.doSyncClaimsCodeTables();
        }
    }

    getUpdatedClaim(saveCommentsOnly:boolean) {

        //make a deep copy
        let updatedCalculation = JSON.parse(JSON.stringify(this.calculationDetail));

        if (saveCommentsOnly) {

            updatedCalculation.calculationComment = this.viewModel.formGroup.controls.calculationComment.value

        } else {

            updatedCalculation.primaryPerilCode = this.viewModel.formGroup.controls.primaryPerilCode.value
            updatedCalculation.secondaryPerilCode = this.viewModel.formGroup.controls.secondaryPerilCode.value

            updatedCalculation.claimCalculationGrapes.coverageAssessedReason = this.viewModel.formGroup.controls.coverageAssessedReason.value
            updatedCalculation.claimCalculationGrapes.coverageAmountAssessed = dollarsToNumber(this.viewModel.formGroup.controls.coverageAmountAssessed.value)
            updatedCalculation.claimCalculationGrapes.coverageAmountAdjusted = dollarsToNumber(updatedCalculation.claimCalculationGrapes.coverageAmountAdjusted) 
            updatedCalculation.claimCalculationGrapes.totalProductionValue = dollarsToNumber(updatedCalculation.claimCalculationGrapes.totalProductionValue)
            updatedCalculation.totalClaimAmount = dollarsToNumber(updatedCalculation.totalClaimAmount) 
            updatedCalculation.calculationComment = this.viewModel.formGroup.controls.calculationComment.value

            var self = this;        
            updatedCalculation.varieties.forEach( function ( v ) {

                let vla: UntypedFormArray = self.viewModel.formGroup.controls.varieties as UntypedFormArray

                const tempVariety =  vla.controls.find( c => c.value.cropVarietyId == v.cropVarietyId ) ?.value || null;
                if (tempVariety) {
                    v.yieldActual = tempVariety.yieldActual * 1
                    v.yieldAssessed = tempVariety.yieldAssessed * 1
                    v.yieldAssessedReason = tempVariety.yieldAssessedReason
                    if (!isNaN(tempVariety.averagePrice)) {
                        v.averagePrice = tempVariety.averagePrice * 1  
                    }  
                    if (!isNaN(tempVariety.averagePriceOverride)) {
                        v.averagePriceOverride = tempVariety.averagePriceOverride * 1  
                    }               
                    v.varietyProductionValue = dollarsToNumber(tempVariety.varietyProductionValue) 
                }

            } )
        }
        return updatedCalculation
    }

    isFormValid (claimForm: vmCalculation) {
        var self = this
        if (!claimForm.primaryPerilCode )  {
            //displayErrorMessage(this.snackbarService, "Please choose a primary peril")
            alert( "Please choose a primary peril")
            return false;
        }

        // check if all varieties have averagePrice or averagePriceOverride
        let isMissingAveragePriceOverride:boolean = false
        const frmVarieties: UntypedFormArray = this.viewModel.formGroup.controls.varieties as UntypedFormArray

        if (this.isWineGrape()) {
            for (let i=0; i < claimForm.varieties.length; i++) {
                if ( (claimForm.varieties[i].yieldActual || claimForm.varieties[i].yieldAssessed)
                 && claimForm.varieties[i].averagePrice == 0 && claimForm.varieties[i].averagePriceOverride == 0 ) {
    
                    (<any>frmVarieties.controls[i].get('averagePriceOverride')).nativeElement.setAttribute('style', 'background: orange')
                    isMissingAveragePriceOverride = true
    
                }
            }
    
            if ( isMissingAveragePriceOverride ) {
                alert("For any varieties with yield, please enter the Average Price Override if the Average Price is missing");
                return false
            }
        }
        
        return true;
    }

    showSubmitButton() {

        if (this.calculationDetail.isOutOfSync == null) {
            return false
        }

        if (this.calculationDetail.isOutOfSync || this.isClaimTotalHigh ) {
                return false
        }

        if ( this.calculationDetail.calculationStatusCode === CALCULATION_STATUS_CODE.DRAFT && 
            (
              (this.calculationDetail.claimStatusCode === CLAIM_STATUS_CODE.OPEN && this.calculationDetail.currentHasChequeReqInd == false ) 
              || 
              (this.calculationDetail.claimStatusCode === CLAIM_STATUS_CODE.IN_PROGRESS && this.calculationDetail.currentHasChequeReqInd == true ) 
            )
        ) {

              return true

        } else {

          return false
        }
    }


    doSyncClaimsCodeTables(){
      this.store.dispatch(syncClaimsCodeTables());
    }

    onPrint() {

        const originalTitle = this.titleService.getTitle()
 
        const title = getPrintTitle(this.calculationDetail.commodityName, 
                                    this.calculationDetail.coverageName, 
                                    this.calculationDetail.claimNumber,
                                    this.calculationDetail.policyNumber,
                                    this.calculationDetail.growerNumber)


        this.titleService.setTitle(title);

        window.print()

        this.titleService.setTitle(originalTitle);
    }

    onSubmit() {
        let  updatedClaim = this.getUpdatedClaim(false);

        if (this.isFormValid(updatedClaim) )  {

            updatedClaim.calculationStatusCode = CALCULATION_STATUS_CODE.SUBMITTED
            this.store.dispatch(updateCalculationDetailMetadata(updatedClaim, CALCULATION_UPDATE_TYPE.SUBMIT))

        }
    }

    onReopen() {
        const  updatedClaim = this.getUpdatedClaim(true);
        if (this.isFormValid(updatedClaim) )  {

            updatedClaim.calculationStatusCode = CALCULATION_STATUS_CODE.DRAFT;
            this.store.dispatch(updateCalculationDetailMetadata(updatedClaim, ""));
            this.doSyncClaimsCodeTables();
        }
    }

    showReopenButton() {

        //Allow to reopen a calculation if the claim has been amended and is in status In Progress
        // and the calculation is in status submitted
        if ( this.calculationDetail.calculationStatusCode === CALCULATION_STATUS_CODE.SUBMITTED && 
            this.calculationDetail.claimStatusCode === CLAIM_STATUS_CODE.IN_PROGRESS && 
            this.calculationDetail.currentHasChequeReqInd == true){
            return true
        } else {
          return false
        }
    }    

    isWineGrape() {
          if (makeTitleCase(this.calculationDetail.commodityName) === 'Wine Grape') {
              return true
          }else {
              return false
          }
      }

      setStyles(){

        let styles = {
            'grid-auto-rows': 'minmax( 1em, auto )',
            'grid-template-columns': '1fr 1fr 1fr ' + (this.isWineGrape()? '3fr 1fr' : ' 4fr')  +' 1fr 1fr ' + (this.isWineGrape()? ' 1fr' : '') ,

            'div' : {
                'justify-content': 'center'
            },

            '.ccc-expression' : {
                'padding-right': 0
            },

          }

        return styles;
    }

    numberOnly(event): boolean {
        return makeNumberOnly(event)
    }

    setComment() {
        this.calculationComment = this.viewModel.formGroup.controls.calculationComment.value
    }

    
    isMyFormDirty(){
        const hasChanged = this.isMyFormReallyDirty()

        if (hasChanged) {
            this.store.dispatch(setFormStateUnsaved(CALCULATION_DETAIL_COMPONENT_ID, true ));
        }
        }

        isMyFormReallyDirty(): boolean {

        if (!this.calculationDetail) return false

        const frmMain = this.viewModel.formGroup as UntypedFormGroup

        if ( areNotEqual (this.calculationDetail.primaryPerilCode, frmMain.controls.primaryPerilCode.value) || 
                areNotEqual (this.calculationDetail.secondaryPerilCode, frmMain.controls.secondaryPerilCode.value) || 
                areNotEqual (this.calculationDetail.calculationComment, frmMain.controls.calculationComment.value)  ) {
            
            return true
        }

        if (this.calculationDetail.claimCalculationGrapes && 
            ( areNotEqual (this.calculationDetail.claimCalculationGrapes.coverageAssessedReason, frmMain.controls.coverageAssessedReason.value) ||
                areNotEqual (this.calculationDetail.claimCalculationGrapes.coverageAmountAssessed, frmMain.controls.coverageAmountAssessed.value) || 
                areNotEqual (this.calculationDetail.claimCalculationGrapes.coverageAmountAdjusted, frmMain.controls.coverageAmountAdjusted.value) 
            )
            ) {

            return true
        }

        // check for changes in the varieties

        // start checking if the information for each field and planting was changed from the original one
        const formVarieties: UntypedFormArray = frmMain.controls.varieties as UntypedFormArray

        for (let i = 0; i < formVarieties.controls.length; i++){
            let frmVrty = formVarieties.controls[i] as UntypedFormArray

            let originalVrty = this.calculationDetail.varieties.find( v => v.cropVarietyId == frmVrty.value.cropVarietyId)

            if (originalVrty) {

                // check if the field name, legal location or number of plantings for each field have changed 
                if (frmVrty.value.yieldActual != originalVrty.yieldActual ||
                    frmVrty.value.yieldAssessed != originalVrty.yieldAssessed ||
                    frmVrty.value.yieldAssessedReason != originalVrty.yieldAssessedReason ||  
                    frmVrty.value.varietyProductionValue != originalVrty.varietyProductionValue ||
                    frmVrty.value.averagePriceOverride != originalVrty.averagePriceOverride 
                ) {

                    return true
                }
            }

        }


        return false
    }

}
