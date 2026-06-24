import {BaseComponentModel} from "../../common/base/base.component.model";
import {DomSanitizer} from "@angular/platform-browser";
import {UntypedFormBuilder, FormArray, Validators} from "@angular/forms";
import {vmCalculation} from "../../../conversion/models";



export class CalculationDetailGrainSpotLossComponentModel extends BaseComponentModel {
    calculationDetail: vmCalculation;

    constructor(protected sanitizer: DomSanitizer,
                private fb: UntypedFormBuilder,
                private calculationData: vmCalculation) {
        super(sanitizer);
        this.calculationDetail = calculationData;
        
        this.formGroup = this.fb.group({
            primaryPerilCode: [{value: this.calculationDetail ? this.calculationDetail.primaryPerilCode : null, disabled: false}, [Validators.required]],            
            secondaryPerilCode: [{value: this.calculationDetail ? this.calculationDetail.secondaryPerilCode : null, disabled: false}], 
            adjustedAcres: [''],     
            percentYieldReduction: [''],
            calculationComment: [{value: this.calculationDetail ? this.calculationDetail.calculationComment : null, disabled: false}, [Validators.maxLength(1000)]], 
        });
    }

    public clone(): CalculationDetailGrainSpotLossComponentModel {
        let clonedModel: CalculationDetailGrainSpotLossComponentModel = new CalculationDetailGrainSpotLossComponentModel(this.sanitizer, this.fb, this.calculationDetail);
        return clonedModel;
    }
}
