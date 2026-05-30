import {BaseComponentModel} from "../common/base/base.component.model";
import {DomSanitizer} from "@angular/platform-browser";

export class ClaimsComponentModel extends BaseComponentModel {
    constructor(protected sanitizer: DomSanitizer) {
        super(sanitizer);
    }

    public clone(): ClaimsComponentModel {
        let clonedModel: ClaimsComponentModel = new ClaimsComponentModel(this.sanitizer);
        return clonedModel;
    }
}
