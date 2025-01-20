import {Injectable} from "@angular/core";
import {SwUpdate} from "@angular/service-worker";
import {interval} from "rxjs";
import {MatSnackBar} from "@angular/material/snack-bar";
import {WFSnackbarComponent} from "../components/common/snackbars/wf-snackbar.component";
import {getSnackbarConfig} from "../utils/user-feedback-utils";
import {WF_SNACKBAR_TYPES} from "../utils";

@Injectable({providedIn: "root"})
export class UpdateService {
    constructor(public swUpdate: SwUpdate, public snackbarService: MatSnackBar) {
        console.log("cirras-claims swUpdate enabled:", swUpdate.isEnabled);
        if (swUpdate.isEnabled) {
            swUpdate.checkForUpdate(); //Check on load up, then check every interval
            interval(30 * 1000).subscribe(() => {
                return swUpdate.checkForUpdate();
            });
        }
    }

    public checkForUpdates(): void {
        if (this.swUpdate.isEnabled) {
            // this.swUpdate.versionUpdates.subscribe(event => {
            //     if(event.type === 'VERSION_READY'){
            //         console.log("current version is", event.currentVersion);
            //         console.log("available version is", event.latestVersion);

            //         let snackbarRef = this.snackbarService.openFromComponent(WFSnackbarComponent, getSnackbarConfig("A new version is available", WF_SNACKBAR_TYPES.UPDATE));
            //         snackbarRef.onAction().subscribe(
            //             () => {
            //                 this.swUpdate.activateUpdate().then(() => document.location.reload());
            //             }
            //         );
            //     }

            // });

            this.swUpdate.versionUpdates.subscribe((evt) => {
                switch (evt.type) {
                  case 'VERSION_DETECTED':
                    console.log(`Downloading new app version: ${evt.version.hash}`);
                    break;
                  case 'VERSION_READY':
                    console.log(`Current app version: ${evt.currentVersion.hash}`);
                    console.log(`New app version ready for use: ${evt.latestVersion.hash}`);

                    let snackbarRef = this.snackbarService.openFromComponent(WFSnackbarComponent, getSnackbarConfig("A new version is available", WF_SNACKBAR_TYPES.UPDATE));
                    snackbarRef.onAction().subscribe(
                        () => {
                            this.swUpdate.activateUpdate().then(() => document.location.reload());
                        }
                    );

                    break;
                  case 'VERSION_INSTALLATION_FAILED':
                    console.log(`Failed to install app version '${evt.version.hash}': ${evt.error}`);
                    break;
                }
              });


            
        }
    }
}
