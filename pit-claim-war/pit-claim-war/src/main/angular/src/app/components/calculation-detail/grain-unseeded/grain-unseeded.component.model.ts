import {BaseComponentModel} from "../../common/base/base.component.model";
import {DomSanitizer} from "@angular/platform-browser";
import {UntypedFormBuilder, FormArray, Validators} from "@angular/forms";
import {vmCalculation} from "../../../conversion/models";



export class GrainUnseededComponentModel extends BaseComponentModel {
    calculationDetail: vmCalculation;

    constructor(protected sanitizer: DomSanitizer,
                private fb: UntypedFormBuilder,
                private calculationData: vmCalculation) {
        super(sanitizer);
        this.calculationDetail = calculationData;

        this.formGroup = this.fb.group({
            primaryPerilCode: [{value: this.calculationDetail ? this.calculationDetail.primaryPerilCode : null, disabled: false}, [Validators.required]],            
            lessAdjustmentAcres: [{value: this.calculationDetail && this.calculationDetail.claimCalculationGrainUnseeded ? this.calculationDetail.claimCalculationGrainUnseeded.lessAdjustmentAcres : null, disabled: false }],     
            unseededAcres:  [{value: this.calculationDetail  && this.calculationDetail.claimCalculationGrainUnseeded ? this.calculationDetail.claimCalculationGrainUnseeded.unseededAcres : null, disabled: false }], 
            lessAssessmentAcres:  [{value: this.calculationDetail && this.calculationDetail.claimCalculationGrainUnseeded ? this.calculationDetail.claimCalculationGrainUnseeded.lessAssessmentAcres : null, disabled: false }], 
            calculationComment: [{value: this.calculationDetail ? this.calculationDetail.calculationComment : null, disabled: false}, [Validators.maxLength(1000)]], 
            totalClaimAmount:       [ '' ],
        });
    }

    public clone(): GrainUnseededComponentModel {
        let clonedModel: GrainUnseededComponentModel = new GrainUnseededComponentModel(this.sanitizer, this.fb, this.calculationDetail);
        return clonedModel;
    }
}
