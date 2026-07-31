import {BaseComponentModel} from "../../common/base/base.component.model";
import {DomSanitizer} from "@angular/platform-browser";
import {UntypedFormBuilder, FormArray, Validators} from "@angular/forms";
import {vmCalculation} from "../../../conversion/models";



export class CalculationDetailBerriesComponentModel extends BaseComponentModel {
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
            secondaryPerilCode: [{value: this.calculationDetail ? this.calculationDetail.secondaryPerilCode : null, disabled: false}, [Validators.required]], 
            coverageAmountAdjusted: [{value: this.calculationDetail ? this.calculationDetail.claimCalculationBerries.coverageAmountAdjusted : null, disabled: false }, [Validators.required]],     
            
            confirmedAcres:  [{value: this.calculationDetail ? this.calculationDetail.claimCalculationBerries.confirmedAcres : null, disabled: false }],  
            harvestedYield:  [{value: this.calculationDetail ? this.calculationDetail.claimCalculationBerries.harvestedYield : null, disabled: false }], 
            appraisedYield:  [{value: this.calculationDetail ? this.calculationDetail.claimCalculationBerries.appraisedYield : null, disabled: false }], 
            abandonedYield:  [{value: this.calculationDetail ? this.calculationDetail.claimCalculationBerries.abandonedYield : null, disabled: false }], 
            totalYieldFromAdjuster:  [{value: this.calculationDetail ? this.calculationDetail.claimCalculationBerries.totalYieldFromAdjuster : null, disabled: false }], 
            yieldAssessment:  [{value: this.calculationDetail ? this.calculationDetail.claimCalculationBerries.yieldAssessment : null, disabled: false }], 

            calculationComment: [{value: this.calculationDetail ? this.calculationDetail.calculationComment : null, disabled: false}, [Validators.maxLength(1000)]], 
            totalProductionValue:   [ '' ],            
            totalClaimAmount:       [ '' ],
            //claimCalculationBerries: new FormArray([])
        });
    }

    public clone(): CalculationDetailBerriesComponentModel {
        let clonedModel: CalculationDetailBerriesComponentModel = new CalculationDetailBerriesComponentModel(this.sanitizer, this.fb, this.calculationDetail);
        return clonedModel;
    }
}
