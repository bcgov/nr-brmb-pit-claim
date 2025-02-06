import {BaseComponentModel} from "../common/base/base.component.model";
import {DomSanitizer} from "@angular/platform-browser";

export class CalculationsComponentModel extends BaseComponentModel {
    constructor(protected sanitizer: DomSanitizer) {
        super(sanitizer);
    }

    public clone(): CalculationsComponentModel {
        let clonedModel: CalculationsComponentModel = new CalculationsComponentModel(this.sanitizer);
        return clonedModel;
    }
}
