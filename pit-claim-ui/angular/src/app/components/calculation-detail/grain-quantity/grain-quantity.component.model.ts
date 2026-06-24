import {BaseComponentModel} from "../../common/base/base.component.model";
import {DomSanitizer} from "@angular/platform-browser";
import {UntypedFormBuilder, FormArray, Validators} from "@angular/forms";
import {vmCalculation} from "../../../conversion/models";



export class CalculationDetailGrainQuantityComponentModel extends BaseComponentModel {
    calculationDetail: vmCalculation;

    constructor(protected sanitizer: DomSanitizer,
                private fb: UntypedFormBuilder,
                private calculationData: vmCalculation) {
        super(sanitizer);
        this.calculationDetail = calculationData;
        
        this.formGroup = this.fb.group({
            primaryPerilCode: [{value: this.calculationDetail ? this.calculationDetail.primaryPerilCode : null, disabled: false}, [Validators.required]],            
            secondaryPerilCode: [{value: this.calculationDetail ? this.calculationDetail.secondaryPerilCode : null, disabled: false}],     
            assessedYieldNonPedigree: [''],
            assessedYieldPedigree: [''],
            damagedAcresNonPedigree: [''],
            damagedAcresPedigree: [''],
            seededAcresNonPedigree: [''],
            seededAcresPedigree: [''],
            inspEarlyEstYieldNonPedigree: [''],
            inspEarlyEstYieldPedigree: [''],
            reseedClaim: [''],
            advancedClaim: [''],
            totalClaimAmountNonPedigree: [''],
            totalClaimAmountPedigree: [''],
            calculationComment: [{value: this.calculationDetail ? this.calculationDetail.calculationComment : null, disabled: false}, [Validators.maxLength(1000)]], 
        });
    }

    public clone(): CalculationDetailGrainQuantityComponentModel {
        let clonedModel: CalculationDetailGrainQuantityComponentModel = new CalculationDetailGrainQuantityComponentModel(this.sanitizer, this.fb, this.calculationDetail);
        return clonedModel;
    }
}
