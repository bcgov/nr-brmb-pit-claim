import {Component, OnDestroy, OnInit} from "@angular/core";
import {BaseComponent} from "../common/base/base.component";

@Component({
    selector: "cirras-claims-sign-out-page",
    templateUrl: "./sign-out-page.component.html",
    styleUrls: ["./sign-out-page.component.scss"],
    standalone: false
})
export class SignOutPageComponent extends BaseComponent implements OnInit, OnDestroy {
    ngOnInit() {
        const appConfig = (<any>this.appConfigService.getConfig()).application;
        const keycloakConfig = (<any>this.appConfigService.getConfig()).keycloak;
        localStorage.clear();
        const url = `${keycloakConfig.logoutUrl}?client_id=${encodeURIComponent(keycloakConfig.clientId)}&post_logout_redirect_uri=${encodeURIComponent(appConfig.baseUrl)}`;
        window.location.href = url;
    }

    ngOnDestroy() {

    }

}
