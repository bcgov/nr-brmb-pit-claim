import {ChangeDetectionStrategy, ChangeDetectorRef, Component, HostListener, OnInit} from "@angular/core";
import {UpdateService} from "../../services/update.service";
import {Router} from "@angular/router";
import {Store} from "@ngrx/store";
import {RootState} from "../../store";
import {DomSanitizer} from "@angular/platform-browser";
import {MatIconRegistry} from "@angular/material/icon";
import {ApplicationStateService} from "../../services/application-state.service";
import {addRemoveCdkOverlayClass, ResourcesRoutes} from "../../utils";

import {Subscription} from "rxjs";
import { RouterLink, WfApplicationConfiguration, WfApplicationState, WfDevice } from "@wf1/wfcc-application-ui";
import { ROUTE_SCOPES } from "src/app/utils/scopes";
import { MatDialog } from "@angular/material/dialog";
import { AppConfigService, TokenService } from "@wf1/wfcc-core-lib";
import { SecurityUtilService } from "src/app/services/security-util.service";

const DEVICE: WfDevice = 'desktop';

@Component({
    selector: "cirras-claims-root",
    templateUrl: "./app.component.html",
    styleUrls: ["./app.component.scss"],
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class AppComponent implements OnInit {

    applicationState: WfApplicationState = {
        menu: 'expanded'
    };

    appMenu: RouterLink[] = [];

    applicationConfig: WfApplicationConfiguration = {
        title: "CLAIMS CALCULATOR",
        device: DEVICE,
        userName: "",
        version: {
            long: "",
            short: ""
        },
        environment: ""
    };

    appConfigSubscription: Subscription;
    tokenSubscription: Subscription;

    constructor(private updateService: UpdateService,
        private router: Router, private store: Store<RootState>,
        protected cdr: ChangeDetectorRef,
        protected dialog: MatDialog,
        protected tokenService: TokenService,
        protected securityUtilService: SecurityUtilService,
        protected appConfigService: AppConfigService,
        protected applicationStateService: ApplicationStateService,
        private matIconRegistry: MatIconRegistry,
        private domSanitizer: DomSanitizer) {

        this.updateService.checkForUpdates();

        this.setAppProperties();

        this.matIconRegistry.addSvgIcon(
            "filter-cancel",
            this.domSanitizer.bypassSecurityTrustResourceUrl("assets/icons/filter-cancel.svg")
        );

        this.matIconRegistry.addSvgIcon(
            "calculate",
            this.domSanitizer.bypassSecurityTrustResourceUrl("assets/icons/calculate.svg")
        );
    }

    setAppProperties() {
        let vh = window.innerHeight * 0.01;
        document.documentElement.style.setProperty("--vh", `${vh}px`);

        let vw = window.innerWidth * 0.01;
        document.documentElement.style.setProperty("--vw", `${vw}px`);

        document.documentElement.style.setProperty("--cirras-claims-gutter-space", this.applicationStateService.getIsMobileResolution() ? "8px" : "16px");
        addRemoveCdkOverlayClass(this.applicationStateService.getIsMobileResolution());

        this.initMainNavItems();

    }

    @HostListener("window:orientationchange", ["$event"])
    onOrientationChange(event) {
        setTimeout(() => {
            let vh = window.innerHeight * 0.01;
            document.documentElement.style.setProperty("--vh", `${vh}px`);

            let vw = window.innerWidth * 0.01;
            document.documentElement.style.setProperty("--vw", `${vw}px`);

            addRemoveCdkOverlayClass(this.applicationStateService.getIsMobileResolution());
            document.documentElement.style.setProperty("--cirras-claims-gutter-space", this.applicationStateService.getIsMobileResolution() ? "8px" : "16px");
        }, 250);
    }

    @HostListener("window:resize", ["$event"])
    onResize(event) {
        setTimeout(() => {
            let vh = window.innerHeight * 0.01;
            document.documentElement.style.setProperty("--vh", `${vh}px`);

            let vw = window.innerWidth * 0.01;
            document.documentElement.style.setProperty("--vw", `${vw}px`);

            addRemoveCdkOverlayClass(this.applicationStateService.getIsMobileResolution());
            document.documentElement.style.setProperty("--cirras-claims-gutter-space", this.applicationStateService.getIsMobileResolution() ? "8px" : "16px");

        }, 250);
    }

    ngOnInit(): void {
        this.appConfigSubscription = this.appConfigService.configEmitter.subscribe((config) => {

            this.applicationConfig.version.short = config.application.version.replace(/-SNAPSHOT/i, '');
            this.applicationConfig.version.long = config.application.version.replace(/-SNAPSHOT/i, '');
            this.applicationConfig.environment = config.application.environment.replace(/^.*prod.*$/i, '');

            this.applicationConfig.device =  'desktop'; // this.applicationStateService.getIsMobileResolution() ? 'mobile' : 'desktop';
        });
        this.tokenSubscription = this.tokenService.credentialsEmitter.subscribe( (creds) => {
            let first = creds.given_name || creds.givenName;
            let last = creds.family_name || creds.familyName;

            this.applicationConfig.userName = `${ first } ${ last }`;
            this.initMainNavItems();
        } );

        //window[ 'SPLASH_SCREEN' ].remove();
    }

    ngOnDestroy() {
        if (this.appConfigSubscription) {
            this.appConfigSubscription.unsubscribe();
        }
        if (this.tokenSubscription) {
            this.tokenSubscription.unsubscribe();
        }
    }

    initMainNavItems(): RouterLink[] {
        let items: RouterLink[] = [];

        if (this.securityUtilService.hasScopes(ROUTE_SCOPES.CLAIM)) {
            items.push(new RouterLink("Claims",
                "/" + ResourcesRoutes.CLAIM_LIST,
                "list_alt",
                this.applicationConfig.device == 'desktop' ? null : 'hidden',
                this.router,
                true));
        }
        if (this.securityUtilService.hasScopes(ROUTE_SCOPES.CALCULATION)) {
            items.push(new RouterLink("Calculations",
                "/" + ResourcesRoutes.CALCULATION_LIST,
                "calculate",
                this.applicationConfig.device == 'desktop' ? null : 'hidden',
                this.router,
                true));
        }
    
        this.appMenu = items;
        return items;
    }


}
