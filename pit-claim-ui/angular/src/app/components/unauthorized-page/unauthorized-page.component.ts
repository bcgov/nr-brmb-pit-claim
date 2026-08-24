import {Component} from "@angular/core";
import {BaseComponent} from "../common/base/base.component";
import { BaseWrapperComponent } from "../common/base-wrapper/base-wrapper.component";

@Component({
    selector: "cirras-claims-unauthorized-page",
    templateUrl: "./unauthorized-page.component.html",
    styleUrls: ["./unauthorized-page.component.scss"],
    imports: [BaseWrapperComponent]
}) 
export class UnauthorizedPageComponent extends BaseComponent {
 
}
