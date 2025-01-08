import {BaseComponentModel} from "../../common/base/base.component.model";
import {DomSanitizer} from "@angular/platform-browser";
import {UntypedFormBuilder, FormArray, Validators} from "@angular/forms";
import {vmCalculation} from "../../../conversion/models";



export class CalculationDetailBlueberriesPlantComponentModel extends BaseComponentModel {
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
            lessAdjustmentReason: [{value: this.calculationDetail && this.calculationDetail.claimCalculationPlantUnits ? this.calculationDetail.claimCalculationPlantUnits.lessAdjustmentReason : null, disabled: false }],     
            lessAdjustmentUnits:  [{value: this.calculationDetail && this.calculationDetail.claimCalculationPlantUnits ? this.calculationDetail.claimCalculationPlantUnits.lessAdjustmentUnits : null, disabled: false }],  
            damagedUnits:  [{value: this.calculationDetail  && this.calculationDetail.claimCalculationPlantUnits ? this.calculationDetail.claimCalculationPlantUnits.damagedUnits : null, disabled: false }], 
            lessAssessmentReason:  [{value: this.calculationDetail && this.calculationDetail.claimCalculationPlantUnits ? this.calculationDetail.claimCalculationPlantUnits.lessAssessmentReason : null, disabled: false }], 
            lessAssessmentUnits:  [{value: this.calculationDetail && this.calculationDetail.claimCalculationPlantUnits ? this.calculationDetail.claimCalculationPlantUnits.lessAssessmentUnits : null, disabled: false }], 
            calculationComment: [{value: this.calculationDetail ? this.calculationDetail.calculationComment : null, disabled: false}, [Validators.maxLength(1000)]], 
           
            totalClaimAmount:       [ '' ],
        });
    }

    public clone(): CalculationDetailBlueberriesPlantComponentModel {
        let clonedModel: CalculationDetailBlueberriesPlantComponentModel = new CalculationDetailBlueberriesPlantComponentModel(this.sanitizer, this.fb, this.calculationDetail);
        return clonedModel;
    }
}
