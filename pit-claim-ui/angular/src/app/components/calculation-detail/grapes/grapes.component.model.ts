import {BaseComponentModel} from "../../common/base/base.component.model";
import {DomSanitizer} from "@angular/platform-browser";
import {UntypedFormBuilder, UntypedFormArray, Validators} from "@angular/forms";
import {vmCalculation} from "../../../conversion/models";
import {requiredIfValidator} from "../../../utils";


export class CalculationDetailGrapesComponentModel extends BaseComponentModel {
    calculationDetail: vmCalculation;

    constructor(protected sanitizer: DomSanitizer,
                private fb: UntypedFormBuilder,
                private calculationData: vmCalculation) {
        super(sanitizer);
        this.calculationDetail = calculationData;
        // note that we are setting panelDisabled = false for accessibility:
        // all of the form fields should receive focus and be read out by
        // the screen reader even if they are non-editable
        // Non-editable form elements should be set to readonly in the html file
        
        this.formGroup = this.fb.group({
            primaryPerilCode: [{value: this.calculationDetail ? this.calculationDetail.primaryPerilCode : null, disabled: false}, [Validators.required]],            
            secondaryPerilCode: [{value: this.calculationDetail ? this.calculationDetail.secondaryPerilCode : null, disabled: false}], 
            coverageAssessedReason: [{value: this.calculationDetail ? this.calculationDetail.claimCalculationGrapes.coverageAssessedReason : null, disabled: false}],           
            coverageAmount: [{value: this.calculationDetail ? this.calculationDetail.claimCalculationGrapes.coverageAmount : null, disabled: false}], 
            coverageAmountAssessed: [{value: this.calculationDetail ? this.calculationDetail.claimCalculationGrapes.coverageAmountAssessed : null, disabled: false}], 
            coverageAmountAdjusted: [{value: this.calculationDetail ? this.calculationDetail.claimCalculationGrapes.coverageAmountAdjusted : null, disabled: false }],     
            calculationComment: [{value: this.calculationDetail ? this.calculationDetail.calculationComment : null, disabled: false}], 
            totalProductionValue:   [ '' ],            
            totalClaimAmount:       [ '' ],  
            varieties: new UntypedFormArray([])
        });
    }

    public clone(): CalculationDetailGrapesComponentModel {
        let clonedModel: CalculationDetailGrapesComponentModel = new CalculationDetailGrapesComponentModel(this.sanitizer, this.fb, this.calculationDetail);
        return clonedModel;
    }
}
